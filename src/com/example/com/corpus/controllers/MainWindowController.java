package com.example.com.corpus.controllers;
import com.example.com.corpus.data.DataBaseHandler;
import com.example.com.corpus.models.Couplet;
import com.example.com.corpus.models.Poem;
import com.example.com.corpus.models.Root;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;


import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainWindowController implements Initializable {

    @FXML
    private ListView left_list_view;
    @FXML
    private Label title;
    @FXML
    private Label book;
    @FXML
    private Label poet;
    @FXML
    private BorderPane mainBorderPane;

    @FXML
    private TableView<Couplet> coupletTableView;
    @FXML
    private TableColumn<Couplet, String> line1;
    @FXML
    private TableColumn<Couplet, String> line2;

    //root table
    @FXML
    private TableView<Root> rootTableView;
    @FXML
    private TableColumn rootLetters;
    @FXML
    private TableColumn<Root, Boolean> inQuran;


    @FXML
    private TableView poemTable;
    @FXML
    private TableColumn<Poem, String> title_column;
    @FXML
    private TableColumn<Poem, String> book_column;
    @FXML
    private TableColumn<Poem, String> poet_column;


    ObservableList<Poem> poems;
    ObservableList<Root> roots;
    ObservableList<Couplet> couplets;

    //context menu
    @FXML
    private ContextMenu contextMenu;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        poems=FXCollections.observableArrayList();
        couplets=FXCollections.observableArrayList();
        roots=FXCollections.observableArrayList();
        loadPoems();
        loadRoots();
    }

    @FXML
    public void showAddNewPoemDialog(ActionEvent event){
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/com/example/com/corpus/views/add_poem.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch(IOException e) {
            System.out.println("Couldn't load the dialog");
            e.printStackTrace();
            return;
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {
            AddPoemController controller = fxmlLoader.getController();
            Poem poem=controller.processResults();
            DataBaseHandler.getInstance().insertPoem(poem);
            poems.add(poem);
            System.out.println("Poem Inserted Successfully");
        } else {
            System.out.println("Cancel pressed");
        }
    }
    @FXML
    public void showAddNewCoupletDialog(ActionEvent event){
        Poem selectedPoem= (Poem) poemTable.getSelectionModel().getSelectedItem();
        if(selectedPoem==null){
            System.out.println("No Poem selected.");
        }else {
            System.out.println("poem id: "+selectedPoem.getID());
            Dialog<ButtonType> dialog=new Dialog<>();

            FXMLLoader fxmlLoader=new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/com/example/com/corpus/views/add_couplet.fxml"));

            dialog.initOwner(mainBorderPane.getScene().getWindow());
            try {
                dialog.getDialogPane().setContent(fxmlLoader.load());
            } catch (IOException e) {
                e.printStackTrace();
            }
            dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
            dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

            Optional<ButtonType> result = dialog.showAndWait();
            if(result.isPresent() && result.get() == ButtonType.OK) {
                AddCoupletController controller = fxmlLoader.getController();
                Couplet couplet=controller.processResults();
                DataBaseHandler.getInstance().insertCouplet(selectedPoem.getID(), couplet);
                couplets.add(couplet);
                System.out.println("Data Inserted Successfully");
            } else {
                System.out.println("Cancel pressed");
            }
        }
    }
    @FXML
    public void showAddNewRootDialog(ActionEvent event){
        Dialog<ButtonType> dialog=new Dialog<>();
        FXMLLoader fxmlLoader=new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/com/example/com/corpus/views/add_root.fxml"));
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {
            AddRootController controller = fxmlLoader.getController();
            Root root=controller.processResults();
            DataBaseHandler.getInstance().insertRoot(root);
            roots.add(root);
            System.out.println("Data Inserted Successfully");
        } else {
            System.out.println("Cancel pressed");
        }
    }



    private void loadRoots() {
        roots=DataBaseHandler.getInstance().getRootList();
        rootTableView.setItems(roots);
        rootLetters.setCellValueFactory(new PropertyValueFactory<>("root"));
        inQuran.setCellValueFactory(new PropertyValueFactory<>("inQuran"));

        ContextMenu contextMenu=new ContextMenu();
        MenuItem delMenuItem=new MenuItem("Delete");
        MenuItem updateMenuItem=new MenuItem("Update");
        contextMenu.getItems().addAll(delMenuItem, updateMenuItem);
        rootTableView.setContextMenu(contextMenu);
        delMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Root root=new Root();
                root=rootTableView.getSelectionModel().getSelectedItem();
                DataBaseHandler.getInstance().deleteRoot(root);
                roots.remove(root);
            }
        });
        updateMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Update");
            }
        });


    }

    private void loadPoems() {
        poems=DataBaseHandler.getInstance().getPoems();
        contextMenu=new ContextMenu();
        MenuItem  delMenuItem=new MenuItem("Delete");
        MenuItem  updateMenuItem=new MenuItem("Update");
        contextMenu.getItems().addAll(delMenuItem, updateMenuItem);
        poemTable.setContextMenu(contextMenu);
        delMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                    //System.out.println("Del pressed");
                    Poem poem=new Poem();
                    poem= (Poem) poemTable.getSelectionModel().getSelectedItem();
                    //System.out.println(poem);
                    DataBaseHandler.getInstance().deletePoem(poem);
            }
        });
        updateMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
               // System.out.println("update pressed");
                Poem poem=new Poem();
                poem= (Poem) poemTable.getSelectionModel().getSelectedItem();
                FXMLLoader fxmlLoader=new FXMLLoader();
                Dialog<ButtonType> dialog=new Dialog<>();
                fxmlLoader.setLocation(getClass().getResource("/com/example/com/corpus/views/add_poem.fxml"));
                try {
                    dialog.getDialogPane().setContent(fxmlLoader.load());
                } catch(IOException e) {
                    System.out.println("Couldn't load the dialog");
                    e.printStackTrace();
                    return;
                }

                dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
                dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

                Optional<ButtonType> result = dialog.showAndWait();
                if(result.isPresent() && result.get() == ButtonType.OK) {
                    AddPoemController controller = fxmlLoader.getController();
                    controller.setData(poem);
                    //DataBaseHandler.getInstance().insertPoem(poem);
                    //poems.add(poem);
                    System.out.println("Poem Inserted Successfully");
                } else {
                    System.out.println("Cancel pressed");
                }

            }
        });

        poemTable.setItems(poems);
        title_column.setCellValueFactory(new PropertyValueFactory<Poem, String>("title"));
        book_column.setCellValueFactory(new PropertyValueFactory<Poem, String>("book"));
        poet_column.setCellValueFactory(new PropertyValueFactory<Poem, String>("poet"));
        poemTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        Poem poem= (Poem) poemTable.getSelectionModel().getSelectedItem();

        System.out.println("Selected: "+poem);
        if (poem!=null)
        loadCouplets(poem.getID());
        poemTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                Poem poem= (Poem) poemTable.getSelectionModel().getSelectedItem();
                title_column.setCellValueFactory(new PropertyValueFactory<Poem, String>("title"));
                book_column.setCellValueFactory(new PropertyValueFactory<Poem, String>("book"));
                poet_column.setCellValueFactory(new PropertyValueFactory<Poem, String>("poet"));
                loadCouplets(poem.getID());
            }
        });
    }

    private void loadCouplets(int id) {
        coupletTableView.getItems().clear();
        couplets=DataBaseHandler.getInstance().getCouplets(id);
        coupletTableView.setItems(couplets);
        line1.setCellValueFactory(new PropertyValueFactory<Couplet, String>("line1"));
        line2.setCellValueFactory(new PropertyValueFactory<Couplet, String>("line2"));

        ContextMenu contextMenu=new ContextMenu();
        MenuItem delMenuItem=new MenuItem("Delete");
        MenuItem updateMenuItem=new MenuItem("Update");
        contextMenu.getItems().addAll(delMenuItem, updateMenuItem);
        coupletTableView.setContextMenu(contextMenu);
        delMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Couplet couplet=coupletTableView.getSelectionModel().getSelectedItem();
                DataBaseHandler.getInstance().deleteSingleCouplet(couplet.getID());
                couplets.remove(couplet);
                System.out.println("Del Pressed");
            }
        });
        updateMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Update Pressed");
            }
        });

    }
}
