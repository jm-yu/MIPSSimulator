package pipeline;
import instruction.*;
public class ROBSlot {
	private int index;
	private boolean ready;
	private Instruction instruction;
	private int destination;
	int value;
	    static int DEFAULT_VALUE = Integer.MIN_VALUE;
	    public int getIndex() {
			return index;
		}

	    public boolean isReady() {
			return ready;
		}
		public void makeReady() {
			ready = true;
		}

		public Instruction getInstruction() {
			return instruction;
		}

		public int getDestination() {
			return destination;
		}

		public void setDestination(int destination) {
			this.destination = destination;
		}

		public int getValue() {
			return value;
		}
		public void setValue(int value) {
			this.value = value;
		}

		ROBSlot(int index, Instruction instruction){
			this.index = index;
			ready = false;
			this.instruction = instruction;
			destination = instruction.getDestination();
			value = DEFAULT_VALUE;
		}
	}

	