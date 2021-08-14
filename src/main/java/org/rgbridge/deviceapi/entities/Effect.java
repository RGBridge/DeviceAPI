package org.rgbridge.deviceapi.entities;

import java.util.UUID;

public abstract class Effect {
	private String effectUuid;

	public Effect() {
		this.effectUuid = UUID.randomUUID().toString();
	}

	public abstract String getEffectName();

	public abstract Thread setupEffect();

	public void startEffect() {
		setupEffect().start();
	}

	public void stopEffect() {
		setupEffect().stop();
	}

	public String geEffectUUID() {
		return this.effectUuid;
	}
}
