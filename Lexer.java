import java.util.*;
public class Lexer
{
	private List<Token> tokenList;
	
	public Lexer()
	{
		tokenList = new ArrayList<Token>();
	}
	
	public void clearList()
	{
		tokenList.clear();
	}
	
	public void printTokens()
	{
		for(Token t:tokenList)
		{
			System.out.print(t.toString());
		}
		System.out.println();
	}
	
	public List<Token> getList()
	{
		return tokenList;
	}
	
	public void Lex(String line)
	{
		String building = "";
		int state = 0;
		for(int i=0; i<line.length(); i++)
		{
			char pos = line.charAt(i);
			
			if(state==0) //KNOW NOTHING STATE
			{
				if((pos>= 65 && pos<=90) || (pos >= 97 && pos<=122)) //is a letter
				{
					building = building + pos;
					state=1;
				}
				else if((pos>=48 && pos<=57) || pos==43 || pos==45) //is a number
				{
					building = building + pos;
					state=2;
				}
				else if(pos==' ')
				{
					//do nothing
				}
				else
				{
					throw new RuntimeException("ONLY ALPHANUMERIC CHARACTERS ALLOWED");
				}
			}
			else if(state==1) //building a word
			{
				if((pos>= 65 && pos<=90) || (pos >= 97 && pos<=122)) //is a letter
				{
					building = building + pos;
				}
				else if(pos>=48 && pos<=57) //is a number
				{
					building = building + pos;
				}
				else if(pos==' ')
				{
					tokenList.add(new Token(building));
					building="";
					state=0;
				}
				else
				{
					throw new RuntimeException("ONLY ALPHANUMERIC CHARACTERS ALLOWED");
				}
			}
			else if(state==2) //building a number
			{
				if((pos>= 65 && pos<=90) || (pos >= 97 && pos<=122)) //is a letter
				{
					throw new RuntimeException("WORDS CANNOT BEGIN WITH NUMBERS");
				}
				else if(pos>=48 && pos<=57) //is a number
				{
					building = building + pos;
				}
				else if(pos==' ')
				{
					tokenList.add(new Token(building));
					building="";
					state=0;
				}
				else
				{
					throw new RuntimeException("ONLY ALPHANUMERIC CHARACTERS ALLOWED");
				}
			}
		}
		
		if(state==1 || state==2)
		{
			tokenList.add(new Token(building));
		}
	}
}