import Model.Hospital;
import Model.Pacient;
import Model.Persona;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class WindowController {
    public Button btnSearch, btnList, BtnFile;
    public TableView<Pacient> tblView;
    public TextField WeightF, WeightT, AgeF, AgeT, HeightF, HeightT;
    public TextField SearchDni, SearchSurname, SearchName;;

    private List<Pacient> pacientList = new ArrayList<>();
    private boolean listaCargada = false;
    private boolean tablaHecha = false;
    private Hospital hospital = new Hospital();
    private File csvPath = new File("");
    private ObservableList<Pacient> data = FXCollections.observableArrayList();


    private void alerta(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Ves amb compte!");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void CamposTabla() {
        TableColumn DNI = new TableColumn("DNI");
        TableColumn Nom = new TableColumn("Nom");
        TableColumn Cognoms = new TableColumn("Cognoms");
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
        tablaHecha = true;
    }

    public void VerLista() {
        if (listaCargada) {
            tblView.refresh();
            pacientList.clear();
            if (!tablaHecha) CamposTabla();
           // data.add(new Pacient("12345678C", "Test", "Java", LocalDate.of(2019, 12, 12), Persona.Genere.DONA, "55555599", 5.4f, 100));
            loadData();
            data.addAll(pacientList);
            tblView.setItems(data);
        } else {
            alerta("No hi han dades.", "No s'ha carregat cap dada al programa.");
        }
    }

    private void loadData() {
        pacientList.addAll(hospital.loadPacients(csvPath));
    }

    public void ChooseFile(ActionEvent event) {
        Node node = (Node) event.getSource();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Escull el CSV de pacients.");
        csvPath = fileChooser.showOpenDialog(node.getScene().getWindow());
        listaCargada = hospital.checkCSV(csvPath);
        if (!listaCargada) alerta("Fitxer not vàlid.", "El programa no pot llegir aquest fitxer");
        else VerLista();
    }
    public  void Buscar(ActionEvent event){
        List<Pacient> pacients = pacientList.stream()
                .filter(pacient -> pacient.getDNI().equals(SearchDni.getText()))
                .collect(Collectors.toList());
        if(SearchDni.getText().equals("")){
            data.clear();
            data.addAll(pacientList);
            tblView.setItems(data);
        }else{
            data.clear();
            data.addAll(pacients);
            tblView.setItems(data);
        }
    }

    public void searchtext(KeyEvent keyEvent) {
        data.clear();
        List<Pacient> pacients = pacientList.stream()
                .filter(pacient -> pacient.getNom().contains(SearchName.getText()))
                .filter((pacient -> pacient.getCognoms().contains(SearchSurname.getText())))
                .filter(pacient -> pacient.getDNI().contains(SearchDni.getText()))
                .collect(Collectors.toList());
        data.addAll(pacients);
        tblView.setItems(data);
    }

}