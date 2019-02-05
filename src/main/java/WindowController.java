import Model.Hospital;
import Model.Pacient;
import Model.Persona;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class WindowController {
    public Button btnSearch, btnList, BtnFile;
    public TableView<Pacient> tblView;

    private List<Pacient> pacientList = new ArrayList<>();
    private boolean listaCargada = false;

    public void VerLista(javafx.event.ActionEvent event) {
        if (listaCargada) {
            Node node = (Node) event.getSource();

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Escull el CSV de pacients.");
            File file = fileChooser.showOpenDialog(node.getScene().getWindow());

            TableColumn DNI = new TableColumn("DNI");
            TableColumn Nom = new TableColumn("Nom");
            TableColumn Cognoms = new TableColumn("Cognoms");
            TableColumn Email = new TableColumn("Email");
            TableColumn DataNaix = new TableColumn("Data de Naixament");
            TableColumn Genre = new TableColumn("Gènere");
            TableColumn pes = new TableColumn("Pes");
            TableColumn Alcada = new TableColumn("Alçada");
            TableColumn Telefon = new TableColumn("Telèfon");

            // COMPTE!!!! les propietats han de tenir getters i setters
            DNI.setCellValueFactory(new PropertyValueFactory<Pacient, String>("DNI"));
            Nom.setCellValueFactory(new PropertyValueFactory<Pacient, String>("Nom"));
            Cognoms.setCellValueFactory(new PropertyValueFactory<Pacient, String>("Cognoms"));
            DataNaix.setCellValueFactory(new PropertyValueFactory<Pacient, String>("DataNaixament"));
            Genre.setCellValueFactory(new PropertyValueFactory<Pacient, String>("genere"));
            pes.setCellValueFactory(new PropertyValueFactory<Pacient, Float>("Pes"));
            Alcada.setCellValueFactory(new PropertyValueFactory<Pacient, Integer>("Alçada"));
            Telefon.setCellValueFactory(new PropertyValueFactory<Pacient, String>("Telefon"));

            tblView.getColumns().addAll(DNI, Nom, Cognoms, DataNaix, Genre, pes, Alcada, Telefon);

            ObservableList<Pacient> data = FXCollections.observableArrayList();
            data.add(new Pacient("12345678C", "Test", "Java", LocalDate.of(2019, 12, 12), Persona.Genere.DONA, "555555599", 5.4f, 100));
            loadData(file);
            data.addAll(pacientList);
            tblView.setItems(data);
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Ves amb compte!");
            alert.setHeaderText("No n'hi han dades.");
            alert.setContentText("No s'ha carregat cap dada al programa.");
            alert.showAndWait();
        }
    }

    private void loadData(File csvPath) {
        Hospital hospital = new Hospital();
        pacientList.addAll(hospital.loadPacients(csvPath));
    }


}