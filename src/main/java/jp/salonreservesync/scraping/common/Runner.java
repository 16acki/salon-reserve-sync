package jp.salonreservesync.scraping.common;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;
import jp.salonreservesync.dto.OrderDto;

/**
 * 各コマンドの実行クラス
 */
public class Runner extends Thread
{
  /** 予約情報 */
  private OrderDto order;

  /** WebDriver */
  private WebDriver web;

  /** WebDriverWait */
  private WebDriverWait wait;

  /** コマンドリスト */
  private List<Command> commands;

  // ******************************************************

  /**
   * インスタンス取得
   * @param orderDto
   * @return Runner
   */
  public static Runner of(OrderDto order)
  {
    return new Runner(order);
  }

  /**
   * プライベートコンストラクタ
   * @param orderDto
   */
  private Runner(OrderDto order)
  {
    // 予約情報を設定する
    this.order = order;

    // Chrome 関係セットアップ
    WebDriverManager.chromedriver().setup();
    ChromeOptions options = new ChromeOptions();
    options.addArguments("--headless", "--no-sandbox");
    web = new ChromeDriver(options);

    // WebDriverWait 取得
    wait = new WebDriverWait(web, 5); // 要素が現れるまで 5 秒待つ

    // コマンドリストをインスタンス化
    commands = new ArrayList<Command>();
  }

  // ******************************************************
  // コマンドの登録
  // ******************************************************

  /**
   * コマンドを登録する
   * @param command
   * @return Runner
   */
  public Runner add(Command ...command)
  {
    for (Command cmd : command)
    {
      add(cmd);
    }
    return this;
  }

  /**
   * コマンドを登録する
   * @param command
   * @return Runner
   */
  public Runner add(Command command)
  {
    command.set(this);
    commands.add(command);
    return this;
  }

  // ******************************************************
  // コマンドの実行
  // ******************************************************

  /**
   * 登録したコマンドを非同期実行
   */
  @Override
  public void run()
  {
    execute();
  }

  /**
   * 登録したコマンドを同期実行
   */
  public Runner execute()
  {
    try
    {
      for (Command cmd : commands)
      {
        cmd.execute();
      }

      // 登録データが送信され切るまで待つ
      Thread.sleep(1000);

      return this;
    }
    catch (Exception e)
    {
      closeWeb();
      throw new RuntimeException(e);
    }
    finally
    {
      commands.clear();
    }
  }

  // ******************************************************
  // その他関数
  // ******************************************************

  /**
   * 設定されている予約情報を変更する
   * @param orderDto
   * @return Runner
   */
  public Runner changeOrder(OrderDto order)
  {
    this.order = order;
    return this;
  }

  /**
   * WebDriver をプロセスから削除する
   */
  public void closeWeb()
  {
    if (web != null)
    {
      web.quit();
    }
  }

  // ******************************************************
  // Getter / Setter
  // ******************************************************

  /**
   * 予約情報を返す
   * @return OrderDto
   */
  public OrderDto getOrder()
  {
    return order;
  }

  /**
   * WebDriver を返す
   * @return WebDriver
   */
  public WebDriver getWeb()
  {
    return web;
  }

  /**
   * WebDriverWait を返す
   * @return WebDriverWait
   */
  public WebDriverWait getWait()
  {
    return wait;
  }
}
