package pipeline;

public class Registers {
	private int[] reg = new int[32];
    public Registers() {
            for (int i = 0; i < 32; i++) {
                reg[i] = 0;
            }
        }
        public int get(int regNo) {
            assert (regNo >= 0 && regNo < 32);
            return reg[regNo];
        }

        public void set(int regNo, int value) {
            assert (regNo >= 0 && regNo < 32);
            reg[regNo] = value;
        }

        public String registersDump() {
           String ss;
           ss = "Registers:";
           for (int i = 0; i < 32; i++) {
                // Add "RXX:" for modulo 8 registers
                if (i % 8 == 0) {
                    ss = ss+ "\r\n"+ "R";
                    // Extra 0 padding
                    if (i < 10) {
                        ss = ss+ "0";
                    }
                    ss =ss+ i + ":";
                }
                ss =ss+ "\t" + reg[i];
            }
            return ss;
        }
}


