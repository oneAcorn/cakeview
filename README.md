cakeview
========

<h3>带动画的饼图</h3>

![github](https://github.com/oneAcorn/cakeview/blob/master/cake1.png)
![github](https://github.com/oneAcorn/cakeview/blob/master/cake2.png)
![github](https://github.com/oneAcorn/cakeview/blob/master/cake3.png)
![github](https://github.com/oneAcorn/cakeview/blob/master/cakegif1.gif)
![github](https://github.com/oneAcorn/cakeview/blob/master/cakegif2.gif)

<h3>使用方法</h3>

@Override<br />
<span style="white-space:pre">	</span>protected void onCreate(Bundle savedInstanceState) {<br />
<span style="white-space:pre">		</span>super.onCreate(savedInstanceState);<br />
<span style="white-space:pre">		</span>setContentView(R.layout.activity_main);<br />
<span style="white-space:pre">		</span>cakeSurfaceView = (CakeSurfaceView) findViewById(R.id.cakeSurfaceView1);<br />
<span style="white-space:pre">		</span>List&lt;CakeSurfaceView.CakeValue&gt; cakeValues2 = new ArrayList&lt;CakeSurfaceView.CakeValue&gt;();<br />
<span style="white-space:pre">		</span>cakeValues2.add(new CakeSurfaceView.CakeValue(&quot;猫猫猫&quot;, 10f,&quot;我是猫,我是item0.我是猫,我是item0.我是猫,我是item0.我是猫,我是item0.我是猫,我是item0.我是<span style="white-space:pre">		</span>猫,我是item0.我是猫,我是item0.&quot;));<br />
<span style="white-space:pre">		</span>cakeValues2.add(new CakeSurfaceView.CakeValue(&quot;狗狗狗&quot;, 0f,&quot;狗狗狗,item1.狗狗狗,item1.狗狗狗,item1.狗狗狗,item1.狗狗狗,item1.狗狗狗,item1.狗狗狗,item1.<span style="white-space:pre">		</span>狗狗狗,item1.狗狗狗,item1.狗狗狗,item1.&quot;));<br />
<span style="white-space:pre">		</span>cakeValues2.add(new CakeSurfaceView.CakeValue(&quot;人人人&quot;, 0f,&quot;人,item2.人,item2.人,item2.人,item2.人,item2.人,item2.人,item2.人,item2.人,item2.人,item2.<span style="white-space:pre">			</span>人,item2.人,item2.人,item2.人,item2.人,item2.&quot;));<br />
<span style="white-space:pre">		</span>cakeValues2.add(new CakeSurfaceView.CakeValue(&quot;acorn&quot;, 20f,&quot;橡果&quot;));<br />
<span style="white-space:pre">		</span>cakeValues2.add(new CakeSurfaceView.CakeValue(&quot;瓜皮&quot;, 26f,&quot;瓜皮,item4.瓜皮,item4.瓜皮,item4.瓜皮,item4.瓜皮,item4.瓜皮,item4.瓜皮,item4.瓜皮,item4.瓜<span style="white-space:pre">			</span>皮,item4.瓜皮,item4.瓜皮,item4.&quot;));<br />
<span style="white-space:pre">		</span>cakeValues2.add(new CakeSurfaceView.CakeValue(&quot;鸭嘴兽&quot;, 1f,&quot;鸭嘴兽,item5.鸭嘴兽,item5.鸭嘴兽,item5.鸭嘴兽,item5.鸭嘴兽,item5.鸭嘴兽,item5.鸭嘴兽,item5.<span style="white-space:pre">		</span>鸭嘴兽,item5.鸭嘴兽,item5.&quot;));<br />
<span style="white-space:pre">		</span>cakeValues2.add(new CakeSurfaceView.CakeValue(&quot;面包&quot;, 0f,&quot;面包,item6.面包,item6.面包,item6.面包,item6.面包,item6.面包,item6.面包,item6.面包,item6.面<span style="white-space:pre">			</span>包,item6.面包,item6.面包,item6.面包,item6.&quot;));<br />
<span style="white-space:pre">		</span>cakeValues2.add(new CakeSurfaceView.CakeValue(&quot;dog&quot;, <span style="white-space:pre">		</span>3f,&quot;dog,item7.dog,item7.dog,item7.dog,item7.dog,item7.dog,item7.dog,item7.dog,item7.dog,item7.dog,item7.dog,item7.dog,item7.dog,item7.dog,item7.&quot;));<br />
<span style="white-space:pre">		</span>cakeValues2.add(new CakeSurfaceView.CakeValue(&quot;cat&quot;, <span style="white-space:pre">		</span>13f,&quot;cat,item8.cat,item8.cat,item8.cat,item8.cat,item8.cat,item8.cat,item8.cat,item8.cat,item8.cat,item8.cat,item8.&quot;));<br />
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
