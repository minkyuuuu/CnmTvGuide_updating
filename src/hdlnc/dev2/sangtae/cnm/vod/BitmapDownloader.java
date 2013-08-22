package hdlnc.dev2.sangtae.cnm.vod;


import hdlnc.dev2.sangtae.cnm.vod.CoverflowImageDownloader.BitmapDownloaderTask;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

public class BitmapDownloader {
	
	Bitmap downloadBitmap;
	
	public void BitmapDownloadStart(String url, Bitmap bitmap)
	{
		downloadBitmap = bitmap;
		new BitmapDownloadTask().execute(url);
	}
	
	public static Bitmap downloadBitmap(String url) {		

		final HttpClient client = AndroidHttpClient.newInstance("Android");		// ����ġ Ŭ���̾�Ʈ ����.
		final HttpGet getRequest = new HttpGet(url);

		try {
			HttpResponse response = client.execute(getRequest);					// ����� �޾ƿ�.
			final int statusCode = response.getStatusLine().getStatusCode();	// ��� ���� �ڵ带 �����´�.
			if (statusCode != HttpStatus.SC_OK) { 								// 200 OK (HTTP/1.0 - RFC 1945) Ȯ���Ѵ�.
				Log.w("ImageDownloader", "Error " + statusCode + " while retrieving bitmap from " + url); 		// ���� ����
				return null;
			}

			final HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream inputStream = null;
				try {
					inputStream = entity.getContent();		// respone���� inputStream�� �޾� �����´�. 
					final Bitmap bitmap = BitmapFactory.decodeStream(new FlushedInputStream(inputStream));		// inputStream�� Bitmap���� ��ȯ.
					return bitmap;
				} finally {			// ���� �۾�
					if (inputStream != null) {
						inputStream.close();  
					}
					entity.consumeContent();
				}
			}
		} catch (Exception e) {
			// Could provide a more explicit error message for IOException or IllegalStateException
			getRequest.abort();			// Request �۾� ������ �ߴ��Ѵ�.
			Log.w("ImageDownloader", "Error while retrieving bitmap from " + url + e.toString());
		} finally {
			if ((client instanceof AndroidHttpClient)) {			// connection ����.
				((AndroidHttpClient) client).close();
			}
		}
		return null;

	}

	static class FlushedInputStream extends FilterInputStream {
		public FlushedInputStream(InputStream inputStream) {
			super(inputStream);
		}

		@Override
		public long skip(long n) throws IOException {
			long totalBytesSkipped = 0L;
			while (totalBytesSkipped < n) {
				long bytesSkipped = in.skip(n - totalBytesSkipped);
				if (bytesSkipped == 0L) {
					int b = read();
					if (b < 0) {
						break;  // we reached EOF
					} else {
						bytesSkipped = 1; // we read one byte
					}
				}
				totalBytesSkipped += bytesSkipped;
			}
			return totalBytesSkipped;
		}
	}
	
	class BitmapDownloadTask extends AsyncTask<String, Void, Bitmap>
	{
		@Override
		protected Bitmap doInBackground(String... params) {
			// TODO Auto-generated method stub
			return downloadBitmap(params[0]);
		}
		@Override
		protected void onPostExecute(Bitmap result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			downloadBitmap = result;
		}
	}
	
}
