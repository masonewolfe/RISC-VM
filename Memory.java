public class Memory
{
	private Bit[] memory = new Bit[8192];
	
	
	
	public Memory()
	{
		for(int i=0;i<memory.length;i++)
		{
			memory[i]=new Bit();
		}
	}
	
	public Longword read(Longword address)
	{
		int pos = address.getSigned();
		Longword output = new Longword();
		if(pos>=0 && pos<=1020)
		{
			int i = 0;
			int startBit = pos * 8;
			int endBit = startBit + 32;
			while(startBit<endBit)
			{
				output.setBit(i, memory[startBit]);
				startBit++;
				i++;
			}
			return output;
		}
		else
		{
			System.out.println("ADDRESS OUT OF RANGE");
			throw new RuntimeException();
		}
		
	}
	
	public void write(Longword address, Longword value)
	{
		int pos = address.getSigned();
		if(pos>=0 && pos<=1020)
		{	
			int i = 0;
			int startBit = pos * 8;
			int endBit = startBit + 32;
			while(startBit<endBit)
			{
				memory[startBit].Set(value.getBit(i).GetValue());
				startBit++;
				i++;
			}
		}
		else
		{
			System.out.println("ADDRESS OUT OF RANGE");
			throw new RuntimeException();
		}
	}
	
	public void printMemory()
	{
		for(int i=0;i<memory.length;i++)
		{
			if(i%128==0 && i!=0)
			{
				System.out.print("\n"+memory[i].toString());
			}
			else
			{
				System.out.print(memory[i].toString());
			}
		}
		System.out.println();
		
	}
	

}