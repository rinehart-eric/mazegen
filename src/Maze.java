import javax.swing.JFrame;
import java.util.Stack;
import java.util.ArrayList;
import java.lang.Math;

public class Maze extends JFrame {
	private static final long serialVersionUID = 1L;

	private int rows;
	private int cols;
	private Cell[][] cells;
	private Cell currentCell;
	private Stack<Cell> cellStack;

	public Maze(int rows, int cols) {
		this.rows = rows;
		this.cols = cols;
		cells = new Cell[rows][cols];
		cellStack = new Stack<Cell>();
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				cells[i][j] = new Cell(i, j);
			}
		}
		generateMaze();
	}

	public void generateMaze() {
		currentCell = cells[0][0];
		currentCell.markVisited();
		while (anyCellsUnvisited()) {
			ArrayList<Cell> unvisitedAdjacent = new ArrayList<>();
			if (currentCell.getX() > 0 && cells[currentCell.getX()-1][currentCell.getY()].getVisited() == false)
				unvisitedAdjacent.add(cells[currentCell.getX()-1][currentCell.getY()]);
			if (currentCell.getY() > 0 && cells[currentCell.getX()][currentCell.getY()-1].getVisited() == false)
				unvisitedAdjacent.add(cells[currentCell.getX()][currentCell.getY()-1]);
			if (currentCell.getX() < rows - 1 && cells[currentCell.getX()+1][currentCell.getY()].getVisited() == false)
				unvisitedAdjacent.add(cells[currentCell.getX()+1][currentCell.getY()]);
			if (currentCell.getY() < cols - 1 && cells[currentCell.getX()][currentCell.getY()+1].getVisited() == false)
				unvisitedAdjacent.add(cells[currentCell.getX()][currentCell.getY()+1]);
			if (unvisitedAdjacent.size() > 0 && currentCell != cells[rows - 1][cols - 1]) {
				Cell newCell = unvisitedAdjacent.get((int)(Math.random() * unvisitedAdjacent.size()));
				cellStack.push(currentCell);
				removeCommonWall(currentCell, newCell);
				currentCell = newCell;
				currentCell.markVisited();
			} else if (!cellStack.empty()) {
				currentCell = cellStack.pop();
			} else {
				do {
					currentCell = cells[(int)(Math.random() * rows)][(int)(Math.random() * cols)];
				} while (currentCell.getVisited());
			}
		}
	}

	private boolean anyCellsUnvisited() {
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				if (!cells[i][j].getVisited()) {
					return true;
				}
			}
		}
		return false;
	}

	private void removeCommonWall(Cell cell1, Cell cell2) {
		if (cell1.getX() < cell2.getX()) {
			cell1.removeBottomWall();
		} else if (cell1.getY() < cell2.getY()) {
			cell1.removeRightWall();
		} else if (cell1.getX() > cell2.getX()) {
			cell2.removeBottomWall();
		} else {
			cell2.removeRightWall();
		}
	}
	
	public int getRowCount() {
		return rows;
	}
	
	public int getColCount() {
		return cols;
	}
	
	public Cell getCellAt(int row, int col) {
		return cells[row][col];
	}
	
	public Cell getStartCell() {
		return getCellAt(0, 0);
	}
	
	public Cell getEndCell() {
		return getCellAt(rows - 1, cols - 1);
	}
	
	public static class Cell {
		private int x;
		private int y;
		private boolean visited = false;
		private boolean rightWall = true;
		private boolean bottomWall = true;

		public Cell(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public int getX() {
			return x;
		}

		public int getY() {
			return y;
		}

		public boolean getVisited() {
			return visited;
		}

		public void markVisited() {
			visited = true;
		}

		public boolean hasRightWall() {
			return rightWall;
		}

		public void removeRightWall() {
			rightWall = false;
		}

		public boolean hasBottomWall() {
			return bottomWall;
		}

		public void removeBottomWall() {
			bottomWall = false;
		}
	}
}