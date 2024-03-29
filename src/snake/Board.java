package snake;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Board extends JPanel implements ActionListener {

    private final int B_WIDTH = 500;
    private final int B_HEIGHT = 500;
    private final int DOT_SIZE = 10;
    private final int RAND_POS = 29;
    private final int DELAY = 140;

    private final int NPLAYERS = 2;
    
    private final Map<String, Snake> snakeMap = new HashMap<String, Snake>();
    


    static int yInit = 0;
    private int apple_x;
    private int apple_y;


    private boolean inGame = true;

    private Timer timer;
    private Image apple;


    public Board() {

        addKeyListener(new TAdapter());
        setBackground(Color.black);
        setFocusable(true);

        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        loadImages();
        initGame();
    }

    private void loadImages() {
    	
    	
    	ImageIcon iia = new ImageIcon("src/apple.png");
        apple = iia.getImage();
    	

    }

    private void locate(String snake) {
    	snakeMap.get(snake).dots = 3;
    	yInit += 50;
        for (int z = 0; z < snakeMap.get(snake).dots; z++) {
        	snakeMap.get(snake).x[z] = 50 - z * 10;
        	snakeMap.get(snake).y[z] = yInit;
        }
    }
    
    
    private void initGame() {
    	
        for(int j = 1; j<NPLAYERS+1;j++) {
        	String s = "Snake"+j;
        	snakeMap.put(s, new Snake());
        	locate(s);
        }
    	
    	


        locateApple();

        timer = new Timer(DELAY, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);
    }
    
    
    private void drawSnake(String snake,Graphics g) {
        for (int z = 0; z < snakeMap.get(snake).dots; z++) {
            if (z == 0) {
                g.drawImage(snakeMap.get(snake).head, snakeMap.get(snake).x[z], snakeMap.get(snake).y[z], this);
            } else {
                g.drawImage(snakeMap.get(snake).ball, snakeMap.get(snake).x[z], snakeMap.get(snake).y[z], this);
            }
        }
    }
    
    private void doDrawing(Graphics g) {
        
        if (inGame) {

            g.drawImage(apple, apple_x, apple_y, this);

            for(int j = 1; j<NPLAYERS+1;j++) {
            	String s = "Snake"+j;
            	drawSnake(s,g);
            
            }


            Toolkit.getDefaultToolkit().sync();

        } else {

            gameOver(g);
        }        
    }

    private void gameOver(Graphics g) {
        
        String msg = "Game Over";
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg, (B_WIDTH - metr.stringWidth(msg)) / 2, B_HEIGHT / 2);
    }

    private void checkApple(String snake) {

        if ((snakeMap.get(snake).x[0] == apple_x) && (snakeMap.get(snake).y[0] == apple_y)) {

        	snakeMap.get(snake).dots++;
            locateApple();
        }
    }

    private void move(String snake) {

        for (int z = snakeMap.get(snake).dots; z > 0; z--) {
        	snakeMap.get(snake).x[z] = snakeMap.get(snake).x[(z - 1)];
            snakeMap.get(snake).y[z] = snakeMap.get(snake).y[(z - 1)];
        }

        if (snakeMap.get(snake).leftDirection) {
        	snakeMap.get(snake).x[0] -= DOT_SIZE;
        }

        if (snakeMap.get(snake).rightDirection) {
        	snakeMap.get(snake).x[0] += DOT_SIZE;
        }

        if (snakeMap.get(snake).upDirection) {
        	snakeMap.get(snake).y[0] -= DOT_SIZE;
        }

        if (snakeMap.get(snake).downDirection) {
        	snakeMap.get(snake).y[0] += DOT_SIZE;
        }
    }

    private void checkCollision(String snake) {

        for (int z = snakeMap.get(snake).dots; z > 0; z--) {

            if ((z > 4) && (snakeMap.get(snake).x[0] == snakeMap.get(snake).x[z]) && (snakeMap.get(snake).y[0] == snakeMap.get(snake).y[z])) {
                inGame = false;
            }
        }

        if (snakeMap.get(snake).y[0] >= B_HEIGHT) {
            inGame = false;
        }

        if (snakeMap.get(snake).y[0] < 0) {
            inGame = false;
        }

        if (snakeMap.get(snake).x[0] >= B_WIDTH) {
            inGame = false;
        }

        if (snakeMap.get(snake).x[0] < 0) {
            inGame = false;
        }
        
        if(!inGame) {
            timer.stop();
        }
    }

    private void locateApple() {

        int r = (int) (Math.random() * RAND_POS);
        apple_x = ((r * DOT_SIZE));

        r = (int) (Math.random() * RAND_POS);
        apple_y = ((r * DOT_SIZE));
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (inGame) {

        	for(int j = 1; j<NPLAYERS+1;j++) {
            	String s = "Snake"+j;
        	
	            checkApple(s);
	            checkCollision(s);
	            move(s);
        	}
        }

        repaint();
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();

            if ((key == KeyEvent.VK_LEFT) && (!snakeMap.get("Snake1").rightDirection)) {
            	snakeMap.get("Snake1").leftDirection = true;
            	snakeMap.get("Snake1").upDirection = false;
            	snakeMap.get("Snake1").downDirection = false;
            }

            if ((key == KeyEvent.VK_RIGHT) && (!snakeMap.get("Snake1").leftDirection)) {
            	snakeMap.get("Snake1").rightDirection = true;
            	snakeMap.get("Snake1"). upDirection = false;
            	snakeMap.get("Snake1").downDirection = false;
            }

            if ((key == KeyEvent.VK_UP) && (!snakeMap.get("Snake1").downDirection)) {
            	snakeMap.get("Snake1").upDirection = true;
            	snakeMap.get("Snake1").rightDirection = false;
            	snakeMap.get("Snake1").leftDirection = false;
            }

            if ((key == KeyEvent.VK_DOWN) && (!snakeMap.get("Snake1").upDirection)) {
            	snakeMap.get("Snake1").downDirection = true;
            	snakeMap.get("Snake1").rightDirection = false;
            	snakeMap.get("Snake1").leftDirection = false;
            }
        }
    }
}
