import java.util.Scanner;

public class EvergreenIntercalacao {
    
    public static String trocaNome(String linha1, String linha2) {
        StringBuilder nomeCorreto = new StringBuilder();
        int i = 0;
        int j = 0; 

        while (i < linha1.length() || j < linha2.length()) {

            if (i < linha1.length()) {
                nomeCorreto.append(linha1.charAt(i++));
            }
            if (i < linha1.length()) {
                nomeCorreto.append(linha1.charAt(i++));
            }

            if (j < linha2.length()) {
                nomeCorreto.append(linha2.charAt(j++));
            }
            if (j < linha2.length()) {
                nomeCorreto.append(linha2.charAt(j++));
            }
        }
        
        return nomeCorreto.toString();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            String linha1 = scanner.nextLine();
            if (linha1.equals("FIM")) {
                break;
            }

            String linha2 = scanner.nextLine();
            
            System.out.println(trocaNome(linha1, linha2));
        }
        
        scanner.close();
    }
}
