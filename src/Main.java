import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

import com.logitech.gaming.LogiLED;

public class Main{

	Board board = new Board();
	private JFrame frame;
	JPanel[][] circles = new JPanel[6][7];
	private JTextField turnTF;
	Color[] colors = {Color.red, Color.orange};

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		LogiLED.LogiLedInit();
		Main window = new Main();
		window.frame.setVisible(true);


	}

	public void paint(Graphics g) {
		g.fillOval(100, 100, 200, 200);
	}

	/**
	 * Create the application.
	 */
	public Main() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 558, 575);
		frame.setTitle("Connect Four");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.getContentPane().setBackground(Color.white);
		frame.getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(42, 124, 469, 402);
		frame.getContentPane().add(panel);
		panel.setLayout(new GridLayout(6, 7, 0, 0));
		
		JLabel winnerLabel = new JLabel();
		winnerLabel.setFont(new Font("Source Sans Pro Black", Font.PLAIN, 18));
		winnerLabel.setBounds(42, 99, 249, 19);
		frame.getContentPane().add(winnerLabel);
		
		JButton newGameBtn = new JButton("New Game");
		newGameBtn.setBackground(Color.white);
		newGameBtn.setForeground(Color.black);
		newGameBtn.setFocusPainted(false);
		newGameBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("----------New Game----------");
				
				board = new Board();
				for (int i = 0; i < circles.length; i++) 
					for (int j = 0; j < circles[i].length; j++)
						((Circle) circles[i][j]).setColor('E');
				turnTF.setText(board.getTurn()+"");
				LogiLED.LogiLedSetLighting(getGColors()[0], getGColors()[1], getGColors()[2]);
				winnerLabel.setText("");
			}
		});
		newGameBtn.setBounds(45, 59, 131, 32);
		frame.getContentPane().add(newGameBtn);
		
		turnTF = new JTextField();
		turnTF.setEditable(false);
		turnTF.setBounds(470, 59, 41, 32);
		turnTF.setText(board.getTurn()+"");
		LogiLED.LogiLedSetLighting(getGColors()[0], getGColors()[1], getGColors()[2]);
		turnTF.setBackground(Color.gray);
		turnTF.setHorizontalAlignment(JTextField.CENTER);
		frame.getContentPane().add(turnTF);
		turnTF.setColumns(10);
		
		//creating circles
		for (int i = 0; i < circles.length; i++) {
			for (int j = 0; j < circles[i].length; j++) {
				//creating canvas
				circles[i][j] = new Circle('E', colors);
				circles[i][j].setLayout(new BorderLayout());
				
				//creating button
				JButton btn = new JButton();
				btn.setOpaque(false);
				
				int clm = j;
				btn.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						int[] cords = board.drop(clm);
						if(cords != null) {
							int dropped_y = cords[0];
							int dropped_x = cords[1];
							
							((Circle) circles[dropped_y][dropped_x]).setColor(board.getBoard()[dropped_y][dropped_x].getColor());
						}else {
							System.out.println("Can't Drop Here");
						}
						
						if(board.isFinished()) {
							if(board.getTurn() == '1') {
								winnerLabel.setForeground(colors[0]);
								winnerLabel.setText("Player1 is The Winner");
							}else {
								winnerLabel.setForeground(colors[1]);
								winnerLabel.setText("Player2 is The Winner");
							}
						}
						
						turnTF.setText(board.getTurn()+"");	
						turnTF.setForeground(board.getTurn() == '1'? colors[0]: colors[1]);
						LogiLED.LogiLedSetLighting(getGColors()[0], getGColors()[1], getGColors()[2]);
						
					}});
				
				//finish and add
				circles[i][j].add(btn);
				panel.add(circles[i][j]);
			}
		}
	}
	
	private int[] getGColors() {
		int[] output = new int[3];
		
		Color color = board.getTurn() == '1' ? colors[0] : colors[1];
		
		output[0] = (int) (color.getRed() * (100.0 / 255));
		output[1] = (int) (color.getGreen() * (100.0 / 255));
		output[2] = (int) (color.getBlue() * (100.0 / 255));
		
		return output;
	}
}

@SuppressWarnings("serial")
class Circle extends JPanel{
	char c;
	Color[] colors;

	Circle(char c, Color[] colors){
		this.c = Character.toUpperCase(c);
		this.colors = colors;
	}

	void setColor(char c){
		this.c = c;
		this.repaint();
	}

	@Override
	public void paint(Graphics g2) {
		Graphics2D g = (Graphics2D)g2;
		
		g2.setColor(Color.white);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		
		if(c == '1')
			g.setColor(colors[0]);
		else if (c == '2')
			g.setColor(colors[1]);
		else
			g.setColor(Color.white);
		g.fillOval(0, 0, this.getWidth()-1, this.getHeight()-1);
		
		g.setColor(Color.black);
		g.drawOval(0, 0, this.getWidth()-1, this.getHeight()-1);
	}
}
