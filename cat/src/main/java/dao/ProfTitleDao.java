package dao;


import domain.ProfTitle;
import util.JdbcHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.TreeSet;

public final class ProfTitleDao {
	private static ProfTitleDao profTitleDao=new ProfTitleDao();
	private ProfTitleDao(){}
	public static ProfTitleDao getInstance(){
		return profTitleDao;
	}
	private static Collection<ProfTitle> profTitles;

	public Collection<ProfTitle> findAll() throws SQLException {
		Connection connection = JdbcHelper.getConn();
		PreparedStatement preparedStatement = connection.prepareStatement("SELECT * from proftitle");
		ResultSet resultSet = preparedStatement.executeQuery();
		profTitles = new TreeSet<ProfTitle>();
		while(resultSet.next()){
			int id = resultSet.getInt("id");
			String no = resultSet.getString("no");
			String description = resultSet.getString("description");
			String remarks = resultSet.getString("remarks");
			ProfTitle profTitle = new ProfTitle(id,description,no,remarks);
			profTitles.add(profTitle);
		}
		JdbcHelper.close(resultSet,preparedStatement,connection);
		return profTitles;
	}

	public ProfTitle find(Integer id) throws SQLException{
		ProfTitle profTitle = null;
		Connection connection = JdbcHelper.getConn();
		PreparedStatement preparedStatement = connection.prepareStatement("select * from proftitle where id = ?");
		preparedStatement.setInt(1,id);
		ResultSet resultSet = preparedStatement.executeQuery();
		if(resultSet.next()){
			profTitle = new ProfTitle(id,resultSet.getString("description"),resultSet.getString("no")
					,resultSet.getString("remarks"));
		}
		JdbcHelper.close(resultSet,preparedStatement,connection);
		return profTitle;
	}

	public boolean update(ProfTitle profTitle)throws SQLException{
		Connection connection = JdbcHelper.getConn();
		PreparedStatement preparedStatement = connection.prepareStatement("update proftitle set description = ?" +
				", no =?, remarks = ? where id = ?");
		preparedStatement.setString(1,profTitle.getDescription());
		preparedStatement.setString(2,profTitle.getNo());
		preparedStatement.setString(3,profTitle.getRemarks());
		preparedStatement.setInt(4,profTitle.getId());
		int a = preparedStatement.executeUpdate();
		JdbcHelper.close(preparedStatement,connection);
		if (a!=0){
			return true;
		}else{
			return false;
		}
	}

	public boolean add(ProfTitle profTitle)throws SQLException{
		Connection  connection= JdbcHelper.getConn();
		PreparedStatement preparedStatement =  connection.prepareStatement("INSERT INTO proftitle (no,description,remarks) VALUES " +
				"(?,?,?)");
		preparedStatement.setString(1,profTitle.getNo());
		preparedStatement.setString(2,profTitle.getDescription());
		preparedStatement.setString(3,profTitle.getRemarks());
		int isAdded = preparedStatement.executeUpdate();
		JdbcHelper.close(preparedStatement,connection);
		return isAdded>0;
	}

	public boolean delete(Integer id)throws SQLException{
		Connection connection = JdbcHelper.getConn();
		PreparedStatement preparedStatement = connection.prepareStatement("DELETE from proftitle where id=?");
		preparedStatement.setInt(1,id);
		int a = preparedStatement.executeUpdate();
		JdbcHelper.close(preparedStatement,connection);
		return a>0;
	}

	public boolean delete(ProfTitle profTitle)throws SQLException{
		Connection connection = JdbcHelper.getConn();
		PreparedStatement preparedStatement = connection.prepareStatement("DELETE from proftitle where id=?");
		preparedStatement.setInt(1,profTitle.getId());
		int a = preparedStatement.executeUpdate();
		JdbcHelper.close(preparedStatement,connection);
		return a>0;
	}
}

