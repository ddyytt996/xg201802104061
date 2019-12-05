package dao;
import domain.Teacher;
import domain.User;
import util.JdbcHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public final class UserDao {

    private static UserDao userDao=new UserDao();
    private UserDao(){}

    public static UserDao getInstance(){
        return userDao;
    }
    public boolean changePassword(User user) throws SQLException {
        Connection connection = JdbcHelper.getConn();
        PreparedStatement preparedStatement = connection.prepareStatement("update set password=?,username=?,teacher_id=? where" +
                "id = ?");
        preparedStatement.setString(1,user.getPassword());
        preparedStatement.setString(2,user.getUsername());
        preparedStatement.setInt(3,user.getTeacher().getId());
        preparedStatement.setInt(4,user.getId());
        int affected = preparedStatement.executeUpdate();
        JdbcHelper.close(preparedStatement,connection);
        return affected>0;
    }

    public User findByUsername(String username) throws SQLException {
        Connection connection = JdbcHelper.getConn();
        PreparedStatement preparedStatement = connection.prepareStatement("" +
                "select * from user where username=?");
        preparedStatement.setString(1,username);
        ResultSet resultSet = preparedStatement.executeQuery();
        User user =null;
        if(resultSet.next()){
            user = new User(resultSet.getInt("id"),resultSet.getString("username"),
                    resultSet.getString("password"),TeacherDao.getInstance().find(resultSet.getInt("teacher_id")));
        }
        JdbcHelper.close(resultSet,preparedStatement,connection);
        return user;
    }

    public boolean addByTeacher(Teacher teacher,Connection connection,int teacher_id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("" +
                "insert into user(username,password,teacher_id) values (?,?,?)");
        preparedStatement.setString(1,teacher.getNo());
        preparedStatement.setString(2,teacher.getNo());
        preparedStatement.setInt(3,teacher_id);
        int affected =  preparedStatement.executeUpdate();
        return affected > 0;
    }

    public boolean deleteByTeacher(Teacher teacher,Connection connection) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("" +
                "delete from user where teacher_id = ?");
        preparedStatement.setInt(1,teacher.getId());
        int affected =  preparedStatement.executeUpdate();
        return affected > 0;
    }

    public User login(String username,String password) throws SQLException {
        Connection connection = JdbcHelper.getConn();
        PreparedStatement preparedStatement = connection.prepareStatement("select * from teacher where username = ? and " +
                "password = ?");
        preparedStatement.setString(1,username);
        preparedStatement.setString(2,password);
        User user = null;
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next()){
            user = new User(resultSet.getInt("id"),resultSet.getString("username"),
                    resultSet.getString("password"),TeacherDao.getInstance().find(resultSet.getInt("teacher_id")));
        }
        JdbcHelper.close(resultSet,preparedStatement,connection);
        return user;
    }
}

