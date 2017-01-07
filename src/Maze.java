import java.util.Stack;
import java.util.ArrayList;
import java.lang.Math;

public class Maze {
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
	}

	public void generateMaze() {
		currentCell = cells[0][0];
		currentCell.markVisited();
		
		int[] dRow = { 0, 0, 1, -1 };
		int[] dCol = { 1, -1, 0, 0 };
		while (anyCellsUnvisited()) {
			ArrayList<Cell> unvisitedAdjacent = new ArrayList<>();
			for (int i = 0; i < dRow.length; i++) {
				int adjRow = currentCell.getRow() + dRow[i];
				int adjCol = currentCell.getCol() + dCol[i];
				if (inBounds(adjRow, adjCol) && !cells[adjRow][adjCol].visited()) {
					unvisitedAdjacent.add(cells[adjRow][adjCol]);
				}
			}
			
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
				} while (currentCell.visited());
			}
		}
	}
	
	private boolean inBounds(int row, int col) {
		return row >= 0 && col >= 0 && row < rows && col < cols;
	}

	private boolean anyCellsUnvisited() {
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				if (!cells[i][j].visited()) {
					return true;
				}
			}
		}
		return false;
	}

	private void removeCommonWall(Cell cell1, Cell cell2) {
		if (cell1.getRow() < cell2.getRow()) {
			cell1.removeBottomWall();
		} else if (cell1.getCol() < cell2.getCol()) {
			cell1.removeRightWall();
		} else if (cell1.getRow() > cell2.getRow()) {
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
	
	public boolean isStartCell(int x, int y) {
		return x == 0 && y == 0;
	}
	
	public boolean isEndCell(int x, int y) {
		return x == rows - 1 && y == cols - 1;
	}
	
	public static class Cell {
		private int row;
		private int col;
		private boolean visited = false;
		private boolean rightWall = true;
		private boolean bottomWall = true;

		public Cell(int row, int col) {
			this.row = row;
			this.col = col;
		}

		public int getRow() {
			return row;
		}

		public int getCol() {
			return col;
		}

		public boolean visited() {
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