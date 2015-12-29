package com.lixu.jishiben.PreferenceActivity;

import com.lixu.jishiben.R;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

//��׿�ٷ�������3.0�汾����ò�Ҫ��FragmentActivity Ӧ����PreferenceFragment���ڴ���Activity�е��á�
public class SettingFragment extends PreferenceFragment {

	private MyListener mMyListener = new MyListener();
	private EditTextPreference mEditTextPreference;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ��ȡֵ��ͨ�÷���
		SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

		// �������ѡ��
		addPreferencesFromResource(R.xml.preference);
		// �ҵ������ؼ���ť
		mEditTextPreference = (EditTextPreference) findPreference("edit_text_key_1");
		// ���ó�ʼֵ
		String str2 = sharedpreferences.getString("edit_text_key_1", "null");
		mEditTextPreference.setSummary(str2);

		// �����Ŀ���ݷ����仯ʱ��ļ���
		sharedpreferences.unregisterOnSharedPreferenceChangeListener(mMyListener);
	}

	// ������ͣʱȡ��ע������¼���ʹ�ô������������
	@Override
	public void onPause() {
		super.onPause();
		getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(mMyListener);
	}

	// ��������ʱע��
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