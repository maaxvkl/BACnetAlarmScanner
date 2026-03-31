package scanner.ui;

import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import scanner.config.ScanConfiguration;

public class AnalogInputTabController {

    @FXML
    private CheckBox presentValueCheck;
    @FXML
    private CheckBox unitsCheck;
    @FXML
    private CheckBox outOfServiceCheck;

    public AnalogInputTabController() {
    }

    public void contribute(ScanConfiguration scanConfiguration) {

        if (presentValueCheck.isSelected()) {
            scanConfiguration.enable(ObjectType.analogInput, PropertyIdentifier.presentValue);
        }
        if (unitsCheck.isSelected()) {
            scanConfiguration.enable(ObjectType.analogInput, PropertyIdentifier.units);
        }
        if (outOfServiceCheck.isSelected()) {
            scanConfiguration.enable(ObjectType.analogInput, PropertyIdentifier.inactiveText);
        }
        if(!presentValueCheck.isSelected() & !unitsCheck.isSelected() & !outOfServiceCheck.isSelected())  {
            scanConfiguration.enable(ObjectType.analogInput, null);
        }
    }
}

