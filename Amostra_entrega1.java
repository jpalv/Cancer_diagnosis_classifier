package entrega_1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class Amostra_entrega1 implements Serializable{
	
	private static final long serialVersionUID = 1L;

	ArrayList<int []> amostra; 		//O nosso objeto "amostra" e' uma ArrayList, ou seja, e' um vetor com tamanho variavel, cujas componentes sao arrays (tamanho fixo).
	int[]			 	  dom; 		//Lista do dominio de todas as variaveis dos elementos da amostra (Xi) e a classe (c).
	
	public Amostra_entrega1() { 				//Metodo construtor que permite gerar uma nova amostra vazia.
		this.amostra = new ArrayList<int []>();
		this.dom = new int[0];		}
	
	public Amostra_entrega1(String filename) { //Metodo construtor que permite receber uma amostra de um ficheiro csv, onde em cada linha se encontra uma amostra com os valores das várias variaveis separados por virgulas.
			
			BufferedReader br = null;
			String line       = "";
			String csvSplit   = ",";
			this.amostra = new ArrayList<int []>();
			this.dom = new int[0];
			
			try {	//So le o ficheiro se hou ver um com o nome que inserimos
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
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
	}

	@Override
	public String toString() { //Este metodo serve essencialmente para fazer testes
		String str ="";
		for (int[] v : amostra)
			str += Arrays.toString(v)+"\n";
		return str;
	}
	
	void add(int[] v) { 						//Este metodo adiciona um vetor a ArrayList. Usamos o tipo de dados void, quando um metodo nao tem return de nada, ou seja, faz return de void. Neste caso isso acontece, porque estamos so a adicionar algo a amostra e nao queremos obter nada.
		amostra.add(v);
		if (dom.length==0)					  	//Se este for o primeiro elemento da lista, estabelecemos o comprimento de dom (vazio) igual ao comprimento de v e preenchemo-lo com zeros em todas as entradas.
			this.dom = new int[v.length]; 
		for (int i=0 ; i < v.length ; i++) {  	//Comparamos o dominio de cada variavel com o valor+1 dessa variavel no novo elemento (vetor v). O comprimento de v tem de ser igual ao de dom.
			dom[i] = Math.max(dom[i], v[i]+1);
		}
	}

	int length() { //Este metodo devolve o tamanho da amostra
		return amostra.size();
	}
	
	int[] element(int p) { //Este metodo devolve o elemento da amostra na posiçao p
		return amostra.get(p);
	}

	int domain(ArrayList<Integer> vp) { 	//Com esta funçao pegamos na lista dos dominios de cada variavel que ja temos no atributo dom e fazemos o produto de todos, de maneira a obter o domínio da amostra. 
		int y=1;
		for (int i=0 ; i<vp.size() ; i++) {
			y=y*dom[vp.get(i)]; }
		return y;
	}
	
	int count(ArrayList<Integer> var, ArrayList<Integer> val) { 	//var e val têm de ter o mesmo comprimento
		int x=0;
		for (int j=0 ; j < amostra.size() ; j++) {
			int i=0;
			while (i < var.size() && amostra.get(j)[var.get(i)]==val.get(i)) {
				i++;
			}
			if (i == var.size()) {x++ ; }
		}
		return x;
	}
	
	
	public static void main(String[] args) {
		
		
	}
}


