package org.rgbridge.deviceapi.listeners;

public interface RegistrationCallback {
	public void onRegistered();
	public void onFailure(String message);
}
