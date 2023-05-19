package com.myxh.api.transactionbyutils;

import com.myxh.api.utils.JdbcUtilsVersion2;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author MYXH
 * @date 2023/5/15
 * Description: bank 表的数据库操作方法存储类
 */
public class BankDao
{
    /**
     * 存钱的数据库操作方法(jdbc)
     * @param account 存钱的账号
     * @param money 存钱的金额
     */
    public void add(String account, BigDecimal money) throws SQLException
    {
        Connection connection = JdbcUtilsVersion2.getConnection();

        // 3. 编写 SQL 语句结构
        String sql = "update t_bank set money = money + ? where account = ? ;";

        // 4. 创建 PreparedStatement
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        // 5. 占位符赋值
        preparedStatement.setObject(1, money);
        preparedStatement.setObject(2, account);

        // 6. 发送 SQL 语句
        preparedStatement.executeUpdate();

        // 7. 关闭资源
        preparedStatement.close();

        System.out.println("存钱成功！");
    }

    /**
     * 取钱的数据库操作方法(jdbc)
     * @param account 取钱的账号
     * @param money 取钱的金额
     */
    public void sub(String account, BigDecimal money) throws SQLException
    {
        Connection connection = JdbcUtilsVersion2.getConnection();

        // 3. 编写 SQL 语句结构
        String sql = "update t_bank set money = money - ? where account = ? ;";

        // 4. 创建 PreparedStatement
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        // 5. 占位符赋值
        preparedStatement.setObject(1, money);
        preparedStatement.setObject(2, account);

        // 6. 发送 SQL 语句
        preparedStatement.executeUpdate();

        // 7. 关闭资源
        preparedStatement.close();

        System.out.println("取钱成功！");
    }
}
