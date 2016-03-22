package Game;

import java.util.Random;
import java.util.Stack;


/*
 * Сделать мапу в виде амссив cells
 * и попробовать сгенерировать еще раз
 * 
 */

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
	
	
	volatile byte[][] map = {// x - по вертикали
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
			{BEACH_SHALLOW_3, 	 SHALLOW, BEACH_SHALLOW_1,BEACH_SHALLOW_3, SHALLOW, SHALLOW, SHALLOW, SHALLOW, SHALLOW,SHALLOW ,BEACH_SHALLOW_1,BEACH,BEACH,BEACH,BEACH_SHALLOW_3,SHALLOW,BEACH_SHALLOW_1},
			{BEACH_SHALLOW_IN_4, BEACH_SHALLOW_2,BEACH_SHALLOW_IN_3,BEACH_SHALLOW_IN_4,BEACH_SHALLOW_2, BEACH_SHALLOW_2, BEACH_SHALLOW_2, BEACH_SHALLOW_2, BEACH_SHALLOW_2, BEACH_SHALLOW_2, BEACH_SHALLOW_IN_3,BEACH,BEACH,BEACH,BEACH_SHALLOW_IN_4,BEACH_SHALLOW_2,BEACH_SHALLOW_IN_3},
			/*
			{GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS },
			{GRASS, GRASS_BEACH_IN_1, GRASS_BEACH_4, GRASS_BEACH_4, GRASS_BEACH_IN_2, GRASS_BEACH_IN_1 , GRASS_BEACH_4, GRASS_BEACH_4 , GRASS_BEACH_IN_2 , GRASS},
			{GRASS, GRASS_BEACH_3, BEACH, BEACH, GRASS_BEACH_OUT_4, GRASS_BEACH_OUT_3 , BEACH , BEACH, GRASS_BEACH_1, GRASS},
			{GRASS, GRASS_BEACH_3, BEACH_SHALLOW_IN_1, BEACH_SHALLOW_4, BEACH_SHALLOW_IN_2, BEACH_SHALLOW_IN_1, BEACH_SHALLOW_4, BEACH_SHALLOW_IN_2, GRASS_BEACH_1, GRASS },
			{GRASS, GRASS_BEACH_3, BEACH_SHALLOW_3, SHALLOW, BEACH_SHALLOW_OUT_4, BEACH_SHALLOW_OUT_3, SHALLOW, BEACH_SHALLOW_1,  GRASS_BEACH_1, GRASS },
			{GRASS, GRASS_BEACH_3, BEACH_SHALLOW_3, SHALLOW_DEEP_IN_1, SHALLOW_DEEP_4, SHALLOW_DEEP_4 , SHALLOW_DEEP_IN_2 , BEACH_SHALLOW_1,  GRASS_BEACH_1, GRASS },
			{GRASS, GRASS_BEACH_3, BEACH_SHALLOW_3, SHALLOW_DEEP_3, DEEP , DEEP , SHALLOW_DEEP_1 , BEACH_SHALLOW_1,  GRASS_BEACH_1, GRASS },
			{GRASS, GRASS_BEACH_3, BEACH_SHALLOW_3, SHALLOW_DEEP_3, DEEP , DEEP , SHALLOW_DEEP_1 , BEACH_SHALLOW_1,  GRASS_BEACH_1, GRASS },
			{GRASS, GRASS_BEACH_3, BEACH_SHALLOW_3, SHALLOW_DEEP_IN_4, SHALLOW_DEEP_2, SHALLOW_DEEP_2, SHALLOW_DEEP_IN_3 , BEACH_SHALLOW_1,  GRASS_BEACH_1, GRASS },
			{GRASS, GRASS_BEACH_3, BEACH_SHALLOW_3, SHALLOW, BEACH_SHALLOW_OUT_1, BEACH_SHALLOW_OUT_2, SHALLOW, BEACH_SHALLOW_1,  GRASS_BEACH_1, GRASS },
			{GRASS, GRASS_BEACH_3, BEACH_SHALLOW_IN_4, BEACH_SHALLOW_2, BEACH_SHALLOW_IN_3, BEACH_SHALLOW_IN_4, BEACH_SHALLOW_2, BEACH_SHALLOW_IN_3, GRASS_BEACH_1, GRASS },
			{GRASS, GRASS_BEACH_3, BEACH, BEACH, GRASS_BEACH_OUT_1, GRASS_BEACH_OUT_2 , BEACH , BEACH, GRASS_BEACH_1, GRASS},
			{GRASS, GRASS_BEACH_IN_4, GRASS_BEACH_2, GRASS_BEACH_2, GRASS_BEACH_IN_3, GRASS_BEACH_IN_4 , GRASS_BEACH_2, GRASS_BEACH_2 , GRASS_BEACH_IN_3 , GRASS},
			{GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS, GRASS },
	*/
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
	
	public Map()
	{
		//clear();
	}
	
	public byte[][] getMap()
	{
		return map;
	}
	
	public int getLength()
	{
		return map.length;
	}
	
	private void clear()
	{
		for (int i=0; i<map.length; i++)
			for (int j=0; j<map[i].length; j++)
			{
				//if (i==0 || j==0 || i==map.length-1 || j==map[i].length-1)
				//	map[i][j] = 1;
				//if((i % 2 != 0  && j % 2 != 0) && //если ячейка нечетная по x и y, 
			   //            (i < map.length && j < map.length))
			//		map[i][j] = 0;
			//	else map[i][j] = 1;
					
			}
		
	}
	
	public void generate()
	{
		clear();
		
		Cell startCell = new Cell (1, 1);
		Cell currentCell = startCell;
		Cell neighbourCell;
		
		Stack<Cell> stack = new Stack<>();
		
		Random rnd = new Random(System.currentTimeMillis());

		while (unvisitedCount() > 0)
		{
		    Cell[] Neighbours = getNeighbours(map.length, map.length, startCell);
		    if(Neighbours.length != 0){ //если у клетки есть непосещенные соседи
		        int randNum   = rnd.nextInt(Neighbours.length-1);
		        neighbourCell = Neighbours[randNum]; //выбираем случайного соседа
		        stack.push(neighbourCell); //заносим текущую точку в стек
		        removeWall(currentCell, neighbourCell);//убираем стену между текущей и сосендней точками
		        currentCell = neighbourCell; //делаем соседнюю точку текущей и отмечаем ее посещенной
		        map[startCell.x][startCell.y] = VISITED;
		        
		    }
		    else if(!stack.empty())
		    { //если нет соседей, возвращаемся на предыдущую точку
		        startCell = stack.pop();
		    }
		    else
		    { //если нет соседей и точек в стеке, но не все точки посещены, выбираем случайную из непосещенных
		        currentCell = new Cell(rnd.nextInt(map.length-1), rnd.nextInt(map.length-1));
		    }
		}
		
		/*
		
		
		int p1y =  1+rnd.nextInt(map.length-1);
		int p2y =  1+rnd.nextInt(map.length-1);
		
		for (int i=0; i<map.length-1; i++)
		{
			map[0][i] = 1;
			map[map.length][i] = 2;
		}
		
		for (int i=0; i<map.length-1; i++)
		{
			if (i<p1y) map[i][0] = 1;
			else if (i==p1y) map[i][0] = 0;
			else map[i][0] = 2;
			
			if (i<p2y) map[i][map.length-1] = 1;
			else if (i==p1y) map[i][map.length] = 0;
			else map[i][map.length] = 2;
		}
		/*/
	}
		
	public int unvisitedCount()
	{
		int size = 0;
		
		
		
		return size;
	}
	
	void removeWall(Cell first, Cell second)
	{
	    int xDiff = second.x - first.x;
	    int yDiff = second.y - first.y;
	    int addX, addY;
	    Cell target = new Cell(0,0);
		addX = (xDiff != 0) ? (xDiff / Math.abs(xDiff)) : 0;
	    addY = (yDiff != 0) ? (yDiff / Math.abs(yDiff)) : 0;
		target.x = (byte) (first.x + addX); //координаты стенки
	    target.y = (byte) (first.y + addY);
		map[target.y][target.x] = VISITED;
	}
		
		
	public Cell[] getNeighbours( int width, int height, Cell c)
	{
	    byte i;
	    byte x = c.x;
	    byte y = c.y;
	    Cell up = new Cell(x, (byte) (y - distance));
	    Cell rt = new Cell((byte)(x + distance), y);
	    Cell dw = new Cell(x,(byte) (y + distance));
	    Cell lt = new Cell((byte)(x - distance), y);
	    Cell[] d  =  {dw, rt, up, lt};
	    int size = 0;

	    Cell[] cells = new Cell[4];
	    

	    for(i = 0; i < 4; i++){ //для каждого направдения
	        if(d[i].x > 0 && d[i].x < width && d[i].y > 0 && d[i].y < height){ //если не выходит за границы лабиринта
	            int mazeCellCurrent = map[d[i].y][d[i].x];
	            Cell  cellCurrent     = d[i];
	            if(mazeCellCurrent != WALL && mazeCellCurrent != VISITED){ //и не посещена\является стеной
	                //cells.cells[size] = cellCurrent; //записать в массив;
	                size++;
	            }
	        }
	    }
	    //cells.size = size;
	    return cells;
	}
}
