import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AlgebraBool {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String entrada;

        while (true) {
            entrada = scanner.nextLine();

            if (entrada.equals("0")) {
                break;
            }

            if (entrada.trim().isEmpty()) {
                continue;
            }

            String[] partes = entrada.split(" ");
            int quantidadeValores = Integer.parseInt(partes[0]);
            int[] valores = new int[quantidadeValores];

            for (int i = 0; i < quantidadeValores; i++) {
                valores[i] = Integer.parseInt(partes[i + 1]);
            }

            String expressao = entrada.substring(entrada.indexOf(partes[quantidadeValores + 1]));
            int resultado = avaliarExpressao(expressao, valores);
            System.out.println(resultado);
        }

        scanner.close();
    }

    private static int avaliarExpressao(String expressao, int[] valores) {
        return avaliarRecursivo(expressao.replaceAll(" ", ""), valores);
    }

    private static int avaliarRecursivo(String expressao, int[] valores) {
        if (expressao.length() == 1) {
            char caractere = expressao.charAt(0);
            return Character.isDigit(caractere) ? Character.getNumericValue(caractere) : valores[caractere - 'A'];
        }

        if (expressao.startsWith("not(")) {
            int fim = encontrarFechamento(expressao, 4);
            int resultado = avaliarRecursivo(expressao.substring(4, fim), valores);
            return resultado == 1 ? 0 : 1;
        }

        if (expressao.startsWith("and(")) {
            int fim = encontrarFechamento(expressao, 4);
            String[] subExpressoes = separarArgumentos(expressao.substring(4, fim));
            int resultado = 1;  
            for (String subExpressao : subExpressoes) {
                resultado &= avaliarRecursivo(subExpressao, valores);
            }
            return resultado;
        }

        if (expressao.startsWith("or(")) {
            int fim = encontrarFechamento(expressao, 3);
            String[] subExpressoes = separarArgumentos(expressao.substring(3, fim));
            int resultado = 0;  
            for (String subExpressao : subExpressoes) {
                resultado |= avaliarRecursivo(subExpressao, valores);
            }
            return resultado;
        }

        return 0;
    }

    private static int encontrarFechamento(String expressao, int inicio) {
        int contador = 1;
        for (int i = inicio; i < expressao.length(); i++) {
            char caractere = expressao.charAt(i);
            if (caractere == '(') contador++;
            if (caractere == ')') contador--;
            if (contador == 0) return i;
        }
        return -1;
    }

    private static String[] separarArgumentos(String expressao) {
        List<String> argumentos = new ArrayList<>();
        int contador = 0;
        int inicio = 0;

        for (int i = 0; i < expressao.length(); i++) {
            char caractere = expressao.charAt(i);
            if (caractere == '(') contador++;
            if (caractere == ')') contador--;
            if (caractere == ',' && contador == 0) {
                argumentos.add(expressao.substring(inicio, i).trim());
                inicio = i + 1;
            }
        }

        argumentos.add(expressao.substring(inicio).trim());

        return argumentos.toArray(new String[0]);
    }
}
