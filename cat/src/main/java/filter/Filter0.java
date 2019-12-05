package filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "Filter 0",urlPatterns = "/*")
public class Filter0 implements Filter {
    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        String path = request.getRequestURI();
        String method = request.getMethod();
        if(path.contains("/myapp")){
            filterChain.doFilter(servletRequest,servletResponse);
        } else {
            System.out.println(method);
            response.setContentType("text/html;charset=utf-8");
            if (("POST-PUT").contains(method)) {
                request.setCharacterEncoding("UTF-8");
            }
        }
        System.out.println("Filter 0 - setUTF begin");
        filterChain.doFilter(servletRequest,servletResponse);
        System.out.println("Filter 0 - setUTF ends");
    }
}
