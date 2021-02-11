import java.util.*;
import java.io.*;


public class RunGame {
	public static void main(String[] args) {
		
		printWeights("moves1b.txt");
		
		//HashMap<String, float[]> temp = new HashMap<String, float[]>();
		//float[] testArr = {19.0f, 21.0f, 0.0f};
		//temp.put("2 2 2 5 3 2 0 4 2 1 2 2 2 1 2 3 4 2 4 0 0 5 2 2 6 0 0 7 0 0 8 0 0 9 0 0 10 0 0 11 2 1 12 0 0 13 0 0 14 0 0 15 0 0 16 0 0 17 1 1 18 1 1 19 2 1 20 0 0 21 4 1 22 3 1 23 2 1 0 1 0 2 ", testArr);
		
		//updateWeights("moves2.txt", temp);

		//  Declare the arrays that will be used to store the doc names
		
		/*
		String[] moves1 = {"moves1b.txt","moves1a.txt"};
		String[] moves2 = {"moves2b.txt","moves2a.txt"};
		int nbUpd = 0;
		
		
		
		double timeStart = System.currentTimeMillis();

		int nTries = 100000000;
		int capacity = 1000000;  
		float loadFactor = 0.3f;

		System.out.println("hiya");

		double timeAvg = 0;
		double timeUpd = 0;

		double startLoad = System.currentTimeMillis();
		
		// Initiates new HashMaps since not necessary to load weights every time.
		HashMap<String, float[]> dataBase1 = new HashMap<String, float[]>();
		HashMap<String, float[]> dataBase2 = new HashMap<String, float[]>();
		
		double endLoad = System.currentTimeMillis();
		System.out.println("Loading took: " + (endLoad - startLoad) + "ms.");

		for (int i = 0; i < nTries; i++) {

			double timeStartGame = System.currentTimeMillis();

			double startW = System.currentTimeMillis();
			weightTrain(dataBase1, dataBase2);
			double endW = System.currentTimeMillis();
			timeUpd += (endW - startW);
			System.out.println("The average update time is: " + timeUpd / i);

			if (dataBase1.size()>1000000 || dataBase2.size()> 1000000) {
				
				double startUpd = System.currentTimeMillis();
				
				System.out.println("Updating the files");
				
				int readFrom = nbUpd%2;
				int writeTo = (nbUpd+1)%2;
				
				updateWeights(moves1[readFrom], moves1[writeTo], dataBase1);
				clearDocs(moves1[readFrom]);
				updateWeights(moves2[readFrom], moves2[writeTo], dataBase2);
				clearDocs(moves2[readFrom]);
				
				dataBase1 = new HashMap<String, float[]>();
				dataBase2 = new HashMap<String, float[]>();
				
				nbUpd++;
				
				double endUpd = System.currentTimeMillis();
				
				System.out.println("Update no " + nbUpd + " took " + (endUpd-startUpd) + "ms.");
				
			}

			double endGame = System.currentTimeMillis();

			System.out.println("Game no " + i + " over.  Played for " + (endGame - timeStartGame) + "ms.");
			System.out.println("You have completed: " + ((double) i) / nTries + "%.");
			timeAvg += (endGame - timeStartGame);

			System.out.println("database1 size: " + dataBase1.size());
			System.out.println("database2 size: " + dataBase2.size());

		}
		
		int readFrom = nbUpd%2;
		int writeTo = (nbUpd+1)%2;
		
		updateWeights(moves1[readFrom], moves1[writeTo], dataBase1);
		updateWeights(moves2[readFrom], moves2[writeTo], dataBase2);
		
		dataBase1 = new HashMap<String, float[]>();
		dataBase2 = new HashMap<String, float[]>();
		
		clearDocs(moves1[readFrom]);
		clearDocs(moves2[readFrom]);

		double timeEnd = System.currentTimeMillis();

		System.out.println("For initial cap: " + capacity + " load factor: " + loadFactor + " TIME: "
				+ (timeEnd - timeStart) + "ms.");
		System.out.println("Average time is : " + (timeAvg / nTries));

		//double startWrite = System.currentTimeMillis();
		//writeToFile("moves1.txt", dataBase1);
		//writeToFile("moves2.txt", dataBase2);
		//double endWrite = System.currentTimeMillis();
		//System.out.println("Writing took: " + (endWrite - startWrite) + "ms.");
 
		*/
		
	}
	
	public static void printWeights(String readFrom){
		
		try{
			
			File myObj = new File(readFrom);

			Scanner myReader = new Scanner(myObj);

			while (myReader.hasNextLine()) {

				String[] data = (myReader.nextLine().split(";"));
				String[] array = data[1].split(",");
				float[] weights = new float[3];

				for (int i = 0; i < array.length; i++) {
					weights[i] = Float.parseFloat(array[i].replaceAll("[^0-9.]", ""));
				}

				System.out.println(data[0] + " " + Arrays.toString(weights));

			}
			myReader.close();

			
			
		}
		catch(Exception e){
			System.out.println("Failed");
			System.out.println(e.getStackTrace());
		}
	}

