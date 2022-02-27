package jp.salonreservesync.scraping.a;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import jp.salonreservesync.scraping.common.Command;
import jp.salonreservesync.scraping.common.Runner;

/**
 * 【7】キャンセルする
 */
public class ADummyCancel implements Command
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
    WebElement btnDelete = web.findElement(By.xpath("//*[@id=\"select_time\"]/div[1]/div/div[10]/input[1]"));
    btnDelete.click();

    WebElement btnArea = web.findElement(By.className("btn_area"));
    List<WebElement> inputs =  btnArea.findElements(By.tagName("input"));
    inputs.get(0).click();
  }
}
