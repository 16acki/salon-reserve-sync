package jp.salonreservesync.scraping.b;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import jp.salonreservesync.enums.EnumBaseUrl;
import jp.salonreservesync.enums.EnumCredentials;
import jp.salonreservesync.scraping.common.Command;
import jp.salonreservesync.scraping.common.Runner;

/**
 * 【1】ログイン
 */
public class BLogin implements Command
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
    web.get(EnumBaseUrl.BASE_URL_B.getBaseurl() + "/member/login.php");

    String[] credentials = EnumCredentials.CREDENTIALS_B.getCredentials();

    WebElement login_name = web.findElement(By.id("reset_1"));
    login_name.sendKeys(credentials[0]);

    WebElement login_password = web.findElement(By.id("reset_2"));
    login_password.sendKeys(credentials[1]);

    WebElement btnLogin = web.findElement(By.xpath("//*[@id=\"login\"]/form/div[1]/a"));
    btnLogin.click();
  }
}
