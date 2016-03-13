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
    @Column(columnDefinition = "BINARY(16)")
    private final UUID uuid;

    @Column(nullable = false)
    private final String featureName;

    @Column(nullable = false)
    private final Date createdOn;
    
    @Column(nullable = false)
    private final String createdBy;

    @ManyToOne
    @JoinColumn(nullable = false)
    private DeviceModel model;
    
    @ElementCollection
    @MapKeyColumn(name = "attribute")
    @Column(name = "value")
    @CollectionTable(name = "featureAttributes", joinColumns = @JoinColumn(name = "featureId") )
    private Map<String, String> attributes;
    
    @SuppressWarnings("unused")
	private DeviceFeature() {
    
    	this.uuid = null;
    	this.model = null;
    	this.featureName = null;
    	this.createdOn = null;
    	this.createdBy = null;
    }
    
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
    
    public void setModel(DeviceModel model) {
       
    	Objects.requireNonNull(model, "DeviceModel cannot be null");
        this.model = model;
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
		result = prime * result + ((attributes == null) ? 0 : attributes.hashCode());
		result = prime * result + ((featureName == null) ? 0 : featureName.hashCode());
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
		if (attributes == null) {
			if (other.attributes != null)
				return false;
		} else if (!attributes.equals(other.attributes))
			return false;
		if (featureName == null) {
			if (other.featureName != null)
				return false;
		} else if (!featureName.equals(other.featureName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "DeviceFeature [uuid=" + uuid + ", featureName=" + featureName + ", createdOn=" + createdOn
				+ ", createdBy=" + createdBy + ", attributes=" + attributes + "]";
	}
}
