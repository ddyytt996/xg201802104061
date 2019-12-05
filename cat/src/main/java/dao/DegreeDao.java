package dao;


import domain.Degree;
import util.JdbcHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.TreeSet;

public final class DegreeDao {
	private static DegreeDao degreeDao=
			new DegreeDao();
	private DegreeDao(){}
	public static DegreeDao getInstance(){
		return degreeDao;
	}
	private static Collection<Degree> degrees;

	public Collection<Degree> findAll() throws SQLException {
		Connection connection = JdbcHelper.getConn();
		PreparedStatement preparedStatement = connection.prepareStatement("SELECT * from degree");
		ResultSet resultSet = preparedStatement.executeQuery();
		degrees = new TreeSet<Degree>();
		while(resultSet.next()){
			int id = resultSet.getInt("id");
			String no = resultSet.getString("no");
			String description = resultSet.getString("description");
			String remarks = resultSet.getString("remarks");
			Degree degree = new Degree(id,description,no,remarks);
			degrees.add(degree);
		}
		JdbcHelper.close(resultSet,preparedStatement,connection);
		return DegreeDao.degrees;
	}

	public Degree find(int id) throws SQLException{
		Degree desiredDegree = null;
		Connection connection = JdbcHelper.getConn();
		PreparedStatement preparedStatement = connection.prepareStatement("select * from degree where id = ?");
		preparedStatement.setInt(1,id);
		ResultSet resultSet = preparedStatement.executeQuery();
		if(resultSet.next()){
			desiredDegree = new Degree(id,resultSet.getString("description"),resultSet.getString("no")
					,resultSet.getString("remarks"));
		}
		JdbcHelper.close(resultSet,preparedStatement,connection);
		return desiredDegree;
	}
	public boolean update(Degree degree)throws  SQLException{
		Connection connection = JdbcHelper.getConn();
		PreparedStatement preparedStatement = connection.prepareStatement("update degree set description = ?" +
				",no =?, remarks = ? where id = ?");
		preparedStatement.setString(1,degree.getDescription());
		preparedStatement.setString(2,degree.getNo());
		preparedStatement.setString(3,degree.getRemarks());
		preparedStatement.setInt(4,degree.getId());
		//System.out.println(preparedStatement.toString());
		int a = preparedStatement.executeUpdate();
		JdbcHelper.close(preparedStatement,connection);
		return a>0;
	}

	public boolean add(Degree degree) throws SQLException {
		Connection  connection= JdbcHelper.getConn();
		PreparedStatement preparedStatement =  connection.prepareStatement("INSERT INTO degree (no,description,remarks) VALUES " +
				"(?,?,?)");
		preparedStatement.setString(1,degree.getNo());
		preparedStatement.setString(2,degree.getDescription());
		preparedStatement.setString(3,degree.getRemarks());
		int isAdded = preparedStatement.executeUpdate();
		JdbcHelper.close(null,preparedStatement,connection);
		return isAdded>0;
	}

	public boolean delete(Integer id) throws SQLException {
		Connection connection = JdbcHelper.getConn();
		PreparedStatement preparedStatement = connection.prepareStatement("DELETE from degree where id=?");
		preparedStatement.setInt(1,id);
		int a = preparedStatement.executeUpdate();
		JdbcHelper.close(preparedStatement,connection);
		return a>0;
	}

	public boolean delete(Degree degree) throws SQLException {
		Connection connection = JdbcHelper.getConn();
		PreparedStatement preparedStatement = connection.prepareStatement("DELETE from degree where id=?");
		preparedStatement.setInt(1,degree.getId());
		int a = preparedStatement.executeUpdate();
		JdbcHelper.close(preparedStatement,connection);
		return a>0;
	}
}

