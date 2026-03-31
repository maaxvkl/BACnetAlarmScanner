package scanner.mapper;

import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.enumerated.EngineeringUnits;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.primitive.Boolean;
import com.serotonin.bacnet4j.type.primitive.Real;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AnalogMapper {

    Map<PropertyIdentifier, Encodable> properties;

    public AnalogMapper(Map<PropertyIdentifier, Encodable> properties) {
        this.properties = properties;
    }

    public Map<String, String> mapAnalogProperties() {
        Map<String, String> mappedProperties = new HashMap<>();
        if (!properties.isEmpty()) {
            for (Map.Entry<PropertyIdentifier, Encodable> entry : properties.entrySet()) {
                PropertyIdentifier propertyIdentifier = entry.getKey();
                Encodable encodable = entry.getValue();
                if (propertyIdentifier == PropertyIdentifier.presentValue) {
                    Real presentValue = (Real) encodable;
                    double value = presentValue.floatValue();
                    mappedProperties.put(propertyIdentifier.toString(), String.valueOf(value));
                }
                if (propertyIdentifier == PropertyIdentifier.units) {
                    assert encodable instanceof EngineeringUnits;
                    EngineeringUnits units = (EngineeringUnits) encodable;
                    String unitText = units.toString();
                    mappedProperties.put(propertyIdentifier.toString(), unitText);
                }
                if (propertyIdentifier == PropertyIdentifier.outOfService) {
                    assert encodable instanceof Boolean;
                    Boolean outOfService = (Boolean) encodable;
                    String result = outOfService.toString();
                    mappedProperties.put(propertyIdentifier.toString(), result);
                }
            }
        } else {
            return Collections.emptyMap();
        }
        return mappedProperties;
    }
}
