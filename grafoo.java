package entrega_1;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class grafoo extends grafoo_entrega1 implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public grafoo(int dim) {
		super(dim);
	}

	public int getDim() {
		return dim; }
	
	@Override
	public void invert_edge(int p, int f) throws Exception {
		if (edgeQ(p,f)) {
			adj.get(f).remove((Integer) p);					//Vai a lista da posição f (no filho) e adiciona a essa lista p(no pai) e vai à lista da posição p (no pai) e remove a essa lista f(no filho). 
			if (!connected(p,f)) {							//Garante que ao inverter a aresta, não estamos a criar um ciclo
				adj.get(p).add(f);	
				Collections.sort(adj.get(p));}
			else {
				adj.get(f).add(p); 
				Collections.sort(adj.get(f));}}
	}

	private ArrayList<ArrayList<Integer>> combi(Amostra A, ArrayList<Integer> p) { 	//Recebe uma amostra, um no e ArrayList com todos os pais devolve todos os dominios possiveis 	
		int dp = A.domain(p);															//Domï¿½nio dos pais -> 'pi'i
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
	
	private double It(Amostra A, int f, ArrayList<Integer> pc) {		//pc arraylist com posicao dos pais e classe
		ArrayList<Integer> filho = new ArrayList<Integer>();
		filho.add(f);													//Arraylist com posicao do filho 
		int df = A.domain(filho);										//Dominio do filho									
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
			for (ArrayList<Integer> wici : combi(A, pc)) {				//wici e a arraylist com os valores de pais e classe
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
		return r/(A.length());											//Divide pelo nï¿½mero de elementos da amostra (m) 
	}
	
	@Override
	public double MDLdelta(Amostra A, int p, int f, int op) throws Exception { 	//Para usar esta funcao temos de salvaguardar antes que é possível realizar a operação respetiva sem formar um grafo ciclico.
		ArrayList<Integer> filho = new ArrayList<Integer>();
		filho.add(f);
		ArrayList<Integer> pais = new ArrayList<Integer>();
		pais = parents(f);
		if (op==0) {
			ArrayList<Integer> paissemp = (ArrayList<Integer>) pais.clone();
			paissemp.remove((Integer) p);
			return log2((float) A.length())/2*(A.domain(filho)-1) * (-A.domain(pais) + A.domain(paissemp))
					- A.length() * (-It(A, f, pais) + It(A,f,paissemp)); }
		if (op==1) {
			ArrayList<Integer> paissemp = (ArrayList<Integer>) pais.clone();
			paissemp.remove((Integer) p);
			ArrayList<Integer> pai = new ArrayList<Integer>();
			pai.add((Integer) p);
			ArrayList<Integer> paisdopai = (ArrayList<Integer>) parents(p).clone();
			ArrayList<Integer> paisdopaicomf = (ArrayList<Integer>) parents(p).clone();
			paisdopaicomf.add((Integer) f);
			Collections.sort(paisdopaicomf);
			return log2((float) A.length())/2*
					((A.domain(filho)-1) * (-A.domain(pais) + A.domain(paissemp)) + (A.domain(pai)-1)*(- A.domain(paisdopai) + A.domain(paisdopaicomf))) 
					- A.length() * (-It(A, f, pais) + It(A,f,paissemp) -It(A, p, paisdopai) + It(A,p,paisdopaicomf));}
		if (op==2) {
			ArrayList<Integer> paiscomp = (ArrayList<Integer>) pais.clone();
			paiscomp.add((Integer) p);
			Collections.sort(paiscomp);
			return (log2((float) A.length())/2*(A.domain(filho)-1) * (-A.domain(pais) + A.domain(paiscomp)) 
					- A.length() * (-It(A, f, pais) + It(A,f,paiscomp))); } 
		else
			return 0;
	}
	
	public static void main(String[] args) {
		try {			
			Amostra Teste= new Amostra("bcancer.csv");
			ArrayList<Integer> d = new ArrayList<Integer>();
			d.add(1);
			d.add(2);
			ArrayList<Integer> c = new ArrayList<Integer>();
			c.add(4);
			c.add(6);
			int Dom = Teste.domain(d);
			int Ct = Teste.count(d,c);
			System.out.println(Teste);
			System.out.println(Dom);
			System.out.println(Ct);
			System.out.println("");
			
			grafoo gr = new grafoo(11);
			int edges [][] = {{0, 1}, {0, 2}, {1, 2}, {2, 6}, {6, 4}, {5, 0}, {3, 2},{10,0},{10,1},{10,2},{10,3},{10,4},{10,5},{10,6},{10,7},{10,8},{10,9}};
			for (int [] edge : edges) 
				gr.add_edge(edge[0], edge[1]); 
			double x=gr.MDL(Teste);
			System.out.println("");
			System.out.println(gr.MDLdelta(Teste,3,4,2));
			gr.add_edge(3, 4);
			double y=gr.MDL(Teste);
			System.out.println(y-x);	//Serve pata testar MDLdelta com op==2
			System.out.println(gr.MDLdelta(Teste,3,4,0));
			gr.remove_edge(3, 4);
			double z=gr.MDL(Teste);
			System.out.println(z-y); //Serve pata testar MDLdelta com op==0
			System.out.println(gr.MDLdelta(Teste,3,2,1));
			gr.invert_edge(3,2);
			double w=gr.MDL(Teste);
			System.out.println(w-x); //Serve pata testar MDLdelta com op==1
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
