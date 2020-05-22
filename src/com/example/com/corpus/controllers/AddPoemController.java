package com.example.com.corpus.controllers;

import com.example.com.corpus.models.Poem;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class AddPoemController {

    @FXML
    private TextField title;
    @FXML
    private TextField poet;
    @FXML
    private TextField book;
    @FXML
    public Poem processResults(){
        System.out.println("process data");
        Poem poem=new Poem();
        poem.setTitle(title.getText());
        poem.setPoet(poet.getText());
        poem.setBook(book.getText());
        return poem;
    }

    public void setData(Poem poem) {
        title.setText(poem.getTitle());
        book.setText(poem.getBook());
        poet.setText(poem.getPoet());
    }

    public Poem updatedPoem() {
        Poem poem=new Poem();
        poem.setTitle(title.getText());
        poem.setPoet(poet.getText());
        poem.setBook(book.getText());
        return poem;
    }
}
