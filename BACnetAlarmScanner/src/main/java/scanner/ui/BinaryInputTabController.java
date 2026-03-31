package scanner.ui;

import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import scanner.config.ScanConfiguration;

public class BinaryInputTabController {

    @FXML
    private CheckBox presentValueCheck;
    @FXML
    private CheckBox activeTextCheck;
    @FXML
    private CheckBox inactiveTextCheck;


    public BinaryInputTabController() {
    }

    public void contribute(ScanConfiguration scanConfiguration) {

        if (presentValueCheck.isSelected()) {
            scanConfiguration.enable(ObjectType.binaryInput, PropertyIdentifier.presentValue);
        }
        if (activeTextCheck.isSelected()) {
            scanConfiguration.enable(ObjectType.binaryInput, PropertyIdentifier.activeText);
        }
        if (inactiveTextCheck.isSelected()) {
            scanConfiguration.enable(ObjectType.binaryInput, PropertyIdentifier.inactiveText);
        }
        if (!inactiveTextCheck.isSelected() & !presentValueCheck.isSelected() & !activeTextCheck.isSelected()) {
            scanConfiguration.enable(ObjectType.binaryInput, null);
        }
    }
}

