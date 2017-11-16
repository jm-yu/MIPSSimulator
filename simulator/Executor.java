package simulator;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import commons.*;
import instruction.*;
import pipeline.*;

public class Executor extends Simulator {
	private BranchTargetBuffer btb;
	private CDB cdb;
	private RegisterStatus regStatus;
	private Registers registers;
	private ROB rob;
	private ReservationStation resStation;
	private int nextPC;

		// SW address to count mapping.
	Map <Integer, Integer> SWAddToCount = new HashMap<Integer,Integer>();

	private static int executionCycle;

	private boolean done;
	
	private File logFile;

	private int codeSegmentEnd;

	private int startCycle;

	private int endCycle;

	public void instFetchStage() {
		// Read memory for nextPC.
		// Add instruction to instructionQueue

	    if (nextPC > codeSegmentEnd) {
	        return;
	    }
		Data data = getMemoryData(nextPC);
	    
	    Instruction inst = InstructionBuilder.build(nextPC, data.getBitString());
	    if (inst.getOpCode() == INSTRUCTIONS.BREAK) {
	        codeSegmentEnd = nextPC;
	    }
	    RawInstruction rawInst = new RawInstruction(data, getExecutionCycle() + 1);
		instructionQueue.addLast(rawInst);
		////cout << "	Pushed Instruction at address: " << data.getAddress()+"\r\n");
		// Check BTB for next address
		// update nextPC
	    if (inst.getOpCode() == INSTRUCTIONS.J || inst.isBranchInst()) {
	        nextPC = btb.getNextPC(nextPC, inst.getDestination());
	        rawInst.setOutCome(nextPC == inst.getDestination());
	        //cout = "Branch Fetch Outcome: " << rawInst.getOutCome() << endl;
	    } else {
	        nextPC = nextPC + 4;
	    }
	}

	public void decodeStage() {
		// Read instructionQueue. If empty do nothing
		if (instructionQueue.size() == 0) {
			return;
		}

		// If front most inst scheduled for future cycle, then do nothing
		RawInstruction rawInst = instructionQueue.getFirst();
		if (rawInst.getDecodeCycle() > executionCycle) {
			return;
		}


		// Else decode Instruction using instructionBuilder.
		Instruction instruction = InstructionBuilder.build(rawInst.getAddress(),
				rawInst.getBitString(), executionCycle + 1);
		instruction.setOutCome(rawInst.getOutCome());
		////cout << "	Decode Instruction: " << instruction.instructionString()+"\r\n");

		INSTRUCTIONS opCode = instruction.getOpCode();

		// Check ROB and RS. If not empty, do nothing
		// Extra slot in ROB for saving address of the load and store instruction??
		if (!rob.isFull() && (opCode == INSTRUCTIONS.NOP || opCode == INSTRUCTIONS.BREAK)) {

			// IN NOP or BREAK and slot available in ROB, simply add
			ROBSlot slotEntry = rob.queueInstruction(instruction);
			instruction.setROBSlot(slotEntry);
	        slotEntry.makeReady();
			instructionQueue.removeFirst();

		} else if ((!rob.isFull()) && (!resStation.isFull())) {

			// Else ensure space in both RS and ROB
			//System.out.println(instruction.getBitString());
			ROBSlot slotEntry = rob.queueInstruction(instruction);
			instruction.setROBSlot(slotEntry);

			// Add entry to RS. Update arguments from register or ROB. (Qj, Qk,Vj, Vk)
			RSEntry rsEntry = resStation.add(instruction);
			instruction.setRSId(rsEntry.getRSId());

			// If inst writes to register, update ROBId in registerStatus
			if (instruction.writesToRegister()) {
				regStatus.set(instruction.getDestination(),
						slotEntry.getIndex());
			}
			instructionQueue.removeFirst();

		}
	}

