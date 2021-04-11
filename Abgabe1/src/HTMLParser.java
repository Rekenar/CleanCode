import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;

public class HTMLParser {
    private static final int MAX_DEPTH = 2;
    private HashSet<String> links;
    private String content;
    private String fileName;

    public HTMLParser(String fileName) {
        links = new HashSet<>();
        this.fileName = fileName;
        this.content = "";
    }

    public void getLinksFromURL(String URL, int depth) {
        System.out.println(URL + " " + depth);
        if (!links.contains(URL) && depth <= MAX_DEPTH && checkForResponse(URL)) {
            System.out.println(URL + " " + depth);
            try {
                links.add(URL);
                Document doc = Jsoup.connect(URL).get();
                appendContent(URL, doc);

                Elements linksOnPage = doc.select("a[href]");

                depth++;
                for (Element page : linksOnPage) {
                    getLinksFromURL(page.attr("abs:href"), depth);
                }
            } catch (IOException e) {
                System.err.println("For '" + URL + "': " + e.getMessage());
            }
        }
    }

    public void writeAnalysisToFile() {
        try {
            FileWriter fileWriter = new FileWriter(this.fileName);
            fileWriter.append(this.content);
            fileWriter.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void appendContent(String URL, Document doc) {
        content += "URL: " + URL +
                "\nWordCount: " + countWords(doc) +
                "\nImageCount: " + countSelector(doc,"img") +
                "\nVideoCount: " + countSelector(doc,"video") + "\n";
    }

    private int countSelector(Document doc, String selector) {
        int countSelector = 0;
        Elements selectorOnPage = doc.getElementsByTag(selector);
        for (Element selectorElement : selectorOnPage) {
            countSelector++;
        }
        return countSelector;
    }

    private int countWords(Document doc) {
        return doc.text().split(" ").length;
    }

    private boolean checkForResponse(String URL) {
        if(URL.length() < 10)return false;
        try {
            Response response = Jsoup.connect(URL).followRedirects(false).execute();
            if (response.statusCode() == 404) {
                return false;
            }
        } catch (IOException e) {
            System.err.println("For '" + URL + "': " + e.getMessage());
            return false;
        }
        return true;
    }
}
