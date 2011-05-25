package com.pyjioh.core;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;

import android.net.http.AndroidHttpClient;

public class WebManager {

	public static String getPageSource(String url) {
		StringBuilder source = new StringBuilder();

		AndroidHttpClient client = AndroidHttpClient.newInstance("Android");
		HttpGet request = new HttpGet(url);
		HttpResponse response;
		try {
			response = client.execute(request);
			InputStream in;
			in = response.getEntity().getContent();
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(in));
			String line = null;
			while ((line = reader.readLine()) != null)
				source.append(line);

			in.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return source.toString();
	}

}
