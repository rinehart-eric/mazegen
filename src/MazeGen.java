import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.GridLayout;
import java.awt.Color;
import java.awt.event.*;
import java.util.Stack;
import java.util.ArrayList;
import java.lang.Math;
public class MazeGen extends JFrame {
  public class Cell {
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
  
  private class TAdapter extends KeyAdapter {
    public void keyPressed(KeyEvent e) {
      int dX = 0, dY = 0;
      switch (e.getKeyCode()) {
        case KeyEvent.VK_UP:
          dX = -1;
          break;
        case KeyEvent.VK_DOWN:
          dX = 1;
          break;
        case KeyEvent.VK_RIGHT:
          dY = 1;
          break;
        case KeyEvent.VK_LEFT:
          dY = -1;
          break;
      }
      if (tiles[currentX + dX][currentY + dY].getBackground() != Color.black) {
        tiles[currentX][currentY].setBackground(Color.white);
        currentX += dX;
        currentY += dY;
        tiles[currentX][currentY].setBackground(Color.darkGray);
      }
    }
  }
  
  private int rows;
  private int cols;
  private Cell[][] cells;
  private Cell currentCell;
  private Cell startCell;
  private Cell endCell;
  private int currentX = 1;
  private int currentY = 1;
  private Stack<Cell> cellStack;
  private JButton[][] tiles;
  
  public MazeGen(int rows, int cols) {
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
    int dispRows = rows * 2 + 1;
    int dispCols = cols * 2 + 1;
    JPanel main = new JPanel(new GridLayout(dispRows, dispCols));
    tiles = new JButton[dispRows][dispCols];
    for (int i = 0; i < dispRows; i++) {
      for (int j = 0; j < dispCols; j++) {
        tiles[i][j] = new JButton();
        tiles[i][j].setBackground(Color.black);
        tiles[i][j].addKeyListener(new TAdapter());
        main.add(tiles[i][j]);
      }
    }
    for (int i = 1; i < dispRows; i += 2) {
      for (int j = 1; j < dispCols; j += 2) {
        if (cells[(i-1)/2][(j-1)/2] == startCell)
          tiles[i][j].setBackground(Color.green);
        else if (cells[(i-1)/2][(j-1)/2] == endCell)
          tiles[i][j].setBackground(Color.red);
        else
          tiles[i][j].setBackground(Color.white);
        if (!cells[(i-1)/2][(j-1)/2].hasRightWall())
          tiles[i][j+1].setBackground(Color.white);
        if (!cells[(i-1)/2][(j-1)/2].hasBottomWall())
          tiles[i+1][j].setBackground(Color.white);
      }
    }
    addKeyListener(new TAdapter());
    getContentPane().add(main, "Center");
    setSize(dispCols*20, dispRows*20);
    setVisible(true);
  }
  
  public void generateMaze() {
    currentCell = cells[0][0];
    startCell = currentCell;
    endCell = cells[rows-1][cols-1];
    currentCell.markVisited();
    while (hasUnvisited()) {
      ArrayList<Cell> unvisitedAdjacent = new ArrayList<Cell>(0);
      if (currentCell.getX() > 0 && cells[currentCell.getX()-1][currentCell.getY()].getVisited() == false)
        unvisitedAdjacent.add(cells[currentCell.getX()-1][currentCell.getY()]);
      if (currentCell.getY() > 0 && cells[currentCell.getX()][currentCell.getY()-1].getVisited() == false)
        unvisitedAdjacent.add(cells[currentCell.getX()][currentCell.getY()-1]);
      if (currentCell.getX() < rows - 1 && cells[currentCell.getX()+1][currentCell.getY()].getVisited() == false)
        unvisitedAdjacent.add(cells[currentCell.getX()+1][currentCell.getY()]);
      if (currentCell.getY() < cols - 1 && cells[currentCell.getX()][currentCell.getY()+1].getVisited() == false)
        unvisitedAdjacent.add(cells[currentCell.getX()][currentCell.getY()+1]);
      if (unvisitedAdjacent.size() > 0 && currentCell != cells[rows-1][cols-1]) {
        Cell newCell = unvisitedAdjacent.get((int)(Math.random() * unvisitedAdjacent.size()));
        cellStack.push(currentCell);
        removeCommonWall(currentCell, newCell);
        currentCell = newCell;
        currentCell.markVisited();
      }
      else if (!cellStack.empty()) {
        currentCell = cellStack.pop();
      }
      else {
        do {
          currentCell = cells[(int)(Math.random() * rows)][(int)(Math.random() * cols)];
        } while (currentCell.getVisited());
      }
    }
  }
  
  public boolean hasUnvisited() {
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        if (!cells[i][j].getVisited()) {
          return true;
        }
      }
    }
    return false;
  }
  
  public void removeCommonWall(Cell cell1, Cell cell2) {
    if (cell1.getX() < cell2.getX())
      cell1.removeBottomWall();
    else if (cell1.getY() < cell2.getY())
      cell1.removeRightWall();
    else if (cell1.getX() > cell2.getX())
      cell2.removeBottomWall();
    else
      cell2.removeRightWall();
  }
  
  public static void main(String[] args) {
	  new MazeGen(12, 30);
  }
}