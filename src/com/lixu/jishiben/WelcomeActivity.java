package com.lixu.jishiben;

import com.gitonway.lee.niftynotification.lib.Effects;
import com.lixu.jishiben.R;
import com.lixu.jishiben.draws.Mydrawable;
import com.lixu.jishiben.untils.Untils;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

public class WelcomeActivity extends FragmentActivity {
	private static final String NAME = "name";
	private Mydrawable mMydrawable;
	private Activity activity;
	private String APP_FIRST_RUN = "app_first_run";
	private int[] images = { R.drawable.p1, R.drawable.p2, R.drawable.p3, };
	private Effects effect;
	// ���ֵ�������Ч��
	private Effects[] eff = { Effects.jelly, Effects.slideIn, Effects.slideOnTop };

	private String[] xiaoxin = { "��ӭʹ��ľ�Ӽ��±�����������С�£����˽������Ŷ��������ôô�գ�", "ϣ���㾭����¼���˽�˱ʼ�Ŷ��", "���ڵ������Ϳ��Կ�ʼʹ����Ŷ���һ���ʱ������ߣ�" };

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		activity = this;
		super.onCreate(savedInstanceState);

//		SharedPreferences mSharedPreferences = this.getSharedPreferences(NAME, this.MODE_PRIVATE);
//		boolean first_run = mSharedPreferences.getBoolean(APP_FIRST_RUN, true);
//		// ��һ�ν�����޸ĳ�false �Ժ����Ͳ������½��뻶ӭ������
//		if (first_run == true) {
//			Editor e = mSharedPreferences.edit();
//			e.putBoolean(APP_FIRST_RUN, false);
//			e.commit();
//			// ����������رճ�ʼ��������
//			this.finish();
//		} else {
//			Intent intent = new Intent(this, MainActivity.class);
//			startActivity(intent);
//			this.finish();
//		}

		// �ó���ȫ��
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		Untils.toggleFullscreen(activity, true);

		setContentView(R.layout.welcome);

		mMydrawable = (Mydrawable) findViewById(R.id.mydrawable);
		// �����������Բ������
		mMydrawable.setCount(images.length);

		ViewPager vp = (ViewPager) findViewById(R.id.vp);

		vp.setAdapter(new MyAdapter(this.getSupportFragmentManager()));

		vp.setOnPageChangeListener(new OnPageChangeListener() {
			// ����ҳ�滬��ʱ�ı����λ��
			@Override
			public void onPageSelected(int pos) {

				mMydrawable.choose(pos);

				NotifiActivity nba = new NotifiActivity(activity, xiaoxin[pos], eff[pos], R.layout.welcome_first,
						R.drawable.dfdf);
				nba.show();

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
		// ��ʼ�����Ի���
		effect = Effects.flip;
		NotifiActivity nba = new NotifiActivity(activity, xiaoxin[0], effect, R.layout.welcome_first, R.drawable.dfdf);
		nba.show();

	}

	private class MyAdapter extends FragmentPagerAdapter {

		public MyAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(final int pos) {

			Fragment f = new Fragment() {
				@Override
				public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

					if (pos == (getCount() - 1)) {

						View view = inflater.inflate(R.layout.welcome_end, null);
						Button btn = (Button) view.findViewById(R.id.btn);
						btn.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								Intent intent = new Intent(activity, MainActivity.class);
								startActivity(intent);
								// ����������رճ�ʼ��������
								activity.finish();
							}
						});
						ImageView iv = (ImageView) view.findViewById(R.id.iv_end);
						iv.setImageResource(images[pos]);

						return view;

					} else {
						View view = inflater.inflate(R.layout.welcome_first, null);

						ImageView iv = (ImageView) view.findViewById(R.id.iv_first);
						iv.setImageResource(images[pos]);

						return view;

					}
				}
			};

			return f;
		}

		@Override
		public int getCount() {
			return images.length;
		}

	}

}
