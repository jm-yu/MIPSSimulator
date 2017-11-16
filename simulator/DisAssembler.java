package simulator;
import commons.*;
import instruction.Instruction;
import instruction.InstructionBuilder;

import java.lang.reflect.*;
import java.util.Iterator;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;


public class DisAssembler extends Simulator {

	public DisAssembler(String logFilename) {
		super(logFilename);
		// TODO Auto-generated constructor stub
	}
	public void run(){
		File logFile = new File(this.getLogFileName());
		PrintWriter output = null;
		try {
			output = new PrintWriter(logFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		boolean isBreak = false;
		for (Iterator<Data> it = memory.iterator();it.hasNext();){
			Data data = it.next();
		if (!isBreak) {
			Instruction instP = InstructionBuilder.build(
					data.getAddress(), data.getBitString());
			output.write(instP.toString() +"\n");
			isBreak = instP.getOpCode() == INSTRUCTIONS.BREAK;
		} else {
			output.write(it.next().toString() +"\n");
		}
	}
}

}
