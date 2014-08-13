cakeview
========

<h3>带动画的饼图</h3>

![github](https://github.com/oneAcorn/cakeview/blob/master/cakegif1.gif)
![github](https://github.com/oneAcorn/cakeview/blob/master/cakegif2.gif)
![github](https://github.com/oneAcorn/cakeview/blob/master/cake2.png)

<h3>详细说明参见:</h3>
http://www.eoeandroid.com/thread-540249-2-1.html

<h3>使用方法</h3>

@Override<br />
<span style="white-space:pre">	</span>protected void onCreate(Bundle savedInstanceState) {<br />
<span style="white-space:pre">		</span>super.onCreate(savedInstanceState);<br />
<span style="white-space:pre">		</span>setContentView(R.layout.activity_main);<br />
<span style="white-space:pre">		</span>cakeSurfaceView = (CakeSurfaceView) findViewById(R.id.cakeSurfaceView1);<br />
<span style="white-space:pre">		</span>List&lt;CakeSurfaceView.CakeValue&gt; cakeValues2 = new ArrayList&lt;CakeSurfaceView.CakeValue&gt;();<br />
<span style="white-space:pre">		</span>cakeValues2.add(new CakeSurfaceView.CakeValue(&quot;猫猫猫&quot;, 12f,&quot;详细信息&quot;));<br />
<span style="white-space:pre">		</span>cakeValues2.add(new CakeSurfaceView.CakeValue(&quot;狗狗狗&quot;, 0f,&quot;详细信息自动换行&quot;));<br />
<span style="white-space:pre">		</span>cakeValues2.add(new CakeSurfaceView.CakeValue(&quot;acorn&quot;, 24f,&quot;橡果&quot;));<br />
<span style="white-space:pre">		</span>cakeValues2.add(new CakeSurfaceView.CakeValue(&quot;人人人&quot;, 0f));<br />
<span style="white-space:pre">		</span>cakeValues2.add(new CakeSurfaceView.CakeValue(&quot;瓜皮&quot;, 0f));<br />
<span style="white-space:pre">		</span>cakeValues2.add(new CakeSurfaceView.CakeValue(&quot;鸭嘴兽&quot;, 1f));<br />
<span style="white-space:pre">		</span>cakeSurfaceView.setData(cakeValues2);<br />
<span style="white-space:pre">		</span>//设置饼图信息的显示位置(目前只有bottom模式支持点击动画)<br />
<span style="white-space:pre">		</span>cakeSurfaceView.setGravity(Gravity.bottom);<br />
<span style="white-space:pre">		</span>//设置饼图信息与饼图的间隔(dp)<br />
<span style="white-space:pre">		</span>cakeSurfaceView.setDetailTopSpacing(15);<br />
<span style="white-space:pre">		</span>//设置饼图的每一项的点击事件<br />
<span style="white-space:pre">		</span>cakeSurfaceView.setOnItemClickListener(new OnItemClickListener() {<br />
<br />
<br />
<span style="white-space:pre">			</span>@Override<br />
<span style="white-space:pre">			</span>public void onItemClick(int position) {<br />
<span style="white-space:pre">				</span>Toast.makeText(MainActivity.this, &quot;点击:&quot; + position, 0).show();<br />
<span style="white-space:pre">			</span>}<br />
<span style="white-space:pre">		</span>});<br />
<span style="white-space:pre">	</span>}
