package scanner.mapper;

import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.enumerated.BinaryPV;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.primitive.CharacterString;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class BinaryMapper {

    Map<PropertyIdentifier, Encodable> properties;

    public BinaryMapper(Map<PropertyIdentifier, Encodable> properties) {
        this.properties = properties;
    }

    public Map<String, String> mapBinaryProperties() {

        Map<String, String> mappedProperties = new HashMap<>();
        if (!properties.isEmpty()) {
            for (Map.Entry<PropertyIdentifier, Encodable> entry : properties.entrySet()) {
                PropertyIdentifier propertyIdentifier = entry.getKey();
                Encodable encodable = entry.getValue();
                if (propertyIdentifier == PropertyIdentifier.activeText) {
                    CharacterString activeText = (CharacterString) encodable;
                    String result = activeText.getValue();
                    mappedProperties.put(propertyIdentifier.toString(), result);
                }
                if (propertyIdentifier == PropertyIdentifier.inactiveText) {
                    CharacterString inactiveText = (CharacterString) encodable;
                    String result = inactiveText.getValue();
                    mappedProperties.put(propertyIdentifier.toString(), result);
                }
                if (propertyIdentifier == PropertyIdentifier.presentValue) {
                    assert encodable instanceof BinaryPV;
                    BinaryPV binaryPV = (BinaryPV) encodable;
                    String result = binaryPV.toString();
                    mappedProperties.put(propertyIdentifier.toString(), result);
                }
            }
        } else {
            return Collections.emptyMap();
        }
        return mappedProperties;
    }
}
