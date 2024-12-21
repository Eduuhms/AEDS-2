import java.util.NoSuchElementException;
import java.util.Scanner;


class Celula<T> {
    private final T item;
	private Celula<T> proximo;

	public Celula() {
		this.item = null;
		setProximo(null);
	}

	public Celula(T item) {
		this.item = item;
		setProximo(null);
	}

	public Celula(T item, Celula<T> proximo) {
        this.item = item;
        this.proximo = proximo;
    }
	
	public T getItem() {
		return item;
	}

	public Celula<T> getProximo() {
		return proximo;
	}

	public void setProximo(Celula<T> proximo) {
		this.proximo = proximo;
	}
}


class Pilha<E> {

    private Celula<E> topo;
	private Celula<E> fundo;

	public Pilha() {

		Celula<E> sentinela = new Celula<E>();
		fundo = sentinela;
		topo = sentinela;

	}

	public boolean vazia() {
		return fundo == topo;
	}

	public void empilhar(E item) {

		topo = new Celula<E>(item, topo);
	}

	public E desempilhar() {

		E desempilhado = consultarTopo();
		topo = topo.getProximo();
		return desempilhado;

	}

	public E consultarTopo() {

		if (vazia()) {
			throw new NoSuchElementException("Nao há nenhum item na pilha!");
		}

		return topo.getItem();

	}
}


public class VerificarSequencia {

    public static boolean bemFormada(String expressao) {
        Pilha<Character> pilha = new Pilha<>();
        
        for (int i = 0; i < expressao.length(); i++) {
            char c = expressao.charAt(i);

            if (c == '(' || c == '[') {
                pilha.empilhar(c);
            }
            else if (c == ')' || c == ']') {
                if (pilha.vazia()) {
                    return false;
                }
                char topo = pilha.desempilhar();
                if ((c == ')' && topo != '(') || (c == ']' && topo != '[')) {
                    return false; 
                }
            }
        }

        return pilha.vazia();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String expressaoMat;


        while (true) {
            expressaoMat = scanner.nextLine();
            
            if (expressaoMat.equalsIgnoreCase("FIM")) {
                break;
            }

            if (bemFormada(expressaoMat)) {
                System.out.println("correto");
            } else {
                System.out.println("incorreto");
            }

        }
        scanner.close();
    }
}
