package jp.salonreservesync.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jp.salonreservesync.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;

/**
 * 例外をハンドルするコントローラ
 */
@RestControllerAdvice
@Slf4j
public class ExceptionController
{
  /**
   * 業務例外
   * @param e 業務例外
   * @return ResponseEntity＜Object＞
   */
  @ExceptionHandler({BusinessException.class})
  public ResponseEntity<Object> handleBusinessException(BusinessException e)
  {
    log.error(e.getMessage(), e);
    return ResponseEntity.status(e.getStatus()).header("Content-Type", "application/json").body(e.getResponseBody());
  }

  /**
   * システムエラー
   * @param e システムエラー
   * @return ResponseEntity＜Object＞
   */
  @ExceptionHandler({RuntimeException.class, Exception.class})
  public ResponseEntity<Object> handleException(Exception e)
  {
    log.error(e.getMessage(), e);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
  }
}
