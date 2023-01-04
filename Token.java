public class Token
{
	public enum possibleTokens
	{
		AND,OR,XOR,NOT,SHL,SHR,ADD,SUB,IMUL,REGISTER,NUMBER,MOV,
		PRINT,REGISTERS,MEMORY,CMP,JMP,JG,JGE,JE,JNE,PUSH,POP,CALL,RETURN
	}
	
	private String literal;
	private possibleTokens tokenType;
	
	public Token(String value)
	{
		literal = value;
		HashMap(value);
	}
	
	public String getLiteral()
	{
		return literal;
	}
	
	public possibleTokens getTokenType()
	{
		return tokenType;
	}
	
	public String toString()
	{
		return tokenType + "(" + literal + ") ";
	}
	
	public void HashMap(String value)
	{
		switch(value)
		{
			case "cmp": tokenType = possibleTokens.CMP; break;
			case "jmp": tokenType = possibleTokens.JMP; break; 
			case "jg": tokenType = possibleTokens.JG; break; 
			case "jge": tokenType = possibleTokens.JGE; break; 
			case "je": tokenType = possibleTokens.JE; break; 
			case "jne": tokenType = possibleTokens.JNE; break; 
			case "and": tokenType = possibleTokens.AND; break;
			case "or": tokenType = possibleTokens.OR; break;
			case "xor": tokenType = possibleTokens.XOR; break;
			case "not": tokenType = possibleTokens.NOT; break;
			case "shl": tokenType = possibleTokens.SHL; break;
			case "shr": tokenType = possibleTokens.SHR; break;
			case "add": tokenType = possibleTokens.ADD; break;
			case "sub": tokenType = possibleTokens.SUB; break;
			case "imul": tokenType = possibleTokens.IMUL; break;
			case "mov": tokenType = possibleTokens.MOV; break;
			case "print" : tokenType = possibleTokens.PRINT; break;
			case "registers" : tokenType = possibleTokens.REGISTERS; break;
			case "memory" : tokenType = possibleTokens.MEMORY; break;
			case "push" : tokenType = possibleTokens.PUSH; break;
			case "pop" : tokenType = possibleTokens.POP; break;
			case "call" : tokenType = possibleTokens.CALL; break;
			case "return" : tokenType = possibleTokens.RETURN; break;
			default:
				try
				{
					Integer.parseInt(value);
					tokenType = possibleTokens.NUMBER; break;
				}catch(Exception e)
				{
					if(value.length()>=2 && value.length()<=3 && (value.charAt(0)=='R' ||  value.charAt(0)=='r'))
					{
						for(int i=1; i<value.length(); i++)
						{
							if(value.charAt(i)<48 || value.charAt(i)>57)
							{
								throw new RuntimeException("INVALID REGISTER");
							}
						}
						tokenType = possibleTokens.REGISTER; break;
					}
					else
					{
						throw new RuntimeException("INVALID TOKEN: " + value);
					}
				}		
		}
	}
	
	
	
	
	
	
	
	
	
}