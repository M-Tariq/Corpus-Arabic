package com.example.com.corpus;

import com.example.com.corpus.data.DataBaseHandler;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        try {
            Parent root = FXMLLoader.load(getClass().getResource("views/main_window.fxml"));
            primaryStage.setTitle("Corpus-Arabic");
            primaryStage.setScene(new Scene(root, 900, 500));
            primaryStage.show();
        }catch (Exception e){
            System.out.println(e.getMessage()+"\n"+e.getCause());
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() throws Exception {
        DataBaseHandler dataBaseHandler=DataBaseHandler.getInstance();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }
}
