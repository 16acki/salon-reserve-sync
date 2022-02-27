package jp.salonreservesync.scraping.a;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import jp.salonreservesync.dto.OrderDto;
import jp.salonreservesync.enums.EnumBaseUrl;
import jp.salonreservesync.scraping.common.Command;
import jp.salonreservesync.scraping.common.Runner;

/**
 * 【3】カレンダーの年月合わせ
 */
public class AAdjustYearMonth implements Command
{
  /** 予約情報 */
  private OrderDto order;

  /** WebDriver */
  private WebDriver web;

  /** WebDriverWait */
  private WebDriverWait wait;

  @Override
  public void set(Runner runner)
  {
    order = runner.getOrder();
    web = runner.getWeb();
    wait = runner.getWait();
  }

  @Override
  public void execute()
  {
    // 予約受付ページへ遷移
    web.get(EnumBaseUrl.BASE_URL_A.getBaseurl() + "/Orders");

    // DatePicker をクリックしてカレンダー表示
    WebElement glDatePicker = web.findElement(By.id("glDatePicker"));
    glDatePicker.click();

    // カレンダーの年月が合うまで呼び出す再帰関数
    recursive();
  }

  /**
   * カレンダーの年月が合うまで呼び出す再帰関数
   */
  private void recursive()
  {
    // 描画待ち
    try
    {
      Thread.sleep(500);
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }

    // カレンダーを次の月に進めるボタンがクリック可能になるまで待つ
    WebElement we_next_arrow = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"gd_picker\"]/div[3]/a")));

    // 対象年月
    String _orderYearMonth = order.getYear() + order.getMonth();
    int orderYearMonth = Integer.valueOf(_orderYearMonth);

    // カレンダーの年
    WebElement weYear = web.findElement(By.xpath("//*[@id=\"gd_picker\"]/div[2]/div/span[1]"));
    String calYear = weYear.getText().replaceAll("\\D", "");
    
    // カレンダーの月
    WebElement weMonth = web.findElement(By.xpath("//*[@id=\"gd_picker\"]/div[2]/div/span[2]"));
    String calMonth = weMonth.getText().replaceAll("\\D", "");

    // カレンダーの年月
    String _calYearMonth = calYear + String.format("%02d", Integer.valueOf(calMonth));
    int calYearMonth = Integer.valueOf(_calYearMonth);

    // 対象年月がカレンダーの年月以前ならエラー
    if (orderYearMonth < calYearMonth)
    {
      // TODO 試験用に後ろに進む
      // throw new Exception("対象年月が過去です。");
    }

    // 対象年月とカレンダーの年月が合致すれば、ストップ
    if (orderYearMonth == calYearMonth)
    {
      return;
    }

    if (orderYearMonth < calYearMonth)
    {
      // TODO 試験用に後ろに進む
      WebElement prev_arrow = web.findElement(By.xpath("//*[@id=\"gd_picker\"]/div[1]/a"));

      prev_arrow.click();

      recursive();

      return;
    }

    // カレンダーを次の月に進める
    we_next_arrow.click();

    // 再帰呼出し
    recursive();
  }
}
