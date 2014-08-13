package com.acorn.cakeviewwidget;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.acorn.cakeviewwidget.CakeSurfaceView.Gravity;
import com.acorn.cakeviewwidget.CakeSurfaceView.OnItemClickListener;

public class MainActivity extends Activity {
	private CakeSurfaceView cakeSurfaceView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		cakeSurfaceView = (CakeSurfaceView) findViewById(R.id.cakeSurfaceView1);
		List<CakeSurfaceView.CakeValue> cakeValues2 = new ArrayList<CakeSurfaceView.CakeValue>();
		cakeValues2.add(new CakeSurfaceView.CakeValue("猫猫猫", 10f,"我是猫,我是item0.我是猫,我是item0.我是猫,我是item0.我是猫,我是item0.我是猫,我是item0.我是猫,我是item0.我是猫,我是item0."));
		cakeValues2.add(new CakeSurfaceView.CakeValue("狗狗狗", 0f,"狗狗狗,item1.狗狗狗,item1.狗狗狗,item1.狗狗狗,item1.狗狗狗,item1.狗狗狗,item1.狗狗狗,item1.狗狗狗,item1.狗狗狗,item1.狗狗狗,item1."));
		cakeValues2.add(new CakeSurfaceView.CakeValue("人人人", 0f,"人,item2.人,item2.人,item2.人,item2.人,item2.人,item2.人,item2.人,item2.人,item2.人,item2.人,item2.人,item2.人,item2.人,item2.人,item2."));
		cakeValues2.add(new CakeSurfaceView.CakeValue("acorn", 20f,"橡果"));
		cakeValues2.add(new CakeSurfaceView.CakeValue("瓜皮", 26f,"瓜皮,item4.瓜皮,item4.瓜皮,item4.瓜皮,item4.瓜皮,item4.瓜皮,item4.瓜皮,item4.瓜皮,item4.瓜皮,item4.瓜皮,item4.瓜皮,item4."));
		cakeValues2.add(new CakeSurfaceView.CakeValue("鸭嘴兽", 1f,"鸭嘴兽,item5.鸭嘴兽,item5.鸭嘴兽,item5.鸭嘴兽,item5.鸭嘴兽,item5.鸭嘴兽,item5.鸭嘴兽,item5.鸭嘴兽,item5.鸭嘴兽,item5."));
		cakeValues2.add(new CakeSurfaceView.CakeValue("面包", 0f,"面包,item6.面包,item6.面包,item6.面包,item6.面包,item6.面包,item6.面包,item6.面包,item6.面包,item6.面包,item6.面包,item6.面包,item6."));
		cakeValues2.add(new CakeSurfaceView.CakeValue("dog", 3f,"dog,item7.dog,item7.dog,item7.dog,item7.dog,item7.dog,item7.dog,item7.dog,item7.dog,item7.dog,item7.dog,item7.dog,item7.dog,item7.dog,item7."));
		cakeValues2.add(new CakeSurfaceView.CakeValue("cat", 13f,"cat,item8.cat,item8.cat,item8.cat,item8.cat,item8.cat,item8.cat,item8.cat,item8.cat,item8.cat,item8.cat,item8."));
		cakeSurfaceView.setData(cakeValues2);
		cakeSurfaceView.setGravity(Gravity.bottom);
		cakeSurfaceView.setDetailTopSpacing(15);
//		cakeSurfaceView.setHighLightOffset(10);
		cakeSurfaceView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(int position) {
				Toast.makeText(MainActivity.this, "点击:" + position, 0).show();
			}
		});
		Log.v("ts", "mainactivity create;");


	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
