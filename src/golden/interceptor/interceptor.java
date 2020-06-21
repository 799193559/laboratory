package golden.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.luxsuen.jsonutil.util.JsonUtil;

import golden.model.*;
import golden.service.userservice;
import golden.token.deToken_;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import java.io.PrintWriter;
@Component
public class interceptor implements HandlerInterceptor{
  @Resource
  userservice userService;
  
  public void setUserService(userservice userService) {
    this.userService = userService;
  }
  
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception,TokenExpiredException {
//    Cookie[] cookies = request.getCookies();
    String token = null;
//    if (cookies != null) {
//      byte b;
//      int i;
//      Cookie[] arrayOfCookie;
//      for (i = (arrayOfCookie = cookies).length, b = 0; b < i; ) {
//        Cookie cookie = arrayOfCookie[b];
//        if (cookie.getName().equals("token"))
//          token = cookie.getValue(); 
//        b++;
//      } 
//    } 
//    if (token != null) {
//      deToken_ detoken = new deToken_();
//      DecodedJWT jwt = null;
//      jwt = detoken.deToken(token);
//      if (jwt == null)
//        return false; 
//      jwt = JWT.decode(token);
//      String account = jwt.getClaim("account").asString();
//      UserInfo user_1 = new UserInfo();
//      user_1.setAccount(account);
//      UserInfo user_2 = this.userService.selectby_account(account);
//      if (user_2 == null) {
//        System.out.print("account don't exit");
//        return false;
//      } 
//      String name_1 = user_2.getName();
//      String name_2 = jwt.getClaim("name").asString();
//      if (name_1.equals(name_2))
//        return true; 
//      System.out.println("username don't match");
//      return false;
//    } 
    String temp = request.getHeader("Authorization");
    PrintWriter pw = response.getWriter();
    response.reset();
    response.setCharacterEncoding("UTF-8");
    response.setContentType("application/json;charset=UTF-8");
    HttpServletRequest httpServletRequest = (HttpServletRequest)request;
    HttpServletResponse httpServletResponse = (HttpServletResponse)response;
    httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
    httpServletResponse.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
    httpServletResponse.setHeader("Access-Control-Max-Age", "0");
    httpServletResponse.setHeader("Access-Control-Allow-Headers", "Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With,userId,token,authorization");
    httpServletResponse.setHeader("Access-Control-Allow-Credentials", "true");
    httpServletResponse.setHeader("XDomainRequestAllowed", "1");
    JSONObject result=new JSONObject();
    if (temp != null) {
      String[] str = temp.split(" ");
      token = str[1];
      deToken_ detoken = new deToken_();
      DecodedJWT jwt = null;
      jwt = detoken.deToken(token);
      if (jwt == null) {
    	  result.put("code",-1);
          result.put("msg","token expiration or token invalid");
    	  pw.write(result.toJSONString());
        return false; 
      }
      jwt = JWT.decode(token);
      String account = jwt.getClaim("account").asString();
//      User user_1 = new User();
//      user_1.setUsername(account);
      UserInfo user_2 = this.userService.selectby_account(account);
      if (user_2 == null) {
    	  result.put("code",-2);
          result.put("msg","account doesn't exit");
    	  pw.write(result.toJSONString());
          return false;
      } 
      String name_1 = user_2.getName();
      String name_2 = jwt.getClaim("name").asString();
      if (name_1.equals(name_2))
        return true; 
      
      result.put("code",-3);
      result.put("msg","username doesn't match");
	  pw.write(result.toJSONString());
      return false;
    } 
    result.put("code",-4);
    result.put("msg","token missing");
	pw.write(result.toJSONString());
    pw.flush();
    pw.close();
    return false;
  }
  
  public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3) throws Exception {}
  
  public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3) throws Exception {}
}

