package jp.salonreservesync.scraping.a;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import jp.salonreservesync.dto.OrderDto;
import jp.salonreservesync.scraping.common.Command;
import jp.salonreservesync.scraping.common.Runner;

/**
 * 【6】予約時間を埋める
 */
public class ADummyReserve implements Command
{
  /** 予約情報 */
  private OrderDto order;

  /** webDriverWait */
  private WebDriverWait wait;

  @Override
  public void set(Runner runner)
  {
    order = runner.getOrder();
    wait = runner.getWait();
  }

  @Override
  public void execute()
  {
    // 業務ボタン押下
    WebElement btnGyomu = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"user_panel_select_gender_operation_view\"]/div[3]/input[2]")));
    btnGyomu.click();

    // 業務時間(稼働時間)設定
    WebElement worksminute = wait.until(ExpectedConditions.elementToBeClickable(By.id("worksminute")));
    worksminute.sendKeys(String.valueOf(order.getRunTime()));

    // 業務コメント設定
    WebElement comment_text = wait.until(ExpectedConditions.elementToBeClickable(By.id("comment_text")));
    comment_text.sendKeys(order.getSite().getValue() + " " + order.getReserveId());

    // 登録ボタン押下
    WebElement btnToroku = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"select_time\"]/div[1]/div/div[10]/input[2]")));
    btnToroku.click();
  }
}
