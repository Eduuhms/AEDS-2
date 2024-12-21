import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        String palavra = "";

        do {
            palavra = scanner.nextLine();

            if (!palavra.equals("FIM")) {
                if (ePalindromo(palavra)) {
                    System.out.println("SIM");
                } else {
                    System.out.println("NAO");
                }
            }
        } while (!palavra.equals("FIM"));

        scanner.close(); 
    }

    public static boolean ePalindromo(String palavra) {
        return ePalindromoRecursivo(palavra, 0, palavra.length() - 1);
    }

    private static boolean ePalindromoRecursivo(String palavra, int começo, int fim) {
        if (começo >= fim) {
            return true;
        }

        if (palavra.charAt(começo) != palavra.charAt(fim)) {
            return false;
        }

        return ePalindromoRecursivo(palavra, começo + 1, fim - 1);
    }
}
