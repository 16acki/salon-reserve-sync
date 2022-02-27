package jp.salonreservesync.dto;

import lombok.Data;

/**
 * JWT 発行要求があったときの userId を保持
 */
@Data
public class LoginRequestDto
{
  private String userId;
}
