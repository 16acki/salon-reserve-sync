package jp.salonreservesync.scraping.b;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import jp.salonreservesync.scraping.common.Command;
import jp.salonreservesync.scraping.common.Runner;

/**
 * 【5-2】キャンセルする
 */
public class BDummyCancel implements Command
{
  /** WebDriver */
  private WebDriver web;

  @Override
  public void set(Runner runner)
  {
    web = runner.getWeb();
  }

  @Override
  public void execute()
  {
    // ごみ箱ボタン押下
    WebElement btnTrash = web.findElement(By.xpath("//*[@id=\"plan_delete_btn\"]/a"));
    btnTrash.click();
  }
}
