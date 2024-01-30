package com.mshembelev.mindskeeper.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@EnableConfigurationProperties({ FileStorageProperty.class})
@ConfigurationProperties(prefix = "file")
public class FileStorageProperty {
    private String uploadDirectory;

    public String getUploadDirectory(){
        return uploadDirectory;
    }

    public void setUploadDirectory(String uploadDirectory){
        this.uploadDirectory = uploadDirectory;
    }
}
