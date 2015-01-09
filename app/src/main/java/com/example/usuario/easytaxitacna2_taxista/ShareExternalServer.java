package com.example.usuario.easytaxitacna2_taxista;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ShareExternalServer {

	public String shareRegIdWithAppServer(final Context context,
			final String regId) {

        JSONObject jsonSubscribe = new JSONObject();

        try {
            jsonSubscribe.put("user","taxista");
            jsonSubscribe.put("type","android");
            jsonSubscribe.put("token",regId);
        }catch (Exception e)
        {
            Log.e("JSONError","Error en envio de JSON para suscribir");
        }

        String result = "";

		try {
			URL serverUrl = null;
			try {
				serverUrl = new URL(Config.APP_SERVER_URL);
			} catch (MalformedURLException e) {
				Log.e("AppUtil", "URL Connection Error: "
						+ Config.APP_SERVER_URL, e);
				result = "Invalid URL: " + Config.APP_SERVER_URL;
			}

            byte[] bytes =  jsonSubscribe.toString().getBytes();
			HttpURLConnection httpCon = null;
			try {
				httpCon = (HttpURLConnection) serverUrl.openConnection();
				httpCon.setDoOutput(true);
				httpCon.setUseCaches(false);
				httpCon.setFixedLengthStreamingMode(bytes.length);
				httpCon.setRequestMethod("POST");
				httpCon.setRequestProperty("Content-Type","application/json");
                httpCon.connect();

                OutputStream out = httpCon.getOutputStream();
				out.write(bytes);
				out.close();

				int status = httpCon.getResponseCode();
				if (status == httpCon.HTTP_OK) {
					result = "RegId shared with Application Server. RegId: "
							+ regId;
				} else {
					result = "Post Failure." + " Status: " + status;
				}
			} finally {
				if (httpCon != null) {
					httpCon.disconnect();
				}
			}

		} catch (IOException e) {
			result = "Post Failure. Error in sharing with App Server.";
			Log.e("AppUtil", "Error in sharing with App Server: " + e);
		}
		return result;
	}
}
