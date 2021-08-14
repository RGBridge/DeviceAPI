package org.rgbridge.deviceapi;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.rgbridge.deviceapi.entities.Device;
import org.rgbridge.deviceapi.entities.Effect;
import org.rgbridge.deviceapi.exceptions.WebSocketException;
import org.rgbridge.deviceapi.listeners.InitialisationCallback;
import org.rgbridge.deviceapi.listeners.RegistrationCallback;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;

public class DeviceAPI {
	private WebSocketClient webSocketClient;
	private HashMap<String, HashMap<String, Effect>> effects;

	public DeviceAPI() {
		effects = new HashMap<String, HashMap<String, Effect>>();
	}

	public void initialise(String pluginName, InitialisationCallback callback) {
		URI uri;

		try {
			uri = new URI("ws://localhost:32230/device");
		} catch(URISyntaxException e) {
			e.printStackTrace();
			return;
		}

		webSocketClient = new WebSocketClient(uri) {
			@Override
			public void onOpen(ServerHandshake serverHandshake) {
				callback.onInitialised();
			}

			@Override
			public void onMessage(String message) {
				try {
					JSONObject messageJson = new JSONObject(message);

					switch(messageJson.getString("method")) {
						case "EFFON":
							String deviceUuid = messageJson.getString("device");
							String effectUuid = messageJson.getString("effect");

							HashMap<String, Effect> deviceEffects = effects.get(deviceUuid);
							Effect referencedEffect = deviceEffects.get(effectUuid);

							referencedEffect.startEffect();
							break;
					}
				} catch(JSONException e) {}
			}

			@Override
			public void onClose(int i, String message, boolean b) {
				try {
					throw new WebSocketException(message);
				} catch(WebSocketException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onError(Exception e) {
				callback.onFailure("Failed to connect: " + e.getMessage());
			}
		};

		webSocketClient.connect();
	}

	public void registerDevice(Device device, RegistrationCallback callback) {
		ArrayList<Effect> deviceEffects = device.getEffects();
		JSONArray effectsArray = new JSONArray();

		for(Effect effect : deviceEffects) {
			JSONObject effectObject = new JSONObject();
			effectObject.put("name", effect.getEffectName());
			effectObject.put("uuid", effect.geEffectUUID());

			effectsArray.put(effectObject);
		}

		JSONObject deviceObj = new JSONObject();
		deviceObj.put("name", device.getName());
		deviceObj.put("uuid", device.getUUID());
		deviceObj.put("effects", effectsArray);

		JSONObject deviceJson = new JSONObject();
		deviceJson.put("method", "REG");
		deviceJson.put("device", deviceObj);

		webSocketClient.send(deviceJson.toString());

		HashMap<String, Effect> identifiedEffects = new HashMap<String, Effect>();

		for(Effect effect : deviceEffects) {
			identifiedEffects.put(effect.geEffectUUID(), effect);
		}

		effects.put(
				device.getUUID(),
				identifiedEffects
		);

		callback.onRegistered();
	}
}
