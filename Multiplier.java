public class Multiplier
{
	public Multiplier()
	{
	
	
	}
	
	public static Longword Multiply(Longword a, Longword b)
	{
		int shiftAmount = 0;
		Longword tempWord = new Longword();
		Longword tempWord2 = new Longword();
		RippleAdder ripple = new RippleAdder();
		
		for(int i = 31; i>=0; i--)
		{
			for(int j = 31; j >=0; j--)
			{
				Bit tempBit = b.getBit(i).And(a.getBit(j));
				tempWord.setBit(j, tempBit);
			}
			tempWord = tempWord.leftShift(shiftAmount);
			if(shiftAmount!=0)
			{
				tempWord2.copy(ripple.Add(tempWord2, tempWord));
			}
			else
			{
				
				tempWord2.copy(tempWord);
			}
			shiftAmount++;
		}
		return tempWord2;
	}



}