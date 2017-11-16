package pipeline;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import commons.*;
import instruction.Instruction;
import simulator.Executor;

public class ReservationStation {
	private Vector<RSEntry> reservations = new Vector<RSEntry>();
	private int index;
	private CDB cdb;
	private ROB rob;
	private RegisterStatus regStatus;
	private Map<Integer,Integer> SWAddToCount = new HashMap<Integer,Integer>();
	private Registers registers;


	public static int MAX_SIZE = 10;

	public boolean isFull() {
			return (reservations.size() == MAX_SIZE);
		}


	public void flush() {
		reservations.clear();
	}

		

	public Vector<RSEntry> getEntries() {
		return reservations;
	}

	public int size() {
	    return reservations.size();
	}

	    public ReservationStation(CDB cdb, RegisterStatus regStatus,
				Map<Integer,Integer> SWAddToCount, Registers registers, ROB rob){
				
			index = 0;
			this.cdb = cdb;
			this.registers = registers;
			this.regStatus = regStatus;
	        this.rob = rob;
		}
	    public RSEntry add(Instruction instruction) {
	    	assert(reservations.size() < MAX_SIZE);

	    	int Vj = RSEntry.DEFAULT_VALUE, Vk = RSEntry.DEFAULT_VALUE;
	    	int Qj = RSEntry.DEFAULT_Q, Qk = RSEntry.DEFAULT_Q;
	    	int A = 0;
	    	// Code to check registerstatus and update Vj, Vk, Qj, Qk.
	    	// Address field to be populated.
	    	INSTRUCTION_TYPE type = instruction.getType();
	    	INSTRUCTIONS opCode = instruction.getOpCode();

	    	// Update A for LOAD and STORE inst.
	    	if (opCode == INSTRUCTIONS.SW || opCode == INSTRUCTIONS.LW) {
	    		A = instruction.getImmediate();
	    	}

	    	if (type == INSTRUCTION_TYPE.ITYPE) {
	    		// Read register1 value, if in Register Status,
	    		// Update Q else get value from register
	    		int register1 = instruction.getArg1();
	    		if (regStatus.isSet(register1)) {
	    			int ROBId = regStatus.get(register1);
	    			int ROBValue = rob.getValue(ROBId);
	    			int cdbValue = cdb.get(ROBId);
	    			if (ROBValue != ROBSlot.DEFAULT_VALUE) {
	    				Vj = ROBValue;
	    			} else if(cdbValue != CDB.DEFAULT_VALUE) {
	    				Vj = cdbValue;
	    			} else {
	    				Qj = ROBId;
	    			}
	    		} else {
	    			Vj = registers.get(register1);
	    		}


	    		// For following instruction, registerT is used as 2nd argument
	    		if (opCode == INSTRUCTIONS.SW || opCode == INSTRUCTIONS.LW ||
	                    opCode == INSTRUCTIONS.BEQ || opCode == INSTRUCTIONS.BNE) {
	    			int register2 = instruction.getArg2();
	    			if (regStatus.isSet(register2)) {
	    				int ROBId = regStatus.get(register2);
	    				int ROBValue = rob.getValue(ROBId);
	    				int cdbValue = cdb.get(ROBId);
	    				if (ROBValue != ROBSlot.DEFAULT_VALUE) {
	    					Vk = ROBValue;
	    				} else if(cdbValue != CDB.DEFAULT_VALUE) {
	    					Vk = cdbValue;
	    				} else {
	    					Qk = ROBId;
	    				}
	    			} else {
	    				Vk = registers.get(register2);
	    			}
	    		}
	    	} else if (type == INSTRUCTION_TYPE.RTYPE) {
	    		// BREAK and NOP shouldn't hit RS
	    		assert(opCode != INSTRUCTIONS.NOP && opCode != INSTRUCTIONS.BREAK);

	    		// Update Arg1 and Arg2 to Vj/Qj and Vk/Qk
	    		int register1 = instruction.getArg1();
	    		if (regStatus.isSet(register1)) {
	    			int ROBId = regStatus.get(register1);
	    			int ROBValue = rob.getValue(ROBId);
	    			int cdbValue = cdb.get(ROBId);
	    			if (ROBValue != ROBSlot.DEFAULT_VALUE) {
	    				Vj = ROBValue;
	    			} else if(cdbValue != CDB.DEFAULT_VALUE) {
	    				Vj = cdbValue;
	    			} else {
	    				Qj = ROBId;
	    			}
	    		} else {
	    			Vj = registers.get(register1);
	    		}

	    		int register2 = instruction.getArg2();
	    		if (regStatus.isSet(register2)) {
	    			int ROBId = regStatus.get(register2);
	    			int ROBValue = rob.getValue(ROBId);
	    			int cdbValue = cdb.get(ROBId);
	    			if (ROBValue != ROBSlot.DEFAULT_VALUE) {
	    				Vk = ROBValue;
	    			} else if(cdbValue != CDB.DEFAULT_VALUE) {
	    				Vk = cdbValue;
	    			} else {
	    				Qk = ROBId;
	    			}
	    		} else {
	    			Vk = registers.get(register2);
	    		}
	    	}   // J doesn't do any lookup.

	    	// create RS entry
	    	// index is unique for each entry.
	    	RSEntry entry = new RSEntry(++index, instruction, Vj, Vk, Qj, Qk, A);

	    	// add to reservations.
	    	reservations.addElement(entry);

	    	return entry;
	    }

