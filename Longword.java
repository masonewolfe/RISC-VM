import java.lang.Math;

public class Longword
{
	private Bit[] bitArr;
	
	public Longword()
	{
		bitArr = new Bit[32];
		for(int i=0; i<bitArr.length; i++)
		{
			bitArr[i] = new Bit();
		}
	}
	
	
	public Bit getBit(int pos)
	{
		if(pos>31)
		{
			System.out.println("This bit is out of range for the amount of bits longword holds (0-31)");
			return null;
		}
		else
		{
			return bitArr[pos];
		}
	}
	
	public void setBit(int pos, Bit val)
	{
		if(pos>31)
		{
			System.out.println("This bit is out of range for the amount of bits longword holds (0-31)");
		}
		else
		{
			bitArr[pos].Set(val.GetValue());
		}
	}
	
	public Longword AndWords(Longword other)
	{
		Longword output = new Longword();
		
		for(int i = 0; i<bitArr.length; i++)
		{
			output.setBit(i, bitArr[i].And(other.getBit(i)));
		}
		
		return output;
	}
	
	public Longword OrWords(Longword other)
	{
		Longword output = new Longword();
		
		for(int i = 0; i<bitArr.length; i++)
		{
			output.setBit(i, bitArr[i].Or(other.getBit(i)));
		}
		
		return output;
	}
	
	public Longword XorWords(Longword other)
	{
		Longword output = new Longword();
		
		for(int i = 0; i<bitArr.length; i++)
		{
			output.setBit(i, bitArr[i].Xor(other.getBit(i)));
		}
		
		return output;
	}
	
	public Longword NotWord()
	{
		Longword output = new Longword();
		
		for(int i = 0; i<bitArr.length; i++)
		{
			output.setBit(i, bitArr[i].Not());
		}
		
		return output;
	}
	
	public String toString()
	{
		String output = "";
		for(int i=0; i<bitArr.length; i++)
		{
			output = output + bitArr[i].toString();
		}	
		
		return output;
	}
	
	public Longword rightShift(int amount)
	{
		Longword output = new Longword();
		
		for(int i = 0; i<bitArr.length; i++)
		{
			output.setBit(i, bitArr[i]);
		}
		
		while(amount>0)
		{
			for(int i=31; i>0; i--)
			{
				output.setBit(i, output.getBit(i-1));
			}
		
			output.setBit(0, new Bit(false));
			
			amount--;
		}
		
		return output;
	}
	
	public Longword leftShift(int amount)
	{
		Longword output = new Longword();
			
		for(int i = 0; i<bitArr.length; i++)
		{
			output.setBit(i, bitArr[i]);
		}
		
		while(amount>0)
		{
			for(int i=0; i<31; i++)
			{
				output.setBit(i, output.getBit(i+1));
			}
		
			output.setBit(31, new Bit(false));
			
			amount--;
		}
		
		return output;
	}
	
	public long getUnsigned()
	{
		long sum = 0;
		
		for(int i=31; i>=0; i--)
		{
			if(bitArr[i].GetValue()==true)
			{
				sum = sum + twoExp(31-i);
			}
		}
		
		return sum;
	}
	
	
	
	public long twoExp(int exp)
	{
		if(exp==0)
		{
			return 1;
		}
		else if(exp==1)
		{
			return 2;
		}
		else
		{
			long num = 2;
			while(exp>=2)
			{
				num = num*2;
				exp--;
			}
			return num;
		}
	}
	
	public int getSigned()
	{
		if(bitArr[0].GetValue()==true)
		{
			Longword temp = new Longword();
			temp.copy(twosCompliment());
			
			long sum = 0;
		
			for(int i=31; i>=0; i--)
			{
				if(temp.getBit(i).GetValue()==true)
				{
					sum = sum + twoExp(31-i);
				}
			}
		
			return Math.toIntExact(sum*(-1));
			
		}
		else
		{
			int sum = 0;
		
			for(int i=31; i>0; i--)
			{
				if(bitArr[i].GetValue()==true)
				{
					sum = sum + Math.toIntExact(twoExp(31-i));
				}
			}
		
			return sum;
		}
	}
	
	public void copy(Longword other)
	{
		for(int i=0; i<bitArr.length; i++)
		{
			bitArr[i].Set(other.getBit(i).GetValue());
		}
	}
	
	public Longword twosCompliment()
	{
		Longword output = new Longword();
		output.copy(NotWord());
		
		for(int i = 31; i>=0; i--)
		{
			if(output.getBit(i).GetValue()==true)
			{
				output.setBit(i, new Bit(false));
			}
			else
			{
				output.setBit(i, new Bit(true));
				break;
			}
		}

		return output;
	}
	
	void Set(int num)
	{
		long temp = num;
		if(num<0)
		{
			temp = temp * -1;
		}
		
		
		int start = 31;
		int pos = 0;
		
		while(temp>0)
		{
			if(temp-twoExp(start)>=0)
			{
				temp = temp - twoExp(start);
				bitArr[pos].Set(true);
			}
			else
			{
				bitArr[pos].Set(false);
			}
			
			pos++;
			start--;
		}
		
		if(pos<=31)
		{
			while(pos<=31)
			{
				bitArr[pos].Set(false);
				pos++;
			}
		}
		
		if(num<0)
		{
			copy(twosCompliment());
		}
	}




















}