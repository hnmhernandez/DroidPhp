package org.opendroidphp.app.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by harold on 6/09/16.
 */
public class DateUtils {

    //Obtener la fecha actual
    public static String getToday(boolean humanFormat, boolean withHour) {
        Date today = new Date();
        if (humanFormat) {
            DateFormat dateFormatter = DateFormat.getDateInstance();
            return dateFormatter.format(today);
        } else {
            Calendar c = Calendar.getInstance();
            int day = c.get(Calendar.DATE);
            int month = c.get(Calendar.MONTH) + 1;
            int year = c.get(Calendar.YEAR);
            if (withHour) {
                int minute = c.get(Calendar.MINUTE);
                int hour = c.get(Calendar.HOUR_OF_DAY);

                //Extraer Huso horario del equipo en formato +0000
                String timezone = TimeZone.getDefault().getDisplayName(true, TimeZone.SHORT);
                timezone = timezone.substring(3, timezone.length());
                timezone = timezone.split(":")[0] + timezone.split(":")[1];

                return year + "-" + addZero(month) + "-" + addZero(day) + " " + addZero(hour) +
                        ":" + addZero(minute) + ":00" + timezone;
            } else {
                return year + "-" + addZero(month) + "-" + addZero(day);
            }
        }
    }

    public static Date getToday() {
        return stringToDate(getToday(false, true));
    }

    //Convierte un String a una Date
    public static Date stringToDate(String strFecha) {
        SimpleDateFormat formatoDelTexto = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ");
        Date date = null;
        try {
            date = formatoDelTexto.parse(strFecha);
        } catch (ParseException e) {
            SimpleDateFormat formatoDelTexto2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                date = formatoDelTexto2.parse(strFecha);
            } catch (ParseException e1) {
                SimpleDateFormat formatoDelTexto3 = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy",
                        Locale.US);
                try {
                    date = formatoDelTexto3.parse(strFecha);
                } catch (ParseException e2) {
                    SimpleDateFormat formatoDelTexto4 = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        date = formatoDelTexto4.parse(strFecha);
                    } catch (ParseException e3) {
                        SimpleDateFormat formatoDelTexto5 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ");
                        try {
                            date = formatoDelTexto5.parse(strFecha);
                        } catch (ParseException e4) {
                            strFecha = strFecha.substring(0, strFecha.length() - 15);
                            SimpleDateFormat formatoDelTexto6 = new SimpleDateFormat(
                                    "EEE MMM dd yyyy HH:mm:ss", Locale.US);
                            try {
                                date = formatoDelTexto6.parse(strFecha);
                            } catch (ParseException ignored) {
                            }
                        }
                    }
                }
            }
        }
        return date;
    }

    //Agrega el numero cero delante del dia o mes si este es menor a 9 para respetar siempre el
    //formato yyyy-mm-dd
    public static String addZero(int num) {
        String result;
        if (num <= 9) {
            result = "0" + num;
        } else {
            result = String.valueOf(num);
        }
        return result;
    }
}
