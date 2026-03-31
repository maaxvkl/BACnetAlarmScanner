package scanner.factory;

import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.constructed.Address;
import com.serotonin.bacnet4j.type.constructed.StatusFlags;
import com.serotonin.bacnet4j.type.enumerated.EventState;
import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.primitive.CharacterString;
import scanner.fault.ActiveFault;
import scanner.mapper.AnalogMapper;
import scanner.mapper.BinaryMapper;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FaultFactory {

    private final CharacterString objectName;
    private final CharacterString objectDescription;
    private final ObjectType objectType;
    private final Address deviceAddress;
    private final EventState eventState;
    private final StatusFlags statusFlags;
    private final LocalDateTime timestamp;
    private final Map<PropertyIdentifier, Encodable> properties;

    public FaultFactory(CharacterString objectName, CharacterString objectDescription,
                              ObjectType objectType, Address address, EventState eventState,
                              StatusFlags statusFlags, LocalDateTime timestamp, Map<PropertyIdentifier, Encodable> properties) {
        this.objectName = objectName;
        this.objectDescription = objectDescription;
        this.objectType = objectType;
        this.deviceAddress = address;
        this.eventState = eventState;
        this.statusFlags = statusFlags;
        this.timestamp = timestamp;
        this.properties = properties;
    }

    public ActiveFault createActiveFault() {
        String name = objectName.getValue();
        String description = objectDescription.getValue();
        String type = objectType.toString();
        String address = deviceAddress.getDescription();
        String state = eventState.toString();
        String flags = getStatusFlagsText(statusFlags);
        if(objectType == ObjectType.binaryInput || objectType == ObjectType.binaryValue) {
            BinaryMapper binaryMapper = new BinaryMapper(properties);
            Map<String, String> mappedFaultProperties = binaryMapper.mapBinaryProperties();
            return new ActiveFault(name, description, type, address, state, flags, timestamp, mappedFaultProperties);
        }
        if(objectType == ObjectType.analogInput || objectType == ObjectType.analogValue) {
            AnalogMapper analogMapper = new AnalogMapper(properties);
            Map<String, String> mappedFaultProperties = analogMapper.mapAnalogProperties();
            return new ActiveFault(name, description, type, address, state, flags, timestamp, mappedFaultProperties);
        }
        return new ActiveFault(null,null,null,null,null,null,null,null);
    }

    private String getStatusFlagsText(StatusFlags flags) {
        List<String> parts = new ArrayList<>();

        if (flags.isInAlarm()) parts.add("Alarm");
        if (flags.isFault()) parts.add("Fehler");
        if (flags.isOverridden()) parts.add("Override");
        if (flags.isOutOfService()) parts.add("OutOfService");

        return parts.isEmpty()
                ? "OK"
                : String.join(", ", parts);
    }
}
