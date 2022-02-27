package jp.salonreservesync.scraping.a;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import jp.salonreservesync.dto.OrderDto;
import jp.salonreservesync.enums.EnumBaseUrl;
import jp.salonreservesync.scraping.common.Command;
import jp.salonreservesync.scraping.common.Runner;
import jp.salonreservesync.utils.DateUtils;

/**
 * 【2】予約詳細を取得する。<br>
 * 1. 予約日<br>
 * 2. 開始時間<br>
 * 3. 終了時間<br>
 * 4. 稼働時間<br>
 * 5. 担当スタッフ
 */
public class AGetDetail implements Command
{
  /** 予約IDカラム */
  private static final int COLUMN_RESERVE_ID = 0;

  /** 担当スタッフカラム */
  private static final int COLUMN_STAFF = 6;

  /** 予約情報 */
  private OrderDto order;

  /** WebDriver */
  private WebDriver web;

  // ******************************************************

  @Override
  public void set(Runner runner)
  {
    order = runner.getOrder();
    web = runner.getWeb();
  }

  @Override
  public void execute()
  {
    // オーダ詳細ページへ遷移
    String url_detail = EnumBaseUrl.BASE_URL_A.getBaseurl() + "/" + order.getReserveId();
    web.get(url_detail);
    // ----------------------
    // 1. 予約日
    WebElement BookingThedate = web.findElement(By.id("BookingThedate"));
    String ymd[] = BookingThedate.getAttribute("value").split("-");
    order.setYear(ymd[0]);
    order.setMonth(ymd[1]);
    order.setDay(ymd[2]);
    // ----------------------
    // 2. 開始時間
    WebElement BookingDatetimebegin = web.findElement(By.id("BookingDatetimebegin"));
    // 2021-12-30 15:30:00
    String[] hms = BookingDatetimebegin.getAttribute("value").replaceAll("\\d{4}-\\d{2}-\\d{2} ", "").split(":");
    order.setStartHour(hms[0]);
    order.setStartMinute(hms[1]);
    // ----------------------
    // 3. 終了時間
    WebElement BookingDatetimeend = web.findElement(By.id("BookingDatetimeend"));
    // 2021-12-30 17:00:00
    hms = BookingDatetimeend.getAttribute("value").replaceAll("\\d{4}-\\d{2}-\\d{2} ", "").split(":");
    order.setEndHour(hms[0]);
    order.setEndMinute(hms[1]);
    // ----------------------------------------------------
    // 4. 稼働時間
    int runTime = DateUtils.runTime(order.getYear(),
                                    order.getMonth(),
                                    order.getDay(),
                                    order.getStartHour(),
                                    order.getStartMinute(),
                                    order.getEndHour(),
                                    order.getEndMinute());
    order.setRunTime(runTime);
    // ----------------------------------------------------
    // 5. 担当スタッフ
    String url_staff = EnumBaseUrl.BASE_URL_A.getBaseurl() + "/bookings/serach?datest={y}-{m}-{d}";
    url_staff = url_staff.replace("{y}", order.getYear()).replace("{m}", order.getMonth()).replace("{d}", order.getDay());

    // 該当ページへ遷移
    web.get(url_staff);

    WebElement tbody = web.findElement(By.xpath("/html/body/div[2]/div/div/div[2]/div[2]/div/table/tbody"));
    List<WebElement> trs = tbody.findElements(By.tagName("tr"));
    for (WebElement tr : trs)
    {
      List<WebElement> tds = tr.findElements(By.tagName("td"));
      String reserveId = tds.get(COLUMN_RESERVE_ID).findElement(By.tagName("a")).getText();
      if (!reserveId.equals(order.getReserveId())) continue;
      String staff = tds.get(COLUMN_STAFF).getText();
      order.setStaff(staff);
      break;
    }
  }
}
