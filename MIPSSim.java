import simulator.*;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.Vector;

import commons.*;

public class MIPSSim{
public static int flag = 0;
public static Vector<String> instruction_stream = new Vector<String>();
public static Vector<String> translation_result = new Vector<String>();
public static String Start_Address = "600";
	public static void main(String[] args) {

		// TODO Auto-generated method stub
		String input_file_address = args[0];
		//String input_file_address = "fibonacci_bin.bin";
		String output_file_address = args[1];
		//String output_file_address = "out.txt";
		//String s = "0:100";
		//String[] Str = s.split(":");
		String[] Str = args[2].split(":");
		int start_cycle = Integer.valueOf(Str[0]);
		int end_cycle = Integer.valueOf(Str[1]);
		
		
		File input_file = new File(input_file_address);
		
	
		InputStream input = null;
		int totalBytesRead = 0;
		try {
			input = new BufferedInputStream(new FileInputStream(input_file));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		byte[] result = new byte[(int) (input_file.length())];
		while(totalBytesRead < result.length){
			int bytesRemaining = result.length - totalBytesRead;
			int bytesRead = 0;
			try {
				bytesRead = input.read(result, totalBytesRead,  bytesRemaining);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (bytesRead > 0){
				totalBytesRead = totalBytesRead+ bytesRead;
			}
		}
		try {
			input.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(int i = 0; i < result.length/4; i++){
			String s1 = String.format("%8s", Integer.toBinaryString(result[4*i+0] & 0xFF)).replace(' ','0');
			String s2 = String.format("%8s", Integer.toBinaryString(result[4*i+1] & 0xFF)).replace(' ','0');
			String s3 = String.format("%8s", Integer.toBinaryString(result[4*i+2] & 0xFF)).replace(' ','0');
			String s4 = String.format("%8s", Integer.toBinaryString(result[4*i+3] & 0xFF)).replace(' ','0');
			String str_line = s1+s2+s3+s4;
			instruction_stream.addElement(str_line);
			BigInteger s1_ = new BigInteger(s1,2);
			BigInteger s2_ = new BigInteger(s2,2);
			BigInteger s3_ = new BigInteger(s3,2);
			BigInteger s4_ = new BigInteger(s4,2);
			int judge = s1_.intValue() + s2_.intValue() + s3_.intValue() + s4_.intValue();
			if (judge != 0)
				flag = i;
			
		}
		//sim_function(output_file_address);
		//System.out.println("be happy");
		//dis_function();
	
	
		int PC = 600;
		File output_file = new File(output_file_address);
		if (!output_file.exists())
			try {
				output_file.createNewFile();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		File logFile = new File(output_file_address);
		PrintWriter output = null;
		try {
			output = new PrintWriter(logFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		output.write("");
		output.close();
		
		
		Simulator simulator = SimulatorBuilder.build(output_file_address,start_cycle,end_cycle);
		for(int i = 0; i < instruction_stream.size(); i++){
			simulator.addToMemory(new Data(PC,instruction_stream.get(i)));
			PC = PC+4;
		}
		simulator.run();
		
	}
}
	/*public static void dis_function(){
		//String output_file_address = args[1];
				String output_file_address = "fibonacci_out.txt";
				File output_file = new File(output_file_address);
				if (!output_file.exists())
					try {
						output_file.createNewFile();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				OutputStream output = null;
				
				try {
					output = new BufferedOutputStream(new FileOutputStream(output_file));
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
		 		for(int i = 0; i < instruction_stream.size(); i++){
					
					translate(instruction_stream.get(i));
					}
				// combine the instruction stream and the result together.
				for (int i = 0; i < flag+1; i++){
					int current_address_int = Integer.valueOf(Start_Address) + i*4;
					String Address = Integer.toString(current_address_int);
					String str_line = instruction_stream.get(i).substring(0,6)+" "+instruction_stream.get(i).substring(6,11)+" "
					+instruction_stream.get(i).substring(11,16)+" "+instruction_stream.get(i).substring(16,21)+" "
					+instruction_stream.get(i).substring(21,26)+" "+instruction_stream.get(i).substring(26,32)+" "+Address+" "+translation_result.get(i)+"\r\n";
					//System.out.println(str_line);
					byte bytes[] = str_line.getBytes();
			        try {
						output.write(bytes);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					}
				for (int i = flag+1; i < instruction_stream.size(); i++){
					int current_address_int = Integer.valueOf(Start_Address) + i*4;
					String Address = Integer.toString(current_address_int);
					String str_line = instruction_stream.get(i)+" "+Address+" "+translation_result.get(i)+"\r\n";
					byte bytes[] = str_line.getBytes();
			        try {
						output.write(bytes);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					}
				

				try {
					output.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
	public static void translate(String str){
		String str1_o,str2_o,str3_o,str4_o,str_o = null;
		String str1 = str.substring(0,6);
		String str2 = str.substring(6,11);
		String str3 = str.substring(11,16);
		String str4 = str.substring(16,21);
		String str5 = str.substring(21,26);
		String str6 = str.substring(26,32);
		
		



		if (str1.equals("001000")){//1
			str1_o = "ADDI";
			BigInteger num1 = new BigInteger(str2,2);
			str2_o = num1.toString();
			BigInteger num2 = new BigInteger(str3,2);
			str3_o = num2.toString();
			BigInteger num3 = new BigInteger(str4+str5+str6,2);
			int num3_int = num3.intValue();
			String str4_1 = str4.substring(0,1);
			if (str4_1.equals("1")){
				num3_int = num3.intValue() - 65536;
			}
			str4_o = Integer.toString(num3_int);
			str_o = str1_o+" R"+str3_o+", R"+str2_o+", #"+str4_o;
			}
		else if (str1.equals("101011")){//2
			str1_o = "SW";
			BigInteger num1 = new BigInteger(str2,2);
			str2_o = num1.toString();
			BigInteger num2 = new BigInteger(str3,2);
			str3_o = num2.toString();
			BigInteger num3 = new BigInteger(str4+str5+str6,2);
			int num3_int = num3.intValue();
			String str4_1 = str4.substring(0,1);
			if (str4_1.equals("1")){
				num3_int = num3.intValue() - 65536;
			}
			str4_o = Integer.toString(num3_int);
			str_o = str1_o+" R"+str3_o+", "+str4_o+"(R"+str2_o+")";
			}
		else if (str1.equals("100011")){//3
			str1_o = "LW";
			BigInteger num1 = new BigInteger(str2,2);
			str2_o = num1.toString();
			BigInteger num2 = new BigInteger(str3,2);
			str3_o = num2.toString();
			BigInteger num3 = new BigInteger(str4+str5+str6,2);
			int num3_int = num3.intValue();
			String str4_1 = str4.substring(0,1);
			if (str4_1.equals("1")){
				num3_int = num3.intValue() - 65536;
			}
			str4_o = Integer.toString(num3_int);
			str_o = str1_o+" R"+str3_o+", "+str4_o+"(R"+str2_o+")";
			}
		else if (str1.equals("001001")){//4
			str1_o = "ADDIU";
			BigInteger num1 = new BigInteger(str2,2);
			str2_o = num1.toString();
			BigInteger num2 = new BigInteger(str3,2);
			str3_o = num2.toString();
			BigInteger num3 = new BigInteger(str4+str5+str6,2);
			int num3_int = num3.intValue();
			String str4_1 = str4.substring(0,1);
			if (str4_1.equals("1")){
				num3_int = num3.intValue() - 65536;
			}
			str4_o = Integer.toString(num3_int);
			str_o = str1_o+" R"+str3_o+", R"+str2_o+", #"+str4_o;
			}
		else if (str1.equals("000010")){//5
			str1_o = "J";
			BigInteger num1 = new BigInteger(str2+str3+str4+str5+str6+"00",2);
			str2_o = num1.toString();
			str_o = str1_o + " #"+str2_o;
		}
		else if (str1.equals("000000")){//special
			if (str6.equals("001101")){//6
				str_o = "BREAK";
			}
			else if (str6.equals("101010")&&(str5.equals("00000"))){//7
				str1_o = "SLT";
				BigInteger num1 = new BigInteger(str2,2);
				str2_o = num1.toString();
				BigInteger num2 = new BigInteger(str3,2);
				str3_o = num2.toString();
				BigInteger num3 = new BigInteger(str4,2);
				str4_o = num3.toString();
				str_o = str1_o+" R"+str4_o+", R"+str2_o+", R"+str3_o;
			}
			else if (str6.equals("000000")&&(str2.equals("00000"))&&(!str5.equals("00000"))){//8
				str1_o = "SLL";
				BigInteger num1 = new BigInteger(str2,2);
				str2_o = num1.toString();
				BigInteger num2 = new BigInteger(str3,2);
				str3_o = num2.toString();
				BigInteger num3 = new BigInteger(str4,2);
				str4_o = num3.toString();
				str_o = str1_o+" R"+str4_o+", R"+str2_o+", #"+str3_o;
			}
			else if (str6.equals("000010")&&(str2.equals("00000"))){//9
				str1_o = "SRL";
				BigInteger num1 = new BigInteger(str3,2);
				str2_o = num1.toString();
				BigInteger num2 = new BigInteger(str4,2);
				str3_o = num2.toString();
				BigInteger num3 = new BigInteger(str5,2);
				str4_o = num3.toString();
				str_o = str1_o+" R"+str3_o+", R"+str2_o+", "+str4_o;
			}
			else if (str6.equals("101011")&&(str5.equals("00000"))){//10
				str1_o = "SLTU";
				BigInteger num1 = new BigInteger(str2,2);
				str2_o = num1.toString();
				BigInteger num2 = new BigInteger(str3,2);
				str3_o = num2.toString();
				BigInteger num3 = new BigInteger(str4,2);
				str4_o = num3.toString();
				str_o = str1_o+" R"+str4_o+", R"+str2_o+", R"+str3_o;
			}
			else if (str6.equals("000011")&&(str2.equals("00000"))){//11
				str1_o = "SRA";
				BigInteger num1 = new BigInteger(str3,2);
				str2_o = num1.toString();
				BigInteger num2 = new BigInteger(str4,2);
				str3_o = num2.toString();
				BigInteger num3 = new BigInteger(str5,2);
				str4_o = num3.toString();
				str_o = str1_o+" R"+str3_o+", R"+str2_o+", "+str4_o;
			}
			else if (str6.equals("100000")&&(str5.equals("00000"))){//12
				str1_o = "ADD";
				BigInteger num1 = new BigInteger(str2,2);
				str2_o = num1.toString();
				BigInteger num2 = new BigInteger(str3,2);
				str3_o = num2.toString();
				BigInteger num3 = new BigInteger(str4,2);
				str4_o = num3.toString();
				str_o = str1_o+" R"+str4_o+", R"+str2_o+", R"+str3_o;
			}
			else if (str6.equals("100001")&&(str5.equals("00000"))){//13
				str1_o = "ADDU";
				BigInteger num1 = new BigInteger(str2,2);
				str2_o = num1.toString();
				BigInteger num2 = new BigInteger(str3,2);
				str3_o = num2.toString();
				BigInteger num3 = new BigInteger(str4,2);
				str4_o = num3.toString();
				str_o = str1_o+" R"+str4_o+", R"+str2_o+", R"+str3_o;
			}
			else if (str6.equals("100010")&&(str5.equals("00000"))){//14
				str1_o = "SUB";
				BigInteger num1 = new BigInteger(str2,2);
				str2_o = num1.toString();
				BigInteger num2 = new BigInteger(str3,2);
				str3_o = num2.toString();
				BigInteger num3 = new BigInteger(str4,2);
				str4_o = num3.toString();
				str_o = str1_o+" R"+str4_o+", R"+str2_o+", R"+str3_o;
			}
			else if (str6.equals("100011")&&(str5.equals("00000"))){//15
				str1_o = "SUBU";
				BigInteger num1 = new BigInteger(str2,2);
				str2_o = num1.toString();
				BigInteger num2 = new BigInteger(str3,2);
				str3_o = num2.toString();
				BigInteger num3 = new BigInteger(str4,2);
				str4_o = num3.toString();
				str_o = str1_o+" R"+str4_o+", R"+str2_o+", R"+str3_o;
			}
			else if (str6.equals("100100")&&(str5.equals("00000"))){//16
				str1_o = "AND";
				BigInteger num1 = new BigInteger(str2,2);
				str2_o = num1.toString();
				BigInteger num2 = new BigInteger(str3,2);
				str3_o = num2.toString();
				BigInteger num3 = new BigInteger(str4,2);
				str4_o = num3.toString();
				str_o = str1_o+" R"+str4_o+", R"+str2_o+", R"+str3_o;
			}
			else if (str6.equals("100101")&&(str5.equals("00000"))){//17
				str1_o = "OR";
				BigInteger num1 = new BigInteger(str2,2);
				str2_o = num1.toString();
				BigInteger num2 = new BigInteger(str3,2);
				str3_o = num2.toString();
				BigInteger num3 = new BigInteger(str4,2);
				str4_o = num3.toString();
				str_o = str1_o+" R"+str4_o+", R"+str2_o+", R"+str3_o;
			}
			else if (str6.equals("100110")&&(str5.equals("00000"))){//18
				str1_o = "XOR";
				BigInteger num1 = new BigInteger(str2,2);
				str2_o = num1.toString();
				BigInteger num2 = new BigInteger(str3,2);
				str3_o = num2.toString();
				BigInteger num3 = new BigInteger(str4,2);
				str4_o = num3.toString();
				str_o = str1_o+" R"+str4_o+", R"+str2_o+", R"+str3_o;
			}
			else if (str6.equals("100111")&&(str5.equals("00000"))){//19
				str1_o = "NOR";
				BigInteger num1 = new BigInteger(str2,2);
				str2_o = num1.toString();
				BigInteger num2 = new BigInteger(str3,2);
				str3_o = num2.toString();
				BigInteger num3 = new BigInteger(str4,2);
				str4_o = num3.toString();
				str_o = str1_o+" R"+str4_o+", R"+str2_o+", R"+str3_o;
			}
			else if(str.equals("00000000000000000000000000000000"))
				str_o = "NOP";
			else 
				str_o = "instruction not included";
		}
		else if (str1.equals("001010")){//20
			str1_o = "SLTI";
			BigInteger num1 = new BigInteger(str2,2);
			str2_o = num1.toString();
			BigInteger num2 = new BigInteger(str3,2);
			str3_o = num2.toString();
			BigInteger num3 = new BigInteger(str4+str5+str6,2);
			int num3_int = num3.intValue();
			String str4_1 = str4.substring(0,1);
			if (str4_1.equals("1")){
				num3_int = num3.intValue() - 65536;
			}
			str4_o = Integer.toString(num3_int);
			str_o = str1_o+" R"+str3_o+", R"+str2_o+", #"+str4_o;
			}
		else if (str1.equals("000101")){//21
			str1_o = "BNE";
			BigInteger num1 = new BigInteger(str2,2);
			str2_o = num1.toString();
			BigInteger num2 = new BigInteger(str3,2);
			str3_o = num2.toString();
			BigInteger num3 = new BigInteger(str4+str5+str6+"00",2);
			str4_o = num3.toString();
			str_o = str1_o+" R"+str2_o+", R"+str3_o+", #"+str4_o;
			}
		else if (str1.equals("000100")){//22
			str1_o = "BEQ";
			BigInteger num1 = new BigInteger(str2,2);
			str2_o = num1.toString();
			BigInteger num2 = new BigInteger(str3,2);
			str3_o = num2.toString();
			BigInteger num3 = new BigInteger(str4+str5+str6+"00",2);
			int num3_int = num3.intValue();
			String str4_1 = str4.substring(0,1);
			if (str4_1.equals("1")){
				num3_int = num3.intValue() - 65536*4;
			}
			str4_o = Integer.toString(num3_int);
			str_o = str1_o+" R"+str2_o+", R"+str3_o+", #"+str4_o;
			}
		else if (str1.equals("000001")&&str3.equals("00001")){//23
			str1_o = "BGEZ";
			BigInteger num1 = new BigInteger(str2,2);
			str2_o = num1.toString();		
			BigInteger num3 = new BigInteger(str4+str5+str6+"00",2);
			int num3_int = num3.intValue();
			String str4_1 = str4.substring(0,1);
			if (str4_1.equals("1")){
				num3_int = num3.intValue() - 65536*4;
			}
			str4_o = Integer.toString(num3_int);
			str_o = str1_o+" R"+str2_o+", #"+str4_o;
			}
		else if (str1.equals("000111")&&str3.equals("00000")){//24
			str1_o = "BGTZ";
			BigInteger num1 = new BigInteger(str2,2);
			str2_o = num1.toString();		
			BigInteger num3 = new BigInteger(str4+str5+str6+"00",2);
			int num3_int = num3.intValue();
			String str4_1 = str4.substring(0,1);
			if (str4_1.equals("1")){
				num3_int = num3.intValue() - 65536*4;
			}
			str4_o = Integer.toString(num3_int);
			str_o = str1_o+" R"+str2_o+", #"+str4_o;
			}
		else if (str1.equals("000110")&&str3.equals("00000")){//25
			str1_o = "BLEZ";
			BigInteger num1 = new BigInteger(str2,2);
			str2_o = num1.toString();		
			BigInteger num3 = new BigInteger(str4+str5+str6+"00",2);
			int num3_int = num3.intValue();
			String str4_1 = str4.substring(0,1);
			if (str4_1.equals("1")){
				num3_int = num3.intValue() - 65536*4;
			}
			str4_o = Integer.toString(num3_int);
			str_o = str1_o+" R"+str2_o+", #"+str4_o;
			}
		else if (str1.equals("000001")&&str3.equals("00000")){//26
			str1_o = "BLTZ";
			BigInteger num1 = new BigInteger(str2,2);
			str2_o = num1.toString();		
			BigInteger num3 = new BigInteger(str4+str5+str6+"00",2);
			int num3_int = num3.intValue();
			String str4_1 = str4.substring(0,1);
			if (str4_1.equals("1")){
				num3_int = num3.intValue() - 65536*4;
			}
			str4_o = Integer.toString(num3_int);
			str_o = str1_o+" R"+str2_o+", #"+str4_o;
			}
		
		else 
			str_o ="instruction not included";
	    if (str_o.equals("NOP")&&translation_result.size()>flag)
	    	str_o ="0";
		translation_result.addElement(str_o);	
		//System.out.println(str_o);
	}*/


	

