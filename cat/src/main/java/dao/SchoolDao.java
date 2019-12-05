package dao;

import domain.School;
import util.JdbcHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.TreeSet;

public final class SchoolDao {
	private static SchoolDao schoolDao=
			new SchoolDao();
	private SchoolDao(){}
	public static SchoolDao getInstance(){
		return schoolDao;
	}
	private static Collection<School> schools;

	public static Collection<School> findAll() throws SQLException {
		Connection connection = JdbcHelper.getConn();
		PreparedStatement preparedStatement = connection.prepareStatement("SELECT * from school");
		ResultSet resultSet = preparedStatement.executeQuery();
		schools = new TreeSet<School>();
		while(resultSet.next()){
			int id = resultSet.getInt("id");
			String no = resultSet.getString("no");
			String description = resultSet.getString("description");
			String remarks = resultSet.getString("remarks");
			School school = new School(id,description,no,remarks);
			schools.add(school);
		}
		JdbcHelper.close(resultSet,preparedStatement,connection);
		return schoolDao.schools;
	}

	public static School find(int id) throws SQLException{
		School desiredschool = null;
		Connection connection = JdbcHelper.getConn();
		PreparedStatement preparedStatement = connection.prepareStatement("select * from school where id = ?");
		preparedStatement.setInt(1,id);
		ResultSet resultSet = preparedStatement.executeQuery();
		if(resultSet.next()){
			desiredschool = new School(id,resultSet.getString("description"),resultSet.getString("no")
					,resultSet.getString("remarks"));
		}
		JdbcHelper.close(resultSet,preparedStatement,connection);
		return desiredschool;
	}
	public boolean update(School school)throws  SQLException{
		Connection connection = JdbcHelper.getConn();
		PreparedStatement preparedStatement = connection.prepareStatement("update school set description = ?" +
				",no =?, remarks = ? where id = ?");
		preparedStatement.setString(1,school.getDescription());
		preparedStatement.setString(2,school.getNo());
		preparedStatement.setString(3,school.getRemarks());
		preparedStatement.setInt(4,school.getId());
		int a = preparedStatement.executeUpdate();
		JdbcHelper.close(preparedStatement,connection);
		return a>0;
	}

	public boolean add(School school) throws SQLException {
		Connection  connection= JdbcHelper.getConn();
		PreparedStatement preparedStatement =  connection.prepareStatement("INSERT INTO school (no,description,remarks) VALUES " +
				"(?,?,?)");
		preparedStatement.setString(1,school.getNo());
		preparedStatement.setString(2,school.getDescription());
		preparedStatement.setString(3,school.getRemarks());
		int isAdded = preparedStatement.executeUpdate();
		JdbcHelper.close(null,preparedStatement,connection);
		return isAdded>0;
	}

	public boolean delete(Integer id) throws SQLException {
		Connection connection = JdbcHelper.getConn();
		PreparedStatement preparedStatement = connection.prepareStatement("DELETE from school where id=?");
		preparedStatement.setInt(1,id);
		int a = preparedStatement.executeUpdate();
		JdbcHelper.close(preparedStatement,connection);
		return a>0;
	}

	public boolean delete(School school) throws SQLException {
		Connection connection = JdbcHelper.getConn();
		PreparedStatement preparedStatement = connection.prepareStatement("DELETE from school where id=?");
		preparedStatement.setInt(1,school.getId());
		int a = preparedStatement.executeUpdate();
		JdbcHelper.close(preparedStatement,connection);
		return a>0;
	}
}

