# cakeview
可触摸操作的饼图

拖动旋转,点击高亮及惯性动画

![github](https://github.com/oneAcorn/cakeview/blob/master/20190506_092928.gif "拖动旋转,点击高亮及惯性动画")

中空模式

![github](https://github.com/oneAcorn/cakeview/blob/master/20190506_093340.gif "拖动旋转,点击高亮及惯性动画")

### 一 引用方法

##### 1.在root build.gradle中加入

```gradle
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

##### 2.在项目的 build.gradle中加入

```gradle
dependencies {
	        implementation 'com.github.oneAcorn:cakeview:1.0'
	}
```

### 二 使用方法(更多详细设置请下载代码,并参看PieActivity内demo.)

##### 1.在xml中声明

```xml
          <com.acorn.library.PieView
            android:id="@+id/pieView"
            android:layout_width="match_parent"
            android:layout_height="300dp"
                />
```

##### 2.在代码中 

普通模式饼图

```java
        //简单示例,更多参数请下载代码,并参看PieActivity内demo.
        List<PieEntry> pieEntries = new ArrayList<>();
        //参数1:扇形区域占比,参数2:饼图内文字,参数3:指示线文字
        pieEntries.add(new PieEntry(0.23f, "北美 23%", "$327亿"));
        pieEntries.add(new PieEntry(0.04f, "拉丁美洲 4%", "$50亿"));
        pieEntries.add(new PieEntry(0.52f, "亚太地区 52%", "$714亿"));
        pieEntries.add(new PieEntry(0.21f, "欧洲,中东和非洲 21%", "$287亿"));
        mPieView.setPieEntries(pieEntries);
```

中空模式饼图

```java
        //简单示例,更多参数请下载代码,并参看PieActivity内demo.
        List<HollowPieEntry> pieEntries = new ArrayList<>();
        //参数1:扇形区域占比,参数2:饼图内文字,参数3:指示线文字
        pieEntries.add(new HollowPieEntry(0.23f, "北美 23%", "$327亿"));
        pieEntries.add(new HollowPieEntry(0.04f, "拉丁美洲 4%", "$50亿"));
        pieEntries.add(new HollowPieEntry(0.52f, "亚太地区 52%", "$714亿"));
        pieEntries.add(new HollowPieEntry(0.21f, "欧洲,中东和非洲 21%", "$287亿"));
        mPieView.setHollowPieEntries(pieEntries);
```

##### 3.其他设置

按筛选条件显示/隐藏文字

![github](https://github.com/oneAcorn/cakeview/blob/master/20190506_093532.gif "按筛选条件显示/隐藏文字")

* 1 只显示占比大于等于15%的饼图内文字
* 2 只显示占比小于15%的指示线文字

```java
        //大于等于0.15的显示饼图内文字
        mPieView.setPieTextVisibleFilter(new PieTextVisibleFilter<PieEntry>() {
            @Override
            public boolean isShowText(PieEntry pieEntry) {
                return Float.compare(pieEntry.getValue(), 0.15f) == 1 || Float.compare(pieEntry.getValue(), 0.15f) == 0;
            }
        });
        //小于0.15的显示指示线
        mPieView.setPieIndicateTextVisibleFilter(new PieTextVisibleFilter<PieEntry>() {
            @Override
            public boolean isShowText(PieEntry pieEntry) {
                return Float.compare(pieEntry.getValue(), 0.15f) == -1;
            }
        });
```

去掉点击高亮效果

```java
  mPieView.setHighlightEnable(false);
```

关闭点击高亮区域时自动收回其他区域的效果

```java
 mPieView.setAutoUnpressOther(false);
```

### 三 自定义饼图
如果想实现自己的饼图
* 1 需要先继承抽象类BaseSectorDrawable,实现其中必要的抽象方法,可参照SectorDrawable/HollowSectorDrawable
* 2 继承抽象类BaseTextDrawable,饼图内文字(SectorTextDrawable/HollowSectorDrawable)
和指示线文字(SectorIndicateTextDrawable/HollowSectorIndicateTextDrawable)都是该抽象类的子类,可供参考.
* 3 BaseSectorDrawable和BaseTextDrawable的绑定通过调用BaseSectorDrawable内的方法设置监听器
```java
    public <K extends OnSectorChangeListener<T>> void addOnSectorChangeListener(K onSectorChangeListener) {
        if (null == mOnSectorChangeListeners)
            mOnSectorChangeListeners = new ArrayList<>();
        mOnSectorChangeListeners.add(onSectorChangeListener);
    }
```

然后通过BaseTextDrawable内的抽象方法
```java
 public abstract void setTextPoint(T pieEntry, int cx, int cy, int radius, int source);
```
通知文字Drawable扇形区域的初始化,旋转,高亮等的状态变化,请自行根据扇形区状态更新文字状态
* 4 可根据自己需要继承PieEntry后,新增必要的参数
* 5 最后调用PieView内方法,该方法通过工厂模式生成相应的扇形区,饼图内文字,指示线文字Drawable,请自行实现工厂生成接口
```java
    /**
     *
     * @param pieEntries 饼图实体
     * @param sectorFactory 饼图工厂
     * @param pieTextFactory 饼图内文字工厂
     * @param pieIndicateTextFactory 指示线文字工厂
     */
    public <T extends PieEntry, K extends BaseSectorDrawable, U extends BaseTextDrawable, E extends BaseTextDrawable>
    void setPieEntries(List<T> pieEntries, SectorFactory<T, ? super K> sectorFactory, PieTextFactory<? super U> pieTextFactory, PieTextFactory<? super E> pieIndicateTextFactory) {
        if (null == pieEntries || pieEntries.isEmpty()) {
            throw new IllegalStateException("pieEntries is null or empty");
        }
        ensureSectorDrawables(revisePieEntries(pieEntries), sectorFactory, pieTextFactory, pieIndicateTextFactory);
        invalidate();
    }
```
可参照中空模式饼图的实现
```java
        setPieEntries(pieEntries, new SectorFactory<HollowPieEntry, HollowSectorDrawable>() {
            @Override
            public HollowSectorDrawable createSector(@NonNull HollowPieEntry pieEntry, int position) {
                return new HollowSectorDrawable(pieEntry);
            }
        }, new PieTextFactory<HollowSectorTextDrawable>() {
            @Override
            public HollowSectorTextDrawable createPieText() {
                return new HollowSectorTextDrawable(getContext());
            }
        }, new PieTextFactory<HollowSectorIndicateTextDrawable>() {
            @Override
            public HollowSectorIndicateTextDrawable createPieText() {
                return new HollowSectorIndicateTextDrawable(getContext());
            }
        });
```
