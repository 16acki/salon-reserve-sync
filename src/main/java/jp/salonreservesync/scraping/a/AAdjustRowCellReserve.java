package jp.salonreservesync.scraping.a;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import jp.salonreservesync.dto.OrderDto;
import jp.salonreservesync.scraping.common.Command;
import jp.salonreservesync.scraping.common.Runner;

/**
 * 【4-1】スタッフ行、時間列を選択
 */
public class AAdjustRowCellReserve implements Command
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
    // 描画待ち
    try
    {
      Thread.sleep(500);
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }

    // 全スタッフ行を取得
    List<WebElement> staffs = web.findElements(By.className("orders-shifts__shift-name"));

    // 担当スタッフの行インデックスを取得
    int staffRowIndex = getStaffRowIndex(staffs);

    // ----------------------------------------------------

    // 行を取得
    WebElement orders_cells__container = web.findElements(By.className("orders-cells__container")).get(staffRowIndex);
    WebElement orders_cells__row = orders_cells__container.findElements(By.className("orders-cells__row")).get(staffRowIndex);

    // 全列取得
    List<WebElement> cells = orders_cells__row.findElements(By.className("orders-cells__cell"));

    // 開店時間
    String start = cells.get(0).getAttribute("title");
    start = start.replaceAll("\\D", "");

    // 予約時分
    String orderHourMinute = Integer.valueOf(order.getStartHour()).toString() + order.getStartMinute();

    // 差分
    int sa = Integer.valueOf(orderHourMinute) - Integer.valueOf(start);

    // 列インデックス
    double _colidx = (sa * 0.12);
    int colidx = (int)_colidx;

    // クリック
    cells.get(colidx).click();
  }

  /**
   * 担当スタッフの行インデックスを取得
   * @param rows
   * @return int
   */
  private int getStaffRowIndex(List<WebElement> rows)
  {
    String orderStaff = order.getStaff();
    if (orderStaff.contains(" ") || orderStaff.contains("　"))
    {
      String seimei[] = orderStaff.split("( |　)");
      for (int i = 0; i < rows.size(); i++)
      {
        String rowStaff = rows.get(i).getText();
        if (rowStaff.contains(seimei[0]) && rowStaff.contains(seimei[1]))
        {
          return i;
        }
      }
    }
    else
    {
      for (int i = 0; i < rows.size(); i++)
      {
        String rowStaff = rows.get(i).getText();
        if (rowStaff.equals(orderStaff))
        {
          return i;
        }
      }
    }
    // スタッフ指名なしでオーダーが来る場合、1 行目にしておく
    return 0;
  }
}
