package com.example.com.corpus.controllers;

import com.example.com.corpus.models.Couplet;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;


public class AddCoupletController {
    @FXML
    private TextField coupletline1;
    @FXML
    private TextField coupletline2;

    public Couplet processResults() {
        Couplet couplet=new Couplet();
        couplet.setLine1(coupletline1.getText());
        couplet.setLine2(coupletline2.getText());
        return couplet;
    }
}
