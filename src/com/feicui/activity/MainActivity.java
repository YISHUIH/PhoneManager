package com.feicui.activity;

import java.text.DecimalFormat;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.feicui.base.BaseActivity;
import com.feicui.manager.MemoryManager;
import com.feicui.manager.RunAppManager;
import com.feicui.manager.SoftManager;
import com.feicui.utils.Toastutil;
import com.feicui.view.ClearView;
import com.feicui.view.MyView;
import com.feicui.view.ClearView.OnAngleChangeListener;
import com.feicui.view.MyView.OnAngleColorListener;

public class MainActivity extends BaseActivity implements OnClickListener {
	TextView tv_tel, tv_speed, tv_score,tv_soft,tv_check;
	Button bt_go;
	ClearView cv_clear;
	LinearLayout main_rl;
	private StringBuffer sb;

	private MyView mv_speed;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		sb = new StringBuffer();
		setTitle();
		initView();
		initEvent();
		mv_speed.setOnAngleColorListener(onAngleColorListener);
	}
	//角度颜色渐变监听
	private OnAngleColorListener onAngleColorListener = new OnAngleColorListener() {

		@Override
		public void onAngleColorListener(int red,int green) {
			Color color=new Color();
			int c=color.argb(150, red, green, 0);
			main_rl.setBackgroundColor(c);
		}
	};

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		showScore();
	}

	private void initEvent() {
		tv_tel.setOnClickListener(this);
		tv_speed.setOnClickListener(this);
		tv_soft.setOnClickListener(this);
		tv_check.setOnClickListener(this);
		// 手机加速按钮监听
		bt_go.setOnClickListener(this);
		// 设置圆角度百分比监听
		cv_clear.setOnAngleChangeListener(onAngleChangeListener);
		// 刻度盘形状的
		mv_speed.setOnClickListener(this);
		
	}

	private void initView() {
		tv_tel = (TextView) findViewById(R.id.tv_tel);
		tv_speed = (TextView) findViewById(R.id.tv_speed);
		tv_score = (TextView) findViewById(R.id.tv_score);
		tv_soft=(TextView) findViewById(R.id.tv_soft);
		tv_check=(TextView) findViewById(R.id.tv_check);
		bt_go = (Button) findViewById(R.id.bt_go);
		cv_clear = (ClearView) findViewById(R.id.cv_clear);
		// 刻度盘形状的
		mv_speed = (MyView) findViewById(R.id.mv_speed);
		
		main_rl = (LinearLayout) findViewById(R.id.main_rl);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_tel:
			startActivity(TelManagerActivity.class);
			break;
		case R.id.tv_speed:
			startActivity(SpeedUpActivity.class);
			break;
		case R.id.tv_soft:
			startActivity(SoftManagerActivity.class);
			break;
		case R.id.tv_check:
			startActivity(PhoneCheckActivity.class);
			break;
		case R.id.bt_go:
			cv_clear.changeAngle(200);
			break;
		case R.id.mv_speed:
			// 先杀进程
			new Thread(new Runnable() {

				@Override
				public void run() {
					RunAppManager.killAllUser(MainActivity.this);
					// 动态显示
					showScore();
				}
			}).start();

			break;
			
			
		default:
			break;
		}

	}

	/**
	 * 查看当前内存使用情况
	 */
	private void showScore() {
		// 获取内存使用情况
		long allRam = MemoryManager.getPhoneTotalRamMemory();
		long freeRam = MemoryManager.getPhoneFreeRamMemory(this);
		// 已占用内存所占比例
		float score = (freeRam) / (float) allRam * 300;
		//动态显示分数
		mv_speed.change(score);
		//动态水球
		mv_speed.moveWaterLine();
		
	}

	private OnAngleChangeListener onAngleChangeListener = new OnAngleChangeListener() {

		@Override
		public void onAngleChangeListener(final float score) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// 小数格式化，保留两位小数
					DecimalFormat df = new DecimalFormat("0.00");
					// 得到解析后的字符串
					sb.append(df.format(score));
					tv_score.setText("" + sb.toString());
					sb.delete(0, sb.capacity());
				}
			});

		}
	};

}
