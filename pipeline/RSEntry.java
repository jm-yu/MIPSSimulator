package pipeline;

import instruction.Instruction;

public class RSEntry {
	private int RSId;      // RS Entry ID
	private Instruction instruction;   // Instruction Reference
	private int Vj;     // Value of 1st argument
	private int Vk;     // Value of 2nd argument
	private int Qj; // ROBId for ROB entry of dependent instruction for 1st argument, 0 is Vj present
	private int Qk; // ROBID for ROB entry of dependent instruction for 2nd argument, 0 if Vk present;
	private int A;


	public static int DEFAULT_VALUE = Integer.MIN_VALUE;
	public static int DEFAULT_Q = 0;
	public int getRSId() {
			return RSId;
		}
	public Instruction getInstruction() {
			return instruction;
		}

	public int getVj() {
			return Vj;
		}
	public void setVj(int Vj) {
			this.Vj = Vj;
		}

	public int getVk() {
			return Vk;
		}
	public void setVk(int Vk) {
			this.Vk = Vk;
		}

	public int getQj() {
			return Qj;
		}
	public void resetQj() {
			this.Qj = DEFAULT_Q;
		}

	public int getQk() {
			return Qk;
		}
	public void resetQk() {
			this.Qk = DEFAULT_Q;
		}

	public int getAddress() {
			return A;
		}
	public void setAddress(int address) {
			this.A = address;
		}

	public boolean isReady() {
			return Qj == DEFAULT_Q && Qk == DEFAULT_Q;
		}

	public RSEntry(int RSId, Instruction instruction, int Vj, int Vk, int Qj, int Qk,
				 int A){
		
			this.instruction = instruction;
			this.RSId = RSId;
			this.Vj = Vj;
			this.Vk = Vk;
			this.Qj = Qj;
			this.Qk = Qk;
			this.A = A;
		}


	}

	// Reservation Station. It is a collection of upto MaxSize reservation
	// station entries for each execution unit.

