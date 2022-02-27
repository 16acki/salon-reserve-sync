package jp.salonreservesync.dto;

/**
 * RequestDto から OrderDto に設定する各項目を返すインターフェース
 */
public interface RequestParser
{
  /**
   * お客様の操作が入ったサイト
   * @return
   */
  public EnumSite getSite();

  /**
   * お客様の行った操作
   * @return
   */
  public EnumOperation getOperation();

  /**
   * 予約番号
   * @return
   */
  public String getReserveId();

  /**
   * 対象日
   * @return
   */
  public String[] getYYYYMMDD();

  /**
   * 開始時間
   * @return
   */
  public String[] getStartHourMinute();

  /**
   * 稼働時間
   * @return
   */
  public int getRunTime();

  /**
   * 終了時間
   * @return
   */
  public String[] getEndHourMinute();

  /**
   * 担当スタッフ
   * @return
   */
  public String getStaff();
}
