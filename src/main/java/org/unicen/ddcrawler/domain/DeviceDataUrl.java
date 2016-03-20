package org.unicen.ddcrawler.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * 
 */
@Entity
public class DeviceDataUrl {

	@Id
	@GeneratedValue
	private Integer id;

	@Column(nullable = false)
	private final String modelUrl;

	public DeviceDataUrl(String modelUrl) {
		this.modelUrl = modelUrl;
	}

	public Integer getId() {
		return id;
	}

	public String getModelUrl() {
		return modelUrl;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((modelUrl == null) ? 0 : modelUrl.hashCode());
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
		DeviceDataUrl other = (DeviceDataUrl) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (modelUrl == null) {
			if (other.modelUrl != null)
				return false;
		} else if (!modelUrl.equals(other.modelUrl))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "DeviceDataUrl [id=" + id + ", modelUrl=" + modelUrl + "]";
	}
}
