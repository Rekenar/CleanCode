import java.util.Collections;
import java.util.Set;

public class AnalyzedDocument {
	private final String url;
	private final int wordCount;
	private final int imageCount;
	private final int videoCount;
	private final Set<String> links;

	public AnalyzedDocument(String url, int wordCount, int imageCount, int videoCount, Set<String> links) {
		this.url = url;
		this.wordCount = wordCount;
		this.imageCount = imageCount;
		this.videoCount = videoCount;
		this.links = Collections.unmodifiableSet(links);

	}

	@Override
	public String toString() {
		return "URL=" + url + "\n" +
				"wordCount=" + wordCount +
				", imageCount=" + imageCount +
				", videoCount=" + videoCount + "\n";
	}

	public String getUrl() {
		return url;
	}

	public int getWordCount() {
		return wordCount;
	}

	public int getImageCount() {
		return imageCount;
	}

	public int getVideoCount() {
		return videoCount;
	}

	public Set<String> getLinks() {
		return links;
	}
}
