package org.unicen.ddcrawler.firebase;

import java.util.Map;
import java.util.Objects;

public class FirebaseData {

	private final String urlId;
	
	private final Map<String, Object> data;

	public FirebaseData(String urlId, Map<String, Object> data) {

		Objects.requireNonNull(urlId, "UrlId cannot be null");
		Objects.requireNonNull(data, "Data cannot be null");		

		this.urlId = urlId;
		this.data = data;
	}

	public String getUrlId() {
		return urlId;
	}

	public Map<String, Object> getData() {
		return data;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result + ((urlId == null) ? 0 : urlId.hashCode());
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
		FirebaseData other = (FirebaseData) obj;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		if (urlId == null) {
			if (other.urlId != null)
				return false;
		} else if (!urlId.equals(other.urlId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "FirebaseData [urlId=" + urlId + ", data=" + data + "]";
	}
}
