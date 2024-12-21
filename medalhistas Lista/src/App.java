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

    public E remover(int posicao) {

        Celula<E> anterior, celulaRemovida, proximaCelula;

        if (vazia())
            throw new IllegalStateException("Não foi possível remover o item da lista: "
                    + "a lista está vazia!");

        if ((posicao < 0) || (posicao >= this.tamanho))
            throw new IndexOutOfBoundsException("Não foi possível remover o item da lista: "
                    + "a posição informada é inválida!");

        anterior = this.primeiro;
        for (int i = 0; i < posicao; i++)
            anterior = anterior.getProximo();

        celulaRemovida = anterior.getProximo();

        proximaCelula = celulaRemovida.getProximo();

        anterior.setProximo(proximaCelula);
        celulaRemovida.setProximo(null);

        if (celulaRemovida == this.ultimo)
            this.ultimo = anterior;

        this.tamanho--;

        return (celulaRemovida.getItem());
    }

    public void inserirInicio(E item) {
        Celula<E> novaCelula = new Celula<>(item);
        novaCelula.setProximo(primeiro.getProximo());
        primeiro.setProximo(novaCelula);

        if (vazia()) {
            ultimo = novaCelula;
        }

        tamanho++;
    }

    public void inserirFinal(E item) {
        Celula<E> novaCelula = new Celula<>(item);
        ultimo.setProximo(novaCelula);
        ultimo = novaCelula;
        tamanho++;
    }

    public E removerInicio() {
        if (vazia()) {
            throw new IllegalStateException("Não foi possível remover o item da lista: "
                    + "a lista está vazia!");
        }
        Celula<E> removida = primeiro.getProximo();
        primeiro.setProximo(removida.getProximo());
        removida.setProximo(null);

        if (removida == ultimo) {
            ultimo = primeiro;
        }

        tamanho--;
        return removida.getItem();
    }

    public E localizar(E procurado) {
        Celula<E> atual = primeiro.getProximo();

        while (atual != null) {
            if (atual.getItem().equals(procurado)) {
                return atual.getItem();
            }
            atual = atual.getProximo();
        }

        throw new NoSuchElementException("Elemento não encontrado na lista.");
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("");
        Celula<E> atual = primeiro.getProximo();

        while (atual != null) {
            stringBuilder.append(atual.getItem());
            if (atual.getProximo() != null) {
                stringBuilder.append("\n");
            }
            atual = atual.getProximo();
        }

        stringBuilder.append("");
        return stringBuilder.toString();
    }

    public void inverter() {
        Lista<E> aux = new Lista<>();

        while (!this.vazia()) {
            aux.inserirInicio(this.removerInicio());
        }

        while (!aux.vazia()) {
            this.inserirFinal(aux.removerInicio());
        }
    }

    public Lista<E> obterListaSemRepeticao() {
        Lista<E> novaLista = new Lista<>();
        Celula<E> atual = primeiro.getProximo();

        while (atual != null) {
            if (!elementoJaAdicionado(novaLista, atual.getItem())) {
                novaLista.inserirFinal(atual.getItem());
            }
            atual = atual.getProximo();
        }

        return novaLista;
    }

    private boolean elementoJaAdicionado(Lista<E> lista, E item) {
        Celula<E> atual = lista.primeiro.getProximo();

        while (atual != null) {
            if (atual.getItem().equals(item)) {
                return true;
            }
            atual = atual.getProximo();
        }

        return false;
    }

    public int tamanho() {
        return tamanho;
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
    private static final int MAX_MEDALHISTAS = 100000;
    private static Medalhista[] medalhistas = new Medalhista[MAX_MEDALHISTAS];
    private static int medalhistaCount = 0;
    private static Lista<Medalhista> lista = new Lista<>();

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
                case "INSERIR INICIO":
                    nome = partes[1];
                    encontrado = Medalhista.buscarMedalhista(nome, medalhistas, medalhistaCount);
                    if (encontrado != null) {
                        lista.inserirInicio(encontrado);
                    } else {
                        System.out.println("Medalhista não encontrado.");
                    }
                    break;

                case "INSERIR FINAL":
                    nome = partes[1];
                    encontrado = Medalhista.buscarMedalhista(nome, medalhistas, medalhistaCount);
                    if (encontrado != null) {
                        lista.inserirFinal(encontrado);
                    } else {
                        System.out.println("Medalhista não encontrado.");
                    }
                    break;

                case "REMOVER INICIO":
                    try {
                        Medalhista removido = lista.removerInicio();
                        System.out.println("(REMOVIDO) " + removido);

                        int ouros = removido.ouros().length;
                        int pratas = removido.pratas().length;
                        int bronzes = removido.bronzes().length;

                        int maxCount = Math.max(ouros, Math.max(pratas, bronzes));

                        boolean empateOuroPrata = (ouros == maxCount && pratas == maxCount);
                        boolean empateOuroBronze = (ouros == maxCount && bronzes == maxCount);
                        boolean empatePrataBronze = (pratas == maxCount && bronzes == maxCount);

                        if (empateOuroPrata) {
                            System.out.println("Quantidade de medalhas de ouro: " + maxCount);
                            System.out.println("Quantidade de medalhas de prata: " + maxCount);
                        } else if (empateOuroBronze) {
                            System.out.println("Quantidade de medalhas de ouro: " + maxCount);
                            System.out.println("Quantidade de medalhas de bronze: " + maxCount);
                        } else if (empatePrataBronze) {
                            System.out.println("Quantidade de medalhas de prata: " + maxCount);
                            System.out.println("Quantidade de medalhas de bronze: " + maxCount);
                        } else {

                            if (ouros == maxCount) {
                                System.out.println("Quantidade de medalhas de ouro: " + maxCount);
                            } else if (pratas == maxCount) {
                                System.out.println("Quantidade de medalhas de prata: " + maxCount);
                            } else {
                                System.out.println("Quantidade de medalhas de bronze: " + maxCount);
                            }
                        }
                        System.out.println();
                    } catch (NoSuchElementException e) {
                        System.out.println("Lista vazia.");
                    }
                    break;

                case "INVERTER":
                    lista.inverter();
                    System.out.println("LISTA INVERTIDA DE MEDALHISTAS");
                    System.out.println(lista);
                    lista = new Lista<>();
                    System.out.println();
                    break;

                case "SEM REPETICAO":
                    Lista<Medalhista> listaSemRepeticoes = lista.obterListaSemRepeticao();
                    System.out.println("LISTA DE MEDALHISTAS SEM REPETICAO");
                    System.out.println(listaSemRepeticoes);
                    System.out.println();
                    break;

                default:
                    System.out.println("Comando não reconhecido.");
                    break;
            }
        }
        scannerzin.close();
    }
}
