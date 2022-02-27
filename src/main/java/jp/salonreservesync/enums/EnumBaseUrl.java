package jp.salonreservesync.enums;

import java.util.ResourceBundle;

/**
 * 予約サイトのドメイン
 */
public enum EnumBaseUrl
{
  BASE_URL_A,
  BASE_URL_B
  ;

  private ResourceBundle r;

  private EnumBaseUrl()
  {
    if (r != null)
      return;

    r = ResourceBundle.getBundle("application");
  }

  public String getBaseurl()
  {
    return r.getString(this.name());
  }
}
