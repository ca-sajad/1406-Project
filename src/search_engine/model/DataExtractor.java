package search_engine.model;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static search_engine.model.Constants.*;

public class DataExtractor {

    /*
    finds absolute webpage addresses that the current webpage links to from its html
    returns an arraylist of found addresses
     */
    static ArrayList<String> extractLinks(String text, String baseUrl){
        ArrayList<String> links = new ArrayList<>();

        String s = HREF_START.getName() + "(.*?)" + QUOTATION.getName();
        Pattern p1 = Pattern.compile( s, Pattern.CASE_INSENSITIVE);
        Matcher m1 = p1.matcher(text);
        while (m1.find()){
            String link = text.substring(m1.start() + HREF_START.getName().length(),
                    m1.end() - QUOTATION.getName().length());
            String absoluteLink = link;
            if (link.charAt(0) == DOT.getName().charAt(0) & link.charAt(1) == SLASH.getName().charAt(0)){
                String relativeLink = link.substring(2);
                absoluteLink = baseUrl + relativeLink;
            }
            links.add(absoluteLink);
        }
        return links;
    }

    /*
    finds the words (fruits) between <p> / </p> tags in the webpage html
    returns an array of found words
     */
    static String[] extractWords(String text){
        String s = PARA_TAG_START.getName() + "(.*?)" + PARA_TAG_END.getName();
        Pattern p1 = Pattern.compile( s, Pattern.CASE_INSENSITIVE);
        Matcher m1 = p1.matcher(text.replace(NEW_LINE.getName(), " "));
        if (m1.find()) {
            String words = text.substring(m1.start() + PARA_TAG_START.getName().length() + 1,
                    m1.end() - PARA_TAG_END.getName().length() - 1);
            return words.split(NEW_LINE.getName());
        }
        return new String[0];
    }

    /*
    finds and returns the title of the webpage from its html (stored between <title> / </title> tags)
     */
    static String extractTitle(String text){
        int titleIndexStart = text.indexOf(TITLE_TAG_START.getName()) + TITLE_TAG_START.getName().length();
        int titleIndexEnd = text.indexOf(TITLE_TAG_END.getName());
        return text.substring(titleIndexStart, titleIndexEnd);
    }

}
