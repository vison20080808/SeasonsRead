package com.seasonsread.app.util;

import android.util.Log;

import com.seasonsread.app.base.SRConfig;

import static com.seasonsread.app.util.LangUtils.*;

/**
 * Class to help Logging when debug.
 *
 * @author Cuper Hu created at Apr 17, 2009 5:00:23 PM
 */
public class LogUtils {
  public static final String LOG_ID = "SeasonsRead";
  public static final int MIN_LOG_LEVEL = SRConfig.DEV_BUILD ? Log.DEBUG : Log.INFO;
  public static final boolean LOG_LINE_NUMBER = false;

  public static final boolean V = Log.VERBOSE >= MIN_LOG_LEVEL;
  public static final boolean D = Log.DEBUG >= MIN_LOG_LEVEL;
  public static final boolean I = Log.INFO >= MIN_LOG_LEVEL;
  public static final boolean W = Log.WARN >= MIN_LOG_LEVEL;
  public static final boolean DW = SRConfig.DEV_BUILD && Log.WARN >= MIN_LOG_LEVEL;
  public static final boolean E = Log.ERROR >= MIN_LOG_LEVEL;

  private LogUtils() {}

  private static void log(int priority, String msg, Throwable throwable) {
    try {
        if (throwable != null)
            Log.println(
                    priority,
                    LOG_ID,
                    LangUtils.releaseStringBuilder(LangUtils.acquireStringBuilder(0)
                            .append(msg)
                            .append(", exception:\n\t")
                            .append(
                                    (priority >= Log.ERROR || SRConfig.DEV_BUILD) ? Log.getStackTraceString(throwable)
                                            : LangUtils.toString(throwable))));
        else
            Log.println(priority, LOG_ID, releaseStringBuilder(acquireStringBuilder(0).append(msg)));

    } catch (Exception e) {
      Log.e(LOG_ID, "Failed to log: " + e.getMessage());
    }
  }

  /**
   * Warn the incomplete implemented of methods.
   */
  public static void warnIncomplete() {
    if (LOG_LINE_NUMBER) {
      StackTraceElement[] sts = Thread.currentThread().getStackTrace();
      StackTraceElement st = null;
      for (int i = 2; i < sts.length; ++i) {
        StackTraceElement tmp = sts[i];
        if (!tmp.getClassName().contains("LogUtils")) {
          st = tmp;
          break;
        }
      }
      if (st != null)
        Log.println(
            Log.WARN,
            LOG_ID,
            releaseStringBuilder(acquireStringBuilder(0).append(st.getFileName()).append(" line")
                .append(st.getLineNumber()).append(", ").append(st.getMethodName())
                .append("(): incomplete implementation")));
      else
        Log.println(Log.WARN, LOG_ID, "incomplete implementation");
    } else
      Log.println(Log.WARN, LOG_ID, "incomplete implementation");
  }

  /**
   * Output VERBOSE information in log.
   *
   * @param throwable
   * @param format
   * @param args
   */
  public static void v(Throwable throwable, String format, Object... args) {
    if (Log.VERBOSE >= MIN_LOG_LEVEL)
      try {
        log(Log.VERBOSE, format(format, args), throwable);
      } catch (Exception e) {
        Log.e(LOG_ID, "Failed to v: " + e.getMessage());
      }
  }

  /**
   * Output VERBOSE information in log.
   *
   * @param format
   * @param args
   */
  public static void v(String format, Object... args) {
    v(null, format, args);
  }

  /**
   * Output DEBUG information in log.
   *
   * @param throwable
   * @param format
   * @param args
   */
  public static void d(Throwable throwable, String format,  Object... args) {
    if (Log.DEBUG >= MIN_LOG_LEVEL)
      try {
        if (args == null || args.length == 0)
          log(Log.DEBUG, format, throwable);
        else
          log(Log.DEBUG, format(format, args), throwable);
      } catch (Exception e) {
        Log.e(LOG_ID, "Failed to d: " + e.getMessage());
      }
  }

  /**
   * Output DEBUG information in log.
   *
   * @param format
   * @param args
   */
  public static void d(String format, Object... args) {
    d(null, format, args);
  }

  /**
   * Output INFO information in log.
   *
   * @param throwable
   * @param format
   * @param args
   */
  public static void i(Throwable throwable, String format, Object... args) {
    if (Log.INFO >= MIN_LOG_LEVEL)
      try {
        if (args == null || args.length == 0)
          log(Log.INFO, format, throwable);
        else
          log(Log.INFO, format(format, args), throwable);
      } catch (Exception e) {
        Log.e(LOG_ID, "Failed to i: " + e.getMessage());
      }
  }

  /**
   * Output INFO information in log.
   *
   * @param format
   * @param args
   */
  public static void i(String format, Object... args) {
    i(null, format, args);
  }

  /**
   * Output WARN information in log.
   *
   * @param throwable
   * @param format
   * @param args
   */
  public static void w(Throwable throwable, String format, Object... args) {
    if (Log.WARN >= MIN_LOG_LEVEL)
      try {
        if (args == null || args.length == 0)
          log(Log.WARN, format, throwable);
        else
          log(Log.WARN, format(format, args), throwable);
      } catch (Exception e) {
        Log.e(LOG_ID, "Failed to w: " + e.getMessage());
      }
  }

  /**
   * Output WARN information in log.
   *
   * @param format
   * @param args
   */
  public static void w(String format, Object... args) {
    w(null, format, args);
  }

  /**
   * Output ERROR information in log
   *
   * @param throwable
   * @param format
   * @param args
   */
  public static void e(Throwable throwable, String format, Object... args) {
    if (Log.ERROR >= MIN_LOG_LEVEL)
      try {
        String msg = format;
        if (args != null && args.length > 0)
          msg = format(format, args);

        log(Log.ERROR, msg, throwable);
        // if (SRConfig.DEV_BUILD)
        // ViewUtils.showToast(throwable == null ? msg : msg + " : " + throwable, Toast.LENGTH_LONG,
        // true);

      } catch (Exception e) {
        Log.e(LOG_ID, "Failed to e: " + e.getMessage());
      }
  }

  /**
   * Output ERROR information in log.
   *
   * @param format
   * @param args
   */
  public static void e(String format, Object... args) {
    e(null, format, args);
  }

  /**
   * Output the assertion that something is true in log.
   *
   * @param bool
   */
  public static void assertTrue(boolean bool) {
    if (!bool)
      LogUtils.e("Assertion failed");
  }

  /**
   * Out put WARN & DEV_BUILD information in log.
   *
   * @param throwable
   * @param format
   * @param args
   */
  public static void dw(Throwable throwable, String format, Object... args) {
    if (DW)
      try {
        String msg = format;
        if (args != null && args.length > 0)
          msg = format(format, args);

        log(Log.WARN, msg, throwable);

        // if (SRConfig.DEV_BUILD)
        // ViewUtils.showToast(throwable == null ? msg : msg + " : " + throwable, Toast.LENGTH_LONG,
        // true);
      } catch (Exception e) {
        Log.e(LOG_ID, "Failed to w: " + e.getMessage()); //$NON-NLS-1$
      }
  }

  /**
   * Out put WARN & DEV_BUILD information in log.
   *
   * @param format
   * @param args
   */
  public static void dw(String format, Object... args) {
    dw(null, format, args);
  }

  /**
   * Output in log that a specific feature is not supported.
   *
   * @param feature
   */
  public static void missedFeature(String feature) {
    e("%s is not supported in Social SDK", feature);
  }
}
