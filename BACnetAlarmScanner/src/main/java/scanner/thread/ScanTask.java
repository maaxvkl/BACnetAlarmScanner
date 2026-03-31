package scanner.thread;

import javafx.concurrent.Task;
import scanner.bacnet.BacnetDiscovery;
import scanner.config.ScanConfiguration;
import scanner.fault.ActiveFault;

import java.util.Collections;
import java.util.List;

public class ScanTask extends Task<List<ActiveFault>> {

    private final BacnetDiscovery bacnetDiscovery;
    private final ScanConfiguration scanConfiguration;

    public ScanTask(ScanConfiguration scanConfiguration) {
        this.bacnetDiscovery = new BacnetDiscovery();
        this.scanConfiguration = scanConfiguration;
    }

    @Override
    protected List<ActiveFault> call() throws Exception {
        updateMessage("Initializing Scan");

        try {
            updateMessage("Starting Scan...");

            List<ActiveFault> faults = bacnetDiscovery.discoverDevices(scanConfiguration);

            updateMessage("Found faults " + faults.size());

            if (isCancelled()) {
                updateMessage("Scan canceled.");
                return Collections.emptyList();
            }

            updateMessage("Generating Excel File");

            return faults;

        } catch (Exception e) {
            updateMessage("Fehler beim Scan.");
            throw e;

        }
    }
}

