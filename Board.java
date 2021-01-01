//  Representation of the board
import java.util.*;

public class Board {
	
	private ArrayList<Stack<Pawn>> boardState;
	private int playerTurn;
	private Stack<Pawn> prison1;
	private Stack<Pawn> prison2;
	private boolean gameWon;
	private ArrayList<Integer> diceR;
	private int countW;
	private int countB;
	private ArrayList<String> move1;
	private ArrayList<String> move2;
	//private double startTime;
	
	public Board(){
		this.boardState = this.initBoard();
		this.playerTurn = 1;
		this.prison1 = new Stack<Pawn>();
		this.prison2 = new Stack<Pawn>();
		this.gameWon = false;
		this.diceR = diceRoll();
		this.countW = 15;
		this.countB = 15;
		this.move1 = new ArrayList<String>();
		this.move2 = new ArrayList<String>();
		//this.startTime = System.currentTimeMillis();
		
	}
	
	//  Getter methods, mostly without use
	//  Except for getGameWon which will stop the main stage
	public ArrayList<Stack<Pawn>> getBoardState(){
		return this.boardState;
	}
	
	public int getTurn(){
		return this.playerTurn;
	}
	
	public Stack<Pawn> getPris1(){
		return this.prison1;
	}
	
	public Stack<Pawn> getPris2(){
		return this.prison2;
	}
	
	public boolean getGameWon(){
		return this.gameWon;
	}
	
	public ArrayList<Integer> getDiceRoll(){
		return this.diceR;
	}
	
	public ArrayList<String> getMoves(int playerNum){
		
		ArrayList<String> toRet = new ArrayList<String>();
		
		if(playerNum==1){
			toRet = this.move1;
		}
		else{
			toRet = this.move2;
		}
		
		return toRet;
	}
	//  Real schmdeal
	//  Returns the initial state of the board
	public ArrayList<Stack<Pawn>> initBoard(){
		
		ArrayList<Stack<Pawn>> initBoard = new ArrayList<Stack<Pawn>>(24);
		
		for (int i=0; i<24; i++){
			
			int k = 0;
			int piece = 0;
			
			//  Board build condition for white
			if(i==0){
				piece=1;
				k=2;
			}
			else if(i==11){
				piece=1;
				k=5;
			}
			else if(i==16){
				piece=1;
				k=3;
			}
			else if(i==18){
				piece=1;
				k=5;
			}
			
			//  Board build cond for black
			else if(i==5){
				piece=2;
				k=5;
			}
			else if(i==7){
				piece=2;
				k=3;
			}
			else if(i==12){
				piece=2;
				k=5;
			}
			else if(i==23){
				piece=2;
				k=2;
			}
			
			Stack<Pawn> row = new Stack<Pawn>();
			
			for(int j=0; j<k; j++){
				row.push(new Pawn(i, piece));
			}
			
			initBoard.add(row);
			
		}
		
		return initBoard;	
	}
	
	//  Determines whether the prison of each participants is empty or not
	private boolean isPrisEmpty(){
		boolean isEmp = false;
		
		if(this.playerTurn==1) isEmp = this.prison1.isEmpty();
		else{
			isEmp = this.prison2.isEmpty();
		}
		
		return isEmp;
	}
	
	//  Returns if there are still pawns going home on the board 
	//  Inside a if loop with prison: no need to recheck here
	//  BUT IMPORTANT TO LEAVE IT IN THERE
	public boolean isExit(){
		
		boolean isEx = true;
		
		int start = 0;
		int end = 18;
		
		if(this.playerTurn==2){
			
			start=6;
			end=24;
		}
		
		for(int i=start; i<end; i++){
			
			Stack<Pawn> temp = this.boardState.get(i);
			
			if(!temp.isEmpty() && temp.peek().getTurn()==this.playerTurn){
				isEx = false;
			}
		}
		
		return isEx;
	}
	
	//  Method to return a random diceRoll with two values.
	//  Or four if both dices are equivalent
	private ArrayList<Integer> diceRoll(){
		
		ArrayList<Integer> diceR = new ArrayList<Integer>(2);
		
		Random r = new Random();
		//  Line for the dice
		int r1 = r.nextInt((6-1)+1) + 1;
		int r2 = r.nextInt((6-1)+1) + 1;
		
		if(r1==r2){
			for(int i=0; i<4; i++){
				diceR.add(r1);
			}
		}
		else{
			diceR.add(r1);
			diceR.add(r2);
		}
		
		return diceR;
	}
	
