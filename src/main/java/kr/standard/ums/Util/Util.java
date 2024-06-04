package kr.standard.ums.Util;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Util {
    public static String getCurrentTime() {
        Format format = new SimpleDateFormat("yyyyMMddHHmmss");
        Calendar calendar = Calendar.getInstance();

        return format.format(calendar.getTime());
    }
}
