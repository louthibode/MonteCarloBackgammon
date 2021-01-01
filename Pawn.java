
public class Pawn {
	
	private int position;
	private int turnPlayer;
	
	public Pawn(int position, int turnPlayer){
		this.position = position;
		this.turnPlayer = turnPlayer;
	}
	
	public Pawn(){
		this.position=-1;
		this.turnPlayer=0;
	}
	
	public int getPos(){
		return this.position;
	}
	
	public int getTurn(){
		return this.turnPlayer;
	}
	
	public String toString(){
		return "Pawn- Position: " + this.position + " Player: " + this.turnPlayer;
	}

}
