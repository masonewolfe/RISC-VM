import java.util.*;
import java.lang.Math;
public class Parser
{
	private List<Token> tokenList;
	String onesAndZeros;
	
	public Parser(List<Token> input)
	{
		tokenList = input;
	}
	
	public String Parse()
	{
		String command = "";
		while(!tokenList.isEmpty())
		{
			if(tokenList.get(0).getTokenType()==Token.possibleTokens.MOV)
			{
				command = command + parseAMov() + " ";
			}
			else if(tokenList.get(0).getTokenType()==Token.possibleTokens.POP ||
					tokenList.get(0).getTokenType()==Token.possibleTokens.PUSH)
			{
				command = command + parsePP() + " ";
			}
			else if(tokenList.get(0).getTokenType()==Token.possibleTokens.CALL)
			{
				command = command + parseCall() + " ";
			}
			else if(tokenList.get(0).getTokenType()==Token.possibleTokens.RETURN)
			{
				command = command + parseRet() + " ";
			}
			else if(tokenList.get(0).getTokenType()==Token.possibleTokens.JMP)
			{
				command = command + parseJumpCommand() + " ";
			}
			else if(tokenList.get(0).getTokenType()==Token.possibleTokens.CMP)
			{
				command = command + parseCompareCommand() + " ";
			}
			else if(tokenList.get(0).getTokenType()==Token.possibleTokens.JG ||
					tokenList.get(0).getTokenType()==Token.possibleTokens.JGE || 
					tokenList.get(0).getTokenType()==Token.possibleTokens.JE ||
					tokenList.get(0).getTokenType()==Token.possibleTokens.JNE)
			{
				command = command + parseBranchCommand() + " ";
			}
			else if(tokenList.get(0).getTokenType()==Token.possibleTokens.ADD ||
					tokenList.get(0).getTokenType()==Token.possibleTokens.SUB || 
					tokenList.get(0).getTokenType()==Token.possibleTokens.AND ||
					tokenList.get(0).getTokenType()==Token.possibleTokens.OR ||
					tokenList.get(0).getTokenType()==Token.possibleTokens.XOR ||
					tokenList.get(0).getTokenType()==Token.possibleTokens.IMUL ||
					tokenList.get(0).getTokenType()==Token.possibleTokens.SHL ||
					tokenList.get(0).getTokenType()==Token.possibleTokens.SHR ||
					tokenList.get(0).getTokenType()==Token.possibleTokens.NOT)
				
			{
				command = command + parseAnOp() + " ";
			}
			else if(tokenList.get(0).getTokenType()==Token.possibleTokens.PRINT)
			{
				command = command + parsePrintCommand() + " ";
			}
			else
			{
				throw new RuntimeException("INVALID COMMAND");
			}
		}
		return command;
	}
	
	public void printTokens()
	{
		for(Token t:tokenList)
		{
			System.out.print(t.toString());
		}
		System.out.println();
	}
	
	public Token Pop(Token match)
	{
		if(!tokenList.isEmpty())
		{
			if(tokenList.get(0).getTokenType() == match.getTokenType())
			{
				Token temp = tokenList.get(0);
				tokenList.remove(0);
				return temp;
			}
			else
			{
				return null;
			}
		}else{return null;}
	}
	
	
	public String parseCall()
	{
		String output = "";
		Token temp = null;
		temp = Pop(new Token("call"));
		if(temp==null)
		{
			return null;
		}
		output = output + "0110 10";
		temp = Pop(new Token("1"));
		if(temp==null)
		{
			return null;
		}
		int num = Integer.parseInt(temp.getLiteral());
		if(num>=0 && num<=1020)
		{
			return output + numInBinary(num, 10);
		}else {return null;}
	}
	
	public String parseRet()
	{
		Token temp = Pop(new Token("return"));
		if(temp==null)
		{
			return null;
		}
		return "0110 1100 0000 0000";
	}
	
	
	public String parsePP()
	{
		String output = "";
		Token temp = null;
		temp = Pop(new Token("pop"));
		if(temp!=null)
		{
			output = output + "0110 0100 0000 ";
		}
		else
		{
			temp = Pop(new Token("push"));
			if(temp!=null)
			{
				output = output + "0110 0000 0000 ";
			}
			else{return null;}
		}
		temp = Pop(new Token("R0"));
		if(temp==null)
		{
			return null;
		}
		output = output + regInBinary(getRegNum(temp.getLiteral()));
		return output;
	}
	
	public String parseBranchCommand()
	{
		String output = "";
		Token temp=null;
		for(int i =0; i<4; i++)
		{
			switch(i)
			{
			case 0:temp = Pop(new Token("jg")); break;
			case 1:temp = Pop(new Token("jge")); break;
			case 2:temp = Pop(new Token("je")); break;
			case 3:temp = Pop(new Token("jne")); break;
			}
			if(temp!=null)
			{
				if(temp.getTokenType()==Token.possibleTokens.JG)
				{
					output = "0101 10";
				}
				else if(temp.getTokenType()==Token.possibleTokens.JGE)
				{
					output = "0101 11";
				}
				else if(temp.getTokenType()==Token.possibleTokens.JE)
				{
					output = "0101 01";
				}
				else if(temp.getTokenType()==Token.possibleTokens.JNE)
				{
					output = "0101 00";
				}
				break;
			}
		}
		
		temp = Pop(new Token("1"));
		if(temp==null)
		{
			return null;
		}
		
		output = output + numInBinary(Integer.parseInt(temp.getLiteral()), 10);
		return output;
	}
	public String parseCompareCommand()
	{
		String tempString = "";
		Token temp = Pop(new Token("cmp"));
		if(temp==null)
		{
			return null;
		}
		tempString = "0100 0000 ";
		
		temp = Pop(new Token("R0"));
		if(temp==null)
		{
			return null;
		}
		tempString = tempString + regInBinary(getRegNum(temp.getLiteral()));
		
		temp = Pop(new Token("R0"));
		if(temp==null)
		{
			return null;
		}
		tempString = tempString + " " + regInBinary(getRegNum(temp.getLiteral()));
		
		
		return tempString;
	}
	public String parseJumpCommand()
	{
		Token temp = Pop(new Token("jmp"));
		if(temp==null)
		{
			return null;
		}
		temp = Pop(new Token("1"));
		if(temp==null)
		{
			return null;
		}
		int num = Integer.parseInt(temp.getLiteral());
		if(num>=0 && num<=1023)
		{
			return "0011 00"+ numInBinary(num, 10);
		}
		else
		{
			throw new RuntimeException("MEMORY ADDRESS OUT OF RANGE");
		}
		
	}
	
