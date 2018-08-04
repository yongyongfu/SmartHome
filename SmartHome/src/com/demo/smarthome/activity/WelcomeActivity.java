package com.demo.smarthome.activity;

import com.demo.smarthome.R;
import com.demo.smarthome.tools.StrTools;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.Window;

/**
 * ��ӭ������
 * 
 * @author Administrator
 * 
 */
public class WelcomeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); // ע��˳��
		setContentView(R.layout.activity_welcome);
		new Handler().postDelayed(r, 3000);// 3���رգ�����ת����ҳ��
		
		
//		//test
//		String idStr="0x0099DBD9";
//		String passStr="0x02D012C3";
//		String deviceId = StrTools.StrHexLowToLong(idStr) + "";
//		;
//		String devicePwd = StrTools.StrHexHighToLong(passStr) + "";
//		System.out.println("idStr:"+idStr);
//		System.out.println("deviceId:"+deviceId);
//		System.out.println("passStr:"+passStr);
//		System.out.println("devicePwd:"+devicePwd);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.welcome, menu);
		return true;
	}

	Runnable r = new Runnable() {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			intent.setClass(WelcomeActivity.this, LoginActivity.class);
			startActivity(intent);
			finish();
		}
	};
}
