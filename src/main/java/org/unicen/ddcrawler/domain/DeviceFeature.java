package org.unicen.ddcrawler.domain;

import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;

@Entity
public class DeviceFeature {

    @Id
    private final UUID uuid;

    @ManyToOne
    @JoinColumn(nullable = false)
    private final DeviceModel model;

    @Column(nullable = false)
    private final String featureName;

    @Column(nullable = false)
    private final Date createdOn;
    
    @Column(nullable = false)
    private final String createdBy;
    
    @ElementCollection
    @MapKeyColumn(name = "attribute")
    @Column(name = "value")
    @CollectionTable(name = "featureAttributes", joinColumns = @JoinColumn(name = "featureId") )
    private Map<String, String> attributes;
    
    public DeviceFeature(DeviceModel model, String featureName, String createdBy) {

        Objects.requireNonNull(model, "DeviceModel cannot be null");
        Objects.requireNonNull(featureName, "FeatureName cannot be null");
        Objects.requireNonNull(createdBy, "CreatedBy cannot be null");
        
        this.uuid = UUID.randomUUID();
        this.createdOn = new Date();
        
        this.model = model;
        this.featureName = featureName;
        this.createdBy = createdBy;
    }

    public UUID getUuid() {
        return uuid;
    }

    public DeviceModel getModel() {
        return model;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    
    public String getFeatureName() {
        return featureName;
    }

    public Map<String, String> getAttributes() {
        return attributes;
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
        DeviceFeature other = (DeviceFeature) obj;
        if (uuid == null) {
            if (other.uuid != null)
                return false;
        } else if (!uuid.equals(other.uuid))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "DeviceFeature [uuid=" + uuid + ", model=" + model + ", featureName=" + featureName + ", attributes=" + attributes + "]";
    }
}
