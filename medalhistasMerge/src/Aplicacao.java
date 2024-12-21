import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Scanner;

/**
 * Interface IOrdenator
 * @param <T>
 */
interface IOrdenator<T> {
    void ordenar(T[] lista, Comparator<T> comparador);
}

class Mergesort<T> implements IOrdenator<T> {
    private long comparacoes = 0;
    private long movs = 0;

    public long getComparacoes() {
        return comparacoes;
    }

    public long getMovimentacoes() {
        return movs;
    }

    @Override
    public void ordenar(T[] lista, Comparator<T> comparador) {
        if (lista == null || lista.length < 2) {
            return;
        }
        mergesort(lista, 0, lista.length - 1, comparador);
    }

    private void mergesort(T[] lista, int esquerda, int direita, Comparator<T> comparador) {
        if (esquerda < direita) {
            int meio = (esquerda + direita) / 2;
            mergesort(lista, esquerda, meio, comparador);
            mergesort(lista, meio + 1, direita, comparador);
            merge(lista, esquerda, meio, direita, comparador);
        }
    }

    @SuppressWarnings("unchecked")
    private void merge(T[] lista, int esquerda, int meio, int direita, Comparator<T> comparador) {
        int n1 = meio - esquerda + 1;
        int n2 = direita - meio;

        T[] esquerdaArray = (T[]) new Object[n1];
        T[] direitaArray = (T[]) new Object[n2];

        System.arraycopy(lista, esquerda, esquerdaArray, 0, n1);
        System.arraycopy(lista, meio + 1, direitaArray, 0, n2);

        int i = 0, j = 0;
        int k = esquerda;

        while (i < n1 && j < n2) {
            comparacoes++;
            if (comparador.compare(esquerdaArray[i], direitaArray[j]) <= 0) {
                lista[k] = esquerdaArray[i];
                i++;
            } else {
                lista[k] = direitaArray[j];
                j++;
            }
            movs++;
            k++;
        }

        while (i < n1) {
            lista[k] = esquerdaArray[i];
            i++;
            k++;
            movs++;
        }

        while (j < n2) {
            lista[k] = direitaArray[j];
            j++;
            k++;
            movs++;
        }
    }
}



/**
 * Classe Medalha: representa uma medalha olímpica e seu evento.
 */
class Medalha {
    private TipoMedalha medalType;
    private LocalDate medalDate;
    private Evento evento;

    public Medalha(TipoMedalha tipo, LocalDate data, Evento evento) {
        this.medalType = tipo;
        this.medalDate = data;
        this.evento = evento;
    }

    public TipoMedalha getTipo() {
        return medalType;
    }

    @Override
    public String toString() {
        return "Tipo: " + medalType + ", Data: " + medalDate + ", Evento: " + evento;
    }
}


/**
 * Classe Medalhista: representa um medalhista olímpico e sua coleção de medalhas.
 */
class Medalhista {
    public static final int MAX_MEDALHAS = 8;
    private String name;
    private String gender;
    private LocalDate birthDate;
    private Pais pais;
    private Medalha[] medalhas;
    private int medalCount;

    public Medalhista(String nome, String genero, LocalDate nascimento, Pais pais) {
        this.name = nome;
        this.gender = genero;
        this.birthDate = nascimento;
        this.pais = pais;
        this.medalhas = new Medalha[1];
        this.medalCount = 0;
    }

    public void incluirMedalha(Medalha medalha) {
        if (medalCount < MAX_MEDALHAS) {
            if (medalCount == medalhas.length) {
                Medalha[] novoArray = new Medalha[medalhas.length * 2];
                System.arraycopy(medalhas, 0, novoArray, 0, medalhas.length);
                this.medalhas = novoArray;
            }
            medalhas[medalCount] = medalha;
            medalCount++;
        }
    }

    public int totalDeMedalhas() {
        return medalCount;
    }