	//  WHERE IS THE EATING MECHANISM FOR PRISON EXIT!!!!
	public ArrayList<Move> getPrisMoves(){
		ArrayList<Move> moves = new ArrayList<Move>();
		
		for(int i=0; i<this.diceR.size(); i++){
			
			int prisLoc = 24;
			int rollIdx = this.diceR.get(i)-1;
			
			if(this.playerTurn==2){
				prisLoc = 25;
				rollIdx = 23-rollIdx;
			}
			
			if(isDestSafe(rollIdx)){
				moves.add(new Move(prisLoc, rollIdx, this.playerTurn, i, 1));
			}
		}
		
		return moves;
	}
	
	public ArrayList<Move> getHomingMoves(){
		ArrayList<Move> homeMoves = new ArrayList<Move>();
		
		int playerT = this.playerTurn;
		
		for(int j=0; j<this.boardState.size(); j++){
			
			//  Init pawn stack that we'll use.
			Stack<Pawn> temp = this.boardState.get(j);
			
			if(!temp.isEmpty() && temp.peek().getTurn()==playerT){
				
				int sum = 0;
				
				for(int i=0; i<this.diceR.size(); i++){
					
					int roll = this.diceR.get(i);
					int turnMult = 1;
					sum += roll;
					
					if(playerT==2) turnMult = -1;
					
					int dest = j+turnMult*roll;
					
					if(inBounds(dest) && isDestSafe(dest)) homeMoves.add(new Move(j, dest, playerT, i, 1));
					
					
					if(i!=0){
						int destSum = j+turnMult*sum;
						
						if(inBounds(destSum) && isDestSafe(destSum)) homeMoves.add(new Move(j, destSum, playerT, i, i+1));
						
					}
					
				}
			}
			
		}
		
		return homeMoves;
	}
	
	private boolean inBounds(int dest){
		boolean inB = false;
		
		if(dest>=0 && dest<=23){
			inB = true;
		}
		
		return inB;
	}
	
	//  Used to check if the destination is safe
	private boolean isDestSafe(int dest){
		boolean destSafe = false;
		
		Stack<Pawn> temp = this.boardState.get(dest);
		
		if(temp.size()<=1 || temp.peek().getTurn()==this.playerTurn) destSafe = true;
		
		return destSafe;
	
	}
	
	//  GET ALL MOVES TO BE PLAYED DURING THE EXIT
	public ArrayList<Move> getExitMove(){
		
		ArrayList<Move> exitMoves = new ArrayList<Move>();
		int playerT = this.playerTurn;
		int maxIdx = this.getMaxIdx();
		
		for(int i=0; i<6; i++){
			int idx=i;
			if(playerT==1) idx=23-i;
			
			Stack<Pawn> temp = this.boardState.get(idx);
			
			if(!temp.isEmpty() && temp.peek().getTurn()==playerT){
				
				for(int j=0; j<this.diceR.size() && j<2; j++){
					int roll = this.diceR.get(j)-1;
					
					if(i==roll || (i<roll && i==maxIdx)){
						exitMoves.add(new Move(idx, 26, playerT, j, 1));
					}
				}
			}
		}
		
		
		return exitMoves;
	}
	
	//  USED TO GET MAX IDX IN CASE OF EXIT SITUATION
	private int getMaxIdx(){
		int maxIdx = 0;
		
		for(int i=0; i<6; i++){
			int idx = i;
			
			if(this.playerTurn==1) idx = 23-i;
			
			Stack<Pawn> temp = this.boardState.get(idx);
			
			if(!temp.isEmpty() && temp.peek().getTurn()==this.playerTurn){
				maxIdx = i;
			}
		}
		
		return maxIdx;
	}
	
	public ArrayList<Move> getMoves(){
		ArrayList<Move> moves = new ArrayList<Move>();
		
		if(!this.isPrisEmpty()){
			moves.addAll(this.getPrisMoves());
		}
		else{
			moves.addAll(this.getHomingMoves());
			
			if(this.isExit()){
				moves.addAll(this.getExitMove());
			}
		}
		return moves;
	}
	
