package entrega_1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class AmostraLOO extends Amostra {

	private static final long serialVersionUID = 1L;
	int[] vetor;												//sera armazenado o vetor que for retirado a amostra
	int valorcorreto;											//sera armazenado aqui o valor da classe de vetor

	
	public AmostraLOO(String filename, int x) { 				//Método construtor quase igual a um dos da Amostra. Apenas recebe tambem um inteiro correspondente a posicao do vetor na amostra que sera retirado para ser classificado
		
		BufferedReader br = null;
		String line       = "";
		String csvSplit   = ",";
		this.amostra = new ArrayList<int []>();
		this.dom = new int[0];
		
		try {	//So le o ficheiro se houver um com o nome que inserimos
				// Criar um buffer para o descritor do ficheiro com nome filename
			br = new BufferedReader(new FileReader(filename));
			try {
				while ( (line = br.readLine()) != null ) { 				// Enquanto ha linhas
					String[] a_element = line.split(csvSplit); 			// Dividir linha por ,
					int[] b_element = new int[a_element.length]; 		//Conta o nr de elementos separados por virgulas por linha.
					for (int l=0; l < a_element.length; l++) {			//Adiciona ao array b_element[] o respetivo valor da linha convertido de str para int.
						b_element[l] = Integer.parseInt(a_element[l]);
					}
					amostra.add(b_element);								//Adiciona o elemento criado, correspondente a uma linha, a amostra
					if (dom.length==0)					  				//Se este for o primeiro elemento da lista, estabelecemos o comprimento de dom (vazio) igual ao comprimento de v e preenchemo-lo com zeros em todas as entradas.
						this.dom = new int[b_element.length]; 
					for (int i=0 ; i < b_element.length ; i++) {  		//Comparamos o dominio de cada variavel com o valor+1 dessa variavel no novo elemento (vetor v). O comprimento de v tem de ser igual ao de dom.
						dom[i] = Math.max(dom[i], b_element[i]+1);
				}
				}
				int[] v;
				v = amostra.remove(x);								//Guarda o vetor que é retirado a amostra e que será posteriormente classificado
				int[] vetor = new int[v.length-1];
				for (int l=0;l<(v.length-1);l++) {
					vetor[l]=v[l];}
				this.vetor = vetor;
				valorcorreto = v[v.length-1];					//Valor da classe correto para o vetor, será comparado com o resultado da classificação para verificar a correção do programa
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
}
	
	public int getvalorcorreto() {	
		return valorcorreto;
	}
	
	public int[] getvetor() {
		return vetor;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}