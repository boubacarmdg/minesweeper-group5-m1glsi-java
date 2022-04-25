package pkg;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.awt.event.*;


import javax.imageio.ImageIO;

public class Game extends JFrame {
	
	    // Définition de nos variables
	
	    private static final long serialVersionUID = 1L;
		private int size; // Nombres de cases
		private int level; // Niveau de jeu
		private int bombs; // Nombre de bombes - #bombsInGame
		private String gameName = Minesweeper.gameName; // Titre de la fenêtre du jeu
		private ArrayList<String> refTile = new ArrayList<String>(size); // Liste contenant les références de chaque case
		
		private JButton[][] gameTiles;  // Conteneur des cases
	    private JPanel gameInformations;  // Conteneur des inforamtions du jeu
	    private JPanel gameArea;  // Conteneur des cases du jeu
	    private JLabel gameFlags;  // Nombre de drapeaux disponibles
	    private JLabel gameTime;  // Durée total du jeu
        private JPanel gameScore; // Conteneur du dernier score du joueur
	    private JLabel lastScore; // Score de la dernière partie gagnée avec le niveau selectionné

	    private int bombsInGame = 0;  // Nombre de bombes présentes dans le jeu
	    private int[][] tilePosition;  // Position (x,y) d'une case
	    private boolean[][] isShowed;  // Vérifie si la case de coordonnées (x,y) a déjà été affichée
	    private int tilesShowed;  // Combien de cases on déjà été affichées
	    private boolean[][] isFlagged;  // Vérifie si la case de coordonnées (x,y) contient un drapeau
	    
	    private Image flagImg; // Image du drapeau
	    private Image flagImgContainer; // Conteneur de l'image du drapeau
	    private Image bombImg; // Image de la bombe
	    private Image bombImgContainer; // Conteneur de l'image de la bombe
	
	    // Customisation du jeu
	    private Color borderColor = new Color(73, 162, 233); // Couleur de la bordure de la case
	    private Color tilesAlreadyShowedColor = new Color(228, 229, 228); // Couleur de la case lorsqu'elle a été affichée (active state)
	    private Color tileButtonColor = new Color(208, 209, 209); // Couleur par défaut de la case
	    private Color tileButtonColorHover = new Color(193, 195, 194); // Couleur de la case lorsqu'on passe la souris dessus (hover state)
	    private Color tileBombColor = new Color(234, 173, 173); // Couleur de la case contenant une bombe
	    private String one = "rgb(73, 162, 233)"; // Couleur de la case avec 1 bombe adjacentes
	    private String two = "red"; // Couleur de la case avec 2 bombes adjacentes
	    private String three = "green"; // Couleur de la case avec 3 bombes adjacentes
	    private String four = "blue"; // Couleur de la case avec 4 bombes adjacentes
	    private String five = "#945E0F"; // Couleur de la case avec 5 bombes adjacentes
	    
	    public static final int TILE_SIZE = 45;
    
	
    public Game(int size, int level, int bombs) {
    	bombsInGame = bombs;
    	this.size = size;
    	this.level = level;
    	this.bombs = bombs;
        this.setSize(size*TILE_SIZE, size*TILE_SIZE + 50);
        this.setTitle(gameName);
        this.setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    }

