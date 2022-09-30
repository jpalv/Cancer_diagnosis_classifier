package entrega_1;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class grafoo_entrega1 implements Serializable{
	
	private static final long serialVersionUID = 1L;

	int                           dim;	//nr de nos (nr variáveis mais classe)
	ArrayList<ArrayList<Integer>> adj;	//lista de adjacência
	
	/*Para definir um grafo orientado neste caso, o que faz mais sentido e' usar uma lista de listas de inteiros, onde 
	 em cada lista de inteiros são incluidos os nos pais do no correspondente a essa lista de inteiros na lista de listas.*/ 
	
	public grafoo_entrega1(int dim) {							//Se escrevermos new grafoo_entrega1(n) gera um novo grafo orientado vazio com dimensão n
		this.dim = dim;  										//Define a dimensão do grafo
		this.adj = new ArrayList<ArrayList<Integer>>(dim);		//Cria uma nova lista de listas vazia (sem listas)
		for (int i = 0; i < dim; i++) {							//Este ciclo serve para inserir na lista de listas, n listas de inteiros vazias
			adj.add(new ArrayList<Integer>());
		}
	}
	
	@Override
	public String toString() {
		return "Grafo_Orientado [dim=" + dim + ", adj=" + adj + "]";
	}
	
	protected boolean edgeQ(int p, int f) throws Exception {
		return adj.get(f).contains(p);							//Vai à lista da posição f (nó filho) e confirma se nessa lista se encontra p(nó pai)
	}
	
	public void add_edge(int p, int f) throws Exception {
		if (!edgeQ(p,f) && !connected(f,p)) {					//Para adicionarmos uma aresta temos de agrantir que ela não existe já, que não vamos criar um caminho cíclico e que um filho não fica com mais de 4 pais 
			adj.get(f).add(p);									//Vai a lista da posição f (no filho) e adiciona a essa lista p(no pai)
			Collections.sort(adj.get(f));	}					//Ordena os pais 
	}
	
	public void remove_edge(int p, int f) throws Exception{
		adj.get(f).remove((Integer) p); 					
	}
	
	public void invert_edge(int p, int f) throws Exception {
		if (edgeQ(p,f)) {
			adj.get(f).remove((Integer) p);					//Vai a lista da posição f (no filho) e adiciona a essa lista p(no pai) e vai à lista da posição p (no pai) e remove a essa lista f(no filho). 
			if (!connected(p,f)) {							//Garante que ao inverter a aresta, não estamos a criar um ciclo
				adj.get(p).add(f);	}
			else {
				adj.get(f).add(p); }}
	}

	public boolean connected(int p, int f) throws Exception{
		boolean [] visited       = new boolean[dim];			//cria uma lista de boleanos com a dimensão igual ao nº total de nós com todos os elementos iguais a false. Por definicao, o novo boleano começa sempre com valor logico falso.
		ArrayList<Integer> queue = new ArrayList<Integer>();	//cria uma lista vazia
		queue.add(f); 											//adiciona f no final da lista queue, ou seja, partimos do filho e vamos tentar seguir um caminho inverso ate p (pai). Se conseguirmos, significa que ha um caminho de p para f
		boolean found = false;
		while (!queue.isEmpty() && !found) {
			int first = queue.remove(0); 						//À queue e retirado o primeiro elemento (posição 0) e first e' igual a esse elemento
			if (first == p)										//Se encontramos p, então existe caminho
				found = true;
			if (!visited[first]) {								//Se na poscao first do vetor visited estiver false, entao quer dizer que esse no ainda nao tinha sido visitado. Logo marcamo-lo como visitado e adicionamos os pais desse no a queue para repetirmos o mesmo processo para todos os nos pais. Seguindo este raciocinio ou encontramos um caminho ou analisamos todos os caminhos possivel e concluimos que nao existe nenhum.
				visited[first] = true;							
				for (int x : parents(first)) 
					queue.add(x);
			}
		}
		return found;
	}
	
	public ArrayList<Integer> parents(int f) {	 		//Devolve uma lista de todos os pais do no f, contando com a classe, que e pai de todos os nos.
			return adj.get(f);
		}

	public ArrayList<Integer> parents_c(int f) {		//Devolve uma lista de todos os pais do no f, sem contar com a classe.
		ArrayList<Integer> x = new ArrayList<Integer>();
		x=adj.get(f);
		x.remove(x.size()-1);
		return x;
		}
	
	private ArrayList<ArrayList<Integer>> combi(Amostra A, int f) { 						//Recebe uma amostra e um no e devolve todos os dominios possiveis 	
			ArrayList<Integer> p = parents(f);												//ArrayList com todos os pais
			int dp = A.domain(p);															//Domínio dos pais -> 'pi'i
			int n = p.size();																//n e o numero de pais
			ArrayList<ArrayList<Integer>> total = new ArrayList<ArrayList<Integer>>();		//O que sera retornado. ArrayList de Arrays com todas as possiveis combinacoes dos pais
			for (int y = 0 ; y < dp ; y++) {
				ArrayList<Integer> t = new ArrayList<Integer>(n);
				for (int j = 0 ; j < n; j++)
					t.add(0);
				total.add(t);
			}
			int [] vdp = new int[n];														//Vetor com os dominios de cada pai 
			for (int i=0 ; i < n ; i++) {													//Ciclo para colocar em vdp os dominios de cada pai
				ArrayList<Integer> x = new ArrayList<Integer>();							//Cria um array com o int p[i] la dentro
				x.add(p.get(i));
				vdp[i] = A.domain(x);
			}
			for (int w = 0 ; w < dp; w++) {													//Ciclo para percorrer 'total' nos 'dp' diferentes arranjos e cria-los, para cada valor de w		
				int i = w;
				for (int pos = n - 1; pos >= 0; pos = pos - 1) {
					total.get(w).set(pos,(Integer) i%vdp[pos]); 
					i = i / vdp[pos];
				}
			}									
			return total;	
	}

	private double It(Amostra A, int f) {
		ArrayList<Integer> filho = new ArrayList<Integer>();
		filho.add(f);													//Arraylist com posicao do filho 
		int df = A.domain(filho);										//Dominio do filho
		ArrayList<Integer> pc = new ArrayList<Integer>();
		pc = parents(f);												//arraylist com posicao dos pais e classe
		ArrayList<Integer> c = new ArrayList<Integer>();
		c.add(dim - 1);													//Arraylist com posicao da classe
		ArrayList<Integer> fc = new ArrayList<Integer>();				//Arraylist com posicao do filho e da classe
		fc.add(f);
		if (f!=dim-1)
			fc.add(dim - 1);
		ArrayList<Integer> fpc = new ArrayList<Integer>();				//Arraylist com posicao do filho, pais e classe
		fpc.add(f);
		fpc.addAll(pc);
		
		double r = 0;
		for (int di=0; di < df; di++) {
			for (ArrayList<Integer> wici : combi(A, f)) {				//wici é a arraylist com os valores de pais e classe
				ArrayList<Integer> diwici = new ArrayList<Integer>();	//Arraylist com os valores do filho, pais e classe
				diwici.add(di);
				diwici.addAll(wici);
				ArrayList<Integer> ci = new ArrayList<Integer>();		//Arraylist com o valor da classe
				ci.add(wici.get(wici.size() - 1));						//Admitindo que a classe e o ultimo elemento de wici
				ArrayList<Integer> dici = new ArrayList<Integer>();		//Arraylist com o valor do filho e da classe
				dici.add(di);
				dici.add(ci.get(0));
				
				if (A.count(fpc,diwici) != 0)							//Prt(d1,wi,c) e' a menor probabilidade na formula do It, logo caso Prt(d1,wi,c)=0 assumimos essa parcela do It igual a 0.
					r = r + (((float) A.count(fpc,diwici))*log2(((float) A.count(fpc,diwici))*A.count(c, ci)/(A.count(fc, dici)*A.count(pc, wici))));
			}
		}
		return r/(A.length());											//Divide pelo número de elementos da amostra (m) 
	}
	
	protected double log2(float f) {						//Como esta salvaguardado nas funções que usam log2 que float f != 0, não temos de incluir essa condição aqui.
			return Math.log(f)/Math.log(2.0);
	}
		
	public double MDL(Amostra A) {
		ArrayList<Integer> c = new ArrayList<Integer>();
		c.add(dim - 1);														//Arraylist com posicao da classe
		int theta = A.domain(c)-1;
		double somIt = 0;
		for (int f = 0; f < dim - 1; f++) {
			ArrayList<Integer> filho = new ArrayList<Integer>();
			filho.add(f);													//Arraylist com posicao do filho 
			int df = A.domain(filho);										//Dominio do filho
			ArrayList<Integer> pc = new ArrayList<Integer>();
			pc = parents(f);												//Arraylist com posicao dos pais e classe
			theta = theta + (df-1) * A.domain(pc);
			somIt = somIt + It(A,f);
		}
		
		return log2((float) A.length())/2*theta - A.length()*somIt;
	}
	
	public double MDLdelta(Amostra A, int p, int f, int op) throws Exception {
		if (op==0) {
			double x1=MDL(A);
			remove_edge(p,f);
			return (MDL(A)-x1); }
		if (op==1) {
			double x2=MDL(A);
			invert_edge(p,f);
			return (MDL(A)-x2); }
		if (op==2) {
			double x3=MDL(A);
			add_edge(p,f);
			return (MDL(A)-x3); }
		else
			return 0;
	}

	public static void main(String[] args) {
		try {			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}