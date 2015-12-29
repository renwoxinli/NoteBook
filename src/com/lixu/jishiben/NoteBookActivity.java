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

	// 设置第三方开源框架吐丝框的风格
	private Effects effect;

	// 设置文本字体大小初始值
	private float size = 12.0f;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		activity = this;
		// 设置全屏
		Untils.toggleFullscreen(activity, true);
		setContentView(R.layout.notebook);

		title_text = (EditText) findViewById(R.id.title);
		text_text = (EditText) findViewById(R.id.text);

		// 从intent中获取MainActivity传来的数据
		Intent intent = getIntent();
		int text_id = intent.getIntExtra(Data.USER_ID, -1);
		// 如果进入新建 文本则为空
		String title1 = "", text1 = "";

		if (text_id != -1) {
			// 从listview中点击进入
			dataDao = GetDao();
			try {
				mdata = dataDao.queryForId(text_id);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			title1 = mdata.getTitle();
			text1 = mdata.getText();

		} else {
			// 从新建文本进入
			// 实例化
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
		// 设置复用xml里面的textview的内容
		TextView title = (TextView) findViewById(R.id.tv1);
		title.setText("创建记事本");

		findViewById(R.id.iv2).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				save();
			}
		});

	}

	// 弹出保存与否对话框
	private void save() {
		final String title_first = title_text.getText().toString();
		final String text_first = text_text.getText().toString();
		AlertDialog dialog1 = new AlertDialog.Builder(activity).create();
		dialog1.setTitle("您还未保存，是否保存吗？");
		dialog1.setIcon(R.drawable.delete);
		dialog1.setButton(DialogInterface.BUTTON_POSITIVE, "确认", new DialogInterface.OnClickListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void onClick(DialogInterface dialog, int which) {

				if (title_first.trim().equals("") || text_first.trim().equals("")) {
					effect = Effects.standard;
					NotifiActivity nba = new NotifiActivity(activity, "请输入标题和内容！", effect, R.id.notify_weizhi,
							R.drawable.dfdf);
					nba.show();
				} else {
					setDataToDB(title_first, text_first);

					activity.finish();

				}

			}
		});

		dialog1.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				activity.finish();
			}
		});
		dialog1.show();
	}

	// 获取手机返回按钮，添加判断事件
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			String title_first = title_text.getText().toString();
			String text_first = text_text.getText().toString();
			if (title_first.trim().equals("") || text_first.trim().equals("")) {
				effect = Effects.slideOnTop;
				NotifiActivity nba = new NotifiActivity(activity, "请输入标题和内容！", effect, R.id.notify_weizhi,
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

		// 获取设置的字体大小值
		SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(activity);
		String s = sharedpreferences.getString("edit_text_key_1", "15");
		size = Float.parseFloat(s);

		title_text.setTextSize(size);
		text_text.setTextSize(size);
		super.onResume();
	}

	private Dao GetDao() {
		// 通过ORM数据库工具找到Dao工具类
		ORMLiteDatabaseHelper mDatabaseHelper = ORMLiteDatabaseHelper.getInstance(this);
		dataDao = mDatabaseHelper.getUserDao();
		return dataDao;
	}

	public void setDataToDB(String title, String text) {
		GetDao();

		// 设置数据

		mdata.setTime_text(Untils.formatTimeInMillis(System.currentTimeMillis()));
		mdata.setTitle(title);
		mdata.setText(text);

		// 将数据保存到ORM数据库
		try {
			dataDao.createOrUpdate(mdata);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void showPopupMenu(View view) {
		// View当前PopupMenu显示的相对View的位置
		PopupMenu popupMenu = new PopupMenu(this, view);

		// menu布局
		popupMenu.getMenuInflater().inflate(R.menu.main1, popupMenu.getMenu());

		// menu的item点击事件
		popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				String title_first = title_text.getText().toString();
				String text_first = text_text.getText().toString();

				if (item.getItemId() == R.id.text_save) {

					if (title_first.trim().equals("") || text_first.trim().equals("")) {
						effect = Effects.standard;
						NotifiActivity nba = new NotifiActivity(activity, "请输入标题和内容！", effect, R.id.notify_weizhi,
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
