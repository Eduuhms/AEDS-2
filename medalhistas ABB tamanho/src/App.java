import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.NoSuchElementException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

class Medalhista implements Comparable<Medalhista> {
    private static final int MAX_MEDALHAS = 8;
    private String name;
    private String gender;
    private LocalDate birthDate;
    private String country;
    private Medalha[] medals;
    private int medalCount;

    public Medalhista(String nome, String genero, LocalDate nascimento, String pais) {
        this.name = nome;
        this.gender = genero;
        this.birthDate = nascimento;
        this.country = pais;
        this.medals = new Medalha[MAX_MEDALHAS];
        this.medalCount = 0;
    }

    public int incluirMedalha(Medalha medalha) {
        if (medalCount < MAX_MEDALHAS) {
            medals[medalCount] = medalha;
            medalCount++;
        } else {
            System.out.println("Limite de medalhas alcançado.");
        }
        return medalCount;
    }

    public static Medalhista buscarMedalhista(String nome, Medalhista[] medalhistas, int count) {
        for (int i = 0; i < count; i++) {
            if (medalhistas[i].getNome().equalsIgnoreCase(nome)) {
                return medalhistas[i];
            }
        }
        return null;
    }

    public int totalMedalhas() {
        return medalCount;
    }

    public Medalha[] ouros() {
        return filtrarMedalhasPorTipo(TipoMedalha.OURO);
    }

    public Medalha[] pratas() {
        return filtrarMedalhasPorTipo(TipoMedalha.PRATA);
    }

    public Medalha[] bronzes() {
        return filtrarMedalhasPorTipo(TipoMedalha.BRONZE);
    }

    private Medalha[] filtrarMedalhasPorTipo(TipoMedalha tipo) {
        int count = 0;
        for (int i = 0; i < medalCount; i++) {
            if (medals[i].getTipo() == tipo) {
                count++;
            }
        }

        Medalha[] medalhasFiltradas = new Medalha[count];
        int index = 0;

        for (int i = 0; i < medalCount; i++) {
            if (medals[i].getTipo() == tipo) {
                medalhasFiltradas[index++] = medals[i];
            }
        }
    
        return medalhasFiltradas;
    }

    public String getPais() {
        return country;
    }

    public String getGenero() {
        return gender;
    }

    public LocalDate getNascimento() {
        return LocalDate.from(birthDate);
    }

    @Override
    public String toString() {
        return String.format("%s, %s. Nascimento: %s. Pais: %s",
                name, gender.toUpperCase(),
                DateTimeFormatter.ofPattern("dd/MM/yyyy").format(birthDate),
                country);
    }

    public String getNome() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Medalhista other = (Medalhista) obj;
        return name.equalsIgnoreCase(other.name);
    }

    @Override
    public int compareTo(Medalhista outro) {
        return this.name.compareTo(outro.name);
    }
}

enum TipoMedalha {
    OURO, PRATA, BRONZE
}

class Medalha {
    private TipoMedalha metalType;
    private LocalDate medalDate;
    private String discipline;
    private String event;

    public Medalha(TipoMedalha tipo, LocalDate data, String disciplina, String evento) {
        metalType = tipo;
        medalDate = data;
        discipline = disciplina;
        event = evento;
    }

    public TipoMedalha getTipo() {
        return metalType;
    }

    public String toString() {
        String dataFormatada = DateTimeFormatter.ofPattern("dd/MM/yyyy").format(medalDate);
        return String.format("%s - %s - %s - %s", metalType.toString(), discipline, event, dataFormatada);
    }
}

class ABB<E extends Comparable<E>> {

	private No<E> raiz; // referência à raiz da árvore.

	/**
     * Construtor da classe.
     * Esse construtor cria uma nova árvore binária de busca vazia. Para isso, esse método atribui null à raiz da árvore.
     */
    public ABB() {
        raiz = null;
    }

    /**
     * Método booleano que indica se a árvore está vazia ou não.
     * @return
     * verdadeiro: se a raiz da árvore for null, o que significa que a árvore está vazia.
     * falso: se a raiz da árvore não for null, o que significa que a árvore não está vazia.
     */
    public Boolean vazia() {
        return (this.raiz == null);
    }

