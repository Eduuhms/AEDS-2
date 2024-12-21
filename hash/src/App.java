import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class App {
    public static void main(String[] args) {

        DateTimeFormatter formatoCsv = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter formatoCorretoInput = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        TabelaHash<LocalDate, ABB<Evento>> tabela = new TabelaHash<>(100);

        try (BufferedReader br = new BufferedReader(new FileReader("/tmp/medallists.csv"))) {
            String linha;
            br.readLine(); 
            while ((linha = br.readLine()) != null) {
                String[] campos = linha.split(",");
                LocalDate dataEvento = LocalDate.parse(campos[2], formatoCsv);
                String disciplina = campos[6];
                String evento = campos[7];

                Evento novoEvento = new Evento(evento, disciplina, dataEvento);

                ABB<Evento> arvore = null;
                try {
                    arvore = tabela.pesquisar(dataEvento);
                } catch (NoSuchElementException e) {
                    arvore = new ABB<>();
                    tabela.inserir(dataEvento, arvore);
                }

                try {
                    arvore.adicionar(novoEvento);
                } catch (Exception e) {
                }
                
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo: " + e.getMessage());
        }

        try (Scanner scannerzin = new Scanner(System.in)) {
            while (scannerzin.hasNextLine()) {
                String entrada = scannerzin.nextLine();
                if (entrada.equals("FIM")) 
                break;

                LocalDate dataConsulta = LocalDate.parse(entrada, formatoCorretoInput);

                System.out.println("Eventos do dia " + entrada);
                try {
                    ABB<Evento> eventos = tabela.pesquisar(dataConsulta);
                    eventos.caminhamentoEmOrdem();
                    System.out.println();
                } catch (NoSuchElementException e) {
                    System.out.println("Sem eventos!!!");
                }
                catch (Exception e) {
                    System.out.println("Data inválida. Tente novamente no formato dd/mm/yyyy.");
                }
            }
        }
    }
}

class Evento implements Comparable<Evento> {
    private String event;
    private String discipline;
    private LocalDate date;

    public Evento(String event, String discipline, LocalDate date) {
        this.event = event;
        this.discipline = discipline;
        this.date = date;
    }

    @Override
    public String toString() {
        return discipline + " - " + event;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Evento other = (Evento) obj;
        return event.equalsIgnoreCase(other.event) && discipline.equalsIgnoreCase(other.discipline);
    }

    @Override
    public int compareTo(Evento outro) {
        int comparacaoNome = this.discipline.compareTo(outro.discipline);
        if (comparacaoNome == 0) {
            return this.event.compareTo(outro.event);
        }
        return comparacaoNome;
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

        int comparacao;

        if (raizArvore == null)
            raizArvore = new No<>(item);
        else {
            comparacao = item.compareTo(raizArvore.getItem());

            if (comparacao < 0)
                raizArvore.setEsquerda(adicionar(raizArvore.getEsquerda(), item));
            else if (comparacao > 0)
                raizArvore.setDireita(adicionar(raizArvore.getDireita(), item));
            else

                throw new RuntimeException("O item já foi inserido anteriormente na árvore.");
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

class Celula<T> {

    private T item;
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

    public void setItem(T item) {
        this.item = item;
    }

    public Celula<T> getProximo() {
        return proximo;
    }

    public void setProximo(Celula<T> proximo) {
        this.proximo = proximo;
    }
}

class Lista<E> {

    private Celula<E> primeiro;
    private Celula<E> ultimo;
    private int tamanho;

    public Lista() {

        Celula<E> sentinela = new Celula<>();

        this.primeiro = this.ultimo = sentinela;
        this.tamanho = 0;
    }

    public boolean vazia() {

        return (this.primeiro == this.ultimo);
    }

    public void inserir(E novo, int posicao) {

        Celula<E> anterior, novaCelula, proximaCelula;

        if ((posicao < 0) || (posicao > this.tamanho))
            throw new IndexOutOfBoundsException("Não foi possível inserir o item na lista: "
                    + "a posição informada é inválida!");

        anterior = this.primeiro;
        for (int i = 0; i < posicao; i++)
            anterior = anterior.getProximo();

        novaCelula = new Celula<>(novo);

        proximaCelula = anterior.getProximo();

        anterior.setProximo(novaCelula);
        novaCelula.setProximo(proximaCelula);

        if (posicao == this.tamanho)
            this.ultimo = novaCelula;

        this.tamanho++;
    }

    public void inserirFinal(E novo) {

        Celula<E> novaCelula = new Celula<>(novo);

        this.ultimo.setProximo(novaCelula);
        this.ultimo = novaCelula;

        this.tamanho++;
    }

    private E removerProxima(Celula<E> anterior) {

        Celula<E> celulaRemovida, proximaCelula;

        celulaRemovida = anterior.getProximo();

        proximaCelula = celulaRemovida.getProximo();

        anterior.setProximo(proximaCelula);
        celulaRemovida.setProximo(null);

        if (celulaRemovida == this.ultimo)
            this.ultimo = anterior;

        this.tamanho--;

        return (celulaRemovida.getItem());
    }

    public E remover(int posicao) {

        Celula<E> anterior;

        if (vazia())
            throw new IllegalStateException("Não foi possível remover o item da lista: "
                    + "a lista está vazia!");

        if ((posicao < 0) || (posicao >= this.tamanho))
            throw new IndexOutOfBoundsException("Não foi possível remover o item da lista: "
                    + "a posição informada é inválida!");

        anterior = this.primeiro;
        for (int i = 0; i < posicao; i++)
            anterior = anterior.getProximo();

        return (removerProxima(anterior));
    }

    public E remover(E elemento) {

        Celula<E> anterior;

        if (vazia())
            throw new IllegalStateException("Não foi possível remover o item da lista: "
                    + "a lista está vazia!");

        anterior = this.primeiro;
        while ((anterior.getProximo() != null) && !(anterior.getProximo().getItem().equals(elemento)))
            anterior = anterior.getProximo();

        if (anterior.getProximo() == null)
            throw new NoSuchElementException("Item não encontrado!");
        else {
            return (removerProxima(anterior));
        }
    }

    public E pesquisar(E procurado) {

        Celula<E> aux;

        aux = this.primeiro.getProximo();

        while (aux != null) {
            if (aux.getItem().equals(procurado))
                return aux.getItem();
            aux = aux.getProximo();
        }

        throw new NoSuchElementException("Item não encontrado!");
    }

    public void imprimir() {

        Celula<E> aux;

        aux = this.primeiro.getProximo();

        while (aux != null) {
            System.out.println(aux.getItem());
            aux = aux.getProximo();
        }
    }
}

class TabelaHash<K, V> {

    private Lista<Entrada<K, V>>[] tabelaHash;

    private int capacidade;

    @SuppressWarnings("unchecked")
    public TabelaHash(int capacidade) {

        this.capacidade = capacidade;
        this.tabelaHash = (Lista<Entrada<K, V>>[]) new Lista[this.capacidade];

        for (int i = 0; i < this.capacidade; i++)
            this.tabelaHash[i] = new Lista<>();
    }

    private int funcaoHash(K chave) {
        return Math.abs(chave.hashCode() % this.capacidade);
    }

    public int inserir(K chave, V item) {

        int posicao = funcaoHash(chave);

        Entrada<K, V> entrada = new Entrada<>(chave, item);

        try {
            this.tabelaHash[posicao].pesquisar(entrada);
            throw new IllegalArgumentException("O item já havia sido inserido anteriormente na tabela hash!");
        } catch (NoSuchElementException excecao) {
            this.tabelaHash[posicao].inserirFinal(entrada);
            return posicao;
        }
    }

    public V pesquisar(K chave) {

        int posicao = funcaoHash(chave);

        Entrada<K, V> procurado = new Entrada<>(chave, null);

        procurado = this.tabelaHash[posicao].pesquisar(procurado);
        return procurado.getValor();
    }

    public V remover(K chave) {

        int posicao = funcaoHash(chave);

        Entrada<K, V> procurado = new Entrada<>(chave, null);
        procurado = this.tabelaHash[posicao].remover(procurado);
        return procurado.getValor();
    }

    public void imprimir() {

        for (int i = 0; i < this.capacidade; i++) {
            System.out.println("Posição[" + i + "]: ");
            if (this.tabelaHash[i].vazia())
                System.out.println("vazia");
            else
                this.tabelaHash[i].imprimir();
        }
    }
}

class Entrada<K, V> {

    private final K chave;
    private V valor;

    public Entrada(K chave, V item) {
        this.chave = chave;
        this.valor = item;
    }

    public K getChave() {
        return chave;
    }

    public V getValor() {
        return valor;
    }

    public void setValor(V valor) {
        this.valor = valor;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object outroObjeto) {

        Entrada<K, V> outraEntrada;

        if (this == outroObjeto)
            return true;
        else if (outroObjeto == null || !(outroObjeto.getClass() == this.getClass()))
            return false;
        else {
            outraEntrada = (Entrada<K, V>) outroObjeto;
            return (outraEntrada.getChave().equals(this.chave));
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.chave);
    }

    @Override
    public String toString() {
        return (this.chave + "\n" + this.valor);
    }
}