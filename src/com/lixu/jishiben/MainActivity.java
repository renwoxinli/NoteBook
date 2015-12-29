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
	// ����Ĭ������ֵ
	private float size = 15.0f;

	// ���õ�������Դ�����˿��ķ��
	private Effects effect;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = this;
		// ����ȫ��
		Untils.toggleFullscreen(activity, true);
		setContentView(R.layout.main_activity);

		list = new ArrayList<HashMap<String, Object>>();

		lv = (ListView) findViewById(R.id.listview);
		mMyAdapter = new MyAdapter(this, -1);
		lv.setAdapter(mMyAdapter);
		// ����listview��������˵�
		lv.setOnCreateContextMenuListener(this);

		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
				// �����±��ȡ�����ݿ��ж�Ӧ���ݵ�id������intent�д��ݵ�NoteBookActivity
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
		// ���ø���xml�����÷��ص�ָʾ��ͷ��ʧ
		findViewById(R.id.iv2).setVisibility(View.GONE);

	}

	// Ϊ��ֹ�������̣߳���ʱ��������AsyncTask�д���
	@Override
	protected void onResume() {
		new MyAsyncTask().execute();
		super.onResume();
	}

	private void showPopupMenu(View view) {
		// View��ǰPopupMenu��ʾ�����View��λ��
		PopupMenu popupMenu = new PopupMenu(this, view);

		// menu����
		popupMenu.getMenuInflater().inflate(R.menu.main, popupMenu.getMenu());

		// menu��item����¼�
		popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				switch (item.getItemId()) {
				case R.id.new_text:
					Intent intent = new Intent(activity, NoteBookActivity.class);
					// ��������һ��ֵΪ-1
					intent.putExtra(Data.USER_ID, -1);
					startActivity(intent);
					break;

				case R.id.jishiben_setting:
					// ������Ҫ����һ����ת��Activity������SettingFragment
					Intent intent1 = new Intent(activity, ActivitySetting.class);
					startActivity(intent1);

					break;

				case R.id.exit:
					// ����activityȫ���˳�
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

	// �첽���񣬴����ʱ����
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
			// ��ȡ���õ������Сֵ
			SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(activity);
			String s = sharedpreferences.getString("edit_text_key_1", "15");
			size = Float.parseFloat(s);
			// ��������״̬ʱ�����list���ٴ����ݿ��ȡ���ݣ������ظ���ӵ�list��
			list.clear();

			for (HashMap<String, Object> n : result)
				list.add(n);
			// ˢ��������
			mMyAdapter.notifyDataSetChanged();
		}

	}

	// �������泤����������˵��ĵ���¼�
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		// ��ȡ����������ݵ��±�
		final int pos = info.position;
		// �����±��ȡ�����ݿ��ж�Ӧ���ݵ�id
		final int text_now_id = (Integer) list.get(pos).get(Data.USER_ID);

		switch (item.getItemId()) {
		case 1001:
			zhiding(pos, text_now_id);

			effect = Effects.thumbSlider;
			NotifiActivity nba = new NotifiActivity(activity, "���ö���", effect, R.id.main, R.drawable.dfdf);
			nba.show();
			break;

		case 1002:
			// �Ի���ʾѡ���
			AlertDialog dialog1 = new AlertDialog.Builder(this).create();
			dialog1.setTitle("ȷ��Ҫɾ����");
			dialog1.setIcon(R.drawable.delete);
			dialog1.setButton(DialogInterface.BUTTON_POSITIVE, "ȷ��", new DialogInterface.OnClickListener() {

				@SuppressWarnings("unchecked")
				@Override
				public void onClick(DialogInterface dialog, int which) {
					deleteData(pos, text_now_id);
				}
			});

			dialog1.setButton(DialogInterface.BUTTON_NEGATIVE, "ȡ��", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {

					effect = Effects.flip;
					NotifiActivity nba = new NotifiActivity(activity, "��ȡ����", effect, R.id.main, R.drawable.dfdf);
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

	// ��ȡ�ֻ����ذ�ť�������η�����ȫ�˳�����
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exittime) > 2000) { // System.currentTimeMillis()���ۺ�ʱ���ã��϶�����2000

				effect = Effects.slideOnTop;
				NotifiActivity nba = new NotifiActivity(activity, "�ٰ�һ�η��ؼ��˳�����", effect, R.id.main, R.drawable.dfdf);
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
		// arraylist�����ö�
		HashMap<String, Object> map = new HashMap<String, Object>();
		map = list.get(pos);
		// �޸�map�е�ʱ��
		map.remove(Data.SYSTEM_TIME);
		map.put(Data.SYSTEM_TIME, Untils.formatTimeInMillis(System.currentTimeMillis()));
		list.remove(pos);
		list.add(0, map);
		// ���ݿ������ö�
		Dao dao = GetDao();
		try {
			Data d = (Data) dao.queryForId(id);
			// �ö���ı��޸�ʱ��
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
			// ɾ�����ݿ��е�����
			dao.deleteById(id);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		// ɾ��arraylist�е�����
		list.remove(pos);

		mMyAdapter.notifyDataSetChanged();

		effect = Effects.jelly;
		NotifiActivity nba = new NotifiActivity(activity, "��ɾ����", effect, R.id.main, R.drawable.dfdf);
		nba.show();
	}

	// �������泤����������˵�
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		menu.add(0, 1001, 101, "�ö����ı�");
		menu.add(0, 1002, 102, "ɾ�����ı�");

	}

	// ͨ��ORM���ݿ⹤���ҵ�Dao������
	private Dao GetDao() {

		ORMLiteDatabaseHelper mDatabaseHelper = ORMLiteDatabaseHelper.getInstance(this);
		Dao dataDao = mDatabaseHelper.getUserDao();

		return dataDao;
	}
	// �����ݿ��ȡ����

	private ArrayList<HashMap<String, Object>> GetDataFromDB() {
		// ���û���list
		ArrayList<HashMap<String, Object>> list_cache = new ArrayList<HashMap<String, Object>>();
		Dao dataDao = GetDao();

		try {
			// �����ݿ����ݰ�����(������ʱ��)���У�ʹ��ÿ��������ı������
			// list.add(0,map)����Ҳ����ʵ�֣�������������������Ժ������Ӱ��ܴ�
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
