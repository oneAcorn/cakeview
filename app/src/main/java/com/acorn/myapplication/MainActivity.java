package com.acorn.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.acorn.library.OnPieViewItemClickListener;
import com.acorn.library.PieEntry;
import com.acorn.library.PieView;
import com.acorn.library.SectorDrawable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

        mPieView.setPieEntries(getTestData2());
        mPieView.setOnPieViewItemClickListener(new OnPieViewItemClickListener() {
            @Override
            public void onPieClick(SectorDrawable sectorDrawable, int position) {
                if (null == sectorDrawable) {
                    Toast.makeText(MainActivity.this, "没点到", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(MainActivity.this, sectorDrawable.getPieEntry().getTitle() + "," + sectorDrawable.isHighlighting(), Toast.LENGTH_SHORT).show();
                if (sectorDrawable.isHighlighting()) {
                    sectorDrawable.unPress();
                } else {
                    sectorDrawable.press();
                }
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
}
