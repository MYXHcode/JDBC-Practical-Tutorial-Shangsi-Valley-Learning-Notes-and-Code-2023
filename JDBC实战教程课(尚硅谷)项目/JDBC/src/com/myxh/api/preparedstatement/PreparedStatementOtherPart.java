package com.myxh.api.preparedstatement;

import org.junit.Test;

import java.sql.*;

/**
 * @author MYXH
 * @date 2023/5/12
 * Description: 练习 PreparedStatement 的特殊使用情况
 */
public class PreparedStatementOtherPart
{
    /**
     * TODO:
     *     t_user 插入一条数据, 并且获取数据库自增长的主键
     * TODO: 使用总结
     *     1. 创建 PrepareStatement 的时候,告知携带回数据库自增长的主键, prepareStatement(sql,PrepareStatement.RETURN_GENERATED_KEYS);
     *     2．获取 PrepareStatement 装主键值的结果集对象, 一行一列, 获取对应的数据即可, ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
     */
    @Test
    public void testReturnPrimaryKey() throws ClassNotFoundException, SQLException
    {
        // 1. 注册驱动
        Class.forName("com.mysql.cj.jdbc.Driver");

        // 2. 获取连接
        Connection connection = DriverManager.getConnection("jdbc:mysql:///my_jdbc?user=MYXH&password=520.ILY!");

        // 3. 编写 SQL 语句
        String sql = "insert into t_user(account, password, nickname) values (?, ?, ?);";

        // 4. 创建 PreparedStatement
        PreparedStatement preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

        // 5. 占位符赋值
        preparedStatement.setObject(1, "test");
        preparedStatement.setObject(2, "test");
        preparedStatement.setObject(3, "测试员");

        // 6. 发送 SQL 语句,并且获取结果
        int rows = preparedStatement.executeUpdate();

        // 7. 结果解析
        if (rows > 0)
        {
            System.out.println("数据插入成功！");

            // 可以获取回显的主键
            // 获取 PrepareStatement 装主键的结果集对象, 一行一列, id = 值
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            generatedKeys.next();   // 移动下光标
            int id = generatedKeys.getInt(1);

            System.out.println("id = " + id);
        }
        else
        {
            System.out.println("数据插入失败！");
        }

        // 8. 关闭资源
        preparedStatement.close();
        connection.close();
    }

    /**
     * 使用普通的方式插入 10000 条数据
     */
    @Test
    public void testInsert() throws ClassNotFoundException, SQLException
    {
        // 1. 注册驱动
        Class.forName("com.mysql.cj.jdbc.Driver");

        // 2. 获取连接
        Connection connection = DriverManager.getConnection("jdbc:mysql:///my_jdbc?user=MYXH&password=520.ILY!");

        // 3. 编写 SQL 语句
        String sql = "insert into t_user(account, password, nickname) values (?, ?, ?);";

        // 4. 创建 PreparedStatement
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        // 5. 占位符赋值
        long startTime = System.currentTimeMillis();

        for (int i = 1; i <= 10000; i++)
        {
            preparedStatement.setObject(1, "test" + i);
            preparedStatement.setObject(2, "test" + i);
            preparedStatement.setObject(3, "测试员" + i);

            // 6. 发送 SQL 语句,并且获取结果
            preparedStatement.executeUpdate();
        }

        long endTime = System.currentTimeMillis();

        // 7. 结果解析: 执行 10000 次数据插入消耗的时间：15921ms
        System.out.println("执行 10000 次数据插入消耗的时间：" + (endTime - startTime) + "ms");

        // 8. 关闭资源
        preparedStatement.close();
        connection.close();
    }

    /**
     * 使用批量插入的方式插入 10000 条数据
     * TODO: 总结批量插入
     *     1. 路径后面添加 ?rewriteBatchedStatements=true, 允许批量插入
     *     2. insert into values 必须写, 语句不能添加;结束
     *     3. 不是执行语句每条，是批量添加 addBatch();
     *     4. 遍历添加完毕以后, 统一批量执行 executeBatch()
     */
    @Test
    public void testBatchInsert() throws ClassNotFoundException, SQLException
    {
        // 1. 注册驱动
        Class.forName("com.mysql.cj.jdbc.Driver");

        // 2. 获取连接
        Connection connection = DriverManager.getConnection("jdbc:mysql:///my_jdbc?rewriteBatchedStatements=true", "MYXH", "520.ILY!");

        // 3. 编写 SQL 语句
        String sql = "insert into t_user(account, password, nickname) values (?, ?, ?)";

        // 4. 创建 PreparedStatement
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        // 5. 占位符赋值
        long startTime = System.currentTimeMillis();

        for (int i = 1; i <= 10000; i++)
        {
            preparedStatement.setObject(1, "BatchTest" + i);
            preparedStatement.setObject(2, "BatchTest" + i);
            preparedStatement.setObject(3, "批量测试员" + i);

            preparedStatement.addBatch();   // 不执行, 追加到values后面
        }

        // 6. 发送 SQL 语句,并且获取结果
        preparedStatement.executeBatch();   // 执行批量操作

        long endTime = System.currentTimeMillis();

        // 7. 结果解析: 执行 10000 次数据插入消耗的时间：797ms
        System.out.println("执行 10000 次数据插入消耗的时间：" + (endTime - startTime) + "ms");

        // 8. 关闭资源
        preparedStatement.close();
        connection.close();
    }
}
