package com.feicui.view;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class ClearView extends View {
	private RectF oval;
	private float startAngle;
	private float sweepAngle;
	private boolean useCenter;
	private int state;
	//�����ӿڶ���
	private OnAngleChangeListener onAngleChangeListener;
	Paint paint;

	public ClearView(Context context, AttributeSet attrs) {
		super(context, attrs);
		startAngle = -90;
		sweepAngle = 360;
		// ����һ��״̬���ж�Ӧ��ǰ������Ӧ�ú���
		// Լ��=1��ǰ����=2����
		state = 2;
		paint = new Paint();
		paint.setColor(Color.GREEN);
		paint.setAntiAlias(true);

		changeAngle(90);
	}
	private int[] back={2,2,4,4,6,6,8,8,10};
	private int[] go={10,10,8,8,6,6,4,4,2};
	private int go_index=0;
	private int back_index=0;
	private static boolean isRunning=false;
	/**
	 * �ǶȵĶ�̬����
	 * @param targetAngle  ��ʵ�ĽǶ�
	 */
	public void changeAngle(final int targetAngle) {
		
		if(isRunning){
			return;
		}
		
		final Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				switch (state) {
				case 1:
					sweepAngle+=go[go_index];
					if(go_index==go.length){
						go_index--;
					}
					if(sweepAngle>=targetAngle){//���е�Ŀ��Ƕ�
						sweepAngle=targetAngle;
						//��״̬��Ϊ����״̬
						state=2;
						//��������
						isRunning=false;
						//����ʱ������
						timer.cancel();
					}
					break;
				case 2:
					//������ʼ������Ϊ��������
					isRunning=true;
					sweepAngle -= back[back_index];
					back_index++;
					//����±�������鳤�ȣ��ͼ�һ��ֻȡ���һ��Ԫ��
					if(back_index==back.length){
						back_index--;
					}
					if(sweepAngle<=0){//�����˵�0�Ƕȣ��Ϳ�ʼǰ��
						sweepAngle=0;
						//����Ϊǰ��״̬
						state=1;
					}
					break;
				default:
					break;
				}
				//��ýǶȵİٷֱ�
				float score=sweepAngle/360*100;
				
				if(onAngleChangeListener !=null){
					onAngleChangeListener.onAngleChangeListener(score);
				}
				
				postInvalidate();
			}
		}, 500, 30);

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		// ͨ�����������ÿ�͸�
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);
		// ȡ����Сֵ
		int len = Math.min(width, height);
		oval = new RectF(0, 0, len, len);
		setMeasuredDimension(width, height);
	}

	@Override
	protected void onDraw(Canvas canvas) {

		canvas.drawArc(oval, startAngle, sweepAngle, true, paint);
	}
	
	
	public void setOnAngleChangeListener(OnAngleChangeListener onAngleChangeListener) {
		this.onAngleChangeListener = onAngleChangeListener;
	}

	/**
	 * �����Ƕȵı仯
	 * @author Administrator
	 *
	 */
	public interface OnAngleChangeListener{
		void onAngleChangeListener(float score);
	}
	
}