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

		// �ó���ȫ��
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		Untils.toggleFullscreen(this, true);

		setContentView(R.layout.setting);

		// ���ø���xml�����÷��ص�ָʾ��ͷ��ʧ
		findViewById(R.id.iv2).setVisibility(View.GONE);
		// ���ø���xml������������ʧ
		findViewById(R.id.iv1).setVisibility(View.GONE);
		// ���ø���xml�����textview������
		TextView title = (TextView) findViewById(R.id.tv1);
		title.setText("���Ի�����");

		// ����Fragment������
		FragmentManager fm = this.getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		// ��ԭ�е���Activity���ǵ�ԭ��֮��
		ft.replace(R.id.setting, new SettingFragment());
		// �ύ
		ft.commit();

	}
}
