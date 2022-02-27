package jp.salonreservesync.dto;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.salonreservesync.utils.DateUtils;

/**
 * GAS が送信してきたデータから、予約情報に設定する項目を返すインターフェース（予約サイトB）
 */
public class RequestParserImplB implements RequestParser
{
  /** 件名 */
  private String subject;

  /** 本文 */
  private String body;

  public RequestParserImplB(RequestDto requestGasDto)
  {
    subject = requestGasDto.getSubject();
    body = requestGasDto.getBody();
  }

  @Override
  public EnumSite getSite()
  {
    return EnumSite.SITE_B;
  }

  @Override
  public EnumOperation getOperation()
  {
    if (subject.contains("確定"))
      return EnumOperation.RESERVE;

    else if (subject.contains("キャンセル"))
      return EnumOperation.CANCEL;

    else if (subject.contains("変更"))
      return EnumOperation.CHANGE;

    throw new RuntimeException("Invalid Operation");
  }

  @Override
  public String getReserveId()
  {
    Matcher m = Pattern.compile("予約番号.*").matcher(body);
    if (m.find())
    {
      String reserveId = m.group().replaceAll("\\D", "");
      return reserveId;
    }
    throw new RuntimeException("Invalid ReserveId");
  }

  @Override
  public String[] getYYYYMMDD()
  {
    Matcher m = Pattern.compile("来店日時.*").matcher(body);
    if (m.find())
    {
      m = Pattern.compile("\\d{4}\\/\\d\\d?\\/\\d\\d?").matcher(m.group());
      if (m.find())
      {
        String tmp[] = m.group().split("\\D");
        String year = tmp[0];
        String month= String.format("%02d", Integer.valueOf(tmp[1]));
        String day  = String.format("%02d", Integer.valueOf(tmp[2]));
        return new String[] { year, month, day };
      }
    }
    throw new RuntimeException("Invalid YYYYMMDD");
  }

  @Override
  public String[] getStartHourMinute()
  {
    Matcher m = Pattern.compile("来店日時.*").matcher(body);
    if (m.find())
    {
      m = Pattern.compile("\\d{2}:\\d{2}").matcher(m.group());
      if (m.find())
      {
        String tmp[] = m.group().split("\\D");
        String startHH = String.format("%02d", Integer.valueOf(tmp[0]));
        String startMM = String.format("%02d", Integer.valueOf(tmp[1]));
        return new String[] { startHH, startMM };
      }
    }
    throw new RuntimeException("Invalid StartHourMinute");
  }

  @Override
  public int getRunTime()
  {
    Matcher m = Pattern.compile("合計施術時間.*").matcher(body);
    if (m.find())
    {
      String runTime = m.group().replaceAll("\\D", "");
      return Integer.valueOf(runTime);
    }
    return 0;
  }

  @Override
  public String[] getEndHourMinute()
  {
    String ymd[] = getYYYYMMDD();
    String startHourMinute[] = getStartHourMinute();
    int runTime = getRunTime();
    return DateUtils.endHHMM(ymd[0], ymd[1], ymd[2], startHourMinute[0], startHourMinute[1], runTime);
  }

  @Override
  public String getStaff()
  {
    Matcher m = Pattern.compile("担当スタッフ.*").matcher(body);
    if (m.find())
    {
      String staff = m.group().replaceAll("担当スタッフ +", "");
      return staff;
    }
    throw new RuntimeException("Invalid Staff");
  }
}
