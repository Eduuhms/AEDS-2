import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
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
        List<Medalha> medalhasFiltradas = new ArrayList<>();
        for (int i = 0; i < medalCount; i++) {
            if (medals[i].getTipo() == tipo) {
                medalhasFiltradas.add(medals[i]);
            }
        }
        return medalhasFiltradas.toArray(new Medalha[0]);
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

    public String toString() {
        return String.format("%s, %s. Nascimento: %s. Pais: %s", name, gender.toUpperCase(),
        DateTimeFormatter.ofPattern("dd/MM/yyyy").format(birthDate), country);
    }

    public String getNome() {
        return name;
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

class Fila<E> {
    Celula<E> frente;
    private Celula<E> tras;

    Fila() {
        Celula<E> sentinela = new Celula<>();
        frente = tras = sentinela;
    }

    public boolean vazia() {
        return (frente == tras);
    }

    public void enfileirar(E item) {
        Celula<E> novaCelula = new Celula<>(item);
        tras.setProximo(novaCelula);
        tras = tras.getProximo();
    }

    public E desenfileirar() {
        E item = null;
        Celula<E> primeiro;
        item = consultarPrimeiro();
        primeiro = frente.getProximo();
        frente.setProximo(primeiro.getProximo());
        primeiro.setProximo(null);

        if (primeiro == tras) {
            tras = frente;
        }
        return item;
    }

    public E consultarPrimeiro() {
        if (vazia()) {
            throw new NoSuchElementException("Nao há nenhum item na fila!");
        }
        return frente.getProximo().getItem();
    }

    public void imprimir() {
        Celula<E> aux;
        if (vazia()) {
        } else {
            aux = this.frente.getProximo();
            while (aux != null) {
                System.out.println(aux.getItem());
                aux = aux.getProximo();
            }
        }
    }

    public boolean verificarExistencia(E item) {
        Celula<E> aux = frente.getProximo();
        while (aux != null) {
            if (aux.getItem().equals(item)) {
                return true;
            }
            aux = aux.getProximo();
        }
        return false;
    }

    public Fila<E> dividir() {
        Fila<E> novaFila = new Fila<>();
        Fila<E> filaAtual= new Fila<>();
        
        Celula<E> aux = frente.getProximo();
        int posicao = 0;

        while (aux != null) {
            if (posicao % 2 == 0) {
                novaFila.enfileirar(aux.getItem());
            }
            else{filaAtual.enfileirar(aux.getItem());}
            aux = aux.getProximo();
            posicao++;
        }

        this.frente = filaAtual.frente;
        this.tras = filaAtual.tras;

        return novaFila;
    }

    public void resetarFila() {
        this.frente = new Celula<>();
        this.tras = this.frente;
    }
   
}

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

public class App {
    public static void main(String[] args) {
        Fila<Medalhista> fila = new Fila<>();
        List<Medalhista> medalhistasLidos = new ArrayList<>();
        Scanner scannerzin = new Scanner(System.in);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
        try (BufferedReader br = new BufferedReader(new FileReader("/tmp/medallists.csv"))) {
            String linha = br.readLine(); 
            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(",");
                String nome = dados[0];
                String genero = dados[3];
                LocalDate nascimento = LocalDate.parse(dados[4], formatter);
                String pais = dados[5];
                Medalhista medalhista = new Medalhista(nome, genero, nascimento, pais);
                medalhistasLidos.add(medalhista);
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo: " + e.getMessage());
        }

        while (scannerzin.hasNextLine()) {
            String comando = scannerzin.nextLine();
            String[] partes = comando.split(" ", 2);

            switch (partes[0]) {
                case "ENFILEIRAR":
                    String nome = partes[1];
                    Medalhista encontrado = buscarMedalhista(nome, medalhistasLidos); 
                    if (encontrado != null) {
                        fila.enfileirar(encontrado);
                    } else {
                        System.out.println("Medalhista " + nome + " não encontrado.");
                    }
                    break;

                case "DESENFILEIRAR":
                    try {
                        Medalhista desenfileirado = fila.desenfileirar();
                        System.out.println("(DESENFILEIRADO) " + desenfileirado.getNome());
                    } catch (NoSuchElementException e) {
                        System.out.println("Fila vazia. Não há medalhistas para desenfileirar.");
                    }
                    break;

                case "EXISTE":
                    nome = partes[1];
                    Medalhista buscar = buscarMedalhista(nome, medalhistasLidos); 
                    if (fila.verificarExistencia(buscar)) {
                        System.out.println(nome + " EXISTE NA FILA? SIM");
                    } else {
                        System.out.println(nome + " EXISTE NA FILA? NAO");
                    }
                    break;

                case "DIVIDIR":
                    Fila<Medalhista> novaFila = fila.dividir();

                    System.out.println("");
                    System.out.println("FILA ORIGINAL");
                    fila.imprimir();
                    System.out.println("");
                    System.out.println("FILA NOVA");
                    novaFila.imprimir();

                    fila.resetarFila();
                    
                    fila.imprimir();
                    System.out.println("");
                    break;

                default:         
            }
        }
        scannerzin.close();
    }

    private static Medalhista buscarMedalhista(String nome, List<Medalhista> medalhistas) {
        for (Medalhista medalhista : medalhistas) {
            if (medalhista.getNome().equalsIgnoreCase(nome)) {
                return medalhista;
            }
        }
        return null; 
    }
}