	//Takes a move, processes it
	public void applyMove(Move toApp){
		
		int origin = toApp.getOrig();
		int dest = toApp.getDest();
		int diceIdx = toApp.getIdxDice();
		int nbUsed = toApp.getNbUsed();
		
		//  A PAWN IS MOVING OUT OF PRISON YEA!
		if(origin==24 || origin==25){
			
			Pawn toMove = new Pawn();
			
			if(this.playerTurn==1) toMove = this.prison1.pop();
			else{
				toMove = this.prison2.pop();
			}
			
			Stack<Pawn> destStack = this.boardState.get(dest);
			
			if(this.isEaten(dest)){
				Pawn eaten = destStack.pop();
				
				if(this.playerTurn==1) this.prison2.push(eaten);
				else{
					this.prison1.push(eaten);
				}
			}
			
			destStack.push(toMove);
		}
		else if(dest==26){
			this.boardState.get(origin).pop();
			
			if(this.playerTurn==1) this.countW--;
			else{
				this.countB--;
			}
		}
		else{
			Pawn toMove = this.boardState.get(origin).pop();
			
			if(isEaten(dest)){
				Pawn eaten = this.boardState.get(dest).pop();
				
				if(this.playerTurn==1) this.prison2.push(eaten);
				else{
					this.prison1.push(eaten);
				}
			}
			
			this.boardState.get(dest).push(toMove);
		}
		
		this.remRoll(diceIdx, nbUsed);
		
	}
	
	//Removes from the dice array the dice that has just been used
	private void remRoll(int diceIdx, int nbUsed){
		
		if(nbUsed == 1){
			this.diceR.remove(diceIdx);
		}
		else{
			for(int i=0; i<nbUsed; i++){
				this.diceR.remove(0);
			}
		}
	}
	
	//  Checks whether a pawn is getting eaten on the move
	public boolean isEaten(int dest){
		boolean isEat = false;
		Stack<Pawn> temp = this.boardState.get(dest);
		
		if(temp.size()==1 && temp.peek().getTurn()!=this.playerTurn){
			isEat = true;
		}
	
		return isEat;
	}
	
	private void playTurn(Scanner getSel){
		
		Random r = new Random();
		
		//System.out.println(this);
		//System.out.println(this.diceR);
		//double turnTime = System.currentTimeMillis();
		
		//int i = 0;
		
		while(this.diceR.size()>0 && !this.isGameWon()){
			//double startIn = System.currentTimeMillis();
			
			//double startMoves = System.currentTimeMillis();
			ArrayList<Move> moves = this.getMoves();
			//double endMoves = System.currentTimeMillis();
			//System.out.println(i + "th get move took : " + (endMoves-startMoves) + "ms.");
			
			//double startVaria = System.currentTimeMillis();
			if(moves.size()==0) break;
			
			//System.out.println("Which move would you like to select?");
			
			int moveSel = r.nextInt((moves.size()-1-0)+1) + 0;
			//int moveSel = getSel.nextInt();
			
			Move selec = moves.get(moveSel);
			
			//double endVaria = System.currentTimeMillis();
			//System.out.println(i + "th varia took : " + (endVaria-startVaria) + "ms.");
			
			//double startToRec = System.currentTimeMillis();
			if(this.playerTurn==1){
				this.move1.add(this.toRec(selec));
			}
			else{
				this.move2.add(this.toRec(selec));
			}
			//double endToRec = System.currentTimeMillis();
			//System.out.println(i + "th toRec took : " + (endToRec-startToRec) + "ms.");
			
			//double startApply = System.currentTimeMillis();
			this.applyMove(selec);
			//double endApply = System.currentTimeMillis();
			//System.out.println(i + "th apply took : " + (endApply-startApply) + "ms.");
			
			//double endIn = System.currentTimeMillis();
			
			//System.out.println("The " + i + "th iteration took " + (endIn-startIn) + "ms.");
			//i++;
			
		}
		
		//double endTurn = System.currentTimeMillis();
		
		//System.out.println("This turn lasted: " + (endTurn-turnTime) + "ms.");
		
		//if((endTurn-turnTime)>100){
		//	System.out.println("LONG ASS TURN");
		//	int[] crash = {1,2,3};
		//	System.out.println(crash[3000]);
		//} 
		
		this.diceR = this.diceRoll();
		this.changeTurn();
		
		//System.out.println("");
		
	}
	
