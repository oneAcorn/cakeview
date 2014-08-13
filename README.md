cakeview
========

带动画的饼图\<h3\>

![github](https://github.com/oneAcorn/cakeview/blob/master/cake1.png)
![github](https://github.com/oneAcorn/cakeview/blob/master/cake2.png)
![github](https://github.com/oneAcorn/cakeview/blob/master/cake3.png)
![github](https://github.com/oneAcorn/cakeview/blob/master/cakegif1.gif)
![github](https://github.com/oneAcorn/cakeview/blob/master/cakegif2.gif)

使用方法\<h3\>
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
