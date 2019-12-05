package filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "Filter 2",urlPatterns = "/*")
public class Filter2 implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        String path = request.getRequestURI();
        if(path.contains("/myapp")){
            filterChain.doFilter(servletRequest,servletResponse);
        }
        else if(path.contains("/login")){
            filterChain.doFilter(servletRequest,servletResponse);
        }else{
            System.out.println("Filter 2 - setUTF begins");
            if(request.getMethod().equals("POST")||request.getMethod().equals("PUT")){
                request.setCharacterEncoding("UTF-8");
                response.setCharacterEncoding("UTF-8");
            }else if(request.getMethod().equals("DELETE")||request.getMethod().equals("GET")){
                response.setCharacterEncoding("UTF-8");
            }
            filterChain.doFilter(servletRequest,servletResponse);
            System.out.println("Filter 2 - setUTF ends");
        }
    }
}
