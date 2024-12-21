import java.util.Scanner;

public class ContaOcorrencias{
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            String linha = scanner.nextLine();
            if (linha.equals("FIM")) {
                break;
            }

            String[] elementos = linha.split(";");
            int[] A = new int[elementos.length];
            for (int i = 0; i < elementos.length; i++) {
                A[i] = Integer.parseInt(elementos[i]);
            }

            int x = Integer.parseInt(scanner.nextLine());

            int ocorrencias = contarOcorrencias(A, A.length, x);
            System.out.println(ocorrencias);
        }

        scanner.close();
    }

    public static int contarOcorrencias(int[] A, int n, int x) {
        if (n == 0) {
            return 0;
        }

        int ocorrenciasNoResto = contarOcorrencias(A, n - 1, x);

        if (A[n - 1] == x) {
            return 1 + ocorrenciasNoResto;
        } else {
            return ocorrenciasNoResto;
        }
    }
}