package snake;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.text.Highlighter.Highlight;

import java.awt.GridBagLayout;
import javax.swing.JButton;
import java.awt.GridBagConstraints;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Component;
import javax.swing.Box;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.FormSpecs;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.awt.event.ActionEvent;


public class Menu extends JFrame {

	/**
	 * Launch the application.
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private static final int WIDTH = 800, HEIGHT = 600;
	private String path = "src/Scores.txt";
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {				
					Menu menu = new Menu();
					menu.setUndecorated(false);
					menu.setFocusable(true);
					menu.setPreferredSize(new Dimension(WIDTH, HEIGHT));
					menu.setResizable(false);
					menu.pack();
					menu.setLocationRelativeTo(null);
					menu.setVisible(true);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	
	public Menu()  {
		setTitle("Snake game");		
		getContentPane().setLayout(null);
		
		JButton StartButton = new JButton("Start");
		StartButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFrame game = new JFrame();
				Screen screen = new Screen(WIDTH,HEIGHT);
				game.getContentPane().add(screen);
				game.setUndecorated(true);
				game.setResizable(false);
				game.pack();
				game.setLocationRelativeTo(null);
				game.setVisible(true);
				//getContentPane().setVisible(false);
			}
		});		
		
		StartButton.setFont(new Font("Britannic Bold", Font.PLAIN, 24));
		StartButton.setBounds((WIDTH / 2) - 177 / 2, 100, 177, 59);
		getContentPane().add(StartButton);
		
		JButton HighscoreButton = new JButton("High scores");
		HighscoreButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					
					String highScores = GetHighScoreString();
					JOptionPane.showMessageDialog(null, highScores, "Top 10 high scores!", JOptionPane.CLOSED_OPTION);
				}
				catch(Exception ex) {
					
				}
			}
		});
		
		HighscoreButton.setFont(new Font("Britannic Bold", Font.PLAIN, 24));
		HighscoreButton.setBounds((WIDTH / 2) - 177 / 2, 200, 177, 59);
		getContentPane().add(HighscoreButton);
		
		JButton CloseButton = new JButton("Close");
		CloseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		CloseButton.setFont(new Font("Britannic Bold", Font.PLAIN, 24));
		CloseButton.setBounds((WIDTH / 2) - 177 / 2, 300, 177, 59);
		getContentPane().add(CloseButton);

	}
	
	private String GetHighScoreString() throws Exception  {
		List<HighscoreLine> highScores = ReadLinesFromFile();
		//highScores = highScores.stream().sorted(x ->)
		
		highScores = highScores.stream().sorted(Comparator.comparing(HighscoreLine::getScore).reversed())
				.collect(Collectors.toList());
		
		StringBuffer sb = new StringBuffer();
		for(int i =0 ; i < highScores.size(); i++) {
			sb.append((i + 1) + ". " + highScores.get(i).name + " " + highScores.get(i).score);
			sb.append(System.lineSeparator());
		}
		return sb.toString();
	}
	
	private List<HighscoreLine> ReadLinesFromFile() throws Exception 
	{
		List<HighscoreLine> result = new ArrayList<HighscoreLine>();	
		
		Scanner scanner = new Scanner(new File(this.path));
		
		while(scanner.hasNextLine()) 
		{
			result.add(ParseLine(scanner.nextLine()));
		}
		scanner.close();
		
		return result;
	}

	private HighscoreLine ParseLine(String nextLine) throws Exception 
	{
		String[] values = nextLine.split(",");
		
		if(values.length != 2) //odczytano wiêcej ni¿ 2 liczby z pojedynczego wiersza
			throw new Exception("Missing data in file!");
		
		return new HighscoreLine(values[0], Integer.parseInt(values[1]));
	}
}
