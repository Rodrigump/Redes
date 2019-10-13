package snake;

import java.awt.EventQueue;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;


public class Snake {
    public final int x[] = new int[900];
    public final int y[] = new int[900];

    public int dots;
    
    public boolean leftDirection = false;
    public boolean rightDirection = true;
    public boolean upDirection = false;
    public boolean downDirection = false;
    public ImageIcon iid = new ImageIcon("src/body.png");
    public ImageIcon iih = new ImageIcon("src/head.png");
    public Image head = iih.getImage();
    public Image ball = iid.getImage();
    public int score = 0;
    
}
