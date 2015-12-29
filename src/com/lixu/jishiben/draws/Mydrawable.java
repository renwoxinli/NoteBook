package com.lixu.jishiben.draws;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class Mydrawable extends View {
	private int posfirst = 0;
	// Բ�������
	private int count = 0;

	// ��������췽��,�������ᱨ��
	public Mydrawable(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	// �����������Բ������
	public void setCount(int a) {
		count = a;
	}

	public void choose(int pos) {
		posfirst = pos;
		// ִ��������� �����»�ͼ
		this.invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		Paint p = new Paint();
		// ȥ��ͼ�εľ�ݣ�ʹ��Բ���Բ��
		p.setAntiAlias(true);

		// ��ȡ��ĻX Y����
		float w = getWidth();
		float h = getHeight();

		// Բ��ļ��(��Ը��ֻ�����Ļ���䣩
		int juli = (int) (w / 7);

		// ����Բ��İ뾶(��Ը��ֻ�����Ļ���䣩
		float radius = w / 50;

		for (int i = 0; i < count; i++) {
			if (i == posfirst) {

				p.setColor(Color.RED);
				// ��һ����ɫԲ��
				canvas.drawCircle((w - (count - 1) * juli) / 2 + juli * i, (float) (h * 0.9), radius + w / 142, p);
			} else {

				p.setColor(Color.GRAY);
				// ��һ����ɫԲ��
				canvas.drawCircle((w - (count - 1) * juli) / 2 + juli * i, (float) (h * 0.9), radius, p);

			}

		}

	}

}
