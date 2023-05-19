package com.myxh.api.transaction;

import org.junit.Test;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author MYXH
 * @date 2023/5/12
 * Description: bank 表业务类, 添加转账业务
 *              银行卡业务方法, 调用 BankDAO 的方法
 */
public class BankService
{
    @Test
    public void testStart() throws SQLException, ClassNotFoundException
    {
        // 经理给管理员转 500 元
        transFer("管理员", "经理", BigDecimal.valueOf(500));
    }

    /**
     * TODO:
     *     事务添加是在业务方法中
     *     利用 try catch 代码块, 开启事务, 提交事务和事务回滚
     *     将 Connection 传入 BankDao 层即可, BankDao 只负责使用, 不负责 close();
     */
    public void transFer(String addAccount, String subAccount, BigDecimal money) throws SQLException, ClassNotFoundException
    {
        BankDao bankDao = new BankDao();

        // 一个事务的最基本要求, 必须是同一个连接对象 connection
        // 一个转账方法属于一个事务〔存钱、取钱)
        // 1. 注册驱动
        Class.forName("com.mysql.cj.jdbc.Driver");

        // 2. 获取连接
        Connection connection = DriverManager.getConnection("jdbc:mysql:///my_jdbc?user=MYXH&password=520.ILY!");

        try
        {
            // 开启事务
            // 关闭事务提交
            connection.setAutoCommit(false);

            // 执行数据库动作
            bankDao.add(addAccount, money, connection);
            System.out.println("----------");
            bankDao.sub(subAccount, money, connection);

            // 事务提交
            connection.commit();
        }
        catch (Exception e)
        {
            // 事务回滚
            connection.rollback();

            // 抛出异常信息
            throw e;
        }
        finally
        {
            connection.close();
        }
    }
}
