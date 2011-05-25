package com.pyjioh.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.pyjioh.R;
import com.pyjioh.core.ApodModel;
import com.pyjioh.core.Gestures;
import com.pyjioh.core.ImageDownloader;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;

public class ApodGestures extends Activity implements
		OnGesturePerformedListener {

	private GestureLibrary mGestureLib;
	private ApodModel mApodModel = ApodModel.getInstance();
	private ImageDownloader mImageDownloader = new ImageDownloader();

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		mGestureLib = GestureLibraries.fromRawResource(this, R.raw.gestures);
		if (!mGestureLib.load()) {
			finish();
		}

		GestureOverlayView gestures = (GestureOverlayView) findViewById(R.id.gestures);
		gestures.addOnGesturePerformedListener(this);

		refreshActivity();
	}

	public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
		ArrayList<Prediction> predictions = mGestureLib.recognize(gesture);
		if (predictions.size() > 0) {
			Prediction prediction = predictions.get(0);
			if (prediction.score > 1.0) {
				if (prediction.name.equals(Gestures.prev))
					showPrevDay();
				else if (prediction.name.equals(Gestures.next))
					showNextDay();
				else if (prediction.name.equals(Gestures.date))
					showSelectDateDialog();
			}
		}
	}

	private String formatDate(Date date) {
		return new SimpleDateFormat("MMM dd, yyyy").format(date);
	}

	private void refreshActivity() {
		Button buttonDate = (Button) findViewById(R.id.button_select_date);
		buttonDate.setText(formatDate(mApodModel.getCalendar().getTime()));

		ImageView imageView = (ImageView) findViewById(R.id.ImageView);
		mImageDownloader.download(imageView);
	}

	public void showNextDay() {
		mApodModel.incDay();
		refreshActivity();
	}

	public void showPrevDay() {
		mApodModel.decDay();
		refreshActivity();
	}

	public void showSelectDateDialog() {
		Calendar calendar = mApodModel.getCalendar();
		
		OnDateSetListener dateSetListener = new OnDateSetListener() {
			
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				mApodModel.setDate(year, monthOfYear, dayOfMonth);
				refreshActivity();
			};
		};
		
		new DatePickerDialog(this, dateSetListener,
				calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH)).show();
	}

}