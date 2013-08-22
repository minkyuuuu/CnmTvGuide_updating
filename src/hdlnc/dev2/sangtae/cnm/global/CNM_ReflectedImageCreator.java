package hdlnc.dev2.sangtae.cnm.global;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.graphics.Shader.TileMode;

public class CNM_ReflectedImageCreator {

	
	public Bitmap createReflectedImages(Bitmap bitmap, int r) {
		// The gap we want between the reflection and the original image
		final int reflectionGap = 0;
		final int ratio = r;
		
		if(bitmap == null)
			return null;
		
		Bitmap originalImage = bitmap;

		int width = originalImage.getWidth();
		int height = originalImage.getHeight();

		// This will not scale but will flip on the Y axis
		Matrix matrix = new Matrix();
		matrix.preScale(1, -1);

		// Create a Bitmap with the flip matrix applied to it.
		// We only want the bottom half of the image
		Bitmap reflectionImage = Bitmap.createBitmap(originalImage, 0,
				height - height / ratio, width, height / ratio, matrix, false);

		// Create a new bitmap with same width but taller to fit reflection
		Bitmap bitmapWithReflection = Bitmap.createBitmap(width,
				(height + height / ratio), Config.ARGB_8888);

		// Create a new Canvas with the bitmap that's big enough for
		// the image plus gap plus reflection
		Canvas canvas = new Canvas(bitmapWithReflection);
		// Draw in the original image
		canvas.drawBitmap(originalImage, 0, 0, null);
		// Draw in the gap
		Paint deafaultPaint = new Paint();
		canvas
				.drawRect(0, height, width, height + reflectionGap,
						deafaultPaint);
		// Draw in the reflection
//		matrix.reset();
//		matrix.postScale(0, height * ratio);
//		matrix.postTranslate(0, height * ratio);
		canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);
		// canvas.drawBitmap(reflectionImage,matrix, null);

		// Create a shader that is a linear gradient that covers the reflection
		Paint paint = new Paint();
		LinearGradient shader = new LinearGradient(0,
				originalImage.getHeight(), 0, bitmapWithReflection.getHeight()
						+ reflectionGap, 0x70ffffff, 0x00ffffff, TileMode.CLAMP);
		// Set the paint to use this shader (linear gradient)
		paint.setShader(shader);
		// Set the Transfer mode to be porter duff and destination in
		paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
		// Draw a rectangle using the paint with our linear gradient
		canvas.drawRect(0, height, width, bitmapWithReflection.getHeight()
				+ reflectionGap, paint);
		
		originalImage.recycle();
		reflectionImage.recycle();
		bitmap.recycle();
		
		return bitmapWithReflection;
	}
}
