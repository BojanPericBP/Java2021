import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;



public class GUI extends JFrame {

	private static final long serialVersionUID = 1L;
	static final int MAT_SIZE = 30;
	
	static int[] trenutniBrVozilaNaPutevima = {0, 0, 0};
	static char[][] mapa;
	static ArrayList<ZeljeznickaStanica> stanice = new ArrayList<>(5);

	
	static private GridLayout gridLayout;
	static private JPanel contentPanel;
	static JFrame frame;
	JButton btnKretanje;
	JButton btnStart;
	static public JPanel[][] guiMapa;
	/**
	 * Launch the application.
	 */

	private void initialize()
	{
		stanice.add(new ZeljeznickaStanica('A', new ArrayList<Koordinate>()));
		stanice.get(0).koordinate.add(new Koordinate(27,2));
		
		stanice.add(new ZeljeznickaStanica('B', new ArrayList<Koordinate>()));
		stanice.get(1).koordinate.add(new Koordinate(6,6));
		stanice.get(1).koordinate.add(new Koordinate(6,7));
		
		stanice.add(new ZeljeznickaStanica('C', new ArrayList<Koordinate>()));
		stanice.get(2).koordinate.add(new Koordinate(12,19));
		stanice.get(2).koordinate.add(new Koordinate(12,20));
		stanice.get(2).koordinate.add(new Koordinate(13,20));
		stanice.get(2).koordinate.add(new Koordinate(13,19));
		
		stanice.add(new ZeljeznickaStanica('D', new ArrayList<Koordinate>()));
		stanice.get(3).koordinate.add(new Koordinate(1,26));
		
		stanice.add(new ZeljeznickaStanica('E', new ArrayList<Koordinate>()));
		stanice.get(4).koordinate.add(new Koordinate(25,26));
	}
	
	public static void main(String[] args) throws Exception
	{
		
		GUI gui = new GUI();

		ZeljeznickaStanica a = stanice.get(0);
		ZeljeznickaStanica b = stanice.get(1);
		ZeljeznickaStanica c = stanice.get(2);
		ZeljeznickaStanica d = stanice.get(3);
		ZeljeznickaStanica e = stanice.get(4);
		
		ZeljeznickaStanica polazak = a;
		ZeljeznickaStanica odrediste = b;
		
		
		
		Kompozicija komp = new Kompozicija(2, 3, "LU", 0.5, polazak, odrediste,"lokomotiva.png");
		polazak.redUStanici.add(komp);
		
		//Kompozicija komp1 = new Kompozicija(2, 3, "LU", 0.5, polazak, odrediste,"train.png");
		//polazak.redUStanici.add(komp1);
		
		polazak.run();
		odrediste.run();
		
		//c.run();
		//b.run();
		
		//stanice.get(0).redUStanici.add(komp1);
		/*
		a.run();
		b.run();
		c.run();
		d.run();
		e.run();
		*/
		/*a.run();
		for (int i = 0; i < 1; i++) {
			b.run();
		}*/
		//a.wait();
		//b.wait();
		
		/*
		for (int i = 0; i < 1; i++) {
			SwingUtilities.updateComponentTreeUI(gui);
			b.run();
			SwingUtilities.updateComponentTreeUI(gui);
		}*/
		
		//b.start();
		
		/*Timer timer = new Timer();
		timer.schedule(new KreiranjeVozila(), 0, 500);*/
	
		//Thread.sleep(5000);
		//timer.cancel();
/*		
		Vozilo v1 = new Vozilo(1, 'C');
		Vozilo v2 = new Vozilo(1, 'C');
		Vozilo v3 = new Vozilo(1, 'C');
		
		v1.trKoo.i = 20;
		v1.trKoo.j = 23;
		v1.preKoo.i = 20;
		v1.preKoo.j = 24;
		v1.smjer = '0';
		v1.trenutnaBrzina = 500;
		
		v2.trKoo.i = 20;
		v2.trKoo.j = 24;
		v2.preKoo.i = 20;
		v2.preKoo.j = 25;
		v2.smjer = '0';
		v2.trenutnaBrzina = 400;
		
		v3.trKoo.i = 20;
		v3.trKoo.j = 25;
		v3.preKoo.i = 20;
		v3.preKoo.j = 26;
		v3.smjer = '0';
		v3.trenutnaBrzina = 400;
		
		/*v4.trKoo.i = 15;
		v4.trKoo.j = 14;
		v4.preKoo.i = 16;
		v4.preKoo.j = 14;
		v4.smjer = '0';
		v4.trenutnaBrzina = 1200;
		
		v5.trKoo.i = 22;
		v5.trKoo.j = 14;
		v5.preKoo.i = 23;
		v5.preKoo.j = 14;
		v5.smjer = '0';
		v5.trenutnaBrzina = 900;*/
		
/*
		guiMapa[v1.trKoo.i][v1.trKoo.j].add(new JLabel(new ImageIcon("car.png")));
		((JLabel)guiMapa[v1.trKoo.i][v1.trKoo.j].getComponents()[0]).setName(""+(long)v1.trenutnaBrzina);
		
		guiMapa[v2.trKoo.i][v2.trKoo.j].add(new JLabel(new ImageIcon("car.png")));
		((JLabel)guiMapa[v2.trKoo.i][v2.trKoo.j].getComponents()[0]).setName(""+(long)v2.trenutnaBrzina);
		
		guiMapa[v3.trKoo.i][v3.trKoo.j].add(new JLabel(new ImageIcon("car.png")));
		((JLabel)guiMapa[v3.trKoo.i][v3.trKoo.j].getComponents()[0]).setName(""+(long)v3.trenutnaBrzina);
		
		/*guiMapa[v4.trKoo.i][v4.trKoo.j].add(new JLabel(new ImageIcon("car.png")));
		((JLabel)guiMapa[v4.trKoo.i][v4.trKoo.j].getComponents()[0]).setName(""+(long)v4.trenutnaBrzina);
		
		guiMapa[v5.trKoo.i][v5.trKoo.j].add(new JLabel(new ImageIcon("car.png")));
		((JLabel)guiMapa[v5.trKoo.i][v5.trKoo.j].getComponents()[0]).setName(""+(long)v5.trenutnaBrzina);*/
/*		v1.start();
		v2.start();
		v3.start();
		//v4.start();
		//v5.start();
		
		
		//TOOD pomjeranje automobila kod
		/*JLabel test1 = new JLabel(new ImageIcon("C:\\Users\\Bojan\\Desktop\\matrica.png"));
		JLabel test2 = new JLabel(new ImageIcon("C:\\Users\\Bojan\\Desktop\\matrica.png"));
	
		
		
		guiMapa[20][i].add(test1);
		Thread.sleep(2000);
		guiMapa[20][i-1].add(test1);
		SwingUtilities.updateComponentTreeUI(frame);*/
		
	}