	public void executeStage() {
		int aluUsed = 0;

		Vector<RSEntry> reservations = resStation.getEntries();

		// Calculate memory address of LW\SW which is ahead in the queue
		RSEntry minLoadStoreEntry = null;

		for (Iterator<RSEntry> it = reservations.iterator(); it.hasNext();) {
			RSEntry rsEntry = it.next();
			Instruction instruction = rsEntry.getInstruction();

	        // If scheduled for future cycles, just ignore
	        if (instruction.getExecutionCycle() > executionCycle) {
	            continue;
	        }

			INSTRUCTIONS opcode = instruction.getOpCode();
	         int executionCyclesLeft =
	                instruction.getExecuteCyclesLeft();

	        // If rsEntry is not ready, cannot schedule.
	        if (executionCyclesLeft == 0) {
	            continue;
	        }

	        if (opcode == INSTRUCTIONS.SW || opcode == INSTRUCTIONS.LW) {

	            //Memory address will be calculated in the first cycle
	            // Enforcing in order address computation.
	            // While iterating RS, find out one with min cycles,
	            // later execute the address calc for the first one.
	        	//System.out.println(minLoadStoreEntry.getAddress());
	            if (executionCyclesLeft == 2 &&     // if not started executing and
	                 rsEntry.getQj() == RSEntry.DEFAULT_Q &&
	                    (minLoadStoreEntry==null ||      // if first LW or SW, then its min
	                        // else if scheduled before, then that is min.
	                        minLoadStoreEntry.getInstruction().getExecutionCycle()
	                        < instruction.getExecutionCycle())) {

	                    minLoadStoreEntry = rsEntry;

	            // If one cycle left, do memory access and
	            // write to cdb if LW or
	            // mark ready in ROB if SW and register to be written is ready.
	            } else if (executionCyclesLeft == 1) {
	                if (opcode == INSTRUCTIONS.SW) {
	                    // TODO::Verify that we have to wait for both Qj and Qk to be
	                    // zero for SW instruction.
	                    // If so, this check not needed
	                    if (rsEntry.getQk() == RSEntry.DEFAULT_Q) {
	                        instruction.decrementExecuteCyclesLeft();
	                        instruction.getROBSlot().makeReady();
	                        instruction.setExecutionCycle(executionCycle + 1);
	                        instruction.getROBSlot().setValue(rsEntry.getVk());
	                        instruction.getROBSlot().setDestination(rsEntry.getAddress());
	                        ////cout << "	Executing Final write of SW Inst: " << instruction.instructionString()+"\r\n");
	                    }
	                } else {
	                    boolean earlyStorePresent = false;
	                    for (Iterator<RSEntry> itTemp = reservations.iterator(); itTemp.hasNext(); ) {
	                        RSEntry rsEntryTemp = itTemp.next();
	                        Instruction instructionTemp =
	                        		rsEntryTemp.getInstruction();
	                        INSTRUCTIONS opcodeTemp = instructionTemp.getOpCode();
	                        // If this RS inst is SW and is scheduled before.
	                        // say early store present.
	                        // TODO:: Not very correct I guess.
	                        // Should be ROB that decides
	                        if (opcodeTemp == INSTRUCTIONS.SW
	                                && instructionTemp.getExecutionCycle()
	                                        < instruction.getExecutionCycle()
	                                && rsEntryTemp.getAddress()
	                                        == rsEntry.getAddress()) {
	                            earlyStorePresent = true;
	                            break;
	                        }
	                    }

	                    if (!earlyStorePresent) {
	                    	//System.out.println(rsEntry.getAddress());
	                        cdb.set(instruction.getROBSlot().getIndex(),
	                                getMemoryData(rsEntry.getAddress()).getValue());
	                        instruction.setExecutionCycle(executionCycle + 1);
	                        ////cout << "	Executing LW Inst final phase: " << instruction.instructionString()+"\r\n");
	                        instruction.decrementExecuteCyclesLeft();
	                    }
	                }
	            }
	        } else if (rsEntry.isReady()) {
	            if (instruction.isBranchInst()||opcode==INSTRUCTIONS.J) {
	                boolean outcome = instruction.outcome(rsEntry.getVj(),
	                        rsEntry.getVk());

	               // //cout << "	Executing Branch Or Jump Inst: " << instruction.instructionString()+"\r\n");
	                 int newAddress = instruction.getDestination();

	                //Update BTB
	                if (instruction.isBranchInst())
	                	btb.updateOrAdd(instruction.getAddress(), instruction.getDestination(),(outcome ? BTBEntry.TAKEN : BTBEntry.NOT_TAKEN));
	                

	                //Update ROB with the destination and branch outcome
	                instruction.getROBSlot().setDestination(newAddress);

	                // Update instruction about the outcome.
	                ////cout << instruction.getOutCome() << endl;
	                if(outcome != instruction.getOutCome()) {
	                    instruction.setFlush();
	                }

	                instruction.setOutCome(outcome);
	                //Mark the instruction ready
	                instruction.getROBSlot().makeReady();
	                instruction.setExecutionCycle(executionCycle + 1);
	                instruction.decrementExecuteCyclesLeft();
	                aluUsed++;
	              //  if (outcome) {
	              //      nextPC = newAddress;
	              //  }
	            } else {
	                //Write the result to CDB
	                ////cout << "	Executing ALU Inst: " << instruction.instructionString()+"\r\n");
	                cdb.set(instruction.getROBSlot().getIndex(),
	                        instruction.execute(rsEntry.getVj(),
	                                rsEntry.getVk()));
	                instruction.setExecutionCycle(executionCycle + 1);
	                aluUsed++;
	                instruction.decrementExecuteCyclesLeft();
	            }
	        }   // if (rsEntry.ready())
		}       // for loop

		// Address should be calculated for The LW\SW entry at the head of the queue.
		if (minLoadStoreEntry!=null) {

			minLoadStoreEntry.setAddress(
					minLoadStoreEntry.getInstruction().execute(
							minLoadStoreEntry.getVj(),
							minLoadStoreEntry.getVk()));

			minLoadStoreEntry.getInstruction().decrementExecuteCyclesLeft();
			//cout << "	Executing Add translation of LW or SW Inst: " << minLoadStoreEntry.getInstruction().instructionString()+"\r\n");


	        Instruction minLWSWInst = minLoadStoreEntry.getInstruction();
	        if (minLWSWInst.getOpCode() == INSTRUCTIONS.SW &&
	                minLoadStoreEntry.getQk() == RSEntry.DEFAULT_Q) {

	                //cout << "	Vk available, executing Final write of SW Inst: " << minLWSWInst.instructionString()+"\r\n");
	                minLWSWInst.decrementExecuteCyclesLeft();
	                minLWSWInst.getROBSlot().makeReady();
	                minLWSWInst.setExecutionCycle(executionCycle + 1);
	                minLWSWInst.getROBSlot().setValue(minLoadStoreEntry.getVk());
	                minLWSWInst.getROBSlot().setDestination(minLoadStoreEntry.getAddress());
	        }
		}

	}

