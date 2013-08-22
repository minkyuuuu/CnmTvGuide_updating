package hdlnc.dev2.sangtae.cnm.global;

import hdlnc.dev2.sangtae.cnm.R;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

public class CNM_SoundPool {
	private SoundPool mSoundPool;
	private AudioManager  mAudioManager;
	private int tak;
	private float mVolume=1f;
	public CNM_SoundPool(Context context) {
		// TODO Auto-generated constructor stub
		mSoundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
		tak=mSoundPool.load(context, R.raw.button_click_sound, 1);
		mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
	}

	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		super.finalize();
		mSoundPool.release();
	}

	private void soundEffectCheck() {
		// TODO Auto-generated method stub
		switch (mAudioManager.getRingerMode()) {
		case AudioManager.RINGER_MODE_NORMAL:
			mVolume=1f;
			break;
		case AudioManager.RINGER_MODE_SILENT:
			mVolume=0f;
			break;
		case AudioManager.RINGER_MODE_VIBRATE:
			mVolume=0f;
			break;
		default:
			break;
		}

	}

	public void play() {
		// TODO Auto-generated method stub
		Log.d("TEST","paly");
		soundEffectCheck();
		mSoundPool.play(tak, mVolume, mVolume, 0, 0, 1f);

		/*
		// 버튼 클릭음 콜스택 확인용 무조건 예외처리
		try {
			int c=1;	c = c/0;
		} catch(Exception e) {
			Log.d("-------------------------------------------------------","e = ");
			e.printStackTrace();
			Log.d("????????????????????????????????????????????????????????","e = ");
		}
		*/

	}
}
