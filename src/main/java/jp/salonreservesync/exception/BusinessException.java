package jp.salonreservesync.exception;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.http.HttpStatus;

import lombok.Getter;

/**
 * 業務例外
 */
@Getter
public class BusinessException extends RuntimeException
{
  private HttpStatus status;
  private String message;

  // ******************************************************

  /**
   * コンストラクタ
   * @param status HttpStatus
   */
  public BusinessException(HttpStatus status)
  {
    this.status  = status;
  }

  /**
   * コンストラクタ
   * @param status HttpStatus
   * @param message String
   */
  public BusinessException(HttpStatus status, String message)
  {
    this.status  = status;
    this.message = message;
  }

  /**
   * コンストラクタ
   * @param status HttpStatus
   * @param e Exception
   */
  public BusinessException(HttpStatus status, Exception e)
  {
    super(e);
    this.status  = status;
  }

  // ******************************************************

  /**
   * ResponseBody を返す
   * @return JSON Object
   */
  public Object getResponseBody()
  {
    ConcurrentHashMap<String, Object> map = new ConcurrentHashMap<>();
    map.put("status", status.value());

    if (message == null)
    {
      message = status.toString();
    }

    map.put("message", message);
    return map;
  }
}
