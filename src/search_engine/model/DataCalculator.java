package search_engine.model;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

import static search_engine.model.Constants.*;


public class DataCalculator {

    /*
    counts number of occurrences of each word in the webpage based on the array of words in the webpage
    returns a map where key: word in the webpage, value: number of occurrences of the word
     */
    static Map<String, Integer> calculateWordCount(String[] words){
        Map<String, Integer> wordsCount = new HashMap<>();
        Arrays.stream(words).iterator().forEachRemaining(
                word -> wordsCount.put(word, wordsCount.getOrDefault(word, 0) + 1));
        return wordsCount;
    }

    /*
    updates a map that counts occurrences of each word read in the webpages based on the word presence in the
    current webpage (if the word is present in the current webpage, 1 is added to the counter, regardless of
    how many times it's been repeated in the current webpage)
    returns a map where key: a word read in one or more of the visited webpages, value: updated number of webpages
    with the word
     */
    static void updateWordsCount(Map<String, Integer> totalCount, Map<String, Integer> wordsInUrl){
        for (Map.Entry<String, Integer> urlEntry : wordsInUrl.entrySet()){
            String key = urlEntry.getKey();
            if (totalCount.containsKey(key)){
                totalCount.put(key, totalCount.get(key) + 1);
            } else {
                totalCount.put(key, 1);
            }
        }
    }

    /*
   calculates tf value for each word in the current webpage based on the number of occurrences of that word
   returns a map where key: word in the webpage, value: word frequency (tf) in the webpage
    */
    static Map<String, Double> calculateTfInUrl(Map<String, Integer> wordsCount){
        int size = wordsCount.values().stream().mapToInt(value -> value).sum();
        return wordsCount.entrySet().stream().collect(Collectors.toMap(
                Map.Entry::getKey, word -> (double) (word.getValue()) / size));
    }

    /*
    finds incoming links to a webpage based on other webpages outgoing links
    adds found incoming links to the Page object linksIn attribute
     */
    static void findIncomingLinks(Map<String, Page> pages){
        pages.forEach((key, value) ->
                value.getLinksOut().forEach(link -> pages.get(link).addLinkIn(key)));
    }

    /*
    calculates tf_idf value for each page and saves it in the page tfIdf attribute (a map with word as the key and
    tf_idf as the value).
     */
    static void calculateTfIdf(Map<String, Page> pages){
        for (Page page : pages.values()){
            Map<String, Double> tf = page.getTf();
            for (Map.Entry<String, Double> entry : tf.entrySet()){
                String word = entry.getKey();
                double idf = DataReader.getIDF(word);
                double tfIDF = Math.log(1 + tf.get(word)) / Math.log(2) * idf;
                page.addTfIdf(word, tfIDF);
            }
        }
    }

    /*
    calculates each page's rank and saves it in the pageRank attribute of each page object.
     */
    static void calculatePageRank(Map<String, Page> pages){
        int size = pages.size();
        double[][] adjMatrix = new double[size][size];
        List<String> urls = new ArrayList<>(pages.keySet().stream().toList());
        // creating adjacency matrix
        int i0 = 0;
        for (Page page : pages.values()){
            for (String outLink : page.getLinksOut()){
                int j = urls.indexOf(outLink);
                adjMatrix[i0][j] = 1.0;
            }
            i0++;
        }
        double[] sumRows = Arrays.stream(adjMatrix).mapToDouble(arr -> DoubleStream.of(arr).sum()).toArray();
        double alpha = Double.parseDouble(ALPHA.getName());
        for (int i = 0; i < adjMatrix.length; i++){
            for (int j = 0; j < adjMatrix[i].length; j++){
                adjMatrix[i][j] = adjMatrix[i][j] / sumRows[i] * (1 - alpha) + alpha / size;
            }
        }
        // calculate array of page ranks
        double[] arr1 = new double[size];
        Arrays.fill(arr1, (double) 1 / size);
        double error = 1.0;
        double threshold = Double.parseDouble(THRESHOLD.getName());
        while (error > threshold){
            double[] finalArray = arr1;
            double[] arr2 = matrixMult(arr1, adjMatrix);
            error = Math.sqrt(IntStream.range(0, arr2.length).mapToDouble(i -> Math.pow(arr2[i] - finalArray[i], 2)).sum());
            arr1 = arr2;
        }
        // assign page rank to each page
        for (int i = 0; i < urls.size(); i++){
            String url = urls.get(i);
            pages.get(url).setPageRank(arr1[i]);
        }
    }

    /*
    calculates matrix multiplication of an array and a matrix
     */
    private static double[] matrixMult(double[] arr, double[][] matrix){
        double[] temp = new double[matrix[0].length];
        for (int i = 0; i < matrix[0].length; i++){
            for (int j = 0; j < matrix[0].length; j++){
                temp[i] += matrix[j][i] * arr[j];
            }
        }
        return temp;
    }

}
