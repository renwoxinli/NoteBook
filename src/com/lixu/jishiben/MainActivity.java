package com.lixu.jishiben;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.gitonway.lee.niftynotification.lib.Effects;
import com.j256.ormlite.dao.Dao;
import com.lixu.jishiben.R;
import com.lixu.jishiben.database.Data;
import com.lixu.jishiben.database.ORMLiteDatabaseHelper;
import com.lixu.jishiben.untils.Untils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract.CommonDataKinds.Event;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private Activity activity;
	private ListView lv;
	private MyAdapter mMyAdapter;
	private ArrayList<HashMap<String, Object>> list;
	private long exittime = 0;
	// 设置默认字体值
	private float size = 15.0f;

	// 设置第三方开源框架吐丝框的风格
	private Effects effect;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = this;
		// 设置全屏
		Untils.toggleFullscreen(activity, true);
		setContentView(R.layout.main_activity);

		list = new ArrayList<HashMap<String, Object>>();

		lv = (ListView) findViewById(R.id.listview);
		mMyAdapter = new MyAdapter(this, -1);
		lv.setAdapter(mMyAdapter);
		// 设置listview长按点击菜单
		lv.setOnCreateContextMenuListener(this);

		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
				// 根据下标获取到数据库中对应数据的id，放入intent中传递到NoteBookActivity
				int text_id = (Integer) list.get(pos).get(Data.USER_ID);

				Intent intent = new Intent(activity, NoteBookActivity.class);
				intent.putExtra(Data.USER_ID, text_id);
				startActivity(intent);
			}
		});

		final ImageView iv = (ImageView) findViewById(R.id.iv1);
		iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showPopupMenu(iv);
			}
		});
		// 设置复用xml里面让返回的指示箭头消失
		findViewById(R.id.iv2).setVisibility(View.GONE);

	}

	// 为防止阻塞主线程，耗时操作放入AsyncTask中处理
	@Override
	protected void onResume() {
		new MyAsyncTask().execute();
		super.onResume();
	}

	private void showPopupMenu(View view) {
		// View当前PopupMenu显示的相对View的位置
		PopupMenu popupMenu = new PopupMenu(this, view);

		// menu布局
		popupMenu.getMenuInflater().inflate(R.menu.main, popupMenu.getMenu());

		// menu的item点击事件
		popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				switch (item.getItemId()) {
				case R.id.new_text:
					Intent intent = new Intent(activity, NoteBookActivity.class);
					// 故意设置一个值为-1
					intent.putExtra(Data.USER_ID, -1);
					startActivity(intent);
					break;

				case R.id.jishiben_setting:
					// 这里需要启动一个中转的Activity来启动SettingFragment
					Intent intent1 = new Intent(activity, ActivitySetting.class);
					startActivity(intent1);

					break;

				case R.id.exit:
					// 程序activity全部退出
					System.exit(0);
					break;

				default:
					break;
				}

				return false;
			}
		});

		popupMenu.show();
	}

	// 异步任务，处理耗时操作
	private class MyAsyncTask extends AsyncTask<String, Integer, ArrayList<HashMap<String, Object>>> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected ArrayList<HashMap<String, Object>> doInBackground(String... params) {

			return GetDataFromDB();

		}

		@Override
		protected void onPostExecute(ArrayList<HashMap<String, Object>> result) {
			// 获取设置的字体大小值
			SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(activity);
			String s = sharedpreferences.getString("edit_text_key_1", "15");
			size = Float.parseFloat(s);
			// 返回运行状态时，清空list，再从数据库读取数据，避免重复添加到list中
			list.clear();

			for (HashMap<String, Object> n : result)
				list.add(n);
			// 刷新适配器
			mMyAdapter.notifyDataSetChanged();
		}

	}

	// 创建界面长按点击弹出菜单的点击事件
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		// 获取点击哪条数据的下标
		final int pos = info.position;
		// 根据下标获取到数据库中对应数据的id
		final int text_now_id = (Integer) list.get(pos).get(Data.USER_ID);

		switch (item.getItemId()) {
		case 1001:
			zhiding(pos, text_now_id);

			effect = Effects.thumbSlider;
			NotifiActivity nba = new NotifiActivity(activity, "已置顶！", effect, R.id.main, R.drawable.dfdf);
			nba.show();
			break;

		case 1002:
			// 对话提示选择框
			AlertDialog dialog1 = new AlertDialog.Builder(this).create();
			dialog1.setTitle("确定要删除吗？");
			dialog1.setIcon(R.drawable.delete);
			dialog1.setButton(DialogInterface.BUTTON_POSITIVE, "确认", new DialogInterface.OnClickListener() {

				@SuppressWarnings("unchecked")
				@Override
				public void onClick(DialogInterface dialog, int which) {
					deleteData(pos, text_now_id);
				}
			});

			dialog1.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {

					effect = Effects.flip;
					NotifiActivity nba = new NotifiActivity(activity, "已取消！", effect, R.id.main, R.drawable.dfdf);
					nba.show();
				}
			});
			dialog1.show();

			break;

		default:
			break;
		}

		return super.onContextItemSelected(item);
	}

	// 获取手机返回按钮，按两次返回完全退出程序
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exittime) > 2000) { // System.currentTimeMillis()无论何时调用，肯定大于2000

				effect = Effects.slideOnTop;
				NotifiActivity nba = new NotifiActivity(activity, "再按一次返回键退出程序！", effect, R.id.main, R.drawable.dfdf);
				nba.show();
				exittime = System.currentTimeMillis();
			} else {
				finish();
				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);

	}

	private void zhiding(int pos, int id) {
		// arraylist数据置顶
		HashMap<String, Object> map = new HashMap<String, Object>();
		map = list.get(pos);
		// 修改map中的时间
		map.remove(Data.SYSTEM_TIME);
		map.put(Data.SYSTEM_TIME, Untils.formatTimeInMillis(System.currentTimeMillis()));
		list.remove(pos);
		list.add(0, map);
		// 数据库数据置顶
		Dao dao = GetDao();
		try {
			Data d = (Data) dao.queryForId(id);
			// 置顶后改变修改时间
			d.setTime_text(Untils.formatTimeInMillis(System.currentTimeMillis()));
			dao.createOrUpdate(d);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		mMyAdapter.notifyDataSetChanged();

	}

	private void deleteData(int pos, int id) {
		Dao dao = GetDao();

		try {
			// 删除数据库中的数据
			dao.deleteById(id);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 删除arraylist中的数据
		list.remove(pos);

		mMyAdapter.notifyDataSetChanged();

		effect = Effects.jelly;
		NotifiActivity nba = new NotifiActivity(activity, "已删除！", effect, R.id.main, R.drawable.dfdf);
		nba.show();
	}

	// 创建界面长按点击弹出菜单
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		menu.add(0, 1001, 101, "置顶此文本");
		menu.add(0, 1002, 102, "删除此文本");

	}

	// 通过ORM数据库工具找到Dao工具类
	private Dao GetDao() {

		ORMLiteDatabaseHelper mDatabaseHelper = ORMLiteDatabaseHelper.getInstance(this);
		Dao dataDao = mDatabaseHelper.getUserDao();

		return dataDao;
	}
	// 从数据库获取数据

	private ArrayList<HashMap<String, Object>> GetDataFromDB() {
		// 设置缓存list
		ArrayList<HashMap<String, Object>> list_cache = new ArrayList<HashMap<String, Object>>();
		Dao dataDao = GetDao();

		try {
			// 将数据库内容按逆序(按更新时间)排列，使得每次添加新文本到最顶部
			// list.add(0,map)这样也可以实现，但是如果数据量大了以后对性能影响很大
			List<Data> datas = dataDao.queryBuilder().orderBy(Data.SYSTEM_TIME, false).query();

			for (Data d : datas) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put(Data.TEXT, d.getText());
				map.put(Data.TITLE, d.getTitle());
				map.put(Data.SYSTEM_TIME, d.getTime_text());
				map.put(Data.USER_ID, d.user_id);

				list_cache.add(map);

			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list_cache;

	}

	private class MyAdapter extends ArrayAdapter<String> {
		private LayoutInflater flater;

		public MyAdapter(Context context, int resource) {
			super(context, resource);
			flater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null)
				convertView = flater.inflate(R.layout.listview_layout, null);

			TextView tv_biaoti = (TextView) convertView.findViewById(R.id.biaoti);

			tv_biaoti.setTextSize(size);
			TextView tv_time = (TextView) convertView.findViewById(R.id.time);

			tv_biaoti.setText(list.get(position).get(Data.TITLE).toString());
			tv_time.setText(list.get(position).get(Data.SYSTEM_TIME).toString());

			return convertView;
		}

	}

}
