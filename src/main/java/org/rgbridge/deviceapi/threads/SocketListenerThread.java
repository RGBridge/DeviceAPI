package org.rgbridge.deviceapi.threads;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketListenerThread extends Thread {
	private Socket socket;
	private PrintWriter printWriter;

	public SocketListenerThread(Socket socket) {
		try {
			this.socket = socket;
			this.printWriter = new PrintWriter(socket.getOutputStream(), true);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			String inputLine;
			while((inputLine = reader.readLine()) != null) {
				System.out.println(inputLine);

				try {
					JSONObject objectRaw = new JSONObject(inputLine);

					switch(objectRaw.getString("method")) {
						case "EFFON":

							break;
					}
				} catch(JSONException e) {
					JSONObject errorObj = new JSONObject();
					errorObj.put("status", 400);
					errorObj.put("message", "Bad request");

					printWriter.println(errorObj);
				}
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}
