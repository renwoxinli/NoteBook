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
	// 三种弹出动画效果
	private Effects[] eff = { Effects.jelly, Effects.slideIn, Effects.slideOnTop };

	private String[] xiaoxin = { "欢迎使用木子记事本，我是蜡笔小新，你的私人助理哦。。。。么么哒！", "希望你经常记录你的私人笔记哦！", "现在点击进入就可以开始使用了哦，我会随时在你身边！" };

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		activity = this;
		super.onCreate(savedInstanceState);

//		SharedPreferences mSharedPreferences = this.getSharedPreferences(NAME, this.MODE_PRIVATE);
//		boolean first_run = mSharedPreferences.getBoolean(APP_FIRST_RUN, true);
//		// 第一次进入后修改成false 以后进入就不会重新进入欢迎界面了
//		if (first_run == true) {
//			Editor e = mSharedPreferences.edit();
//			e.putBoolean(APP_FIRST_RUN, false);
//			e.commit();
//			// 进入主程序关闭初始动画界面
//			this.finish();
//		} else {
//			Intent intent = new Intent(this, MainActivity.class);
//			startActivity(intent);
//			this.finish();
//		}

		// 让程序全屏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		Untils.toggleFullscreen(activity, true);

		setContentView(R.layout.welcome);

		mMydrawable = (Mydrawable) findViewById(R.id.mydrawable);
		// 根据情况设置圆球数量
		mMydrawable.setCount(images.length);

		ViewPager vp = (ViewPager) findViewById(R.id.vp);

		vp.setAdapter(new MyAdapter(this.getSupportFragmentManager()));

		vp.setOnPageChangeListener(new OnPageChangeListener() {
			// 设置页面滑动时改变红点的位置
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
		// 初始弹出对话框
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
								// 进入主程序关闭初始动画界面
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
