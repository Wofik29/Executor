package Game;

public class Quad {

	int width;
	int height;
	int x, y;
	
	int[] programm;
	int count;
	
	
	int currentStep = 100;
	int lastStep_x;
	int lastStep_y;
	boolean isStep = true;
	
	Quad(int x, int y,int w, int h)
	{
		width = w;
		height = h;
		this.x = x;
		this.y = y;
		count = 0;
		lastStep_x = x;
		lastStep_y = y;
	}
	
	void setProgramm(int[] i)
	{
		programm = i;
	}
	
	void nextStep()
	{
		if (count == programm.length-1) count = 0;
		else count++;
		lastStep_x = x;
		lastStep_y = y;
		isStep = true;
	}
	
	boolean isStep()
	{
		return isStep;
	}
	
	void step(int step)
	{
		
		if (programm != null && isStep)
		{
			switch(programm[count])
			{
			case 0:// Left
				x-=step;
				if (lastStep_x - currentStep > x) isStep = false;
				break;
			case 1: // up
				y+=step;
				if (lastStep_y + currentStep < y) isStep = false;
				break;
			case 2: // rigth
				x+=step;
				if (lastStep_x + currentStep < x) isStep = false;
				break;
			case 3: // down
				y-=step;
				if (lastStep_y - currentStep > y) isStep = false;
				break;
			}
		}
			
	}
	
	public void print()
	{
		System.out.println("width : "+width);
	}
	
	public void setW(int w)
	{
		width = w;
	}

}