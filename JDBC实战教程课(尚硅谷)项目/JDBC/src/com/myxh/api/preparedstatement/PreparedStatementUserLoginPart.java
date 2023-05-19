package com.myxh.api.preparedstatement;

import java.sql.*;
import java.util.Scanner;

/**
 * @author MYXH
 * @date 2023/5/10
 * @Description 使用预编译 Statement 解决注入攻击问题, 完成用户登录
 *
 * TODO: 防止注入攻击, 演示 PreparedStatement 的使用流程
 */
public class PreparedStatementUserLoginPart
{
    public static void main(String[] args) throws ClassNotFoundException, SQLException
    {
        // 1. 获取用户输入信息
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入账号：");
        String account = scanner.nextLine();
        System.out.println("请输入密码：");
        String password = scanner.nextLine();

        // PreparedStatement 的数据库流程
        // 2. 注册驱动
        Class.forName("com.mysql.cj.jdbc.Driver");

        // 3. 获取连接
        Connection connection = DriverManager.getConnection("jdbc:mysql:///my_jdbc?user=MYXH&password=520.ILY!");

        /*
        Statement
            1. 创建 Statement
            2. 拼接 SQL 语句
            3. 发送 SQL 语句, 并且获取返回结果

        PreparedStatement
            1. 编写 SQL 语句结果, 不包含动态值部分的语句, 动态值部分使用占位符 ? 替代, 注意: ? 只能替代动态值
            2. 创建 PreparedStatement, 并且传入动态值
            3. 动态值占位符赋值 ? 单独赋值即可
            4. 发送 SQL 语句即可,并获取返回结果
         */

        // 4. 编写 SQL 语句结果
        String sql = "select * from t_user where account = ? and password = ? ;";

        // 5. 创建预编译 Statement 并且设置 SQL 语句结果
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        // 6. 单独的占位符进行赋值
        /*
        参数 1: index 占位符的位置从左向右数从 1 开始, 账号 ? 1
        参数 2: object 占位符的值可以设置任何类型的数据，避免了我们拼接且类型更加丰富
         */
        preparedStatement.setObject(1, account);
        preparedStatement.setObject(2, password);

        // 7. 发送 SQL 语句, 并获取返回结果
        /*
        statement.executeUpdate 或 executeQuery (String sql);
        preparedStatement.executeUpdate 或 executeQuery();    TODO: 因为它已经知道语句，知道语句动态值
         */
        ResultSet resultSet = preparedStatement.executeQuery();

        // 8. 结果集解析
        if (resultSet.next())
        {
            System.out.println("登录成功！");
        }
        else
        {
            System.out.println("登录失败！");
        }

        // 9. 关闭数据
        resultSet.close();
        preparedStatement.close();
        connection.close();
    }
}
