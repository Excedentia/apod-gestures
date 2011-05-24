package com.pyjioh.core;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.client.ClientProtocolException;

public class ApodModel {
	private static final String APOD_NASA_URL = "http://apod.nasa.gov/apod/";
	private static final String GET_IMAGE_REGEXP = "<[ \\s]*IMG[ \\s]*+SRC[ \\s]*=[ \\s]*\\\"([^\"]*.jpg|[^\"]*.jpeg|[^\"]*.gif|[^\"]*.png|[^\"]*.bmp)\\\"";

	private Calendar mCalendar = GregorianCalendar.getInstance();
	private DecimalFormat mFormat = new DecimalFormat("00");

	private static ApodModel instance;

	private ApodModel() {
	}

	public static synchronized ApodModel getInstance() {
		if (instance == null)
			instance = new ApodModel();
		return instance;
	}

	private String getApodURL() {
		int year = mCalendar.get(Calendar.YEAR) - 2000;
		if (year < 0)
			year += 100; // for 1996 year etc.
		int month = mCalendar.get(Calendar.MONTH) + 1;
		int day = mCalendar.get(Calendar.DAY_OF_MONTH);

		return APOD_NASA_URL + "ap" + mFormat.format(year) + mFormat.format(month)
				+ mFormat.format(day) + ".html";
	}

	public String getImageURL() {
		String imageLink = "";
		String websiteSource = "";
		try {
			websiteSource = WebManager.getPageSource(getApodURL());
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Pattern pattern = Pattern.compile(GET_IMAGE_REGEXP);
		Matcher matcher = pattern.matcher(websiteSource.toString());
		if (matcher.find())
			imageLink = matcher.group(1);

		return APOD_NASA_URL + imageLink;
	}

	public String getImageExplanation() {
		return "";
	}

	public void setDate(int year, int monthOfYear, int dayOfMonth) {
		mCalendar.set(Calendar.YEAR, year);
		mCalendar.set(Calendar.MONTH, monthOfYear);
		mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
	}

	public Calendar getCalendar() {
		return mCalendar;
	}

	public void incDay() {
		mCalendar.add(Calendar.DAY_OF_YEAR, 1);
	}

	public void decDay() {
		mCalendar.add(Calendar.DAY_OF_YEAR, -1);
	}

	public void today() {
		mCalendar = GregorianCalendar.getInstance();
	}

}
