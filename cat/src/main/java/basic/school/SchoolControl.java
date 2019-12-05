package basic.school;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import dao.SchoolDao;
import domain.School;
import service.SchoolService;
import util.JSONUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

@WebServlet("/school.ctl")
public class SchoolControl extends HttpServlet {

    /**
     * 查操作：只查一个(id)；全查
     * 测试服务器url：GET     49.234.234.176：8080/bysj/school.ctl
     * 远程：http://localhost:8080/bysj/myschool/school.ctl
     * 网址：http://localhost:8080/myschool/school.ctl
     * 本地测试url：GET:localhost：8080/school.ctl
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //设置响应字符编码为UTF-8
        //读取参数id
        String id_str = request.getParameter("id");
        //如果id = null, 表示响应所有学位对象，否则响应id指定的学位对象
        JSONObject message = new JSONObject();
        if(id_str == null){
            try {
                responseschools(response);
                return;
            } catch (SQLException e) {
                e.printStackTrace();
                //加入数据信息
                message.put("message", "添加失败");
            } catch (Exception e){
                e.printStackTrace();
                message.put("message","网络异常");
            }
        }else{
            int id = Integer.parseInt(id_str);
            try {
                responseschool(id, response);
                return;
            } catch (SQLException e) {
                e.printStackTrace();
                //加入数据信息
                message.put("message", "添加失败");
            } catch (Exception e){
                e.printStackTrace();
                message.put("message","网络异常");
            }
        }
        response.getWriter().println(message);
    }
    //响应一个学位对象
    private void responseschool(int id, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //根据id查找老师
        School school = SchoolService.getInstance().find(id);
        if(school==null){
            response.getWriter().println("查无此人");
            return;
        }
        String school_json = JSON.toJSONString(school);
        //响应
        response.getWriter().println(school_json);
    }

    //响应所有学位对象
    private void responseschools(HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //获得所有学院
        Collection<School> schools = SchoolService.getInstance().findAll();
        String schools_json = JSON.toJSONString(schools);
        //响应message到前端
        response.getWriter().println(schools_json);
    }

    /**
     * 增加操作，从前台接受一个不带id的school类型的json对象
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //根据request对象，获得代表参数的JSON字串
        String school_json = JSONUtil.getJSON(request);
        //将JSON字串解析为school对象
        School schoolToAdd = JSON.parseObject(school_json,School.class);
        //创建JSON对象
        JSONObject resp = new JSONObject();
        //增加加school对象
        try {
            if(SchoolService.getInstance().add(schoolToAdd)){
                //加入数据信息
                resp.put("MSG", "OK");
            }else {
                resp.put("MSG","数据库未发生改变");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            resp.put("MSG","后端程序抛出异常");
        }
        response.getWriter().println(resp);
    }

    /**
     * 修改操作，需传入一个含id值的json对象
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String school_json = JSONUtil.getJSON(request);
        //将JSON字串解析为school对象
        School schoolToAdd = JSON.parseObject(school_json, School.class);
        JSONObject message = new JSONObject();
        //增加加school对象
        try {
            if(SchoolService.getInstance().update(schoolToAdd)){
                message.put("message","修改成功");
            }else{
                message.put("message","数据库未发生改变");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            message.put("message","后台程序抛出异常");
        }
        //响应message到前端
        response.getWriter().println(message);
    }

    /**
     * 删除操作，前端传输一个id字段
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id_str = request.getParameter("id");
        Integer id = Integer.parseInt(id_str);
        JSONObject message = new JSONObject();
        try {
            if(SchoolDao.getInstance().delete(id)){
                message.put("message","删除成功");
            }else{
                message.put("message","数据库未发生改变");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        response.getWriter().println(message);
    }
}
