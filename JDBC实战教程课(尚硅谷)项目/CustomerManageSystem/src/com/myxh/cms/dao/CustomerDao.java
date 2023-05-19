package com.myxh.cms.dao;

import com.myxh.cms.javabean.Customer;
import com.myxh.cms.utils.BaseDao;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

/**
 * @author MYXH
 * @date 2023/5/16
 * @Description: CustomerDao对应的数据库方法
 */
public class CustomerDao extends BaseDao
{
    /**
     * 查询数据库客户集合
     */
    public List<Customer> findAll() throws SQLException, NoSuchFieldException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException
    {
        List<Customer> customerList = executeQuery(Customer.class, "select * from t_customer");

        return customerList;
    }

    /**
     * 添加客户的方法
     */
    public void addCustomer(Customer customer) throws SQLException
    {

        String sql = "insert into t_customer(name, gender, age, salary, phone) values(?, ?, ?, ?, ?);";

        executeUpdate(sql, customer.getName(), customer.getGender(),
                customer.getAge(), customer.getSalary(), customer.getPhone());
    }

    /**
     * 修改对象信息
     * @return 影响行数
     */
    public  int updateById(Customer customer) throws SQLException
    {

        String sql = "update t_customer set name = ?, gender = ?, age= ?, salary = ?, phone = ? where id = ?;";

        int rows = executeUpdate(sql, customer.getName(), customer.getGender(), customer.getAge(),
                customer.getSalary(), customer.getPhone(), customer.getId());

        return rows;
    }

    /**
     * 根据id查询客户信息
     */
    public Customer findById(int id) throws SQLException, NoSuchFieldException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException
    {
        String sql = "select * from t_customer where id = ?;";

        List<Customer> customerList = executeQuery(Customer.class, sql, id);

        if (customerList != null && customerList.size() > 0)
        {
            return customerList.get(0);
        }

        return null;
    }

    public int removeById(int id) throws SQLException
    {
        String sql = "delete from t_customer where id = ? ;";

        int rows = executeUpdate(sql, id);

        return rows;
    }
}
