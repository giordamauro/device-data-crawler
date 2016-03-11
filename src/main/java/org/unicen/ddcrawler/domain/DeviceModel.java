package org.unicen.ddcrawler.domain;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 
 */
@Entity
public class DeviceModel {

    @Id
    private final UUID uuid;

    @Column(nullable = false)
    private final String brandName;

    @Column(nullable = false)
    private final String model;

    @Column
    private String modelAlias;

    @Column(nullable = false)
    private final Date extractionDate;
    
    @Column
    private String extractionVersion;

    
    @SuppressWarnings("unused")
    private DeviceModel() {
        // ORM initialization
        
        this.uuid = null;
        this.brandName = null;
        this.model = null;
        this.extractionDate = null;
    }

    public DeviceModel(String brandName, String model) {

        Objects.requireNonNull(brandName);
        Objects.requireNonNull(model);

        this.uuid = UUID.randomUUID();
        this.extractionDate = new Date();
        
        this.brandName = brandName;
        this.model = model;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getBrandName() {
        return brandName;
    }

    public String getModel() {
        return model;
    }

    public String getModelAlias() {
        return modelAlias;
    }

    public String getExtractionVersion() {
        return extractionVersion;
    }
    
    public void setModelAlias(String modelAlias) {
        this.modelAlias = modelAlias;
    }

    public void setExtractionVersion(String extractionVersion) {
        this.extractionVersion = extractionVersion;
    }

    public Date getExtractionDate() {
        return extractionDate;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DeviceModel other = (DeviceModel) obj;
        if (uuid == null) {
            if (other.uuid != null)
                return false;
        } else if (!uuid.equals(other.uuid))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "DeviceModel [uuid=" + uuid + ", brandName=" + brandName + ", model=" + model + ", modelAlias=" + modelAlias + ", extractionDate=" + extractionDate + ", extractionVersion="
                + extractionVersion + "]";
    }
}
