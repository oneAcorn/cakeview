package com.acorn.myapplication;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.acorn.library.PieView;
import com.acorn.library.entry.HollowPieEntry;
import com.acorn.library.entry.PieEntry;
import com.acorn.library.interfaces.OnPieViewItemClickListener;
import com.acorn.library.interfaces.PieTextVisibleFilter;

import java.util.ArrayList;
import java.util.List;

public class PieActivity extends AppCompatActivity {
    private PieView mPieView;
    private TextView titleTv, centerTitleTv;
    private Switch mSwitch;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie);
        mPieView = findViewById(R.id.pieView);
        titleTv = findViewById(R.id.title_tv);
        centerTitleTv = findViewById(R.id.center_title_tv);
        mSwitch = findViewById(R.id.switch1);
        mPieView.setPieEntries(getMarsmanElement());
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Toast.makeText(PieActivity.this, "点击任意绿色按钮后生效", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void simpleExample1(View view) {
        changeStyle("2018年全球游戏市场");
        List<PieEntry> pieEntries = new ArrayList<>();
        pieEntries.add(new PieEntry(0.23f, "北美 23%"));
        pieEntries.add(new PieEntry(0.04f, "拉丁美洲 4%"));
        pieEntries.add(new PieEntry(0.52f, "亚太地区 52%"));
        pieEntries.add(new PieEntry(0.21f, "欧洲,中东和非洲 21%"));

        if (mSwitch.isChecked()) {
            mPieView.setHollowPieEntries(pieEntryArrToHollowArr(pieEntries));
        } else {
            mPieView.setPieEntries(pieEntries);
        }
        mPieView.setOnPieViewItemClickListener(new OnPieViewItemClickListener() {
            @Override
            public void onPieClick(PieEntry pieEntry) {
                Toast.makeText(PieActivity.this, "点击:" + pieEntry.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void simpleExample2(View view) {
        changeStyle("2018年全球游戏市场");
        List<PieEntry> pieEntries = new ArrayList<>();
        pieEntries.add(new PieEntry(0.23f, "北美 23%", "$327亿"));
        pieEntries.add(new PieEntry(0.04f, "拉丁美洲 4%", "$50亿"));
        pieEntries.add(new PieEntry(0.52f, "亚太地区 52%", "$714亿"));
        pieEntries.add(new PieEntry(0.21f, "欧洲,中东和非洲 21%", "$287亿"));

        if (mSwitch.isChecked()) {
            mPieView.setHollowPieEntries(pieEntryArrToHollowArr(pieEntries));
        } else {
            mPieView.setPieEntries(pieEntries);
        }
        mPieView.setOnPieViewItemClickListener(null);
    }

    public void simpleExample3(View view) {
        changeStyle("火星人体元素组成");
        if (mSwitch.isChecked()) {
            mPieView.setHollowPieEntries(pieEntryArrToHollowArr(getMarsmanElement()));
        } else {
            mPieView.setPieEntries(getMarsmanElement());
        }
        mPieView.setOnPieViewItemClickListener(null);
    }

    public void varyParameterExample(View view) {
        changeStyle("2018年全球游戏市场");
        //饼图内文字颜色
        int textColor = 0xff000000;
        //饼图内文字大小sp
        int textSize = 12;
        //指示线颜色
        int indicateColor = 0xFFFF0000;
        //指示文字颜色
        int indicateTextColor = 0xFFFF0000;
        //指示文字大小
        int indicateTextSize = 10;
        List<PieEntry> pieEntries = new ArrayList<>();
        PieEntry pieEntry1 = new PieEntry(0.23f, "北美 23%", textColor, textSize, "$327亿", indicateColor, indicateTextColor, indicateTextSize);
        PieEntry pieEntry2 = new PieEntry(0.04f, "拉丁美洲 4%", textColor, textSize, "$50亿", indicateColor, indicateTextColor, indicateTextSize);
        pieEntry2.setShowPieText(false); //不显示饼图文字
        //可单独设置扇形区域各种属性
        PieEntry pieEntry3 = new PieEntry(0.52f, "亚太地区 52%", 0xFF00A438, 16, "$714亿", 0xFF0000FF, 0xFF00FFA0, 12);
        //indicateText传null,则不显示指示线及指示文字
        PieEntry pieEntry4 = new PieEntry(0.21f, "欧洲,中东和非洲 21%", textColor, textSize, null, indicateColor, indicateTextColor, indicateTextSize);
        pieEntry4.setColor(0xff9393FF); //设置饼图颜色

        pieEntries.add(pieEntry1);
        pieEntries.add(pieEntry2);
        pieEntries.add(pieEntry3);
        pieEntries.add(pieEntry4);
        if (mSwitch.isChecked()) {
            mPieView.setHollowPieEntries(pieEntryArrToHollowArr(pieEntries));
        } else {
            mPieView.setPieEntries(pieEntries);
        }
        mPieView.setOnPieViewItemClickListener(null);
    }

    private boolean isNotFilter = false;

    public void filterExampe(View view) {
        //大于等于0.15的显示饼图内文字
        mPieView.setPieTextVisibleFilter(new PieTextVisibleFilter<PieEntry>() {
            @Override
            public boolean isShowText(PieEntry pieEntry) {
                if (isNotFilter)
                    return true;
                return Float.compare(pieEntry.getValue(), 0.15f) == 1 || Float.compare(pieEntry.getValue(), 0.15f) == 0;
            }
        });
        //小于0.15的显示指示线
        mPieView.setPieIndicateTextVisibleFilter(new PieTextVisibleFilter<PieEntry>() {
            @Override
            public boolean isShowText(PieEntry pieEntry) {
                if (isNotFilter)
                    return true;
                return Float.compare(pieEntry.getValue(), 0.15f) == -1;
            }
        });
        isNotFilter = !isNotFilter;
        ((Button) view).setText(isNotFilter ? "禁用文字显示筛选器" : "启用文字显示筛选器");
    }

    public void toggleHighLightEnable(View view) {
        mPieView.setHighlightEnable(!mPieView.isHighlightEnable());
        ((Button) view).setText(mPieView.isHighlightEnable() ? "禁用点击高亮" : "启用点击高亮");
    }

    public void toggleAutoUnpress(View view) {
        mPieView.setAutoUnpressOther(!mPieView.isAutoUnpressOther());
        ((Button) view).setText(mPieView.isAutoUnpressOther() ? "禁用自动收回其他非高亮饼图" : "自动收回其他非高亮饼图");
    }

    private List<PieEntry> getMarsmanElement() {
        int indicateColor = 0xFF063761;
        int indicateTextColor = 0xFF094D1D;
        int textSize = 12;
        int indicateTextSize = 10;
        List<PieEntry> pieEntries = new ArrayList<>();
        pieEntries.add(new PieEntry(0.28f, "C", 0xffffffff, textSize, "碳 28%", indicateColor, indicateTextColor, indicateTextSize));
        pieEntries.add(new PieEntry(0.19f, "N", 0xffffffff, textSize, "氮 19%", indicateColor, indicateTextColor, indicateTextSize));
        pieEntries.add(new PieEntry(0.17f, "P", 0xffffffff, textSize, "磷 17%", indicateColor, indicateTextColor, indicateTextSize));
        pieEntries.add(new PieEntry(0.135f, "H", 0xffffffff, textSize, "氢 13.5%", indicateColor, indicateTextColor, indicateTextSize));
        pieEntries.add(new PieEntry(0.087f, "O", 0xffffffff, textSize, "氧 8.7%", indicateColor, indicateTextColor, indicateTextSize));
        pieEntries.add(new PieEntry(0.06f, "Fe", 0xffffffff, textSize, "铁 6%", indicateColor, indicateTextColor, indicateTextSize));
        pieEntries.add(new PieEntry(0.055f, "S", 0xffffffff, textSize, "硫 5.5%", indicateColor, indicateTextColor, indicateTextSize));
        pieEntries.add(new PieEntry(0.023f, "其他", 0xffffffff, textSize, "少于3%", indicateColor, indicateTextColor, indicateTextSize));
        return pieEntries;
    }

    private List<HollowPieEntry> pieEntryArrToHollowArr(@NonNull List<PieEntry> pieEntries) {
        List<HollowPieEntry> hollowPieEntries = new ArrayList<>();
        float hollowLengthRate = 0.6f; //距圆心多少距离内中空(0~1)
        for (PieEntry pieEntry : pieEntries) {
            HollowPieEntry hollowPieEntry = new HollowPieEntry(pieEntry, hollowLengthRate);
            hollowPieEntries.add(hollowPieEntry);
        }
        return hollowPieEntries;
    }

    private void changeStyle(String title) {
        if (mSwitch.isChecked()) {
            centerTitleTv.setText(title);
            mPieView.setBackgroundColor(0xFF7EC471);
        } else {
            mPieView.setBackgroundColor(0);
        }
        titleTv.setText(title);
        centerTitleTv.setVisibility(mSwitch.isChecked() ? View.VISIBLE : View.GONE);
    }
}
