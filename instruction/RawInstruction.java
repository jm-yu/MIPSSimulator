package instruction;

import commons.Data;

public class RawInstruction {
	private int address;
	private String bitString;
	private int decodeCycle;
	private boolean outcome;

	public int getAddress() {
			return address;
		}
	public String getBitString() {
			return bitString;
		}
	public int getDecodeCycle() {
			return decodeCycle;
		}

	public void setOutCome(boolean outcome) {
	        this.outcome = outcome;
	    }

	public boolean getOutCome() {
	        return outcome;
	    }

	public RawInstruction(Data data, int decodeCycle) {
	        outcome = false;
			address = data.getAddress();
			bitString = data.getBitString();
			this.decodeCycle = decodeCycle;
		}

}
