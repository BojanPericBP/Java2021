import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import java.awt.Font;

public class FormaPodaci {
	static public JTextArea textArea;
	public FormaPodaci() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @wbp.parser.entryPoint
	 */
	void showWindow()
	{
		JFrame frame = new JFrame("Podaci o kretanjima");
		frame.setBounds(100,100,892,584);
		frame.getContentPane().setLayout(null);
		
		textArea = new JTextArea();
		textArea.setBounds(29, 69, 776, 428);
		frame.getContentPane().add(textArea);
		textArea.setEditable(false);
		textArea.setBackground(Color.pink);
		
		JLabel lblNewLabel = new JLabel("Podaci o kretanju kompozicija");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel.setBounds(41, 37, 233, 22);
		frame.getContentPane().add(lblNewLabel);
		frame.setVisible(true);
	}
}
