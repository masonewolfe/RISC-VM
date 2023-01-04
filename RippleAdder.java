public class RippleAdder
{
	public RippleAdder()
	{
	
	
	}
	
	public static Longword Add(Longword left, Longword right)
	{
		Longword output = new Longword();
		boolean carry = false;
		for(int i = 31; i>=0; i--)
		{
			Bit temp = left.getBit(i).Xor(right.getBit(i));
			
			if(carry == true)
			{
				temp.Toggle();
			}
			
			if((left.getBit(i).GetValue()==true && right.getBit(i).GetValue()==true)||(carry==true && (left.getBit(i).GetValue()==true || right.getBit(i).GetValue()==true)))
			{
				carry = true;
			}
			else
			{
				carry = false;
			}
			
			output.setBit(i, temp);
		}
		
		return output;
	}
	
	public static Longword Subtract(Longword left, Longword right)
	{
		return Add(left, right.twosCompliment());
	}


}