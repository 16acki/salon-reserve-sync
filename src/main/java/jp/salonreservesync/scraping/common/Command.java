package jp.salonreservesync.scraping.common;

/**
 * スクレイピングのコマンド
 */
public interface Command
{
  /**
   * 親(runner)を設定する
   * @param runner
   */
  public void set(Runner runner);

  /**
   * コマンドを実行する
   */
  public void execute();
}