    private void dispatchBombs(int size) {
        Random rand = new Random();
        
        tilePosition = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                tilePosition[i][j] = 0;
            }
        }

        int count = 0;
        int xBomb;
        int yBomb;
        while(count<bombsInGame) {
            xBomb = rand.nextInt(size);
            yBomb = rand.nextInt(size);
            if (tilePosition[xBomb][yBomb]!=-1) {
                tilePosition[xBomb][yBomb]=-1;  // Bombe
                count++;
            }
        }
        
        // Lu nombre à afficher sur la case selectionnée en fonction du nombre de bombes adjacentes
        for (int i = 0; i < size; i++) {
        for (int j = 0; j < size; j++) {
            if (tilePosition[i][j]==-1) {
                    for (int k = -1; k <= 1 ; k++) {
                    for (int l = -1; l <= 1; l++) {
                        
                        try {
                            if (tilePosition[i+k][j+l]!=-1) {
                                tilePosition[i+k][j+l] += 1;
                            }
                        }catch (Exception e) {}
                    }
                    }
            }
        }
        }
    }

    public void container(Game gameEnclosure, int size) {

        GameInteractions interactions = new GameInteractions(gameEnclosure);
        TrackMouse trackMouse = new TrackMouse(gameEnclosure);
        JPanel gameWindow = new JPanel();

        gameInformations = new JPanel();
        gameArea = new JPanel();
        gameScore = new JPanel();
        
        this.tilesShowed = 0;

        isShowed = new boolean[size][size];
        isFlagged = new boolean[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                isShowed[i][j] = false;
                isFlagged[i][j] = false;
            }
        }

        // Images
        try {
            flagImg = ImageIO.read(getClass().getResource("assets/red-flag.png"));
            flagImgContainer = flagImg.getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH);

            bombImg = ImageIO.read(getClass().getResource("assets/explosion.png"));
            bombImgContainer = bombImg.getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH);
            
        }catch (Exception e){}

        gameWindow.setLayout(new BoxLayout(gameWindow, BoxLayout.Y_AXIS));

        BoxLayout box1 = new BoxLayout(gameInformations, BoxLayout.X_AXIS);
        gameInformations.setLayout(box1);


        // Affichage de la durée total de la partie en seconde
        JLabel timeJLabel = new JLabel("Durée du jeu: ");
        gameTime = new JLabel("0");
        gameTime.setAlignmentX(Component.RIGHT_ALIGNMENT);
        gameTime.setHorizontalAlignment(JLabel.LEFT);
        
        // Affichage des drapeaux restants
        JLabel flagsJLabel = new JLabel("Nombres de drapeaux: ");
        flagsJLabel.setHorizontalAlignment(JLabel.RIGHT);
        flagsJLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        gameFlags = new JLabel(""+this.bombsInGame);


        gameInformations.add(flagsJLabel);
        gameInformations.add(gameFlags);
        gameInformations.add(Box.createRigidArea(new Dimension((size-1)*15 - 80,50)));
        gameInformations.add(Box.createRigidArea(new Dimension((size-1)*15 - 85,50)));
        gameInformations.add(timeJLabel);
        gameInformations.add(gameTime);
        
//        lastScore = new JLabel("Score");
//        gameScore.add(lastScore, BorderLayout.SOUTH);
        
        GridLayout box2 = new GridLayout(size, size);
        gameArea.setLayout(box2);
        

        gameTiles = new JButton[size][size];

        for (int i=0; i<size; i++) {
            for (int j=0; j<size ; j++ ) {
                gameTiles[i][j] = new JButton();
                
            	JButton selectedTile = gameTiles[i][j];
                gameTiles[i][j].setPreferredSize(new Dimension(15, 15));
                gameTiles[i][j].setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, borderColor));
                gameTiles[i][j].setBackground(tileButtonColor);
                gameTiles[i][j].addMouseListener(new MouseAdapter() 
                {
                   public void mouseEntered(MouseEvent evt) 
                   {
                	   
                	   if(refTile.contains(""+selectedTile) == false) {
                		   selectedTile.setBackground(tileButtonColorHover);
                	   }
                   }
                   public void mouseExited(MouseEvent evt) 
                   {
                	   if(refTile.contains(""+selectedTile) == false) {
                		   selectedTile.setBackground(tileButtonColor); 
                	   }
                	   
                   }
                });
                gameTiles[i][j].setBorderPainted(true);
                gameTiles[i][j].setName(i + " " + j);
                gameTiles[i][j].addActionListener(interactions);
                gameTiles[i][j].addMouseListener(trackMouse);
               
                gameArea.add(gameTiles[i][j]);
   
            }
        }

        // On ajoute à la fenêtre tous les éléments du jeu
        gameWindow.add(gameInformations);
        gameWindow.add(gameArea);
