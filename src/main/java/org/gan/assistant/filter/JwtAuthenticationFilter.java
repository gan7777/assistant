package org.gan.assistant.filter;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.gan.assistant.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {


    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsService userDetailsService;//这是spring Security自带的接口

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        System.out.println("================== JWT 过滤器生效了 ==================");
        logger.info("请求路径: " + request.getRequestURI());
        //1.获取请求头里的Authorization
        String authHeader=request.getHeader("Authorization");
        logger.info("Authorization Header: " + authHeader);
        String username=null;
        String jwt=null;

        //2.校验Header是否以Bearer开头
        if(authHeader!=null&&authHeader.startsWith("Bearer")){
            jwt=authHeader.substring(7);//去掉"Bearer"前缀
            try{
                username=jwtUtils.extractUsername(jwt);//从token里取出用户名
            } catch (Exception e) {
                logger.warn("JWT Token 解析失败:"+e.getMessage());
            }
        }
        //3.如果拿到了用户名，且上下文中没有认证信息(避免重复认证)
        if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null){
            //根据用户名加载用户信息
            UserDetails userDetails=this.userDetailsService.loadUserByUsername(username);

            //4.校验Token是否有效
            if(jwtUtils.validateToken(jwt)){
                //5.创建认证令牌，放入Security 上下文(告诉Spring Security,这个用户已经登录)
                UsernamePasswordAuthenticationToken authToken=new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                System.out.println("认证已成功放入上下文！");
            }
        }


        //6.放行请求，进入下一个过滤器或者Controller
        filterChain.doFilter(request,response);
    }
}
