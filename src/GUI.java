import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Timer;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class GUI extends JFrame 
{
	private static final long serialVersionUID = 1L;
	static final int MAT_SIZE = 30;
	
	static int[] trenutniBrVozilaNaPutevima = {0, 0, 0};
	static char[][] mapa;
	static ArrayList<ZeljeznickaStanica> stanice = new ArrayList<>(5);
	static boolean btnFlagStart = false;
	
	static private GridLayout gridLayout;
	static private JPanel contentPanel;
	static JFrame frame;
	JButton btnKretanje;
	JButton btnStart;
	static public JPanel[][] guiMapa;
	KreiranjeKompozicija kreiranjeKompozicija;
	static boolean simulacijaUToku;
	Timer timer;

	private void initialize()
	{
		stanice.add(new ZeljeznickaStanica('A', new ArrayList<Point>()));
		stanice.get(0).koordinate.add(new Point(27,2));
		
		stanice.add(new ZeljeznickaStanica('B', new ArrayList<Point>()));
		stanice.get(1).koordinate.add(new Point(6,6));
		stanice.get(1).koordinate.add(new Point(6,7));
		
		stanice.add(new ZeljeznickaStanica('C', new ArrayList<Point>()));
		stanice.get(2).koordinate.add(new Point(12,19));
		stanice.get(2).koordinate.add(new Point(12,20));
		stanice.get(2).koordinate.add(new Point(13,20));
		stanice.get(2).koordinate.add(new Point(13,19));
		
		stanice.add(new ZeljeznickaStanica('D', new ArrayList<Point>()));
		stanice.get(3).koordinate.add(new Point(1,26));
		
		stanice.add(new ZeljeznickaStanica('E', new ArrayList<Point>()));
		stanice.get(4).koordinate.add(new Point(25,26));
	}
	
	public static void main(String[] args) throws Exception
	{
		new GUI();

		/*Automobil v1 = new Automobil(10,'A',"car.png");
		guiMapa[21][0].add(new JLabel(new ImageIcon(v1.putanjaSlike)));
		Automobil v2 = new Automobil(10,'A',"car.png");
		guiMapa[21][3].add(new JLabel(new ImageIcon(v1.putanjaSlike)));
		v2.trKoo.x = v2.preKoo.x = 21;
		v2.trKoo.y = v2.preKoo.y =0;
		
		v2.smjer = '1';
		simulacijaUToku = true;
		v2.run();*/
		
	}

	public static void refreshGui()
	{
		GUI.frame.invalidate();
		GUI.frame.validate();
		GUI.frame.repaint();
	}
	
	public GUI() 
	{
		frame = new JFrame("NASLOV");
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				simulacijaUToku=false;
				 if(timer!=null) timer.cancel();
				 e.getWindow().dispose();
				 System.exit(0);
			}
		});
		frame.setSize(213,68);
		
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
		btnStart.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				btnStart.setEnabled(false);
				simulacijaUToku=true;
				stanice.get(0).start();
				stanice.get(1).start();
				stanice.get(2).start();
				stanice.get(3).start();
				stanice.get(4).start();
				kreiranjeKompozicija=new KreiranjeKompozicija();
				kreiranjeKompozicija.start();
				timer=new Timer();
				timer.schedule(new KreiranjeVozila(), 0, 500);
			}
		});
		leftPanel.add(btnStart);
		btnStart.setBounds(5, 5, 15, 30);
		
		btnKretanje = new JButton("Podaci");
		btnKretanje.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 FormaPodaci formaPodaci = new FormaPodaci();
				 formaPodaci.showWindow();
	
			}
		});
		
		
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
		guiMapa[9][27].setBackground(Color.green);
		mapa[9][27]='p';
		guiMapa[3][23].setBackground(Color.green);
		mapa[3][23]='p';
		guiMapa[4][23].setBackground(Color.green);
		mapa[4][23]='p';
		guiMapa[2][26].setBackground(Color.green);
		guiMapa[2][27].setBackground(Color.green);
		guiMapa[27][1].setBackground(Color.green);
		guiMapa[28][1].setBackground(Color.green);
		guiMapa[5][6].setBackground(Color.green);
		guiMapa[5][7].setBackground(Color.green);
		
		mapa[27][2] = 's';
		
		guiMapa[5][7].add(new JLabel(new ImageIcon("resource/station.png")));
		guiMapa[2][27].add(new JLabel(new ImageIcon("resource/station.png")));
		guiMapa[26][25].add(new JLabel(new ImageIcon("resource/station.png")));
		
		guiMapa[27][1].add(new JLabel(new ImageIcon("resource/a.png")));
		guiMapa[27][2].add(new JLabel(new ImageIcon("resource/stop.png")));
		guiMapa[28][1].add(new JLabel(new ImageIcon("resource/station.png")));
		mapa[27][2] = mapa[28][2] = 's';
		guiMapa[6][6].add(new JLabel(new ImageIcon("resource/stop.png")));
		guiMapa[6][7].add(new JLabel(new ImageIcon("resource/stop.png")));
		guiMapa[5][6].add(new JLabel(new ImageIcon("resource/b.png")));
		mapa[6][6] = 's'; mapa[6][7] = 's';
		guiMapa[12][19].add(new JLabel(new ImageIcon("resource/stop.png")));
		guiMapa[13][19].add(new JLabel(new ImageIcon("resource/c.png")));
		guiMapa[12][20].add(new JLabel(new ImageIcon("resource/stop.png")));
		guiMapa[13][20].add(new JLabel(new ImageIcon("resource/stop.png")));
		mapa[12][20]='s';
		
		guiMapa[1][26].add(new JLabel(new ImageIcon("resource/stop.png")));
		guiMapa[2][26].add(new JLabel(new ImageIcon("resource/d.png")));
		mapa[1][26] = mapa[1][27] = 's';
		guiMapa[25][25].add(new JLabel(new ImageIcon("resource/e.png")));
		guiMapa[25][26].add(new JLabel(new ImageIcon("resource/stop.png")));
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
