package org.unicen.ddcrawler.domain;

import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class DeviceFeature {

    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private DeviceModel device;
    
    @Column(nullable = false)
    private final String category;

    @Column(nullable = false)
    private final String name;

    @Column(nullable = false)
    private final String value;
    
    
    @Column(nullable = false)
    private final Date createdOn;
    
    @Column
    private String createdBy;

    
    @SuppressWarnings("unused")
	private DeviceFeature() {
    
    	this.category = null;
    	this.name = null;
    	this.value = null;
    	this.createdOn = null;
    }
    
    public DeviceFeature(String category, String name, String value) {

        Objects.requireNonNull(category, "Category cannot be null");
        Objects.requireNonNull(name, "Name cannot be null");
        Objects.requireNonNull(value, "Value cannot be null");
        
        this.createdOn = new Date();
        this.category = category;
        this.name = name;
        this.value = value;
    }

    public Integer getId() {
		return id;
	}

	public DeviceModel getDevice() {
		return device;
	}

	public String getCategory() {
		return category;
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setModel(DeviceModel model) {
        
    	Objects.requireNonNull(model, "DeviceModel cannot be null");
        this.device = model;
    }

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((category == null) ? 0 : category.hashCode());
		result = prime * result + ((createdBy == null) ? 0 : createdBy.hashCode());
		result = prime * result + ((device == null) ? 0 : device.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
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
		if (category == null) {
			if (other.category != null)
				return false;
		} else if (!category.equals(other.category))
			return false;
		if (createdBy == null) {
			if (other.createdBy != null)
				return false;
		} else if (!createdBy.equals(other.createdBy))
			return false;
		if (device == null) {
			if (other.device != null)
				return false;
		} else if (!device.equals(other.device))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "DeviceFeature [id=" + id + ", category=" + category + ", name=" + name + ", value=" + value
				+ ", createdOn=" + createdOn + ", createdBy=" + createdBy + "]";
	}
}
