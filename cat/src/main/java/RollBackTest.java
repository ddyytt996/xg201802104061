import util.JdbcHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RollBackTest {
    public static void main(String[] args) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = JdbcHelper.getConn();
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(
                    "INSERT INTO SCHOOL(description,no)values(?,?)"
            );
            preparedStatement.setString(1,"管理学院");
            preparedStatement.setString(2,"02");
            preparedStatement.executeUpdate();
            preparedStatement = connection.prepareStatement(
                    "insert into school(description,no)values(?,?) "
            );
            preparedStatement.setString(1,"土木学院");
            preparedStatement.setString(2,"02");
            preparedStatement.executeUpdate();
            connection.commit();
        }catch (SQLException e){
            System.out.println(e.getMessage()+"\n errorCode="+e.getErrorCode()
            );
            /*
                Duplicate entry '02' for key 'no'
                errorCode=1062
                由于no是unique字段，no的值都等于“02”导致土木学院的信息无法插入表中，所以报错，
                然后执行回滚操作，把第一条的影响消除
             */
            try{
                if(connection!=null){
                    connection.rollback();
                }
            }catch (SQLException e1){
                e1.printStackTrace();
            }
        }finally {
            /*
                恢复数据库设置，关闭数据库事件执行模式
             */
            try {
                if(connection!=null){
                    connection.setAutoCommit(true);
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
            JdbcHelper.close(preparedStatement,connection);
        }
    }
}
