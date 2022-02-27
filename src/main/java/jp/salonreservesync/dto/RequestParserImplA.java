package jp.salonreservesync.dto;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * GAS が送信してきたデータから、予約情報に設定する項目を返すインターフェース（予約サイトA）
 */
public class RequestParserImplA implements RequestParser
{
  /** 件名 */
  private String subject;

  /** 本文 */
  private String body;

  public RequestParserImplA(RequestDto requestGasDto)
  {
    subject = requestGasDto.getSubject();
    body = requestGasDto.getBody();
  }

  @Override
  public EnumSite getSite()
  {
    return EnumSite.SITE_A;
  }

  @Override
  public EnumOperation getOperation()
  {
    if (subject.contains("入り"))
      return EnumOperation.RESERVE;

    else if (subject.contains("取り消"))
      return EnumOperation.CANCEL;

    else if (subject.contains("変更"))
      return EnumOperation.CHANGE;

    throw new RuntimeException("Invalid operation");
  }

  @Override
  public String getReserveId()
  {
    Matcher m = Pattern.compile("予約ID.*").matcher(body);
    if (m.find())
    {
      String reserveId = m.group().replaceAll("\\D", "");
      return reserveId;
    }
    throw new RuntimeException("Invalid reserveId");
  }

  @Override
  public String[] getYYYYMMDD()
  {
    // スクレイピングで取得
    return null;
  }

  @Override
  public String[] getStartHourMinute()
  {
    // スクレイピングで取得
    return null;
  }

  @Override
  public int getRunTime()
  {
    // スクレイピングで取得
    return 0;
  }

  @Override
  public String[] getEndHourMinute()
  {
    // スクレイピングで取得
    return null;
  }

  @Override
  public String getStaff()
  {
    // スクレイピングで取得
    return null;
  }
}
