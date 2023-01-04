public class Assembler
{
	
	public static String[] Assemble(String[] commands)
	{
		Lexer lex = new Lexer();
		for(String s : commands)
		{
			lex.Lex(s);
		}
		Parser parse = new Parser(lex.getList());
		String binary = parse.Parse();
		System.out.println("BINARY CODE TO BE WRITTEN TO MEMORY: " + binary);
		return binary.split(" ");
	}
	
	
	
	
	
	
}