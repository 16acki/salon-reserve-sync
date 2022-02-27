package jp.salonreservesync.dto;

import org.w3c.dom.Element;

import lombok.Data;

/**
 * 予約情報
 */
@Data
public class OrderDto
{
  private EnumSite site;
  private EnumOperation operation;
  private String reserveId;
  private String year;
  private String month;
  private String day;
  private String startHour;
  private String startMinute;
  private int runTime;
  private String endHour;
  private String endMinute;
  private String staff;

  /**
   * コンストラクタ
   * @param requestParser
   */
  public OrderDto(RequestParser requestParser)
  {
    site = requestParser.getSite();
    operation = requestParser.getOperation();
    reserveId = requestParser.getReserveId();

    String[] ymd = requestParser.getYYYYMMDD();
    if (ymd != null)
    {
      year = ymd[0];
      month= ymd[1];
      day  = ymd[2];
    }

    String[] start = requestParser.getStartHourMinute();
    if (start != null)
    {
      startHour = start[0];
      startMinute = start[1];
    }

    runTime = requestParser.getRunTime();

    String[] end = requestParser.getEndHourMinute();
    if (end != null)
    {
      endHour = end[0];
      endMinute = end[1];
    }

    staff = requestParser.getStaff();
  }

  /**
   * コンストラクタ
   * @param item Element
   */
  public OrderDto(Element item)
  {
    String strSite = item.getAttribute("site");
    site = EnumSite.SITE_A.reverseLookup(strSite);
    String strOperation = item.getAttribute("operation");
    operation = EnumOperation.valueOf(strOperation);
    reserveId = item.getAttribute("reserveId");
    year = item.getAttribute("year");
    month = item.getAttribute("month");
    day = item.getAttribute("day");
    startHour = item.getAttribute("startHour");
    startMinute = item.getAttribute("startMinute");
    runTime = Integer.valueOf(item.getAttribute("runTime"));
    endHour = item.getAttribute("endHour");
    endMinute = item.getAttribute("endMinute");
    staff = item.getAttribute("staff");
  }

  /**
   * お客様の操作が入ったサイト
   * @param site EnumSite
   * @return boolean
   */
  public boolean isSite(EnumSite site)
  {
    return (this.site == site);
  }

  /**
   * お客様の行った操作
   * @param operation EnumOperation
   * @return boolean
   */
  public boolean isOperation(EnumOperation operation)
  {
    return (this.operation == operation);
  }
}
