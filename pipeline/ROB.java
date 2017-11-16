package pipeline;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

import instruction.Instruction;
import simulator.Executor;

public class ROB {

	private Deque<ROBSlot> robQueue= new ArrayDeque<ROBSlot>();
	private int index;  // index starts at 1.
	private CDB cdb;
	private RegisterStatus registerStatus;

	public static int MAXSIZE = 6;
	public boolean isFull() {
		return robQueue.size() == MAXSIZE;
	}
	public ROBSlot queueInstruction(Instruction instruction){
		//assert(robQueue.size() < MAXSIZE);

		// Create the entry in ROB
		ROBSlot robSlot = new ROBSlot(++index, instruction);
		robQueue.addLast(robSlot);
		//System.out.println(robSlot.getInstruction());
		//System.out.println("3");

		return robSlot;
	}
	public ROBSlot dequeueInstruction(){
		if (robQueue.size() == 0) {
			return null;
		}
		ROBSlot entry = robQueue.getFirst();
		robQueue.removeFirst();
		return entry;
	}

	public boolean isEmpty() {
		return robQueue.size() == 0;
	}

	public ROBSlot peekTop() {
		return robQueue.getFirst();
	}

	public void flush() {
		robQueue.clear();
	}

	public ROB(CDB cdb, RegisterStatus registerStatus){
		this.cdb = cdb;
		this.registerStatus= registerStatus; 
		index = 0;
	}

	public int getValue(int ROBId){
	    int value = ROBSlot.DEFAULT_VALUE;
	    for (Iterator<ROBSlot> it = robQueue.iterator() ; it.hasNext();) {
	        ROBSlot slot = it.next();
	        if (slot.isReady() && slot.getIndex() == ROBId) {
	            value = slot.getValue();
	            break;
	        }
	    }

	    return value;
	}


	public void updateFromCDB() {
	for (Iterator<ROBSlot> it = robQueue.iterator(); it.hasNext();){
		ROBSlot slot = it.next();
		int ROBId = slot.getIndex();
		Instruction inst = slot.getInstruction();
		int executionCycle = Executor.getExecutionCycle();

	// If scheduled for future cycles, ignore.
	if (inst.getExecutionCycle() > executionCycle) {
		continue;
	}

    // if already ready, just continue
	if (slot.isReady()) {
		continue;
	}

	// If ROBId in cdb.
	// Update value and make it ready for commit
	// Set the instruction execution cycle to next
	// cycle to ensure commit in next cycle or later.
	int value = cdb.get(ROBId);
	if (value != CDB.DEFAULT_VALUE) {
		slot.setValue(value);
		inst.setExecutionCycle(executionCycle + 1);
		slot.makeReady();
	}
}
}


	public String robDump(){
	    String ss;
	    ss = "ROB:";
		for (Iterator<ROBSlot> it = robQueue.iterator(); it.hasNext();){
			ROBSlot slot = it.next();
			Instruction inst = slot.getInstruction();
	        ss = ss + "\r\n" + "[" + inst.instructionString() +"]";
		}
	    return ss;
	}


}
