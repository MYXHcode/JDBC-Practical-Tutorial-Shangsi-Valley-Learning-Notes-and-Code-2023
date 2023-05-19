package com.myxh.cms.service;

import com.myxh.cms.dao.CustomerDao;
import com.myxh.cms.javabean.Customer;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

/**
 * @author MYXH
 * @date 2023/5/16
 * 这是一个具有管理功能的功能类, 内部数据不允许外部随意修改, 具有更好的封装性
 */
public class CustomerService
{
    private final CustomerDao customerDao = new CustomerDao();

    /**
     * 用途： 返回所有客户对象
     * 返回： 集合
     */
    public List<Customer> getList()
    {
        try
        {
            return customerDao.findAll();
        }
        catch (SQLException | NoSuchFieldException | InstantiationException | IllegalAccessException |
               InvocationTargetException | NoSuchMethodException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * 用途：添加新客户
     * 参数：customer 指定要添加的客户对象
     */
    public void addCustomer(Customer customer)
    {
        try
        {
            customerDao.addCustomer(customer);
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * 修改指定 id 号的客户对象的信息
     * @param id 客户 id
     * @param customer 对象
     * @return 修改成功返回 true, false 表明指定 id 的客户未找到
     */
    public boolean modifyCustomer(int id, Customer customer)
    {
        int rows = 0;

        try
        {
            rows = customerDao.updateById(customer);
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }

        // 返回修改成功
        return rows != 0;
    }

    /**
     * 用途：返回指定 id 的客户对象记录
     * 参数： id 就是要获取的客户的 id 号
     * 返回：封装了客户信息的 Customer 对象
     */
    public Customer getCustomer(int id)
    {
        try
        {
            return customerDao.findById(id);
        }
        catch (SQLException | NoSuchFieldException | InstantiationException | IllegalAccessException |
               NoSuchMethodException | InvocationTargetException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * 用途：删除指定 id 号的的客户对象记录
     * 参数： id 要删除的客户的 id 号
     * 返回：删除成功返回 true, false表 示没有找到
     */
    public boolean removeCustomer(int id)
    {
        int rows = 0;

        try
        {
            rows = customerDao.removeById(id);
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }

        return rows != 0;
    }
}
