package scanner.ui;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import scanner.config.ScanConfiguration;
import scanner.fault.ActiveFault;
import scanner.filehandling.ExcelSaver;
import scanner.thread.ScanTask;

import java.util.List;

public class MainController {

    ScanConfiguration scanConfiguration;

    @FXML
    private MainTabController mainTabIncludeController;

    @FXML
    private BinaryValueTabController binaryValueTabIncludeController;

    @FXML
    private BinaryInputTabController binaryInputTabIncludeController;

    @FXML
    private AnalogInputTabController analogInputTabIncludeController;

    @FXML
    private Parent mainTabInclude;

    @FXML
    private Parent binaryValueTabInclude;

    @FXML
    private Parent binaryInputTabInclude;

    @FXML
    private Parent analogInputTabInclude;

    @FXML
    private TabPane tabPane;

    @FXML
    private Label statusLabel;

    @FXML
    private Button cancelButton;


    public MainController() {
        this.scanConfiguration = new ScanConfiguration();
    }

    @FXML
    public void initialize() {
        mainTabIncludeController.getBvCheck().selectedProperty().addListener((observable, oldValue, newValue) -> {
            binaryValueTabInclude.setDisable(!newValue);
        });
        mainTabIncludeController.getBiCheck().selectedProperty().addListener((observable, oldValue, newValue) -> {
            binaryInputTabInclude.setDisable(!newValue);
        });

    }

    @FXML
    private void startScan() {
        scanConfiguration.setDiscoveryTime(mainTabIncludeController.getDiscoveryTime());
        scanConfiguration.setIpAddress(mainTabIncludeController.getIpAddress());

        if (mainTabIncludeController.getBvCheck().isSelected()) {
            binaryValueTabIncludeController.contribute(scanConfiguration);
        }
        if (mainTabIncludeController.getBiCheck().isSelected()) {
            binaryInputTabIncludeController.contribute(scanConfiguration);
        }
        if (mainTabIncludeController.getAiCheck().isSelected()) {
            analogInputTabIncludeController.contribute(scanConfiguration);
        }

        ScanTask scanTask = new ScanTask(scanConfiguration);

        statusLabel.textProperty().bind(scanTask.messageProperty());

        scanTask.setOnSucceeded(e -> {
            List<ActiveFault> faults = scanTask.getValue();
            ExcelSaver excelSaver = new ExcelSaver();
            excelSaver.saveExcelFile(faults);
        });

        scanTask.setOnFailed(e -> {
            scanTask.getException().printStackTrace();
        });

        Thread t = new Thread(scanTask);
        t.setDaemon(true);
        t.start();

    }


    @FXML
    private void cancelScan() {
        // leer lassen reicht fürs Starten
    }
}
