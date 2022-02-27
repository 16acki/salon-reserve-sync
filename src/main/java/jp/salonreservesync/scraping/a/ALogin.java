package jp.salonreservesync.scraping.a;

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
public class ALogin implements Command
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
    web.get(EnumBaseUrl.BASE_URL_A.getBaseurl() + "/users/login");

    String[] credentials = EnumCredentials.CREDENTIALS_A.getCredentials();

    WebElement UserLogin = web.findElement(By.id("UserLogin"));
    UserLogin.sendKeys(credentials[0]);

    WebElement UserPassword = web.findElement(By.id("UserPassword"));
    UserPassword.sendKeys(credentials[1]);

    WebElement btnLogin = web.findElement(By.xpath("//*[@id=\"UserLoginForm\"]/button"));
    btnLogin.click();
  }
}
