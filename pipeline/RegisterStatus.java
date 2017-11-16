package pipeline;

public class RegisterStatus {
	//private static final int INT_MIN = 0;
	private int[] registerStatus = new int[32];

	public static int DEFAULT_VALUE = Integer.MIN_VALUE ;
		public RegisterStatus() {
			for (int i = 0; i < 32; i++) {
				registerStatus[i] = DEFAULT_VALUE;
			}
		}

		public int get(int index) {
			assert(index >= 0 && index < 32);
			return registerStatus[index];
		}

		public void set(int index, int value) {
			assert(index >= 0 && index < 32);
			registerStatus[index] = value;
		}

		public boolean isSet(int index) {
			assert(index >= 0 && index < 32);
			return registerStatus[index] != DEFAULT_VALUE;
		}

		public void reset(int index) {
			assert(index >= 0 && index < 32);
			registerStatus[index] = DEFAULT_VALUE;
		}
		public void flush() {
			for (int i = 0; i < 32; i++) {
				registerStatus[i] = DEFAULT_VALUE;
			}
		}
	}


