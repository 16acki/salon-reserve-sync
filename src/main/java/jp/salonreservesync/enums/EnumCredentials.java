package jp.salonreservesync.enums;

import java.util.ResourceBundle;

/**
 * 予約サイトへのログイン情報
 */
public enum EnumCredentials
{
  CREDENTIALS_A,
  CREDENTIALS_B
  ;

  private ResourceBundle r;

  private EnumCredentials()
  {
    if (r != null)
      return;

    r = ResourceBundle.getBundle("application");
  }

  public String[] getCredentials()
  {
    return r.getString(this.name()).split(":");
  }
}
