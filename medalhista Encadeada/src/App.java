import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

class Medalhista {
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

    public void mesclar(ListaDuplamenteEncadeada<E> outraLista) {
        Celula<E> atual = this.primeiro.getProximo();
        Celula<E> outraAtual = outraLista.primeiro.getProximo();
        int index = 0;

        while (outraAtual != null) {
            Celula<E> novaCelula = new Celula<>(outraAtual.getItem());
            if (atual == null) {
                this.inserirFinal(novaCelula.getItem());
            } else {
                Celula<E> proximoAtual = atual.getProximo();
                atual.setProximo(novaCelula);
                novaCelula.setAnterior(atual);
                novaCelula.setProximo(proximoAtual);

                if (proximoAtual != null) {
                    proximoAtual.setAnterior(novaCelula);
                } else {
                    this.ultimo = novaCelula;
                }

                atual = proximoAtual;
            }
            outraAtual = outraAtual.getProximo();
            index++;
        }
    }

    public boolean contemSequencia(ListaDuplamenteEncadeada<E> outraLista) {
        if (outraLista.vazia())
            return true;
        if (this.vazia())
            return false;

        Celula<E> atual = this.primeiro.getProximo();
        Celula<E> outraAtual = outraLista.primeiro.getProximo();

        while (atual != null) {
            Celula<E> tempAtual = atual;
            Celula<E> tempOutraAtual = outraAtual;

            while (tempAtual != null && tempOutraAtual != null &&
                    tempAtual.getItem().equals(tempOutraAtual.getItem())) {
                tempAtual = tempAtual.getProximo();
                tempOutraAtual = tempOutraAtual.getProximo();
            }

            if (tempOutraAtual == null)
                return true;
            atual = atual.getProximo();
        }
        return false;
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
    private static final int MAX_MEDALHISTAS = 100000;
    private static Medalhista[] medalhistas = new Medalhista[MAX_MEDALHISTAS];
    private static int medalhistaCount = 0;
    private static ListaDuplamenteEncadeada<Medalhista> lista = new ListaDuplamenteEncadeada<>();

    public static void main(String[] args) {
        Scanner scannerzin = new Scanner(System.in);
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

                Medalhista medalhista = Medalhista.buscarMedalhista(nome, medalhistas, medalhistaCount);

                if (medalhista == null) {
                    medalhista = new Medalhista(nome, genero, nascimento, pais);
                    if (medalhistaCount < MAX_MEDALHISTAS) {
                        medalhistas[medalhistaCount++] = medalhista;
                    }
                }

                Medalha medalha = new Medalha(tipo, data, disciplina, evento);
                medalhista.incluirMedalha(medalha);
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo: " + e.getMessage());
        }

        while (scannerzin.hasNextLine()) {
            String comando = scannerzin.nextLine();
            if (comando.equals("FIM")) {
                break;
            }
            String[] partes = comando.split("; ", 2);
            String nome;
            Medalhista encontrado;

            switch (partes[0]) {
                case "INSERIR FINAL":
                    nome = partes[1];
                    encontrado = Medalhista.buscarMedalhista(nome, medalhistas, medalhistaCount);

                    if (encontrado != null) {
                        lista.inserirFinal(encontrado);
                    } else {
                        System.out.println("Medalhista " + nome + " não encontrado.");
                    }
                    break;

                case "CONTEM SEQUENCIA":
                    int numMedalhistas = Integer.parseInt(partes[1]);
                    ListaDuplamenteEncadeada<Medalhista> outraListaSeq = new ListaDuplamenteEncadeada<>();

                    for (int i = 0; i < numMedalhistas; i++) {
                        nome = scannerzin.nextLine();
                        encontrado = Medalhista.buscarMedalhista(nome, medalhistas, medalhistaCount);

                        if (encontrado != null) {
                            outraListaSeq.inserirFinal(encontrado);
                        } else {
                            System.out.println("Medalhista " + nome + " não encontrado.");
                        }
                    }

                    if (lista.contemSequencia(outraListaSeq)) {
                        System.out.println("LISTA DE MEDALHISTAS CONTEM SEQUENCIA");
                        System.out.println("");
                    } else {
                        System.out.println("LISTA DE MEDALHISTAS NAO CONTEM SEQUENCIA");
                        System.out.println("");
                    }
                    break;

                case "MESCLAR":
                    int numMedalhistasMesclar = Integer.parseInt(partes[1]);
                    ListaDuplamenteEncadeada<Medalhista> outraListaMesclar = new ListaDuplamenteEncadeada<>();

                    for (int i = 0; i < numMedalhistasMesclar; i++) {
                        nome = scannerzin.nextLine();
                        encontrado = Medalhista.buscarMedalhista(nome, medalhistas, medalhistaCount);

                        if (encontrado != null) {
                            outraListaMesclar.inserirFinal(encontrado);
                        } else {
                            System.out.println("Medalhista " + nome + " não encontrado.");
                        }
                    }

                    lista.mesclar(outraListaMesclar);
                    System.out.println("LISTA MESCLADA DE MEDALHISTAS\n" + lista + "\n");

                    outraListaMesclar = new ListaDuplamenteEncadeada<>();
                    lista = new ListaDuplamenteEncadeada<>();
                    break;

                default:
                    System.out.println("Comando não reconhecido.");
                    break;
            }
        }
        scannerzin.close();
    }
}
