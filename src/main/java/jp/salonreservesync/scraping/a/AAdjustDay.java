package jp.salonreservesync.scraping.a;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import jp.salonreservesync.dto.OrderDto;
import jp.salonreservesync.scraping.common.Command;
import jp.salonreservesync.scraping.common.Runner;

/**
 * 【5】カレンダーの対象日押下
 */
public class AAdjustDay implements Command
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
    WebElement gd_picker = web.findElement(By.id("gd_picker"));

    // 全セル取得
    List<WebElement> cells = gd_picker.findElements(By.tagName("div"));

    for (WebElement cell : cells)
    {
      // このカレンダーの有効日以外は飛ばす
      String attrClass = cell.getAttribute("class");
      if (attrClass.contains("monyear") || attrClass.contains("dow") || attrClass.contains("outday")) continue;

      // 対象日でないなら飛ばす
      if (!cell.getText().equals(targetDay)) continue;

      // 対象日を押下
      cell.click();

      break;
    }
  }
}
