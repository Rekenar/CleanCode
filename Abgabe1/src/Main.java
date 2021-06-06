import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.Semaphore;
//https://github.com/jhy/jsoup/blob/) 404 Link

public class Main {
	public static void main(String[] args) throws InterruptedException {
		Scanner scanner = new Scanner(System.in);
		ArrayList<AnalyzedDocument> a = new ArrayList<>();

		System.out.print("Geben Sie einen Link ein: ");
		String URL = scanner.nextLine();

		String[] array = URL.split(" ");

		System.out.print("Geben Sie die Tiefe ein: ");
		int depth = scanner.nextInt();


		a = WebCrawler.multipleThread(array,depth);
/*
		Thread[] threads = new Thread[array.length];
		Semaphore semaphore = new Semaphore(-array.length+1);

		for (int i = 0; i < threads.length; i++) {
			String link = array[i];
			threads[i] = new Thread(() -> {
				a.addAll(WebCrawler.main(link, depth));
				semaphore.release();
			});
			threads[i].start();
		}

		semaphore.acquire();

 */

		WebCrawler.writeFile("testfile.txt", a);
	}
}