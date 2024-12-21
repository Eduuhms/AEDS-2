// public class MedicaoDesempenho {

//     public static int codigoA(int n){
//         int b = 0;
//         for(int i = 0; i <= n; i += 2){
//             b += 3;
//         }
//         return b;
//     }

//     public static void main(String[] args) {
//         long limiteTempo = 50 * 1000000000L; // 50 segundos em nanossegundos
//         for (int n = 15625; n > 0 && n <= Integer.MAX_VALUE; n *= 2) {
//             long[] tempos = new long[5];
//             int operacoes = (n / 2) + 1; // número de operações no algoritmo

//             // Realizar 5 medições
//             for (int j = 0; j < 5; j++) {
//                 long inicio = System.nanoTime();
//                 codigoA(n);
//                 long fim = System.nanoTime();
//                 tempos[j] = fim - inicio;
//             }

//             // Ordenar os tempos
//             java.util.Arrays.sort(tempos);

//             // Calcular a média dos 3 tempos intermediários
//             long mediaTempo = (tempos[1] + tempos[2] + tempos[3]) / 3;

//             // Exibir os resultados
//             System.out.println("n = " + n + ", Operações = " + operacoes + ", Tempo Médio = " + mediaTempo + " ns");

//             // Verificar se o tempo limite foi excedido
//             if (mediaTempo > limiteTempo) {
//                 break;
//             }
//         }
//     }
// }


public class MedicaoDesempenho {

    public static void codigoB(int[] vet) {
        for (int i = 0; i < vet.length; i += 2) {
            for (int j = i; j < (vet.length / 2); j++) {
                vet[i] += vet[j];
            }
        }
    }

    public static void main(String[] args) {
        long limiteTempo = 50 * 1000000000L; // 50 segundos em nanossegundos
        for (int n = 15625; n > 0 && n <= Integer.MAX_VALUE / 2; n *= 2) {
            long[] tempos = new long[5];
            long operacoes = 0;

            // Cria um vetor de tamanho n preenchido com valores
            int[] vet = new int[n];
            for (int i = 0; i < vet.length; i++) {
                vet[i] = 1; // Preenchendo com valores para simplificar
            }

            // Contagem de operações
            for (int i = 0; i < vet.length; i += 2) {
                for (int j = i; j < (vet.length / 2); j++) {
                    operacoes++; // Contabiliza a operação vet[i] += vet[j]
                }
            }

            // Realizar 5 medições
            for (int j = 0; j < 5; j++) {
                long inicio = System.nanoTime();
                codigoB(vet.clone()); // Usa clone para evitar interferência entre medições
                long fim = System.nanoTime();
                tempos[j] = fim - inicio;
            }

            // Ordenar os tempos
            java.util.Arrays.sort(tempos);

            // Calcular a média dos 3 tempos intermediários
            long mediaTempo = (tempos[1] + tempos[2] + tempos[3]) / 3;

            // Exibir os resultados
            System.out.println("n = " + n + ", Operações = " + operacoes + ", Tempo Médio = " + mediaTempo + " ns");

            // Verificar se o tempo limite foi excedido
            if (mediaTempo > limiteTempo) {
                break;
            }
        }
    }
}
