package instruction;

import java.util.HashMap;
import java.util.Map;
import commons.INSTRUCTIONS;

public class OpcodeMap {
	public static Map<String, INSTRUCTIONS> strToOpCodeMap = new HashMap<String,INSTRUCTIONS>();
	public static Map<String, INSTRUCTIONS> functionToOpCodeMap = new HashMap<String, INSTRUCTIONS>();
	public static Map<String, INSTRUCTIONS> buildMap(){
		strToOpCodeMap.put("101011", INSTRUCTIONS.SW);
		strToOpCodeMap.put("100011", INSTRUCTIONS.LW);
		strToOpCodeMap.put("000100", INSTRUCTIONS.BEQ);
		strToOpCodeMap.put("000101", INSTRUCTIONS.BNE);
		strToOpCodeMap.put("000001", INSTRUCTIONS.BGEZ);
		strToOpCodeMap.put("000111", INSTRUCTIONS.BGTZ);
		strToOpCodeMap.put("000110", INSTRUCTIONS.BLEZ);
		strToOpCodeMap.put("000001", INSTRUCTIONS.BLTZ);
		strToOpCodeMap.put("001000", INSTRUCTIONS.ADDI);
		strToOpCodeMap.put("001001", INSTRUCTIONS.ADDIU);
		strToOpCodeMap.put("001010", INSTRUCTIONS.SLTI);
		strToOpCodeMap.put("000000", INSTRUCTIONS.SLTU);
		strToOpCodeMap.put("000000", INSTRUCTIONS.SLL);
		strToOpCodeMap.put("000000", INSTRUCTIONS.SRL);
		strToOpCodeMap.put("000000", INSTRUCTIONS.SRA);
		strToOpCodeMap.put("000000", INSTRUCTIONS.SUB);
		strToOpCodeMap.put("000000", INSTRUCTIONS.SUBU);
		strToOpCodeMap.put("000000", INSTRUCTIONS.ADD);
		strToOpCodeMap.put("000000", INSTRUCTIONS.ADDU);
		strToOpCodeMap.put("000000", INSTRUCTIONS.AND);
		strToOpCodeMap.put("000000", INSTRUCTIONS.OR);
		strToOpCodeMap.put("000000", INSTRUCTIONS.XOR);
		strToOpCodeMap.put("000000", INSTRUCTIONS.NOR);
		strToOpCodeMap.put("000000", INSTRUCTIONS.NOP);
		strToOpCodeMap.put("000000", INSTRUCTIONS.SLT);
		strToOpCodeMap.put("000010", INSTRUCTIONS.J);
		strToOpCodeMap.put("000000", INSTRUCTIONS.BREAK);

		return strToOpCodeMap;
	}
	
	public static Map<String, INSTRUCTIONS> buildFunctionMap(){
		functionToOpCodeMap.put("101011", INSTRUCTIONS.SLTU);
		functionToOpCodeMap.put("000000", INSTRUCTIONS.SLL);
		functionToOpCodeMap.put("000010", INSTRUCTIONS.SRL);
		functionToOpCodeMap.put("000011", INSTRUCTIONS.SRA);
		functionToOpCodeMap.put("100010", INSTRUCTIONS.SUB);
		functionToOpCodeMap.put("100011", INSTRUCTIONS.SUBU);
		functionToOpCodeMap.put("100000", INSTRUCTIONS.ADD);
		functionToOpCodeMap.put("100001", INSTRUCTIONS.ADDU);
		functionToOpCodeMap.put("100100", INSTRUCTIONS.AND);
		functionToOpCodeMap.put("100101", INSTRUCTIONS.OR);
		functionToOpCodeMap.put("100110", INSTRUCTIONS.XOR);
		functionToOpCodeMap.put("100111", INSTRUCTIONS.NOR);
		functionToOpCodeMap.put("101010", INSTRUCTIONS.SLT);
		functionToOpCodeMap.put("001101", INSTRUCTIONS.BREAK);

		return functionToOpCodeMap;

	}


}