    public int totalDeMedalhas(TipoMedalha tipo) {
        int count = 0;
        for (int i = 0; i < medalCount; i++) {
            if (medalhas[i].getTipo() == tipo) {
                count++;
            }
        }
        return count;
    }

    public String relatorioDeMedalhas() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < medalCount; i++) {
            sb.append(medalhas[i].toString()).append("\n");
        }
        return sb.toString();
    }

    public int compareTo(Medalhista outro) {
        int comparacaoMedalhas = Integer.compare(outro.totalDeMedalhas(), this.totalDeMedalhas());
        
        if (comparacaoMedalhas == 0) {
            return this.name.compareTo(outro.name);
        }
        
        return comparacaoMedalhas;
    }

    public String getGenero() {
        return gender;
    }

    public Pais getPais() {
        return pais;
    }

    public LocalDate getNascimento() {
        return birthDate;
    }

    @Override
    public String toString() {
        String dataFormatada = DateTimeFormatter.ofPattern("dd/MM/yyyy").format(birthDate);
        return name + ", " + gender +". Nascimento: " + dataFormatada+ ". Pais: " + pais.getNome();
    }
}



/** Enumerador para tipos de medalhas */
enum TipoMedalha {
    OURO,
    PRATA,
    BRONZE
}



/**
 * Classe País, que mantém as informações sobre o país e suas medalhas.
 */
class Pais {
    private String nome;
    private Medalhista[] medalhistas;
    private int countMedalhistas;

    public Pais(String nome) {
        this.nome = nome;
        this.medalhistas = new Medalhista[1]; // Inicializa com capacidade 1
        this.countMedalhistas = 0;
    }

    public String getNome() {
        return nome;
    }

    public void incluirMedalhista(Medalhista medalhista) {
        if (countMedalhistas == medalhistas.length) {
            Medalhista[] novoArray = new Medalhista[medalhistas.length * 2];
            System.arraycopy(medalhistas, 0, novoArray, 0, medalhistas.length);
            this.medalhistas = novoArray;
        }
        medalhistas[countMedalhistas] = medalhista;
        countMedalhistas++;
    }

    public int totalDeMedalhas() {
        int total = 0;
        for (int i = 0; i < countMedalhistas; i++) {
            total += medalhistas[i].totalDeMedalhas();
        }
        return total;
    }

    public int totalDeMedalhas(TipoMedalha tipo) {
        int total = 0;
        for (int i = 0; i < countMedalhistas; i++) {
            total += medalhistas[i].totalDeMedalhas(tipo);
        }
        return total;
    }

    public int compareTo(Pais outro) {
        int comparacaoMedalhas = Integer.compare(outro.totalDeMedalhas(), this.totalDeMedalhas());
        
        if (comparacaoMedalhas == 0) {
            return this.nome.compareTo(outro.nome);
        }
        
        return comparacaoMedalhas;
    }

    public String relatorioMedalhistas() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < countMedalhistas; i++) {
            sb.append(medalhistas[i].toString()).append("\n");
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return "País: " + nome;
    }
}


/**
 * Classe Evento: representa um evento olímpico e seus medalhistas.
 */
class Evento implements Comparable<Evento> {
    private String nomeEvento;
    private String esporte;
    private Medalhista[] medalhistas;
    private int medalhistaCount;

    public Evento(String nomeEvento, String esporte) {
        this.nomeEvento = nomeEvento;
        this.esporte = esporte;
        this.medalhistas = new Medalhista[100]; 
        this.medalhistaCount = 0;
    }

    public String getNomeEvento() {
        return nomeEvento;
    }

    public String getEsporte() {
        return esporte;
    }

    public void incluirMedalhista(Medalhista medalhista) {
        for (int i = 0; i < medalhistaCount; i++) {
            if (medalhistas[i].equals(medalhista)) {
                return; 
            }
        }
        medalhistas[medalhistaCount++] = medalhista;
    }

    public Medalhista[] getMedalhistas() {
        Medalhista[] lista = new Medalhista[medalhistaCount];
        System.arraycopy(medalhistas, 0, lista, 0, medalhistaCount);
        return lista;
    }

