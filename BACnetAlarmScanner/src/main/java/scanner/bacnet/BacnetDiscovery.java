package scanner.bacnet;

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.transport.DefaultTransport;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.constructed.Address;
import com.serotonin.bacnet4j.type.constructed.SequenceOf;
import com.serotonin.bacnet4j.type.constructed.StatusFlags;
import com.serotonin.bacnet4j.type.enumerated.EventState;
import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.primitive.CharacterString;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.util.RemoteDeviceDiscoverer;
import com.serotonin.bacnet4j.npdu.ip.IpNetworkBuilder;
import com.serotonin.bacnet4j.npdu.ip.IpNetwork;
import com.serotonin.bacnet4j.util.RequestUtils;
import scanner.config.ScanConfiguration;
import scanner.factory.FaultFactory;
import scanner.fault.ActiveFault;


import java.time.LocalDateTime;
import java.util.*;


public class BacnetDiscovery {

    public List<ActiveFault> discoverDevices(ScanConfiguration scanConfiguration) {

        List<ActiveFault> faults = new ArrayList<>();
        Map<PropertyIdentifier, Encodable> faultProperties = new HashMap<>();

        try {
            IpNetwork network = new IpNetworkBuilder()
                    .withLocalBindAddress(scanConfiguration.getIpAddress())
                    .withSubnet("10.64.28.0", 23)  //192.1.1.0  //networkPrefixLength 24
                    .withPort(47808)
                    .build();

            LocalDevice localDevice = new LocalDevice(12345, new DefaultTransport(network));

            localDevice.initialize();

            RemoteDeviceDiscoverer discoverer = localDevice.startRemoteDeviceDiscovery();

            Thread.sleep(scanConfiguration.getDiscoveryTime() * 1000L);

            List<RemoteDevice> remoteDevices = new ArrayList<>(discoverer.getRemoteDevices());

            discoverer.stop();

            System.out.println(remoteDevices.size());

            for (RemoteDevice remoteDevice : remoteDevices) {

                if(!remoteDevice.getAddress().getDescription().startsWith("10.64.28")){
                    continue;
                }

                SequenceOf<ObjectIdentifier> objectList = RequestUtils.getObjectList(localDevice, remoteDevice);

                for (ObjectIdentifier objectIdentifier : objectList) {

                    ObjectType objectType = objectIdentifier.getObjectType();

                    if (scanConfiguration.isEnabled(objectType)) {
                        EventState eventState = RequestUtils.getProperty(localDevice, remoteDevice, objectIdentifier, PropertyIdentifier.eventState);
                        StatusFlags statusFlags = RequestUtils.getProperty(localDevice, remoteDevice, objectIdentifier, PropertyIdentifier.statusFlags);

                        if (statusFlags.isInAlarm() || statusFlags.isFault()) {
                            Set<PropertyIdentifier> properties = scanConfiguration.getPropertyFor(objectType);
                            if (objectType == ObjectType.binaryValue || objectType == ObjectType.binaryInput || objectType == ObjectType.binaryOutput) {
                                try {
                                    if (!properties.isEmpty()) {
                                        properties.forEach(propertyIdentifier -> {
                                            Encodable encodable = null;
                                            try {
                                                encodable = RequestUtils.getProperty(localDevice, remoteDevice, objectIdentifier, propertyIdentifier);
                                            } catch (BACnetException e) {
                                                throw new RuntimeException(e);
                                            }
                                            faultProperties.put(propertyIdentifier, encodable);
                                        });
                                    }
                                    CharacterString name = RequestUtils.getProperty(localDevice, remoteDevice, objectIdentifier, PropertyIdentifier.objectName);
                                    CharacterString description = RequestUtils.getProperty(localDevice, remoteDevice, objectIdentifier, PropertyIdentifier.description);
                  //                  StatusFlags statusFlags = RequestUtils.getProperty(localDevice, remoteDevice, objectIdentifier, PropertyIdentifier.statusFlags);
                                    Address address = remoteDevice.getAddress();
                                    FaultFactory binaryFaultFactory = new FaultFactory(name, description, objectType, address,
                                            eventState, statusFlags, LocalDateTime.now(), faultProperties);
                                    ActiveFault binaryActiveFault = binaryFaultFactory.createActiveFault();
                                    faults.add(binaryActiveFault);

                                } catch (BACnetException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                            if (objectType == ObjectType.analogValue || objectType == ObjectType.analogInput || objectType == ObjectType.analogOutput) {
                                try {
                                    if (!properties.isEmpty()) {
                                        properties.forEach(propertyIdentifier -> {
                                            Encodable encodable = null;
                                            try {
                                                encodable = RequestUtils.getProperty(localDevice, remoteDevice, objectIdentifier, propertyIdentifier);
                                            } catch (BACnetException e) {
                                                throw new RuntimeException(e);
                                            }
                                            faultProperties.put(propertyIdentifier, encodable);
                                        });
                                    }
                                    CharacterString name = RequestUtils.getProperty(localDevice, remoteDevice, objectIdentifier, PropertyIdentifier.objectName);
                                    CharacterString description = RequestUtils.getProperty(localDevice, remoteDevice, objectIdentifier, PropertyIdentifier.description);
                     //               StatusFlags statusFlags = RequestUtils.getProperty(localDevice, remoteDevice, objectIdentifier, PropertyIdentifier.statusFlags);
                                    Address address = remoteDevice.getAddress();
                                    FaultFactory analogFaultFactory = new FaultFactory(name, description, objectType, address,
                                            eventState, statusFlags, LocalDateTime.now(), faultProperties);
                                    ActiveFault analogActiveFault = analogFaultFactory.createActiveFault();
                                    faults.add(analogActiveFault);



                                } catch (BACnetException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                    }
                }
            }

            network.terminate();
            localDevice.terminate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return faults;
    }

    public void discoverDevicesTest(ScanConfiguration scanConfiguration) {

        try {
            IpNetwork network = new IpNetworkBuilder()
                    .withLocalBindAddress(scanConfiguration.getIpAddress())
                    .withSubnet("10.64.28.0", 23)  //192.1.1.0
                    .withPort(47808)
                    .build();

            LocalDevice localDevice = new LocalDevice(12345, new DefaultTransport(network));

            localDevice.initialize();

            RemoteDeviceDiscoverer discoverer = localDevice.startRemoteDeviceDiscovery();

            Thread.sleep(scanConfiguration.getDiscoveryTime() * 1000L);

            List<RemoteDevice> remoteDevices = new ArrayList<>(discoverer.getRemoteDevices());
            System.out.println(remoteDevices.size());

            discoverer.stop();

            for (RemoteDevice remoteDevice : remoteDevices) {
                if(!remoteDevice.getAddress().getDescription().startsWith("10.64.28")) {
                    continue;
                }
                System.out.println(remoteDevice.getAddress().getDescription());

            }

            network.terminate();
            localDevice.terminate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }
}





