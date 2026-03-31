package scanner.ui;

import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import scanner.config.ScanConfiguration;

import java.util.Collections;

public class BinaryValueTabController {

    @FXML
    private CheckBox presentValueCheck;
    @FXML
    private CheckBox activeTextCheck;
    @FXML
    private CheckBox inactiveTextCheck;


    public BinaryValueTabController() {
    }

    public void contribute(ScanConfiguration scanConfiguration) {

        if (presentValueCheck.isSelected()) {
            scanConfiguration.enable(ObjectType.binaryValue, PropertyIdentifier.presentValue);
        }
        if (activeTextCheck.isSelected()) {
            scanConfiguration.enable(ObjectType.binaryValue, PropertyIdentifier.activeText);
        }
        if (inactiveTextCheck.isSelected()) {
            scanConfiguration.enable(ObjectType.binaryValue, PropertyIdentifier.inactiveText);
        }
        if (!inactiveTextCheck.isSelected() & !presentValueCheck.isSelected() & !activeTextCheck.isSelected()) {
            scanConfiguration.enable(ObjectType.binaryValue, null);
        }
    }
}