    private E pesquisar(No<E> raizArvore, E procurado) {
    	
    	int comparacao;
    	
    	if (raizArvore == null)
    		/// Se a raiz da árvore ou sub-árvore for null, a árvore está vazia e então o item não foi encontrado.
    		throw new NoSuchElementException("O item não foi localizado na árvore!");
    	
    	comparacao = procurado.compareTo(raizArvore.getItem());
    	
    	if (comparacao == 0)
    		/// O item procurado foi encontrado.
    		return raizArvore.getItem();
    	else if (comparacao < 0)
    		/// Se o item procurado for menor do que o item armazenado na raiz da árvore:
            /// pesquise esse item na sub-árvore esquerda.    
    		return pesquisar(raizArvore.getEsquerda(), procurado);
    	else
    		/// Se o item procurado for maior do que o item armazenado na raiz da árvore:
            /// pesquise esse item na sub-árvore direita.
    		return pesquisar(raizArvore.getDireita(), procurado);
    }
    
    public E pesquisar(E procurado) {
    	return pesquisar(this.raiz, procurado);
    }
    
    /**
     * Método recursivo responsável por adicionar um item à árvore.
     * @param raizArvore: raiz da árvore ou sub-árvore em que o item será adicionado.
     * @param item: item que deverá ser adicionado à árvore.
     * @return a raiz atualizada da árvore ou sub-árvore em que o item foi adicionado.
     */
    protected No<E> adicionar(No<E> raizArvore, E item) {
    	
    	int comparacao;
    	
        /// Se a raiz da árvore ou sub-árvore for null, a árvore está vazia e então um novo item é inserido.
        if (raizArvore == null)
            raizArvore = new No<>(item);
        else {
        	comparacao = item.compareTo(raizArvore.getItem());
        
        	if (comparacao < 0)
        		/// Se o item que deverá ser inserido na árvore for menor do que o item armazenado na raiz da árvore:
        		/// adicione esse novo item à sub-árvore esquerda; e atualize a referência para a sub-árvore esquerda modificada. 
        		raizArvore.setEsquerda(adicionar(raizArvore.getEsquerda(), item));
        	else if (comparacao > 0)
        		/// Se o item que deverá ser inserido na árvore for maior do que o item armazenado na raiz da árvore:
        		/// adicione esse novo item à sub-árvore direita; e atualize a referência para a sub-árvore direita modificada.
        		raizArvore.setDireita(adicionar(raizArvore.getDireita(), item));
        	else
        		/// O item armazenado na raiz da árvore é igual ao novo item que deveria ser inserido na árvore.
        		throw new RuntimeException("O item já foi inserido anteriormente na árvore.");
        }
        
        /// Retorna a raiz atualizada da árvore ou sub-árvore em que o item foi adicionado.
        return raizArvore;
    }

    /**
     * Método que encapsula a adição recursiva de itens à árvore.
     * @param item: item que deverá ser inserido na árvore.
     */
    public void adicionar(E item) {
        /// Chama o método recursivo "adicionar", que será responsável por adicionar, o item passado como parâmetro, à árvore.
        /// O método "adicionar" recursivo receberá, como primeiro parâmetro, a raiz atual da árvore; e, como segundo parâmetro, 
    	/// o item que deverá ser adicionado à árvore.
        /// Por fim, a raiz atual da árvore é atualizada, com a raiz retornada pelo método "adicionar" recursivo.
        this.raiz = adicionar(this.raiz, item);
    }
    
    public void caminhamentoEmOrdem() {
    	
    	if (vazia())
    		throw new IllegalStateException("A árvore está vazia!");
    	
    	caminhamentoEmOrdem(this.raiz);
    }
    
    private void caminhamentoEmOrdem(No<E> raizArvore) {
    	if (raizArvore != null) {
    		caminhamentoEmOrdem(raizArvore.getEsquerda());
    		System.out.println(raizArvore.getItem());
    		caminhamentoEmOrdem(raizArvore.getDireita());
    	}
    }
    
    /**
     * Método recursivo responsável por localizar na árvore ou sub-árvore o antecessor do nó que deverá ser retirado. 
     * O antecessor do nó que deverá ser retirado da árvore corresponde
     * ao nó que armazena o item que é o maior, 
     * dentre os itens menores do que o item que deverá ser retirado.
     * Depois de ser localizado na árvore ou sub-árvore, 
     * o antecessor do nó que deverá ser retirado da árvore o substitui.
     * Adicionalmente, a árvore ou sub-árvore é atualizada com a remoção do antecessor.
     * @param itemRetirar: referência ao nó que armazena o item que deverá ser retirado da árvore.
     * @param raizArvore: raiz da árvore ou sub-árvore em que o antecessor do nó que deverá ser retirado deverá ser localizado.
     * @return a raiz atualizada da árvore ou sub-árvore após a remoção do antecessor do nó que foi retirado da árvore.
     */
    protected No<E> removerNoAntecessor(No<E> itemRetirar, No<E> raizArvore) {
        /// Se o antecessor do nó que deverá ser retirado da árvore ainda não foi encontrado...
        if (raizArvore.getDireita() != null)
            /// Pesquise o antecessor na sub-árvore direita.
            raizArvore.setDireita(removerNoAntecessor(itemRetirar, raizArvore.getDireita()));
        else {
        	/// O antecessor do nó que deverá ser retirado da árvore foi encontrado e deverá substitui-lo.
            itemRetirar.setItem(raizArvore.getItem());
            /// A raiz da árvore ou sub-árvore é atualizada com os descendentes à esquerda do antecessor.
            /// Ou seja, retira-se o antecessor da árvore.
            raizArvore = raizArvore.getEsquerda();
        }
        return raizArvore;
    }