	public static void updateWeights(String readFrom, String writeTo, HashMap<String, float[]> curDb) {

		try {

			File myObj = new File(readFrom);

			Scanner myReader = new Scanner(myObj);
			FileWriter fr = new FileWriter(writeTo);
			
			while(myReader.hasNextLine()){
				
				String[] data = (myReader.nextLine().split(";"));
				String[] array = data[1].split(",");
				float[] weights = new float[3];
				
				for (int i = 0; i < array.length; i++) {
					weights[i] = Float.parseFloat(array[i].replaceAll("[^0-9.]", ""));
				}
				
				float[] upd = curDb.get(data[0]);
				
				if(!(upd==null)){
					
					weights[0] += upd[0];
					weights[1] += upd[1];
					weights[2] = upd[0]/upd[1];
					
					curDb.remove(data[0]);
					
					
				}
				
				fr.write("" + data[0] + ";" + Arrays.toString(weights) + "\n");
				
				//System.out.println(data[0] + " " + Arrays.toString(weights));
			}
			
			myReader.close();
			fr.close();

		}

		catch (Exception e) {

			System.out.println("An error occurred.");
			e.printStackTrace();
		}

	}

	public static void weightTrain(HashMap<String, float[]> dataBase1, HashMap<String, float[]> dataBase2) {

		Board gameRun = new Board();

		// double startGame = System.currentTimeMillis();
		gameRun.playGame();
		// double endGame = System.currentTimeMillis();

		// System.out.println("THE GAME LASTED: " + (endGame-startGame) +
		// "ms.");
		// System.out.println("TOTAL NB OF MOVES: " +
		// gameRun.getMoves(1).size()*2);

		boolean won;

		if (gameRun.getTurn() == 1) {
			won = true;
		} else {
			won = false;
		}

		updateWeights(won, gameRun.getMoves(1), dataBase1);
		updateWeights(!won, gameRun.getMoves(2), dataBase2);

	}

	public static HashMap<String, float[]> updateWeights(boolean won, ArrayList<String> moves,
			HashMap<String, float[]> dataBase) {

		for (int i = 0; i < moves.size(); i++) {

			String key = moves.get(i);
			float[] temp = dataBase.get(key);

			if (temp == null) {
				if (won) {
					float[] wonArr = { 1, 1, 1 };
					dataBase.put(key, wonArr);
				} else {
					float[] lostArr = { 0, 1, 0 };
					dataBase.put(key, lostArr);
				}
			} else {
				if (won) {
					temp[0]++;
				}
				temp[1]++;
				temp[2] = temp[0] / temp[1];

				dataBase.put(key, temp);
			}
		}

		return dataBase;
	}

	public static HashMap<String, float[]> loadData(String fileName) {

		HashMap<String, float[]> dataBase = new HashMap<String, float[]>();

		try {

			File myObj = new File(fileName);

			Scanner myReader = new Scanner(myObj);

			while (myReader.hasNextLine()) {

				String[] data = (myReader.nextLine().split(";"));
				String[] array = data[1].split(",");
				float[] weights = new float[3];

				for (int i = 0; i < array.length; i++) {
					weights[i] = Float.parseFloat(array[i].replaceAll("[^0-9.]", ""));
				}

				dataBase.put(data[0], weights);

			}
			myReader.close();

		}

		catch (FileNotFoundException e) {

			System.out.println("An error occurred.");
			e.printStackTrace();
		}

		return dataBase;
	}

	public static void writeToFile(String fileName, HashMap<String, float[]> dataBase) {

		try {

			FileWriter myWriter = new FileWriter(fileName);

			String[] keySet1 = dataBase.keySet().toArray(new String[dataBase.size()]);

			for (int i = 0; i < keySet1.length; i++) {

				myWriter.write("" + keySet1[i] + ";" + Arrays.toString(dataBase.get(keySet1[i])) + "\n");

			}

			myWriter.close();

		}

		catch (IOException e) {

			System.out.println("An error occurred.");
			e.printStackTrace();

		}
	}

	public static void writeToFile(HashMap<String, float[]> dataBase, FileWriter writeFile) {

		try {

			String[] keySet1 = dataBase.keySet().toArray(new String[dataBase.size()]);

			for (int i = 0; i < keySet1.length; i++) {

				writeFile.write("" + keySet1[i] + ";" + Arrays.toString(dataBase.get(keySet1[i])) + "\n");

			}

		}

		catch (IOException e) {

			System.out.println("An error occurred.");
			e.printStackTrace();

		}
	}

	// Helper method to clear out the tings
	public static void clearDocs(String fileName) {

		try {

			FileWriter clear = new FileWriter(fileName);
			clear.write("");
			clear.close();

		}

		catch (IOException e) {

			System.out.println("An error occurred.");
			e.printStackTrace();

		}

	}

}
