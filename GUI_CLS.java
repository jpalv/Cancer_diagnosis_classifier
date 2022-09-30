package entrega_1;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.Color;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.text.DecimalFormat;
import java.awt.event.ActionEvent;

public class GUI_CLS {

	private JFrame frame;
	private JTextField textField;
	private JTextField textField_1;
	private BN rede;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI_CLS window = new GUI_CLS();
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
	public GUI_CLS() throws IOException {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 * @throws IOException 
	 */
	private void initialize() throws IOException {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 370);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		
	
		JFileChooser fc = new JFileChooser();											//Para mostrar os ficheiros deste diretório põe-se "."
		File path = new File(new File(".").getCanonicalPath());
		fc.setCurrentDirectory(path);
		
		JButton btnNewButton = new JButton("Selecionar ficheiro");					// Selecionar ficheiro
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					java.io.File file = fc.getSelectedFile();
					try {
						FileInputStream   f = new FileInputStream(new File(""+file));
						ObjectInputStream o = new ObjectInputStream(f);
						rede = (BN) o.readObject();
						o.close();
						f.close();
						textField_1.setText(" Ficheiro aceite");
				} catch (Exception e1) {
					textField_1.setText(" Ficheiro errado");
				}
					
					
					
				}
					}
				});
		btnNewButton.setBounds(282, 65, 140, 38);
		frame.getContentPane().add(btnNewButton);
		
		
		JButton btnClassificar = new JButton("Classificar");
		btnClassificar.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
		btnClassificar.setVisible(false);                               			 //Botão classificar
		btnClassificar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String line = textField.getText();
					String csvSplit   = ",";
					String[] v_str = line.split(csvSplit); 							// Dividir linha por ,
					int[] v_int = new int[v_str.length]; 							//Conta o nr de elementos separados por virgulas por linha.
					for (int l=0; l < v_str.length; l++) {							//Adiciona ao array b_element[] o respetivo valor da linha convertido de str para int.
						v_int[l] = Integer.parseInt(v_str[l]);}
					
					Classificador C = new Classificador(rede, v_int);
					DecimalFormat df2 = new DecimalFormat("#.##");
					textField_1.setText(" A classe assume o valor "+ (int)C.class_value()[0]+" com probabilidade de "+df2.format(C.class_value()[1]*100)+" %.");
														
				} catch (Exception e1) {
					textField_1.setText(" ERROR: Parâmetros inválidos");
					Toolkit.getDefaultToolkit().beep();
				}
			}	
	});
		
		btnClassificar.setBounds(282, 186, 140, 29);
		frame.getContentPane().add(btnClassificar);
		
		JLabel lblClassificador = new JLabel("Classificador");
		lblClassificador.setForeground(Color.DARK_GRAY);
		lblClassificador.setFont(new Font("Helvetica Neue", Font.PLAIN, 18));
		lblClassificador.setBounds(29, 16, 327, 26);
		frame.getContentPane().add(lblClassificador);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(29, 44, 393, 16);
		frame.getContentPane().add(separator_1);
		
		JLabel lblParmetrosDoPaciente = new JLabel("Parâmetros do paciente");
		lblParmetrosDoPaciente.setForeground(Color.DARK_GRAY);
		lblParmetrosDoPaciente.setFont(new Font("Helvetica Neue", Font.PLAIN, 11));
		lblParmetrosDoPaciente.setBounds(29, 110, 132, 26);
		frame.getContentPane().add(lblParmetrosDoPaciente);
		
		textField = new JTextField();									//Parametros paciente
		textField.setColumns(10);
		textField.setBounds(23, 136, 399, 38);
		frame.getContentPane().add(textField);
		
		textField.addActionListener(new ActionListener () {					//Acao do textfield parametros paciente
			public void actionPerformed(ActionEvent e) {
				if (textField.getText() != null) {
					try {
						
						btnClassificar.setVisible(true);
						
					} catch (Exception e1) {
						textField_1.setText(" ERROR: parametros invalidos.");		//Se o nr maximo de pais nao for valido devolve erro
						Toolkit.getDefaultToolkit().beep();
					}
				}
			}
		});
		
		
		
		JSeparator separator_1_1 = new JSeparator();
		separator_1_1.setBounds(29, 227, 393, 16);
		frame.getContentPane().add(separator_1_1);
		
		textField_1 = new JTextField();									//Output
		textField_1.setEditable(false);
		textField_1.setColumns(10);
		textField_1.setBounds(23, 281, 399, 38);
		frame.getContentPane().add(textField_1);
		
		JLabel lblOutput = new JLabel("Output");
		lblOutput.setForeground(Color.DARK_GRAY);
		lblOutput.setFont(new Font("Helvetica Neue", Font.PLAIN, 11));
		lblOutput.setBounds(29, 255, 132, 26);
		frame.getContentPane().add(lblOutput);
		
		JLabel lblNewLabel = new JLabel("Ficheiro com Rede de Bayes");
		lblNewLabel.setForeground(Color.DARK_GRAY);
		lblNewLabel.setFont(new Font("Helvetica Neue", Font.PLAIN, 11));
		lblNewLabel.setBounds(28, 70, 216, 29);
		frame.getContentPane().add(lblNewLabel);
		
	
		
		
		}
	}


