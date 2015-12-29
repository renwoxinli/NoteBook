package com.lixu.jishiben;

import com.gitonway.lee.niftynotification.lib.Effects;
import com.gitonway.lee.niftynotification.lib.NiftyNotificationView;

import android.app.Activity;

public class NotifiActivity {

	private Activity activity;
	private String msg;
	private Effects effect;
	private int id;
	private int image_id;

	public NotifiActivity(Activity activity, String msg, Effects effect, int id, int image_id) {
		this.activity = activity;
		this.msg = msg;
		this.effect = effect;
		this.id = id;
		this.image_id = image_id;
	}

	public void show() {
		NiftyNotificationView.build(activity, msg, effect, id).setIcon(image_id).show();
	}

}
