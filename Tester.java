import java.util.*;
import java.util.Random;
import java.util.Scanner;
public class Tester
{
	public Tester()
	{
		
	}
	public static void runTests()
	{
		List<String> commands = new ArrayList<String>();
		String[] commandsArr;
		String[] binary;
		Scanner readIn = new Scanner(System.in);
		
		

		System.out.println("PLEASE ENTER 1 COMMAND AT A TIME");
		System.out.println("WHEN YOU WANT TO RUN THE PROGRAM PLEASE TYPE: run ");
		System.out.println("LIST OF ALL COMMANDS: and, or, xor, not, shl, shr, add, sub, imul, ");
		System.out.println("mov, cmp, jmp, je, jne, jge, jg, pop, push, call, return, print");
		System.out.println("EXAMPLE COMMANDS: ");
		System.out.println("pop r1");
		System.out.println("push r1");
		System.out.println("call 6");
		System.out.println("return");
		System.out.println("mov r1 123");
		System.out.println("add r1 r2 r3");
		System.out.println("imul r1 r2 r2");
		System.out.println("shl r1 r2 r7");
		System.out.println("xor r4 r5 r6");
		System.out.println("jmp 10");
		System.out.println("cmp r4 r5");
		System.out.println("je 5");
		System.out.println("print registers");
		System.out.println("print memory");

		String command = readIn.nextLine().trim();
		while(!command.equals("run"))
		{
			if(command!="")
			{
				commands.add(command);
			}
			command = readIn.nextLine().trim();
		}
		
		commandsArr = new String[commands.size()];
		int i = 0;
		for(String s : commands)
		{
			commandsArr[i] = s;
			i++;
		}
		
		if(commandsArr.length>0)
		{
			binary = Assembler.Assemble(commandsArr);
			Computer comp = new Computer();
			comp.preload(binary);
			comp.Run();
		}
	}
	
	private static int isNeg()
	{
		Random rand = new Random();
		int val = rand.nextInt(5)%2;
		switch(val)
		{
			case 1: val = -1; break;
			case 0: val = 1; break;
		}
		return val;
	}
	
	private static Bit[] getOp(int num)
	{
		if(num>=7 && num <=15)
		{
			Bit[] nibble = new Bit[4];
			Longword temp = new Longword();
			temp.Set(num);
			for(int i = 0; i<4 ; i++)
			{
				nibble[i] = temp.getBit(i+28);
			}
			return nibble;
		}
		else
		{
			throw new RuntimeException("NOT A VALID OPERATION");
		}
	}

}