package jp.salonreservesync.scraping.b;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import jp.salonreservesync.dto.OrderDto;
import jp.salonreservesync.scraping.common.Command;
import jp.salonreservesync.scraping.common.Runner;

/**
 * 【4】カレンダーの対象日押下
 */
public class BAdjustDay implements Command
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
    // 対象日
    String targetDay = Integer.valueOf(order.getDay()).toString();

    // カレンダー取得
    WebElement tbody = web.findElement(By.xpath("//*[@id=\"datepicker\"]/div[2]/table/tbody"));

    // 全行取得
    List<WebElement> trs = tbody.findElements(By.tagName("tr"));

    for (WebElement tr : trs)
    {
      // 列を取得
      List<WebElement> as = tr.findElements(By.tagName("a"));

      for (WebElement a : as)
      {
        // 対象日でないなら飛ばす
        if (!a.getText().equals(targetDay)) continue;

        // 対象日を押下
        a.click();

        return;
      }
    }
  }
}
