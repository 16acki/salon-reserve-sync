package jp.salonreservesync.scraping.b;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import jp.salonreservesync.dto.OrderDto;
import jp.salonreservesync.scraping.common.Command;
import jp.salonreservesync.scraping.common.Runner;

/**
 * 【3-1】スタッフ行、時間列を選択
 */
public class BAdjustRowCellReserve implements Command
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
    // 担当スタッフ行を取得
    WebElement staffRoot = web.findElement(By.xpath("//*[@id=\"colHeader\"]/tbody"));
    List<WebElement> staffRows = staffRoot.findElements(By.className("staffName"));

    // 担当スタッフの行インデックスを取得
    int staffRowIndex = getStaffRowIndex(staffRows);

    // 行を取得
    WebElement reserveRoot = web.findElement(By.xpath("//*[@id=\"scrollTable\"]/tbody"));
    List<WebElement> rows = reserveRoot.findElements(By.tagName("tr"));
    WebElement row = rows.get(staffRowIndex * 2);

    // 対象時分
    String targetHourMinute = order.getStartHour() + order.getStartMinute();

    // 列を取得
    List<WebElement> cells = row.findElements(By.tagName("td"));
    for (WebElement cell : cells)
    {
      String startTime = cell.getAttribute("data-start-time");
      if (!startTime.equals(targetHourMinute)) continue;
      cell.click();
      break;
    }
  }

  /**
   * 担当スタッフの行インデックスを取得
   * @param staffRows
   * @return int
   */
  private int getStaffRowIndex(List<WebElement> staffRows)
  {
    String orderStaff = order.getStaff();
    if (orderStaff.contains(" ") || orderStaff.contains("　"))
    {
      String seimei[] = orderStaff.split("( |　)");
      for (int i = 0; i < staffRows.size(); i++)
      {
        String rowStaff = staffRows.get(i).getText();
        if (rowStaff.contains(seimei[0]) && rowStaff.contains(seimei[1]))
        {
          return i;
        }
      }
    }
    else
    {
      for (int i = 0; i < staffRows.size(); i++)
      {
        if (staffRows.get(i).getText().equals(orderStaff))
        {
          return i;
        }
      }
    }
    // スタッフ指名なしでオーダーが来る場合、1 行目にしておく
    return 0;
  }
}
