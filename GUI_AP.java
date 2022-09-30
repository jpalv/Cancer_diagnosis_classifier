package entrega_1;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Color;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.awt.event.ActionEvent;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTextArea;



public class GUI_AP {

	private JFrame frame;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JLabel lblPseudocontagens;
	private JLabel lblAprendizagemDaRede;
	private JSeparator separator_1;
	private JLabel lblSelecionarFicheiro;
	private JSeparator separator_2;
	private JSeparator separator;
	private JLabel lblOutput;
	private JTextArea textArea;
	private JButton btnAprender;
	private Amostra am;
	private Aprendizagem ap;
	private JTextField textField_3;

	/**
	 * Launch the application.
	 */
	public GUI_AP() throws IOException {
		initialize(); 
	}
	

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI_AP window = new GUI_AP();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * @throws IOException 
	 */
	

	/**
	 * Initialize the contents of the frame.
	 * @throws IOException 
	 */
	private void initialize() throws IOException {
		frame = new JFrame();
		frame.setBounds(100, 100, 350, 520);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JTextArea textArea = new JTextArea();
		textArea.setRows(2);
		textArea.setBounds(74, 431, 246, 38);
		frame.getContentPane().add(textArea);
		textArea.setEditable(false);
		
		textField = new JTextField();										// nr max de pais
		textField.setBounds(193, 139, 127, 38);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		textField.setEditable(false);
		
		textField.addActionListener(new ActionListener () {					//Acao do textfield nr max pais
			public void actionPerformed(ActionEvent e) {
				if (textField.getText() != null) {
					try {
						int max_p = Integer.parseInt(textField.getText());
						textArea.setText(" ");
					} catch (Exception e1) {
						textArea.setText(" ERROR: nr maximos de pais invalido.");		//Se o nr maximo de pais nao for valido devolve erro
					}
				}
			}
		});
		
		textField_1 = new JTextField();										// nr grafos inicial
		textField_1.setColumns(10);
		textField_1.setBounds(193, 189, 127, 38);
		frame.getContentPane().add(textField_1);
		textField_1.setEditable(false);
		
		textField_1.addActionListener(new ActionListener () {					//Acao do textfield nr grafos inicial
			public void actionPerformed(ActionEvent e) {
				if (textField_1.getText() != null) {
					try {
						int nr_graf = Integer.parseInt(textField_1.getText());
						textArea.setText(" ");
					} catch (Exception e1) {
						textArea.setText(" ERROR: nr grafos inicial invalido.");		//Se o nr grafos inicial nao for valido devolve erro
					}
				}
			}
		});
		
		textField_2 = new JTextField();										// pseudo-contagens
		textField_2.setColumns(10);
		textField_2.setBounds(193, 240, 127, 38);
		frame.getContentPane().add(textField_2);
		textField_2.setEditable(false);
		
		textField_2.addActionListener(new ActionListener () {					//Acao do textfield pseudo contagens
			public void actionPerformed(ActionEvent e) {
				if (textField_2.getText() != null) {
					try {
						double S = Double.parseDouble(textField_2.getText());
						textArea.setText(" ");
					} catch (Exception e1) {
						textArea.setText(" ERROR: valor pseudo contagens \n invalido.");		//Se as pseudo contagens nao for valido devolve erro
					}
				}
			}
		});
		
		
		textField_3 = new JTextField();														//Text field do nome do ficheiro
		textField_3.setBounds(168, 317, 155, 38);
		frame.getContentPane().add(textField_3);
		textField_3.setColumns(10);
		
		textField_3.addActionListener(new ActionListener () {					
			public void actionPerformed(ActionEvent e) {
				if (textField_3.getText() != null) {
					try {
						
					} catch (Exception e1) {
						
					}
				}
			}
		});
		
		JFileChooser fc = new JFileChooser();											//Para mostrar os ficheiros deste diretório põe-se "."
		File path = new File(new File(".").getCanonicalPath());
		fc.setCurrentDirectory(path);
		
	
		
		
		
		JButton btnNewButton = new JButton("file.csv");
		btnNewButton.setFont(new Font("Helvetica Neue", Font.PLAIN, 11));
		btnNewButton.setForeground(Color.GRAY);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					java.io.File file = fc.getSelectedFile();
					try {
						am = new Amostra(""+file);
						textField.setEditable(true);									//Quando selecionamos um ficheiro os campos de texto passam a ser editaveis
						textField_1.setEditable(true);
						textField_2.setEditable(true);
						btnAprender.setVisible(true);
						textArea.setText(" Ficheiro aceite.");   // o que aparece no output se o ficheiro inserido funcionar
						
					} catch (Exception e1) {
						textArea.setText(" ERROR: Ficheiro nao aceite.");       // o que aparece no output se o ficheiro inserido não funcionar
						Toolkit.getDefaultToolkit().beep();
					}
					
					
			
		}
			}
		});
		btnNewButton.setBounds(193, 64, 126, 36);
		frame.getContentPane().add(btnNewButton);
		btnNewButton.setIcon(null);
		
		
		JLabel lblNewLabel = new JLabel("Nº máximo de pais");
		lblNewLabel.setForeground(Color.DARK_GRAY);
		lblNewLabel.setFont(new Font("Helvetica Neue", Font.PLAIN, 11));
		lblNewLabel.setBounds(29, 139, 101, 38);
		frame.getContentPane().add(lblNewLabel);
		
		JLabel lblNDeGrafos = new JLabel("Nº de grafos inicial");
		lblNDeGrafos.setForeground(Color.DARK_GRAY);
		lblNDeGrafos.setFont(new Font("Helvetica Neue", Font.PLAIN, 11));
		lblNDeGrafos.setBounds(29, 189, 101, 38);
		frame.getContentPane().add(lblNDeGrafos);
		
		
		lblPseudocontagens = new JLabel("Pseudo-contagens");
		lblPseudocontagens.setForeground(Color.DARK_GRAY);
		lblPseudocontagens.setFont(new Font("Helvetica Neue", Font.PLAIN, 11));
		lblPseudocontagens.setBounds(29, 240, 101, 38);
		frame.getContentPane().add(lblPseudocontagens);
		
		lblAprendizagemDaRede = new JLabel("Aprendizagem da Rede de Bayes");
		lblAprendizagemDaRede.setForeground(Color.DARK_GRAY);
		lblAprendizagemDaRede.setFont(new Font("Helvetica Neue", Font.PLAIN, 18));
		lblAprendizagemDaRede.setBounds(29, 16, 327, 26);
		frame.getContentPane().add(lblAprendizagemDaRede);
		
		separator_1 = new JSeparator();
		separator_1.setBounds(29, 44, 291, 16);
		frame.getContentPane().add(separator_1);
		
		lblSelecionarFicheiro = new JLabel("Selecionar ficheiro");
		lblSelecionarFicheiro.setForeground(Color.DARK_GRAY);
		lblSelecionarFicheiro.setFont(new Font("Helvetica Neue", Font.PLAIN, 11));
		lblSelecionarFicheiro.setBounds(29, 70, 101, 26);
		frame.getContentPane().add(lblSelecionarFicheiro);
		
		separator_2 = new JSeparator();
		separator_2.setBounds(29, 110, 291, 16);
		frame.getContentPane().add(separator_2);
		
		separator = new JSeparator();
		separator.setBounds(29, 299, 291, 16);
		frame.getContentPane().add(separator);
		
		lblOutput = new JLabel("Output");
		lblOutput.setForeground(Color.DARK_GRAY);
		lblOutput.setFont(new Font("Helvetica Neue", Font.PLAIN, 11));
		lblOutput.setBounds(29, 431, 101, 38);
		frame.getContentPane().add(lblOutput);
		
		
		btnAprender = new JButton("Aprender");													//Botao aprender
		btnAprender.setVisible(false);
		btnAprender.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					ap = new Aprendizagem(Integer.parseInt(textField_1.getText()), Integer.parseInt(textField.getText()), Double.parseDouble(textField_2.getText()), am, textField_3.getText());
					textArea.setText(" Rede de Bayes aprendida \n e guardada como "+textField_3.getText());            						// o que aparece no output se o ficheiro inserido funcionar
					
				} catch (Exception e1) {
					textArea.setText(" ERROR: Nao é possivel aprender \n esta rede de Bayes.");       // o que aparece no output se o ficheiro inserido não funcionar		
					Toolkit.getDefaultToolkit().beep();
				}
			}	
	});
		
		btnAprender.setForeground(Color.BLACK);
		btnAprender.setBackground(Color.WHITE);
		btnAprender.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
		btnAprender.setBounds(192, 389, 128, 29);
		frame.getContentPane().add(btnAprender);
		
		JSeparator separator_3 = new JSeparator();
		separator_3.setBounds(29, 367, 291, 16);
		frame.getContentPane().add(separator_3);
		
		
		
		JLabel lblNewLabel_1 = new JLabel("Nome do ficheiro gerado");
		lblNewLabel_1.setForeground(Color.DARK_GRAY);
		lblNewLabel_1.setFont(new Font("Helvetica Neue", Font.PLAIN, 11));
		lblNewLabel_1.setBounds(29, 327, 127, 16);
		frame.getContentPane().add(lblNewLabel_1);
		
		
	
	}
}



