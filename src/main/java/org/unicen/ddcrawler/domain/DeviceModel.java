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
    private final String url;

    @Column
    private String modelAlias;

    @JoinColumn
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<DeviceFeature> features;    
    
    private DeviceModel() {
        // ORM initialization
        
        this.id = null;
        this.brand = null;
        this.model = null;
        this.createdOn = null;
        this.url = null;
    }

    private DeviceModel(Builder builder) {

        Objects.requireNonNull(builder.brand, "Brand cannot be null");
        Objects.requireNonNull(builder.model, "Model cannot be null");
        Objects.requireNonNull(builder.url, "Url cannot be null");
        
        this.createdOn = new Date();
        
        this.brand = builder.brand;
        this.model = builder.model;
        this.url = builder.url;
        this.modelAlias = builder.modelAlias;
    }

    public void setModelAlias(String modelAlias) {
        this.modelAlias = modelAlias;
    }

    public void addFeatures(Set<DeviceFeature> features) {
    	
    	features.forEach(feature -> feature.setModel(this));
		
    	if(this.features == null){
			this.features = new HashSet<>();
		}
    	
    	this.features.addAll(features);
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
    
    public Optional<String> getModelAlias() {
        return Optional.ofNullable(modelAlias);
    }
        
	public Date getCreatedOn() {
		return createdOn;
	}

	public String getUrl() {
		return url;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((brand == null) ? 0 : brand.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		result = prime * result + ((model == null) ? 0 : model.hashCode());
		result = prime * result + ((modelAlias == null) ? 0 : modelAlias.hashCode());
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
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		if (model == null) {
			if (other.model != null)
				return false;
		} else if (!model.equals(other.model))
			return false;
		if (modelAlias == null) {
			if (other.modelAlias != null)
				return false;
		} else if (!modelAlias.equals(other.modelAlias))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "DeviceModel [id=" + id + ", brand=" + brand + ", model=" + model + ", createdOn=" + createdOn
				+ ", url=" + url + ", modelAlias=" + modelAlias + ", features=" + features + "]";
	}

	public static class Builder {
    	
    	private String brand;
        private String model;
        private String url;
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

		public Builder setUrl(String url) {
			
			Objects.requireNonNull(url);
			this.url = url;
			
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