//        gameWindow.add(gameScore);
        gameEnclosure.setContentPane(gameWindow);
        this.setVisible(true);
        
        // Positionnelent aléatoire des bombes en fonction du nombre de cases et du nombre de bombes initialisées
        dispatchBombs(size);

        // Chrono
        CalculateGameTime timer = new CalculateGameTime(this);
        timer.start();

    }

    // Mis à jour de la durée du jeu toutes les secondes
    public void getGameTime() {
        String[] time = this.gameTime.getText().split(" ");
        int time0 = Integer.parseInt(time[0]);
        ++time0;
        this.gameTime.setText(Integer.toString(time0) + " secs");
    }


    // Gestion des drapeaux
    public void placeFlag(int x, int y) {
        if(!isShowed[x][y]) {
            if (isFlagged[x][y]) {
                gameTiles[x][y].setIcon(null);
                isFlagged[x][y] = false;
                int old = Integer.parseInt(this.gameFlags.getText());
                ++old;
                this.gameFlags.setText(""+old);
            }
            else {
                if (Integer.parseInt(this.gameFlags.getText())>0) {
                    gameTiles[x][y].setIcon(new ImageIcon(flagImgContainer));
                    isFlagged[x][y] = true;
                    int old = Integer.parseInt(this.gameFlags.getText());
                    --old;
                    this.gameFlags.setText(""+old);
                }
            }
        }
    }

    // Si le nombre des cases montrées est égal au nombre de cases totales du jeu alors le joueur a effectivement gagné
    private boolean isSuccessfullyFinished() {
        return (this.tilesShowed) ==  (Math.pow(this.tilePosition.length, 2) - this.bombsInGame);
    }

    // Fonction exécutée lorsqu'on appuie sur la case positionnée aux coordonnées (x,y)
    public void tileClicked(int x, int y) {
        if(!isShowed[x][y] && !isFlagged[x][y]) {
            isShowed[x][y] = true;

            switch (tilePosition[x][y]) {
                case -1:
                    try {
                        gameTiles[x][y].setIcon(new ImageIcon(bombImgContainer));
                    } catch (Exception e1) {
                    }
                    gameTiles[x][y].setBackground(tileBombColor);
                

                    
                    Object[] choices = {"Ré-essayer", "Quitter"};
                    int overGame = JOptionPane.showOptionDialog(null,
                            "Oh nooon! Vous avez perdu", gameName,
                            JOptionPane.YES_NO_CANCEL_OPTION,
                            JOptionPane.ERROR_MESSAGE,
                            null,
                            choices,
                            choices[1]);
                    if(overGame == -1)
                        System.exit(0);
                   
                    if(overGame == 0) {
                    	int currentParams[] = {size,level,bombs};
                    	Minesweeper.gameInit(currentParams);     
                    } else { 	
                    	System.exit(0);
                    }
                    



                   

                    break;

                case 0:
                    gameTiles[x][y].setBackground(tilesAlreadyShowedColor);
                	refTile.add(""+gameTiles[x][y]);
                    ++this.tilesShowed;
                    
                    // Gestion de la victoire du joueur
                    if (isSuccessfullyFinished()) {
                        
                    	SaveGame sv = new SaveGame(
                    			Integer.parseInt(this.gameFlags.getText()),
                    			this.gameTime.getText(),
                    			level
                    			);
                    	sv.proceed();
                    	
                        Object[] choicesW = {"Rejouer", "Quitter"};
                        int winner = JOptionPane.showOptionDialog(null,
                                "Bravo, vous avez gagné! \nVotre progression a bien été enregistrée!", gameName,
                                JOptionPane.YES_NO_CANCEL_OPTION,
                                JOptionPane.INFORMATION_MESSAGE,
                                null,
                                choicesW,
                                choicesW[1]);
                        if(winner == -1)
                            System.exit(0);
                       
                        if(winner == 0) {
                        	int currentParams[] = {size,level,bombs};
                        	Minesweeper.gameInit(currentParams);     
                        } else { 	
                        	System.exit(0);
                        }
                    }  

                    
                    // Si c'est pas le cas on passe à la case suivante
                    for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        try {
                            tileClicked(x + i, y + j);
                        }catch (Exception e3) {}
                    }
                    }

                    break;

                default:
                	gameTiles[x][y].setBackground(tilesAlreadyShowedColor);
                    refTile.add(""+gameTiles[x][y]);
                    
                	switch(Integer.toString(tilePosition[x][y])){
                		case "1":
                			gameTiles[x][y].setText("<html><div style=\"padding:14px;background:rgb(228, 229, 228);color:"+one+";\">"+Integer.toString(tilePosition[x][y])+"</div><html>");   
                			break;
                		case "2":
                			gameTiles[x][y].setText("<html><div style=\"padding:14px;background:rgb(228, 229, 228);color:"+two+";\">"+Integer.toString(tilePosition[x][y])+"</div><html>");  
                			break;
                		case "3":
                			gameTiles[x][y].setText("<html><div style=\"padding:14px;background:rgb(228, 229, 228);color:"+three+"\">"+Integer.toString(tilePosition[x][y])+"</div><html>");   
                			break;
                		case "4":
                			gameTiles[x][y].setText("<html><div style=\"padding:14px;background:rgb(228, 229, 228);color:"+four+";\">"+Integer.toString(tilePosition[x][y])+"</div><html>");   
                			break; 
                		case "5":
                			gameTiles[x][y].setText("<html><div style=\"padding:14px;background:rgb(228, 229, 228);color:"+five+";\">"+Integer.toString(tilePosition[x][y])+"</div><html>");   
                			break;
                	}
                                
                    
                    ++this.tilesShowed;
                    if (isSuccessfullyFinished()) {
                    	
                    	SaveGame sv = new SaveGame(
                    			Integer.parseInt(this.gameFlags.getText()),
                    			this.gameTime.getText(),
                    			level
                    			);
                    	sv.proceed();

                    	Object[] choicesW = {"Rejouer", "Quitter"};
                        int winner = JOptionPane.showOptionDialog(null,
                                "Bravo, vous avez gagné! \nVotre progression a bien été enregistrée!", gameName,
                                JOptionPane.YES_NO_CANCEL_OPTION,
                                JOptionPane.INFORMATION_MESSAGE,
                                null,
                                choicesW,
                                choicesW[1]);
                        if(winner == -1)
                            System.exit(0);
                       
                        if(winner == 0) {
                        	int currentParams[] = {size,level,bombs};
                        	Minesweeper.gameInit(currentParams);     
                        } else { 	
                        	System.exit(0);
                        }
                    }

                    break;
            }
        }
        
    }
  

}

