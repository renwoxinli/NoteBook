package com.lixu.jishiben;

import com.lixu.jishiben.PreferenceActivity.SettingFragment;
import com.lixu.jishiben.untils.Untils;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class ActivitySetting extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 让程序全屏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		Untils.toggleFullscreen(this, true);

		setContentView(R.layout.setting);

		// 设置复用xml里面让返回的指示箭头消失
		findViewById(R.id.iv2).setVisibility(View.GONE);
		// 设置复用xml里面让三点消失
		findViewById(R.id.iv1).setVisibility(View.GONE);
		// 设置复用xml里面的textview的内容
		TextView title = (TextView) findViewById(R.id.tv1);
		title.setText("个性化设置");

		// 创建Fragment管理器
		FragmentManager fm = this.getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		// 将原有的新Activity覆盖到原有之上
		ft.replace(R.id.setting, new SettingFragment());
		// 提交
		ft.commit();

	}
}
