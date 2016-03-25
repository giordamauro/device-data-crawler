package org.unicen.ddcrawler.gsupport;

public class AndroidDevice {

	private String retailBranding;
	private String marketingName;
	private String device;
	private String model;

	public String getRetailBranding() {
		return retailBranding;
	}

	public void setRetailBranding(String retailBranding) {
		this.retailBranding = retailBranding;
	}

	public String getMarketingName() {
		return marketingName;
	}

	public void setMarketingName(String marketingName) {
		this.marketingName = marketingName;
	}

	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	@Override
	public String toString() {
		return "AndroidDevice [retailBranding=" + retailBranding + ", marketingName=" + marketingName + ", device="
				+ device + ", model=" + model + "]";
	}
	
	
}
