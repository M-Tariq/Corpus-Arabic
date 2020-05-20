package com.example.com.corpus.controllers;

import com.example.com.corpus.models.Root;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;


public class AddRootController {
    @FXML
    private TextField rootTv;
    @FXML
    private CheckBox inQuranCheckBox;

    public Root processResults() {
        Root root=new Root();
        root.setRoot(rootTv.getText());
        root.setInQuran(inQuranCheckBox.isSelected());
        return root;
    }

}
