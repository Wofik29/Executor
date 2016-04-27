package Game;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class Map 
{
	
	public static final byte  GRASS = 0;
	public static final byte  DEEP = 1;
	public static final byte  BEACH = 2;
	public static final byte  SHALLOW = 3;
	public static final byte  GRASS_BEACH_IN_1 = 4;
	public static final byte  GRASS_BEACH_IN_2 = 5;
	public static final byte  GRASS_BEACH_IN_3 = 6;
	public static final byte  GRASS_BEACH_IN_4 = 7;
	public static final byte  GRASS_BEACH_OUT_1 = 8; 
	public static final byte  GRASS_BEACH_OUT_2 = 9;
	public static final byte  GRASS_BEACH_OUT_3 = 10;
	public static final byte  GRASS_BEACH_OUT_4 = 11;
	public static final byte  GRASS_BEACH_1 = 12;
	public static final byte  GRASS_BEACH_2 = 13;
	public static final byte  GRASS_BEACH_3 = 14;
	public static final byte  GRASS_BEACH_4 = 15;
	public static final byte  BEACH_SHALLOW_IN_1 = 16;
	public static final byte  BEACH_SHALLOW_IN_2 = 17;
	public static final byte  BEACH_SHALLOW_IN_3 = 18;
	public static final byte  BEACH_SHALLOW_IN_4 = 19;
	public static final byte  BEACH_SHALLOW_OUT_1 = 20;
	public static final byte  BEACH_SHALLOW_OUT_2 = 21;
	public static final byte  BEACH_SHALLOW_OUT_3 = 22;
	public static final byte  BEACH_SHALLOW_OUT_4 = 23;
	public static final byte  SHALLOW_DEEP_IN_1 = 24;
	public static final byte  SHALLOW_DEEP_IN_2 = 25;
	public static final byte  SHALLOW_DEEP_IN_3 = 26;
	public static final byte  SHALLOW_DEEP_IN_4 = 27;
	public static final byte  SHALLOW_DEEP_OUT_1 = 28;
	public static final byte  SHALLOW_DEEP_OUT_2 = 29;
	public static final byte  SHALLOW_DEEP_OUT_3 = 30;
	public static final byte  SHALLOW_DEEP_OUT_4 = 31;
	public static final byte  SHALLOW_DEEP_1 = 32;
	public static final byte  SHALLOW_DEEP_2 = 33;
	public static final byte  SHALLOW_DEEP_3 = 34;
	public static final byte  SHALLOW_DEEP_4 = 35;
	public static final byte  BEACH_SHALLOW_1 = 36;
	public static final byte  BEACH_SHALLOW_2 = 37;
	public static final byte  BEACH_SHALLOW_3 = 38;
	public static final byte  BEACH_SHALLOW_4 = 39;
	public static final byte SHIP = 40;
	public static final byte JEWEL = 41;
	
	
	static volatile byte[][] texture_map = {// x - по вертикали
			{BEACH_SHALLOW_IN_1, BEACH_SHALLOW_4, BEACH_SHALLOW_4, BEACH_SHALLOW_4,BEACH_SHALLOW_4,BEACH_SHALLOW_4,BEACH_SHALLOW_4,BEACH_SHALLOW_4,BEACH_SHALLOW_4,BEACH_SHALLOW_4,BEACH_SHALLOW_4, BEACH_SHALLOW_IN_2, BEACH_SHALLOW_IN_1,BEACH_SHALLOW_4,BEACH_SHALLOW_4,BEACH_SHALLOW_4, BEACH_SHALLOW_IN_2},
			{BEACH_SHALLOW_3, 	 SHALLOW,SHALLOW, SHALLOW, SHALLOW, SHALLOW, SHALLOW, SHALLOW, SHALLOW, SHALLOW, SHALLOW, BEACH_SHALLOW_1,BEACH_SHALLOW_3,SHALLOW,SHALLOW,SHALLOW,BEACH_SHALLOW_1},
			{BEACH_SHALLOW_3, 	 SHALLOW, BEACH_SHALLOW_OUT_1, BEACH_SHALLOW_2,BEACH_SHALLOW_OUT_2, SHALLOW, BEACH_SHALLOW_OUT_1, BEACH_SHALLOW_2,BEACH_SHALLOW_2,BEACH_SHALLOW_2,BEACH_SHALLOW_2, BEACH_SHALLOW_IN_3,BEACH_SHALLOW_IN_4, BEACH_SHALLOW_2, BEACH_SHALLOW_OUT_2,SHALLOW,BEACH_SHALLOW_1},
			{BEACH_SHALLOW_3, 	 SHALLOW, BEACH_SHALLOW_1, BEACH, BEACH_SHALLOW_3, SHALLOW, BEACH_SHALLOW_1, BEACH_SHALLOW_IN_1, BEACH_SHALLOW_4, BEACH_SHALLOW_4, BEACH_SHALLOW_4, BEACH_SHALLOW_4, BEACH_SHALLOW_4, BEACH_SHALLOW_4, BEACH_SHALLOW_OUT_3,SHALLOW,BEACH_SHALLOW_1},
			{BEACH_SHALLOW_3, 	 SHALLOW, BEACH_SHALLOW_1, BEACH, BEACH_SHALLOW_3, SHALLOW, BEACH_SHALLOW_1, BEACH_SHALLOW_3, SHALLOW, SHALLOW, SHALLOW, SHALLOW, SHALLOW,SHALLOW,SHALLOW,SHALLOW,BEACH_SHALLOW_1},
			{BEACH_SHALLOW_3, 	 SHALLOW, BEACH_SHALLOW_1, BEACH, BEACH_SHALLOW_3, SHALLOW, BEACH_SHALLOW_1, BEACH_SHALLOW_3, SHALLOW, BEACH_SHALLOW_OUT_1,BEACH_SHALLOW_2,BEACH_SHALLOW_2,BEACH_SHALLOW_2,BEACH_SHALLOW_2,BEACH_SHALLOW_OUT_2,SHALLOW,BEACH_SHALLOW_1},
			{BEACH_SHALLOW_3, 	 SHALLOW, BEACH_SHALLOW_1, BEACH, BEACH_SHALLOW_3, SHALLOW, BEACH_SHALLOW_1, BEACH_SHALLOW_3, SHALLOW, BEACH_SHALLOW_1, GRASS_BEACH_OUT_1,GRASS_BEACH_2,GRASS_BEACH_OUT_2,BEACH,BEACH_SHALLOW_3,SHALLOW,BEACH_SHALLOW_1},
			{BEACH_SHALLOW_3, 	 SHALLOW, BEACH_SHALLOW_1, BEACH, BEACH_SHALLOW_3, SHALLOW, BEACH_SHALLOW_1, BEACH_SHALLOW_3, SHALLOW, BEACH_SHALLOW_1, GRASS_BEACH_1,GRASS,GRASS_BEACH_3,BEACH,BEACH_SHALLOW_3,SHALLOW,BEACH_SHALLOW_1},
			{BEACH_SHALLOW_3,	 SHALLOW, BEACH_SHALLOW_1, BEACH, BEACH_SHALLOW_3, SHALLOW, BEACH_SHALLOW_1, BEACH_SHALLOW_3, SHALLOW, BEACH_SHALLOW_1, GRASS_BEACH_OUT_4,GRASS_BEACH_4,GRASS_BEACH_OUT_3,BEACH,BEACH_SHALLOW_3,SHALLOW,BEACH_SHALLOW_1},
			{BEACH_SHALLOW_3, 	 SHALLOW, BEACH_SHALLOW_1, BEACH, BEACH_SHALLOW_3, SHALLOW, BEACH_SHALLOW_1, BEACH_SHALLOW_3, SHALLOW,BEACH_SHALLOW_1,  BEACH,BEACH,BEACH,BEACH,BEACH_SHALLOW_3,SHALLOW,BEACH_SHALLOW_1},
			{BEACH_SHALLOW_3, 	 SHALLOW, BEACH_SHALLOW_1, BEACH, BEACH_SHALLOW_3, SHALLOW, BEACH_SHALLOW_1, BEACH_SHALLOW_3, SHALLOW, BEACH_SHALLOW_1, BEACH,BEACH,BEACH,BEACH,BEACH_SHALLOW_3,SHALLOW,BEACH_SHALLOW_1},
			{BEACH_SHALLOW_3, 	 SHALLOW, BEACH_SHALLOW_1,BEACH_SHALLOW_IN_1, BEACH_SHALLOW_OUT_3, SHALLOW, BEACH_SHALLOW_OUT_4, BEACH_SHALLOW_OUT_3, SHALLOW,BEACH_SHALLOW_OUT_4, BEACH_SHALLOW_IN_2,BEACH,BEACH,BEACH,BEACH_SHALLOW_3,SHALLOW,BEACH_SHALLOW_1},
			{BEACH_SHALLOW_3, 	 SHALLOW, BEACH_SHALLOW_1,BEACH_SHALLOW_3, SHALLOW, SHALLOW, SHALLOW, SHALLOW, SHALLOW,SHALLOW ,BEACH_SHALLOW_1,BEACH,BEACH,JEWEL,BEACH_SHALLOW_3,SHALLOW,BEACH_SHALLOW_1},
			{BEACH_SHALLOW_IN_4, BEACH_SHALLOW_2,BEACH_SHALLOW_IN_3,BEACH_SHALLOW_IN_4,BEACH_SHALLOW_2, BEACH_SHALLOW_2, BEACH_SHALLOW_2, BEACH_SHALLOW_2, BEACH_SHALLOW_2, BEACH_SHALLOW_2, BEACH_SHALLOW_IN_3,BEACH,BEACH,BEACH,BEACH_SHALLOW_IN_4,BEACH_SHALLOW_2,BEACH_SHALLOW_IN_3}
		};
	byte distance = 2;
	
	final byte WALL = 1;
	final byte CELL = 0;
	final byte VISITED = 3;
	
	class Cell
	{
		byte x;
		byte y;
		public Cell(int _x, int _y)
		{
			x = (byte) _x;
			y = (byte) _y;
		}
	}
	
	public byte[][] loadMap(String name) throws Exception
	{
		File file = new File(name);
		if (!file.exists()) throw new Exception("No such file");
		if (!name.substring(  name.lastIndexOf('.'), name.length()-1 ).equals("map"))  throw new Exception("Incorrect file extension");
		
		try
		{
			
			FileReader fr = new FileReader(name);
			
			BufferedReader br = new BufferedReader(fr);
			
			StringBuilder sb = new StringBuilder();
			
			
			int i=0;
			
			if (br.ready()) System.out.println("ready");
			
			while (br.ready())
			{
			//	map_str[i++] = br.readLine().split(":");
				sb.append(br.readLine()).append("\n");
			}
			
			String[] str = sb.toString().split("\n");
			
			String[][] map_str = new String[str.length][100];
			
			for (String s : str)
			{
				map_str[i++]=s.split(":");
			}
			
			br.close();
			fr.close();
			
			texture_map = new byte[map_str.length][ map_str[0].length ];
			
			for (i=0; i<map_str.length; i++)
			{
				for (int j=0; j<map_str[i].length; j++)
				{
					texture_map[i][j] = Byte.parseByte( map_str[i][j]);
				}
				
			}
			
			
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		//map = new byte[1][1];
		
		return texture_map;
	}
	
	public Map()
	{
		//clear();
	}
	
	public byte[][] getMap()
	{
		return texture_map;
	}
	
	public int getLength()
	{
		return texture_map.length;
	}
	
}