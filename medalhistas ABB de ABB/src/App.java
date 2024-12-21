import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


class Medalhista implements Comparable<Medalhista> {
    private static final int MAX_MEDALHAS = 8;
    private String name;
    private String gender;
    private LocalDate birthDate;
    private String country;
    private ListaDuplamenteEncadeada<Medalha> medals;

    public Medalhista(String nome, String genero, LocalDate nascimento, String pais) {
        this.name = nome;
        this.gender = genero;
        this.birthDate = nascimento;
        this.country = pais;
        this.medals = new ListaDuplamenteEncadeada<>();
    }

    public void incluirMedalha(Medalha medalha) {
        this.medals.inserirFinal(medalha);
    }

    public static Medalhista buscarMedalhista(String nome, Medalhista[] medalhistas, int count) {
        for (int i = 0; i < count; i++) {
            if (medalhistas[i].getNome().equalsIgnoreCase(nome)) {
                return medalhistas[i];
            }
        }
        return null;
    }

    public int totalDeMedalhas() {
        int count = 0;
        Celula<Medalha> atual = medals.getPrimeiro().getProximo();
        while (atual != null) {
            count++;
            atual = atual.getProximo();
        }
        return count;
    }

    public ListaDuplamenteEncadeada<Medalha> ouros() {
        return filtrarMedalhasPorTipo(TipoMedalha.OURO);
    }

    public ListaDuplamenteEncadeada<Medalha> pratas() {
        return filtrarMedalhasPorTipo(TipoMedalha.PRATA);
    }

    public ListaDuplamenteEncadeada<Medalha> bronzes() {
        return filtrarMedalhasPorTipo(TipoMedalha.BRONZE);
    }

    public ListaDuplamenteEncadeada<Medalha> filtrarMedalhasPorTipo(TipoMedalha tipo) {
        ListaDuplamenteEncadeada<Medalha> medalhasFiltradas = new ListaDuplamenteEncadeada<>();
        Celula<Medalha> atual = medals.getPrimeiro().getProximo();

        while (atual != null) {
            Medalha medalha = atual.getItem();
            if (medalha.getTipo() == tipo) {
                medalhasFiltradas.inserirFinal(medalha);
            }
            atual = atual.getProximo();
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
        if (this.name == null || outro.name == null) {
            throw new IllegalArgumentException("Nome não pode ser nulo para comparação.");
        }
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
    
        @Override
        public String toString() {
            String dataFormatada = DateTimeFormatter.ofPattern("dd/MM/yyyy").format(medalDate);
            return String.format("%s - %s - %s - %s", metalType.toString(), discipline, event, dataFormatada);
        }
    }
    
    class Evento implements Comparable<Evento> {
        private String event;
        private String discipline;
        private ABB<Medalhista> medalhistas;
    
        public Evento(String event, String discipline) {
            this.event = event;
            this.discipline = discipline;
            this.medalhistas = new ABB<>();
        }
    
        public void incluirMedalhista(Medalhista medalhista) {
            if(medalhista != null)
                    medalhistas.adicionar(medalhista);
        }
    
        public Medalhista pesquisaMedalhista(String nome) {
            try {
                return this.medalhistas.pesquisar(new Medalhista(nome, null, null, null));
            } catch (NoSuchElementException e) {
                System.out.println("Medalhista não encontrado: " + nome);
                return null;
            }
        }
    
        public String relatorioMedalhistas() {
            StringBuilder sb = new StringBuilder();
            sb.append("Evento: ").append(event).append(" - ").append(discipline).append("\n");
            sb.append("Medalhistas:\n");
            medalhistas.caminhamentoEmOrdem();
            return sb.toString();
        }
    
        public int totalMedalhistas() {
            return medalhistas.tamanho(null); 
        }
    
        @Override
        public String toString() {
            return event + " - " + discipline;
        }
    
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Evento other = (Evento) obj;
            return event.equalsIgnoreCase(other.event) && discipline.equalsIgnoreCase(other.discipline);
        }
    
        @Override
        public int compareTo(Evento outro) {
        int comparacaoEvento = this.event.compareTo(outro.event);
        if (comparacaoEvento == 0) {
            return this.discipline.compareTo(outro.discipline);
        }
        return comparacaoEvento;
    }

        public ListaDuplamenteEncadeada<Medalhista> recortarMedalhistas(String deOnde, String ateOnde) {
            Medalhista inicio = new Medalhista(deOnde, "", null, "");
            Medalhista fim = new Medalhista(ateOnde, "", null, "");
            return this.medalhistas.recortar(inicio, fim);
        }
    }

    class ABB<E extends Comparable<E>> {
    
        private No<E> raiz;
    
        public ABB() {
            raiz = null;
        }
    
        public Boolean vazia() {
            return (this.raiz == null);
        }
    
