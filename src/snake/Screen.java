package snake;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class Screen extends JPanel implements Runnable, KeyListener {
	

	 private static final long serialVersionUID = 1L;
	 public static final int WIDTH = 800, HEIGHT = 600;
	 private Thread thread; //w¹tek
	 private boolean running = false; //flaga uruchomienia
	 private BodyPart b; //czêœæ wê¿a
	 private ArrayList<BodyPart> snake; //w¹¿
	 private Apple apple; //jab³ko
	 private ArrayList<Apple> apples; //jab³ka
	 private Random r;
	 private int xCoor = 10, yCoor = 10; //pocz¹tkowe po³o¿enie
	 private int size = 5; //pocz¹tkowy rozmiar
	 private int snakeSpeed=250000*4; //prêdkoœæ wê¿a
	 // pocz¹tkowy kierunek ruchu
	 private boolean right = true, left = false, up = false, down =false;
	 private int ticks = 0; // licznik tików
	 private int score = 0; //licznik punktó

	 private String path = "src/Scores.txt";
	 
	 public Screen(int WIDTH, int HEIGH) 
	 {
		 setFocusable(true);
		 addKeyListener(this);
		 setPreferredSize(new Dimension(WIDTH, HEIGHT));
		 
	     JLabel scoreText = new JLabel("Your score: ");
		 scoreText.setPreferredSize(new Dimension(150,15));
		 scoreText.setBounds((WIDTH / 2) - 150 / 2, 0, 150, 15);

		 
		 add(scoreText);
		 
		 r = new Random();
		 snake = new ArrayList<BodyPart>();
		 apples = new ArrayList<Apple>();
		 start();
	 }
	 
     public void tick()
     {
         //jezeli nie ma weza to stworz
         if (snake.size() == 0)
         {
             //nowa czesc weza (x,y,rozmiar kwadratu w siatce)
             b = new BodyPart(xCoor, yCoor, 10);
             //dodanie czesci do listy weza
             snake.add(b);
         }
         //jezeli nie ma jablka to stworz
         if (apples.size() == 0)
         {
             //losowanie wsp dla jablka
             int xCoor = r.nextInt(79);
             int yCoor = r.nextInt(55) + 2;
             //dodanie jablka
             apple = new Apple(xCoor, yCoor, 10);
             apples.add(apple);
         }
         // sprawdzenie czy waz zjadl jablko
         //jezeli tak to zwieksz rozmiar i usun jablko
         for (int i = 0; i < apples.size(); i++)
         {
             if (xCoor == apples.get(i).getxCoor() &&
             yCoor == apples.get(i).getyCoor())
             {
                 size++;
                 apples.remove(i);
                 i++;
                 score++;
             }
         }
         //sprawdzenie czy waz nie ugryzl ogona
         for (int i = 0; i < snake.size(); i++)
         {
             if (xCoor == snake.get(i).getxCoor() &&
             yCoor == snake.get(i).getyCoor())
             {
                 if (i != snake.size() - 1)
                 {
                     stop();
                 }
             }
         }
         //sprawdzenie czy w¹¿ wszed³ w œcianê
         if (xCoor < 0 || xCoor > 79 || yCoor < 2 || yCoor > 59)
         {
             stop();
         }


         ticks++;
         //po wykonaniu tej metody wiecej razy ni¿ wartosc snakeSpeed
         if (ticks > snakeSpeed)
         {
             //sprawdzenie który przycisk byl wcisniety
             //dodanie lub odjecie 1 od odpowiednich wspolrzednych
             if (right) xCoor++;
             if (left) xCoor--;
             if (up) yCoor--;
             if (down) yCoor++;
             //wyzerowanie licznika
             ticks = 0;
             //dodawanie elementu wê¿a dla nowych wspolrzednych
             b = new BodyPart(xCoor, yCoor, 10);
             snake.add(b);
             //sprawdzenie czy w¹¿ jest wiêkszy od wartoœci size
             //jezeli tak to usuwana jest ostatnia czesc
             if (snake.size() > size)
             {
                 snake.remove(0);
             }
         }
     }
     
     public void paint(Graphics g)
     {
         g.clearRect(0, 0, WIDTH, HEIGHT);
         g.setColor(Color.GREEN);
         g.fillRect(0, 0, WIDTH, HEIGHT);

         g.setColor(Color.BLACK);
         
         g.drawString("Your score: " + score, (WIDTH / 2) - 100 / 2 , 15);
         
         
         for (int i = 0; i < WIDTH / 10; i++)
         {
             g.drawLine(i * 10, 20, i * 10, HEIGHT);
         }
         for (int i = 2; i < HEIGHT / 10; i++)
         {
             g.drawLine(0, i * 10, WIDTH, i * 10);
         }
         for (int i = 0; i < snake.size(); i++)
         {
             snake.get(i).draw(g);
         }
         for (int i = 0; i < apples.size(); i++)
         {
             apples.get(i).draw(g);
         }       	 
     }
     
     public void start()
     {
         running = true;
         thread = new Thread(this);
         thread.start();
     }
     
     public void stop()
     {
         running = false;
         
         String name = JOptionPane.showInputDialog(null, "Please enter name: ", 
                 "Save your score!", JOptionPane.CLOSED_OPTION);
         
         SaveScore(name);
          
         setVisible(false);
         
         final Frame[] frames = Frame.getFrames();
         for (final Frame frame : frames)
         {
             if (frame.isVisible() && frame.isActive())
             {
                 frame.dispose();
             }
         }
         try
         {
             thread.join();
         }
         catch (InterruptedException e)
         {
             e.printStackTrace();
         }
     }     
     
     private void SaveScore(String name) 
     {		
    	 try {
    		 FileWriter writer = new FileWriter(path, true); 
    		 writer.write(name + "," + score);
    		 writer.write(System.lineSeparator());
    		 writer.close();
    	 }
    	 catch(IOException ex) {
    	 }
     }

	public void run()
     {
         while (running)
         {
             tick();
             repaint();
         }
     }
     
     @Override
     public void keyPressed(KeyEvent e)
     {
         int key = e.getKeyCode();
         if (key == KeyEvent.VK_RIGHT && !left)
         {
             up = false;
             down = false;
             right = true;
         }
         if (key == KeyEvent.VK_LEFT && !right)
         {
             up = false;
             down = false;
             left = true;
         }
         if (key == KeyEvent.VK_UP && !down)
         {
             left = false;
             right = false;
             up = true;
         }
         if (key == KeyEvent.VK_DOWN && !up)
         {
             left = false;
             right = false;
             down = true;
         }
     }
     @Override
     public void keyReleased(KeyEvent arg0)
     {
     }
     public void keyTyped(KeyEvent arg0)
     {
     }
}
