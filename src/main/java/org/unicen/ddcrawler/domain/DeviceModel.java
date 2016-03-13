package org.unicen.ddcrawler.domain;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

/**
 * 
 */
@Entity
public class DeviceModel {

    @Id
    private final UUID uuid;

    @Column(nullable = false)
    private final String brand;

    @Column(nullable = false)
    private final String model;

    @Column(nullable = false)
    private final Date createdOn;
    
    @Column(nullable = false)
    private final String createdBy;

    @Column
    private String modelAlias;
    
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn
    private Set<DeviceFeature> features;
    
    private DeviceModel() {
        // ORM initialization
        
        this.uuid = null;
        this.brand = null;
        this.model = null;
        this.createdOn = null;
        this.createdBy = null;
    }

    private DeviceModel(Builder builder) {

        Objects.requireNonNull(builder.brand, "Brand cannot be null");
        Objects.requireNonNull(builder.model, "Model cannot be null");
        Objects.requireNonNull(builder.createdBy, "CreatedBy cannot be null");
        
        this.uuid = UUID.randomUUID();
        this.createdOn = new Date();
        
        this.brand = builder.brand;
        this.model = builder.model;
        this.createdBy = builder.createdBy;
        this.modelAlias = builder.modelAlias;
    }

    public void setModelAlias(String modelAlias) {
        this.modelAlias = modelAlias;
    }
    
	public void setFeatures(Set<DeviceFeature> features) {
		this.features = features;
	}
    
    public UUID getUuid() {
        return uuid;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }
    
    public Optional<String> getModelAlias() {
        return Optional.ofNullable(modelAlias);
    }
    
    public Optional<Set<DeviceFeature>> getFeatures() {
		return Optional.ofNullable(features);
	}
    
	public Date getCreatedOn() {
		return createdOn;
	}

	public String getCreatedBy() {
		return createdBy;
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
		return "DeviceModel [uuid=" + uuid + ", brand=" + brand + ", model=" + model + ", createdOn=" + createdOn
				+ ", createdBy=" + createdBy + ", modelAlias=" + modelAlias + "]";
	}

	public static class Builder {
    	
    	private String brand;
        private String model;
        private String createdBy;
        private String modelAlias;

		public Builder setBrand(String brand) {
			
			Objects.requireNonNull(brand);
			this.brand = brand;

			return this;
		}

		public Builder setModel(String model) {
			
			Objects.requireNonNull(model);
			this.model = model;

			return this;
		}

		public Builder setCreatedBy(String createdBy) {
			
			Objects.requireNonNull(createdBy);
			this.createdBy = createdBy;
			
			return this;
		}

		public Builder setModelAlias(String modelAlias) {
			this.modelAlias = modelAlias;
			
			return this;
		}
    	
		public DeviceModel build() {
			return new DeviceModel(this);
		}
    }
}