    /**
     * Método recursivo responsável por localizar um item na árvore e retirá-lo da árvore.
     * @param raizArvore: raiz da árvore ou sub-árvore da qual o item será retirado.
     * @param itemRemover: item que deverá ser localizado e removido da árvore.
     * @return a raiz atualizada da árvore ou sub-árvore da qual o item foi retirado.
     */
    protected No<E> remover(No<E> raizArvore, E itemRemover) {
    	
    	int comparacao;
    	
        /// Se a raiz da árvore ou sub-árvore for null, a árvore está vazia e o item, que deveria ser retirado dessa árvore, não foi encontrado.
        /// Nesse caso, deve-se lançar uma exceção.
        if (raizArvore == null) 
        	throw new NoSuchElementException("O item a ser removido não foi localizado na árvore!");
        
        comparacao = itemRemover.compareTo(raizArvore.getItem());
        
        if (comparacao == 0) {
            /// O item armazenado na raiz da árvore corresponde ao item que deve ser retirado dessa árvore.
            /// Ou seja, o item que deve ser retirado da árvore foi encontrado.
        	if (raizArvore.getDireita() == null)
        		/// O nó da árvore que será retirado não possui descendentes à direita.
                /// Nesse caso, os descendentes à esquerda do nó que está sendo retirado da árvore passarão a ser descendentes do nó-pai do nó que está sendo retirado.
                raizArvore = raizArvore.getEsquerda();
            else if (raizArvore.getEsquerda() == null)
                /// O nó da árvore que será retirado não possui descendentes à esquerda.
                /// Nesse caso, os descendentes à direita do nó que está sendo retirado da árvore passarão a ser descendentes do nó-pai do nó que está sendo retirado.
                raizArvore = raizArvore.getDireita();
            else
            	/// O nó que está sendo retirado da árvore possui descendentes à esquerda e à direita.
                /// Nesse caso, o antecessor do nó que está sendo retirado é localizado na sub-árvore esquerda desse nó. 
                /// O antecessor do nó que está sendo retirado da árvore corresponde
                /// ao nó que armazena o item que é o maior, 
                /// dentre os itens menores do que o item do nó que está sendo retirado.
                /// Depois de ser localizado na sub-árvore esquerda do nó que está sendo retirado, 
                /// o antecessor desse nó o substitui.
                /// A sub-árvore esquerda do nó que foi retirado é atualizada com a remoção do antecessor.
                raizArvore.setEsquerda(removerNoAntecessor(raizArvore, raizArvore.getEsquerda()));
        } else if (comparacao < 0)
        	/// Se o item que deverá ser localizado e retirado da árvore for menor do que o item armazenado na raiz da árvore:
        	/// pesquise e retire esse item da sub-árvore esquerda.
            raizArvore.setEsquerda(remover(raizArvore.getEsquerda(), itemRemover));
        else
        	/// Se o item que deverá ser localizado e retirado da árvore for maior do que o item armazenado na raiz da árvore:
        	/// pesquise e retire esse item da sub-árvore direita.
            raizArvore.setDireita(remover(raizArvore.getDireita(), itemRemover));
         
        /// Retorna a raiz atualizada da árvore ou sub-árvore da qual o item foi retirado.
        return raizArvore;
    }

    /**
     * Método que encapsula a remoção recursiva de um item da árvore.
     * @param itemRemover: item que deverá ser localizado e removido da árvore.
     */
    public void remover(E itemRemover) {
        /// Chama o método recursivo "remover", que será responsável por pesquisar o item passado como parâmetro na árvore e retirá-lo da árvore.
        /// O método "remover" recursivo receberá, como primeiro parâmetro, a raiz atual da árvore; 
    	/// e, como segundo parâmetro, o item que deverá ser localizado e retirado dessa árvore.
    	/// Por fim, a raiz atual da árvore é atualizada, com a raiz retornada pelo método "remover" recursivo.
        this.raiz = remover(this.raiz, itemRemover);
    }

