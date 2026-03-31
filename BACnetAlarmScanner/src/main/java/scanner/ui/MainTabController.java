package scanner.ui;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import jdk.jfr.Label;
import lombok.Getter;

public class MainTabController {

    @FXML
    private TextField discoveryTime;
    @FXML
    private TextField ipAddressField;
    @Getter
    @FXML
    private CheckBox bvCheck;
    @Getter
    @FXML
    private CheckBox biCheck;
    @Getter
    @FXML
    private CheckBox aiCheck;

    public MainTabController() {}

    public String getDiscoveryTime(){
        return discoveryTime.getText();
    }

    public String getIpAddress(){
        return ipAddressField.getText();
    }
}
