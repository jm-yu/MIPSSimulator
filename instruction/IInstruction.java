package instruction;

import commons.BitCal;
import commons.INSTRUCTIONS;
import commons.INSTRUCTION_TYPE;

public class IInstruction extends Instruction{
	
	private int registerS;
	private int registerT;
	private int immediate;

		boolean outcomeBEQ(int vj, int vk) {
			return (vj == vk);
		}

		boolean outcomeBNE(int vj, int vk) {
			return (vj != vk);
		}

		boolean outcomeBGEZ(int vj, int vk) {
			return (vj >= 0);
		}

		boolean outcomeBGTZ(int vj, int vk) {
			return (vj > 0);
		}

		boolean outcomeBLEZ(int vj, int vk) {
			return (vj <= 0);
		}

		boolean outcomeBLTZ(int vj, int vk) {
			return (vj < 0);
		}

		int executeADDI(int vj, int vk) {
			return immediate + vj;
		}

		int executeADDIU(int vj, int vk) {
			return immediate + vj;
		}

		int executeSLTI(int vj, int vk) {
			return (vj < immediate)?1:0;
		}

		int executeLW(int vj, int vk) {
			return vj + immediate;
		}

		int executeSW(int vj, int vk) {
			return vj + immediate;
		}

	public IInstruction(int lineNo, String bitString, int executionCycle){
			super(lineNo,bitString,executionCycle);
			OpcodeMap.buildMap();

			registerS = BitCal.toint(bitString.substring(6, 11));
			registerT = BitCal.toint(bitString.substring(11, 16));
			immediate = BitCal.fromTwosComplement(BitCal.toint(bitString.substring(16, 32)),16);
			setOpCode(OpcodeMap.strToOpCodeMap.get(bitString.substring(0, 6)));

			INSTRUCTIONS opcode = getOpCode();
			if (opcode == INSTRUCTIONS.BEQ || opcode == INSTRUCTIONS.BNE || opcode == INSTRUCTIONS.BGTZ ||
	                opcode == INSTRUCTIONS.BLEZ || opcode == INSTRUCTIONS.BLTZ || opcode == INSTRUCTIONS.BGEZ) {
				isBranch = true;
			}

			if (opcode == INSTRUCTIONS.SW || opcode == INSTRUCTIONS.LW) {
				setExecuteCyclesLeft(2);
			}

	        if (isBranch || opcode == INSTRUCTIONS.SW) {
	            hasRegisterOutput = false;
	        }

		}

		public int execute(int vj, int vk) {
			switch (getOpCode()) {
			case ADDI:
				return executeADDI(vj, vk);
			case ADDIU:
				return executeADDIU(vj, vk);
			case SLTI:
				return executeSLTI(vj, vk);
			case SW:
				return executeSW(vj, vk);
			case LW:
				return executeLW(vj, vk);
			default:
				break;
			}
			return 0;
		}

		public boolean outcome(int vj, int vk) {
			switch (getOpCode()) {
			case BEQ:
				return outcomeBEQ(vj, vk);
			case BNE:
				return outcomeBNE(vj, vk);
			case BGTZ:
				return outcomeBGTZ(vj, vk);
			case BLEZ:
				return outcomeBLEZ(vj, vk);
			case BLTZ:
				return outcomeBLTZ(vj, vk);
			case BGEZ:
				return outcomeBGEZ(vj, vk);
			default:
				break;
			}
			return true;
		}

		public int getDestination() {
			return (isBranch ? (getAddress() + 4 + immediate * 4) : registerT);
		}

		public INSTRUCTION_TYPE getType() {
			return INSTRUCTION_TYPE.ITYPE;
		}

		public int getArg1() {
			return registerS;
		}

		public int getImmediate() {
			return immediate;
		}

		public int getArg2() {
			return registerT;
		}
		

		public String instructionString() {
			String ss = "";
			//System.out.println("2");
			ss = getOpCode().toString() + " ";
			switch (getOpCode()) {
			case LW:
			case SW:
				ss = ss + "R" + registerT + ", " + immediate + "(" + "R"
						+ registerS + ")";
				break;
			case BEQ:
			case BNE:
				ss = ss + "R" + registerS + ", " + "R" + registerT + ", #"
						+ immediate * 4;
				break;
			case BGEZ:
			case BGTZ:
			case BLEZ:
			case BLTZ:
				ss =ss + "R" + registerS + ", #" + immediate * 4;
				break;
			case ADDI:
			case ADDIU:
			case SLTI:
				ss = ss + "R" + registerT + ", " + "R" + registerS + ", #"
						+ immediate;
				break;
			default:
				ss = "Not a Immediate type opcode";

			};
			return ss;
		}
}
	