    @Override
    public int compareTo(Evento outro) {
        int result = this.esporte.compareTo(outro.esporte);
        if (result == 0) {
            result = this.nomeEvento.compareTo(outro.nomeEvento);
        }
        return result;
    }

    @Override
    public String toString() {
        return esporte + " - " + nomeEvento;
    }
}


/**
 * Classe Aplicacao: classe principal que lê o arquivo CSV e processa os dados dos medalhistas.
 */

 public class Aplicacao {
    private static Evento[] eventos = new Evento[500]; 
    private static int eventoCount = 0;
    private static Pais[] paises = new Pais[200];
    private static int paisCount = 0;

    public static void main(String[] args) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("/tmp/medallists.csv"));
            String linha = reader.readLine(); 
            while ((linha = reader.readLine()) != null) {
                processarLinhaCsv(linha);
            }
            reader.close();

            Scanner scanner = new Scanner(System.in);
            int quantidadeEventos = Integer.parseInt(scanner.nextLine());
            Evento[] eventosParaOrdenar = new Evento[quantidadeEventos];

            for (int i = 0; i < quantidadeEventos; i++) {
                String entrada = scanner.nextLine().trim();
                String[] dados = entrada.split(" - ");
                String nomeEsporte = dados[0].trim();
                String nomeEvento = dados[1].trim();
                Evento evento = procurarEvento(nomeEsporte, nomeEvento);
                if (evento != null) {
                    eventosParaOrdenar[i] = evento;
                }
            }
            scanner.close();

            Mergesort<Evento> mergesort = new Mergesort<>();
            Comparator<Evento> comparador = Comparator
                    .comparing(Evento::getEsporte)
                    .thenComparing(Evento::getNomeEvento);

            long inicio = System.currentTimeMillis();
            mergesort.ordenar(eventosParaOrdenar, comparador);
            long fim = System.currentTimeMillis();

            for (Evento evento : eventosParaOrdenar) {
                if (evento != null) {
                    System.out.println(evento);
                    for (Medalhista medalhista : evento.getMedalhistas()) {
                        System.out.println(medalhista);
                    }
                    System.out.println(); 
                }
            }

            BufferedWriter writer = new BufferedWriter(new FileWriter("1489719_mergesort.txt"));
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unused")
    private static void processarLinhaCsv(String linha) {
        String[] dados = linha.split(",");
        String nomeMedalhista = dados[0].trim();
        TipoMedalha tipoMedalha = TipoMedalha.valueOf(dados[1].trim().toUpperCase());
        LocalDate dataMedalha = LocalDate.parse(dados[2].trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String genero = dados[3].trim();
        LocalDate dataNascimento = LocalDate.parse(dados[4].trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String nomePais = dados[5].trim();
        String disciplina = dados[6].trim();
        String nomeEvento = dados[7].trim();

        Pais pais = procurarPais(nomePais);
        if (pais == null) {
            pais = new Pais(nomePais);
            paises[paisCount++] = pais;
        }

        Medalhista medalhista = new Medalhista(nomeMedalhista, genero, dataNascimento, pais);
        Evento evento = procurarEvento(disciplina, nomeEvento);
        if (evento == null) {
            evento = new Evento(nomeEvento, disciplina);
            eventos[eventoCount++] = evento;
        }

        evento.incluirMedalhista(medalhista);
    }

    private static Pais procurarPais(String nome) {
        for (int i = 0; i < paisCount; i++) {
            if (paises[i].getNome().equalsIgnoreCase(nome)) {
                return paises[i];
            }
        }
        return null;
    }
    private static Evento procurarEvento(String esporte, String nomeEvento) {
        for (int i = 0; i < eventoCount; i++) {
            Evento evento = eventos[i];
            if (evento.getEsporte().equalsIgnoreCase(esporte) && evento.getNomeEvento().equalsIgnoreCase(nomeEvento)) {
                return evento;
            }
        }
        return null;
    }
}

