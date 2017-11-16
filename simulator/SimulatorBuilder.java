package simulator;

import java.util.HashMap;
import java.util.Map;

import pipeline.*;

public class SimulatorBuilder {
	public static Simulator build(String outFile, int startCycle, int endCycle) {
			
		

	            CDB cdb = new CDB();
	            BranchTargetBuffer btb = new BranchTargetBuffer();
	            RegisterStatus regStatus = new RegisterStatus();
	            ROB rob = new ROB(cdb, regStatus);
	            Map<Integer, Integer> tempMap = new HashMap<Integer,Integer>();
	            Registers registers = new Registers();
	            ReservationStation resStation = new ReservationStation(cdb, regStatus, tempMap, registers, rob);

	            return new Executor(outFile, cdb, btb, regStatus, registers, rob, resStation, startCycle, endCycle);
			//cerr << "Unknown Operation: " << operation;
		}
	}

