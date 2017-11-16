package instruction;

import commons.BitCal;
import commons.INSTRUCTIONS;
import commons.INSTRUCTION_TYPE;

public class RInstruction extends Instruction{
	private int registerS;
	private int registerT;
	private int registerD;
	private int shiftAmount;

	public int executeSLTU(int vj, int vk) {
			return (vj < vk)?1:0;
		}

	public int executeSLL(int vj, int vk) {
			return (vj + shiftAmount);
		}

	public int executeSRL(int vj, int vk) {
			return (vj >> shiftAmount);
		}

	public int executeSRA(int vj, int vk) {
			return (vj + shiftAmount);
		}

	public int executeSUB(int vj, int vk) {
			return vj - vk;
		}

	public int executeSUBU(int vj, int vk) {
			return vj - vk;
		}

	public int executeADD(int vj, int vk) {
			return vj + vk;
		}

	public int executeADDU(int vj, int vk) {
			return vj + vk;
		}

	public int executeAND(int vj, int vk) {
			return vj & vk;
		}

	public int executeOR(int vj, int vk) {
			return vj | vk;
		}

	public int executeXOR(int vj, int vk) {
			return vj ^ vk;
		}

	public int executeNOR(int vj, int vk) {
			return ~(vj | vk);
		}

	public int executeSLT(int vj, int vk) {
			return (vj < vk)?1:0;
		}

	public RInstruction(int lineNo, String bitString, int executionCycle){
			super(lineNo, bitString, executionCycle);
			OpcodeMap.buildFunctionMap();

			registerS = BitCal.toint(bitString.substring(6, 11));
			registerT = BitCal.toint(bitString.substring(11, 16));
			registerD = BitCal.toint(bitString.substring(16, 21));
			shiftAmount = BitCal.toint(bitString.substring(21, 26));

			if (BitCal.toint(getBitString()) == 0) {
				setOpCode(INSTRUCTIONS.NOP);
			} else {
				setOpCode(OpcodeMap.functionToOpCodeMap.get(bitString.substring(26, 32)));
				
			}

	        if (getOpCode() == INSTRUCTIONS.NOP || getOpCode() == INSTRUCTIONS.BREAK) {
	            hasRegisterOutput = false;
	        }
		}

	public int execute(int vj, int vk) {
			switch (getOpCode()) {
			case SLTU:
				return executeSLTU(vj, vk);
				
			case SLL:
				return executeSLL(vj, vk);
				
			case SRL:
				return executeSRL(vj, vk);
				
			case SRA:
				return executeSRA(vj, vk);
				
			case SUB:
				return executeSUB(vj, vk);
				
			case SUBU:
				return executeSUBU(vj, vk);
				
			case ADD:
				return executeADD(vj, vk);
				
			case ADDU:
				return executeADDU(vj, vk);
				
			case AND:
				return executeAND(vj, vk);
				
			case OR:
				return executeOR(vj, vk);
				
			case XOR:
				return executeXOR(vj, vk);
				
			case NOR:
				return executeNOR(vj, vk);
				
			case SLT:
				return executeSLT(vj, vk);
				
			default:
				
			}
			return 0;
		}

	public boolean outcome(int vj, int vk) {
			return false;
		}

	public int getDestination() {
			return registerD;
		}

	public INSTRUCTION_TYPE getType() {
			return INSTRUCTION_TYPE.RTYPE;
		}

	public int getArg1() {
			return registerS;
		}

	public int getImmediate() {
			assert(false);
			return registerD;
		}

	public int getArg2() {
			return registerT;
		}

		public String instructionString() {
			if (getOpCode() == INSTRUCTIONS.NOP || getOpCode() == INSTRUCTIONS.BREAK) {
				return getOpCode().name();
			}

			String ss;
			ss = getOpCode().toString()+ " " + "R" + registerD + ", ";
			if (getOpCode() == INSTRUCTIONS.SLL || getOpCode() == INSTRUCTIONS.SRL || getOpCode() == INSTRUCTIONS.SRA) {
				ss = ss+"R" + registerT + ", ";
				ss = ss+"#" + shiftAmount;
			} else {
				ss = ss+"R" + registerS + ", ";
				ss = ss+"R" + registerT;
			}
			return ss;
		}
	};

	/*
	 int main() {
	 RInstruction instr1 = RInstruction(600, "00000000000000000000000000000000", -1);
	 cout + instr1.toString();
	 cout + "\r\n";
	 RInstruction instr2 = RInstruction(604, "00000000000000000000000000001101", -1);
	 cout + instr2.toString();
	 cout + "\r\n";
	 RInstruction instr3 = RInstruction(608, "00000000001000100010100000100000", -1);
	 cout + instr3.toString();
	 cout + "\r\n";
	 }
	 */
	


