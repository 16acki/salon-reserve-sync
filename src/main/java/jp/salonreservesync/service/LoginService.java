package jp.salonreservesync.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.salonreservesync.jwt.JwtProvider;

/**
 * JWT 発行サービス
 */
@Service
public class LoginService
{
  @Autowired
  private JwtProvider jwtProvider;

  public String login(String userId)
  {
    return jwtProvider.create(userId);
  }
}
