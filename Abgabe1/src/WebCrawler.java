import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public final class WebCrawler {

	private WebCrawler() {}
	//Analyze the given Document Object and count the number of words, images and videos.
	//Additionaly find all links in the HTML and add them to a Set
	//Return the AnalyzedDocument Object
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
	//Create ArrayList of AnalyzedDocument Objects.
	//Call crawlThroughWebpage
	//return the ArrayList
	public static ArrayList<AnalyzedDocument> main(String url, int depth) {
		ArrayList<AnalyzedDocument> myDocument = new ArrayList<>();
		crawlThroughWebpage(url, depth, myDocument);

		return myDocument;
	}
	//Write the AnalyzedDocument to the File
	public static void writeFile(String filename, List<AnalyzedDocument> doc) {
		try {
			FileWriter fileWriter = new FileWriter(filename);
			//Write the to String transformed List of AnalyzedDocument Objects to the File
			fileWriter.write(analyzedDocumentsToString(doc));
			fileWriter.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	//Transform the List of AnalyzedDocument to a String
	public static String analyzedDocumentsToString(List<AnalyzedDocument> doc) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < doc.size(); i++) {
			sb.append(doc.get(i).toString());
		}
		return sb.toString();
	}
	//Crawls through the Webpages recursively
	private static void crawlThroughWebpage(String url, int depth, List<AnalyzedDocument> DocumentList) {
		if (depth >= 0) {
			//Get Optional Document from URL
			Optional<Document> toAnalyze = getDocumentFromURL(url);
			//Optional is empty return
			if (toAnalyze.isEmpty()) {
				return;
			}
			//Get AnalyzedDocument Object from Document Object and URL
			AnalyzedDocument a = analyze(toAnalyze.get(), url);
			DocumentList.add(a);//Add the AnalyzedDocument to the DocumentList
			//For every link in the AnalyzedDocument Object call crawlThroughWebpage
			for (String link : a.getLinks()) {
				crawlThroughWebpage(link, depth - 1, DocumentList);
			}
		}
	}
	//It gets the Document object from the given URL
	public static Optional<Document> getDocumentFromURL(String url) {
		//Only try when the URL is longer than 12 "https://www."
		if (!(url.length() < 12)) {
			try {
				return Optional.of(Jsoup.connect(url).get()); //Connect to the URL and get the Data
			} catch (IOException e) {
				System.err.println("For '" + url + "': " + e.getMessage());
			}
		}
		return Optional.empty(); //Return empty Document if URL is not functioning
	}
	//Counts the specified selector of the HTML.
	//It counts the selector by using a css query and returning the size of the List of Elements
	public static int countSelector(Document doc, String selector) {
		Elements selectorOnPage = doc.getElementsByTag(selector);
		return selectorOnPage.size();
	}
	//Counts the words of the HTML.
	//It counts the words by splitting it on spaces.
	public static int countWords(Document doc) {
		return doc.text().split(" ").length;
	}
}
