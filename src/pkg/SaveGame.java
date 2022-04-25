package pkg;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class SaveGame {
	
	private int flag;
	private String time;
	private String level;
	private String flagLabel;
	// Les enregistrements présentes dans le fichier sont des données enregistrées lors du développement
	private String path = "FileSaveGames.csv"; // Fichier de sauvegarde des informations de la partie gagnée
	
	public SaveGame(int flag, String time, int level) {
		this.flag = flag;
		this.time = time;
		
//		if(flag == 1) {
//			flagLabel = "drapeau";
//		} else {
//			flagLabel = "drapeaux";
//		}
		
		switch(level) {
		case 0:
			this.level = "Débutant";
			break;
		case 1: 
			this.level = "Intermédiaire";
			break;
		case 2:
			this.level = "Expert";
			break;
		}
	}
	
	public void proceed() {
		BufferedReader reader = null;
		BufferedWriter writer = null;
		
		try {
			FileReader readScores = new FileReader(path);
			reader = new BufferedReader(readScores);
			
			String line = reader.readLine(); // pour sauter la 1ère ligne (entête)
			
			FileWriter addScore = new FileWriter(path);
			writer = new BufferedWriter(addScore);

			
			writer.write("Nombres de drapeaux restants;Durée partie;Niveau");
			 
		
				writer.newLine();
				writer.write(flag+";"+time+";"+level);
				writer.newLine();
					while ((line = reader.readLine()) != null)    
					{  
						String[] gameSavedRecords = line.split(";");    // séparateur valeur fichier csv ";"
						writer.write(gameSavedRecords[0]+";"+gameSavedRecords[1]+";"+gameSavedRecords[2]);
						writer.newLine();
					}  
				writer.newLine();
				
			
		} catch (Exception e) {
//			System.err.println("Fichier est introuvable ou illisible !");
//			e.printStackTrace();
		} finally {
			try {
				reader.close();
				writer.close();
//				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		
		
	}

}