	//  METHOD USED TO PLAY THE GAME
	public void playGame(){
		
		Scanner getSel = new Scanner(System.in);
		boolean overTime = false;
		
		while(!this.isGameWon()){
			this.playTurn(getSel);
			
		}
		
		if(overTime){
			this.playerTurn=0;
		}
		else{
			this.changeTurn();
		}
		
		
		
		//System.out.println("CONGRATULATIONS!  PLAYER " + this.playerTurn + " WON IN STRIDES!");
		getSel.close();
	}
	
	public boolean isGameWon(){
		boolean won = false;
		
		if(this.countB==0 || this.countW==0) won=true;
		
		return won;
	}
	
	public void setToExit(){
		this.boardState = new ArrayList<Stack<Pawn>>();
		
		for(int i=0; i<24; i++){
			
			Stack<Pawn> temp = new Stack<Pawn>();
			
			if(i<3){
				Pawn tem = new Pawn(0, 2);
				temp.push(tem);
			}
			else if(i>17){
				Pawn tem = new Pawn(0, 1);
				temp.push(tem);
			}
			
			this.boardState.add(temp);
		}
	}
	
	public void changeTurn(){
		if(this.playerTurn==1) this.playerTurn=2;
		else{
			this.playerTurn=1;
		}
	}
	
	public String toString(){
		
		String pretPrint = "";
		String pretPrint2 = "";
		int maxLen = this.getMaxLen();
		
		for(int j=1; j<maxLen+2; j++){
			
			for(int i=11; i>=0; i--){
				
				Stack<Pawn> temp = this.boardState.get(i);
				if(j==maxLen+1){
					pretPrint+= "   ";
				}
				else if(temp.size() >= j){
					pretPrint += " " + temp.peek().getTurn() + " ";
				}
				else{
					pretPrint += " | ";
				}
			}
			
			pretPrint += "\n";
			
			for(int i=12; i<24; i++){
				
				Stack<Pawn> temp = this.boardState.get(i);
				
				if(j==maxLen+1);
				else if(temp.size() >= j){
					pretPrint2 += " " + temp.peek().getTurn() + " ";
				}
				else{
					pretPrint2 += " | ";
				}
			}
			
			pretPrint2 += "\n";
		}
		
		
		
		return pretPrint+pretPrint2;
	}
	
	public int getMaxLen(){
		
		int maxLen = 0;
		
		for(int i=0; i<24; i++){
			int curLen = this.boardState.get(i).size();
			
			if(curLen>maxLen) maxLen = curLen;
		}
		
		return maxLen;
	}
	
	//  Mise en String de l'état de la game
	private String toRec(Move moveRec){
		
		//double startRec = System.currentTimeMillis();
		
		//double startDice = System.currentTimeMillis();
		String strRec = this.diceR.size() + " ";
		
		for(int i=0; i< this.diceR.size(); i++){
			
			strRec += this.diceR.get(i) + " ";
			
		}
		//double endDice = System.currentTimeMillis();
		//System.out.println("Dice: " + (endDice-startDice) + "ms.");
		
		strRec += moveRec.getOrig() + " " + moveRec.getDest() + " " + moveRec.getTurn() + " ";
		
		//  THIS TOOK TO LONG ONCE
		//double startBoard = System.currentTimeMillis();
		for(int i=0; i<this.boardState.size(); i++){
			
			Stack<Pawn> temp = this.boardState.get(i);
			int tempSize = temp.size();
			
			String tempRec = i +  " " + tempSize + " ";
			
			if(tempSize==0) tempRec += 0 + " ";
			else{
				tempRec += temp.peek().getTurn() + " ";
			}
			
			strRec += tempRec;
			
		}
		//double endBoard = System.currentTimeMillis();
		//System.out.println("Board took:" + (endBoard-startBoard) + "ms.");
		
		//double startPris = System.currentTimeMillis();
		//  Mise en String prison.
		String prisRec = this.prison1.size() + " 1 " + this.prison2.size() + " 2 ";
		strRec += prisRec;
		//double endPris = System.currentTimeMillis();
		//System.out.println("Pris: " + (endPris-startPris));
		
		
		//double endRec=System.currentTimeMillis();
		
		//System.out.println("TOREC TOOK " + (endRec-startRec) + "ms.");
		
		return strRec;
	}

}
