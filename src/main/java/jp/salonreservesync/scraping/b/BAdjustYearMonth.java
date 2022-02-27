package jp.salonreservesync.scraping.b;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import jp.salonreservesync.dto.OrderDto;
import jp.salonreservesync.enums.EnumBaseUrl;
import jp.salonreservesync.scraping.common.Command;
import jp.salonreservesync.scraping.common.Runner;

/**
 * 【2】カレンダーの年月合わせ
 */
public class BAdjustYearMonth implements Command
{
  /** 予約情報 */
  private OrderDto order;

  /** WebDriver */
  private WebDriver web;

  @Override
  public void set(Runner runner)
  {
    order = runner.getOrder();
    web = runner.getWeb();
  }

  @Override
  public void execute()
  {
    web.get(EnumBaseUrl.BASE_URL_B.getBaseurl() + "/reservation_table/");

    recursive();
  }

  /**
   * カレンダーの年月が合うまで呼び出す再帰関数
   */
  private void recursive()
  {
    // 対象年月
    String _orderYearMonth = order.getYear() + order.getMonth();
    int orderYearMonth = Integer.valueOf(_orderYearMonth);

    // カレンダーの年
    WebElement weYear = web.findElement(By.className("ui-datepicker-year"));
    String calYear = weYear.getText();

    // カレンダーの月
    WebElement weMonth = web.findElement(By.className("ui-datepicker-month"));
    String calMonth = weMonth.getText().replaceAll("\\D", "");
    calMonth = String.format("%02d", Integer.valueOf(calMonth));

    // カレンダーの年月
    String _calYearMonth = calYear + calMonth;
    int calYearMonth = Integer.valueOf(_calYearMonth);

    // 対象年月がカレンダーの年月以前なら、エラー
    if (orderYearMonth < calYearMonth)
    {
      // 試験用に後ろに進む
      // throw new Exception("対象年月が過去です。");
    }

    // 対象年月とカレンダーの年月が合致すれば、止める
    if (orderYearMonth == calYearMonth)
    {
      return;
    }

    // 試験用に後ろに進む
    if (orderYearMonth < calYearMonth)
    {
      WebElement btnNextMonth = web.findElement(By.xpath("//*[@id=\"datepicker\"]/div[2]/div/a[1]"));
      btnNextMonth.click();
      recursive();
      return;
    }

    // カレンダーを次の月に進める
    WebElement btnNextMonth = web.findElement(By.xpath("//*[@id=\"datepicker\"]/div[2]/div/a[2]"));
    btnNextMonth.click();

    // 再帰呼出し
    recursive();
  }
}
