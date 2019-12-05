import dao.DepartmentDao;
import dao.SchoolDao;
import domain.Department;
import domain.School;

import java.sql.SQLException;

public class Test {
    public static void main(String[] args) throws SQLException {
        //从数据库查找
        Department department = DepartmentDao.getInstance().find(1);
        System.out.println(department.toString());
        department.setDescription("工业管理");
        //从数据库更改查到的这个department
        DepartmentDao.getInstance().update(department);
        //重新从数据查找
        Department department2 = DepartmentDao.getInstance().find(1);
        System.out.println(department2);
        School school = SchoolDao.getInstance().find(1);
        Department department3 = new Department(2,"网管","02","the worst",school);
        Boolean b = DepartmentDao.getInstance().add(department3);
        System.out.println(b);
    }
}
