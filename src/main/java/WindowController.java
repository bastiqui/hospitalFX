import Model.Hospital;
import Model.Pacient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class WindowController {
    public Button btnSearch, btnList, BtnFile;
    public TableView<Pacient> tblView;
    public TextField WeightF, WeightT, AgeF, AgeT, HeightF, HeightT;
    public TextField SearchDni, SearchSurname, SearchName;
    public TextField ChartAF, ChartAT, ChartWF, ChartWT, ChartHF, ChartHT;

    public PieChart Intocable, Editable;

    private List<Pacient> pacientList = new ArrayList<>();
    private boolean listaCargada = false;
    private boolean tablaHecha = false;
    private Hospital hospital = new Hospital();
    private File csvPath = new File("");
    private ObservableList<Pacient> data = FXCollections.observableArrayList();

    private LocalDate today = LocalDate.now();

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
        }else {
            data.clear();
            data.addAll(pacients);
            tblView.setItems(data);
        }
    }

    public void filter(KeyEvent keyEvent) {

        data.clear();

        int mesEdat = 0;
        if (!AgeF.getText().equals(""))  {
            mesEdat = Integer.parseInt(AgeF.getText());
        }
        int menysEdat = 500;
        if (!AgeT.getText().equals(""))  {
            menysEdat = Integer.parseInt(AgeT.getText());
        }
        float mesPes = 0.0f;
        if (!WeightF.getText().equals("")) {
            mesPes = Float.parseFloat(WeightF.getText());
        }
        float menysPes = 10000.0f;
        if (!WeightT.getText().equals("")) {
            menysPes = Float.parseFloat(WeightT.getText());
        }
        int mesAlcada = 0;
        if (!HeightF.getText().equals("")) {
            mesAlcada = Integer.parseInt(HeightF.getText());
        }
        int menysAlcada = 100000;
        if (!HeightT.getText().equals("")) {
            menysAlcada = Integer.parseInt(HeightT.getText());
        }

        int finalMesEdat = mesEdat;
        int finalMenysEdat = menysEdat;
        float finalMesPes = mesPes;
        float finalMenysPes = menysPes;
        int finalMesAlcada = mesAlcada;
        int finalMenysAlcada = menysAlcada;

        List<Pacient> pacients = pacientList.stream()
                .filter(pacient -> AgeCalculator(pacient.getDataNaixament(), today) > finalMesEdat)
                .filter(pacient -> AgeCalculator(pacient.getDataNaixament(), today) < finalMenysEdat)
                .filter(pacient -> pacient.getPes() > finalMesPes)
                .filter(pacient -> pacient.getPes() < finalMenysPes)
                .filter(pacient -> pacient.getAlçada() > finalMesAlcada)
                .filter(pacient -> pacient.getAlçada() < finalMenysAlcada)
                .filter(pacient -> pacient.getNom().contains(SearchName.getText()))
                .filter((pacient -> pacient.getCognoms().contains(SearchSurname.getText())))
                .filter(pacient -> pacient.getDNI().contains(SearchDni.getText()))
                .collect(Collectors.toList());

        data.addAll(pacients);
        tblView.setItems(data);
    }

    private int AgeCalculator (LocalDate birthDate, LocalDate currentDate) {
        if ((birthDate != null) && (currentDate != null)) {
            return Period.between(birthDate, currentDate).getYears();
        } else {
            return 0;
        }
    }

    public void ShowChart(ActionEvent actionEvent) {
        Parent root;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("chart.fxml")));
            Stage stage = new Stage();
            stage.setTitle("Chart section");
            stage.setScene(new Scene(root, 600, 400));
            addChart();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addChart() {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                        new PieChart.Data("Grapefruit", 13),
                        new PieChart.Data("Oranges", 25),
                        new PieChart.Data("Plums", 10),
                        new PieChart.Data("Pears", 22),
                        new PieChart.Data("Apples", 30));
        Intocable.setData(pieChartData);
        Intocable.setTitle("Imported Fruits");
    }
}