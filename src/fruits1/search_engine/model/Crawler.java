package fruits1.search_engine.model;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

import static fruits1.search_engine.model.Constants.SLASH;

public class Crawler implements ProjectTester {
    private Map<String, Page> pages;
    private Queue<String> queueUrls;
    private List<String> processedUrls;
    private Map<String, Integer> wordsCountInPage;          // key: word, value: occurrences of the word per page
    private Map<String, Integer> wordsCountTotal;           // key: word, value: number of pages with the word
    private Map<String, Double> wordsRelFreqInPage;         // key: URL, value:  tf

    public Crawler() {
        this.pages = new HashMap<>();
        this.queueUrls = new LinkedBlockingQueue<>();
        this.processedUrls = new ArrayList<>();
        this.wordsCountInPage = new HashMap<>();
        this.wordsCountTotal = new HashMap<>();
        this.wordsRelFreqInPage = new HashMap<>();
    }

    @Override
    public void initialize() {
        Initializer.initialize();
    }

    @Override
    public void crawl(String seedUrl) {
        String url, baseUrl, pageContent, title;
        String[] urlList, words;
        ArrayList<String> links;
        queueUrls.add(seedUrl);
        while ((url = queueUrls.poll()) != null){
//            find base URL
            urlList = url.split(SLASH.getName());
            int urlNameIndex = url.indexOf(urlList[urlList.length - 1]);
            baseUrl = url.substring(0, urlNameIndex);
            try {
//              reading webpage html
                pageContent = WebRequester.readURL(url);
                title = DataExtractor.extractTitle(pageContent);
                words = DataExtractor.extractWords(pageContent);
                links = new ArrayList<>(DataExtractor.extractLinks(pageContent, baseUrl));
                queueUrls.addAll(links.stream().filter(link -> !queueUrls.contains(link))
                        .filter(link -> !processedUrls.contains(link)).toList());
                wordsCountInPage = DataCalculator.calculateWordCount(words);
                DataCalculator.updateWordsCount(wordsCountTotal, wordsCountInPage);
                wordsRelFreqInPage = DataCalculator.calculateTfInUrl(wordsCountInPage);
                processedUrls.add(url);

                pages.put(url, new Page(url, title, links, wordsRelFreqInPage));
            } catch(IOException e){
                System.out.println("Something is wrong with reading the webpage");
                return;
            }
        }

        DataCalculator.findIncomingLinks(pages);
        DataWriter.writeIdfData(wordsCountTotal, processedUrls.size());
        DataCalculator.calculateTfIdf(pages);
        DataCalculator.calculatePageRank(pages);
        DataWriter.writePageData(pages);
        DataWriter.writeUrls(processedUrls);
    }

    @Override
    public List<String> getOutgoingLinks(String url) {
        return DataReader.getOutgoingLinks(url);
    }

    @Override
    public List<String> getIncomingLinks(String url) {
        return DataReader.getIncomingLinks(url);
    }

    @Override
    public double getPageRank(String url) {
        return DataReader.getPageRank(url);
    }

    @Override
    public double getIDF(String word) {
        return DataReader.getIDF(word);
    }

    @Override
    public double getTF(String url, String word) {
        return DataReader.getTF(url, word);
    }

    @Override
    public double getTFIDF(String url, String word) {
        return DataReader.getTFIDF(url, word);
    }

    @Override
    public List<SearchResult> search(String query, boolean boost, int X) {
        FruitSearch fruitSearch = new FruitSearch();
        return fruitSearch.search(query, boost, X);
    }
}
