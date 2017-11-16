package simulator;
import commons.*;
import instruction.RawInstruction;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Vector;

public class Simulator{
	int basic_PC = 600;
	private String logFileName;
	public Simulator(String logFilename){
		this.logFileName = logFilename;
	}
	public void addToMemory(Data data){
		memory.addElement(data);
	}
	public void writeToMemory(int Address, Data data){
		int index  = (Address - basic_PC)/4;
		//if(memory.size() > index)
		memory.setElementAt(data, index);
	}
	public Data getMemoryData(int Address){
		int index  = (Address - basic_PC)/4;
		return memory.elementAt(index);
	}
	public RawInstruction peekInstQueue() {
        return instructionQueue.getFirst();
    }
	public int getIndexFromAddress( int address) {
			return (address - basic_PC) / 4;
		}


	public void run(){
		;
	}
	protected void instructionQueueFlush() {
	        instructionQueue.clear();
	    }

	protected Deque<RawInstruction> instructionQueue = new ArrayDeque();
	protected Vector<Data> memory = new Vector<Data>();
	protected String getLogFileName(){
		return logFileName;
	}
	
}
