package com.example.com.corpus.data;

import com.example.com.corpus.models.Couplet;
import com.example.com.corpus.models.Poem;
import com.example.com.corpus.models.Root;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import java.sql.*;

public class DataBaseHandler {
    private static DataBaseHandler dataBaseHandler;
    private ObservableList<Poem> poemList;
    private ObservableList<Couplet> coupletlist;
    private ObservableList<Root> rootList;
    private Connection connection;

    private DataBaseHandler() {
        connectToDB();

    }
    private void connectToDB() {
        poemList= FXCollections.observableArrayList();
        coupletlist= FXCollections.observableArrayList();
        String url="jdbc:mysql://localhost:3306/ap_arabic_corpus?characterEncoding=UTF-8&useSSL=false&allowPublicKeyRetrieval=true";
        String user="root";
        String pass="root";
        try {
            connection= DriverManager.getConnection(url, user, pass);
            System.out.println("Connected to MySql");
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
    }
    public static DataBaseHandler getInstance(){
        if(dataBaseHandler==null) {
            dataBaseHandler = new DataBaseHandler();
        }
        return dataBaseHandler;
    }

    public ObservableList<Poem> getPoems(){

        String query="SELECT * FROM poem;";
        try {
            Statement statement=connection.createStatement();
            ResultSet resultSet=statement.executeQuery(query);
            while (resultSet.next()){
                Poem poem=new Poem();
                //poem.setID(resultSet.getInt("poemId"));
                poem.setTitle(resultSet.getString("poemTitle"));
                poem.setBook(resultSet.getString("bookName"));
                poem.setPoet(resultSet.getString("poetName"));
                poem.setID(resultSet.getInt("poemId"));
                if(poem != null) {
                    if(!poemList.contains(poem))
                    poemList.add(poem);
                }
            }
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
        return poemList;
    }
    public ObservableList<Couplet> getCouplets(int poemId){
        System.out.println(""+poemId);

        ObservableList<Couplet> couplets=FXCollections.observableArrayList();
        String query="SELECT * FROM couplet where poemId=?";
        try {
            PreparedStatement preparedStatement=connection.prepareStatement(query);
            preparedStatement.setInt(1, poemId);
            ResultSet resultSet=preparedStatement.executeQuery();
            while (resultSet.next()){
                Couplet couplet=new Couplet();
                couplet.setID(resultSet.getInt("coupletId"));
                couplet.setPoemID(resultSet.getInt("poemId"));
                couplet.setLine1(resultSet.getString("line1"));
                couplet.setLine2(resultSet.getString("line2"));
                if(couplet!=null){
                    couplets.add(couplet);
                }
            }
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
        return couplets;
    }

    public void insertPoem(Poem poem) {
        String query="INSERT INTO poem(poemTitle, bookName, poetName)" +
                "VALUES(?,?,?)";

        try {
            PreparedStatement preparedStatement=connection.prepareStatement(query);
            preparedStatement.setString(1,poem.getTitle());
            preparedStatement.setString(2,poem.getBook());
            preparedStatement.setString(3, poem.getPoet());
            preparedStatement.execute();
            System.out.println("Poem added");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println(throwables.getMessage()+throwables.getCause());
        }
    }

    public void insertCouplet(int id, Couplet couplet) {
        String query="INSERT INTO couplet(poemId, line1, line2)" +
                "VALUES(?, ?, ?)";
        try {
            PreparedStatement preparedStatement=connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, couplet.getLine1());
            preparedStatement.setString(3, couplet.getLine2());
            preparedStatement.execute();
            System.out.println("Couplet Inserted successfully");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void insertRoot(Root root) {
        String query="INSERT INTO root(rootLetters, inQuran) VALUES(?, ?);";
        try {
            PreparedStatement preparedStatement=connection.prepareStatement(query);
            preparedStatement.setString(1, root.getRoot());
            preparedStatement.setBoolean(2, root.isInQuran());
            preparedStatement.execute();
            System.out.println("Root Added Successfully");
        } catch (SQLException throwables) {
            System.out.println("Root not added: "+throwables.getCause());
        }
    }
    public ObservableList<Root> getRootList(){
        ObservableList<Root> roots=FXCollections.observableArrayList();
        String query="SELECT * FROM root";
        try {
            Statement statement=connection.createStatement();
            ResultSet resultSet=statement.executeQuery(query);
            while(resultSet.next()){
                Root root=new Root();
                root.setRoot(resultSet.getString("rootLetters"));
                root.setInQuran(resultSet.getBoolean("inQuran"));
                root.setId(resultSet.getInt("rootId"));
                System.out.println("root: "+root);
                if(root!=null){
                    System.out.println("root: "+root);
                    roots.add(root);
                }
            }
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage()+throwables.getCause());
        }

        return roots;
    }

    public void deletePoem(Poem poem) {
        String query="DELETE from poem where poemId=?;";
        try {
            PreparedStatement preparedStatement=connection.prepareStatement(query);
            preparedStatement.setInt(1,poem.getID());
            preparedStatement.executeUpdate();
            System.out.println("Deleted Successfully");
            poemList.remove(poem);
            deleteCouplet(poem);
        } catch (SQLException e) {
           System.out.println(e.getMessage()+e.getCause());
        }
    }

    private void deleteCouplet(Poem poem) {
        String query="DELETE FROM couplet where poemId=?;";
        PreparedStatement preparedStatement= null;
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, poem.getID());
            preparedStatement.executeUpdate();
            System.out.println("Couplet Deleted Successfully");
        } catch (SQLException e) {
            System.out.println(e.getMessage()+e.getCause());
        }
    }

    public void deleteSingleCouplet(int id) {
        String query="DELETE FROM couplet where coupletId=?;";
        PreparedStatement preparedStatement= null;
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            System.out.println("Couplet Deleted Successfully");
        } catch (SQLException e) {
            System.out.println(e.getMessage()+e.getCause());
        }
    }

