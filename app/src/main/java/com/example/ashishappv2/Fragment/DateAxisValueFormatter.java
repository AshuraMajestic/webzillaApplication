package com.example.ashishappv2.Fragment;

import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateAxisValueFormatter implements IAxisValueFormatter {

    private SimpleDateFormat sdf;

    public DateAxisValueFormatter() {
        // Initialize the date format
        sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        // Convert the float value (milliseconds) to a Date object
        Date date = new Date((long) value);
        // Format the Date object using the SimpleDateFormat
        return sdf.format(date);
    }
}