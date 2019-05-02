package com.acorn.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toast;

import com.acorn.library.PieView;
import com.acorn.library.drawable.BaseSectorDrawable;
import com.acorn.library.drawable.HollowSectorDrawable;
import com.acorn.library.entry.HollowPieEntry;
import com.acorn.library.entry.PieEntry;
import com.acorn.library.listener.OnPieViewItemClickListener;
import com.acorn.library.listener.SectorFactory;

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

//        mPieView.setPieEntries(getTestData1());
        mPieView.setPieEntries(getTestData4(), new SectorFactory<HollowPieEntry>() {
            @Override
            public BaseSectorDrawable<HollowPieEntry> createSector(HollowPieEntry pieEntry, int position) {
                return new HollowSectorDrawable(pieEntry);
            }
        });
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
        pieEntries.add(new PieEntry(0.3f, "1"));
        pieEntries.add(new PieEntry(0.2f, "2"));
        pieEntries.add(new PieEntry(0.4f, "3"));
        pieEntries.add(new PieEntry(0.1f, "4"));
        return pieEntries;
    }

    private List<PieEntry> getTestData2() {
        List<PieEntry> pieEntries = new ArrayList<>();
        pieEntries.add(new PieEntry(0.03f, "1"));
        pieEntries.add(new PieEntry(0.02f, "2"));
        pieEntries.add(new PieEntry(0.04f, "3"));
        pieEntries.add(new PieEntry(0.01f, "4"));
        pieEntries.add(new PieEntry(0.06f, "5"));
        pieEntries.add(new PieEntry(0.04f, "6"));
        pieEntries.add(new PieEntry(0.08f, "7"));
        pieEntries.add(new PieEntry(0.02f, "8"));
        pieEntries.add(new PieEntry(0.3f, "9"));
        pieEntries.add(new PieEntry(0.1f, "10"));
        pieEntries.add(new PieEntry(0.18f, "11"));
        pieEntries.add(new PieEntry(0.02f, "12"));
        pieEntries.add(new PieEntry(0.03f, "13"));
        pieEntries.add(new PieEntry(0.02f, "14"));
        pieEntries.add(new PieEntry(0.04f, "15"));
        pieEntries.add(new PieEntry(0.01f, "16"));
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

    private List<PieEntry> getTestData4() {
        List<PieEntry> pieEntries = new ArrayList<>();
        pieEntries.add(new HollowPieEntry(1f / 7f, "1", 14, 0xFFFF0000, 0.6f));
        pieEntries.add(new HollowPieEntry(1f / 7f, "1", 14, 0xFFFF7F00, 0.6f));
        pieEntries.add(new HollowPieEntry(1f / 7f, "1", 14, 0xFFFFFF00, 0.6f));
        pieEntries.add(new HollowPieEntry(1f / 7f, "1", 14, 0xFF00FF00, 0.6f));
        pieEntries.add(new HollowPieEntry(1f / 7f, "1", 14, 0xFF00FFFF, 0.6f));
        pieEntries.add(new HollowPieEntry(1f / 7f, "1", 14, 0xFF0000FF, 0.6f));
        pieEntries.add(new HollowPieEntry(1f / 7f, "1", 14, 0xFF8B00FF, 0.6f));
        return pieEntries;
    }

//    private List<PieEntry> getTestData5() {
//        List<PieEntry> pieEntries = new ArrayList<>();
//        pieEntries.add(new PieEntry(1f,"sdaa"));
//        pieEntries.add(new HollowPieEntry(123f,"fdsa"));
//    }
}
