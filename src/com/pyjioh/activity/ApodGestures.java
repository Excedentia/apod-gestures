package com.pyjioh.activity;

import java.util.ArrayList;

import com.pyjioh.R;
import com.pyjioh.core.Gestures;

import android.app.Activity;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.os.Bundle;

public class ApodGestures extends Activity implements OnGesturePerformedListener{
	
	private GestureLibrary mGestureLib;
	
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
    }

	public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
	    ArrayList<Prediction> predictions = mGestureLib.recognize(gesture);
	    if (predictions.size() > 0) {
	        Prediction prediction = predictions.get(0);
	        if (prediction.score > 1.0) {
	        	if (prediction.name.equals(Gestures.prev));
//	        		mApodController.showPrevDay();
	        	else if (prediction.name.equals(Gestures.next));
//	        		mApodController.showNextDay();
	        	else if (prediction.name.equals(Gestures.date));
//	        		mApodController.selectDate();
	        }
	    }
	}
}