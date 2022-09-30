package entrega_1;

import java.io.Serializable;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Aprendizagem implements Serializable{

	private static final long serialVersionUID = 1L;
	
	grafoo	GRAF;														//GRAF sera o grafo a retornar no final, ou seja o grafo apÃ³s aprendizagem com o melhor MDL
	BN 		RedeBayes;
	
	public Aprendizagem(int nr_graf, int max_p, double S, Amostra am, String filename) throws Exception {			//Construtor que vai armazenar nos atributos GRAF e MDL o melhor grafo que for aprendido 
		if (nr_graf > 0 && max_p > 0 && S >0) {
			double MDL = 0;
			int nos = am.element(0).length;									//Numero de nos
			boolean primeirograf = true;
			grafoo grafo = new grafoo(nos);									//novo grafo desconexo

			for (int e = 0; e < nos ; e++) 									//iclo para adicionar as arestas da classe para os outros nos
				grafo.add_edge(nos-1, e);										

			for (int i=0; i<nr_graf; i++) {									//percorrer este ciclo de aprendizagem para nr_graf iniciais										
				if (!primeirograf) 	{										//Condicao que permite criar em primeiro lugar o grafo totalmente desconexo
					grafo = randomDAG(max_p , nos);	}
				grafo = GHC(max_p , grafo , am);											//Funcao que ensina o grafo
				if (primeirograf) {
					this.GRAF = grafo;
					MDL = grafo.MDL(am); 
				}
				if (grafo.MDL(am) < MDL) {									//Se o grafo que acabamos de aprender tiver melhor mdl, guardamo-lo como GRAF e MDL
					MDL = grafo.MDL(am);
					this.GRAF = grafo;
				}
				primeirograf = false;
			}
			this.RedeBayes = new BN(GRAF , am , S);
			RedeBayes.saveBN(filename);										//Gera um ficheiro com o nome dado pelo utilizador na primeira interface com a rede de Bayes gravada 
		}
		else {
			throw new Exception("Erro: Número de grafos, número máximo de pais ou pseudo-contagens inválidos (< 0). ");} 
	}
	
	public BN RedeBayes() throws Exception {
		return RedeBayes;
	}
	
	private static grafoo randomDAG(int k, int n) throws Exception {								//k é o nr maximo de pais e n o tamanho do grafo a criar (contando com a classe)
		grafoo  g = new grafoo(n);
		
		List<Integer> perm;																		//Cria uma lista de permutações com todos os nós exceto a classe faz shuffle	
		perm = IntStream.rangeClosed(0, n-2).boxed().collect(Collectors.toList());			 	//Esta lista serve para tornar aleatório os nós que selecionamos para serem pais de outros
		java.util.Collections.shuffle(perm);
		
		for (int i = 0; i < n-2; i++) {											 	 			//O objetivo é criar arestas entre um nó e nós seguintes na lista de maneira a garantir que não há grafos cíclicos
			
			List<Integer> nodes;
			nodes = IntStream.rangeClosed(i+1, n-2).boxed().collect(Collectors.toList()); 	 	//Primeiro gera todos os inteiros entre i+1 e o total de nós, colocando-os depois numa lista											  
			java.util.Collections.shuffle(nodes);												//Este comando faz shuffle de todos os elementos de uma lista
			int num = getRandomInRange(0, nodes.size());										//Quantidade de filhos que o no na posicao i da lista prem
			for (int j = 0; j < num; j++) 
				if (g.parents(perm.get(nodes.get(j))).size() < k - 1)							//AQUI DEVIA SER PARENTS_C???
					g.add_edge(perm.get(i), perm.get(nodes.get(j)));
		}
		for (int i = 0; i < n-1; i++)												 			//Este ciclo adiciona uma aresta entre o nó da classe (último) e todos os restantes nós
			g.add_edge(n-1, i);
		
		return g;
	}
	
	private static grafoo GHC(int max_p , grafoo g , Amostra am) throws Exception {
		int nos = g.getDim();
		double MDLdelta = -0.000000000000001;
		while (MDLdelta < 0) {
			MDLdelta = 0;
			int pai = 0;										//Guarda para cada grafo o pai onde a operacao em causa tem menor MDLdelta
			int filho = 0;										//Guarda para cada grafo o filho onde a operacao em causa tem menor MDLdelta
			int op = 3;											//Guarda para cada grafo a operacao entre o pai e o filho em causa que tem menor MDLdelta. Inicializamo-la com um valor qualquer que não seja 0, 1 ou 2.
			double inv = 0;
			for (int p = 0; p < nos-1 ; p++) {
				for (int f = 0; f < nos-1 && f!=p ; f++) {
					if (g.edgeQ(p, f)) {
						if (g.MDLdelta(am, p, f, 0) < MDLdelta) {
							pai = p;
							filho = f;
							op = 0; 
							MDLdelta = g.MDLdelta(am, p, f, 0); }
						if ( (inv = g.MDLdelta(am, p, f, 1)) < MDLdelta && g.parents(p).size() < max_p) {
							g.remove_edge(p, f); 							//remove a aresta para se poder confirmar a possibilidade de a inverter. Volta a ser adicionada a seguir ao if.
							if (!g.connected(p,f)) {
								pai = p;
								filho = f;
								op = 1; 
								MDLdelta = inv; }
							g.add_edge(p, f);}
					} else {
						if (!g.connected(f,p) && g.MDLdelta(am, p, f, 2) < MDLdelta && g.parents(f).size() < max_p) {
							pai = p;
							filho = f;
							op = 2; 
							MDLdelta = g.MDLdelta(am, p, f, 2); } }
				}
			}
			if (op == 0)
				g.remove_edge(pai, filho);
			if (op == 1)
				g.invert_edge(pai, filho);
			if (op == 2)
				g.add_edge(pai, filho);
		}
		return g;
	}

	private static int getRandomInRange(int min, int max) throws Exception {
		if (min>max)
			throw new Exception("Impossivel");
		else {
			Random r = new Random();
			return r.nextInt((max-min) + 1) + min;
		}
	}
	
	public static void main(String[] args) throws Exception {
		Amostra cancro= new Amostra("bcancer.csv");
		Amostra diabetes= new Amostra("diabetes.csv");
		Amostra hepatitis= new Amostra("hepatitis.csv");
		Amostra thyroid= new Amostra("thyroid.csv");
//		Aprendizagem Ap1 = new Aprendizagem(4 , 4 , 0.5 , cancro , "Teste_Ap_BC");
//		Aprendizagem Ap2 = new Aprendizagem(4 , 4 , 0.5 , cancro , "Teste_Ap_Diabetes");
//		Aprendizagem Ap3 = new Aprendizagem(4 , 4 , 0.5 , cancro , "Teste_Ap_Hepatite");
//		Aprendizagem Ap4 = new Aprendizagem(4 , 4 , 0.5 , cancro , "Teste_Ap_Thyroide");
	}
}
