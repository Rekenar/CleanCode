import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        HTMLParser htmlParser = new HTMLParser("html.txt");
        System.out.print("Geben Sie einen Link ein: ");
        htmlParser.getLinksFromURL(scanner.next(),0);
        htmlParser.writeHTMLToFile();
    }
}