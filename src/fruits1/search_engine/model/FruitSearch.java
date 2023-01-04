package fruits1.search_engine.model;

import java.util.*;

import static fruits1.search_engine.model.Constants.*;


public class FruitSearch {

    public List<SearchResult> search(String query, boolean boost, int X){
        List<String> urls = DataReader.readFile(URLS_FILE_NAME.getName());
        double[] sim = calSimVector(urls, query, boost);
        String[] titles = DataReader.readTitleFiles(urls);
        int urlSize = urls.size();
        // returns the first X URLs as a SearchResult object
        // if two or more URLs have the same similarity index (up to 3 decimal points), the results
        // are returned alphabetically
        Map<String, Double> titleSimMap = new HashMap<>();
        List<SearchResultImp> results = new ArrayList<>();
        for (int i = 0; i < urlSize; i++){
            titleSimMap.put(titles[i], sim[i]);
        }

        for (String title : titles)
            results.add(new SearchResultImp(title, titleSimMap.get(title)));
        Collections.sort(results);
        List<SearchResult> list = new ArrayList<>(results);
        return list.subList(0, X);

//        List<SearchResult> results = new ArrayList<>();
//        int counter = 0;
//        while(counter < Math.min(X, urlSize)) {
//            double maxSim = Arrays.stream(sim).summaryStatistics().getMax();
//            Map<String, Double> maxMap = new TreeMap<>();
//            for (int j = 0; j < urlSize; j++) {
//                if (Math.round(sim[j] * 1000.0) / 1000.0 == Math.round(maxSim * 1000.0) / 1000.0) {
//                    maxMap.put(titles[j], sim[j]);
//                    sim[j] = -1;
//                }
//            }
//
//            for (Map.Entry<String, Double> entry : maxMap.entrySet()) {
//                if (counter < Math.min(X, urlSize)) {
//                    counter++;
//                    results.add(new SearchResult() {
//                        @Override
//                        public String getTitle() {
//                            return entry.getKey();
//                        }
//
//                        @Override
//                        public double getScore() {
//                            return entry.getValue();
//                        }
//                    });
//                }
//            }
//        }
//        return results;
    }

    /*
     finds and returns the list of unique words in the query
     */
    private List<String> findQueryUnique(String[] queryList){
        return new ArrayList<>(Set.copyOf(Arrays.asList(queryList)));
    }

    /*
    returns number of unique words in the query
     */
    private int calQueryUniqueSize(List<String> queryUnique){
        return queryUnique.size();
    }

    /*
     calculates tf_idf value for each word in the query
     return an array of tf_idf values
     */
    private double[] calQueryVector(String[] queryList){
        int querySize = queryList.length;
        List<String> queryUnique = findQueryUnique(queryList);
        int queryUniqueSize = calQueryUniqueSize(queryUnique);
        double[] queryVector = new double[queryUniqueSize];
        for (int j = 0; j < queryUniqueSize; j++){
            String word = queryUnique.get(j);
            double idf = DataReader.getIDF(word);
            long word_counter = Arrays.stream(queryList).filter(el -> Objects.equals(el, word)).count();
            double tf = (double) word_counter / querySize;
            queryVector[j] = Math.log(1 + tf) / Math.log(2) * idf;
        }
        return queryVector;
    }

    /*
    calculates similarity index for each URL
    returns an array of similarity indices
     */
    private double[] calSimVector(List<String> urls, String query, boolean boost){
        // if boost = true : reads page ranks from saved Page objects, otherwise page ranks = 1
        int urlSize = urls.size();
        double[] pageRanks = new double[urlSize];
        Arrays.fill(pageRanks, 1);
        if (boost) {
            for (int i = 0; i < pageRanks.length; i++)
                pageRanks[i] = DataReader.getPageRank(urls.get(i));
        }
        // calculates tf_idf array of the query
        String[] queryList = query.split(SPACE.getName());
        List<String> queryUnique = findQueryUnique(queryList);
        int queryUniqueSize = calQueryUniqueSize(queryUnique);
        double[] queryVector = calQueryVector(queryList);
        // reads tf_idf values and put them into an array
        double[][] vectors = new double[urlSize][queryUniqueSize];
        for (int i = 0; i < urlSize; i++){
            for (int j = 0; j < queryUniqueSize; j++){
                vectors[i][j] = DataReader.getTFIDF(urls.get(i), queryUnique.get(j));
            }
        }
        // calculates similarity index for each URL
        double[] sim = new double[urlSize];
        double[] numer = new double[urlSize];
        double[] denomLeft = new double[urlSize];
        double[] denomRight = new double[urlSize];
        double denom;
        for (int i = 0; i < urlSize; i++){
            for (int j = 0; j < queryUniqueSize; j++) {
                numer[i] += queryVector[j] * vectors[i][j];
                denomLeft[i] += queryVector[j] * queryVector[j];
                denomRight[i] += vectors[i][j] * vectors[i][j];
            }
            denom = Math.sqrt(denomLeft[i] * denomRight[i]);
            if (denom == 0){
                sim[i] = 0;
            } else {
                sim[i] = numer[i] / denom * pageRanks[i];
            }
        }
        return sim;
    }

}
