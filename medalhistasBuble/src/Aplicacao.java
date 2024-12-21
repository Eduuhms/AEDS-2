import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
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
 * Implementação do algoritmo BubbleSort genérico
 * @param <T>
 */
class Bubblesort<T> implements IOrdenator<T> {
    private long comparacoes;
    private long movs;

    public Bubblesort() {
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
        boolean trocou;

        long inicio = System.currentTimeMillis();

        for (int i = 0; i < n - 1; i++) {
            trocou = false;
            for (int j = 0; j < n - i - 1; j++) {
                comparacoes++;
                if (comparador.compare(lista.get(j), lista.get(j + 1)) > 0) {
                    Collections.swap(lista, j, j + 1);
                    movs++;
                    trocou = true;
                }
            }
            if (!trocou) {
                break;
            }
        }

        long fim = System.currentTimeMillis();
        long tempoExec = fim - inicio;

        try {
            FileWriter writer = new FileWriter("1489719_bubblesort.txt");
            writer.write("1489719\t" + tempoExec + "\t" + comparacoes + "\t" + movs);
            System.out.println("1489719\t" + tempoExec + "\t" + comparacoes + "\t" + movs);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
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

    public int contarMedalhas(TipoMedalha tipo) {
        int count = 0;
        for (int i = 0; i < medalCount; i++) {
            if (medals[i].getTipo() == tipo) {
                count++;
            }
        }
        return count;
    }

    public String getPais() {
        return country;
    }

    public LocalDate getNascimento() {
        return LocalDate.from(birthDate);
    }

    public String getNome() {
        return name;
    }

    public String toString() {
        return String.format("%s, %s. Nascimento: %s. Pais: %s", name, gender.toUpperCase(),
            DateTimeFormatter.ofPattern("dd/MM/yyyy").format(birthDate), country);
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

            // Ordena a lista de medalhistas usando BubbleSort
            Bubblesort<Medalhista> bubblesort = new Bubblesort<>();
            Comparator<Medalhista> comparador = (m1, m2) -> {
                if (m1.contarMedalhas(TipoMedalha.OURO) != m2.contarMedalhas(TipoMedalha.OURO)) {
                    return Integer.compare(m2.contarMedalhas(TipoMedalha.OURO), m1.contarMedalhas(TipoMedalha.OURO));
                } else if (m1.contarMedalhas(TipoMedalha.PRATA) != m2.contarMedalhas(TipoMedalha.PRATA)) {
                    return Integer.compare(m2.contarMedalhas(TipoMedalha.PRATA), m1.contarMedalhas(TipoMedalha.PRATA));
                } else if (m1.contarMedalhas(TipoMedalha.BRONZE) != m2.contarMedalhas(TipoMedalha.BRONZE)) {
                    return Integer.compare(m2.contarMedalhas(TipoMedalha.BRONZE), m1.contarMedalhas(TipoMedalha.BRONZE));
                } else {
                    return m1.getNome().compareToIgnoreCase(m2.getNome());
                }
            };

            bubblesort.ordenar(medalhistasParaOrdenar, comparador);

            // Imprime os medalhistas ordenados
            for (Medalhista medalhista : medalhistasParaOrdenar) {
                System.out.println(medalhista);
                
                // Contagem de medalhas
                int ouros = medalhista.contarMedalhas(TipoMedalha.OURO);
                int pratas = medalhista.contarMedalhas(TipoMedalha.PRATA);
                int bronzes = medalhista.contarMedalhas(TipoMedalha.BRONZE);
                
                // Exibe somente se tiver pelo menos uma medalha de ouro
                if (ouros > 0) {
                    System.out.println("Quantidade de medalhas de ouro: " + ouros);
                }
                
                // Exibe somente se tiver pelo menos uma medalha de prata
                if (pratas > 0) {
                    System.out.println("Quantidade de medalhas de prata: " + pratas);
                }
                
                // Exibe somente se tiver pelo menos uma medalha de bronze
                if (bronzes > 0) {
                    System.out.println("Quantidade de medalhas de bronze: " + bronzes);
                }

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