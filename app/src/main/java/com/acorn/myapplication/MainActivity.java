package com.acorn.myapplication;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toast;

import com.acorn.library.PieView;
import com.acorn.library.drawable.BaseSectorDrawable;
import com.acorn.library.drawable.HollowSectorDrawable;
import com.acorn.library.drawable.HollowSectorIndicateTextDrawable;
import com.acorn.library.drawable.HollowSectorTextDrawable;
import com.acorn.library.entry.HollowPieEntry;
import com.acorn.library.entry.PieEntry;
import com.acorn.library.interfaces.OnPieViewItemClickListener;
import com.acorn.library.interfaces.PieTextFactory;
import com.acorn.library.interfaces.SectorFactory;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private PieView mPieView;
    private int times = 0;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPieView = findViewById(R.id.pieView);
        btn = findViewById(R.id.btn);

//        mPieView.setPieEntries(getTestData2());
//        mPieView.setPieTextVisible(new PieTextVisibleFilter() {
//            @Override
//            public boolean isShowText(PieEntry pieEntry) {
//                return true;
//            }
//        });
//        mPieView.setPieEntries(getTestData4(), new SectorFactory<HollowPieEntry, HollowSectorDrawable>() {
//            @Override
//            public HollowSectorDrawable createSector(HollowPieEntry pieEntry, int position) {
//                return null;
//            }
//        }, new PieTextFactory<HollowPieEntry, HollowSectorTextDrawable>() {
//            @Override
//            public HollowSectorTextDrawable createPieText(@NonNull HollowPieEntry pieEntry) {
//                return null;
//            }
//        }, new PieTextFactory<HollowPieEntry, HollowSectorIndicateTextDrawable>() {
//            @Override
//            public HollowSectorIndicateTextDrawable createPieText(@NonNull HollowPieEntry pieEntry) {
//                return null;
//            }
//        });
        mPieView.setPieEntries(getTestData4());
        mPieView.setHighlightEnable(true);
        mPieView.setAutoUnpressOther(true);
        mPieView.setOnPieViewItemClickListener(new OnPieViewItemClickListener() {
            @Override
            public void onPieClick(BaseSectorDrawable sectorDrawable, int position) {
                if (null == sectorDrawable) {
                    Toast.makeText(MainActivity.this, "没点到", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(MainActivity.this, sectorDrawable.getPieEntry().getTitle() + "," + sectorDrawable.isHighlighting(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private List<PieEntry> getTestData1() {
        List<PieEntry> pieEntries = new ArrayList<>();
        PieEntry pieEntry = new PieEntry(0.3f, "的撒范德萨就够了", 14);
        pieEntry.setTextColor(0xffffffff);
        pieEntries.add(pieEntry);
        pieEntries.add(new PieEntry(0.2f, "2"));
        pieEntries.add(new PieEntry(0.4f, "3"));
        pieEntries.add(new PieEntry(0.1f, "4"));
        return pieEntries;
    }

    private List<PieEntry> getTestData2() {
        List<PieEntry> pieEntries = new ArrayList<>();
        PieEntry pieEntry1 = new PieEntry(0.03f, "桂丰大厦1");
        pieEntry1.setIndicateText("发的");
        pieEntries.add(pieEntry1);
        PieEntry pieEntry2 = new PieEntry(0.03f, "ga啊沙发2");
        pieEntry2.setIndicateText("猪");
        pieEntries.add(pieEntry2);
        pieEntries.add(new PieEntry(0.04f, "的撒个3"));
        pieEntries.add(new PieEntry(0.01f, "三国杀v型4"));
        pieEntries.add(new PieEntry(0.06f, "5"));
        pieEntries.add(new PieEntry(0.04f, "6"));
        pieEntries.add(new PieEntry(0.08f, "7"));
        pieEntries.add(new PieEntry(0.02f, "8"));
        PieEntry pieEntry3 = new PieEntry(0.3f, "asfdadsf");
        pieEntry3.setIndicateText("假发啊啊");
        pieEntries.add(pieEntry3);
        pieEntries.add(new PieEntry(0.1f, "10"));
        pieEntries.add(new PieEntry(0.18f, "啊沙发打算11"));
        PieEntry pieEntry4 = new PieEntry(0.02f, "地方撒的撒");
        pieEntry4.setIndicateText("家禽");
        pieEntries.add(pieEntry4);
        pieEntries.add(new PieEntry(0.03f, "13"));
        pieEntries.add(new PieEntry(0.02f, "14"));
        pieEntries.add(new PieEntry(0.04f, "15"));
        return pieEntries;
    }

//    private List<ImagePieEntry> getTestData3() {
//        Resources resources = getResources();
//        List<ImagePieEntry> pieEntries = new ArrayList<>();
//        pieEntries.add(new ImagePieEntry(0.5f, "1", ImageUtil.drawableToBitamp(resources.getDrawable(R.mipmap.award2))));
//        pieEntries.add(new ImagePieEntry(0.2f, "2", ImageUtil.drawableToBitamp(resources.getDrawable(R.mipmap.award_ps4))));
//        pieEntries.add(new ImagePieEntry(0.3f, "3", ImageUtil.drawableToBitamp(resources.getDrawable(R.mipmap.award3))));
////        pieEntries.add(new ImagePieEntry(1f, "3", ImageUtil.drawableToBitamp(resources.getDrawable(R.mipmap.test))));
//        return pieEntries;
//    }

    private List<HollowPieEntry> getTestData4() {
        List<HollowPieEntry> pieEntries = new ArrayList<>();
        HollowPieEntry hollowPieEntry1 = new HollowPieEntry(1f / 7f, "1", 14, 0xFFFF0000, 0.6f);
        hollowPieEntry1.setIndicateText("赤");
        pieEntries.add(hollowPieEntry1);
        HollowPieEntry hollowPieEntry2 = new HollowPieEntry(1f / 7f, "2", 14, 0xFFFF7F00, 0.6f);
        hollowPieEntry2.setIndicateText("橙");
        pieEntries.add(hollowPieEntry2);
        HollowPieEntry hollowPieEntry3 = new HollowPieEntry(1f / 7f, "3", 14, 0xFFFFFF00, 0.6f);
        hollowPieEntry3.setIndicateText("黄");
        pieEntries.add(hollowPieEntry3);
        HollowPieEntry hollowPieEntry4 = new HollowPieEntry(1f / 7f, "4", 14, 0xFF00FF00, 0.6f);
        hollowPieEntry4.setIndicateText("绿");
        pieEntries.add(hollowPieEntry4);
        HollowPieEntry hollowPieEntry5 = new HollowPieEntry(1f / 7f, "5", 14, 0xFF00FFFF, 0.6f);
        hollowPieEntry5.setIndicateText("青");
        pieEntries.add(hollowPieEntry5);
        HollowPieEntry hollowPieEntry6 = new HollowPieEntry(1f / 7f, "6", 14, 0xFF0000FF, 0.6f);
        hollowPieEntry6.setIndicateText("蓝");
        pieEntries.add(hollowPieEntry6);
        HollowPieEntry hollowPieEntry7 = new HollowPieEntry(1f / 7f, "7", 14, 0xFF8B00FF, 0.6f);
        hollowPieEntry7.setIndicateText("紫");
        pieEntries.add(hollowPieEntry7);
        return pieEntries;
    }

//    private List<PieEntry> getTestData5() {
//        List<PieEntry> pieEntries = new ArrayList<>();
//        pieEntries.add(new PieEntry(1f,"sdaa"));
//        pieEntries.add(new HollowPieEntry(123f,"fdsa"));
//    }
}
