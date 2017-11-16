package instruction;

public class InstructionBuilder {
	public static Instruction build(int lineNo, String bitString,int executionCycle) {
			String opcode = bitString.substring(0, 6);
			if (opcode.equals("000000")) {
				return new RInstruction(lineNo, bitString, executionCycle);
			} 
			else if ((opcode.equals("000010"))||(opcode.equals("000011"))){
				return new JInstruction(lineNo, bitString, executionCycle);
			} 
			else{
				return new IInstruction(lineNo, bitString, executionCycle);
			}
		}
	public static Instruction build(int lineNo, String bitString) {
			return build(lineNo, bitString, -1);
		}
}
