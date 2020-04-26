package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import DB.RicercaPerContrastoEllitticitaQuery;
import Entity.Filamento;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class RicercaPerContrastoEllitticitaController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Hyperlink backBtn;

    @FXML
    private TextField tfBrillanza;

    @FXML
    private TextField minTf;

    @FXML
    private TextField maxTf;

    @FXML
    private Button researchBtn;

    @FXML
    private Button nextBtn;

    @FXML
    private TableView<Filamento> filamentiTab;

    @FXML
    private TableColumn<Filamento, Integer> filColumn;

    @FXML
    private TextField tfSatellite;

    public RicercaPerContrastoEllitticitaController() throws IOException {
    }

    @FXML
    void doBackBtn(ActionEvent event) {
        try {
            LoginController l = new LoginController();
            String searcha = "admin";
            String searchu = "user";
            if (l.getRoleString().equalsIgnoreCase(searcha)) {
                Parent anchorParent = FXMLLoader.load((getClass().getResource("/view/HomeAmministratore.fxml")));
                Scene login_scene = new Scene(anchorParent);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

                window.setScene(login_scene);
                window.show();
            }
            else if (l.getRoleString().equalsIgnoreCase(searchu))    {
                Parent anchorParent = FXMLLoader.load((getClass().getResource("/view/HomeUtente.fxml")));
                Scene login_scene = new Scene(anchorParent);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

                window.setScene(login_scene);
                window.show();
            }
        }catch(IOException e)   {
            e.printStackTrace();
            System.out.println("Errore nel caricamento del file .fxml");
        }
    }


    private int rowsPerPage = 20;
    RicercaPerContrastoEllitticitaQuery ricercaPerContrastoElliticitaQuery = new RicercaPerContrastoEllitticitaQuery();


    @FXML
    public void doResearchBtn(ActionEvent event) throws SQLException {
        if(!controlloDati()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Impossibile Procedere");
            alert.setContentText("Inserire tutti i dati!");
            alert.showAndWait();
        }
        else    {
            Double brillanza= Double.parseDouble(tfBrillanza.getText());
            double min = Double.parseDouble(minTf.getText());
            double max = Double.parseDouble(maxTf.getText());
            String satellite = tfSatellite.getText();

            if (brillanza < 0 || min > max) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setHeaderText("Impossibile Procedere");
                alert.setContentText("Devi inserire un valore della brillanza maggiore di zero o un valore corretto di minimo e massimo!");
                alert.showAndWait();
            } else {
                ArrayList<Filamento> fil = ricercaPerContrastoElliticitaQuery.getNumerofilamenti(brillanza, min, max, satellite);
                if (fil == null) {
                    System.out.println("Ho un filamento nullo!");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Dialog");
                    alert.setHeaderText("Nessun filamento trovato!");
                    alert.setContentText("Nessun filamento trovato corrispondente alla ricerca effettuata!");
                    alert.showAndWait();
                    return;
                }
                ObservableList<Filamento> list = FXCollections.observableArrayList(fil);
                //creo una lista filtrata basata sulla observablelist in modo da limitarne le entry
                FilteredList<Filamento> filteredList = new FilteredList<Filamento>(list, filamento -> fil.indexOf(filamento) < rowsPerPage);
                filamentiTab.setItems(filteredList);
            }
        }
    }


    @FXML
    void doNextBtn(ActionEvent event)   throws SQLException {
        Double brillanza= Double.parseDouble(tfBrillanza.getText());
        double min = Double.parseDouble(minTf.getText());
        double max = Double.parseDouble(maxTf.getText());
        String satellite = tfSatellite.getText();

        ArrayList<Filamento> fil = ricercaPerContrastoElliticitaQuery.getNumerofilamenti(brillanza, min, max, satellite);
        if (fil == null) {
            System.out.println("Ho un filamento nullo!");
            return;
        }
        ObservableList<Filamento> list = FXCollections.observableArrayList(fil.subList(rowsPerPage,fil.size()));
        rowsPerPage = rowsPerPage + 20;
        //creo una lista filtrata basata sulla observablelist in modo da limitarne le entry
        FilteredList<Filamento> filteredList = new FilteredList<Filamento>(list, filamento -> fil.indexOf(filamento) < rowsPerPage);
    }

    public boolean controlloDati() {

        if (tfBrillanza.getText().equalsIgnoreCase("")) {
            return false;
        }
        if (minTf.getText().equalsIgnoreCase("")) {
            return false;
        }
        if (maxTf.getText().equalsIgnoreCase("")) {
            return false;
        }
        if (tfSatellite.getText().equalsIgnoreCase("")) {
            return false;
        }
        return true;
    }

    @FXML
    void initialize() {
        filamentiTab.setEditable(true);
        filColumn.setCellValueFactory(new PropertyValueFactory<>("idfil"));
        assert backBtn != null : "fx:id=\"backBtn\" was not injected: check your FXML file 'RicercaPerContrastoEllitticita.fxml'.";
        assert tfBrillanza != null : "fx:id=\"tfBrillantezza\" was not injected: check your FXML file 'RicercaPerContrastoEllitticita.fxml'.";
        assert minTf != null : "fx:id=\"minTf\" was not injected: check your FXML file 'RicercaPerContrastoEllitticita.fxml'.";
        assert maxTf != null : "fx:id=\"maxTf\" was not injected: check your FXML file 'RicercaPerContrastoEllitticita.fxml'.";
        assert researchBtn != null : "fx:id=\"researchBtn\" was not injected: check your FXML file 'RicercaPerContrastoEllitticita.fxml'.";
        assert nextBtn != null : "fx:id=\"nextBtn\" was not injected: check your FXML file 'RicercaPerContrastoEllitticita.fxml'.";
        assert filamentiTab != null : "fx:id=\"filamentiTab\" was not injected: check your FXML file 'RicercaPerContrastoEllitticita.fxml'.";
        assert filColumn != null : "fx:id=\"filColumn\" was not injected: check your FXML file 'RicercaPerContrastoEllitticita.fxml'.";
        assert tfSatellite != null : "fx:id=\"tfSatellite\" was not injected: check your FXML file 'RicercaPerContrastoEllitticita.fxml'.";
    }
}

