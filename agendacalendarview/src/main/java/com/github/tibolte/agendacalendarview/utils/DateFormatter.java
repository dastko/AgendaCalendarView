package com.github.tibolte.agendacalendarview.utils;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by dastko on 6/21/16.
 */
public final class DateFormatter {

    public static String formattedDateFromString(String inputFormat, String outputFormat, String inputDate) {
        if (inputFormat == null) { // if inputFormat = "", set a default input format.
            inputFormat = "EEE-MMM-yy HH:mm:ss'GMT'Z(z)";
        }
        if (outputFormat == null) {
            outputFormat = "dd-MM-yyyy'#'kk:mm"; // if inputFormat = "", set a default output format.
        }
        Date parsed;
        String outputDate = "";

        SimpleDateFormat df_input = new SimpleDateFormat(inputFormat);
        df_input.setTimeZone(TimeZone.getTimeZone("UTC"));
        SimpleDateFormat df_output = new SimpleDateFormat(outputFormat);
        df_output.setTimeZone(TimeZone.getTimeZone("UTC"));

        try {
            parsed = df_input.parse(inputDate);
            outputDate = df_output.format(parsed);
        } catch (Exception e) {
            Log.e("formattedDateFromString", "Exception in formateDateFromstring(): " + e.getMessage());
        }

        return outputDate;
    }

}
