package search_engine.model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static search_engine.model.Constants.*;

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

    public void writePage(StringJoiner s) throws IOException{
        PrintWriter outputWriter;

        String pathTitle = s + File.separator + TITLE_FILE_NAME.getName();
        outputWriter = new PrintWriter(new FileWriter(pathTitle));
        outputWriter.write(getTitle());
        outputWriter.close();

        String pathUrlsIn = s + File.separator + URLS_IN_FILE_NAME.getName();
        outputWriter = new PrintWriter(new FileWriter(pathUrlsIn));
        for (String url : getLinksIn()) {
            outputWriter.println(url);
        }
        outputWriter.close();

        String pathUrlsOut = s + File.separator + URLS_OUT_FILE_NAME.getName();
        outputWriter = new PrintWriter(new FileWriter(pathUrlsOut));
        for (String url : getLinksOut()) {
            outputWriter.println(url);
        }
        outputWriter.close();

        String pageRank = s + File.separator + PAGE_RANK_FILE_NAME.getName();
        outputWriter = new PrintWriter(new FileWriter(pageRank));
        outputWriter.print(getPageRank());
        outputWriter.close();


        String pathStr = s + File.separator + TF_FOLDER_NAME.getName();
        Path path = FileSystems.getDefault().getPath(pathStr);
        if (!Files.exists(path))
            Files.createDirectory(path);
        for (Map.Entry<String, Double> entry : getTf().entrySet()) {
            outputWriter = new PrintWriter(new FileWriter(pathStr + File.separator + entry.getKey()));
            outputWriter.print(entry.getValue());
            outputWriter.flush();
        }
        outputWriter.close();

        pathStr = s + File.separator + TF_IDF_FOLDER_NAME.getName();
        path = FileSystems.getDefault().getPath(pathStr);
        if (!Files.exists(path))
            Files.createDirectory(path);
        for (Map.Entry<String, Double> entry : getTfIdf().entrySet()) {
            outputWriter = new PrintWriter(new FileWriter(pathStr + File.separator + entry.getKey()));
            outputWriter.print(entry.getValue());
            outputWriter.flush();
        }
        outputWriter.close();
    }
}
