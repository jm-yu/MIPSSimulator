MIPS simulator for Computer Architecture class.
---developed a disassembler and a cycle-by-cycle MIPS simulator in Java
——-implemented advanced pipeline using Tomasulo algorithm with out-of-order execution and in-order commit along with a Branch Predictor using Branch Target Buffer

Usage:
Use “java MIPSSim input_file output_file number1:number2” to get the output file.
input_file is input file address
output_file is output file address
number1 is start cycle
number2 is end cycle
when number1:number2 is 0:0 , write only the last state of the simulator to the file.


