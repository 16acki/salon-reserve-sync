package jp.salonreservesync.jwt;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import jp.salonreservesync.dao.SecretDao;
import jp.salonreservesync.exception.BusinessException;

/**
 * JWT の生成と確認
 */
@Component
public class JwtProvider
{
  // 10年で期限切れ
  private static long TIMEOUT = (1 * 1000 * 60) * (60 * 24) * 365 * 10;

  @Autowired
  private SecretDao secretDao;

  /**
   * JWT の生成
   * @param userId
   * @return JWT
   */
  public String create(String userId)
  {
    try
    {
      String secretKey = RandomStringUtils.randomAscii(512);

      Algorithm algorithm = Algorithm.HMAC512(secretKey);

      Date expireTime = new Date();
      expireTime.setTime(expireTime.getTime() + TIMEOUT);

      String token = JWT.create()
                        .withClaim("userId", userId)
                        .withExpiresAt(expireTime)
                        .sign(algorithm);

      secretDao.create(userId, secretKey);

      return token;
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }
  }

  /**
   * JWT の確認
   * @param token
   */
  public void verify(String token)
  {
    try
    {
      String userId = getUserIdFromPayload(token);

      String secretKey = secretDao.read(userId);

      Algorithm algorithm = Algorithm.HMAC512(secretKey);

      JWTVerifier verifier = JWT.require(algorithm)
                                .build();

      verifier.verify(token);
    }
    catch (Exception e)
    {
      throw new BusinessException(HttpStatus.FORBIDDEN, e);
    }
  }

  /**
   * payload から userId を取得する
   * @param token
   * @return userId
   */
  private String getUserIdFromPayload(String token)
  {
    // [header][payload][signature]
    String[] payload = token.split("\\.");

    byte[] b = Base64.getDecoder().decode(payload[1]);

    String json = null;
    try
    {
      json = new String(b, StandardCharsets.UTF_8);
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }

    ObjectMapper objectMapper = new ObjectMapper();
    try
    {
      Map<String, Object> map = objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {});
      String userId = map.get("userId").toString().replace("\"", "");
      return userId;
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }
  }
}
