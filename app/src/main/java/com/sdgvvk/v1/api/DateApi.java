package com.sdgvvk.v1.api;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateApi {

    public static final long MILLISECONDS_IN_DAY = (long) (1000 * 60 * 60 * 24);


    public static int daysDiff(Date from, Date until) {
        Calendar cFrom = Calendar.getInstance();
        cFrom.setTime(DateApi.getDateAtNoon(from));
        int cFromDSTOffset = cFrom.get(Calendar.DST_OFFSET);
        long cFromTime = cFrom.getTime().getTime() + (long) cFromDSTOffset;
        Calendar cUntil = Calendar.getInstance();
        cUntil.setTime(DateApi.getDateAtNoon(until));
        int cUntilDSTOffset = cUntil.get(Calendar.DST_OFFSET);
        long cUntilTime = cUntil.getTime().getTime() + (long) cUntilDSTOffset;
        return (int) ((cUntilTime - cFromTime) / MILLISECONDS_IN_DAY);
    }


    public static Date getDateAtNoon(Date datetime) {
        if (datetime == null)
            return null;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(datetime);
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static String addDaysToDateToddmmyyyy(String oldDate, int daysToAdd) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar c = Calendar.getInstance();
        try {
            //Setting the date to the given date
            c.setTime(sdf.parse(oldDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //Number of Days to add
        c.add(Calendar.DAY_OF_MONTH, daysToAdd);
        //Date after adding the days to the given date
        String newDate = sdf.format(c.getTime());

        return newDate;
    }


}
