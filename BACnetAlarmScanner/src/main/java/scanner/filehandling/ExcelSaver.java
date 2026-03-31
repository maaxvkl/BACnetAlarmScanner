package scanner.filehandling;

import javafx.stage.FileChooser;
import scanner.fault.ActiveFault;
import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

public class ExcelSaver {

    public void saveExcelFile(List<ActiveFault> faults) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save report");

        // Standard-Dateiname
        fileChooser.setInitialFileName("report" + LocalDateTime.now().toLocalDate().toString() +".xlsx");

        // Filter (nur Excel anzeigen)
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Excel Dateien (*.xlsx)", "*.xlsx")
        );

        // Optional: Startordner
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

        File file = fileChooser.showSaveDialog(null); // oder deine Stage

        if (file != null) {
            try {
                FaultExcelWriter writer = new FaultExcelWriter();
                writer.generateExcelFile(faults, file.toPath());

    //            statusLabel.setText("Datei gespeichert: " + file.getAbsolutePath());

            } catch (Exception e) {
                e.printStackTrace();
     //           statusLabel.setText("Fehler beim Speichern!");
            }
        } else {
      //      statusLabel.setText("Speichern abgebrochen.");
        }
    }

}
