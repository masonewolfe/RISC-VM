public class Computer
{
	private int loadPos=0;
	private Longword op1, op2, result;
	private Longword currentInstruction;
	private Longword stackPoint;
	private Longword pc;
	private Bit halted;
	private Memory mem;
	private Longword[] registers;
	private ALU alu;
	private Bit[] cmpResult;
	
	public Computer()
	{
		cmpResult = new Bit[2];
		alu = new ALU();
		result = null;
		op1 = new Longword();
		op2 = new Longword();
		registers = new Longword[16];
		for(int i = 0; i<16; i++)
		{
			registers[i] = new Longword();
		}
		pc = new Longword(); //points to memory
		stackPoint = new Longword();
		stackPoint.Set(1020);
		currentInstruction = new Longword(); //will store instructions from memory
		mem = new Memory(); //buncha zeros
		halted  = new Bit(); //default false aka running
	}
	
	public void Run()
	{
		while(halted.GetValue()!=true)
		{
			fetch();
			decode();
			execute();
			store();
		}
	}
	
	private void fetch()
	{
		currentInstruction.copy(mem.read(pc)); //copy 32 bits from byte 0
		incrementPC(); //increment address by 2 bytes
	}
	
	private void decode()
	{
		if(currentInstruction.getBit(0).GetValue() == false && currentInstruction.getBit(1).GetValue() == false && currentInstruction.getBit(2).GetValue() == false && currentInstruction.getBit(3).GetValue() == true )
		{   //MOVE INSTRUCTION
			op1.copy(currentInstruction.leftShift(4)); 
			op1.copy(op1.rightShift(28)); //NOW OP1.GETSIGNED WILL RETURN THE POSITION IN THE REGISTER ARRAY
			op2.copy(currentInstruction.leftShift(8));
			op2.copy(op2.rightShift(24)); //NOW OP2.GETSIGNED WILL RETURN THE NUMBER TO BE MOVED INTO THE REGISTER
			if(op2.getBit(24).GetValue()==true) //IF THIS BIT IS ON THE NUMBER IS NEG AND NEEDS TO BE PADDED WITH 1s 
			{
				for(int i =0; i<24; i++)
				{
					op2.setBit(i, new Bit(true));
				}
			}
		}
		else if(currentInstruction.getBit(0).GetValue() == false && currentInstruction.getBit(1).GetValue() == true && currentInstruction.getBit(2).GetValue() == true && currentInstruction.getBit(3).GetValue() == false)
		{
			//PUSH/POP/CALL/RETURN
			if(currentInstruction.getBit(4).GetValue()==true && currentInstruction.getBit(5).GetValue()== false) //CALL
			{
				op1.copy(currentInstruction.leftShift(6));
				op1.copy(op1.rightShift(22));
			}
			else if(currentInstruction.getBit(4).GetValue()==true && currentInstruction.getBit(5).GetValue()== true) //RETURN
			{
				//DO NOTHING
			}
			else
			{
				op1.copy(currentInstruction.leftShift(12));
				op1.copy(op1.rightShift(28));
			}
			
		}
		else if(currentInstruction.getBit(0).GetValue() == false && currentInstruction.getBit(1).GetValue() == true && currentInstruction.getBit(2).GetValue() == false && currentInstruction.getBit(3).GetValue() == true)
		{
			//BRANCH INSTRUCTION AKA JUMP THIS FAR AHEAD IN MEMORY
			op1.copy(currentInstruction.leftShift(4));
			op1.copy(op1.rightShift(30)); //CONTAINS TWO BITS REPRESENTING THE COMPARISON
			op2.copy(currentInstruction.leftShift(6));
			op2.copy(op2.rightShift(22)); //CONTAINS AMOUNT OF BYTES TO JUMP FORWARD IN MEMORY
			if(op2.getBit(22).GetValue()==true) //IF THIS BIT IS ON THE NUMBER IS NEGATIVE AND NEEDS TO BE PADDED WITH 1s
			{
				for(int i =0; i<22; i++)
				{
					op2.setBit(i, new Bit(true));
				}
			}
		}
		else if(currentInstruction.getBit(0).GetValue() == false && currentInstruction.getBit(1).GetValue() == false && currentInstruction.getBit(2).GetValue() == true && currentInstruction.getBit(3).GetValue() == true)
		{
			//JUMP TO THIS BYTE ADDRESS INSTRUCTION
			op1.copy(currentInstruction.leftShift(6));
			op1.copy(op1.rightShift(22)); //CONATINS BYTE ADDRESS TO JUMP TO
		}
		else if(currentInstruction.getBit(0).GetValue() == false && currentInstruction.getBit(1).GetValue() == true && currentInstruction.getBit(2).GetValue() == false && currentInstruction.getBit(3).GetValue() == false)
		{
			//COMPARE INSTRUCTION
			op1.copy(currentInstruction.leftShift(8)); 
			op1.copy(op1.rightShift(28));
			op1.copy(registers[op1.getSigned()]); // GOT THE VALUE FROM THE FIRST REGISTER
			op2.copy(currentInstruction.leftShift(12));
			op2.copy(op2.rightShift(28));
			op2.copy(registers[op2.getSigned()]); //GOT THE VALUE FROM THE SECOND REGISTER
		}
		else if(currentInstruction.getBit(0).GetValue() == false && currentInstruction.getBit(1).GetValue() == false && currentInstruction.getBit(2).GetValue() == true && currentInstruction.getBit(3).GetValue() == false)
		{
			//PRINT COMMAND DO NOTHING
		}
		else
		{
			//DO A BIT OPERATION
			op1.copy(currentInstruction.leftShift(4)); //instructions should sit in first 16 bits so left shift 4
			op1.copy(op1.rightShift(28)); //then right shift 28
			op1.copy(registers[op1.getSigned()]);//convert to a decimal number 0-15 and copy from correct register into op1
			op2.copy(currentInstruction.leftShift(8)); //looking for next register so left shift 8
			op2.copy(op2.rightShift(28)); //then right shift 28 (brings the four bits we are looking for to least significant bits in a longword)(everything else is zero)
			op2.copy(registers[op2.getSigned()]); //convert to a decimal number 0-15 and copy from correct register into op2
		}
	}
	
	private void execute() //read first four bits in current instruction for the op code array of 4 bits, then do math using the alu
	{
		if(currentInstruction.getBit(0).GetValue() == false && currentInstruction.getBit(1).GetValue() == false && currentInstruction.getBit(2).GetValue() == false && currentInstruction.getBit(3).GetValue() == true)
		{
			//IF MOVE INSTRUCTION THERE IS NOTHING TO EXECUTE HERE
			//WILL WILL STORE THE NUMBER IN THE REGISTER IN THE STORE FUNCTION
		}
		else if(currentInstruction.getBit(0).GetValue() == false && currentInstruction.getBit(1).GetValue() == true && currentInstruction.getBit(2).GetValue() == true && currentInstruction.getBit(3).GetValue() == false)
		{
			//PUSH/POP
			//PUSH/POP/CALL/RETURN
			if(currentInstruction.getBit(4).GetValue()==true && currentInstruction.getBit(5).GetValue()== false) //CALL
			{
				Longword temp = new Longword();
				temp.Set(2);
				mem.write(stackPoint, pc);
				int num = stackPoint.getSigned();
				num = num - 4;
				stackPoint.Set(num);
				pc.Set(op1.getSigned());
			}
			else if(currentInstruction.getBit(4).GetValue()==true && currentInstruction.getBit(5).GetValue()== true) //RETURN
			{
				int num = stackPoint.getSigned();
				num = num + 4;
				stackPoint.Set(num);
				Longword temp = mem.read(stackPoint);
				mem.write(stackPoint, new Longword());
				pc.Set(temp.getSigned());
				
			}
			else if(currentInstruction.getBit(4).GetValue()==false && currentInstruction.getBit(5).GetValue()==true) //POP
			{
				int num = stackPoint.getSigned();
				num = num + 4;
				stackPoint.Set(num);
				registers[op1.getSigned()].copy(mem.read(stackPoint));
				mem.write(stackPoint, new Longword());
			}
			else if(currentInstruction.getBit(4).GetValue()==false && currentInstruction.getBit(5).GetValue()==false) //PUSH
			{
				mem.write(stackPoint, registers[op1.getSigned()]);
				int num = stackPoint.getSigned();
				num = num - 4;
				stackPoint.Set(num);
			}
		}
		else if(currentInstruction.getBit(0).GetValue() == false && currentInstruction.getBit(1).GetValue() == true && currentInstruction.getBit(2).GetValue() == false && currentInstruction.getBit(3).GetValue() == true)
		{
			//BRANCH INSTRUCTION
			if(op1.getSigned()==0  && cmpResult[1].GetValue()==false) // not equal
			{
				result.copy(op2); //IF THE CONDITION IS TRUE STORE THE AMOUNT OF BYTES TO JUMP IN RESULT
			}
			else if(op1.getSigned()==1 && cmpResult[1].GetValue()==true) // equal
			{
				result.copy(op2);
			}
			else if(op1.getSigned()==2 && cmpResult[0].GetValue()==true) // greater than
			{
				result.copy(op2);
			}
			else if(op1.getSigned()==3 && (cmpResult[0].GetValue()==true || cmpResult[1].GetValue()==true)) // greater than or equal
			{
				result.copy(op2);
			}
			else
			{
				result = null;
			}
		}
		else if(currentInstruction.getBit(0).GetValue() == false && currentInstruction.getBit(1).GetValue() == false && currentInstruction.getBit(2).GetValue() == true && currentInstruction.getBit(3).GetValue() == true)
		{
			//JUMP INSTRUCTION
			pc.Set(op1.getSigned()); //SET ADDRESS POINTER TO VALUE FROM JMP COMMAND
		}
		else if(currentInstruction.getBit(0).GetValue() == false && currentInstruction.getBit(1).GetValue() == true && currentInstruction.getBit(2).GetValue() == false && currentInstruction.getBit(3).GetValue() == false)
		{
			//COMPARE INSTRUCTION
			Bit[] opcode = {new Bit(true), new Bit(true), new Bit(true), new Bit(true)};
			result = ALU.doOp(opcode, op1, op2); //SUBTRACT TWO REGISTERS
		}
		else if(currentInstruction.getSigned()>=536870912 && currentInstruction.getSigned()<=536936447)
		{
			printRegisters();
		}
		else if(currentInstruction.getSigned()>=536936448 && currentInstruction.getSigned()<=537001983)
		{
			printMem();
		}
		else
		{
			Bit[] opcode = new Bit[4];
			for(int i=0;i<opcode.length; i++)
			{
				opcode[i] = currentInstruction.getBit(i);
			}
			if(opcode[0].GetValue()==false && opcode[1].GetValue()==false && opcode[2].GetValue()==false && opcode[3].GetValue()==false)
			{
				halted.Set();
			}
			else
			{
				result = ALU.doOp(opcode, op1, op2);
			}
		}
	}
	
	private void store() 
	{
		if(halted.GetValue()!=true)
		{
			if(currentInstruction.getBit(0).GetValue() == false && currentInstruction.getBit(1).GetValue() == false && currentInstruction.getBit(2).GetValue() == false && currentInstruction.getBit(3).GetValue() == true)
			{
				//MOVE INSTRCUTION
				registers[op1.getSigned()].Set(op2.getSigned()); //MOVE VALUE INTO CORRECT REG
			}
			else if(currentInstruction.getBit(0).GetValue() == false && currentInstruction.getBit(1).GetValue() == true && currentInstruction.getBit(2).GetValue() == false && currentInstruction.getBit(3).GetValue() == true)
			{
				//BRANCH INSTRUCTION
				if(result!=null)
				{
					pc.Set(pc.getSigned() + result.getSigned()); //JUMP THIS MANY BYTES FORWARD/BACKWARD
				}
			}
			else if(currentInstruction.getBit(0).GetValue() == false && currentInstruction.getBit(1).GetValue() == true && currentInstruction.getBit(2).GetValue() == false && currentInstruction.getBit(3).GetValue() == false)
			{
				//COMPARE INSTRUCTION
				if(result.getSigned()>0)
				{
					//first register is greater than the second register
					cmpResult[0] = new Bit(true);
					cmpResult[1] = new Bit(false);
				}
				else if(result.getSigned()<0)
				{
					//first register is less than the second register
					cmpResult[0] = new Bit(false);
					cmpResult[1] = new Bit(false);
				}
				else
				{
					//the registers have equal values
					cmpResult[0] = new Bit(false);
					cmpResult[1] = new Bit(true);
				}
			}
			else if(currentInstruction.getBit(0).GetValue() == false && currentInstruction.getBit(1).GetValue() == true && currentInstruction.getBit(2).GetValue() == true && currentInstruction.getBit(3).GetValue() == false)
			{
				//PUSH/POP/CALL/RETURN
				// DO NOTHING
			}
			else if(result!=null)
			{
				Longword mask = new Longword();
				mask.Set(983040); //used a mask this time to get the third register
				mask.copy(currentInstruction.AndWords(mask)); //and current instruct with 00000000000011110000000000000000
				mask.copy(mask.rightShift(16)); //then right shift to get them in least significant bit location
				registers[mask.getSigned()].copy(result); //convert to deci and copy result calculated previously into assigned register
				result=null;
			}
		}
	}
	
	private void incrementPC() //function i made for incrementing address "pointer"
	{
		Longword incrementer = new Longword();
		incrementer.Set(2);
		Longword temp = RippleAdder.Add(pc, incrementer);
		pc.copy(temp);
	}
	
	public void printRegisters()
	{
		System.out.println("REG0: " + registers[0].toString() + " BASE10: " + registers[0].getSigned());
		System.out.println("REG1: " + registers[1].toString() + " BASE10: " + registers[1].getSigned());
		System.out.println("REG2: " + registers[2].toString() + " BASE10: " + registers[2].getSigned());
		System.out.println("REG3: " + registers[3].toString() + " BASE10: " + registers[3].getSigned());
		System.out.println("REG4: " + registers[4].toString() + " BASE10: " + registers[4].getSigned());
		System.out.println("REG5: " + registers[5].toString() + " BASE10: " + registers[5].getSigned());
		System.out.println("REG6: " + registers[6].toString() + " BASE10: " + registers[6].getSigned());
		System.out.println("REG7: " + registers[7].toString() + " BASE10: " + registers[7].getSigned());
		System.out.println("REG8: " + registers[8].toString() + " BASE10: " + registers[8].getSigned());
		System.out.println("REG9: " + registers[9].toString() + " BASE10: " + registers[9].getSigned());
		System.out.println("REG10: " + registers[10].toString() + " BASE10: " + registers[10].getSigned());
		System.out.println("REG11: " + registers[11].toString() + " BASE10: " + registers[11].getSigned());
		System.out.println("REG12: " + registers[12].toString() + " BASE10: " + registers[12].getSigned());
		System.out.println("REG13: " + registers[13].toString() + " BASE10: " + registers[13].getSigned());
		System.out.println("REG14: " + registers[14].toString() + " BASE10: " + registers[14].getSigned());
		System.out.println("REG15: " + registers[15].toString() + " BASE10: " + registers[15].getSigned());
	}
	
	public void printMem()
	{
		mem.printMemory();
	}
	
	
	void preload(String[] args)
	{
		Longword address = new Longword();
		
		int longWordPosCounter = 0;
		Longword temp = new Longword();
		
		
		for(String miniCode : args)
		{
			if(miniCode.length()==4)
			{
				for(int i=0; i<4; i++)
				{
					if(miniCode.charAt(i)=='0')
					{
						temp.setBit(longWordPosCounter, new Bit(false));
					}
					else if(miniCode.charAt(i)=='1')
					{
						temp.setBit(longWordPosCounter, new Bit(true));
					}else{new RuntimeException(miniCode);}
					longWordPosCounter++;
				}
			}else{throw new RuntimeException();}
			
			if(longWordPosCounter==32)
			{
				address.Set(loadPos);
				mem.write(address, temp);
				loadPos = loadPos + 4;
				temp = new Longword();
				longWordPosCounter = 0;
			}
		}
		
		if(longWordPosCounter!=0)
		{
			address.Set(loadPos);
			mem.write(address, temp);
			loadPos = loadPos + 4;
			temp = new Longword();
			longWordPosCounter = 0;
		}
	}
	
	boolean validChar(char val)
	{
		if(val=='0' || val=='1')
		{
			return true;
		}else
		{
			return false;
		}
		
	}
	
	
}