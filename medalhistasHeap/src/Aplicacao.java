import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

/**
 * Interface IOrdenator
 * @param <T>
 */
interface IOrdenator<T> {
    void ordenar(List<T> lista, Comparator<T> comparador);
}

/**
 * Implementação do algoritmo HeapSort genérico
 * @param <T>
 */
class HeapSort<T> implements IOrdenator<T> {
    private long comparacoes;
    private long movs;

    public HeapSort() {
        this.comparacoes = 0;
        this.movs = 0;
    }

    public long getComparacoes() {
        return comparacoes;
    }

    public long getMovimentacoes() {
        return movs;
    }

    @Override
    public void ordenar(List<T> lista, Comparator<T> comparador) {
        int n = lista.size();
        long inicio = System.currentTimeMillis();

        // Construir o heap (reorganizar a lista)
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(lista, n, i, comparador);
        }

        // Extrair um a um os elementos do heap
        for (int i = n - 1; i > 0; i--) {
            // Mover a raiz atual para o final
            T temp = lista.get(0);
            lista.set(0, lista.get(i));
            lista.set(i, temp);
            movs++;

            // Chamar heapify na raiz reduzida
            heapify(lista, i, 0, comparador);
        }

        long fim = System.currentTimeMillis();
        long tempoExec = fim - inicio;

        try {
            FileWriter writer = new FileWriter("1489719_heapsort.txt");
            writer.write("1489719\t" + tempoExec + "\t" + comparacoes + "\t" + movs);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Função para reorganizar a árvore em forma de heap
    private void heapify(List<T> lista, int n, int i, Comparator<T> comparador) {
        int maior = i;  // Inicializa o maior como a raiz
        int esquerda = 2 * i + 1;  // Filho esquerdo
        int direita = 2 * i + 2;   // Filho direito

        // Se o filho esquerdo é maior que a raiz
        if (esquerda < n) {
            comparacoes++;
            if (comparador.compare(lista.get(esquerda), lista.get(maior)) > 0) {
                maior = esquerda;
            }
        }

        // Se o filho direito é maior que o maior até agora
        if (direita < n) {
            comparacoes++;
            if (comparador.compare(lista.get(direita), lista.get(maior)) > 0) {
                maior = direita;
            }
        }

        // Se o maior não for a raiz
        if (maior != i) {
            T swap = lista.get(i);
            lista.set(i, lista.get(maior));
            lista.set(maior, swap);
            movs++;

            // Recursivamente aplicar o heapify na subárvore
            heapify(lista, n, maior, comparador);
        }
    }
}

/**
 * Classe Medalhista: representa um medalhista olímpico e sua coleção de medalhas.
 */
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

    public String getPais() {
        return country;
    }

    public String getNome() {
        return name;
    }

    public String toString() {
        return String.format("%s, %s. Nascimento: %s. Pais: %s", name, gender.toUpperCase(),
            DateTimeFormatter.ofPattern("dd/MM/yyyy").format(birthDate), country.toUpperCase());
    }
}

/** Enumerador para tipos de medalhas */
enum TipoMedalha {
    OURO,
    PRATA,
    BRONZE
}

/** Representa uma medalha obtida nos Jogos Olímpicos */
class Medalha {
    private TipoMedalha metalType;
    private LocalDate medalDate;
    private String discipline;
    private String event;

    public Medalha(TipoMedalha tipo, LocalDate data, String disciplina, String evento) {
        this.metalType = tipo;
        this.medalDate = data;
        this.discipline = disciplina;
        this.event = evento;
    }

    public TipoMedalha getTipo() {
        return metalType;
    }

    public String toString() {
        String dataFormatada = DateTimeFormatter.ofPattern("dd/MM/yyyy").format(medalDate);
        return String.format("%s - %s - %s - %s", metalType.toString(), discipline, event, dataFormatada);
    }
}

public class Aplicacao {
    private static List<Medalhista> medalhistas = new ArrayList<>();

    public static void main(String[] args) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("/tmp/medallists.csv"));
            String linha = reader.readLine(); // Ignora o cabeçalho
            while ((linha = reader.readLine()) != null) {
                processarLinhaCsv(linha);
            }
            reader.close();

            Scanner scanner = new Scanner(System.in);
            int quantidadeMedalhistas = Integer.parseInt(scanner.nextLine());

            List<Medalhista> medalhistasParaOrdenar = new ArrayList<>();
            for (int i = 0; i < quantidadeMedalhistas; i++) {
                String nome = scanner.nextLine().trim();
                Medalhista medalhista = procurarMedalhista(nome);
                if (medalhista != null) {
                    medalhistasParaOrdenar.add(medalhista);
                }
            }
            scanner.close();

            // Ordena a lista de medalhistas pelo país e nome
            HeapSort<Medalhista> heapSort = new HeapSort<>();
            Comparator<Medalhista> comparador = (m1, m2) -> {
                // Comparar pelo país
                int cmpPais = m1.getPais().toUpperCase().compareTo(m2.getPais().toUpperCase());
                if (cmpPais != 0) {
                    return cmpPais;
                }
                // Se os países forem iguais, comparar pelo nome em letras maiúsculas
                return m1.getNome().toUpperCase().compareTo(m2.getNome().toUpperCase());
            };

            heapSort.ordenar(medalhistasParaOrdenar, comparador);

            // Imprime os medalhistas ordenados
            for (Medalhista medalhista : medalhistasParaOrdenar) {
                System.out.println(medalhista);
                System.out.println(); // Linha vazia entre os medalhistas
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void processarLinhaCsv(String linha) {
        String[] dados = linha.split(",");
        String nome = dados[0].trim();
        TipoMedalha tipoMedalha = TipoMedalha.valueOf(dados[1].trim().toUpperCase());
        LocalDate dataMedalha = LocalDate.parse(dados[2].trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String genero = dados[3].trim();
        LocalDate nascimento = LocalDate.parse(dados[4].trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String pais = dados[5].trim();
        String disciplina = dados[6].trim();
        String evento = dados[7].trim();

        Medalha medalha = new Medalha(tipoMedalha, dataMedalha, disciplina, evento);
        Medalhista medalhista = procurarMedalhista(nome);

        if (medalhista == null) {
            medalhista = new Medalhista(nome, genero, nascimento, pais);
            medalhistas.add(medalhista);
        }

        medalhista.incluirMedalha(medalha);
    }

    private static Medalhista procurarMedalhista(String nome) {
        for (Medalhista m : medalhistas) {
            if (m.getNome().equalsIgnoreCase(nome)) {
                return m;
            }
        }

        return null;
    }
}
