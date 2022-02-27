package jp.salonreservesync.interceptor;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

import jp.salonreservesync.jwt.JwtProvider;

@Component
public class Interceptor implements AsyncHandlerInterceptor
{
  @Autowired
  private JwtProvider jwtProvider;

  /**
   * API の実行前にインターセプトする
   */
  @Override
  public boolean preHandle( HttpServletRequest request,
                            HttpServletResponse response,
                            Object handler) throws Exception
  {
    HandlerMethod handlerMethod = (HandlerMethod) handler;
    Method method = handlerMethod.getMethod();
    NonAuth nonAuth = AnnotationUtils.findAnnotation(method, NonAuth.class);
    if (nonAuth != null)
    {
      return true;
    }

    String token = request.getHeader("X-AUTH-TOKEN");
    jwtProvider.verify(token);
    return true;
  }
}
