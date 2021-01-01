//  Registers the origin and the destination of a move
public class Move {
	
	private int orig;
	private int dest;
	private int playerTurn;
	private int idxDice;
	private int nbUsed;
	
	public Move(int orig, int dest, int playerTurn, int idxDice, int nbUsed){
		this.orig = orig;
		this.dest = dest;
		// 24 for 1 jail, 25 for 2 jail?
		this.playerTurn = playerTurn;
		this.idxDice = idxDice;
		this.nbUsed = nbUsed;
	}
	
	public int getOrig(){
		return this.orig;
	}
	
	public int getDest(){
		return this.dest;
	}
	
	public int getTurn(){
		return this.playerTurn;
	}
	
	public int getIdxDice(){
		return this.idxDice;
	}
	
	public int getNbUsed(){
		return this.nbUsed;
	}
	
	public String toString(){
		return "Move- Destination: " + this.dest + " Origin: " + this.orig + " Played by: " + this.playerTurn + "\n";
	}

}
