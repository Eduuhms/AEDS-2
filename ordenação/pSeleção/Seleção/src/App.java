import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String entradaNum = scanner.nextLine();

        while (!entradaNum.equals("FIM")) {

            String[] numerosStr = entradaNum.split(";");
            int[] numeros = new int[numerosStr.length];

            for (int i = 0; i < numerosStr.length; i++) {
                numeros[i] = Integer.parseInt(numerosStr[i]);
            }

            int comparacoes = comparar(numeros);

            for (int i = 0; i < numeros.length; i++) {
                if (i > 0) System.out.print(" ");
                System.out.print(numeros[i]);
            }
            System.out.println();

            System.out.println("Comparacoes realizadas: " + comparacoes);

            entradaNum = scanner.nextLine();
        }

        scanner.close();
    }
    public static int comparar(int[] arr) {
        int comparacoes = 0;
        int quantidade = arr.length;
    
        for (int i = 0; i < quantidade - 1; i++) {
            int menorValor = i;
            for (int j = i + 1; j < quantidade; j++) {
                comparacoes++;
                if (arr[j] < arr[menorValor]) {
                    menorValor = j;
                }
            }
    
            int temp = arr[menorValor];
            arr[menorValor] = arr[i];
            arr[i] = temp;
        }
    
        return comparacoes;
    }
}
