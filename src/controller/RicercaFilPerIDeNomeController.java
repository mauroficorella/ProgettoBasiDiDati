package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import DB.RicercaFilPerIDeNomeQuery;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class RicercaFilPerIDeNomeController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Hyperlink backBtn;

    @FXML
    private TextField tfNomeIDFil;

    @FXML
    private Button btnResearch;

    @FXML
    private Label latCentroide;

    @FXML
    private Label longCentroide;

    @FXML
    private Label numSegm;

    @FXML
    private Label estLong;

    @FXML
    private Label estLat;

    @FXML
    private TextField tfSatellite;

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

    @FXML
    public void doBtnResearch(ActionEvent event) throws IOException, SQLException {
        RicercaFilPerIDeNomeQuery rf = new RicercaFilPerIDeNomeQuery();
        String value = tfNomeIDFil.getText();
        String satellite = tfSatellite.getText();

        if(!controlloDati()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Impossibile Procedere");
            alert.setContentText("Inserire tutti i dati!");
            alert.showAndWait();
            return;
        }
        else    {
            if ((rf.searchFilament(value, satellite)))  {
                ArrayList<Double> centroide = rf.findCentroide(value, satellite);
                Integer numeroSegm = rf.findNumberSegments(value, satellite);
                ArrayList<Double> estensione = rf.findPerimeterExtension(value, satellite);
                Double longitudine_centroide = Double.valueOf(centroide.get(0));
                Double latitudine_centroide = Double.valueOf(centroide.get(1));
                Double longitudine_ext = Double.valueOf(estensione.get(0));
                Double latitudine_ext = Double.valueOf(estensione.get(1));
                longCentroide.setText(String.valueOf(longitudine_centroide));
                latCentroide.setText(String.valueOf(latitudine_centroide));
                numSegm.setText(String.valueOf(numeroSegm));
                estLong.setText(String.valueOf(longitudine_ext));
                estLat.setText(String.valueOf(latitudine_ext));
            }
            else    {
                showAlert(1);
                return;
            }
        }
    }

    public void showAlert(int i){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Dialog");
        switch (i){
            case 1:
                //nessun filamento trovato
                alert.setHeaderText("Nessun Filamento Trovato");
                alert.setContentText("Non esiste nessun filamento corrispondente\nal nome o all'id inserito");
                alert.showAndWait();
            case 2:
                //valore nell textview non valido
                alert.setHeaderText("Impossibile Procedere");
                alert.setContentText("Inserisci un valore corretto\nper procedere con la ricerca");
                alert.showAndWait();
            default:
                return;
        }
    }

    public boolean controlloDati() {

        if (tfNomeIDFil.getText().equalsIgnoreCase("")) {
            return false;
        }
        if (tfSatellite.getText().equalsIgnoreCase("")) {
            return false;
        }
        return true;
    }

    @FXML
    void initialize() {
        assert backBtn != null : "fx:id=\"backBtn\" was not injected: check your FXML file 'RicercaFilPerIDeNome.fxml'.";
        assert tfNomeIDFil != null : "fx:id=\"tfNomeIDFil\" was not injected: check your FXML file 'RicercaFilPerIDeNome.fxml'.";
        assert btnResearch != null : "fx:id=\"btnResearch\" was not injected: check your FXML file 'RicercaFilPerIDeNome.fxml'.";
        assert latCentroide != null : "fx:id=\"latCentroide\" was not injected: check your FXML file 'RicercaFilPerIDeNome.fxml'.";
        assert longCentroide != null : "fx:id=\"longCentroide\" was not injected: check your FXML file 'RicercaFilPerIDeNome.fxml'.";
        assert numSegm != null : "fx:id=\"numSegm\" was not injected: check your FXML file 'RicercaFilPerIDeNome.fxml'.";
        assert estLong != null : "fx:id=\"estLong\" was not injected: check your FXML file 'RicercaFilPerIDeNome.fxml'.";
        assert estLat != null : "fx:id=\"estLat\" was not injected: check your FXML file 'RicercaFilPerIDeNome.fxml'.";
        assert tfSatellite != null : "fx:id=\"tfSatellite\" was not injected: check your FXML file 'RicercaFilPerIDeNome.fxml'.";
    }
}
