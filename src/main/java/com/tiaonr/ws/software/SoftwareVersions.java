package com.tiaonr.ws.software;

import java.util.List;
import java.util.ArrayList;
import java.nio.file.Files;
import java.nio.file.Paths;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class SoftwareVersions {
    private static final Logger LOGGER = LoggerFactory.getLogger(SoftwareVersions.class);
    private static List<SoftwareVersion> versions = new ArrayList<SoftwareVersion>();

    public static void reload(String path) {
        try {
            byte[] jsonData = Files.readAllBytes(Paths.get(path));
            ObjectMapper objectMapper = new ObjectMapper();
            versions = objectMapper.readValue(jsonData, new TypeReference<List<SoftwareVersion>>() {});
        } catch ( IOException e ) {
            LOGGER.warn("Failed to reload software versions from " + path, e);
        }
    }

    public static SoftwareVersion get(String deviceType) {
        for ( int i = 0; i < versions.size(); i++ ) {
            if ( versions.get(i).getDeviceType().equals(deviceType) ) {
                return versions.get(i);
            }
        }
        return null;
    }
}