        private E pesquisar(No<E> raizArvore, E procurado) {
            
            int comparacao;
            
            if (raizArvore == null)
                throw new NoSuchElementException("O item não foi localizado na árvore!");
            
            comparacao = procurado.compareTo(raizArvore.getItem());
            
            if (comparacao == 0)
                return raizArvore.getItem();
            else if (comparacao < 0)  
                return pesquisar(raizArvore.getEsquerda(), procurado);
            else
                return pesquisar(raizArvore.getDireita(), procurado);
        }
        
        public E pesquisar(E procurado) {
            return pesquisar(this.raiz, procurado);
        }
        
        protected No<E> adicionar(No<E> raizArvore, E item) {
            if (raizArvore == null) {
                raizArvore = new No<>(item);
            } else {
                int comparacao = item.compareTo(raizArvore.getItem());
        
                if (comparacao < 0) {
                    raizArvore.setEsquerda(adicionar(raizArvore.getEsquerda(), item));
                } else { 
                    raizArvore.setDireita(adicionar(raizArvore.getDireita(), item));
                }
            }
            return raizArvore;
        }
    
        public void adicionar(E item) {
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
    
        protected No<E> removerNoAntecessor(No<E> itemRetirar, No<E> raizArvore) {
            if (raizArvore.getDireita() != null)
                raizArvore.setDireita(removerNoAntecessor(itemRetirar, raizArvore.getDireita()));
            else {
                itemRetirar.setItem(raizArvore.getItem());
                raizArvore = raizArvore.getEsquerda();
            }
            return raizArvore;
        }
    
        protected No<E> remover(No<E> raizArvore, E itemRemover) {
            
            int comparacao;
            
            if (raizArvore == null) 
                throw new NoSuchElementException("O item a ser removido não foi localizado na árvore!");
            
            comparacao = itemRemover.compareTo(raizArvore.getItem());
            
            if (comparacao == 0) {
                if (raizArvore.getDireita() == null)
                    raizArvore = raizArvore.getEsquerda();
                else if (raizArvore.getEsquerda() == null)
                    raizArvore = raizArvore.getDireita();
                else
                    raizArvore.setEsquerda(removerNoAntecessor(raizArvore, raizArvore.getEsquerda()));
            } else if (comparacao < 0)
                raizArvore.setEsquerda(remover(raizArvore.getEsquerda(), itemRemover));
            else
                raizArvore.setDireita(remover(raizArvore.getDireita(), itemRemover));
    
            return raizArvore;
        }
    
    
        public void remover(E itemRemover) {
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
    
        public ListaDuplamenteEncadeada<E> recortar(E deOnde, E ateOnde) {
            if (deOnde == null || ateOnde == null || deOnde.compareTo(ateOnde) >= 0) {
                throw new IllegalArgumentException("Os limites do intervalo são inválidos!");
            }
            ListaDuplamenteEncadeada<E> resultado = new ListaDuplamenteEncadeada<>();
            recortar(this.raiz, deOnde, ateOnde, resultado);
            return resultado;
        }
        
        private void recortar(No<E> raizArvore, E deOnde, E ateOnde, ListaDuplamenteEncadeada<E> resultado) {
            if (raizArvore == null) {
                return;
            }
            if (raizArvore.getItem().compareTo(deOnde) > 0) {
                recortar(raizArvore.getEsquerda(), deOnde, ateOnde, resultado);
            }
            if (raizArvore.getItem().compareTo(deOnde) > 0 && raizArvore.getItem().compareTo(ateOnde) < 0) {
                resultado.inserirFinal(raizArvore.getItem());
            }
            if (raizArvore.getItem().compareTo(ateOnde) < 0) {
                recortar(raizArvore.getDireita(), deOnde, ateOnde, resultado);
            }
        }

}

class No<T extends Comparable<T>> {

	private T item;        
	private No<T> direita;    
	private No<T> esquerda;   
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

class ListaDuplamenteEncadeada<E> {

    private Celula<E> primeiro;
    private Celula<E> ultimo;
    private int tamanho;

    public ListaDuplamenteEncadeada() {

        Celula<E> sentinela = new Celula<>();

        this.primeiro = this.ultimo = sentinela;
        this.tamanho = 0;
    }

    public boolean vazia() {

        return (this.primeiro == this.ultimo);
    }

    public void inserirFinal(E novo) {

        Celula<E> novaCelula = new Celula<>(novo, this.ultimo, null);

        this.ultimo.setProximo(novaCelula);
        this.ultimo = novaCelula;

        this.tamanho++;

    }

    public E removerFinal() {

        Celula<E> removida, penultima;

        if (vazia())
            throw new IllegalStateException("Não foi possível remover o último item da lista: "
                    + "a lista está vazia!");

        removida = this.ultimo;

        penultima = this.ultimo.getAnterior();
        penultima.setProximo(null);

        removida.setAnterior(null);

        this.ultimo = penultima;

        this.tamanho--;

        return (removida.getItem());
    }

    public E removerInicio() {
        if (vazia())
            throw new IllegalStateException("Não foi possível remover o primeiro item da lista: a lista está vazia!");

        Celula<E> removida = this.primeiro.getProximo();
        this.primeiro.setProximo(removida.getProximo());

        if (removida.getProximo() != null) {
            removida.getProximo().setAnterior(this.primeiro);
        } else {
            this.ultimo = this.primeiro;
        }

        removida.setAnterior(null);
        removida.setProximo(null);
        this.tamanho--;

        return removida.getItem();
    }

    public Celula<E> getPrimeiro() {
        return this.primeiro;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Celula<E> atual = this.primeiro.getProximo();

        while (atual != null) {
            sb.append(atual.getItem());
            atual = atual.getProximo();
            if (atual != null)
                sb.append("\n");
        }

        return sb.toString();
    }
}

class Celula<T> {

    private final T item;
    private Celula<T> anterior;
    private Celula<T> proximo;

    public Celula() {
        this.item = null;
        setAnterior(null);
        setProximo(null);
    }

    public Celula(T item) {
        this.item = item;
        setAnterior(null);
        setProximo(null);
    }

    public Celula(T item, Celula<T> anterior, Celula<T> proximo) {
        this.item = item;
        this.anterior = anterior;
        this.proximo = proximo;
    }

    public T getItem() {
        return item;
    }

    public Celula<T> getAnterior() {
        return anterior;
    }

    public void setAnterior(Celula<T> anterior) {
        this.anterior = anterior;
    }

    public Celula<T> getProximo() {
        return proximo;
    }

    public void setProximo(Celula<T> proximo) {
        this.proximo = proximo;
    }
}

public class App {

    public static void main(String[] args) {
        ABB<Evento> eventos = new ABB<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try (BufferedReader br = new BufferedReader(new FileReader("/tmp/medallists.csv"))) {
            String linha = br.readLine(); 
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
                Evento eventoAtual = new Evento(evento, disciplina);
                Medalha medalha = new Medalha(tipo, data, disciplina, evento);
                Medalhista medalhista = new Medalhista(nome, genero, nascimento, pais);
                medalhista.incluirMedalha(medalha);

                try {
                    eventos.adicionar(eventoAtual);
                } catch (NoSuchElementException e) {
                    eventoAtual = eventos.pesquisar(eventoAtual);
                }
                eventoAtual.incluirMedalhista(medalhista);}}
         catch (IOException e) {
            System.err.println("Erro ao ler o arquivo: " + e.getMessage());
        }

        Scanner scanner = new Scanner(System.in);
        String comando;

        while (!(comando = scanner.nextLine()).equalsIgnoreCase("FIM")) {
            String[] partes = comando.split(" - ");
            String acao = partes[0].trim();

            switch (acao) {
                case "PESQUISAR": {
                    String esporte = partes[1].trim();
                    String evento = partes[2].trim();
                    String nomeMedalhista = partes[3].trim();

                    try {
                        Evento event = eventos.pesquisar(new Evento(esporte, evento));
                        Medalhista medalhista = event.pesquisaMedalhista(nomeMedalhista);
                        if (medalhista != null) {
                            System.out.println(medalhista.getNome() + " foi medalhista no evento " + evento + " do esporte "
                                    + esporte);
                        } else {
                            System.out.println("Medalhista não encontrado.");
                        }
                        System.out.println();
                    } catch (NoSuchElementException e) {
                        System.out.println("Nao ha medalhistas entre os indicados.");
                    }
                    break;
                }

                case "IMPRIMIR": {
                    String esporte = partes[1].trim();
                    String evento = partes[2].trim();

                    Evento eventoPesquisado = eventos.pesquisar(new Evento(evento, esporte));
                    if (eventoPesquisado != null) {
                        System.out.println(eventoPesquisado.relatorioMedalhistas());
                    } else {
                        System.out.println("Evento não encontrado.");
                    }
                    break;
                }
                case "RECORTAR": {
                    String esporte = partes[1].trim();
                    String evento = partes[2].trim();
                    String de = partes[3].trim();
                    String ate = partes[4].trim();

                    Evento eventoPesquisado = eventos.pesquisar(new Evento(evento, esporte));
                    if (eventoPesquisado != null) {
                        ListaDuplamenteEncadeada<Medalhista> recorte = eventoPesquisado.recortarMedalhistas(de, ate);
                        System.out.println(recorte);
                    } else {
                        System.out.println("Evento não encontrado.");
                    }
                    break;
                }
                default:
                    System.out.println("Comando desconhecido: " + acao);
            }
        }

    }
}
