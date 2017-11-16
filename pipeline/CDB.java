package pipeline;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import instruction.OpcodeMap;
import simulator.Executor;

public class CDB {

		


	// value returned if result not in CDB
	public static int DEFAULT_VALUE = Integer.MIN_VALUE ;
	private Map<Integer, CDBEntry> ROBIdToValueMap = new HashMap<Integer,CDBEntry>();  // map for ROB id to value
	public int get(int ROBId) {
		if (!ROBIdToValueMap.containsKey(ROBId)) {
	        return DEFAULT_VALUE;
	    }
	    CDBEntry entry = ROBIdToValueMap.get(ROBId);
	    //System.out.println(ROBIdToValueMap.keySet());
	    
	    if (entry.executionCycle == Executor.getExecutionCycle()) {
	        return entry.value;
	    }
	    return DEFAULT_VALUE;
	}

	public void set(int ROBId, int value) {
	    CDBEntry entry = new CDBEntry();
	    entry.value = value;
	    entry.executionCycle = Executor.getExecutionCycle() + 1;
		ROBIdToValueMap.put(ROBId, entry);
	}

	public void clear(int ROBId) {
		//assert(ROBIdToValueMap.count(ROBId) > 0);
		ROBIdToValueMap.remove(ROBId);
	}

	public void flush() {
	    for(Iterator <Entry<Integer, CDBEntry>> it = ROBIdToValueMap.entrySet().iterator();it.hasNext();) {
	        CDBEntry entry = it.next().getValue();
	        if (Executor.getExecutionCycle() == entry.executionCycle)  {
	            ROBIdToValueMap.remove(it);
	        } else {
	           ;
	        }
	    }
	}

	public boolean isPopulated(int ROBId) {
		return ROBIdToValueMap.containsKey(ROBId);
	}

	}



