package org.unicen.ddcrawler.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * 
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"brand", "model"})) 
public class DeviceModel {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(nullable = false)
    private final String brand;

    @Column(nullable = false)
    private final String model;

    @Column(nullable = false)
    private final Date createdOn;
    
    @Column(nullable = false)
    private final String createdBy;
    

    @Column(nullable = false)
    private int featuresCount;

    @Column
    private String name;

    @JoinColumn
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<DeviceFeature> features = new HashSet<>();    
    
    private DeviceModel() {
        // ORM initialization
        
        this.id = null;
        this.brand = null;
        this.model = null;
        this.createdOn = null;
        this.createdBy = null;
    }

    private DeviceModel(Builder builder) {

        Objects.requireNonNull(builder.brand, "Brand cannot be null");
        Objects.requireNonNull(builder.model, "Model cannot be null");
        Objects.requireNonNull(builder.createdBy, "CreatedBy cannot be null");
        
        this.createdOn = new Date();
        
        this.brand = builder.brand;
        this.model = builder.model;
        this.createdBy = builder.createdBy;
        this.name = builder.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addFeatures(Set<DeviceFeature> features) {
    	
    	features.forEach(feature -> feature.setModel(this));

    	this.features.addAll(features);
    	this.featuresCount = features.size();
	}
    
	public Integer getId() {
		return id;
	}
	
	public Set<DeviceFeature> getFeatures() {
		return features;
	}

	public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }
    
    public Optional<String> getName() {
        return Optional.ofNullable(name);
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
		result = prime * result + ((brand == null) ? 0 : brand.hashCode());
		result = prime * result + ((createdBy == null) ? 0 : createdBy.hashCode());
		result = prime * result + ((model == null) ? 0 : model.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		if (brand == null) {
			if (other.brand != null)
				return false;
		} else if (!brand.equals(other.brand))
			return false;
		if (createdBy == null) {
			if (other.createdBy != null)
				return false;
		} else if (!createdBy.equals(other.createdBy))
			return false;
		if (model == null) {
			if (other.model != null)
				return false;
		} else if (!model.equals(other.model))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "DeviceModel [id=" + id + ", brand=" + brand + ", model=" + model + ", createdOn=" + createdOn
				+ ", createdBy=" + createdBy + ", name=" + name + ", features=" + features + "]";
	}

	public static class Builder {
    	
    	private String brand;
        private String model;
        private String createdBy;
        private String name;

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

		public Builder setName(String name) {
			this.name = name;
			
			return this;
		}
    	
		public DeviceModel build() {
			return new DeviceModel(this);
		}
    }
}
