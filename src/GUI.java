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
	
	static int[] currVheicleCounter = {0, 0, 0};
	static ArrayList<TrainStation> trainStations = new ArrayList<>(5);
	KreiranjeKompozicija kreiranjeKompozicija;
	Timer timer;
	
	static private GridLayout gridLayout;
	JPanel contentPanel;
	static JFrame frame;
	JButton btnData;
	JButton btnStart;
	static public JPanel[][] trainMap;
	static boolean isAlive;

	private void initialize()
	{
		trainStations.add(new TrainStation('A', new ArrayList<Point>()));
		trainStations.get(0).coordinates.add(new Point(27,2));
		
		trainStations.add(new TrainStation('B', new ArrayList<Point>()));
		trainStations.get(1).coordinates.add(new Point(6,6));
		trainStations.get(1).coordinates.add(new Point(6,7));
		
		trainStations.add(new TrainStation('C', new ArrayList<Point>()));
		trainStations.get(2).coordinates.add(new Point(12,19));
		trainStations.get(2).coordinates.add(new Point(12,20));
		trainStations.get(2).coordinates.add(new Point(13,20));
		trainStations.get(2).coordinates.add(new Point(13,19));
		
		trainStations.add(new TrainStation('D', new ArrayList<Point>()));
		trainStations.get(3).coordinates.add(new Point(1,26));
		
		trainStations.add(new TrainStation('E', new ArrayList<Point>()));
		trainStations.get(4).coordinates.add(new Point(25,26));
	}
	
	public static void main(String[] args) throws Exception
	{
		new GUI();
	}
	
	public GUI() 
	{
		frame = new JFrame("Iz kuce je izleteo na ulicu Iva");
		
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				isAlive=false;
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

		trainMap = new JPanel[MAT_SIZE][MAT_SIZE];
		
		JPanel leftPanel = new JPanel();
		leftPanel.setSize(50,1200);
		frame.getContentPane().add(leftPanel,BorderLayout.WEST);
		
		btnStart = new JButton("Start");
		btnStart.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				isAlive=true;
				trainStations.get(0).start();
				trainStations.get(1).start();
				trainStations.get(2).start();
				trainStations.get(3).start();
				trainStations.get(4).start();
				kreiranjeKompozicija=new KreiranjeKompozicija();
				kreiranjeKompozicija.start();
				timer=new Timer();
				timer.schedule(new CreateVehicle(), 0, 2000);
				
				btnStart.setEnabled(false);
			}
		});
		leftPanel.add(btnStart);
		btnStart.setBounds(5, 5, 15, 30);
		
		btnData = new JButton("Podaci");
		btnData.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 DataForm formaPodaci = new DataForm();
				 formaPodaci.showWindow();
			}
		});
		
		leftPanel.add(btnData);
		btnData.setBounds(15, 5, 15, 30);
		
		for (int i = 0; i < MAT_SIZE; i++) {
			for (int j = 0; j < MAT_SIZE; j++) {
				trainMap[i][j]= new JPanel(); 
				trainMap[i][j].setSize(20,20);
				trainMap[i][j].setBackground(new Color(222, 225, 227));
				contentPanel.add(trainMap[i][j]);
			}
		}
		
		for (int i = 0; i < MAT_SIZE; i++) {
			for (int j = 0; j < MAT_SIZE; j++) {
				
				//setovanje plavih polja
				if(j==13)
					trainMap[i][j].setBackground(new Color(1, 150, 200));
				
				else if(j==14)
					trainMap[i][j].setBackground(new Color(0, 150, 200));
				
				else if((i==20) && (j<9 || j > 20))
					trainMap[i][j].setBackground(new Color(0, 150, 200));
				
				else if(i==21 && (j<9 || j>20))
					trainMap[i][j].setBackground(new Color(1, 150, 200));
				
				else if(i>21 && (j==8 || j == 21 ))
					trainMap[i][j].setBackground(new Color(0, 150, 200));
				
				else if(i>21 && (j==7 || j== 22))
					trainMap[i][j].setBackground(new Color(1, 150, 200));
				
				if((j==2 && i>15) || (i>17 && i< 27 && j==26) || (i==1 && j>22 && j<28))
					trainMap[i][j].setBackground(Color.LIGHT_GRAY);
				else if(j==5 && i>5 && i<17)
					trainMap[i][j].setBackground(Color.LIGHT_GRAY);
				
				else if((i==18 && j>19 && j<27) || (i==6 && j>5 && j<19))
					trainMap[i][j].setBackground(Color.LIGHT_GRAY);
				
				else if((j==20 && i>11 && i<18) || (j==19 && i>5 && i<14))
					trainMap[i][j].setBackground(Color.LIGHT_GRAY);
				
				else if((i==25 && j>24) || (i==12 && j>20 && j<27) || (j==22 && i>0 && i< 4))
					trainMap[i][j].setBackground(Color.LIGHT_GRAY);
				
				else if((j == 26 && i>8 && i < 13)|| (j==28 && i > 4 && i < 10) || (i==5 && j>22 && j<29))
					trainMap[i][j].setBackground(Color.LIGHT_GRAY);
			}
		}
		trainMap[21][8].setBackground(new Color(0,150,200));
		trainMap[21][21].setBackground(new Color(0,150,200));
		
		trainMap[16][3].setBackground(Color.LIGHT_GRAY);
		trainMap[16][4].setBackground(Color.LIGHT_GRAY);
		trainMap[26][25].setBackground(Color.LIGHT_GRAY);
		trainMap[9][27].setBackground(Color.LIGHT_GRAY);
		trainMap[3][23].setBackground(Color.LIGHT_GRAY);
		trainMap[4][23].setBackground(Color.LIGHT_GRAY);
		trainMap[2][26].setBackground(Color.LIGHT_GRAY);
		trainMap[2][27].setBackground(Color.LIGHT_GRAY);
		trainMap[27][1].setBackground(Color.LIGHT_GRAY);
		trainMap[28][1].setBackground(Color.LIGHT_GRAY);
		trainMap[5][6].setBackground(Color.LIGHT_GRAY);
		trainMap[5][7].setBackground(Color.LIGHT_GRAY);
		
		trainMap[27][2].setBackground(Color.yellow);
		trainMap[27][1].setBackground(Color.yellow);
		trainMap[28][2].setBackground(Color.yellow);
		trainMap[28][1].setBackground(Color.yellow);
		
		trainMap[5][7].add(new JLabel(new ImageIcon("resource/station.png")));
		trainMap[2][27].add(new JLabel(new ImageIcon("resource/station.png")));
		trainMap[26][25].add(new JLabel(new ImageIcon("resource/station.png")));
		
		trainMap[27][1].add(new JLabel(new ImageIcon("resource/a.png")));
		trainMap[27][2].add(new JLabel(new ImageIcon("resource/stop.png")));
		trainMap[28][1].add(new JLabel(new ImageIcon("resource/station.png")));
		
		
		trainMap[6][6].add(new JLabel(new ImageIcon("resource/stop.png")));
		trainMap[6][7].add(new JLabel(new ImageIcon("resource/stop.png")));
		trainMap[5][6].add(new JLabel(new ImageIcon("resource/b.png")));
		
		
		trainMap[6][6].setBackground(Color.yellow);
		trainMap[6][7].setBackground(Color.yellow);
		trainMap[5][6].setBackground(Color.yellow);
		trainMap[5][7].setBackground(Color.yellow);
		
		trainMap[12][19].add(new JLabel(new ImageIcon("resource/stop.png")));
		trainMap[13][19].add(new JLabel(new ImageIcon("resource/c.png")));
		trainMap[12][20].add(new JLabel(new ImageIcon("resource/stop.png")));
		trainMap[13][20].add(new JLabel(new ImageIcon("resource/stop.png")));
		trainMap[12][19].setBackground(Color.yellow);
		trainMap[12][20].setBackground(Color.yellow);
		trainMap[13][19].setBackground(Color.yellow);
		trainMap[13][20].setBackground(Color.yellow);
		
		trainMap[1][26].add(new JLabel(new ImageIcon("resource/stop.png")));
		trainMap[2][26].add(new JLabel(new ImageIcon("resource/d.png")));
		trainMap[25][25].add(new JLabel(new ImageIcon("resource/e.png")));
		trainMap[25][26].add(new JLabel(new ImageIcon("resource/stop.png")));
		

		trainMap[25][25].setBackground(Color.yellow);
		trainMap[25][26].setBackground(Color.yellow);
		trainMap[26][25].setBackground(Color.yellow);
		trainMap[26][26].setBackground(Color.yellow);
		
		
		trainMap[1][27].setBackground(Color.yellow);
		trainMap[1][26].setBackground(Color.yellow);
		trainMap[2][27].setBackground(Color.yellow);
		trainMap[2][26].setBackground(Color.yellow);

		trainMap[20][2].setBackground(Color.orange);
		trainMap[21][2].setBackground(Color.orange);
		trainMap[6][13].setBackground(Color.orange);
		trainMap[6][14].setBackground(Color.orange);
		trainMap[20][26].setBackground(Color.orange);
		trainMap[21][26].setBackground(Color.orange);

		
		frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE );
		frame.pack();
		frame.setResizable(true);
		frame.setLocationRelativeTo( null );
		frame.setVisible(true);
		frame.setSize(1200,800);
		
		initialize();
		
	}
	
	public static void refreshGui()
	{
		GUI.frame.invalidate();
		GUI.frame.validate();
		GUI.frame.repaint();
	}
	
}