    public int tamanho(E item) {
        No<E> noh = encontrarNo(this.raiz, item);
        if (noh == null) {
            return 0;
        }
        return contarNos(noh);
    }

    private No<E> encontrarNo(No<E> raizArvore, E item) {
        if (raizArvore == null) {
            return null;
        }
        int comparacao = item.compareTo(raizArvore.getItem());
        if (comparacao == 0) {
            return raizArvore;
        } else if (comparacao < 0) {
            return encontrarNo(raizArvore.getEsquerda(), item);
        } else {
            return encontrarNo(raizArvore.getDireita(), item);
        }
    }

    private int contarNos(No<E> no) {
        if (no == null) {
            return 0;}
        return 1 + contarNos(no.getEsquerda()) + contarNos(no.getDireita());
    }

}

class No<T extends Comparable<T>> {

	private T item;        // contém os dados do item armazenado no nodo da árvore.
	private No<T> direita;    // referência ao nodo armazenado, na árvore, à direita do nó em questão.
	private No<T> esquerda;   // referência ao nodo armazenado, na árvore, à esquerda do nó em questão.
	private int altura;
	
	public No() {
		this.setItem(null);
	    this.setDireita(null);
	    this.setEsquerda(null);
	    this.altura = 0;
	}
	
	public No(T item) {
		this.setItem(item);
	    this.setDireita(null);
	    this.setEsquerda(null);
	    this.altura = 0;
	}

	public No(T item, No<T> esquerda, No<T> direita) {
		this.setItem(item);
	    this.setDireita(direita);
	    this.setEsquerda(esquerda);
	    this.altura = 0;
	}

	public T getItem() {
		return this.item;
	}

	public void setItem(T item) {
		this.item = item;
	}

	public No<T> getDireita() {
		return this.direita;
	}

	public void setDireita(No<T> direita) {
		this.direita = direita;
	}

	public No<T> getEsquerda() {
		return this.esquerda;
	}

	public void setEsquerda(No<T> esquerda) {
		this.esquerda = esquerda;
	}
	
	private int getAltura(No<T> no) {
		
		if (no != null)
			return no.getAltura();
		else
			return -1;
	}
	
	public int getAltura() {
		return this.altura;
	}

	public void setAltura() {
		
		int alturaEsquerda, alturaDireita;
		
		alturaEsquerda = getAltura(this.esquerda);
		alturaDireita = getAltura(this.direita);
		
		if (alturaEsquerda > alturaDireita)
			this.altura = alturaEsquerda + 1;
		else
			this.altura = alturaDireita + 1;
	}
	
	public int getFatorBalanceamento() {
		
		int alturaEsquerda, alturaDireita;
		
		alturaEsquerda = getAltura(this.esquerda);
		alturaDireita = getAltura(this.direita);
		
		return (alturaEsquerda - alturaDireita);
	}
}
public class App {

    public static void main(String[] args) {
        ABB<Medalhista> arvore = new ABB<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Ler o arquivo e inserir na árvore
        try (BufferedReader br = new BufferedReader(new FileReader("/tmp/medallists.csv"))) {
            String linha = br.readLine(); // Ignorar cabeçalho
            while ((linha = br.readLine()) != null) {
                String[] partes = linha.split(",");
                String nome = partes[0].trim();
                TipoMedalha tipo = TipoMedalha.valueOf(partes[1].trim());
                LocalDate data = LocalDate.parse(partes[2].trim(), formatter);
                String genero = partes[3].trim();
                LocalDate nascimento = LocalDate.parse(partes[4].trim(), formatter);
                String pais = partes[5].trim();
                String disciplina = partes[6].trim();
                String evento = partes[7].trim();

                Medalhista medalhista = new Medalhista(nome, genero, nascimento, pais);
                Medalha medalha = new Medalha(tipo, data, disciplina, evento);
                medalhista.incluirMedalha(medalha);

                try {
                    arvore.adicionar(medalhista);
                } catch (RuntimeException ignored) {
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo: " + e.getMessage());
        }

        // Processar entrada padrão
        Scanner scannerzin = new Scanner(System.in);
        while (true) {
            String linha = scannerzin.nextLine();
            if (linha.equals("FIM")) {
                break;
            }
            Medalhista medalhista = new Medalhista(linha, "", null, "");
            try {
                Medalhista encontrado = arvore.pesquisar(medalhista);
                System.out.println(encontrado.toString());
                System.out.println("Tamanho: " + arvore.tamanho(encontrado));
                System.out.println();
            } catch (NoSuchElementException e) {
                System.out.println(linha + " não encontrado.");
            }
        }
    }
}
