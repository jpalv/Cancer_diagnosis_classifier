package entrega_1;

import java.io.Serializable;

public class Amostra extends Amostra_entrega1 implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public Amostra() {
		super();
	}

	public Amostra(String filename) {
		super(filename);
	}

	int domain(int i) throws Exception{			//Funcao que faz o mesmo que a funcao domain da classe Amostra_entrega1, mas no caso em que queremos obter o domínio de apenas um nó.
		if (i<dom.length)
		return dom[i];
		else
			throw new Exception("Error: there are only " + dom.length + "variables.");
	}
	
	int count(int var, int val) { 			//Funcao que faz o mesmo que a funcao count da classe Amostra_entrega1, mas no caso em que queremos obter o count para um valor de apenas um nó.
		int x=0;
		for (int j=0 ; j < amostra.size() ; j++) {
			if (amostra.get(j)[var]==val)
				x++; }
		return x;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}

}
