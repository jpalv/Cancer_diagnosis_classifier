package entrega_1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class BN implements Serializable{

	private static final long serialVersionUID = 1L;
	Amostra							am;
	grafoo							grafo;			
	ArrayList<double[][]>			DFOs;
	double[] 						theta_c;
	
	public BN(grafoo G, Amostra A, double S) throws Exception {
		this.grafo = G;
		this.am = A;
		this.DFOs = new ArrayList<double[][]>();
		int compG = G.getDim()-1;
		for (int i = 0; i < compG ; i++) {																//Ciclo que cria uma lista para cada variavel onde serao inseridos os valores dos DFO
			int dom_p = A.domain(G.parents(i));
			double[][] ti = new double[A.domain(i)][dom_p];												//Adiciona a lista de matrizes DFOs uma matriz com o nr de linhas igual ao dominio da variavel a que corresponde e o nr de colunas igual ao dominio dos seus pais.
			this.DFOs.add(ti);																			//Adiciona uma lista vaziz ao DFOs por cada variavel.
			ArrayList<Integer> vp_i = (ArrayList<Integer>) G.parents(i).clone();						//Cria uma lista com os pais da variavel i, sem contar com a classe.
			ArrayList<Integer> v_i = (ArrayList<Integer>) vp_i.clone();									//Cria o vetor de posicoes dos pais da variavel i
			v_i.add(0, i);																				//Adiciona ao v_i o indice da vari�vel em questao NO INICIO, obtendo o vetor de posi��es a incluir na funcao count para cada variavel i.
			ArrayList<ArrayList<ArrayList<Integer>>> comb = combTotal(G,A,i);							//Cria o vetor de posicoes da variavel i + os seus pais
			for (int j = 0 ; j < A.domain(i) ; j++) {
				for (int l = 0 ; l < comb.get(j).size() ; l++) {
					ArrayList<Integer> vv_j = (ArrayList<Integer>) comb.get(j).get(l).clone();									//Cria o vetor de valores a inserir na funcao count para cada posicao da lista de valores de DFO de cada variavel correspondente ao vetor j das combinacoes.											
					ArrayList<Integer> vvp_j = (ArrayList<Integer>) vv_j.clone();												//Pega no vetor de combinacoes anterior e tira-lhe o valor correspondente a classe, ficando apenas os valores assumidos pelos pais.
					vvp_j.remove(0);	
					DFOs.get(i)[j][l] = ((double)(A.count(v_i,vv_j) + S) / (A.count(vp_i,vvp_j) + (S * A.domain(i)))); } 	//Adiciona o DFO (theta_i) a sua posicao respetiva, ou seja na mesma posicao da lista de DFOs correspondente a uma dada variavel em que o vetor de valores usado na formula do theta_i esta na lista de listas resultantes da combTotal.
			}
		}
		double[] tc = new double[A.domain(compG)];														//O seguinte siclo calcula a lista de theta_c							
		for (int k = 0 ; k < A.domain(compG) ; k++) 													//A.domain_int(G.getDim()-1) da o dominio da "variavel" correspondente ao ultimo no do grafo, ou seja a classe.
			tc[k] = ((double) (A.count(compG,k) + S) / (A.length() + S * A.domain(compG)) );
		this.theta_c = tc;
	}
	
	public String toString() {
		return "BN [grafo=" + grafo + ", DFOs=" + DFOs + ", theta_c=" + Arrays.toString(theta_c) + "]";
	}
	
	public double[] gettheta_c() {
		return theta_c;
	}
	
	public double prob(int[] v) throws Exception {
		double prob_i = 0;
		for (int i = 0; i < DFOs.size() ; i++) {
			ArrayList<ArrayList<Integer>> combi = combi(grafo, am, i);
			ArrayList<Integer> vp = new ArrayList<Integer>();
			for (int l=0; l< grafo.parents(i).size(); l++) {
				vp.add(v[grafo.parents(i).get(l)]); }
			for (int j = 0; j < combi.size() ; j++) {				//DFOs.get(i)[v[i]].length=combi.size()
				if (combi.get(j).equals(vp)) {
					prob_i = prob_i + log2(DFOs.get(i)[v[i]][j]);} 
			}
		}
		double prob_c = 0;
		for (int j = 0; j < am.domain(grafo.getDim()-1) ; j++) {
			if (j == v[v.length-1]) {
				prob_c = log2(theta_c[j]);	}							//Calculo de log(P(C=c))
		}
		double prob = prob_c + prob_i;
		return prob;
	}
	
	public grafoo getgrafo() {
		return grafo;
	}
	
	private ArrayList<ArrayList<Integer>> combi(grafoo G, Amostra A, int f) { 				//Recebe uma amostra e um no e devolve todos as combinacoes possiveis 	
		ArrayList<Integer> p = G.parents(f);												//ArrayList com todos os pais
		int dp = A.domain(p);																//Dominio dos pais -> 'pi'i
		int n = p.size();																	//n e o numero de pais
		ArrayList<ArrayList<Integer>> total = new ArrayList<ArrayList<Integer>>();			//O que sera retornado. ArrayList de Arrays com todas as possiveis combinacoes dos pais
		for (int y = 0 ; y < dp ; y++) {
			ArrayList<Integer> t = new ArrayList<Integer>(n);
			for (int j = 0 ; j < n; j++) {
				t.add(0); }
			total.add(t);
		}
		int [] vdp = new int[n];															//Vetor com os dominios de cada pai 
		for (int i=0 ; i < n ; i++) {														//Ciclo para colocar em vdp os dominios de cada pai
			ArrayList<Integer> x = new ArrayList<Integer>();								//Cria um array com o int p[i] la dentro
			x.add(p.get(i));
			vdp[i] = A.domain(x);
		}
		for (int w = 0 ; w < dp; w++) {														//Ciclo para percorrer 'total' nos 'dp' diferentes arranjos e cria-los, para cada valor de w		
			int i = w;
			for (int pos = n - 1; pos >= 0; pos = pos - 1) {
				total.get(w).set(pos,(Integer) i%vdp[pos]); 
				i = i / vdp[pos];
			}
		}									
		return total;	
	}
	
	private ArrayList<ArrayList<ArrayList<Integer>>> combTotal(grafoo G, Amostra A, int f) throws Exception {
		ArrayList<ArrayList<ArrayList<Integer>>> combpf = new ArrayList<ArrayList<ArrayList<Integer>>>();
		for (int i = 0 ; i < A.domain(f) ; i++) {
			ArrayList<ArrayList<Integer>> combi = combi(G,A,f);
			combpf.add(combi);
			for (int j = 0 ; j < combi.size() ; j++) {
				combpf.get(i).get(j).add(0,i); }
		}
		return combpf;
	}
	
	private double log2(double f) {						
		return Math.log(f)/Math.log(2.0);
	}
	
	public void saveBN(String filename) throws IOException {					//Guarda um ficheiro com o nome filename com uma rede de bayes 

		FileOutputStream   f = new FileOutputStream(new File(filename));
		ObjectOutputStream o = new ObjectOutputStream(f);
		o.writeObject(this);
		o.close();
		f.close();
	}
	
	public static BN openBN(String filename) throws IOException, ClassNotFoundException {

		FileInputStream   f = new FileInputStream(new File(filename));
		ObjectInputStream o = new ObjectInputStream(f);
		BN rede = (BN) o.readObject();
		o.close();
		f.close();
		return rede;
	}

	
	public static void main(String[] args) {
		try {			
			Amostra Teste= new Amostra("hepatitis.csv");
			grafoo gr = new grafoo(11);
			int edges [][] = {{0, 1}, {0, 2}, {1, 2}, {2, 6}, {6, 4}, {5, 0}, {3, 2},{10,0},{10,1},{10,2},{10,3},{10,4},{10,5},{10,6},{10,7},{10,8},{10,9}};
			for (int [] edge : edges) 
				gr.add_edge(edge[0], edge[1]); 
			BN rede = new BN(gr , Teste , 0.5);
			int[] v = {0,0,0,1,1,0,1,1,1,1,1};
			//System.out.println(rede);
			System.out.println(rede.prob(v));
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

