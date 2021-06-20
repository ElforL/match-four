
public class Board {
	private DiscSpot[][] board = new DiscSpot[6][7];
	private boolean isFinished;
	private char turn;

	// Constructor
	public Board() {
		//sets whose turn
		turn = Math.random() >= 0.5 ? '1' : '2';
		System.out.println(turn + " turn");

		//initialize the board spots
		for (int i = 0; i < board.length; i++)
			for (int j = 0; j < board[i].length; j++)
				board[i][j] = new DiscSpot();

		isFinished = false;
	}


	// Getters
	public DiscSpot[][] getBoard() {
		return board;
	}
	
	public char getTurn() {
		return turn;
	}
	
	public boolean isFinished() {
		return isFinished;
	}


	/*
	 * Switches turns between 1 and 2
	 */
	private void finishTurn() {
		if(turn == '1')
			turn = '2';
		else
			turn = '1';

	}

	/*
	 * drops a disc in a given column
	 */
	public int[] drop(int clm) {
		//skip if game is finished
		if(isFinished) return null;
		
		/*
		 * search for empty spot from bottom to up
		 * and place a disc at first empty spot
		 * then check for a connected four
		 * return the coordinates of places slide
		 */
		for(int i = board.length - 1; i >= 0; --i) {
			if(board[i][clm].isEmpty()) {
				board[i][clm].setColor(turn);
				if(check(i, clm))
					finishGame();
				else
					finishTurn();
				return new int[] {i, clm};
			}
		}
		//no empty spots found
		System.out.println("full column");
		return null;
	}

	/*
	 * checks if the spot at the given index makes a connected four
	 */
	private boolean check(int y, int x) {
		int count = 1;
		//checking downwards
		for(int i = y+1; i < board.length; i++) {
			if(board[i][x].getColor() == board[y][x].getColor())
				count++;
			else
				break;
			
			if(count >= 4) {
				System.out.println("Down");
				return true;
			}
		}

		////checking sideways
		count = 0;
		//right
		for (int i = x; i < board[y].length; i++) {
			if(board[y][i].getColor() == board[y][x].getColor())
				count++;
			else
				break;
			
			if(count >= 4) {
				System.out.println("Side-Right");
				return true;
			}
		}
		//left
		for (int i = x-1; i >= 0; i--) {
			if(board[y][i].getColor() == board[y][x].getColor())
				count++;
			else
				break;

			if(count >= 4) {
				System.out.println("Side-Left");
				return true;
			}
		}
		
		////Major diagonal
		count = 1;
		
		//Downwards
		for (int i = 1; y + i < board.length && x + i < board[y + i].length; i++) {
			
			if(board[y + i][x + i].getColor() == board[y][x].getColor()) {
				System.out.println(count+" + 1");
				count++;
			}else
				break;
			
			if(count >= 4) {
				System.out.println("Major-Down");
				return true;
			}
		}
		//Upwards
		for (int i = 1; y - i >= 0 && x - i >= 0; i++) {
			if(board[y - i][x - i].getColor() == board[y][x].getColor())
				count++;
			else
				break;
			
			if(count >= 4) {
				System.out.println("Major-Up");
				return true;
			}
		}
		
		////Minor diagonal
		count = 1;
		
		//Downwards
		for (int i = 1; y + i < board.length && x - i >= 0; i++) {
			if(board[y + i][x - i].getColor() == board[y][x].getColor())
				count++;
			else
				break;
			
			if(count >= 4) {
				System.out.println("Minor Down");
				return true;
			}
		}
		//Upwards
		for (int i = 1; y - i >= 0 && x + i < board[y - i].length; i++) {
			if(board[y - i][x + i].getColor() == board[y][x].getColor())
				count++;
			else
				break;
			
			if(count >= 4)
				return true;
		}

		return false;
	}//check()

	
	/*
	 * called to end the game 
	 */
	private void finishGame() {
		isFinished = true;
		System.out.printf("\n%s is The Winner\n", (turn == '1' ? "Player 1": "Player2") );
	}


}


////////////////////////////////////////////////////////////////////////////////////////////////////////
class DiscSpot{
	private char color;

	// Constructors
	/*
	 * set the color to R = red , Y = yellow, or E = empty
	 * and throw an exception if any other char was given 
	 */
	DiscSpot(char c) {
		c = Character.toUpperCase(c);
		if(c == '1' || c == '2' || c == 'E')
			color = c;
		else
			throw new IllegalArgumentException("Invalid input color input must be 1, 2, or E");
	}

	/*
	 * to creates an empty spot
	 */
	DiscSpot(){
		color = 'E';
	}


	// Getters
	char getColor() {
		return color;
	}

	boolean isEmpty() {
		return (color == 'E');
	}
	

	/*
	 * sets the color for an *empty spot*
	 */
	void setColor(char c) {
		if(color != 'E')
			throw new IllegalArgumentException("spot already taken, can't switch spot color");
		else if(c == '1' || c == '2')
			color = c;
		else
			throw new IllegalArgumentException("Invalid input color input must be 1 or 2");
	}
}