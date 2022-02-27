package jp.salonreservesync.dto;

import java.util.ResourceBundle;

import org.springframework.http.HttpStatus;

import jp.salonreservesync.exception.BusinessException;

/**
 * お客様からの操作が入ったサイト
 */
public enum EnumSite
{
  SITE_A,
  SITE_B
  ;

  private ResourceBundle r;

  private EnumSite()
  {
    if (r != null)
      return;

    r = ResourceBundle.getBundle("application");
  }

  /**
   * 値の取得
   * @return キーに対する値
   */
  public String getValue()
  {
    return r.getString(this.name());
  }

  public EnumSite reverseLookup(String value)
  {
    for (EnumSite site : values())
    {
      if (r.getString(site.name()).equals(value))
        return site;
    }

    throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR, "サイト名が異常。(" + value + ")");
  }
}
