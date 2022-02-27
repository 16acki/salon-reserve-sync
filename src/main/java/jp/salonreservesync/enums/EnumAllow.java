package jp.salonreservesync.enums;

import java.util.ResourceBundle;

import org.springframework.http.HttpStatus;

import jp.salonreservesync.dto.LoginRequestDto;
import jp.salonreservesync.exception.BusinessException;

/**
 * JWT の発行を許可されているユーザ
 */
public enum EnumAllow
{
  ALLOW,
  GMAIL_SEARCH_STRING
  ;

  private ResourceBundle r;

  private EnumAllow()
  {
    if (r != null)
      return;

    r = ResourceBundle.getBundle("application");
  }

  public boolean isAllowed(LoginRequestDto loginRequestDto)
  {
    if (!r.getString(this.name()).contains(loginRequestDto.getUserId()))
    {
      throw new BusinessException(HttpStatus.UNAUTHORIZED);
    }

    return true;
  }

  public String getValue()
  {
    return r.getString(this.name());
  }
}