	    public void remove(int RSId) {
	    	int index = -1;

	    	// search index corresponding to RSId
	    	for (int i = 0; i<reservations.size(); i++) {
	    		if (reservations.elementAt(i).getRSId() == RSId) {
	    			index = i;
	    			break;
	    		}
	    	}

	    	// Should be present
	    	//assert(index > -1);

	    	// Get the entry
	    	//RSEntry entry = reservations.elementAt(index);

	    	// Erase entry
	    	reservations.remove(index);

	    	// Free memory
	    	
	    }

	    public void updateFromCDB() {
	    	// Iterate through RSs.
	    	for (Iterator<RSEntry> it = reservations.iterator();it.hasNext();) {
	    		RSEntry entry = it.next();
	            // Ignore inst to be executed in future cycles.
	    		Instruction instruction = entry.getInstruction();
	    		int currentExecutionCycle = Executor.getExecutionCycle();
	    		if (instruction.getExecutionCycle() > currentExecutionCycle) {
	    			continue;
	    		}

	    		// Check Qj and Qk for each one of them
	    		// If not zero, check CDB
	    		// If value found, update corresponding V entry
	    		// Set Q entry to 0.
	    		int Qj = entry.getQj();
	    		if (Qj != RSEntry.DEFAULT_VALUE) {
	    			int value = cdb.get(Qj);
	                int valueFromROB = rob.getValue(Qj);
	    			if (value != CDB.DEFAULT_VALUE) {
	    				entry.setVj(value);
	    				entry.resetQj();
	                } else if (valueFromROB != ROBSlot.DEFAULT_VALUE) {
	                    entry.setVj(valueFromROB);
	    				entry.resetQj();
	    		    }
	    		}

	    		int Qk = entry.getQk();
	    		if (Qk != RSEntry.DEFAULT_VALUE) {
	    			int value = cdb.get(Qk);
	                int valueFromROB = rob.getValue(Qk);
	    			if (value != CDB.DEFAULT_VALUE) {
	    				entry.setVk(value);
	    				entry.resetQk();
	                } else if (valueFromROB != ROBSlot.DEFAULT_VALUE) {
	                    entry.setVk(valueFromROB);
	    				entry.resetQk();
	    			}
	    		}

	    	}
	    }


	    public String resStationDump() {
	        String ss;
	        ss  = "RS:";
	    	for (Iterator<RSEntry> it = reservations.iterator();it.hasNext();){
	    		RSEntry entry = it.next();
	            Instruction inst = entry.getInstruction();

	            ss = ss+ "\r\n" + "[" + inst.instructionString() + "]";
	        }
	        return ss;
	    }

	}