    public void deleteRoot(Root root) {
        String query="DELETE FROM root where rootId=?;";
        PreparedStatement preparedStatement= null;
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, root.getId());
            preparedStatement.executeUpdate();
            System.out.println("Root Deleted Successfully");
        } catch (SQLException e) {
            System.out.println(e.getMessage()+e.getCause());
        }
    }

    public void updatePoem(Poem poem) {
        String query="UPDATE poem set poemTitle=?, bookName=?, poetName=? where poemId=?;";
        PreparedStatement preparedStatement= null;
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, poem.getTitle());
            preparedStatement.setString(2, poem.getBook());
            preparedStatement.setString(3, poem.getTitle());
            preparedStatement.setInt(4, poem.getID());
            preparedStatement.executeUpdate();
            System.out.println("Poem Updated Successfully");
        } catch (SQLException e) {
            System.out.println(e.getMessage()+e.getCause());
        }
    }

    public void updateCouplet(Couplet updatedCouplet) {
        String query="UPDATE couplet set line1=?, line2=? where coupletId=?;";
        PreparedStatement preparedStatement= null;
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, updatedCouplet.getLine1());
            preparedStatement.setString(2, updatedCouplet.getLine2());
            preparedStatement.setInt(3, updatedCouplet.getID());
            preparedStatement.executeUpdate();
            //System.out.println("Couplet Updated Successfully");
        } catch (SQLException e) {
            System.out.println(e.getMessage()+e.getCause());
        }
    }

    public void updateRoot(Root updatedRoot) {
        String query="UPDATE root set rootLetters=?, inQuran=? where rootId=?;";
        PreparedStatement preparedStatement= null;
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, updatedRoot.getRoot());
            preparedStatement.setBoolean(2, updatedRoot.isInQuran());
            preparedStatement.setInt(3, updatedRoot.getID());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage()+e.getCause());
        }
    }
}
