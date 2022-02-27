package jp.salonreservesync.utils;

import java.util.Calendar;
import java.util.Locale;

/**
 * 日付、時分の操作
 */
public class DateUtils
{
  private DateUtils() {}

  /**
   * 稼働時間を返す
   * @param y 年
   * @param m 月
   * @param d 日
   * @param startHH 開始時
   * @param startMM 開始分
   * @param endHH 終了時
   * @param endMM 終了分
   * @return 分(int)
   */
  public static int runTime(String y, String m, String d, String startHH, String startMM, String endHH, String endMM)
  {
    var start = Calendar.getInstance(Locale.JAPAN);
    start.set(Integer.valueOf(y),
              adjustMonth(m),
              Integer.valueOf(d),
              Integer.valueOf(startHH),
              Integer.valueOf(startMM));
    long lngStart = start.getTimeInMillis();

    var end = Calendar.getInstance(Locale.JAPAN);
    end.set(Integer.valueOf(y),
            adjustMonth(m),
            Integer.valueOf(d),
            Integer.valueOf(endHH),
            Integer.valueOf(endMM));
    long lngEnd = end.getTimeInMillis();

    long lngDiff = lngEnd - lngStart;

    return (int)(lngDiff / (60 * 1000));
  }

  /**
   * 終了時分を返す
   * @param y 年
   * @param m 月
   * @param d 日
   * @param startHH 開始時
   * @param startMM 開始分
   * @param runTime 稼働時間
   * @return [HH][MM]
   */
  public static String[] endHHMM(String y, String m, String d, String startHH, String startMM, int runTime)
  {
    var cal = Calendar.getInstance(Locale.JAPAN);
    cal.set(Integer.valueOf(y),
            adjustMonth(m),
            Integer.valueOf(d),
            Integer.valueOf(startHH),
            Integer.valueOf(startMM));
    cal.add(Calendar.MINUTE, runTime);

    int _endHH = cal.get(Calendar.HOUR_OF_DAY);
    String endHH = String.format("%02d", _endHH);

    int _endMM = cal.get(Calendar.MINUTE);
    String endMM = String.format("%02d", _endMM);

    return new String[] { endHH, endMM };
  }

  /**
   * java.util.Calendar 用の月に修正して返す
   * @param month
   * @return
   */
  private static int adjustMonth(String month)
  {
    int m = Integer.valueOf(month);
    m--;
    if (m < 0) return 11;
    return m;
  }
}
