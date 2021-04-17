import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;

public class HTMLParser {
    private HashSet<String> links;
    private String content;
    private String fileName;

    public HTMLParser(String fileName) {
        links = new HashSet<>();
        this.fileName = fileName;
        this.content = "";
    }

    public void getLinksFromURL(String URL, int depth) {
        if (!links.contains(URL) && depth >= 0 && !(URL.length() < 10)&& checkForResponse(URL)) {
            links.add(URL);
            try {
                Document doc = Jsoup.connect(URL).get();
                Elements linksOnPage = doc.select("a[href]");

                appendContent(URL, doc);

                for (Element page : linksOnPage) {
                    getLinksFromURL(page.attr("abs:href"), depth-1);
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

    public void appendContent(String URL, Document doc) {
        content += "URL: " + URL +
                "\nWordCount: " + countWords(doc) +
                "\nImageCount: " + countSelector(doc,"img") +
                "\nVideoCount: " + countSelector(doc,"video") + "\n";
    }

    public int countSelector(Document doc, String selector) {
        Elements selectorOnPage = doc.getElementsByTag(selector);
        return selectorOnPage.size();
    }

    public int countWords(Document doc) {
        return doc.text().split(" ").length;
    }

    public boolean checkForResponse(String URL) {
        try {
            URL url = new URL(URL);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            if (connection.getResponseCode() == 404 ){
                System.out.println("404 Link not Found");
                return false;
            }
            return true;
        } catch (IOException e) {
            System.err.println("For '" + URL + "': " + e.getMessage());
        }
        return false;
    }
}
