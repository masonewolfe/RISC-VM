public class ALU
{
	public ALU()
	{
		
	}
	
	public static Longword doOp(Bit[] operation, Longword a, Longword b)
	{
		
		RippleAdder rip = new RippleAdder();
		Multiplier times = new Multiplier();
		
		if(operation.length!=4)
		{
			System.out.println("ARRAY SIZE DOESNT MATCH");
			return null;
		}
		else
		{
			if(operation[0].GetValue()==true && operation[1].GetValue()==false && operation[2].GetValue() == false && operation[3].GetValue()==false)
			{
				return a.AndWords(b);
			}
			else if(operation[0].GetValue()==true && operation[1].GetValue()==false && operation[2].GetValue() == false && operation[3].GetValue()==true)
			{
				return a.OrWords(b);
			}
			else if(operation[0].GetValue()==true && operation[1].GetValue()==false && operation[2].GetValue() == true && operation[3].GetValue()==false)
			{
				return a.XorWords(b);
			}
			else if(operation[0].GetValue()==true && operation[1].GetValue()==false && operation[2].GetValue() == true && operation[3].GetValue()==true)
			{
				return a.NotWord();
			}
			else if(operation[0].GetValue()==true && operation[1].GetValue()==true && operation[2].GetValue() == false && operation[3].GetValue()==false)
			{
				return a.leftShift(b.getSigned());
			}
			else if(operation[0].GetValue()==true && operation[1].GetValue()==true && operation[2].GetValue() == false && operation[3].GetValue()==true)
			{
				return a.rightShift(b.getSigned());
			}
			else if(operation[0].GetValue()==true && operation[1].GetValue()==true && operation[2].GetValue() == true && operation[3].GetValue()==false)
			{
				return rip.Add(a,b);
			}
			else if(operation[0].GetValue()==true && operation[1].GetValue()==true && operation[2].GetValue() == true && operation[3].GetValue()==true)
			{
				return rip.Subtract(a,b);
			}
			else if(operation[0].GetValue()==false && operation[1].GetValue()==true && operation[2].GetValue() == true && operation[3].GetValue()==true)
			{
				return times.Multiply(a,b);
			}
			else
			{
				System.out.println("BIT ORDER IN OPERATION ARRAY IS INVALID");
				return null;
			}
		}
	}
	
	public String toString(Bit[] operation)
	{
		if(operation.length!=4)
		{
			System.out.println("ARRAY SIZE DOESNT MATCH");
			return null;
		}
		else
		{
			if(operation[0].GetValue()==true && operation[1].GetValue()==false && operation[2].GetValue() == false && operation[3].GetValue()==false)
			{
				return "AND";
			}
			else if(operation[0].GetValue()==true && operation[1].GetValue()==false && operation[2].GetValue() == false && operation[3].GetValue()==true)
			{
				return "OR";
			}
			else if(operation[0].GetValue()==true && operation[1].GetValue()==false && operation[2].GetValue() == true && operation[3].GetValue()==false)
			{
				return "XOR";
			}
			else if(operation[0].GetValue()==true && operation[1].GetValue()==false && operation[2].GetValue() == true && operation[3].GetValue()==true)
			{
				return "NOT";
			}
			else if(operation[0].GetValue()==true && operation[1].GetValue()==true && operation[2].GetValue() == false && operation[3].GetValue()==false)
			{
				return "LEFTSHIFT";
			}
			else if(operation[0].GetValue()==true && operation[1].GetValue()==true && operation[2].GetValue() == false && operation[3].GetValue()==true)
			{
				return "RIGHTSHIFT";
			}
			else if(operation[0].GetValue()==true && operation[1].GetValue()==true && operation[2].GetValue() == true && operation[3].GetValue()==false)
			{
				return "PLUS";
			}
			else if(operation[0].GetValue()==true && operation[1].GetValue()==true && operation[2].GetValue() == true && operation[3].GetValue()==true)
			{
				return "MINUS";
			}
			else if(operation[0].GetValue()==false && operation[1].GetValue()==true && operation[2].GetValue() == true && operation[3].GetValue()==true)
			{
				return "TIMES";
			}
			else
			{
				System.out.println("BIT ORDER IN OPERATION ARRAY IS INVALID");
				return null;
			}
		}
	}
	
}