package com.lixu.jishiben.untils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import android.app.Activity;
import android.view.WindowManager;

public class Untils {
	public static float size;

	// ��ȡϵͳʱ��Ĺ��ߣ������ʽ��Ϊ�ɶ��ĸ�ʽ
	public static String formatTimeInMillis(long timeInMillis) {

		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(timeInMillis);
		Date date = cal.getTime();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy��MM��dd�� HH:mm:ss");
		String fmt = dateFormat.format(date);

		return fmt;
	}

	public static void toggleFullscreen(Activity activity, boolean fullScreen) {
		// fullScreenΪtrueʱȫ��

		WindowManager.LayoutParams attrs = activity.getWindow().getAttributes();

		if (fullScreen) {
			attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
		} else {
			attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
		}

		activity.getWindow().setAttributes(attrs);
	}
}
