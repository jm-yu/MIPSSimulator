package pipeline;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class BranchTargetBuffer {
	public static int BASE_PC = 600;
	private int tick;
	private Map<Integer, BTBEntry> buffer= new LinkedHashMap<Integer, BTBEntry>();     // pc to BTBEntry
	private Map<Integer, Integer > lastedTickedAt = new LinkedHashMap<Integer,Integer>(); // pc to last tick. Used to morph LRU behavior
	private static int MAXSIZE = 16;	// maximum size of BTB
	private int size;	//Current size of BTB
	public BranchTargetBuffer() {
			tick = 0;
			size = 0;
		}

	

	

	public int getNextPC(int PC, int nextPC) {	
		//assert(PC >= BASE_PC);

		tick++; // Update the tick to keep track of current cycle

		if (buffer.containsKey(PC)) {
			BTBEntry btbEntry = buffer.get(PC);

			if (btbEntry.outcome == BTBEntry.TAKEN) {
				nextPC = btbEntry.predictedPC;
	            //cout << "Predicted next PC: " <<  nextPC << endl;
			} else {
				// if not taken last time, increment 4
				nextPC = PC + 4;
			}

			// Update the tick value to keep LRU behavior
			//assert(lastedTickedAt.count(PC) > 0);
			lastedTickedAt.put(PC,tick);

		} else {

			
			nextPC = PC + 4;
	    }
		return nextPC;
	}

	public void updateOrAdd( int PC,  int nextPC, int outcome) {
		assert(PC >= BASE_PC);
	    //assert(buffer.count(PC) > 0);
	    //assert(lastedTickedAt.count(PC) > 0);

		if(buffer.containsKey(PC)){
	    BTBEntry btbEntry = buffer.get(PC);
	    btbEntry.outcome = outcome;
		}
		else{
	    BTBEntry btbEntry = new BTBEntry(nextPC, outcome);
        //cout << "BTB: " << PC << "Outcome: " << btbEntry->outcome << " NextPC: " << btbEntry->predictedPC << endl;
		// If BTB is full then delete the LRU entry
		if (size == MAXSIZE) {
			int minKey = 0;
			int min = 0;
			for (Iterator<Entry<Integer, Integer>> it = lastedTickedAt.entrySet().iterator();it.hasNext();){
				Entry<Integer, Integer> entry = it.next();
				if (min == 0) {
					min = entry.getValue();
					minKey = entry.getKey();
				} else if (entry.getValue()< min) {
					min = entry.getValue();
					minKey = entry.getKey();
				}
			}

			// Erase the corresponding entry from buffer
			//assert(buffer.count(minKey) > 0);
			buffer.remove(minKey);

			//Erase the entry from lastedTicketedAt
			lastedTickedAt.remove(minKey);
		} else {
			size++;
		}

		buffer.put(PC,btbEntry);
		lastedTickedAt.put(PC, tick);
	}
	}


	public String btbDump() {
	    String ss;
	    ss = "BTB:";
	    int i = 1;
	    for (Iterator<Entry<Integer, BTBEntry>> it = buffer.entrySet().iterator(); it.hasNext();) {
	        ss = ss +"\r\n" + "[" + "Entry " + i++ + "]";
	        Entry<Integer, BTBEntry> entry1 = it.next();
	        BTBEntry entry = entry1.getValue();
	        // TODO::What is not set funda
	        ss  = ss + "<" + entry1.getKey() + "," + entry.predictedPC +",";
	        if (entry.outcome == BTBEntry.NOT_SET)  {
	        	ss = ss+ "NotSet";
	        } else {
	        	ss = ss+entry.outcome;
	        }
	        ss = ss+">";

	    }
	    return ss;
	}


}