	void writeResultStage() {
	    // When result available in CDB
	    //      update ROB
	    //      make ROB instruction ready to commit in next cycle.
		rob.updateFromCDB();

	    //  update RS.
		resStation.updateFromCDB();

	    // Flush CDB at the end of this cycle
		cdb.flush();
	}

	void commitStage() {
	    // If rob empty, nothing to be done
		if (rob.isEmpty()) {
			
			return;
		}
		//System.out.println("1");
		ROBSlot slot = rob.peekTop();
		Instruction inst = slot.getInstruction();
		
		
	    // Check if commit is scheduled in this cycle
	    // If not scheduled in this cycle or not ready, do nothing
		if (!slot.isReady() || inst.getExecutionCycle() > executionCycle) {
			return;
		}
		if(inst.getOpCode()==INSTRUCTIONS.J)
			btb.updateOrAdd(inst.getAddress(), inst.getDestination(), BTBEntry.TAKEN);

		
	    // Commit is in order. One instruction from top of the queue.
		rob.dequeueInstruction();

		int destination = slot.getDestination();
		INSTRUCTIONS opCode = inst.getOpCode();
		
	    if (opCode == INSTRUCTIONS.BREAK) {
	       // If BREAK, this is the last cycle.
	       //cout << " 	Commiting BREAK " << inst.instructionString() << endl;
	       done = true;
	       return;
	    // Branching:
	    } else if (opCode == INSTRUCTIONS.J || inst.isBranchInst()) {
			// check the ROB's next instruction's address
			// is same as destination
			//cout << " 	Commiting J OR BRANCH " << inst.instructionString() << endl;

			//  if not same,
	    	
			if (inst.shallFlush()) {
				// then flush the whole system
				// (ROB, RS, IQ, Register Status)
				//cout << " 	BRANCH not taken" << endl;
				flush();
				nextPC = slot.getDestination();
				return;
			}

		} else if (opCode == INSTRUCTIONS.SW) {
			// update destination memory with value
			writeToMemory(destination, new Data(destination, slot.getValue()));
			//cout << " 	Commiting SW " << inst.instructionString() << "   " << destination << "==>" << getMemoryData(destination).getValue()<< endl;

	    } else if (opCode != INSTRUCTIONS.NOP) {
	    	//cout << " 	Commiting ALU and LW inst " << inst.instructionString() << endl;
	        // For ALU  and load instruction,
	        // register is updated with the result
	        registers.set(destination, slot.getValue());
	        regStatus.reset(destination);
	    }

	    // finally vacate the reservation station.
		int RSId = inst.getRSId();
	    if (RSId != -1) {
	        resStation.remove(RSId);
	    }
	}

