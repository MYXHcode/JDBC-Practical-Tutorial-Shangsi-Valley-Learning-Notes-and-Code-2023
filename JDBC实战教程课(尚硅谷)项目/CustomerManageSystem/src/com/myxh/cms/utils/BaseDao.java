package com.myxh.cms.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author MYXH
 * @date 2023/5/16
 * @Description: 封装 DAO 数据库重复代码
 * TODO:
 *    封装两个方法: 一个简化非 DQL
 *                一个简化 DQL
 */
public abstract class BaseDao
{
    /**
     * 封装简化非 DQL 语句
     * @param sql    带占位符的 SQL 语句
     * @param params 占位符的值, 注意: 传入占位符的值, 必须等于 SQL 语句 ? 位置
     * @return 执行影响的行数
     */
    public int executeUpdate(String sql,Object... params) throws SQLException
    {
        // 获取连接
        Connection connection = JdbcUtils.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        // 占位符赋值
        // 可变参数可以当做数组使用
        for (int i = 1; i <= params.length; i++)
        {
            preparedStatement.setObject(i, params[i - 1]);
        }

        // 发送 SQL 语句
        // DML 类型
        int rows = preparedStatement.executeUpdate();

        preparedStatement.close();

        // 是否回收连接, 需要考虑是不是事务
        if (connection.getAutoCommit())
        {
            // 没有开启事务
            // 没有开启事务, 正常回收连接
            JdbcUtils.freeConnection();
        }

        // connection.setAutoCommit(false); //开启事务后, 不要管连接, 业务层处理

        return rows;
    }


    /*
      非 DQL 语句封装方法 -> 返回值 固定为int

      DQL 语句封装方法 -> 返回值 是什么类型? List<T>
                        并不是list<Map> Map key 和 value自定义, 不用先设定好
                                       Map 没有数据校验机制
                                       Map 不支持反射操作

                        数据库数据 -> Java的实体类

                        table
                            t_user
                                id
                                account
                                password
                                nickname

                        java
                            User
                                id
                                account
                                password
                                nickname

         表中 -> 一行数据 -> Java类的一个对象 -> 多行数据 -> List<Java实体类> list;

         DQL -> List<Map> -> 一行数据 -> Map -> List<Map>

         <T> 声明一个泛型, 不确定类型
             1. 确定泛型 User.class T = User
             2. 要使用反射技术属性赋值
         public <T> List<T> executeQuery(Class<T> clazz,String sql,Object... params);
     */

    /**
     * 将查询结果封装到一个实体类集合
     * @param clazz  要接值的实体类集合的模板对象
     * @param sql    查询语句, 要求列名或者别名等于实体类的属性名, u_id as uId -> uId
     * @param params 占位符的值, 要和 ? 位置对象传递
     * @return       查询的实体类集合
     * @param <T>    声明的结果的类型
     */
    public <T> List<T> executeQuery(Class<T> clazz, String sql,Object... params) throws SQLException, InstantiationException, IllegalAccessException, NoSuchFieldException, NoSuchMethodException, InvocationTargetException
    {
        // 获取连接
        Connection connection = JdbcUtils.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        // 占位符赋值
        if (params != null && params.length != 0)
        {
            for (int i = 1; i <= params.length; i++)
            {
                preparedStatement.setObject(i,params[i - 1]);
            }
        }

        // 发送 SQL 语句
        ResultSet resultSet = preparedStatement.executeQuery();

        // 结果集解析
        List<T> list = new ArrayList<>();

        // 获取列的信息对象
        // TODO: metaData 装的当前结果集列的信息对象, 可以获取列的名称根据下角标, 可以获取列的数量
        ResultSetMetaData metaData = resultSet.getMetaData();

        //可以水平遍历列
        int columnCount = metaData.getColumnCount();

        while (resultSet.next())
        {
            T t = clazz.getDeclaredConstructor().newInstance();    // 调用类的无参构造函数实例化对象

            // 自动遍历列, 注意: 要从 1 开始, 并且小于等于总列数
            for (int i = 1; i <= columnCount; i++)
            {
                // 对象的属性值
                Object value = resultSet.getObject(i);

                // 获取指定列下角标的列的名称, ResultSetMetaData()
                String propertyName = metaData.getColumnLabel(i);

                //反射, 给对象的属性值赋值
                Field field = clazz.getDeclaredField(propertyName);
                field.setAccessible(true);    // 属性可以设置, 取消 private 的修饰限制

                /*
                  参数1: 要要赋值的对象, 如果属性是静态, 第一个参数, 可以为null
                  参数2: 具体的属性值
                 */
                field.set(t,value);
            }

            // 一行数据的所有列全部存到了 map 中
            // 将 map 存储到集合中即可
            list.add(t);
        }

        // 关闭资源
        resultSet.close();
        preparedStatement.close();

        if (connection.getAutoCommit())
        {
            // 没有事务, 可以关闭
            JdbcUtils.freeConnection();
        }

        return list;
    }
}
