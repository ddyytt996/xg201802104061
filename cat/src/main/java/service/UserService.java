package service;


import dao.UserDao;
import domain.Teacher;
import domain.User;

import java.sql.Connection;
import java.sql.SQLException;

public final class UserService {
    private UserDao userDao = UserDao.getInstance();
	private static UserService userService = new UserService();
	
	public UserService() {
	}
	public static UserService getInstance(){
		return UserService.userService;
	}
	
	public boolean changePassword(User user) throws SQLException {
	    return userDao.changePassword(user);
    }

    public User findByUsername(String username) throws SQLException {
	    return userDao.findByUsername(username);
    }

    public boolean addByTeacher(Teacher teacher, Connection connection, int teacher_id) throws SQLException {
        return userDao.addByTeacher(teacher,connection,teacher_id);
    }
    public boolean deleteByTeacher(Teacher teacher,Connection connection) throws SQLException {
	    return userDao.deleteByTeacher(teacher,connection);
    }
    public User login(String username,String password) throws SQLException {
	    return userDao.login(username,password);
    }
}
