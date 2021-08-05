package view;
import java.awt.*;
import java.net.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.*;
import db.ConnectSQL;
import model.Batsman;
import model.Bowler;
import controller.Match;

public class HandCricket extends JFrame {
	JPanel welcome;
	JPanel game;
	JPanel teamP;
	JPanel stats;
	JButton startGame;
	JFrame copy;
	String sTeam = "";
	String oTeam = "";
	JButton continueBtn;
	Batsman teamABat[];
	Bowler teamABowl[];
	static int count = 0;
	Batsman teamBBat[];
	Bowler teamBBowl[];
	int current_choice;
	public ArrayList<ArrayList<String>> l1, l2;
	ConnectSQL connection;
	Match play;
	int buttonClicked;
	volatile boolean isButtonClicked;
	ThreadBuilder firstInnings;
	ThreadBuilder secondInnings;
	JButton button[];
	public TextField displayScore, displayCommentary, tf, displayOvers, displayTarget;
	public JLabel scoreImages[];
	public JLabel batting;
	public JLabel bowling;
	public JLabel targetJb;
	public JTextArea newBatsman;
	public JTextArea newBowler;

	public HandCricket() {
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		copy = this;
		initialize();
		setTitle("Hand Cricket"); 
		connection = new ConnectSQL();
		chooseTeam();
		setVisible(true);
	}

