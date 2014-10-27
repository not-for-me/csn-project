package org.csn.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeGenerator {
    static Logger logger = LoggerFactory.getLogger(TimeGenerator.class);
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

    public static String getCurrentTimestamp() {
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        String now = new SimpleDateFormat(DATE_FORMAT).format(date);
        return now;
    }

    public static long convertDateToEpoch(String date) {
        long epoch = 0;
        try {
            Date tempDate = new SimpleDateFormat(DATE_FORMAT).parse(date);
            epoch = tempDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            return epoch;
        }
    }

    public static String convertEpochToDate(long epoch) {
        Date date = new Date(epoch);
        return new SimpleDateFormat(DATE_FORMAT).format(date);
    }
}
