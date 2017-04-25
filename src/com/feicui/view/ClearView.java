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
	//声明接口对象
	private OnAngleChangeListener onAngleChangeListener;
	Paint paint;

	public ClearView(Context context, AttributeSet attrs) {
		super(context, attrs);
		startAngle = -90;
		sweepAngle = 360;
		// 设置一个状态，判断应该前进还是应该后退
		// 约定=1，前进，=2后退
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
	 * 角度的动态绘制
	 * @param targetAngle  真实的角度
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
					if(sweepAngle>=targetAngle){//运行到目标角度
						sweepAngle=targetAngle;
						//将状态改为后退状态
						state=2;
						//动画结束
						isRunning=false;
						//结束时间任务
						timer.cancel();
					}
					break;
				case 2:
					//动画开始就设置为正在运行
					isRunning=true;
					sweepAngle -= back[back_index];
					back_index++;
					//如果下标等于数组长度，就减一，只取最后一个元素
					if(back_index==back.length){
						back_index--;
					}
					if(sweepAngle<=0){//当回退到0角度，就开始前进
						sweepAngle=0;
						//设置为前进状态
						state=1;
					}
					break;
				default:
					break;
				}
				//获得角度的百分比
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
		// 通过测量规则获得宽和高
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);
		// 取出最小值
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
	 * 监听角度的变化
	 * @author Administrator
	 *
	 */
	public interface OnAngleChangeListener{
		void onAngleChangeListener(float score);
	}
	
}