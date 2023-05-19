package com.myxh.api.preparedstatement;

import org.junit.Test;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author MYXH
 * @date 2023/5/11
 * @Description 使用 PreparedStatement 进行 t_user 表 的 CURD 操作
 */
public class PreparedStatementCurdPart
{
    // 测试方法需要导入 junit 的测试包
    @Test
    public void testInsert() throws ClassNotFoundException, SQLException
    {
        /*
        t_user 插入一条用户数据
            account: test
            password: test
            nickname: 测试
         */

        // 1. 注册驱动
        Class.forName("com.mysql.cj.jdbc.Driver");

        // 2. 获取连接
        Connection connection = DriverManager.getConnection("jdbc:mysql:///my_jdbc", "MYXH", "520.ILY!");

        // 3. 编写 SQL 语句结果, 动态值的部分使用 ? 代替
        String sql = "insert into t_user(account, password, nickname) values(?, ?, ?);";

        // 4. 创建 PreparedStatement, 并且传入 SQL 语句结果
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        // 5. 占位符赋值
        preparedStatement.setObject(1, "test");
        preparedStatement.setObject(2, "test");
        preparedStatement.setObject(3, "测试");

        // 6. 发送 SQL 语句
        // DML 类型
        int rows = preparedStatement.executeUpdate();

        // 7. 输出结果
        if (rows > 0)
        {
            System.out.println("数据插入成功！");
        }
        else
        {
            System.out.println("数据插入失败！");
        }

        // 8. 关闭资源
        preparedStatement.close();
        connection.close();
    }

    @Test
    public void testUpdate() throws ClassNotFoundException, SQLException
    {
        /*
        修改 id = 3 的用户 nickname = 测试员
         */

        // 1. 注册驱动
        Class.forName("com.mysql.cj.jdbc.Driver");

        // 2. 获取连接
        Connection connection = DriverManager.getConnection("jdbc:mysql:///my_jdbc", "MYXH", "520.ILY!");

        // 3. 编写 SQL 语句结果, 动态值的部分使用 ? 代替
        String sql = "update t_user set nickname = ? where id = ?;";

        // 4. 创建 PreparedStatement, 并且传入 SQL 语句结果
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        // 5. 占位符赋值
        preparedStatement.setObject(1, "测试员");
        preparedStatement.setObject(2, 3);

        // 6. 发送 SQL 语句
        int rows = preparedStatement.executeUpdate();

        // 7. 输出结果
        if (rows > 0)
        {
            System.out.println("修改成功！");
        }
        else
        {
            System.out.println("修改失败！");
        }

        // 8. 关闭资源
        preparedStatement.close();
        connection.close();
    }

    @Test
    public void testDelete() throws ClassNotFoundException, SQLException
    {
        /*
        删除 id = 3 的用户数据
         */

        // 1. 注册驱动
        Class.forName("com.mysql.cj.jdbc.Driver");

        // 2. 获取连接
        Connection connection = DriverManager.getConnection("jdbc:mysql:///my_jdbc", "MYXH", "520.ILY!");

        // 3. 编写 SQL 语句结果, 动态值的部分使用 ? 代替
        String sql = "delete from t_user where id = ?;";

        // 4. 创建 PreparedStatement, 并且传入 SQL 语句结果
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        // 5. 占位符赋值
        preparedStatement.setObject(1, 3);

        // 6. 发送 SQL 语句
        int rows = preparedStatement.executeUpdate();

        // 7. 输出结果
        if (rows > 0)
        {
            System.out.println("数据删除成功！");
        }
        else
        {
            System.out.println("数据删除失败！");
        }

        // 8. 关闭资源
        preparedStatement.close();
        connection.close();
    }

    @Test
    public void testSelect() throws ClassNotFoundException, SQLException
    {
        /*
        目标: 查询所有用户数据, 并且封装到一个 List<Map> list 集合中

        解释:
            行    id account password nickname
            行    id account password nickname
            行    id account password nickname

        数据库 -> resultSet -> java -> 一行 -> Map(key=列名, value=列的内容) -> List<Map> list

        实现思路:
            遍历行数据, 一行对应一个 Map, 获取一行的列名和对应的列的属性, 装配即可
            将 Map 装到一个集合就可以了

        难点:
            如何获取列的名称?
         */

        // 1. 注册驱动
        Class.forName("com.mysql.cj.jdbc.Driver");

        // 2. 获取连接
        Connection connection = DriverManager.getConnection("jdbc:mysql:///my_jdbc", "MYXH", "520.ILY!");

        // 3. 编写 SQL 语句结果, 动态值的部分使用 ? 代替
        String sql = "select id, account as ac, password, nickname from t_user;";

        // 4. 创建 PreparedStatement, 并且传入 SQL 语句结果
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        // 5. 占位符赋值
        // 省略占位符赋值

        // 6. 发送 SQL 语句
        ResultSet resultSet = preparedStatement.executeQuery();

        // 7. 结果集解析
        /*
        TODO: 回顾
            resultSet: 有行和有列, 获取数据的时候, 一行一行数据
                       内部有一个游标, 默认指向数据的第一行之前
                       我们可以利用 next()方法移动游标, 指向数据行
                       获取行中的列的数据
         */
        List<Map<String, Object>> list = new ArrayList<>();

        // 获取列的信息对象
        //TODO: metaData 装的当前结果集列的信息对象(可以获取列的名称根据下角标,可以获取列的数量)
        ResultSetMetaData metaData = resultSet.getMetaData();

        // 可以水平遍历列
        int columnCount = metaData.getColumnCount();

        while (resultSet.next())
        {
            Map<String, Object> map = new HashMap<>();
            // 一行数据对应一个 Map

            // 手动取值
            // map.put("id", resultSet.getInt("id"));
            // map.put("account", resultSet.getString("account"));
            // map.put("password", resultSet.getString("password"));
            // map.put("nickname", resultSet.getString("nickname"));

            // 自动遍历列: 注意, 要从 1 开始, 并且小于等于总列数
            for (int i = 1; i <= columnCount; i++)
            {
                // 获取指定列下角标的值, ResultSet
                Object value = resultSet.getObject(i);

                // 获取指定列下角标的列的名称, ResultSetMetaData
                // select * [列名] 或 xxx as name
                // getColumnLabel: 会获取别名, 如果没有写别名才是列的名称, 不要使用getColumnName: 只会获取列的名称
                String columnLabel = metaData.getColumnLabel(i);

                map.put(columnLabel, value);
            }

            // 一行数据的所有列全部存到了 Map 中
            //将 Map 存储到集合中即可
            list.add(map);
        }

        System.out.println("list = " + list);

        // 8. 关闭资源
        resultSet.close();
        preparedStatement.close();
        connection.close();
    }
}
