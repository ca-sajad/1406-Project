package fruits1.search_engine.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Page {
    private String link;
    private String title;
    private double pageRank;
    private List<String> linksOut;
    private List<String> linksIn;
    private Map<String, Double> tf;
    private Map<String, Double> tfIdf;

    public Page(String link, String title, ArrayList<String> linksOut, Map<String, Double> wordsRelFreq) {
        this.link = link;
        this.title = title;
        this.linksOut = linksOut;
        this.tf = wordsRelFreq;
        this.linksIn = new ArrayList<>();
        this.tfIdf = new HashMap<>();
    }

    public String getLink() {
        return link;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getLinksOut() {
        return linksOut;
    }

    public List<String> getLinksIn() {
        return linksIn;
    }

    public double getPageRank() {
        return pageRank;
    }

    public void setPageRank(double pageRank) {
        this.pageRank = pageRank;
    }

    public Map<String, Double> getTfIdf() {
        return tfIdf;
    }

    public void addTfIdf(String word, double tfIdf_value) {
        this.tfIdf.put(word, tfIdf_value);
    }

    public void addLinkIn(String link){
        this.linksIn.add(link);
    }

    public Map<String, Double> getTf() {
        return tf;
    }
}
