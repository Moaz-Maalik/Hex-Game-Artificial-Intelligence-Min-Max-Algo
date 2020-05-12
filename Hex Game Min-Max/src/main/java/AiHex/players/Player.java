package AiHex.players;

import java.util.ArrayList;

import AiHex.hexBoards.Board;
import AiHex.gameMechanics.Move;

public interface Player {

	public static final int CLICK_PLAYER = 3;
	public static final int AIPLayer = 1;
	public static final int ComputerPlayer = 4;
        
	public static final String CLICK_DEFAULT_ARGS = "n/a";
        
	public static final String CLICK_DEFAULT_ARGS2 = "n/a";

	public static final String CLICK_DEFAULT_ARGS3 = "n/a";

	public static final String[] playerList = {"Human Player","AIPLAYER", "ComputerPlayer"};
	public static final int[] playerIndex = {  CLICK_PLAYER ,AIPLayer, ComputerPlayer};

  public static final String[] argsList = { CLICK_DEFAULT_ARGS,CLICK_DEFAULT_ARGS2, CLICK_DEFAULT_ARGS3};
	public Move getMove();

	public ArrayList<Board> getAuxBoards();
}
