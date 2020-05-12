package AiHex.gameMechanics;

import java.util.logging.Level;
import java.util.logging.Logger;

import AiHex.players.ComputerPlayer;
import AiHex.players.Player;
import AiHex.players.PointAndClickPlayer;
import AiHex.hexBoards.GameBoard;
import AiHex.hexBoards.Board;
import AiHex.players.AIPLAYER;
import AiHex.players.ComputerPlayer;

public class GameRunner extends Thread implements Runner {

  private GameBoard board;
  private Player red;
  private Player blue;
  private int currentPlayer = Board.RED;
  private boolean finished = false;
  private volatile boolean stop = false;
  private int gameType;
  private String commentary = "";

  public GameRunner(int size, int redPlayer, String[] redArgs, int bluePlayer, String[] blueArgs) {
    this.board = new GameBoard(size);
    this.red = createPlayer(redPlayer, Board.RED, redArgs);
    this.blue = createPlayer(bluePlayer, Board.BLUE, blueArgs);
  }

  public GameBoard getBoard() {
    return board;
  }

  @Override
  public void run() {
    /*
     * Main running loop
     */
    while (!finished && !stop) {

      boolean moveAccepted = false;

      Move move = null;
      switch (currentPlayer) {
        case Board.RED:
          move = red.getMove();
          break;
        case Board.BLUE:
          move = blue.getMove();
          break;
        default:
          System.err.println("invoking mystery player");
          System.exit(1);
          break;
      }
      try {
        moveAccepted = board.makeMove(move);
      } catch (InvalidMoveException ex) {
        Logger.getLogger(GameRunner.class.getName()).log(Level.SEVERE, null, ex);
      }
      if (!moveAccepted)
        System.out.println("Move was not accepted, passing on...");


      /*
       * move has been accepted
       */

      if (board.checkwin(currentPlayer)) {
        notifyWin(currentPlayer);
        finished = true;
      }


      switch (currentPlayer) {
        case Board.RED:
          this.currentPlayer = Board.BLUE;
          break;
        case Board.BLUE:
          this.currentPlayer = Board.RED;
          break;
        default:
          System.err.println("invoking mystery player");
          System.exit(1);
          break;
      }
    }
  }

  public void notifyWin(int player) {
    this.finished = true;
    java.awt.Toolkit.getDefaultToolkit().beep();
    switch (player) {
      case Board.RED:
        System.out.println("Red wins!");
        announce("Red Wins!");
        break;
      case Board.BLUE:
        System.out.println("Blue wins!");
        break;
    }
  }

  public void stopGame() {
    stop = true;
    System.out.println("Stopped!");
  }

  private Player createPlayer(int type, int colour, String[] args) {
    Player player = null;
    switch (type) {
      case Player.CLICK_PLAYER:
        player = new PointAndClickPlayer(this, colour);
        break;
      case (1):
        player = new AIPLAYER(this, colour);
        break;
      case (4):
        player = new ComputerPlayer(this, colour);
        
      default:
        System.out.println("ERROR - no player or exception");
        break;
    }
    return player;
  }

  public Player getPlayerBlue() {
    return blue;
  }

  public Player getPlayerRed() {
    return red;
  }

  private void announce(String announcement) {
    this.commentary = announcement;
  }

  public String getCommentary() {
    return commentary;
  }
}
