cakeview
========

<h3>带动画的饼图</h3>

![github](https://github.com/oneAcorn/cakeview/blob/master/cakegif1.gif)
![github](https://github.com/oneAcorn/cakeview/blob/master/cakegif2.gif)
![github](https://github.com/oneAcorn/cakeview/blob/master/cake2.png)

<h3>详细说明参见:</h3>
http://www.eoeandroid.com/thread-540249-2-1.html

<h3>使用方法</h3>

@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		cakeSurfaceView = (CakeSurfaceView) findViewById(R.id.cakeSurfaceView1);
		List<CakeSurfaceView.CakeValue> cakeValues2 = new ArrayList<CakeSurfaceView.CakeValue>();
		cakeValues2.add(new CakeSurfaceView.CakeValue("猫猫猫", 12f,"详细信息"));
		cakeValues2.add(new CakeSurfaceView.CakeValue("狗狗狗", 0f,"详细信息自动换行"));
		cakeValues2.add(new CakeSurfaceView.CakeValue("acorn", 24f,"橡果"));
		cakeValues2.add(new CakeSurfaceView.CakeValue("人人人", 0f));
		cakeValues2.add(new CakeSurfaceView.CakeValue("瓜皮", 0f));
		cakeValues2.add(new CakeSurfaceView.CakeValue("鸭嘴兽", 1f));
		cakeSurfaceView.setData(cakeValues2);
		//设置饼图信息的显示位置(目前只有bottom模式支持点击动画)
		cakeSurfaceView.setGravity(Gravity.bottom);
		//设置饼图信息与饼图的间隔(dp)
		cakeSurfaceView.setDetailTopSpacing(15);
		//设置饼图的每一项的点击事件
		cakeSurfaceView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(int position) {
				Toast.makeText(MainActivity.this, "点击:" + position, 0).show();
			}
		});
	}
