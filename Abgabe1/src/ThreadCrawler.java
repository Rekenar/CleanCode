import java.util.ArrayList;

public class ThreadCrawler implements Runnable {

    int depth;
    String links;
    ArrayList<AnalyzedDocument> analyzedDocuments;

    public ThreadCrawler(String links, int depth, ArrayList<AnalyzedDocument> a){
        this.links = links;
        this.depth = depth;
        this.analyzedDocuments = a;
    }


    @Override
    public void run() {
        analyzedDocuments.addAll(WebCrawler.main(links, depth));
    }
}
