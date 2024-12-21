import java.util.Arrays;

public class Experimentos {

    static class Resultados {
        boolean encontrado;
        long tempo;
        long operacoes;

        Resultados(boolean encontrado, long tempo, long operacoes) {
            this.encontrado = encontrado;
            this.tempo = tempo;
            this.operacoes = operacoes;
        }
    }

    boolean pesquisaSequencial(int[] vetor, int x, long[] operacoes) {
        boolean resposta = false;
        int n = vetor.length;
        for (int i = 0; i < n; i++) {
            operacoes[0]++;
            if (vetor[i] == x) {
                resposta = true;
                break;
            }
        }
        return resposta;
    }

    boolean pesquisaBinariaRecursiva(int[] vetor, int x, long[] operacoes) {
        return pesquisaBinariaRecursiva(vetor, x, 0, vetor.length - 1, operacoes);
    }

    boolean pesquisaBinariaRecursiva(int[] vetor, int x, int inicio, int fim, long[] operacoes) {
        operacoes[0]++;
        if (inicio > fim) {
            return false;
        }

        int meio = (inicio + fim) / 2;
        if (vetor[meio] == x) {
            return true;
        } else if (vetor[meio] < x) {
            return pesquisaBinariaRecursiva(vetor, x, meio + 1, fim, operacoes);
        } else {
            return pesquisaBinariaRecursiva(vetor, x, inicio, meio - 1, operacoes);
        }
    }

    void executarExperimentos() {
        long n = 7500000;
        long maxN = 2000000000;

        while (n <= maxN) {
            System.out.println("Tamanho do vetor: " + n);

            int[] vetor = new int[(int) n];
            for (int i = 0; i < vetor.length; i++) {
                vetor[i] = i;
            }

            // Cenários de pesquisa
            int[] chavesPesquisa = { vetor[vetor.length / 2], vetor[0], -1 }; // Elemento presente, primeiro elemento,
                                                                              // elemento ausente

            for (int i = 0; i < chavesPesquisa.length; i++) {
                System.out.println("Cenário " + (i + 1) + ": Pesquisando por "
                        + (i == 2 ? "elemento ausente" : "elemento presente"));

                // Pesquisa Sequencial
                realizarMedicoes("Pesquisa Sequencial", vetor, chavesPesquisa[i], false);

                // Pesquisa Binária
                Arrays.sort(vetor); // É necessário ordenar o vetor para a pesquisa binária
                realizarMedicoes("Pesquisa Binária", vetor, chavesPesquisa[i], true);
            }

            n *= 2; // Dobrar o valor de n a cada passo
        }
    }

    void realizarMedicoes(String tipoPesquisa, int[] vetor, int chave, boolean binaria) {
        long[] tempos = new long[5];
        long[] operacoes = new long[5];

        for (int i = 0; i < 5; i++) {
            long[] operacoesContagem = { 0 };

            long inicio = System.nanoTime();
            boolean encontrado;
            if (binaria) {
                encontrado = pesquisaBinariaRecursiva(vetor, chave, operacoesContagem);
            } else {
                encontrado = pesquisaSequencial(vetor, chave, operacoesContagem);
            }
            long fim = System.nanoTime();

            tempos[i] = fim - inicio;
            operacoes[i] = operacoesContagem[0];
        }

        Arrays.sort(tempos);
        Arrays.sort(operacoes);
        long tempoMedio = (tempos[1] + tempos[2] + tempos[3]) / 3;
        long operacoesMedias = (operacoes[1] + operacoes[2] + operacoes[3]) / 3;

        System.out
                .println("  " + tipoPesquisa + " - Tempo médio: " + tempoMedio + " ns, Operações: " + operacoesMedias);
    }

    public static void main(String[] args) {
        Experimentos exp = new Experimentos();
        exp.executarExperimentos();
    }
}