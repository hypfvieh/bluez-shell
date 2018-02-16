package com.github.hypfvieh.helper;

import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

import com.github.hypfvieh.util.FileIoUtil;

/**
 * Helper which tries to resolve UUID found on bluetooth devices to a human readable name.
 */
public class UuidResolver {
    private static final UuidResolver INSTANCE = new UuidResolver();
    
    private final Properties uuids2name = new Properties();
    
    private UuidResolver() {
        Properties readProperties = FileIoUtil.readProperties(getClass().getClassLoader().getResourceAsStream("uuids2name.properties"));
        uuids2name.putAll(readProperties);
    }
    
    public static UuidResolver getInstance() {
        return INSTANCE;
    }
    
    public String getNameFromUuid(String _uuid) {
        if (StringUtils.isBlank(_uuid)) {
            return null;
        }
        
        String uuidPart = _uuid.substring(4, 8);
        
        return uuids2name.getProperty(uuidPart);
    }
}
