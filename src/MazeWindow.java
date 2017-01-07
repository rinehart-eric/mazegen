import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class MazeWindow extends JFrame {
	private static final long serialVersionUID = 1L;

	private Tile[][] tiles;
	
	private int currentX = 1;
	private int currentY = 1;
	
	public static void main(String[] args) {
		Maze maze = new Maze(16, 30);
		maze.generateMaze();
		
		SwingUtilities.invokeLater(() -> new MazeWindow(maze).setVisible(true));
	}
	
	public MazeWindow(Maze maze) {
		int dispRows = maze.getRowCount() * 2 + 1;
		int dispCols = maze.getColCount() * 2 + 1;
		
		JPanel main = new JPanel(new GridLayout(dispRows, dispCols));
		tiles = new Tile[dispRows][dispCols];
		
		for (int i = 0; i < dispRows; i++) {
			for (int j = 0; j < dispCols; j++) {
				tiles[i][j] = new Tile();
				tiles[i][j].setEnabled(false);
				main.add(tiles[i][j]);
			}
		}
		
		for (int i = 1; i < dispRows; i += 2) {
			for (int j = 1; j < dispCols; j += 2) {
				Maze.Cell cell = maze.getCellAt(dispToCell(i), dispToCell(j));
				
				if (maze.isStartCell(cell.getRow(), cell.getCol())) {
					tiles[i][j].setType(Tile.TileType.START);
				} else if (maze.isEndCell(cell.getRow(), cell.getCol())) {
					tiles[i][j].setType(Tile.TileType.END);
				} else {
					tiles[i][j].setType(Tile.TileType.EMPTY);
				}
				
				if (!cell.hasRightWall()) {
					tiles[i][j+1].setType(Tile.TileType.EMPTY);
				}
				
				if (!cell.hasBottomWall()) {
					tiles[i+1][j].setType(Tile.TileType.EMPTY);
				}
			}
		}
		
		addKeyListener(new KeyAdapter() {
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
				if (tiles[currentX + dX][currentY + dY].type() != Tile.TileType.WALL) {
					tiles[currentX][currentY].setType(Tile.TileType.EMPTY);
					currentX += dX;
					currentY += dY;
					tiles[currentX][currentY].setType(Tile.TileType.OCCUPIED);
				}
			}
		});
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(main, BorderLayout.CENTER);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setSize(dispCols*20, dispRows*20);
	}
	
	private int dispToCell(int coord) {
		return (coord - 1) / 2;
	}
	
	private static class Tile extends JButton {
		private static final long serialVersionUID = 1L;
		
		public static enum TileType {
			WALL(Color.black),
			EMPTY(Color.white),
			OCCUPIED(Color.lightGray),
			START(Color.green),
			END(Color.red);
			
			public Color color;
			
			private TileType(Color color) {
				this.color = color;
			}
		}
		
		private TileType type;
		
		public Tile() {
			setType(TileType.WALL);
		}
		
		public void setType(TileType type) {
			this.type = type;
			setBackground(type.color);
		}
		
		public TileType type() {
			return type;
		}
	}
}
