package com.myxh.api.statement;

import com.mysql.cj.jdbc.Driver;

import java.sql.*;

/**
 * @author MYXH
 * @date 2023/5/9
 * @Description 利用 JDBC 技术, 完成用户数据查询工作
 *              使用 Statement 查询 t_user 表的全部数据
 * TODO: 步骤总结 (6步)
 *     1. 注册驱动
 *     2. 获取连接
 *     3. 创建 Statement
 *     4. 发送 SQL 语句, 并获取结果
 *     5. 结果集解析
 *     6. 关闭资源
 */
public class StatementQueryPart
{
    /**
     * TODO:
     *     DriverManger
     *     Connection
     *     Statement
     *     ResultSet
     */
    public static void main(String[] args) throws SQLException
    {
        // 1. 注册驱动
        /*
        TODO:
            注册驱动
            依赖: 驱动版本 8+ com.mysql.cj.jdbc.Driver
                 驱动版本 5+ com.mysql.jdbc.Driver
         */
        DriverManager.registerDriver(new Driver());

        // 2. 获取链接
        /*
        TODO:
            Java 程序要和数据库创建连接
            Java 程序连接数据库，需要调用某个方法，方法也需要填入连接数据库的基本信息:
                数据库 ip 地址: 127.0.0.1
                数据库端口号: 3306
                账号: MYXH
                密码: 520.ILY!
                连接数据库的名称: my_jdbc
         */

        /*
        参数1: url
              jdbc:数据库厂商名://ip地址:port/数据库名
              jdbc:mysql://127.0.0.1:3306/my_jdbc
        参数2: username 数据库软件的账号 MYXH
        参数3: password 数据库软件的密码 520.ILY!
         */

        // java.sql 接口 = 实现类
        Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/my_jdbc", "MYXH", "520.ILY!");

        // 3. 创建 Statement
        Statement statement = connection.createStatement();

        // 4. 发送 SQL 语句, 并且获取返回结果
        String sql = "select * from t_user;";
        ResultSet resultSet = statement.executeQuery(sql);

        // 5. 进行结果集解析
        // 判断有没有下一行数据, 并获取
        while (resultSet.next())
        {
            int id = resultSet.getInt("id");
            String account = resultSet.getString("account");
            String password = resultSet.getString("password");
            String nickname = resultSet.getString("nickname");

            System.out.println(id + "--" + account + "--" + password + "--" + nickname);
        }

        // 6. 关闭资源
        resultSet.close();
        statement.close();
        connection.close();
    }
}
