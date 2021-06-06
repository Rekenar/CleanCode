import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class WebCrawlerTest {
	private Document doc;
	private AnalyzedDocument analyzedDoc;
	private List<AnalyzedDocument> myAnalyzedDocs;

	@BeforeEach
	public void setup() throws Exception {
		File input = new File("Abgabe1/ressources/ex4.html");
		this.doc = Jsoup.parse(input, "UTF-8");
		this.analyzedDoc = WebCrawler.analyze(doc, "ex4.html");
		Set<String> links = new HashSet<>() {
			{
				add("http://www.aau.at");
				add("http://www.aau.at/dummy1");
			}
		};
		myAnalyzedDocs = Arrays.asList(new AnalyzedDocument("dummy1.com", 150, 25, 1, links), new AnalyzedDocument("dummy2.com", 255, 1, 3, links));
	}

	@Test
	public void shouldReturnTrue_WhenAnalyzerisCorrect() {
		assertEquals(184, analyzedDoc.getWordCount());
		assertEquals(4, analyzedDoc.getImageCount());
		assertEquals(0, analyzedDoc.getVideoCount());
	}

	@Test
	public void shouldReturnTrue_WhenFileWriterWorks() throws IOException {
		WebCrawler.writeFile("fileWriterTest.txt", Collections.singletonList(analyzedDoc));
		assertTrue(new File("fileWriterTest.txt").exists());
		assertEquals(analyzedDoc.toString(), new String(Files.readAllBytes(Paths.get("fileWriterTest.txt"))));
	}

	@Test
	public void shouldReturnTrue_WhenAnalyzedDocumentsToStringWorks() {
		String testString = "URL=dummy1.com\n" +
				"wordCount=150, imageCount=25, videoCount=1\n" +
				"URL=dummy2.com\n" +
				"wordCount=255, imageCount=1, videoCount=3\n";

		assertEquals(analyzedDoc.toString(), WebCrawler.analyzedDocumentsToString(Collections.singletonList(analyzedDoc)));
		assertEquals(testString, WebCrawler.analyzedDocumentsToString(myAnalyzedDocs));
	}

	@Test
	public void shouldReturnTrue_WhenGetDocumentFromUrlWorks() {
		assertTrue(WebCrawler.getDocumentFromURL("a.com").isEmpty());
		assertTrue(WebCrawler.getDocumentFromURL("http://www.a.com").isEmpty());
		assertTrue(WebCrawler.getDocumentFromURL("https://www.google.com/").isPresent());

	}

	@Test
	public void shouldReturnTrue_WhenCountSelectorWorks() {
		Document document = Jsoup.parse("<video></video>, <video></video>, <video></video>, <img src=\"pic1.jpg\">, <img src=\"pic1.jpg\">, <img src=\"pic1.jpg\">");
		assertEquals(3, WebCrawler.countSelector(document, "img"));
		assertEquals(3, WebCrawler.countSelector(document, "video"));
	}

	@Test
	public void shouldReturnTrue_WhenWordCountWorks() {
		Document document = Jsoup.parse("Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.");
		assertEquals(100, WebCrawler.countWords(document));
	}

}


