package jp.salonreservesync.dto;

import org.springframework.http.HttpStatus;

import jp.salonreservesync.exception.BusinessException;
import lombok.Data;

/**
 * GAS から送信されてくるデータ
 */
@Data
public class RequestDto
{
  /** 件名 */
  private String subject;

  /** 本文 */
  private String body;

  /**
   * RequestParser を返す
   * @return RequestParser
   */
  public RequestParser getRequestParser()
  {
    if (body.contains(EnumSite.SITE_A.getValue()))
      return new RequestParserImplA(this);

    else if (body.contains(EnumSite.SITE_B.getValue()))
      return new RequestParserImplB(this);

    else
      throw new BusinessException(HttpStatus.BAD_REQUEST, "不明なサイトからのリクエストです。");
  }
}
