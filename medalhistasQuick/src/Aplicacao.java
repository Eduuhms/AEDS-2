import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Comparator;
import java.util.Scanner;

/**
 * Interface IOrdenator
 * @param <T>
 */
interface IOrdenator<T> {
    void ordenar(T[] lista, Comparator<T> comparador);
}

/**
 * Implementação do algoritmo Quicksort genérico
 * @param <T>
 */
class Quicksort<T> implements IOrdenator<T> {
    private long comparacoes;
    private long movs;

    public Quicksort() {
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
    public void ordenar(T[] lista, Comparator<T> comparador) {
        quicksort(lista, 0, lista.length - 1, comparador);
    }

    private void quicksort(T[] lista, int inicio, int fim, Comparator<T> comparador) {
        if (inicio < fim) {
            int pivoIndex = particionar(lista, inicio, fim, comparador);
            quicksort(lista, inicio, pivoIndex - 1, comparador);
            quicksort(lista, pivoIndex + 1, fim, comparador);
        }
    }

    private int particionar(T[] lista, int inicio, int fim, Comparator<T> comparador) {
        T pivo = lista[fim];
        int i = inicio - 1;

        for (int j = inicio; j < fim; j++) {
            comparacoes++;
            if (comparador.compare(lista[j], pivo) <= 0) {
                i++;
                trocar(lista, i, j);
                movs++;
            }
        }
        trocar(lista, i + 1, fim);
        movs++;
        return i + 1;
    }

    private void trocar(T[] lista, int i, int j) {
        T temp = lista[i];
        lista[i] = lista[j];
        lista[j] = temp;
    }
}

/**
 * Classe Medalhista: representa um medalhista olímpico e sua coleção de medalhas.
 */
class Medalhista {
    private String name;
    private TipoMedalha tipoMedalha;

    public Medalhista(String name, TipoMedalha tipoMedalha) {
        this.name = name;
        this.tipoMedalha = tipoMedalha;
    }

    public String getNome() {
        return name;
    }

    public TipoMedalha getTipoMedalha() {
        return tipoMedalha;
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
    private int ouro;
    private int prata;
    private int bronze;

    public Pais(String nome) {
        this.nome = nome;
    }

    public void adicionarMedalha(TipoMedalha tipoMedalha) {
        switch (tipoMedalha) {
            case OURO:
                ouro++;
                break;
            case PRATA:
                prata++;
                break;
            case BRONZE:
                bronze++;
                break;
        }
    }

    public String getNome() {
        return nome;
    }

    public int getOuro() {
        return ouro;
    }

    public int getPrata() {
        return prata;
    }

    public int getBronze() {
        return bronze;
    }

    public int getTotalMedalhas() {
        return ouro + prata + bronze;
    }

    public String toString() {
        return String.format("%s: %02d ouros %02d pratas %02d bronzes Total: %02d medalhas.", nome, ouro, prata, bronze, getTotalMedalhas());
    }
}

public class Aplicacao {
    private static Pais[] paises = new Pais[200];
    private static int paisCount = 0;

    public static void main(String[] args) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("/tmp/medallists.csv"));
            String linha = reader.readLine(); // Ignora o cabeçalho
            while ((linha = reader.readLine()) != null) {
                processarLinhaCsv(linha);
            }
            reader.close();

            Scanner scanner = new Scanner(System.in);
            int quantidadePaises = Integer.parseInt(scanner.nextLine());
            Pais[] paisesParaOrdenar = new Pais[quantidadePaises];

            for (int i = 0; i < quantidadePaises; i++) {
                String nomePais = scanner.nextLine().trim();
                Pais pais = procurarPais(nomePais);
                if (pais != null) {
                    paisesParaOrdenar[i] = pais;
                }
            }
            scanner.close();

            // Ordena os países pela quantidade de medalhas (ouro, prata, bronze)
            Quicksort<Pais> quicksort = new Quicksort<>();
            Comparator<Pais> comparador = (p1, p2) -> {
                if (p1.getOuro() != p2.getOuro()) {
                    return p2.getOuro() - p1.getOuro(); // Maior quantidade de ouro primeiro
                } else if (p1.getPrata() != p2.getPrata()) {
                    return p2.getPrata() - p1.getPrata(); // Maior quantidade de prata em caso de empate
                } else {
                    return p2.getBronze() - p1.getBronze(); // Maior quantidade de bronze em último caso
                }
            };

            quicksort.ordenar(paisesParaOrdenar, comparador);

            // Imprime os países ordenados
            for (Pais pais : paisesParaOrdenar) {
                if (pais != null) {
                    System.out.println(pais);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void processarLinhaCsv(String linha) {
        String[] dados = linha.split(",");
        String nomePais = dados[5].trim();
        TipoMedalha tipoMedalha = TipoMedalha.valueOf(dados[1].trim().toUpperCase());

        Pais pais = procurarPais(nomePais);
        if (pais == null) {
            pais = new Pais(nomePais);
            paises[paisCount++] = pais;
        }

        pais.adicionarMedalha(tipoMedalha);
    }

    private static Pais procurarPais(String nome) {
        for (int i = 0; i < paisCount; i++) {
            if (paises[i].getNome().equalsIgnoreCase(nome)) {
                return paises[i];
            }
        }
        return null;
    }
}
