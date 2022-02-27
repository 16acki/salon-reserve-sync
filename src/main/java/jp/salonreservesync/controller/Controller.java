package jp.salonreservesync.controller;

import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jp.salonreservesync.enums.EnumAllow;
import jp.salonreservesync.dto.EnumOperation;
import jp.salonreservesync.dto.EnumSite;
import jp.salonreservesync.dto.LoginRequestDto;
import jp.salonreservesync.dto.LoginResponseDto;
import jp.salonreservesync.dto.OrderDto;
import jp.salonreservesync.dto.RequestParser;
import jp.salonreservesync.exception.BusinessException;
import jp.salonreservesync.interceptor.NonAuth;
import jp.salonreservesync.dto.RequestDto;
import jp.salonreservesync.service.AService;
import jp.salonreservesync.service.LoginService;
import jp.salonreservesync.service.BService;

/**
 * エンドポイント
 */
@RestController
public class Controller
{
  @Autowired
  private LoginService loginService;

  @Autowired
  private AService aService;

  @Autowired
  private BService bService;

  /**
   * JWT 発行
   */
  @PostMapping("/login")
  @NonAuth
  public ResponseEntity<Object> login(@RequestBody LoginRequestDto requestDto)
  {
    if (!EnumAllow.ALLOW.isAllowed(requestDto))
    {
      throw new BusinessException(HttpStatus.UNAUTHORIZED);
    }

    String userId = requestDto.getUserId();
    String token = loginService.login(userId);

    String gmail_search_string = EnumAllow.GMAIL_SEARCH_STRING.getValue();

    LoginResponseDto responseDto = new LoginResponseDto(token, gmail_search_string);

    return ResponseEntity.ok().header("Content-Type", "application/json").body(responseDto);
  }

  /**
   * スクレイピングによる同期実行
   * @param requestDto RequestDto
   * @return ResponseEntity＜Object＞
   */
  @PostMapping("/scraping")
  @Async
  public ResponseEntity<Object> scraping(@RequestBody RequestDto requestDto)
  {
    RequestParser requestParser = requestDto.getRequestParser();

    OrderDto order = new OrderDto(requestParser);

    // ****************************************************

    if (order.isSite(EnumSite.SITE_A))
    {
      if (order.isOperation(EnumOperation.RESERVE))
      {
        bService.dummyReserve(order);
      }
      else if (order.isOperation(EnumOperation.CANCEL))
      {
        bService.dummyCancel(order);
      }
      else if (order.isOperation(EnumOperation.CHANGE))
      {
        bService.dummyChange(order);
      }
    }
    else if (order.isSite(EnumSite.SITE_B))
    {
      if (order.isOperation(EnumOperation.RESERVE))
      {
        aService.dummyReserve(order);
      }
      else if (order.isOperation(EnumOperation.CANCEL))
      {
        aService.dummyCancel(order);
      }
      else if (order.isOperation(EnumOperation.CHANGE))
      {
        aService.dummyChange(order);
      }
    }

    // ****************************************************

    return ResponseEntity.ok().build();
  }

  @GetMapping("/download")
  @NonAuth
  public void downloadErrorLog(HttpServletResponse response)
  {
    try (OutputStream os = response.getOutputStream())
    {
      Path filePath = Paths.get("./error.log");
      byte[] b = Files.readAllBytes(filePath);

      response.setContentType("application/octet-stream");
      response.setHeader("Content-Disposition", "attachment; filename=error.log");
      response.setContentLength(b.length);

      os.write(b);
      os.flush();
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }
  }
}
