package com.lixu.jishiben.draws;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class Mydrawable extends View {
	private int posfirst = 0;
	// 圆球的数量
	private int count = 0;

	// 用这个构造方法,用其他会报错
	public Mydrawable(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	// 根据情况设置圆球数量
	public void setCount(int a) {
		count = a;
	}

	public void choose(int pos) {
		posfirst = pos;
		// 执行这个方法 会重新绘图
		this.invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		Paint p = new Paint();
		// 去掉图形的锯齿，使得圆球更圆滑
		p.setAntiAlias(true);

		// 获取屏幕X Y坐标
		float w = getWidth();
		float h = getHeight();

		// 圆球的间距(针对各种机型屏幕适配）
		int juli = (int) (w / 7);

		// 设置圆球的半径(针对各种机型屏幕适配）
		float radius = w / 50;

		for (int i = 0; i < count; i++) {
			if (i == posfirst) {

				p.setColor(Color.RED);
				// 画一个红色圆形
				canvas.drawCircle((w - (count - 1) * juli) / 2 + juli * i, (float) (h * 0.9), radius + w / 142, p);
			} else {

				p.setColor(Color.GRAY);
				// 画一个灰色圆形
				canvas.drawCircle((w - (count - 1) * juli) / 2 + juli * i, (float) (h * 0.9), radius, p);

			}

		}

	}

}
