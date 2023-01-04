package fruits1.search_engine.model;

public class SearchResultImp implements SearchResult,Comparable<SearchResult>{
    String title;
    double score;

    public SearchResultImp(String title, double score) {
        this.title = title;
        this.score = score;
    }

    @Override
    public int compareTo(SearchResult o) {
        double roundedScore = Math.round(score * 1000.0) / 1000.0;
        double ORoundedScore = Math.round(o.getScore() * 1000.0) / 1000.0;
        if (roundedScore > ORoundedScore){
            return -1;
        } else if (roundedScore < ORoundedScore) {
            return 1;
        } else {
            return title.compareTo(o.getTitle());
        }
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public double getScore() {
        return score;
    }
}
