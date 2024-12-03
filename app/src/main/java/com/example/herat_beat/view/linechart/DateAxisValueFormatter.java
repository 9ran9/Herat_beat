package com.example.herat_beat.view.linechart;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DateAxisValueFormatter extends ValueFormatter implements IAxisValueFormatter {
    private List<Date> dates;
    private SimpleDateFormat dateFormat;

    public DateAxisValueFormatter(List<Date> dates) {
        this.dates = dates;
        dateFormat = new SimpleDateFormat("MM月dd日", Locale.getDefault());
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        int index = (int) value;
        if (index >= 0 && index < dates.size()) {
            Date date = dates.get(index);
            return dateFormat.format(date);
        }
        return "";
    }
}