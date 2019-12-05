package security;


import com.alibaba.fastjson.JSONObject;
import domain.User;
import service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/login")
public class LoginController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        //resp.setCharacterEncoding("UTF-8");
        //req.setCharacterEncoding("UTF-8");*/
        JSONObject message = new JSONObject();
        try{
            User user = UserService.getInstance().login(username,password);
            if(user!=null){
                message.put("message","登录成功");
                HttpSession session = req.getSession();
                session.setMaxInactiveInterval(10*60);
                session.setAttribute("currentUser",user);
            }else{
                message.put("message","用户名或密码错误");
            }
        } catch (SQLException e) {
            message.put("message","数据库操作异常");
            e.printStackTrace();
        }catch (Exception e){
            message.put("message","网络异常");
        }
        resp.getWriter().println(message);
    }
}