	public String parsePrintCommand()
	{
		Token temp = Pop(new Token("print"));
		if(temp==null)
		{
			return null;
		}
		temp = Pop(new Token("registers"));
		if(temp!=null)
		{
			return "0010 0000 0000 0000";
		}
		temp = Pop(new Token("memory"));
		if(temp!=null)
		{
			return "0010 0000 0000 0001";
		}
		else
		{
			return null;
		}
	}
	public String parseAMov()
	{
		String output = "";
		String tempString;
		Token temp = Pop(new Token("mov"));
		if(temp==null)
		{
			return null;
		}
		output = output + "0001 ";
		
		temp = Pop(new Token("R0"));
		if(temp==null)
		{
			return null;
		}
		tempString = regInBinary(getRegNum(temp.getLiteral()));
		if(tempString==null)
		{
			return null;
		}
		output = output + tempString + " ";
		
		temp = Pop(new Token("1"));
		if(temp==null)
		{
			return null;
		}
		int num = Integer.parseInt(temp.getLiteral());
		if(num<=127 && num>=-128)
		{
			tempString = numInBinary(num, 8);
		}
		else
		{
			throw new RuntimeException("NOT A VALID NUMBER TO MOVE TO A REGISTER");
		}
		
		output = output + tempString;
		
		return output;
	}
	
	public String parseAnOp()
	{
		String output = "";
		String tempString;
		Token temp = null;
		
		for(int i=0; i<9; i++)
		{
			switch(i)
			{
			case 0: temp = Pop(new Token("and")); break;
			case 1: temp = Pop(new Token("or")); break;
			case 2: temp = Pop(new Token("xor")); break;
			case 3: temp = Pop(new Token("not")); break;
			case 4: temp = Pop(new Token("shl")); break;
			case 5: temp = Pop(new Token("shr")); break;
			case 6: temp = Pop(new Token("add")); break;
			case 7: temp = Pop(new Token("sub")); break;
			case 8: temp = Pop(new Token("imul")); break;
			}
			
			if(temp!=null)
			{
				break;
			}
		}
		output = output + getOpCode(temp) + " ";
		
		for(int i=0; i<3; i++)
		{
			temp = Pop(new Token("R0"));
			if(temp==null)
			{
				return null;
			}
			tempString = regInBinary(getRegNum(temp.getLiteral()));
			if(tempString==null)
			{
				return null;
			}
			
			if(i==2)
			{
				output = output + tempString;
			}
			else
			{
				output = output + tempString + " ";
			}
			
		}
		
		return output;
	}
	
	public int getRegNum(String reg)
	{
		if(reg.length()==2)
		{
			return Character.getNumericValue(reg.charAt(1));
		}
		else
		{
			String num = "";
			num = num + reg.charAt(1);
			num = num + reg.charAt(2);
			return Integer.parseInt(num);
		}
	}
	
	public String regInBinary(int num)
	{
		if(num>=0 && num<=15)
		{
			String output = "";
			int goesInto = 8;
			while(goesInto>=1)
			{
				if(num-goesInto>=0)
				{
					output = output + "1";
					num = num - goesInto;
				}
				else {output = output + "0";}
				goesInto = goesInto/2;
			}
			return output;
		}
		else
		{
			throw new RuntimeException("NOT A VALID REGISTER");
		}
	}
	
	public String numInBinary(int num, int numBits)
	{
		Longword temp = new Longword();
		temp.Set(num);
		int length = temp.toString().length();
		int mostSigBit = length-1-numBits;
		String output = "";
		int spacePlacer = 0;
		for(int i=length-1; i>mostSigBit; i--)
		{
			if(spacePlacer==4)
			{
				output = " " + output;
				spacePlacer = 0;
			}
			output = temp.toString().charAt(i) + output;
			spacePlacer++;
		}
		return output;
	}
	
	public String getOpCode(Token op)
	{
		if(op.getTokenType()==Token.possibleTokens.AND)
		{
			return "1000";
		}
		else if(op.getTokenType()==Token.possibleTokens.OR)
		{
			return "1001";
		}
		else if(op.getTokenType()==Token.possibleTokens.XOR)
		{
			return "1010";
		}
		else if(op.getTokenType()==Token.possibleTokens.NOT)
		{
			return "1011";
		}
		else if(op.getTokenType()==Token.possibleTokens.SHL)
		{
			return "1100";
		}
		else if(op.getTokenType()==Token.possibleTokens.SHR)
		{
			return "1101";
		}
		else if(op.getTokenType()==Token.possibleTokens.ADD)
		{
			return "1110";
		}
		else if(op.getTokenType()==Token.possibleTokens.SUB)
		{
			return "1111";
		}
		else if(op.getTokenType()==Token.possibleTokens.IMUL)
		{
			return "0111";
		}
		else
		{
			return null;
		}
	}

	
	
	
	
	
	
	
	
	
	
	
}