import java.awt.*;
import javax.swing.*;
import java.util.Timer;
import java.util.ArrayList;
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
	FormaPodaci formaPodaci;
	
	static final Color DIGNUTA_RAMPA = Color.black;
	static final Color SPUSTENA_RAMPA = Color.red;
	
	public static void main(String[] args) throws Exception
	{
		new GUI();
	}

	public GUI() 
	{
		frame = new JFrame(" PJ2 PROJEKAT");
		frame.addWindowListener(new WindowAdapter() 
		{
			@Override
			public void windowClosing(WindowEvent e) 
			{
				if(timer!=null) timer.cancel();
				simulacijaUToku=false;
				Kompozicija.handler.close();
				FormaPodaci.handlerFajl.close();
				Vozilo.handler.close();
				Kamion.handler.close();
				Automobil.handler.close();
				KreiranjeVozila.handler.close();
				KreiranjeKompozicija.handler.close();
				ZeljeznickaStanica.handler.close();
				Vagon.handler.close();
				Lokomotiva.handler.close();
				e.getWindow().dispose();
			}
		});

		
		contentPanel= new JPanel();
		gridLayout = new GridLayout(30,30,1,1);
		gridLayout.setHgap(0);
		gridLayout.setVgap(0);
		contentPanel.setLayout(gridLayout);
		frame.getContentPane().add(contentPanel, BorderLayout.CENTER);

		guiMapa = new JPanel[MAT_SIZE][MAT_SIZE];
		mapa = new char[MAT_SIZE][MAT_SIZE];
		
		JPanel panelZaButtone = new JPanel();
		panelZaButtone.setSize(20,1200);
		panelZaButtone.setLayout(new FlowLayout(FlowLayout.CENTER,200,10));
		frame.getContentPane().add(panelZaButtone,BorderLayout.SOUTH); //pozicija dugmadi
		
		formaPodaci = new FormaPodaci();
		
		
		btnKretanje = new JButton("Podaci");
		btnKretanje.setBounds(15, 5, 15, 30);
		btnKretanje.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				formaPodaci.prikaziPodatke();
				formaPodaci.frame.setVisible(true);
			}
		});
		
		
		btnStart = new JButton("Start");
		btnStart.setBounds(5, 5, 15, 30);
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
				timer.schedule(new KreiranjeVozila(), 0, 1000); //TODO promjena brzine generisanja vozila
			} 
		});
		panelZaButtone.add(btnStart);
		panelZaButtone.add(btnKretanje);
		
		
		
		for (int i = 0; i < MAT_SIZE; i++) {
			for (int j = 0; j < MAT_SIZE; j++) {
				guiMapa[i][j]= new JPanel(); 
				guiMapa[i][j].setSize(20,20);
				guiMapa[i][j].setBackground(Color.WHITE);
				guiMapa[i][j].setBorder(BorderFactory.createSoftBevelBorder(1));
				contentPanel.add(guiMapa[i][j]);
			}
		}
		
		final Color myBlue = new Color(141, 179, 226);
		
		for (int i = 0; i < MAT_SIZE; i++) 
		{
			for (int j = 0; j < MAT_SIZE; j++) 
			{
				
				//setovanje plavih polja
				if(j==13)
				{
					guiMapa[i][j].setBackground(myBlue);
					mapa[i][j] = '1'; 
				}
				
				else if(j==14)
				{
					guiMapa[i][j].setBackground(myBlue);
					mapa[i][j] = '0'; 
				}
				
				
				else if((i==20) && (j<9 || j > 20))
				{
					guiMapa[i][j].setBackground(myBlue);
					mapa[i][j] = '0';
				}
				
				else if(i==21 && (j<9 || j>20))
				{
					guiMapa[i][j].setBackground(myBlue);
					mapa[i][j] = '1';
				}
				
				else if(i>21 && (j==8 || j == 21 ))
				{
					guiMapa[i][j].setBackground(myBlue);
					mapa[i][j] = '0';					
				}
				
				else if(i>21 && (j==7 || j== 22))
				{
					guiMapa[i][j].setBackground(myBlue);
					mapa[i][j] = '1';	
				}
				
				//setovanje sivih polja
				if((j==2 && i>15) || (i>17 && i< 27 && j==26) || (i==1 && j>22 && j<28))
				{
					guiMapa[i][j].setBackground(Color.LIGHT_GRAY);
					mapa[i][j] = 'p';
				}
				else if(j==5 && i>5 && i<17)
				{
					guiMapa[i][j].setBackground(Color.LIGHT_GRAY);
					mapa[i][j] = 'p';					
				}
				
				else if((i==18 && j>19 && j<27) || (i==6 && j>5 && j<19))
				{
					mapa[i][j] = 'p';
					guiMapa[i][j].setBackground(Color.LIGHT_GRAY);
				}
				
				else if((j==20 && i>11 && i<18) || (j==19 && i>5 && i<14))
				{
					guiMapa[i][j].setBackground(Color.LIGHT_GRAY);
					mapa[i][j] = 'p';					
				}
				
				else if((i==25 && j>24) || (i==12 && j>20 && j<27) || (j==22 && i>0 && i< 4))
				{
					guiMapa[i][j].setBackground(Color.LIGHT_GRAY);
					mapa[i][j] = 'p';					
				}
				
				else if((j == 26 && i>8 && i < 13)|| (j==28 && i > 4 && i < 10) || (i==5 && j>22 && j<29))
				{
					guiMapa[i][j].setBackground(Color.LIGHT_GRAY);
					mapa[i][j] = 'p';					
				}
			}
		}
		
		guiMapa[16][3].setBackground(Color.LIGHT_GRAY);
		mapa[16][3]='p';
		guiMapa[16][4].setBackground(Color.LIGHT_GRAY);
		mapa[16][4]='p';
		guiMapa[26][25].setBackground(Color.LIGHT_GRAY);
		guiMapa[9][27].setBackground(Color.LIGHT_GRAY);
		mapa[9][27]='p';
		guiMapa[3][23].setBackground(Color.LIGHT_GRAY);
		mapa[3][23]='p';
		guiMapa[4][23].setBackground(Color.LIGHT_GRAY);
		mapa[4][23]='p';
		guiMapa[2][26].setBackground(Color.LIGHT_GRAY);
		guiMapa[2][27].setBackground(Color.LIGHT_GRAY);
		guiMapa[27][1].setBackground(Color.LIGHT_GRAY);
		guiMapa[28][1].setBackground(Color.LIGHT_GRAY);
		guiMapa[5][6].setBackground(Color.LIGHT_GRAY);
		guiMapa[5][7].setBackground(Color.LIGHT_GRAY);
		
		mapa[27][2] = 's';
		
		guiMapa[27][1].add(new JLabel("A"));
		guiMapa[28][1].add(new JLabel("A"));
		guiMapa[27][2].add(new JLabel("A"));
		guiMapa[28][2].add(new JLabel("A"));
		mapa[27][2] = mapa[28][2] = 's';
		guiMapa[6][6].add(new JLabel("B"));
		guiMapa[6][7].add(new JLabel("B"));
		guiMapa[5][6].add(new JLabel("B"));
		guiMapa[5][7].add(new JLabel("B"));
		mapa[6][6] = 's'; mapa[6][7] = 's';
		guiMapa[12][19].add(new JLabel("C"));
		guiMapa[13][19].add(new JLabel("C"));
		guiMapa[12][20].add(new JLabel("C"));
		guiMapa[13][20].add(new JLabel("C"));
		mapa[12][20]='s';
		guiMapa[1][26].add(new JLabel("D"));
		guiMapa[1][27].add(new JLabel("D"));
		guiMapa[2][26].add(new JLabel("D"));
		guiMapa[2][27].add(new JLabel("D"));
		mapa[1][26] = mapa[1][27] = 's';
		guiMapa[25][25].add(new JLabel("E"));
		guiMapa[25][26].add(new JLabel("E"));
		guiMapa[26][25].add(new JLabel("E"));
		guiMapa[26][26].add(new JLabel("E"));
		mapa[25][26] = 's';
		mapa[21][21] = mapa[21][8] = '0';
		mapa[26][26]= mapa [25][25] = mapa [29][2] = mapa [25][27] = 0;
		mapa[13][20] = mapa[13][19] = mapa[12][19] = 's';
		
		
		//setovanje pruznih prelaza
		guiMapa[20][2].setBackground(DIGNUTA_RAMPA);
		mapa[20][2]='x';
		guiMapa[21][2].setBackground(DIGNUTA_RAMPA);
		mapa [21][2] ='x';
		guiMapa[6][13].setBackground(DIGNUTA_RAMPA);
		mapa [6][13] ='x';
		guiMapa[6][14].setBackground(DIGNUTA_RAMPA);
		mapa [6][14] ='x';
		guiMapa[20][26].setBackground(DIGNUTA_RAMPA);
		mapa [20][26] = 'x';
		guiMapa[21][26].setBackground(DIGNUTA_RAMPA);
		mapa [21][26] ='x';

		
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		frame.pack();
		frame.setResizable(true);
		frame.setLocationRelativeTo( null );
		frame.setVisible(true);
		frame.setSize(1300,900);
		frame.setMinimumSize(new Dimension(1000, 850));
		
		initialize();
	}
	
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
}
