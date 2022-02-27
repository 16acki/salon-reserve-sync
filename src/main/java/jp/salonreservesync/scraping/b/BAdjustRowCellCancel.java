package jp.salonreservesync.scraping.b;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import jp.salonreservesync.dto.EnumSite;
import jp.salonreservesync.dto.OrderDto;
import jp.salonreservesync.scraping.common.Command;
import jp.salonreservesync.scraping.common.Runner;

/**
 * 【3-2】ダミー予約セルを選択
 */
public class BAdjustRowCellCancel implements Command
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
    // ルート
    WebElement root = web.findElement(By.xpath("//*[@id=\"scrollTable\"]/tbody"));

    // データ行
    List<WebElement> dataLines = root.findElements(By.className("dataLine"));

    for (var dataLine : dataLines)
    {
      // ダミー予約セルを取得
      List<WebElement> dummyReserveCells = dataLine.findElements(By.className("plan"));
      for (var dummyReserveCell : dummyReserveCells)
      {
        // 内容テキストを取得
        String text = dummyReserveCell.findElement(By.className("customerName")).getText();
        boolean match = (text.contains(EnumSite.SITE_B.getValue()) && text.contains(order.getReserveId()));
        if (!match) continue;
        dummyReserveCell.click();
        break;
      }
    }
  }
}
