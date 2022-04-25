package pkg;

import javax.swing.JOptionPane;

public class Minesweeper {
	
    private static Game createGame;
    public static String gameName = "Démineur M1GLSI Groupe 5";
    
    // TODO: Fermer l'ancienne instance du jeu lorsqu'on appui sur ré-essayer/rejouer

    public static void gameInit(int ...gameParams) {
    	int level = 0;
        int size = 9;
        int bombs = 10;
	    	if(gameParams.length != 0){		
	    		size = gameParams[0];
	    		level = gameParams[1];
	    		bombs = gameParams[2];
	    	} else {
		        Object[] choices = {"Débutant", "Intermédiaire", "Expert"};
		        level = JOptionPane.showOptionDialog(null,
		                "Choisir un niveau de difficulté", gameName,
		                JOptionPane.YES_NO_CANCEL_OPTION,
		                JOptionPane.QUESTION_MESSAGE,
		                null,
		                choices,
		                choices[0]); // Niveau pré-selectionné = débutant
		        if(level == -1)
		            System.exit(0);
		        
		        switch(level) {
			        case 0:
			        	size = 9;
			        	bombs = 10;
			        	break;
			        case 1: 
			        	size = 16;
			        	bombs = 40;
			        	break;
			        case 2:
			        	size = 30;
			        	bombs = 2;
			        	break;
		        }
	    	}
	    
    	createGame = new Game(size, level, bombs);
        createGame.container(createGame, size);
    }
    
    public static void main(String[] args) {
    	gameInit();
    }
    
  
}

