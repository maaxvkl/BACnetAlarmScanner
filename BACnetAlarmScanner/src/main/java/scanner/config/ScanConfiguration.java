package scanner.config;

import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

public class ScanConfiguration {

   private Map<ObjectType,Set<PropertyIdentifier>> propertiesPerType = new HashMap<>();
   @Getter
   private int discoveryTime;
   @Getter
   private String ipAddress;

   public void enable(ObjectType objectType, PropertyIdentifier propertyIdentifier) {
       if (propertyIdentifier != null) {
           propertiesPerType.computeIfAbsent(objectType, t -> new HashSet<>()).add(propertyIdentifier);
       }
       else {
           propertiesPerType.put(objectType, Collections.emptySet());
       }
   }

   public void setDiscoveryTime (String discoveryTime){
           this.discoveryTime = Integer.parseInt(discoveryTime);
   }

   public void setIpAddress(String ipAddress){ this.ipAddress = ipAddress;}

   public boolean isEnabled (ObjectType objectType){
           return propertiesPerType.containsKey(objectType);
   }

   public Set<PropertyIdentifier> getPropertyFor (ObjectType objectType){
           return propertiesPerType.getOrDefault(objectType, Collections.emptySet());
   }
}
