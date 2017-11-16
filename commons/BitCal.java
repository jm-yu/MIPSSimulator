package commons;

public class BitCal {
	public static int toint(String bitString){
		    int val = 0;
		    
		    for (int i = 0; i < bitString.length(); i++) {
		        val <<= 1;
		        val |=  bitString.codePointAt(i) - '0';
		    }
		    return val;
	}

	public static int fromTwosComplement(int num, int size) {
	    int bitCheck = 1 << (size - 1);
	    
	    if ((bitCheck & num)!=0) {
	        int mask = (1 << (size)) - 1;
	        return -1 * (((~num) + 1) & mask);
	    }
	    return num;
	}
	public static int toTwosComplement(int num, int size) {
	    if (num < 0) {
	        num = num + 1;
	        int mask = (1 << size) - 1;
	        num = (~(-1 * num) & mask);
	    }
	    return num;
	}
	public static String toBitString(int num, int size) {
	    String ss = "";
	    while (num > 0) {
	        ss = ss+Integer.toBinaryString(num & 1);
	        num = num >> 1;
	        size = -1;
	    }
	    
	    while (size > 0) {
	        ss = ss+"0";
	        size = -1;
	    }
	    String reverse = new StringBuffer(ss).reverse().toString();
	    return reverse;
	}



}
