import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public final class WebCrawler {
	private WebCrawler() {

	}

	public static AnalyzedDocument analyze(Document doc, String url) {
		int words = countWords(doc);
		int imgs = countSelector(doc, "img");
		int videos = countSelector(doc, "video");

		Set<String> links = new HashSet<>();

		Elements linksOnPage = doc.select("a[href]");

		for (Element page : linksOnPage) {
			links.add(page.attr("abs:href"));
		}

		return new AnalyzedDocument(url, words, imgs, videos, links);
	}

	public static ArrayList<AnalyzedDocument> main(String url, int depth) {
		ArrayList<AnalyzedDocument> myDocument = new ArrayList<>();
		crawlThroughWebpage(url, depth, myDocument);

		return myDocument;
	}

	public static void writeFile(String filename, List<AnalyzedDocument> doc) {
		try {
			FileWriter fileWriter = new FileWriter(filename);
			fileWriter.append(analyzedDocumentsToString(doc));
			fileWriter.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static String analyzedDocumentsToString(List<AnalyzedDocument> doc) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < doc.size(); i++) {
			sb.append(doc.get(i).toString());
		}
		return sb.toString();
	}

	private static void crawlThroughWebpage(String url, int depth, List<AnalyzedDocument> DocumentList) {
		if (depth >= 0) {
			Optional<Document> toAnalyze = getDocumentFromURL(url);

			if (toAnalyze.isEmpty()) {
				return;
			}

			AnalyzedDocument a = analyze(toAnalyze.get(), url);
			DocumentList.add(a);

			for (String link : a.getLinks()) {
				crawlThroughWebpage(link, depth - 1, DocumentList);
			}
		}
	}

	public static Optional<Document> getDocumentFromURL(String url) {
		if (!(url.length() < 10)) {
			try {
				return Optional.of(Jsoup.connect(url).get());
			} catch (IOException e) {
				System.err.println("For '" + url + "': " + e.getMessage());
			}
		}
		return Optional.empty();
	}

	public static int countSelector(Document doc, String selector) {
		Elements selectorOnPage = doc.getElementsByTag(selector);
		return selectorOnPage.size();
	}

	public static int countWords(Document doc) {
		return doc.text().split(" ").length;
	}
}
