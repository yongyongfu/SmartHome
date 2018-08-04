package com.demo.smarthome.zxing.demo;

import java.io.IOException;
import java.util.Vector;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.widget.EditText;
import android.widget.Toast;

import com.demo.smarthome.activity.RegisterActivity;
import com.demo.smarthome.activity.ScanActivity;
import com.demo.smarthome.service.Cfg;
import com.demo.smarthome.zxing.camera.CameraManager;
import com.demo.smarthome.zxing.decoding.CaptureActivityHandler;
import com.demo.smarthome.zxing.decoding.InactivityTimer;
import com.demo.smarthome.zxing.view.ViewfinderView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.demo.smarthome.R;
//import com.zijunlin.Zxing.Demo.R.id;
//import com.zijunlin.Zxing.Demo.R.layout;
//import com.zijunlin.Zxing.Demo.R.raw;

public class CaptureActivity extends Activity implements Callback {

	private CaptureActivityHandler handler;
	private ViewfinderView viewfinderView;
	private boolean hasSurface;
	private Vector<BarcodeFormat> decodeFormats;
	private String characterSet;
	private InactivityTimer inactivityTimer;
	private MediaPlayer mediaPlayer;
	private boolean playBeep;
	private static final float BEEP_VOLUME = 0.10f;
	private boolean vibrate;

	int type = 0;
	String info = "";
	String deviceId = "";
	String devicePwd = "";

	static final int SCAN_SUCCEED = 0;

	Handler handlerScan = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			// dataToui();
			switch (msg.what) {
			case SCAN_SUCCEED:
				// Toast.makeText(RegisterActivity.this, "ע���û��ɹ���",
				// Toast.LENGTH_LONG).show();
				EditText txtDeviceId = (EditText) findViewById(R.id.registerTxtDeviceId);
				EditText txtDevicePwd = (EditText) findViewById(R.id.registerTxtDevicePwd);
				if (txtDeviceId != null) {
					txtDeviceId.setText(deviceId);
				}
				if (txtDevicePwd != null) {
					txtDevicePwd.setText(devicePwd);
				}
				finish();
				break;

			default:
				break;

			}
		}

	};

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		// ��ʼ�� CameraManager
		CameraManager.init(getApplication());
		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);

		Intent intent = this.getIntent();
		type = intent.getIntExtra("type", 0);
	}

	@Override
	protected void onResume() {
		super.onResume();
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
			// surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		decodeFormats = null;
		characterSet = null;

		playBeep = true;
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			playBeep = false;
		}
		initBeepSound();
		vibrate = true;
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		super.onDestroy();
	}

	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			CameraManager.get().openDriver(surfaceHolder);
		} catch (IOException ioe) {
			return;
		} catch (RuntimeException e) {
			return;
		}
		if (handler == null) {
			handler = new CaptureActivityHandler(this, decodeFormats,
					characterSet);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;

	}

	public ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	public Handler getHandler() {
		return handler;
	}

	public void drawViewfinder() {
		viewfinderView.drawViewfinder();

	}

	public void handleDecode(final Result obj, Bitmap barcode) {

		playBeepSoundAndVibrate();
		// ��ת�����ý���
		// Intent intent = new Intent();
		if (type == 1) {
			info = obj.getText();
			// intent.setClass(CaptureActivity.this, RegisterActivity.class);
			EditText txtDeviceId = (EditText) findViewById(R.id.registerTxtDeviceId);
			EditText txtDevicePwd = (EditText) findViewById(R.id.registerTxtDevicePwd);
			if (info != null) {
				if (info.length() >= 5) {
					String[] text = info.split(",");
					int index = 0;
					Log.v("onCreate", "��ά����Ϣ:" + info);
					if (text.length >= 3) {
						deviceId = text[index++].trim();
						devicePwd = text[index++].trim();
						Cfg.deviceId = deviceId;
						Cfg.devicePwd = devicePwd;
						// txtDeviceId.setText(text[index++].trim());
						// txtDevicePwd.setText(text[index++].trim());
						// Message message = new Message();
						// message.what = SCAN_SUCCEED;
						// handlerScan.sendMessage(message);
					}
				}
			}
			finish();
		}
		if (type == 2) {
			Intent intent = new Intent();
			intent.setClass(CaptureActivity.this, RegisterActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("info", obj.getText());
			intent.putExtras(bundle);
			startActivity(intent);// ���½���
			finish();
		} else {
			Intent intent = new Intent();
			intent.setClass(CaptureActivity.this, ScanActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("info", obj.getText());
			intent.putExtras(bundle);
			startActivity(intent);// ���½���
			finish();
		}

		// inactivityTimer.onActivity();
		// playBeepSoundAndVibrate();
		// AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		// if (barcode == null)
		// {
		// dialog.setIcon(null);
		// }
		// else
		// {
		//
		// Drawable drawable = new BitmapDrawable(barcode);
		// dialog.setIcon(drawable);
		// }
		// dialog.setTitle("ɨ����");
		// dialog.setMessage(obj.getText());
		// dialog.setNegativeButton("ȷ��", new DialogInterface.OnClickListener()
		// {
		// @Override
		// public void onClick(DialogInterface dialog, int which)
		// {
		// //��Ĭ���������ɨ��õ��ĵ�ַ
		// // Intent intent = new Intent();
		// // intent.setAction("android.intent.action.VIEW");
		// // Uri content_url = Uri.parse(obj.getText());
		// // intent.setData(content_url);
		// // startActivity(intent);
		//
		// // ��ת�����ý���
		// Intent intent = new Intent();
		// intent.setClass(CaptureActivity.this, ScanActivity.class);
		// // Bundle bundle = new Bundle();
		// // bundle.putInt("devId", dev.getId());
		// // intent.putExtras(bundle);
		// startActivity(intent);// ���½���
		// finish();
		// }
		// });
		// dialog.setPositiveButton("ȡ��", new DialogInterface.OnClickListener()
		// {
		// @Override
		// public void onClick(DialogInterface dialog, int which)
		// {
		// // finish();
		// }
		// });
		// dialog.create().show();
		//

	}

	private void initBeepSound() {
		if (playBeep && mediaPlayer == null) {
			// The volume on STREAM_SYSTEM is not adjustable, and users found it
			// too loud,
			// so we now play on the music stream.
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);

			AssetFileDescriptor file = getResources().openRawResourceFd(
					R.raw.beep);
			try {
				mediaPlayer.setDataSource(file.getFileDescriptor(),
						file.getStartOffset(), file.getLength());
				file.close();
				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
				mediaPlayer.prepare();
			} catch (IOException e) {
				mediaPlayer = null;
			}
		}
	}

	private static final long VIBRATE_DURATION = 200L;

	private void playBeepSoundAndVibrate() {
		if (playBeep && mediaPlayer != null) {
			mediaPlayer.start();
		}
		if (vibrate) {
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	/**
	 * When the beep has finished playing, rewind to queue up another one.
	 */
	private final OnCompletionListener beepListener = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};

}