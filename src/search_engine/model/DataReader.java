package search_engine.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import static search_engine.model.Constants.*;

public class DataReader {

    /*
    reads all "title" files and returns a list of titles
     */
    static String[] readTitleFiles(List<String> urls){
        int urlSize = urls.size();
        String[] titles = new String[urlSize];
        String pathString;
        for (int i = 0; i < urlSize; i++){
            pathString = findUrlFolder(urls.get(i)) + File.separator + TITLE_FILE_NAME.getName();
            if (new File(pathString).exists()){
                try {
                    BufferedReader reader = new BufferedReader(new java.io.FileReader(pathString));
                    titles[i] = reader.readLine();
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        return titles;
    }

    /*
    reads content of files that have a several lines (i.e. incoming links, outgoing links)
     */
    static List<String> readFile(String pathString){
        Path path = FileSystems.getDefault().getPath(pathString);
        if (Files.isRegularFile(path)){
            try (BufferedReader reader = new BufferedReader(new FileReader(pathString))){
                return reader.lines().toList();
            } catch (IOException e){
                e.printStackTrace();
            }
        }
        return null;
    }

    /*
    reads content of files that have only one number (i.e. files saving page rank, idf, tf, tf_idf)
    returns a number
     */
    static double readFile(String pathString, String word){
        if (!word.equals(""))
            pathString += File.separator + word;
        Path path = FileSystems.getDefault().getPath(pathString);
        if (Files.isRegularFile(path)){
            try (BufferedReader reader = new BufferedReader(new FileReader(pathString))){
                return Double.parseDouble(reader.readLine());
            } catch (IOException e){
                e.printStackTrace();
            }
        }
        return -1.0;
    }

    /*
    converts a URL to a String that shows the corresponding directory for that URL in the data directory
    this method returns a String even if the URL isn't among the visisted URLs
     */
    static String findUrlFolder(String url){
        String[] folders = url.split(SLASH.getName());
        StringJoiner s = new StringJoiner(File.separator);
        s.add(BASE_DIR.getName());
        for (int i = 2; i < folders.length; i++)
            s.add(folders[i]);
        return s.toString();
    }

    static List<String> getOutgoingLinks(String url) {
        String pathString = findUrlFolder(url) + File.separator + URLS_OUT_FILE_NAME.getName();
        return readFile(pathString);
    }

    static List<String> getIncomingLinks(String url) {
        String pathString = findUrlFolder(url) + File.separator + URLS_IN_FILE_NAME.getName();
        return readFile(pathString);
    }

    static double getPageRank(String url) {
        String pathString = findUrlFolder(url) + File.separator + PAGE_RANK_FILE_NAME.getName();
        return readFile(pathString, "");
    }

    static double getIDF(String word) {
        String pathString = IDF_FOLDER_NAME.getName();
        double tf = readFile(pathString, word);
        return tf == -1 ? 0 : tf;
    }

    static double getTF(String url, String word) {
        String pathString = findUrlFolder(url) + File.separator + TF_FOLDER_NAME.getName();
        double tf = readFile(pathString, word);
        return tf == -1 ? 0 : tf;
    }

    static double getTFIDF(String url, String word) {
        String pathString = findUrlFolder(url) + File.separator + TF_IDF_FOLDER_NAME.getName();
        double tf = readFile(pathString, word);
        return tf == -1 ? 0 : tf;
    }

}