	/**
	 * Create the frame.
	 */
	
	public GUI() {

		frame = new JFrame("NASLOV");
		frame.setSize(450,348);
		
		contentPanel= new JPanel();
		gridLayout = new GridLayout(30,30,1,1);
		contentPanel.setLayout(gridLayout);
		frame.getContentPane().add(contentPanel, BorderLayout.CENTER);

		guiMapa = new JPanel[MAT_SIZE][MAT_SIZE];
		mapa = new char[MAT_SIZE][MAT_SIZE];
		
		JPanel leftPanel = new JPanel();
		leftPanel.setSize(50,1200);
		frame.getContentPane().add(leftPanel,BorderLayout.WEST);
		
		btnStart = new JButton("Start");
		leftPanel.add(btnStart);
		btnStart.setBounds(5, 5, 15, 30);
		
		btnKretanje = new JButton("Podaci");
		
		
		leftPanel.add(btnKretanje);
		btnKretanje.setBounds(15, 5, 15, 30);
		
		for (int i = 0; i < MAT_SIZE; i++) {
			for (int j = 0; j < MAT_SIZE; j++) {
				guiMapa[i][j]= new JPanel(); 
				guiMapa[i][j].setSize(20,20);
				guiMapa[i][j].setBackground(Color.LIGHT_GRAY);
				contentPanel.add(guiMapa[i][j]);
			}
		}
		
		for (int i = 0; i < MAT_SIZE; i++) {
			for (int j = 0; j < MAT_SIZE; j++) {
				
				//setovanje plavih polja
				if(j==13)
				{
					guiMapa[i][j].setBackground(Color.cyan);
					mapa[i][j] = '1'; 
				}
				
				else if(j==14)
				{
					guiMapa[i][j].setBackground(Color.cyan);
					mapa[i][j] = '0'; 
				}
				
				
				else if((i==20) && (j<9 || j > 20))
				{
					guiMapa[i][j].setBackground(Color.cyan);
					mapa[i][j] = '0';
				}
				
				else if(i==21 && (j<9 || j>20))
				{
					guiMapa[i][j].setBackground(Color.cyan);
					mapa[i][j] = '1';
				}
				
				else if(i>21 && (j==8 || j == 21 ))
				{
					guiMapa[i][j].setBackground(Color.cyan);
					mapa[i][j] = '0';					
				}
				
				else if(i>21 && (j==7 || j== 22))
				{
					guiMapa[i][j].setBackground(Color.cyan);
					mapa[i][j] = '1';	
				}
				
				//setovanje sivih polja
				if((j==2 && i>15) || (i>17 && i< 27 && j==26) || (i==1 && j>22 && j<28))
				{
					guiMapa[i][j].setBackground(Color.green);
					mapa[i][j] = 'p';
				}
				else if(j==5 && i>5 && i<17)
				{
					guiMapa[i][j].setBackground(Color.green);
					mapa[i][j] = 'p';					
				}
				
				else if((i==18 && j>19 && j<27) || (i==6 && j>5 && j<19))
				{
					mapa[i][j] = 'p';
					guiMapa[i][j].setBackground(Color.green);
				}
				
				else if((j==20 && i>11 && i<18) || (j==19 && i>5 && i<14))
				{
					guiMapa[i][j].setBackground(Color.green);
					mapa[i][j] = 'p';					
				}
				
				else if((i==25 && j>24) || (i==12 && j>20 && j<27) || (j==22 && i>0 && i< 4))
				{
					guiMapa[i][j].setBackground(Color.green);
					mapa[i][j] = 'p';					
				}
				
				else if((j == 26 && i>8 && i < 13)|| (j==28 && i > 4 && i < 10) || (i==5 && j>22 && j<29))
				{
					guiMapa[i][j].setBackground(Color.green);
					mapa[i][j] = 'p';					
				}
			}
		}
		
		guiMapa[16][3].setBackground(Color.green);
		mapa[16][3]='p';
		guiMapa[16][4].setBackground(Color.green);
		mapa[16][4]='p';
		guiMapa[26][25].setBackground(Color.green);
		//mapa[26][25]='p';
		guiMapa[9][27].setBackground(Color.green);
		mapa[9][27]='p';
		guiMapa[3][23].setBackground(Color.green);
		mapa[3][23]='p';
		guiMapa[4][23].setBackground(Color.green);
		mapa[4][23]='p';
		guiMapa[2][26].setBackground(Color.green);
		//mapa[2][26]='p';
		guiMapa[2][27].setBackground(Color.green);
		//mapa[2][27]='p';
		guiMapa[27][1].setBackground(Color.green);
		//mapa[27][1]='p';
		guiMapa[28][1].setBackground(Color.green);
		//mapa[28][1]='p';
		guiMapa[5][6].setBackground(Color.green);
		//mapa[5][6]='p';
		guiMapa[5][7].setBackground(Color.green);
		//mapa[5][7]='p';
		
		mapa[27][2] = 's';
		
		guiMapa[28][1].add(new JLabel("A"));
		mapa[27][2] = mapa[28][2] = 's';
		guiMapa[6][6].add(new JLabel("B"));
		mapa[6][6] = 's'; mapa[6][7] = 's';
		guiMapa[13][19].add(new JLabel("C"));
		mapa[12][20]='s';
		guiMapa[2][26].add(new JLabel("D"));
		mapa[1][26] = mapa[1][27] = 's';
		guiMapa[26][25].add(new JLabel("E"));
		mapa[25][26] = 's';
		mapa[21][21] = mapa[21][8] = '0';
		mapa[26][26]= mapa [25][25] = mapa [29][2] = mapa [25][27] = 0;
		mapa[13][20] = mapa[13][19] = mapa[12][19] = 's';
		//setovanje pruznih prelaza
		guiMapa[20][2].setBackground(Color.orange);
		mapa[20][2]='x';
		guiMapa[21][2].setBackground(Color.orange);
		mapa [21][2] ='x';
		guiMapa[6][13].setBackground(Color.orange);
		mapa [6][13] ='x';
		guiMapa[6][14].setBackground(Color.orange);
		mapa [6][14] ='x';
		guiMapa[20][26].setBackground(Color.orange);
		mapa [20][26] = 'x';
		guiMapa[21][26].setBackground(Color.orange);
		mapa [21][26] ='x';

		
		frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE );
		frame.pack();
		frame.setResizable(true);
		frame.setLocationRelativeTo( null );
		frame.setVisible(true);
		frame.setSize(1200,800);
		
		initialize();
		
	}
	
	
}
