package commons;

public class Data {
	private String bitString;
	int value;
	int Address;
	public Data(int Address, String dataString){
		this.Address = Address;
		this.bitString = dataString;
		value = BitCal.fromTwosComplement(BitCal.toint(dataString), 32);
		//System.out.println("1");

	}
	public Data(int Address, int value) {
		this.Address = Address;
		this.value = value;
		bitString = Integer.toString(BitCal.toTwosComplement(value, 32),2);
	}
	public String toString() {
		String ss;
		ss = bitString + " " + Address + " " + value;
		return ss;
	}


	public int getAddress(){
		return Address;
	}
	
	public int getValue() {
		return value;
	}

	public String getBitString(){
		return bitString;
	}
}
