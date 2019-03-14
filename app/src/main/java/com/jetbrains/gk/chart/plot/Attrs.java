package com.jetbrains.gk.chart.plot;

import android.content.res.TypedArray;

import com.jetbrains.gk.chart.R;

public class Attrs {
    private static Attrs instance;

    private int count;
    // TODO: 14.03.2019 create attrs

    public static Attrs getInstance() {
        if (instance == null) {
            instance = new Attrs();
        }
        return instance;
    }

    public void setup(TypedArray typedArray) {
        // TODO: 14.03.2019 init attrs
        count = typedArray.getInt(R.styleable.Plot_chart_count, 0);
        System.out.println(count);
    }
}
