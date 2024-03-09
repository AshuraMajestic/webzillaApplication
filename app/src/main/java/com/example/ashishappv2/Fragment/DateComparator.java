package com.example.ashishappv2.Fragment;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

public class DateComparator implements Comparator<String> {

    @Override
    public int compare(String date1, String date2) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd yyyy", Locale.US);
        try {
            Date d1 = sdf.parse(date1);
            Date d2 = sdf.parse(date2);
            if (d1 != null && d2 != null) {
                return d1.compareTo(d2);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
