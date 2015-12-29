package com.lixu.jishiben.PreferenceActivity;

import com.lixu.jishiben.R;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

//安卓官方申明在3.0版本后最好不要用FragmentActivity 应该用PreferenceFragment，在从主Activity中调用。
public class SettingFragment extends PreferenceFragment {

	private MyListener mMyListener = new MyListener();
	private EditTextPreference mEditTextPreference;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 读取值的通用方法
		SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

		// 添加设置选项
		addPreferencesFromResource(R.xml.preference);
		// 找到各个控件按钮
		mEditTextPreference = (EditTextPreference) findPreference("edit_text_key_1");
		// 设置初始值
		String str2 = sharedpreferences.getString("edit_text_key_1", "null");
		mEditTextPreference.setSummary(str2);

		// 添加项目数据发生变化时候的监听
		sharedpreferences.unregisterOnSharedPreferenceChangeListener(mMyListener);
	}

	// 程序暂停时取消注册监听事件，使得代码更加完整。
	@Override
	public void onPause() {
		super.onPause();
		getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(mMyListener);
	}

	// 程序运行时注册
	@Override
	public void onResume() {
		super.onResume();
		getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(mMyListener);
	}

	private class MyListener implements OnSharedPreferenceChangeListener {

		@Override
		public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

			switch (key) {
			case "edit_text_key_1":
				String str1 = sharedPreferences.getString("edit_text_key_1", "null");
				mEditTextPreference.setSummary(str1);

				break;

			default:
				break;
			}
		}
	}

}