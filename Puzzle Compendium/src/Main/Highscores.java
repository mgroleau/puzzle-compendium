package Main;
import java.io.*;

public class Highscores {
	private static long[] scores = new long[3];
	private static String filename;
	
	public static void read(int game) {
		switch(game){
		case 0: filename = "highscores/agbghighscores.txt"; break;
		case 1: filename = "highscores/minerhighscores.txt"; break;
		case 2: filename = "highscores/manpachighscores.txt"; break;
		case 3: filename = "highscores/basedrnggodhighscores.txt"; break;
		}
		try {
			BufferedReader br = new BufferedReader(new FileReader(
					filename));

			scores[0] = Long.parseLong(br.readLine());
			scores[1] = Long.parseLong(br.readLine());
			scores[2] = Long.parseLong(br.readLine());
			br.close();
		} catch (IOException e2) {

		} catch (NumberFormatException e1){
                    
                }
	}

	public static void save(long score, int game) {
		PrintWriter writer;
		if (score > scores[0]) {
			scores[2] = scores[1];
			scores[1] = scores[0];
			scores[0] = score;
		} else if (score > scores[1]) {
			scores[2] = scores[1];
			scores[1] = score;
		} else if (score > scores[2])
			scores[2] = score;

		switch(game){
		case 0: filename = "highscores/agbghighscores.txt"; break;
		case 1: filename = "highscores/minerhighscores.txt"; break;
		case 2: filename = "highscores/manpachighscores.txt"; break;
		case 3: filename = "highscores/basedrnggodhighscores.txt"; break;
		}
		
		try {
			writer = new PrintWriter(filename);
			writer.println(scores[0]);
			writer.println(scores[1]);
			writer.println(scores[2]);
			writer.close();
		} catch (FileNotFoundException e) {
		}

	}
}
