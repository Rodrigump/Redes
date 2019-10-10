package snake;

import javax.swing.JFrame;

public class Frame extends JFrame {

    public Frame() {

        //add(new Board());
        
    	
    	add(new MainScreen().pane);
    	
        setResizable(false);
        pack();
        
        setTitle("Snake");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}