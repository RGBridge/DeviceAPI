package org.rgbridge.deviceapi.entities;

import java.util.ArrayList;
import java.util.UUID;

public abstract class Device {
	private String deviceUuid;

	public Device() {
		this.deviceUuid = UUID.randomUUID().toString();
	}

	public abstract ArrayList<Effect> getEffects();
	public abstract String getName();

	public String getUUID() {
		return this.deviceUuid;
	}
}
