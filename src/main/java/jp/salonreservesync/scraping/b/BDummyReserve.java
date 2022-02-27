package jp.salonreservesync.scraping.b;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import jp.salonreservesync.dto.OrderDto;
import jp.salonreservesync.scraping.common.Command;
import jp.salonreservesync.scraping.common.Runner;

/**
 * 【5-1】予約時間を埋める
 */
public class BDummyReserve implements Command
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
    // 「予定を登録する」ボタン押下
    WebElement plan_register = web.findElement(By.id("plan-register"));
    plan_register.click();

    // 開始時を設定
    WebElement start_time_h = web.findElement(By.id("start_time_h"));
    Select sel = new Select(start_time_h);
    sel.selectByVisibleText(order.getStartHour());

    // 開始分を設定
    WebElement start_time_m = web.findElement(By.id("start_time_m"));
    sel = new Select(start_time_m);
    sel.selectByVisibleText(order.getStartMinute());

    // 終了時を設定
    WebElement end_time_h = web.findElement(By.id("end_time_h"));
    sel = new Select(end_time_h);
    sel.selectByVisibleText(order.getEndHour());

    // 終了分を設定
    WebElement end_time_m = web.findElement(By.id("end_time_m"));
    sel = new Select(end_time_m);
    sel.selectByVisibleText(order.getEndMinute());

    // メモを設定
    WebElement block_name = web.findElement(By.id("block_name"));
    block_name.sendKeys(order.getSite().getValue() + " " + order.getReserveId());

    // 「予定を登録する」ボタン押下
    WebElement plan_register_btn = web.findElement(By.id("plan_register_btn"));
    WebElement a = plan_register_btn.findElement(By.tagName("a"));
    a.click();
  }
}