	public void initialize() {
		welcome = new JPanel();
		welcome.setLayout(null);
		game = new JPanel();
		stats = new JPanel();
		teamP = new JPanel();
		JLabel imag = new JLabel();
		imag.setIcon(new ImageIcon("E:\\Welcome.png"));// Change the path to your location (...\src\Images\welcome.png)
		imag.setBounds(350, 100, 512, 256);
		welcome.add(imag);
		startGame = new JButton("Let's play");
		startGame.setBounds(534, 421, 99, 28);
		welcome.add(startGame);
		startGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				copy.remove(welcome);
				copy.add(teamP);
				copy.revalidate();
				copy.repaint();
			}
		});
		newBatsman = new JTextArea("");
		newBowler = new JTextArea("");
		this.add(welcome);
		this.setSize(1300, 800);
		game.setSize(300, 300);
		welcome.setSize(300, 300);
		teamP.setSize(300, 300);
	}
	public void chooseTeam() {
		teamP.setLayout(null);
		JLabel jb1 = new JLabel("Select your team");
		jb1.setBounds(400, 300, 1000, 20);
		teamP.add(jb1);
		JComboBox<String> jcb1 = new JComboBox<String>();
		JComboBox<String> jcb2 = new JComboBox<String>();
		jcb1.addItem("INDIA");
		jcb1.addItem("AUSTRALIA");
		jcb1.addItem("ENGLAND");
		jcb1.setBounds(400, 320, 250, 20);
		teamP.add(jcb1);
		JLabel jb2 = new JLabel("Select your opponent team");
		jb2.setBounds(400, 360, 1000, 20);
		teamP.add(jb2);
		jcb2.addItem("INDIA");
		jcb2.addItem("AUSTRALIA");
		jcb2.addItem("ENGLAND");
		jcb2.setBounds(400, 380, 250, 20);
		teamP.add(jcb2);
		
		//To select team1
		jcb1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sTeam = (String)jcb1.getSelectedItem();
			}
		});

		//To select team2
		jcb2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				oTeam = (String)jcb2.getSelectedItem();
			}
		});

		continueBtn = new JButton("Continue");
		continueBtn.setBounds(480, 420, 98, 30);
		teamP.add(continueBtn);
		
		continueBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				teamP.remove(jb1);
				teamP.remove(jb2);
				teamP.remove(jcb1);
				teamP.remove(jcb2);
				teamP.revalidate();
				teamP.remove(continueBtn);
				teamP.repaint();
				connectThem1();
			}
		});
	}

	public void connectThem1() {
		teamP.setLayout(null);
		JCheckBox checkboxes[] = new JCheckBox[11];
		l1 = connection.QueryListPlayers(sTeam);
		for (int i = 0; i < 11; i++) {
			checkboxes[i] = new JCheckBox(l1.get(i).get(1));
			checkboxes[i].setBounds(100, 100 + i * 40, 160, 23);
			teamP.add(checkboxes[i]);
			checkboxes[i].addItemListener(new MyListener());
		}
		JLabel jb1 = new JLabel("Select 6 players from the team");
		jb1.setBounds(400, 300, 1000, 20);
		teamP.add(jb1);
		continueBtn = new JButton("Continue");
		continueBtn.setBounds(400, 400, 98, 30);
		teamP.add(continueBtn);
		continueBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if(count != 6) {  
						throw new Exception(); 
					}
					teamABat = new Batsman[6];
					teamABowl = new Bowler[6];
					int cnt = 0;
					for (int i = 0; i < 11; i++) {
						if(checkboxes[i].isSelected()) {
							teamABat[cnt] = new Batsman(l1.get(i).get(0), l1.get(i).get(1));
							teamABowl[cnt++] = new Bowler(l1.get(i).get(0), l1.get(i).get(1));
						}
					}
					for (int i = 0; i < 11; i++) { 
						teamP.remove(checkboxes[i]);
					}
					teamP.remove(continueBtn);
					teamP.remove(jb1);
					teamP.revalidate();
					teamP.repaint();
					connectThem2();
				} catch(Exception et) {
					et.printStackTrace();
					for (int i = 0; i < 11; i++) {
						checkboxes[i].setSelected(false);
					}
					count = 0;
				}
			}
		});
	}
	public void connectThem2() {
		count = 0;
		JCheckBox checkboxes[] = new JCheckBox[11];
		l2 = connection.QueryListPlayers(oTeam);
		for (int i = 0;i < 11;i++) {
			checkboxes[i]=new JCheckBox(l2.get(i).get(1));
			checkboxes[i].setBounds(100, 100 + i * 40, 160, 23);
			teamP.add(checkboxes[i]);
			checkboxes[i].addItemListener(new MyListener());
		}

		JLabel jb1 = new JLabel("Select 6 players from the team");
		jb1.setBounds(400, 300, 1000, 20);
		teamP.add(jb1);
		JButton continue2 = new JButton("Continue");
		continue2.setBounds(400, 400, 98, 30);
		continue2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if(count != 6) {
					 	throw new Exception();
					}
					count = 0;
					teamBBat = new Batsman[7];
					teamBBowl = new Bowler[7];
					int cnt = 0;
					for (int i = 0; i < 11; i++) {
						if(checkboxes[i].isSelected()) {
							teamBBat[cnt] = new Batsman(l2.get(i).get(0), l2.get(i).get(1));
							teamBBowl[cnt] = new Bowler(l2.get(i).get(0), l2.get(i).get(1));
							cnt++;
						}
					}
					for (int i = 0; i < 11; i++) {
						teamP.remove(checkboxes[i]);
					}
					teamP.remove(continue2);
					teamP.remove(jb1);
					teamP.revalidate();
					teamP.repaint();
					playGame();
				} catch(Exception et) {
					et.printStackTrace();
					for (int i = 0; i < 11; i++) {
						checkboxes[i].setSelected(false);
					}
					count = 0;
				}
			}
		});
		teamP.add(continue2);
	}	

	class MyListener implements ItemListener{
		public void itemStateChanged(ItemEvent e) {
			int count2 = 0;
			if(e.getStateChange() == 1) {
				count2++;
			} else {
				count2--;
			}
			count += count2;
		}
	}

	class MyActionListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			
		}
	}

	public void playGame(){
		displayScore = new TextField("");
		displayOvers = new TextField("");
		displayCommentary = new TextField("");
		displayCommentary.setEditable(false);
		displayOvers.setEditable(false);
		
		displayScore.setEditable(false);
		scoreImages = new JLabel[14];
		batting = new JLabel();
		bowling = new JLabel();
		batting.setBounds(150, 20, 400, 400);
		bowling.setBounds(530, 20, 400, 400);
		newBatsman.setBounds(1000, 320, 300, 200);
		newBowler.setBounds(1000, 530, 300, 200);
		newBatsman.setEditable(false);
		newBowler.setEditable(false);
		batting.setIcon(new ImageIcon("E:\\L0.jpg"));// Change the path to your location (...\src\Images\L0.png)
		bowling.setIcon(new ImageIcon("E:\\R0.jpg"));// Change the path to your location (...\src\Images\R0.png)
		for (int i = 0; i < 14; i++) {
			scoreImages[i] = new JLabel();
		}
		for (int i = 0; i < 7; i++) {
			if (i != 5) {
				scoreImages[i].setIcon(new ImageIcon("E:\\L"+i+".jpg"));// Change the path to your location (...\src\Images\abcd.png)
				scoreImages[i].setBounds(150, 50, 250, 250);
			}
		}
		for (int i = 7;i < 14; i++)
			if(i != 12) {
				scoreImages[i].setIcon(new ImageIcon("E:\\R"+(i-7)+".jpg"));// Change the path to your location (...\src\Images\abcd.png)
				scoreImages[i].setBounds(400, 50, 250, 250);
			}
		displayScore.setBounds(300, 500, 600, 23);
		displayOvers.setBounds(300, 550, 100, 23);
		displayCommentary.setBounds(300, 600, 300, 23);
		isButtonClicked = false;
		teamP.setLayout(null);
		button = new JButton[6];
		for (int i = 0; i < 5; i++) {
			button[i] = new JButton(Integer.toString(i));	
			button[i].setBounds(200 + i * 75, 440 ,50, 23);
			button[i].addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					for (int i = 0; i < 6; i++) {
						if(e.getSource() == button[i]) {
							buttonClicked = Integer.parseInt(button[i].getText());
							isButtonClicked = true;
							break;
						}
					}
				}
			});
			teamP.add(button[i]);
		}
		button[5] = new JButton("6");
		button[5].setBounds(575, 440, 50, 23);
		button[5].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for (int i = 0;i < 6;i++) {
					if(e.getSource() == button[i]) {
						buttonClicked = Integer.parseInt(button[i].getText());
						isButtonClicked = true;
						break;
					}
				}
			}
		});
		teamP.add(button[5]);
		tf = new TextField("");
		tf.setEditable(false);
		JLabel scoreJb = new JLabel("Score: ");
		JLabel commJb = new JLabel("Commentary: ");
		JLabel oversJb = new JLabel("Overs: ");
		displayTarget = new TextField();
		displayTarget.setEditable(false);
		displayTarget.setBounds(300, 650, 50, 23);
		targetJb = new JLabel("Target: ");
		targetJb.setBounds(200, 650, 100, 23);
		scoreJb.setBounds(200, 500, 100, 23);
		commJb.setBounds(200, 600, 100, 23);
		oversJb.setBounds(200, 550, 100, 23);
		newBatsman.setBackground(getBackground());
		newBowler.setBackground(getBackground());
		teamP.add(scoreJb);
		teamP.add(commJb);
		teamP.add(oversJb);
		teamP.add(displayOvers);
		teamP.add(displayScore);
		teamP.add(batting);
		teamP.add(bowling);
		teamP.add(newBatsman);
		teamP.add(newBowler);
		teamP.add(displayCommentary);
		ThreadBuilder firstInnings = new ThreadBuilder(this);    
	}
	public static void main(String args[]) {
		HandCricket hm = new HandCricket();
	}
}
