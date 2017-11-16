package instruction;

import commons.BitCal;
import commons.INSTRUCTION_TYPE;

public class JInstruction extends Instruction{
	private int jumpAddress;

	public JInstruction(int lineNo, String bitString, int executionCycle){
		super(lineNo, bitString, executionCycle);
		jumpAddress = BitCal.toint(bitString.substring(6, 32)) * 4;

		setOpCode(OpcodeMap.strToOpCodeMap.get(bitString.substring(0, 6)));

		hasRegisterOutput = false;
	}

	public int execute(int vj, int vk) {
		return jumpAddress;
	}

	public boolean outcome(int vj, int vk) {
		return true;
	}

	public int getDestination() {
		return ((getAddress() & 0xF0000000) + jumpAddress);
	}

	public INSTRUCTION_TYPE getType() {
		return INSTRUCTION_TYPE.JTYPE;
	}

	public int getArg1() {
		assert(false);
		return jumpAddress;
	}

	public int getImmediate() {
		assert(false);
		return jumpAddress;
	}

	public int getArg2() {
		assert(false);
		return jumpAddress;
	}

	public String instructionString() {
		String ss;
		ss = getOpCode().name() + " #" + jumpAddress;
		return ss;
	}
};

/*
 int main() {
 JInstruction instr = JInstruction(600, "00001000000000000000000010011110", -1);
 cout << instr.toString();
 }
 */
