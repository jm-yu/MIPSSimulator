package pipeline;

public class BTBEntry {
	public static  int NOT_SET = -1;
	public static  int TAKEN = 1;
	public static  int NOT_TAKEN = 0;
	public int predictedPC;    // predicted program counter
	public int outcome;    // outcome == 1 => taken, outcome == 0 => not taken,
	                    // outcome == -1 => not set

	public BTBEntry( int predictedPC, int outcome) {
			this.predictedPC = predictedPC;
			this.outcome = outcome;
		}

}
