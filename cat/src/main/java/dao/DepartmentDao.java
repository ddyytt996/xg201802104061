package dao;


import domain.Department;
import domain.School;
import util.JdbcHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.TreeSet;

public final class DepartmentDao {
	private static Collection<Department> departments;

	private static DepartmentDao departmentDao=new DepartmentDao();
	private DepartmentDao(){}

	public static DepartmentDao getInstance(){
		return departmentDao;
	}

	public Collection<Department> findAllBySchool(int schoolId) throws SQLException{
		Connection connection  = JdbcHelper.getConn();
		PreparedStatement preparedStatement = connection.prepareStatement("SELECT * from department where school_id =?");
		preparedStatement.setInt(1,schoolId);
		ResultSet resultSet = preparedStatement.executeQuery();
		Collection departments = new TreeSet<Department>();
		while (resultSet.next()){
			int id = resultSet.getInt("id");
			String no = resultSet.getString("no");
			String description = resultSet.getString("description");
			String remarks = resultSet.getString("remarks");
			int schoolID = resultSet.getInt("school_id");
			School school = SchoolDao.find(schoolID);
			Department department = new Department(id,description,no,remarks,school);
			departments.add(department);
		}
		JdbcHelper.close(resultSet,preparedStatement,connection);
		return departments;
	}

	public Collection<Department> findAll() throws SQLException {
		Connection connection = JdbcHelper.getConn();
		PreparedStatement preparedStatement = connection.prepareStatement("SELECT * from department");
		ResultSet resultSet = preparedStatement.executeQuery();
		Collection departments = new TreeSet<Department>();
		while(resultSet.next()){
			int id = resultSet.getInt("id");
			String no = resultSet.getString("no");
			String description = resultSet.getString("description");
			String remarks = resultSet.getString("remarks");
			int schoolID = resultSet.getInt("school_id");
			School school = SchoolDao.find(schoolID);
			Department department = new Department(id,description,no,remarks,school);
			departments.add(department);
		}
		JdbcHelper.close(resultSet,preparedStatement,connection);
		return departments;
	}

	public Department find(Integer id) throws SQLException {
		Department desiredDepartment = null;
		Connection connection = JdbcHelper.getConn();
		PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM DEPARTMENT WHERE ID=?");
		preparedStatement.setInt(1,id);
		ResultSet resultSet = preparedStatement.executeQuery();
		if(resultSet.next()){
			desiredDepartment = new Department(id,resultSet.getString("description"),
					resultSet.getString("no"),resultSet.getString("remarks"),
					SchoolDao.getInstance().find(resultSet.getInt("school_id")));
		}
		JdbcHelper.close(resultSet,preparedStatement,connection);
		return  desiredDepartment;
	}

	public boolean update(Department department) throws SQLException {
		Connection connection = JdbcHelper.getConn();
		PreparedStatement preparedStatement = connection.prepareStatement("update department set description=? ,no=? ,remarks=? ,school_id=? where id=?");
		preparedStatement.setString(1,department.getDescription());
		preparedStatement.setString(2,department.getNo());
		preparedStatement.setString(3,department.getRemarks());
		preparedStatement.setInt(4,department.getSchool().getId());
		preparedStatement.setInt(5,department.getId());
		int a = preparedStatement.executeUpdate();
		JdbcHelper.close(preparedStatement,connection);
		if (a!=0){
			return true;
		}else{
			return false;
		}
	}

	public boolean add(Department department) throws SQLException {
		Connection  connection= JdbcHelper.getConn();
		PreparedStatement preparedStatement =  connection.prepareStatement("INSERT INTO department (no,description,remarks,school_id) VALUES " +
				"(?,?,?,?)");
		preparedStatement.setString(1,department.getNo());
		preparedStatement.setString(2,department.getDescription());
		preparedStatement.setString(3,department.getRemarks());
		preparedStatement.setInt(4,department.getSchool().getId());
		int isAdded = preparedStatement.executeUpdate();
		JdbcHelper.close(null,preparedStatement,connection);
		return isAdded>0;
	}

	public boolean delete(Integer id) throws SQLException {
		Connection connection = JdbcHelper.getConn();
		PreparedStatement preparedStatement = connection.prepareStatement("DELETE from department where id=?");
		preparedStatement.setInt(1,id);
		int a = preparedStatement.executeUpdate();
		JdbcHelper.close(preparedStatement,connection);
		return a>0;
	}

	public boolean delete(Department department) throws SQLException {
		Connection connection = JdbcHelper.getConn();
		PreparedStatement preparedStatement = connection.prepareStatement("DELETE from department where id=?");
		preparedStatement.setInt(1,department.getId());
		int a = preparedStatement.executeUpdate();
		JdbcHelper.close(preparedStatement,connection);
		return a>0;
	}
}

