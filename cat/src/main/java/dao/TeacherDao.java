package dao;


import domain.*;
import util.JdbcHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;


public final class TeacherDao {
	private static TeacherDao teacherDao=new TeacherDao();
	private TeacherDao(){}
	public static TeacherDao getInstance(){
		return teacherDao;
	}
	private static Collection<Teacher> teachers;

	public Collection<Teacher> findAll()throws SQLException{
		Connection connection = JdbcHelper.getConn();
		PreparedStatement preparedStatement = connection.prepareStatement("SELECT * from teacher");
		ResultSet resultSet = preparedStatement.executeQuery();
		Collection teachers = new HashSet<Teacher>();
		while(resultSet.next()){
			int id = resultSet.getInt("id");
			String no = resultSet.getString("no");
			String name = resultSet.getString("name");
			int degreeID = resultSet.getInt("degree_id");
			int departmentID = resultSet.getInt("department_id");
			int proftitleID = resultSet.getInt("title_id");
			Degree degree = DegreeDao.getInstance().find(degreeID);
			Department department = DepartmentDao.getInstance().find(departmentID);
			ProfTitle profTitle = ProfTitleDao.getInstance().find(proftitleID);
			Teacher teacher = new Teacher(no,id,name,profTitle,degree,department);
			teachers.add(teacher);
		}
		JdbcHelper.close(resultSet,preparedStatement,connection);
		return teachers;
	}
	
	public Teacher find(Integer id) throws SQLException{
		Teacher teacher = null;
		Connection connection = JdbcHelper.getConn();
		PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM teacher WHERE ID=?");
		preparedStatement.setInt(1,id);
		ResultSet resultSet = preparedStatement.executeQuery();
		if(resultSet.next()){
			teacher = new Teacher(resultSet.getString("no"),id,resultSet.getString("name"),
					ProfTitleDao.getInstance().find(resultSet.getInt("title_id")),
					DegreeDao.getInstance().find(resultSet.getInt("degree_id")),
					DepartmentDao.getInstance().find(resultSet.getInt("department_id")));
		}
		JdbcHelper.close(resultSet,preparedStatement,connection);
		return  teacher;
	}
	
	public boolean update(Teacher teacher)throws SQLException{
		Connection connection = JdbcHelper.getConn();
		PreparedStatement preparedStatement = connection.prepareStatement("update teacher set name=? ,department_id=? " +
				",degree_id=? ,title_id=?,no=? where id=?");
		preparedStatement.setString(1,teacher.getName());
		preparedStatement.setInt(2,teacher.getDepartment().getId());
		preparedStatement.setInt(3,teacher.getDegree().getId());
		preparedStatement.setInt(4,teacher.getTitle().getId());
		preparedStatement.setString(5,teacher.getNo());
		preparedStatement.setInt(6,teacher.getId());
		int a = preparedStatement.executeUpdate();
		JdbcHelper.close(preparedStatement,connection);
		return a>0;
	}

	public int getMaxId() throws SQLException {
		Connection connection = JdbcHelper.getConn();
		PreparedStatement preparedStatement = connection.prepareStatement("select max(id) from teacher");
		ResultSet resultSet = preparedStatement.executeQuery();
		int maxId = -1;
		if(resultSet.next()){
			maxId = resultSet.getInt("max(id)");
		}
		return maxId;
	}

	public boolean add(Teacher teacher)throws SQLException{
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try{
			int teacherId = getMaxId();
			connection= JdbcHelper.getConn();
			connection.setAutoCommit(false);
			preparedStatement =  connection.prepareStatement("INSERT INTO teacher " +
					"(no,name,title_id,degree_id,department_id) VALUES " +
					"(?,?,?,?,?)");
			preparedStatement.setString(1,teacher.getNo());
			preparedStatement.setString(2,teacher.getName());
			preparedStatement.setInt(3,teacher.getTitle().getId());
			preparedStatement.setInt(4,teacher.getDegree().getId());
			preparedStatement.setInt(5,teacher.getDepartment().getId());
			teacherId = getMaxId()+1;
			int isAdded = preparedStatement.executeUpdate();
			if(UserDao.getInstance().addByTeacher(teacher,connection,teacherId)){
				System.out.println("User表添加成功");
			}
			connection.commit();
			return isAdded>0;
		}catch (SQLException e){
			e.printStackTrace();
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			try {
				if(connection!=null){
					connection.setAutoCommit(true);
				}
			}catch (SQLException e){
				e.printStackTrace();
			}
			JdbcHelper.close(preparedStatement,connection);
		}
		return false;

	}

	public boolean delete(Integer id)throws SQLException{
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try{
			connection = JdbcHelper.getConn();
			connection.setAutoCommit(false);
			preparedStatement = connection.prepareStatement("DELETE from teacher where id=?");
			preparedStatement.setInt(1,id);
			UserDao.getInstance().deleteByTeacher(TeacherDao.getInstance().find(id),connection);
			int a = preparedStatement.executeUpdate();
			connection.commit();
			return a>0;
		}catch (SQLException e){
			e.printStackTrace();
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			try {
				if(connection!=null){
					connection.setAutoCommit(true);
				}
			}catch (SQLException e){
				e.printStackTrace();
			}
			JdbcHelper.close(preparedStatement,connection);
		}

		return false;

	}
	
	public boolean delete(Teacher teacher)throws SQLException{
		Connection connection = JdbcHelper.getConn();
		PreparedStatement preparedStatement = connection.prepareStatement("DELETE from teacher where id=?");
		preparedStatement.setInt(1,teacher.getId());
		int a = preparedStatement.executeUpdate();
		JdbcHelper.close(preparedStatement,connection);
		return a>0;
	}
	
	
	
}
