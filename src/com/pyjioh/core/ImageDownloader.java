package com.pyjioh.core;

import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;

import com.pyjioh.R;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

/*
 * ImageDownloader using for async download and set images
 */

public class ImageDownloader {
	public void download(String url, ImageView imageView) {
		BitmapDownloaderTask task = new BitmapDownloaderTask(imageView);
		task.execute(url);
	}
}

class BitmapDownloaderTask extends AsyncTask<String, Void, Bitmap> {
	private static final String LOG_TAG = "ImageDownloader";
	
	private ProgressDialog mProgressDlg;
	private ImageView mImageView;
	private Bitmap mBitmap;

	public BitmapDownloaderTask(ImageView imageView) {
		mImageView = imageView;
	}

	private Bitmap downloadBitmap(String url) {
		final AndroidHttpClient client = AndroidHttpClient.newInstance("Android");
		final HttpGet getRequest = new HttpGet(url);

		try {
			HttpResponse response = client.execute(getRequest);
			final int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				Log.w(LOG_TAG, "Error " + statusCode + " while retrieving bitmap from " + url);
				return null;
			}

			final HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream inputStream = null;
				try {
					inputStream = entity.getContent();
					final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
					return bitmap;
				} finally {
					if (inputStream != null) {
						inputStream.close();
					}
					entity.consumeContent();
				}
			}
		} catch (Exception e) {
			getRequest.abort();
			Log.w(LOG_TAG, "Error while retrieving bitmap from " + url, e);
		} finally {
			if (client != null) {
				client.close();
			}
		}
		return null;
	}

	@Override
	protected void onPreExecute() {
		mProgressDlg = ProgressDialog.show(mImageView.getContext(), null,
				mImageView.getContext().getText(R.string.msg_loading));
		mProgressDlg.setIndeterminate(true);
		mProgressDlg.setCancelable(true);
		mProgressDlg.show();
	}

	@Override
	protected Bitmap doInBackground(String... params) {
		// params comes from the execute() call: params[0] is the url.
		mBitmap = downloadBitmap(params[0]);
		return mBitmap;
	}

	@Override
	protected void onPostExecute(Bitmap bitmap) {
		if (!isCancelled()) 
			mImageView.setImageBitmap(bitmap);
		mProgressDlg.dismiss();
	}

}
