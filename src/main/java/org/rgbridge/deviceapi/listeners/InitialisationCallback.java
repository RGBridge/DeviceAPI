package org.rgbridge.deviceapi.listeners;

public interface InitialisationCallback {
	public void onInitialised();
	public void onFailure(String message);
}
