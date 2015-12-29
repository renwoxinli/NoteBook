package com.lixu.jishiben;

import java.sql.SQLException;

import com.gitonway.lee.niftynotification.lib.Effects;
import com.j256.ormlite.dao.Dao;
import com.lixu.jishiben.R;
import com.lixu.jishiben.database.Data;
import com.lixu.jishiben.database.ORMLiteDatabaseHelper;
import com.lixu.jishiben.untils.Untils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

public class NoteBookActivity extends Activity {
	private Activity activity;
	private int SAVE_ID = 1987;

	private EditText title_text;
	private EditText text_text;

	private Dao<Data, Integer> dataDao;

	private Data mdata;

	// ���õ�������Դ�����˿��ķ��
	private Effects effect;

	// �����ı������С��ʼֵ
	private float size = 12.0f;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		activity = this;
		// ����ȫ��
		Untils.toggleFullscreen(activity, true);
		setContentView(R.layout.notebook);

		title_text = (EditText) findViewById(R.id.title);
		text_text = (EditText) findViewById(R.id.text);

		// ��intent�л�ȡMainActivity����������
		Intent intent = getIntent();
		int text_id = intent.getIntExtra(Data.USER_ID, -1);
		// ��������½� �ı���Ϊ��
		String title1 = "", text1 = "";

		if (text_id != -1) {
			// ��listview�е������
			dataDao = GetDao();
			try {
				mdata = dataDao.queryForId(text_id);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			title1 = mdata.getTitle();
			text1 = mdata.getText();

		} else {
			// ���½��ı�����
			// ʵ����
			mdata = new Data();
		}

		title_text.setText(title1);
		text_text.setText(text1);

		final ImageView iv = (ImageView) findViewById(R.id.iv1);
		iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showPopupMenu(iv);
			}
		});
		// ���ø���xml�����textview������
		TextView title = (TextView) findViewById(R.id.tv1);
		title.setText("�������±�");

		findViewById(R.id.iv2).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				save();
			}
		});

	}

	// �����������Ի���
	private void save() {
		final String title_first = title_text.getText().toString();
		final String text_first = text_text.getText().toString();
		AlertDialog dialog1 = new AlertDialog.Builder(activity).create();
		dialog1.setTitle("����δ���棬�Ƿ񱣴���");
		dialog1.setIcon(R.drawable.delete);
		dialog1.setButton(DialogInterface.BUTTON_POSITIVE, "ȷ��", new DialogInterface.OnClickListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void onClick(DialogInterface dialog, int which) {

				if (title_first.trim().equals("") || text_first.trim().equals("")) {
					effect = Effects.standard;
					NotifiActivity nba = new NotifiActivity(activity, "�������������ݣ�", effect, R.id.notify_weizhi,
							R.drawable.dfdf);
					nba.show();
				} else {
					setDataToDB(title_first, text_first);

					activity.finish();

				}

			}
		});

		dialog1.setButton(DialogInterface.BUTTON_NEGATIVE, "ȡ��", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				activity.finish();
			}
		});
		dialog1.show();
	}

	// ��ȡ�ֻ����ذ�ť������ж��¼�
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			String title_first = title_text.getText().toString();
			String text_first = text_text.getText().toString();
			if (title_first.trim().equals("") || text_first.trim().equals("")) {
				effect = Effects.slideOnTop;
				NotifiActivity nba = new NotifiActivity(activity, "�������������ݣ�", effect, R.id.notify_weizhi,
						R.drawable.dfdf);
				nba.show();
			} else {
				save();
			}
		}

		return false;
	}

	@Override
	protected void onResume() {

		// ��ȡ���õ������Сֵ
		SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(activity);
		String s = sharedpreferences.getString("edit_text_key_1", "15");
		size = Float.parseFloat(s);

		title_text.setTextSize(size);
		text_text.setTextSize(size);
		super.onResume();
	}

	private Dao GetDao() {
		// ͨ��ORM���ݿ⹤���ҵ�Dao������
		ORMLiteDatabaseHelper mDatabaseHelper = ORMLiteDatabaseHelper.getInstance(this);
		dataDao = mDatabaseHelper.getUserDao();
		return dataDao;
	}

	public void setDataToDB(String title, String text) {
		GetDao();

		// ��������

		mdata.setTime_text(Untils.formatTimeInMillis(System.currentTimeMillis()));
		mdata.setTitle(title);
		mdata.setText(text);

		// �����ݱ��浽ORM���ݿ�
		try {
			dataDao.createOrUpdate(mdata);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void showPopupMenu(View view) {
		// View��ǰPopupMenu��ʾ�����View��λ��
		PopupMenu popupMenu = new PopupMenu(this, view);

		// menu����
		popupMenu.getMenuInflater().inflate(R.menu.main1, popupMenu.getMenu());

		// menu��item����¼�
		popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				String title_first = title_text.getText().toString();
				String text_first = text_text.getText().toString();

				if (item.getItemId() == R.id.text_save) {

					if (title_first.trim().equals("") || text_first.trim().equals("")) {
						effect = Effects.standard;
						NotifiActivity nba = new NotifiActivity(activity, "�������������ݣ�", effect, R.id.notify_weizhi,
								R.drawable.dfdf);
						nba.show();
					} else {
						setDataToDB(title_first, text_first);

						activity.finish();

					}

				}

				return false;
			}
		});

		popupMenu.show();
	}

}
