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

	private JButton[][] tiles;
	
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
		tiles = new JButton[dispRows][dispCols];
		
		KeyAdapter moveAdapter = new KeyAdapter() {
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
		};
		
		for (int i = 0; i < dispRows; i++) {
			for (int j = 0; j < dispCols; j++) {
				tiles[i][j] = new JButton();
				tiles[i][j].setBackground(Color.black);
				tiles[i][j].addKeyListener(moveAdapter);
				main.add(tiles[i][j]);
			}
		}
		
		for (int i = 1; i < dispRows; i += 2) {
			for (int j = 1; j < dispCols; j += 2) {
				Maze.Cell cell = maze.getCellAt((i - 1) / 2, (j - 1) / 2);
				
				if (cell == maze.getStartCell()) {
					tiles[i][j].setBackground(Color.green);
				} else if (cell == maze.getEndCell()) {
					tiles[i][j].setBackground(Color.red);
				} else {
					tiles[i][j].setBackground(Color.white);
				}
				
				if (!cell.hasRightWall()) {
					tiles[i][j+1].setBackground(Color.white);
				}
				
				if (!cell.hasBottomWall()) {
					tiles[i+1][j].setBackground(Color.white);
				}
			}
		}
		
		addKeyListener(moveAdapter);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(main, BorderLayout.CENTER);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setSize(dispCols*20, dispRows*20);
	}
}
