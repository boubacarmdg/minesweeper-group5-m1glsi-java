package pkg;

import javax.swing.JOptionPane;

public class Minesweeper {
	
    private static Game createGame;
    public static String gameName = "D�mineur M1GLSI Groupe 5";
    
    // TODO: Fermer l'ancienne instance du jeu lorsqu'on appui sur r�-essayer/rejouer

    public static void gameInit(int ...gameParams) {
    	int level = 0;
        int size = 9;
        int bombs = 10;
	    	if(gameParams.length != 0){		
	    		size = gameParams[0];
	    		level = gameParams[1];
	    		bombs = gameParams[2];
	    	} else {
		        Object[] choices = {"D�butant", "Interm�diaire", "Expert"};
		        level = JOptionPane.showOptionDialog(null,
		                "Choisir un niveau de difficult�", gameName,
		                JOptionPane.YES_NO_CANCEL_OPTION,
		                JOptionPane.QUESTION_MESSAGE,
		                null,
		                choices,
		                choices[0]); // Niveau pr�-selectionn� = d�butant
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

