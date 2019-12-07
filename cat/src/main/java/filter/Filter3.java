package filter;


import com.alibaba.fastjson.JSONObject;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
//@WebFilter(filterName = "Filter 3",urlPatterns = "/*")
public class Filter3 implements Filter {
    @Override
    public void init(FilterConfig filterConfig)  {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse)response;
        /*res.setContentType("text/html;charset=UTF-8");
        res.setCharacterEncoding("UTF-8");*/
        HttpSession session = req.getSession(false);
        JSONObject message = new JSONObject();
        String URI = ((HttpServletRequest) request).getRequestURI();
        if(URI.contains("log")){
        }else if(session==null||session.getAttribute("currentUser")==null){
            message.put("message","请登录或重新登录");
            res.getWriter().println(message);
            return;
        }
        System.out.println("Filter - 3 - is begin");
        chain.doFilter(request,response);
        System.out.println("Filter - 3 - is ends");
    }

    @Override
    public void destroy() {

    }

}
