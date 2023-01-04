package search_engine.model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import static search_engine.model.Constants.*;

public class DataWriter {
    /*
    calculates and writes idf data of all the words in the visited webpages to a directory called "IDF" in
    the first layer of directory hierarchy. "IDF" stores one text file for each word.
    e.g. orange.txt saves idf value for the word "orange"
     */
    static boolean writeIdfData(Map<String, Integer> wordsCount, int size){
        try {
            Path path = FileSystems.getDefault().getPath(IDF_FOLDER_NAME.getName());
            if (!(new File(IDF_FOLDER_NAME.getName()).exists()))
                Files.createDirectory(path);
            double idf;
            for (Map.Entry<String, Integer> entry : wordsCount.entrySet()){
                try(PrintWriter outputWriter = new PrintWriter(new FileWriter(IDF_FOLDER_NAME.getName()
                        + File.separator + entry.getKey()))){
                    idf = Math.log((double) size / (1 + entry.getValue())) / Math.log(2);
                    outputWriter.print(idf);
                }
            }
            return true;
        } catch (IOException e){
            return false;
        }
    }

    /*
    writes attributes of each Page object to a file in a directory named after that page (e.g. N-1.html).
    Each directory contains
    (1) a "TF" directory which stores tf values for each word in that webpage in a text
    file (e.g. /N-1.html/TF/orange.txt is the address of text file that stores tf value of orange)
    (2) a "TF_IDF" directory which stores tf_idf values for each word in that webpage in a text
    file (e.g. /N-1.html/TF_IDF/orange.txt is the address of text file that stores tf_idf value of orange)
    (3) an "incoming  links.txt" file storing the addresses of webpages with a link to the current webpage
    (4) an "outgoing  links.txt" file storing the addresses of webpages that the current webpage has a link to
    (5) a "page_rank.txt" file storing the page rank calculated for the current webpage
    (6) a "title.txt" file storing the webpage title
     */
    static boolean writePageData(Map<String, Page> pages){
        for (Map.Entry<String, Page> pageEntry : pages.entrySet()){
            Page page = pageEntry.getValue();

            String[] folders = pageEntry.getKey().split(SLASH.getName());
            StringJoiner s = new StringJoiner(File.separator);
            s.add(BASE_DIR.getName());
            try {
                for (int i = 2; i < folders.length; i++){
                    s.add(folders[i]);
                    Path path = FileSystems.getDefault().getPath(s.toString());
                    if (!Files.exists(path))
                        Files.createDirectory(path);
                }
                page.writePage(s);
            } catch (IOException e){
                return false;
            }
        }
        return true;
    }

    /*
    writes all the visited URL addresses to the file "URLs.txt" in the first layer of directory hierarchy.
    This file is read during the search.
     */
    static boolean writeUrls(List<String> urls){
        try (PrintWriter outputWriter = new PrintWriter(new FileWriter(URLS_FILE_NAME.getName()))){
            for (String url: urls){
                outputWriter.println(url);
                outputWriter.flush();
            }
            return true;
        } catch (IOException e){
            return false;
        }
    }


}
