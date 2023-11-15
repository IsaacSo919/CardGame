package Thegame;

public class PlayerThread extends Thread {
	  private Player player;

	  public PlayerThread(Player player) {
	      this.player = player;
	  }

	  @Override
	  public void run() {
	      // game logic for this player
	  }
	}
