package com.example.com.corpus.utill;

import javafx.scene.control.Alert;

public class AlertDialog {

    private static AlertDialog instance=new AlertDialog();

    private AlertDialog() {
    }
    public static AlertDialog getInstance(){
        if(instance==null){
            instance=new AlertDialog();
        }
        return instance;
    }
    public void showConfirmationDialog(String message){
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Corpus-Arabic ~ Alert Dialog");
        alert.setHeaderText(message);
        alert.show();
    }
}
