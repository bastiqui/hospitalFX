import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;

public class ChartController {

    public PieChart Intocable, Editable;

    @FXML
    public void initialize() {
        addChart();
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
