package hdlnc.dev2.sangtae.cnm.tvchannel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Debug;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

/**
 * This helper class download images from the Internet and binds those with the provided ImageView.
 *
 * <p>It requires the INTERNET permission, which should be added to your application's manifest
 * file.</p>
 *
 * A local cache of downloaded images is maintained internally to improve performance.
 */
public class TVCHImageDownloader {
	private static final String LOG_TAG = "ImageDownloader";
	private static Bitmap mLoadingImage;

	public enum Mode { NO_ASYNC_TASK, NO_DOWNLOADED_DRAWABLE, CORRECT }
	private Mode mode = Mode.CORRECT;
	//private static int ThreadCounter=0;
	private static final String DEBUG_TAG = "TVCHImageDownloader";
	private static final String PACKGE_NAME = "hdlnc.dev2.sangtae.cnm/cache";

	private static Context mContext = null;

	private BitmapFactory.Options options = new BitmapFactory.Options();
	private File mkDir = null;
	
	
	public TVCHImageDownloader(Context mContext) {
		// TODO Auto-generated constructor stub
		TVCHImageDownloader.mContext = mContext;
		options.inPreferredConfig = Bitmap.Config.ARGB_4444;
		options.inPurgeable = true;
		
		if (mContext != null) {
			mkDir = mContext.getExternalCacheDir();
		}			
		if (mkDir == null) {
			mkDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), PACKGE_NAME);
		}

		if (!mkDir.exists())
			mkDir.mkdirs();
	}
	public void setContext(Context mContext) {
		TVCHImageDownloader.mContext = mContext;
		options.inPreferredConfig = Bitmap.Config.ARGB_4444;
		options.inPurgeable = true;
	}

	/**
	 * Download the specified image from the Internet and binds it to the provided ImageView. The
	 * binding is immediate if the image is found in the cache and will be done asynchronously
	 * otherwise. A null bitmap will be associated to the ImageView if an error occurs.
	 *
	 * @param url The URL of the image to download.
	 * @param imageView The ImageView to bind the downloaded image to.
	 */
	public void download(String url, ImageView imageView) {
		resetPurgeTimer();
		mLoadingImage=null;
		Bitmap bitmap = getBitmapFromCache(url);

		if(url != null)
		if (bitmap == null) {
			if (cancelPotentialDownload(url, imageView)) {
				BitmapDownloaderTask task = new BitmapDownloaderTask(imageView);
				DownloadedDrawable downloadedDrawable = new DownloadedDrawable(
						task);
				imageView.setImageDrawable(downloadedDrawable);
				task.execute(url); // 작업 실행.
			}
		} else {
			cancelPotentialDownload(url, imageView);
			imageView.setImageBitmap(bitmap);
			//imageView.setImageDrawable(new BitmapDrawable(bitmap));
			//bitmap.recycle();
			
		}

		/*
		resetPurgeTimer();
		Bitmap bitmap = getBitmapFromCache(url);

		if (bitmap == null) {
			forceDownload(url, imageView);
		} else {
			cancelPotentialDownload(url, imageView);
			imageView.setImageBitmap(bitmap);
		}
		 */
	}

	public void download(String url, ImageView imageView, Bitmap lodingImage) {
		mLoadingImage=lodingImage;

		resetPurgeTimer();
		Bitmap bitmap = getBitmapFromCache (url);
		if(url != null)
		if (bitmap == null) {
			if (cancelPotentialDownload(url, imageView)) {
				BitmapDownloaderTask task = new BitmapDownloaderTask(imageView);
				DownloadedDrawable downloadedDrawable = new DownloadedDrawable(
						task);
				downloadedDrawable.setAntiAlias(true);
				imageView.setImageDrawable(downloadedDrawable);
				task.execute(url); // 작업 실행.
			}
		} else {
			cancelPotentialDownload(url, imageView);
			imageView.setImageBitmap(bitmap);
			//imageView.setImageDrawable(new BitmapDrawable(bitmap));
			//bitmap.recycle();
		}

		/*
		if (cancelPotentialDownload(url, imageView)) {
			BitmapDownloaderTask task = new BitmapDownloaderTask(imageView);
			DownloadedDrawable downloadedDrawable = new DownloadedDrawable(task);
			imageView.setImageDrawable(downloadedDrawable);
			task.execute(url);			// 작업 실행.
		}
		 */
	}

	/*
	 * Same as download but the image is always downloaded and the cache is not used.
	 * Kept private at the moment as its interest is not clear.
       private void forceDownload(String url, ImageView view) {
          forceDownload(url, view, null);
       }
	 */

	/**
	 * Same as download but the image is always downloaded and the cache is not used.
	 * Kept private at the moment as its interest is not clear.
	 */
	private void forceDownload(String url, ImageView imageView) {
		// State sanity: url is guaranteed to never be null in DownloadedDrawable and cache keys.
		if (url == null) {
			//imageView.setImageBitmap(null);
			imageView.setImageDrawable(null);
			return;
		}

		if (cancelPotentialDownload(url, imageView)) {
			switch (mode) {
			case NO_ASYNC_TASK:
				Bitmap bitmap = downloadBitmap(url);
				addBitmapToCache(url, bitmap);
				imageView.setImageBitmap(bitmap);
				break;

			case NO_DOWNLOADED_DRAWABLE:
				imageView.setMinimumHeight(156);
				BitmapDownloaderTask task = new BitmapDownloaderTask(imageView);
				task.execute(url);
				break;

			case CORRECT:
				task = new BitmapDownloaderTask(imageView);
				DownloadedDrawable downloadedDrawable = new DownloadedDrawable(task);
				imageView.setImageDrawable(downloadedDrawable);
				imageView.setMinimumHeight(156);
				task.execute(url);
				break;
			}
		}
	}

	/**
	 * Returns true if the current download has been canceled or if there was no download in
	 * progress on this image view.
	 * Returns false if the download in progress deals with the same url. The download is not
	 * stopped in that case.
	 */
	private static boolean cancelPotentialDownload(String url, ImageView imageView) {
		BitmapDownloaderTask bitmapDownloaderTask = getBitmapDownloaderTask(imageView);

		if (bitmapDownloaderTask != null) {
			String bitmapUrl = bitmapDownloaderTask.url;
			if ((bitmapUrl == null) || (!bitmapUrl.equals(url))) {
				bitmapDownloaderTask.cancel(true);
			} else {
				// The same URL is already being downloaded.
				return false;
			}
		}
		return true;
	}

	/**
	 * @param imageView Any imageView
	 * @return Retrieve the currently active download task (if any) associated with this imageView.
	 * null if there is no such task.
	 */
	private static BitmapDownloaderTask getBitmapDownloaderTask(ImageView imageView) {
		if (imageView != null) {
			Drawable drawable = imageView.getDrawable();
			if (drawable instanceof DownloadedDrawable) {
				DownloadedDrawable downloadedDrawable = (DownloadedDrawable)drawable;
				return downloadedDrawable.getBitmapDownloaderTask();
			}
		}
		return null;
	}

	Bitmap downloadBitmap(String url) {
		final HttpClient client = AndroidHttpClient.newInstance("Android"); // 아파치
		// 클라이언트
		// 생성.
		final HttpGet getRequest = new HttpGet(url);

		try {
			HttpResponse response = client.execute(getRequest); // 결과를 받아옴.
			final int statusCode = response.getStatusLine().getStatusCode(); // 결과
			// 상태
			// 코드를
			// 가져온다.
			if (statusCode != HttpStatus.SC_OK) { // 200 OK (HTTP/1.0 - RFC
				// 1945) 확인한다.
				Log.w(DEBUG_TAG, "Error " + statusCode
						+ " while retrieving bitmap from " + url); // 수신 실패
				return null;
			}

			final HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream inputStream = null;
				try {
					inputStream = entity.getContent(); // respone에서 inputStream을
					// 받아 가져온다.
					//return BitmapFactory.decodeStream(inputStream);
					// Bug on slow connections, fixed in future release.
					return BitmapFactory.decodeStream(new FlushedInputStream(inputStream));

				} finally { // 종료 작업
					if (inputStream != null) {
						inputStream.close();
					}
					entity.consumeContent();
				}
			}
		} catch (Exception e) {
			// Could provide a more explicit error message for IOException or
			// IllegalStateException
			getRequest.abort(); // Request 작업 진행을 중단한다.
			Log.w(DEBUG_TAG, "Error while retrieving bitmap from "
					+ url + e.toString());
		} finally {
			if ((client instanceof AndroidHttpClient)) { // connection 종료.
				((AndroidHttpClient) client).close();
			}
		}
		return null;
		/*
		final int IO_BUFFER_SIZE = 4 * 1024;

		// AndroidHttpClient is not allowed to be used from the main thread
		final HttpClient client = (mode == Mode.NO_ASYNC_TASK) ? new DefaultHttpClient() :
			AndroidHttpClient.newInstance("Android");
		final HttpGet getRequest = new HttpGet(url);

		try {
			HttpResponse response = client.execute(getRequest);
			final int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				Log.w("ImageDownloader", "Error " + statusCode +
						" while retrieving bitmap from " + url);
				return null;
			}

			final HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream inputStream = null;
				try {
					inputStream = entity.getContent();
					//return BitmapFactory.decodeStream(inputStream);
					// Bug on slow connections, fixed in future release.
					return BitmapFactory.decodeStream(new FlushedInputStream(inputStream));
				} finally {
					if (inputStream != null) {
						inputStream.close();
					}
					entity.consumeContent();
				}
			}
		} catch (IOException e) {
			getRequest.abort();
			Log.w(LOG_TAG, "I/O error while retrieving bitmap from " + url, e);
		} catch (IllegalStateException e) {
			getRequest.abort();
			Log.w(LOG_TAG, "Incorrect URL: " + url);
		} catch (Exception e) {
			getRequest.abort();
			Log.w(LOG_TAG, "Error while retrieving bitmap from " + url, e);
		} finally {
			if ((client instanceof AndroidHttpClient)) {
				((AndroidHttpClient) client).close();
			}
		}
		return null;
		 */
	}

	/*
	 * An InputStream that skips the exact number of bytes provided, unless it reaches EOF.
	 */
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

	/*private Bitmap LoadExternalCache(File aFile){

		try {
			return BitmapFactory.decodeFile(aFile.getCanonicalPath(), options);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
*/
	/**
	 * The actual AsyncTask that will asynchronously download the image.
	 */
	class BitmapDownloaderTask extends AsyncTask<String, Void, Bitmap> {
		private String url;
		private String fileName;
		private final WeakReference<ImageView> imageViewReference;

		public BitmapDownloaderTask(ImageView imageView) {
			if(imageView !=null)
				imageViewReference = new WeakReference<ImageView>(imageView);
			else
				imageViewReference = null;
		}

		/**
		 * Actual download method.
		 */
		@Override
		protected Bitmap doInBackground(String... params) {
			//Log.d("Sangtae", "image: "+params[0]);
			//			url = params[0];
			//			return downloadBitmap(url);
			url = params[0];
			int lastPos = url.lastIndexOf("/");
			int dotPos = url.length();//lastIndexOf(".");
			fileName = url.substring(lastPos, dotPos);

			//File mkDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), PACKGE_NAME);
			
			File file = new File(mkDir.toString(), fileName);
			if (!file.exists()) {
				return downloadBitmap(url);
			}else {
				if (file != null) {
					try {
						return BitmapFactory.decodeFile(file.getCanonicalPath(), options);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

			return BitmapFactory.decodeFile(file.toString(), options);
		}

		/**
		 * Once the image is downloaded, associates it to the imageView
		 */
		@Override
		protected void onPostExecute(Bitmap bitmap) {
			if (isCancelled()) {
				bitmap = null;
			}

			if (bitmap != null) {
				addBitmapToCache(url, bitmap);
				PictureSaveToBitmapFile(bitmap,fileName);
				if (imageViewReference != null) {
					ImageView imageView = imageViewReference.get();
					BitmapDownloaderTask bitmapDownloaderTask = getBitmapDownloaderTask(imageView);
					if (this == bitmapDownloaderTask) {
						if (imageView.getDrawable() != null) {
							if (((BitmapDrawable)imageView.getDrawable()).getBitmap() != null) {
								if ( mLoadingImage != ((BitmapDrawable)imageView.getDrawable()).getBitmap() )
									synchronized (sHardBitmapCache) {
										if (!sHardBitmapCache.containsKey(url)) {
											Log.d("Sangtae", "ImageView Recycle");
											((BitmapDrawable)imageView.getDrawable()).getBitmap().recycle();
										}
									}
								else
									Log.d("KGT_ImageDownload", "defaut image is not recycled."+ Debug.getNativeHeapSize());									
								//imageView.setImageBitmap(null);
							}
							Log.d("KGT_ImageDownload", "onPostExecute() Bitmap Heap recycle: "+ Debug.getNativeHeapSize());
						}
						
						imageView.setImageBitmap(bitmap);
						//imageView.setImageDrawable(new BitmapDrawable(bitmap));
						//bitmap.recycle();
					}
				}
			}
			/*
			if (isCancelled()) {
				bitmap = null;
			}

			addBitmapToCache(url, bitmap);

			if (imageViewReference != null) {
				ImageView imageView = imageViewReference.get();
				BitmapDownloaderTask bitmapDownloaderTask = getBitmapDownloaderTask(imageView);
				// Change bitmap only if this process is still associated with it
				// Or if we don't use any bitmap to task association (NO_DOWNLOADED_DRAWABLE mode)
				if ((this == bitmapDownloaderTask) || (mode != Mode.CORRECT)) {
					//Log.d("Sangtae", "BitMap: "+bitmap);
					if (bitmap != null) {
						try {
							imageView.setImageBitmap(bitmap);	
						} catch (NullPointerException e) {
							// TODO: handle exception
						}

					}
					ThreadCounter--;
				}
			}
			 */
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
		}
	}

	/**
	 * A fake Drawable that will be attached to the imageView while the download is in progress.
	 *
	 * <p>Contains a reference to the actual download task, so that a download task can be stopped
	 * if a new binding is required, and makes sure that only the last started download process can
	 * bind its result, independently of the download finish order.</p>
	 */
	static class DownloadedDrawable extends BitmapDrawable {
		private final WeakReference<BitmapDownloaderTask> bitmapDownloaderTaskReference;

		public DownloadedDrawable(BitmapDownloaderTask bitmapDownloaderTask) {
			//super(Color.BLACK);
			super(mLoadingImage);

			bitmapDownloaderTaskReference =
				new WeakReference<BitmapDownloaderTask>(bitmapDownloaderTask);
		}

		public BitmapDownloaderTask getBitmapDownloaderTask() {
			return bitmapDownloaderTaskReference.get();
		}
	}

	public void setMode(Mode mode) {
		this.mode = mode;
		clearCache();
	}


	/*
	 * Cache-related fields and methods.
	 * 
	 * We use a hard and a soft cache. A soft reference cache is too aggressively cleared by the
	 * Garbage Collector.
	 */

	private static final int HARD_CACHE_CAPACITY = 10;
	private static final int DELAY_BEFORE_PURGE = 10 * 1000; // in milliseconds

	// Hard cache, with a fixed maximum capacity and a life duration
	private final HashMap<String, Bitmap> sHardBitmapCache =
		new LinkedHashMap<String, Bitmap>(HARD_CACHE_CAPACITY / 2, 0.75f, true) {
		/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

		@Override
		protected boolean removeEldestEntry(LinkedHashMap.Entry<String, Bitmap> eldest) {
			if (size() > HARD_CACHE_CAPACITY) {
				// Entries push-out of hard reference cache are transferred to soft reference cache
//				sSoftBitmapCache.put(eldest.getKey(), new SoftReference<Bitmap>(eldest.getValue()));
				return true;
			} else
				return false;
		}
	};

	// Soft cache for bitmaps kicked out of hard cache
//	private final static ConcurrentHashMap<String, SoftReference<Bitmap>> sSoftBitmapCache =
//		new ConcurrentHashMap<String, SoftReference<Bitmap>>(HARD_CACHE_CAPACITY / 2);

	private final Handler purgeHandler = new Handler();

	private final Runnable purger = new Runnable() {
		public void run() {
			clearCache();
		}
	};

	/**
	 * Adds this bitmap to the cache.
	 * @param bitmap The newly downloaded bitmap.
	 */
	private void addBitmapToCache(String url, Bitmap bitmap) {
		if (bitmap != null) {
			synchronized (sHardBitmapCache) {
				sHardBitmapCache.put(url, bitmap);
			}
		}
	}

	/**
	 * @param url The URL of the image that will be retrieved from the cache.
	 * @return The cached bitmap or null if it was not found.
	 */
	private Bitmap getBitmapFromCache(String url) {
		// First try the hard reference cache
		synchronized (sHardBitmapCache) {
			final Bitmap bitmap = sHardBitmapCache.get(url);
			if (bitmap != null) {
				// Bitmap found in hard cache
				// Move element to first position, so that it is removed last
				sHardBitmapCache.remove(url);
				//sHardBitmapCache.get(url).recycle();
				sHardBitmapCache.put(url, bitmap);
				return bitmap;
			}
		}

		// Then try the soft reference cache
/*		SoftReference<Bitmap> bitmapReference = sSoftBitmapCache.get(url);
		if (bitmapReference != null) {
			final Bitmap bitmap = bitmapReference.get();
			if (bitmap != null) {
				// Bitmap found in soft cache
				return bitmap;
			} else {
				// Soft reference has been Garbage Collected
				//sSoftBitmapCache.get(url).get().recycle();
				sSoftBitmapCache.remove(url);
			}
		}
*/
		return null;
	}

	/**
	 * Clears the image cache used internally to improve performance. Note that for memory
	 * efficiency reasons, the cache will automatically be cleared after a certain inactivity delay.
	 */
	public void clearCache() {
		sHardBitmapCache.clear();
//		sSoftBitmapCache.clear();
	}

	/**
	 * Allow a new delay before the automatic cache clear is done.
	 */
	private void resetPurgeTimer() {
		purgeHandler.removeCallbacks(purger);
		purgeHandler.postDelayed(purger, DELAY_BEFORE_PURGE);
	}

	public static boolean PictureSaveToBitmapFile(Bitmap bmp, String fn )
	{
		if(fn == null || fn.length() < 1 )
			return false;

		//	      if( bmp == null )
		//	          return true;

		//	      File mkDir = new File(Environment.getDownloadCacheDirectory().getAbsolutePath(), PACKGE_NAME);
		//File mkDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), PACKGE_NAME);
		File mkDir = null;
		if (mContext != null) {
			mkDir = mContext.getExternalCacheDir();
		}			
		if (mkDir == null) {
			mkDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), PACKGE_NAME);
		}

		if(!mkDir.exists())
			mkDir.mkdirs();

		File file = new File(mkDir.toString(), fn);
		if( file.exists() ) {
			Log.d(DEBUG_TAG, "Use cached file -"+file.toString());
			if (file.length() > 0) {
				return true;
			} else {
				try {
					Log.d(DEBUG_TAG, "Download image bitmap: "+bmp);
					FileOutputStream fOut = new FileOutputStream(file);
					bmp.compress(Bitmap.CompressFormat.PNG, 50, fOut);
					fOut.flush();
					fOut.close();

					return true;

				} catch (FileNotFoundException e) {
					Log.e(DEBUG_TAG, e.getMessage());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				return false;
			}

		}

		try {
			FileOutputStream fOut = new FileOutputStream(file);
			bmp.compress(Bitmap.CompressFormat.PNG, 50, fOut);
			fOut.flush();
			fOut.close();

			return true;

		} catch (FileNotFoundException e) {
			Log.e(DEBUG_TAG, e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
}
