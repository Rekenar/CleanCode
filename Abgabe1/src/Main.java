import java.util.ArrayList;
import java.util.Scanner;
//https://github.com/jhy/jsoup/blob/) 404 Link

public class Main {
	public static void main(String[] args) {

		Scanner scanner = new Scanner(System.in);

		System.out.print("Geben Sie einen Link ein: ");
		String URL = scanner.next();

		System.out.print("Geben Sie die Tiefe ein: ");
		int depth = scanner.nextInt();

		ArrayList<AnalyzedDocument> a = WebCrawler.main(URL, depth);

		WebCrawler.writeFile("testfile.txt", a);
	}
}