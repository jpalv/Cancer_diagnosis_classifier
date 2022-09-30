package entrega_1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class Classificador implements Serializable{

	private static final long serialVersionUID = 1L;

	int  	c;	//Corresponde ao valor mais provavel para a classe dado um vetor de variáveis
	double  p;	//Corresponde a probabilidade de a classe assumir o valor c
	
	
	public Classificador(BN rede , int[] v1) throws Exception {			//Recebe valores para todas as variáveis de um paciente (sem a classe) e devolve o valor mais provável para a classe
		if (v1.length +1 == rede.getgrafo().getDim()) {
			double probc = -1000000000;
			ArrayList<Double> prob = new ArrayList<Double>();
			double ptotal = 0;											//Vai corresponder à soma das probabilidades de a classe assumir todos os valores possíveis
			int[] v = new int[v1.length +1]; 
			for (int l = 0 ; l < v1.length ; l++)
				v[l] = v1[l];
			for (int i=0 ; i < rede.gettheta_c().length ; i++) {
				v[v.length-1] = i;
				prob.add(Math.pow(2,rede.prob(v)));						//Dá o vetor onde na entrada i se encontra a probabilidade de a classe assumir o valor i dado o vetor de valores para as variáveis recebido                                   
				if (rede.prob(v) > probc) {
					probc = rede.prob(v);
					this.c = i; }
			}
			for (double j : prob) {
				ptotal += j; }
			this.p = prob.get(c)/ptotal;
		}
		else {
			throw new Exception("Erro: Dimensão do vetor de parâmetros errada. ");} 
	}

	public double[] class_value() {
		double[] x = {c,p};
		return x;
	}

public static void main(String[] args) throws Exception {
//	Amostra cancro= new Amostra("bcancer.csv");
//	Amostra diabetes= new Amostra("diabetes.csv");
//	Amostra hepatitis= new Amostra("hepatitis.csv");
//	Amostra thyroid= new Amostra("thyroid.csv");
//	Aprendizagem Ap1 = new Aprendizagem(0 , 4 , 0.5 , cancro , "Teste_Ap_BC");
//	Aprendizagem Ap2 = new Aprendizagem(4 , 4 , 0.5 , diabetes , "Teste_Ap_Diabetes");
//	Aprendizagem Ap3 = new Aprendizagem(4 , 4 , 0.5 , hepatitis , "Teste_Ap_Hepatite");
//	Aprendizagem Ap4 = new Aprendizagem(4 , 4 , 0.5 , thyroid , "Teste_Ap_Tiróide");
//	System.out.println(Ap1);
//	System.out.println(Ap1.RedeBayes(cancro, 0.5));
//	int[] bc = {1,0,1,3,0,1,1,1,1,1/*,1*/};
//	int[] h = {0,0,0,1,1,1,0,1,1,0,0,0,0,0,0,0,0,1,0/*,0*/};
//	int[] t = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,4/*,1*/};
//	Classificador Cbc = new Classificador(Ap1.RedeBayes() , bc);
//	Classificador Cbh = new Classificador(Ap3.RedeBayes() , h2);
//	Classificador Cbt = new Classificador(Ap4.RedeBayes(5) , t2);
//	System.out.println(Arrays.toString(Cbc.class_value()));
//	System.out.println(Arrays.toString(Cbh.class_value()));
//	System.out.println(Arrays.toString(Cbt.class_value()));

	
	//	Leave One OUT	//
	
	// Para testar o LOO basta alterar o valor da string FILENAME
	// e os parametros do metodo construtor da Aprendizagem Ap
	
	//Caso se queira realizar este método apenas numa parte da amostra (classificar uma parte mas treinar com toda) pode-se escolher esse range ao definir o int comeca e o int lines (correspondem ao limite inferior e superior desse range)
	
	
	String FILENAME = "bcancer.csv";
	BufferedReader bufferedReader = new BufferedReader(new FileReader(FILENAME));
    int lines = 0;
    while(bufferedReader.readLine() != null)
        lines++;																		//Conta o numero de linhas do ficheiro csv
    int comeca = 0;
    int corretos=0;
    for (int l=comeca; l<lines ; l++) {														//Sera feita a amostra, a aprendizagem e a classificacao tantas vezes quantas as linhas do ficheiro csv
    	AmostraLOO amostra = new AmostraLOO(FILENAME, l);								
    	Aprendizagem Ap = new Aprendizagem(4 , 4 , 0.5 , amostra , "Not_used_in_LOO");
    	Classificador C = new Classificador(Ap.RedeBayes() , amostra.getvetor());
    	System.out.println("Line: "+(l+1));
    	System.out.println((int)C.class_value()[0]);
    	System.out.println(C.class_value()[1]);
    	System.out.println(amostra.valorcorreto);
    	System.out.println("");
    	if ((int)C.class_value()[0] == amostra.valorcorreto) {							
    		corretos ++;}
    	System.out.println((double)corretos/(l-comeca+1) * 100);
    	System.out.println("");
    }
    System.out.println("FIM");

//    // FIM LOO //
}
}