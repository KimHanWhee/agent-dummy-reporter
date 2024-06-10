package kr.standard.ums.Util;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class Util {
    public static String getCurrentTime() {
        Format format = new SimpleDateFormat("yyyyMMddHHmmss");
        Calendar calendar = Calendar.getInstance();

        return format.format(calendar.getTime());
    }

    public static String getTimeAfter1Hour() {
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime oneHourLater = now.plusHours(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        return oneHourLater.atZone(ZoneId.systemDefault()).format(formatter);
    }

}
