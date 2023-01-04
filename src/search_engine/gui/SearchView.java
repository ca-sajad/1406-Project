package search_engine.gui;

import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import javafx.scene.image.Image;
import search_engine.model.Crawler;
import search_engine.model.SearchResult;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static search_engine.model.Constants.*;
import static tinyfruits.search_engine.model.Constants.STYLESHEET;

public class SearchView extends Pane {
    private ListView<String> resultList;
    private Button  searchButton;
    private TextField searchField;
    private RadioButton prButton;

    public Button getSearchButton() {
        return searchButton;
    }

    public SearchView() {
        getStylesheets().add(STYLESHEET.getName());

        HBox parent = new HBox();
        getChildren().add(parent);

        resultList = new ListView<>();
        resultList.setPrefWidth(300);
        resultList.setItems(FXCollections.observableArrayList(makeList(null)));

        Label l1 = new Label("The Best Fruit Search Engine");
        l1.setPrefSize(500, 100);
        File file = new File(CHERRY_PIC.getName());
        Image cherry = new Image(file.toURI().toString());
        ImageView cherryView = new ImageView(cherry);
        l1.setGraphic(cherryView);

        searchField = new TextField();
        searchField.setPrefSize(330,30);
        prButton = new RadioButton("Use PageRank");
        searchButton = new Button("Search");
        searchButton.setPrefSize(100, 40);

        HBox hBox = new HBox();
        hBox.setSpacing(30);
        hBox.setPrefSize(500, 100);
        hBox.setAlignment(Pos.CENTER);
        hBox.getChildren().addAll(searchField, prButton);

        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setPrefSize(700, 500);
        vBox.setSpacing(30);
        vBox.getChildren().addAll(l1, hBox, searchButton);

        parent.getChildren().addAll(vBox, resultList);
        parent.setStyle("-fx-background-color: rgb(140, 185, 225)");
    }

    /*
    populates resultList
     */
    public void update(Crawler model){
        String query = searchField.getText();
        Pattern p = Pattern.compile(".*\\d.*");
        Matcher m = p.matcher(query);
        if (!query.equals("") && !m.matches()) {
            boolean boost = prButton.isSelected();
            List<SearchResult> results = model.search(query, boost, Integer.parseInt(TOP_SIMILARITIES.getName()));
            resultList.setItems(FXCollections.observableArrayList(makeList(results)));
        } else {
            resultList.setItems(FXCollections.observableArrayList(makeList(null)));
        }
    }

    /*
    formats resultList
     */
    private List<String> makeList(List<SearchResult> results){
        List<String> output = new ArrayList<>();
        output.add("  Page Title\t\t\tPage Score");
        output.add("");
        if (results != null) {
            int space;
            String title;
            double score;
            for (SearchResult result : results) {
                title = result.getTitle();
                score = result.getScore();
                space = title.length() == 3 ? 35 : title.length() == 4 ? 34 : 33;
                String s = String.format("\t%-5s%" + space + ".5f", title, score);
                output.add(s);
            }
        }
        return output;
    }
}
