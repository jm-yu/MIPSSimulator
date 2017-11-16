package instruction;
import commons.*;
import pipeline.*;
public class Instruction {
	private INSTRUCTIONS opcode;
	private int address;
	private String bitString;
	ROBSlot robSlot;
	private int RSId;
	public Instruction(int address, String bitString) {
			this.address = address;
			this.bitString = bitString;
			executionCycle = -1;
			isBranch = false;
			robSlot = null;
			RSId = -1;
			hasRegisterOutput = true;
			executeCyclesLeft = 1;
	        branchOutCome = false;
		}

	public Instruction(int address, String bitString, int executionCycle) {
			this.address = address;
			this.bitString = bitString;
			this.executionCycle = executionCycle;
			isBranch = false;
			robSlot = null;
			RSId = -1;
			hasRegisterOutput = true;
	        flush = false;
			executeCyclesLeft = 1;
	        branchOutCome = false;
		}

		public String instructionString() {
			return null;
		}

		public INSTRUCTIONS getOpCode() {
			return opcode;
		}

		public String getBitString() {
			return bitString;
		}

		public int getAddress() {
			return address;
		}

		public ROBSlot getROBSlot() {
			return robSlot;
		}
		public void setROBSlot(ROBSlot robSlot) {
			this.robSlot = robSlot;
		}

		public int getRSId() {
			return RSId;
		}
		public void setRSId(int RSId) {
			this.RSId = RSId;
		}

		public int getExecutionCycle() {
			return executionCycle;
		}
		public void setExecutionCycle(int executionCycle) {
			this.executionCycle = executionCycle;
		}

		public boolean isBranchInst() {
			return isBranch;
		}

		public boolean writesToRegister() {
			return hasRegisterOutput;
		}

		public int getExecuteCyclesLeft() {
			return executeCyclesLeft;
		}

		public void setExecuteCyclesLeft(int executeCyclesLeft) {
			this.executeCyclesLeft = executeCyclesLeft;
		}

		public void decrementExecuteCyclesLeft() {
			executeCyclesLeft--;
		}


		public boolean getOutCome() {
	        return branchOutCome;
	    }

		public void setOutCome(boolean branchOutCome) {
	        this.branchOutCome = branchOutCome;
	    }

		public boolean shallFlush() {
	        return flush;
	    }

		public void setFlush() {
	        flush = true;
	    }

		public int getArg1() {
			return 0;
		}
		public int getArg2() {
			return 0;
		}
		public int getImmediate() {
			return 0;
		}

		public INSTRUCTION_TYPE getType(){
			return null;
		}

		public int getDestination() {
			return 0;
		}

		public int execute(int vj, int vk) {
			return 0;
		}

		public boolean outcome(int vj, int vk) {
			return false;
		}

		public String toString() {
			String ss = "";
			ss = ss + bitString.substring(0, 6) + " " + bitString.substring(6, 11) + " ";
			ss = ss + bitString.substring(11, 16) + " " + bitString.substring(16, 21) + " ";
			ss = ss + bitString.substring(21, 26) + " " + bitString.substring(26, 32);

			ss = ss + " " + address;
			ss = ss + " " + instructionString();
			return ss;
		}

	protected void setOpCode(INSTRUCTIONS opcode) {
			this.opcode = opcode;
		}
	protected boolean isBranch;
	protected boolean hasRegisterOutput;
	protected int executionCycle;
	protected int executeCyclesLeft;
	protected boolean branchOutCome;
	protected boolean flush;
	}

		
