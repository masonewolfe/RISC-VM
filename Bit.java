public class Bit 
{
	private boolean val;
	
	public Bit()
	{
		val = false;
	}
	
	public Bit(boolean value)
	{
		val = value;
	}
	
	public void Set(boolean newVal)
	{
		val = newVal;
	}
	
	public void Toggle()
	{
		if(val==false)
		{
			val = true;
		}
		else
		{
			val = false;
		}
	}
	
	public void Set()
	{
		val = true;
	}
	
	public void Clear()
	{
		val=false;
	}
	
	
	public boolean GetValue()
	{
		return val;
	}
	
	
	public Bit And(Bit other)
	{
		
		if(other.GetValue()==false)
		{
			Bit retBit = new Bit(false);
			return retBit;
		}
		else if(val == false)
		{
			Bit retBit = new Bit(false);
			return retBit;
		}
		else
		{
			Bit retBit = new Bit(true);
			return retBit;
		}
	}
	
	public Bit Or(Bit other)
	{
		if(other.GetValue()==true)
		{
			Bit retBit = new Bit(true);
			return retBit;
		}
		else if(val == true)
		{
			Bit retBit = new Bit(true);
			return retBit;
		}
		else
		{
			Bit retBit = new Bit(false);
			return retBit;
		}
	}
	
	public Bit Xor(Bit other)
	{
		if(other.GetValue()==val)
		{
			Bit retBit = new Bit(false);
			return retBit;
		}
		else
		{
			Bit retBit = new Bit(true);
			return retBit;
		}
	}
	
	public Bit Not()
	{
		if(val==false)
		{
			Bit retBit = new Bit(true);
			return retBit;
		}
		else
		{
			Bit retBit = new Bit(false);
			return retBit;
		}
	}
	
	public String toString()
	{
		if(val==true)
		{
			return "1";
		}
		else
		{
			return "0";
		}
	}
}