class GameInteractions implements ActionListener {
    Game refGame;
    
    GameInteractions(Game currentGame) {
        this.refGame = currentGame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object eventSource = e.getSource();
        JButton clickedButton = (JButton) eventSource;
        
            String[] xy = clickedButton.getName().split(" ", 2);
            int x = Integer.parseInt(xy[0]);
            int y = Integer.parseInt(xy[1]);
            refGame.tileClicked(x, y);

        }
    
}

class TrackMouse implements MouseListener {
    Game refGame;

    TrackMouse(Game currentGame) {
        this.refGame = currentGame;
    }

    public void mouseExited(MouseEvent arg0){
    }
    public void mouseEntered(MouseEvent arg0){
    }
    public void mousePressed(MouseEvent arg0){
    }
    public void mouseClicked(MouseEvent arg0){
    }

    @Override
    public void mouseReleased(MouseEvent arg0) {
        if(SwingUtilities.isRightMouseButton(arg0)){
            Object eventSource = arg0.getSource();
            JButton clickedButton = (JButton) eventSource;
            String[] xy = clickedButton.getName().split(" ", 2);
            int x = Integer.parseInt(xy[0]);
            int y = Integer.parseInt(xy[1]);
            refGame.placeFlag(x, y);
        }
    }
}

class CalculateGameTime implements Runnable {
    private Thread thread;
    private Game refGame;

    CalculateGameTime(Game currentGame) {
        this.refGame = currentGame;
    }

    public void run() {
        while(true) {
            try {
                Thread.sleep(1000);
                refGame.getGameTime();
            }
            catch (InterruptedException e) {
                System.exit(0);
            }
        }
    }

    public void start() {
        if (thread==null) {
            thread = new Thread(this);
            thread.start();
        }
    }
}

