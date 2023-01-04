package search_engine.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import search_engine.model.Crawler;


public class SearchApp extends Application {
    Crawler model;

    public SearchApp() {
        this.model = new Crawler();
    }

    public void start(Stage primaryStage){

        SearchView view = new SearchView();

        Pane aPane = new Pane();
        aPane.getChildren().add(view);

        primaryStage.setTitle("Searching for Your Favorite Fruit?");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(aPane, 1000, 500));
        primaryStage.show();

        view.getSearchButton().setOnAction(actionEvent -> view.update(model));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
