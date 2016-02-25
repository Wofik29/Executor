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
	volatile byte[][] map = new byte[10][10];
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
		clear();
	}
	
	public byte[][] getMap()
	{
		return map;
	}
	
	private void clear()
	{
		for (int i=0; i<map.length; i++)
			for (int j=0; j<map[i].length; j++)
			{
				//if (i==0 || j==0 || i==map.length-1 || j==map[i].length-1)
				//	map[i][j] = 1;
				if((i % 2 != 0  && j % 2 != 0) && //если ячейка нечетная по x и y, 
			               (i < map.length && j < map.length))
					map[i][j] = 0;
				else map[i][j] = 1;
					
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
