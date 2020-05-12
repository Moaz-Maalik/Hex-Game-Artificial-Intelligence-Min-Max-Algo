package AiHex.players;

import AiHex.gameMechanics.Runner;

import java.awt.Point;
import java.util.ArrayList;

import AiHex.hexBoards.Board;
import AiHex.gameMechanics.Move;
import java.util.Random;

public class AIPLAYER implements Player {
	private Runner game = null;
	private int colour = 0;

	public AIPLAYER(Runner game, int colour) {
		this.game = game;
		this.colour = colour;
	}

  public Move getMove() {
		switch (colour) {
		case Board.RED:
			System.out.print("Red move: ");
			break;
		case Board.BLUE:
			System.out.print("Blue move: ");
			break;
		}
                
                Random rand = new Random();
                
                int x;
                int y;
                

		while(true)
                {
                	int rand1 = rand.nextInt();
                	int rand2 = rand.nextInt();
                
                if(rand1 < 0){
                	rand1 *= -1;
				}
				if(rand2 < 0){
					rand2 *= -1;
				}
                
                
                
                x =  rand1% game.getBoard().getSize();
				y = rand2 % game.getBoard().getSize();
                    
                    if(game.getBoard().get(x, y)==Board.BLANK)
                    {
                        break;
                    }
                    
                
                }
                        try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			game.getBoard().setSelected(x,y);

			Point choice = game.getBoard().getSelected();

		Move move = new Move(colour, choice.x, choice.y);

		game.getBoard().setSelected(null);
		return move;
	}

	public ArrayList<Board> getAuxBoards() {
		return new ArrayList<Board>();
	}
}