	public void run() {

	   
		while (!done) {
			logFile = new File(this.getLogFileName());
		    PrintWriter output = null;
		    try {
				output = new PrintWriter(new FileWriter(logFile,true));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			executionCycle++;

			//cout << "Execution Cycle: " << executionCycle+"\r\n");
			instFetchStage();
			decodeStage();
			executeStage();
			writeResultStage();
			commitStage();
			if (startCycle == 0 && endCycle == 0){
				;
			}
			else if ((startCycle == -1 && endCycle == -1)
	                || (executionCycle >= startCycle
	                && executionCycle <= endCycle)) {

	            output.append("Cycle <" + executionCycle + ">:"+"\r\n"); 
	            output.close();
	            dumpLog();
			logFile = new File(this.getLogFileName());
		    PrintWriter output2 = null;
		    try {
				output2 = new PrintWriter(new FileWriter(logFile,true));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    if (!done){
		    	output2.append("\r\n");
		    	output2.close();
		    }
			}
		}
		if(done)
			if (startCycle == 0 && endCycle == 0){
				logFile = new File(this.getLogFileName());
			    PrintWriter output = null;
			    try {
					output = new PrintWriter(new FileWriter(logFile,true));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			    output.append("Cycle <" + executionCycle + ">:"+"\r\n"); 
	            output.close();
	            dumpLog();

			
		}
	}
    
			

	public void flush() {
		
		rob.flush();
		resStation.flush();
		regStatus.flush();
		cdb.flush();
	    this.instructionQueueFlush();
	}


	


	public String instructionQueueDump() {
	   	String ss;
	    ss = "IQ:\r\n";
	    for (Iterator<RawInstruction>it = instructionQueue.iterator();it.hasNext();){
	        ss =ss +"[";
	        RawInstruction raw = it.next();
	        Instruction inst = InstructionBuilder.build(raw.getAddress(),
	                raw.getBitString());
	        ss =ss+ inst.instructionString();
	        ss =ss+ "]\r\n";
	        if (inst.getOpCode() != INSTRUCTIONS.NOP) {
	            //ss =ss;
	        }
	    }
	    return ss;
	}

	public String memoryDump() {
	   int startAddress = 716;
	   String ss;
	   ss = "Data Segment:"+"\r\n";
	   ss = ss+ startAddress + ":";
	   for (int index = getIndexFromAddress(startAddress);
	               index < memory.size(); index++) {
	        ss = ss+ "\t" + memory.elementAt(index).getValue();
	   }
	    return ss;
	}


	public void dumpLog() {
		logFile = new File(this.getLogFileName());
		PrintWriter output = null;
		try {
			output = new PrintWriter(new FileWriter(logFile,true));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    output.append(instructionQueueDump());
	    output.append(resStation.resStationDump()+"\r\n");
	    output.append(rob.robDump()+"\r\n");
	    output.append(btb.btbDump()+"\r\n");
	    output.append(registers.registersDump()+"\r\n");
	    output.append(memoryDump());
	    output.close();
	}


	public int getNextPC() {
	        return nextPC;
	    }

	    public void setNextPC(int nextPC) {
	        this.nextPC = nextPC;
	    }
		public static int getExecutionCycle() {
			return executionCycle;
		}

		public static void setExecutionCycle(int cycle) {
	        executionCycle = cycle;
	    }

		public Executor(String logFileName, CDB cdb, BranchTargetBuffer btb, RegisterStatus regStatus,
	            Registers registers,  ROB rob, ReservationStation resStation, int startCycle, int endCycle){
			super(logFileName);
			nextPC = 600;
	        done = false;
	        codeSegmentEnd = 712;
			this.btb = btb;
			this.cdb = cdb;
			this.regStatus = regStatus;
	        this.registers = registers;
			this.rob = rob;
			this.resStation = resStation;
	        this.startCycle = startCycle;
	        this.endCycle = endCycle;

		}

	}


