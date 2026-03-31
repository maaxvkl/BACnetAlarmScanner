package scanner.fault;


import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
public class ActiveFault {
   // private final String deviceName;
   // private final int deviceId;
   // private final ObjectIdentifier objectIdentifier;
    private final String objectName;
    private final String objectDescription;
    private final String objectType;
    private final String deviceIp;
    private final String eventState;
    private final String statusFlags;
    private final LocalDateTime timestamp;
    private final Map<String, String> properties;

    public ActiveFault(
            //      String deviceName,
            //     int deviceId,
            //     ObjectIdentifier objectIdentifier,
            String objectName,
            String objectDescription,
            String objectType,
            String deviceIp,
            String eventState,
            String statusFlags,
            LocalDateTime timestamp,
            Map<String, String> properties
    ) {
     //  this.deviceName = deviceName;
        this.deviceIp = deviceIp;
     //   this.deviceId = deviceId;
     //   this.objectIdentifier = objectIdentifier;
        this.objectType = objectType;
        this.objectName = objectName;
        this.objectDescription = objectDescription;
        this.eventState = eventState;
        this.statusFlags = statusFlags;
        this.timestamp = timestamp;
        this.properties = properties;
    }

    /*
    protected String baseText() {
        return "time=" + timestamp + " | device=" + deviceName +
                " (" + deviceIp + ", id=" + deviceId + ") | object=" +
                objectName + " [" + objectIdentifier + "] | eventState=" + eventState;
    }

     */
}

