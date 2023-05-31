package com.questcompendium.utils;

import android.os.Environment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class LogV2 {

    private static final Build BUILD = Build.Release;
    private static final int BUFFER_SIZE = 8 * 1021;
    public static final String APP_NAME = "QuestCompendium";
    private static final String DIRECTORY_NAME = "QuestCompendium";
    public static final String PATH = Environment.getExternalStorageDirectory() + "/" + DIRECTORY_NAME;
    public static final String LOG_DIRECTORY = PATH + "/Logs/";

    private enum Build {
        Debug, Release
    }

    /**
     * @return writer object with application log file path.
     * @throws IOException in case unable to create log file path or its inaccessible.
     */
    public static BufferedWriter getBufferedWriter() throws IOException {
        File loDir = new File(LOG_DIRECTORY);
        if (!loDir.exists()) {
            loDir.mkdirs();
        }

        // if (loDir != null && loDir.list().length > 0) {
        // deleteOldLogFiles(loDir);
        // }

        String lsFileName = APP_NAME + "_" + getCurrentDate() + ".log";
        return new BufferedWriter(new FileWriter(LOG_DIRECTORY + "/" + lsFileName, true), BUFFER_SIZE);
    }

    /**
     * Logs specified exception to application log file.
     *
     * @param foTr The exception you would like log.
     */
    public static void logException(String fsTag, Throwable foTr) {
        if (BUILD.equals(Build.Debug)) {
            foTr.printStackTrace();
            try {
                BufferedWriter loBufferedWriter = getBufferedWriter();
                loBufferedWriter.append(getCurrentDateTimeString(true) + "\t" + APP_NAME + "/" + fsTag
                        + "\t" + "/Exception" + "\n" + android.util.Log.getStackTraceString(foTr) + "\n");
                loBufferedWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Logs specified message with tag to application log file.
     *
     * @param fsTag     Used to identify the source of a log message. It usually
     *                  identifies the class or activity where the log call occurs.
     * @param fsMessage The message you would like log.
     */
    public static void i(String fsTag, String fsMessage) {
        if (BUILD.equals(Build.Debug)) {
            android.util.Log.i(fsTag, fsMessage);
            try {
                BufferedWriter loBufferedWriter = getBufferedWriter();
                loBufferedWriter.append(getCurrentDateTimeString(true) + "\t" + APP_NAME + "/" + fsTag
                        + "\t" + fsMessage + "\n");
                android.util.Log.i(fsTag, fsMessage);
                if (loBufferedWriter != null) {
                    loBufferedWriter.flush();
                    loBufferedWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Logs specified message to CSV file.
     *
     * @param fsMessage The message you would like log.
     */
    public static void csv(String fsMessage) {
        if (BUILD.equals(Build.Debug)) {
            try {
                BufferedWriter loBufferedWriter = new BufferedWriter(
                        new FileWriter(LOG_DIRECTORY + "/" + APP_NAME + ".csv", true), BUFFER_SIZE);
                loBufferedWriter.append(getCurrentDateTimeString(true) + "," + fsMessage + "\n");
                if (loBufferedWriter != null) {
                    loBufferedWriter.flush();
                    loBufferedWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getCurrentDate() {
        Date loToday = new Date(System.currentTimeMillis());
        try {
            return new SimpleDateFormat("MMddyyyy", Locale.ENGLISH).format(loToday);
        } catch (Exception e) {
            return loToday.toString();
        }
    }

    public static void deleteOldLogFiles(File foDir) {
        try {
            File[] loFilenames = foDir.listFiles();
            if (loFilenames != null && loFilenames.length > 0) {
                for (File loFile : loFilenames) {
                    long llCurrentDate = getDateMillis(7);
                    long llFileDate = getDate(loFile.lastModified());

                    if (llFileDate < llCurrentDate) {
                        if (loFile.exists()) {
                            loFile.delete();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getCurrentDateTimeString(boolean fbIncludeMillis) {
        Date loToday = new Date(System.currentTimeMillis());
        String lsDateFormat = "MM/dd/yyyy'T'hh:mm:ss";
        if (fbIncludeMillis)
            lsDateFormat += ".SSS";
        else
            lsDateFormat += " a";
        try {
            return new SimpleDateFormat(lsDateFormat, Locale.ENGLISH)
                    .format(loToday);
        } catch (Exception e) {
            return loToday.toString();
        }
    }

    public static long getDateMillis(int fiDays) {
        Calendar liCalendar = Calendar.getInstance();
        liCalendar.add(Calendar.DATE, -fiDays);
        liCalendar.set(Calendar.HOUR_OF_DAY, 0);
        liCalendar.set(Calendar.MINUTE, 0);
        liCalendar.set(Calendar.SECOND, 0);
        liCalendar.set(Calendar.MILLISECOND, 0);
        return liCalendar.getTimeInMillis();
    }

    public static long getDate(long flDateTime) {
        Calendar loCalendar = Calendar.getInstance();
        loCalendar.setTimeInMillis(flDateTime);
        loCalendar.set(Calendar.HOUR_OF_DAY, 0);
        loCalendar.set(Calendar.MINUTE, 0);
        loCalendar.set(Calendar.SECOND, 0);
        loCalendar.set(Calendar.MILLISECOND, 0);
        return loCalendar.getTimeInMillis();
    }
}
