package com.myxh.api.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author MYXH
 * @date 2023/5/15
 * Description: 封装 DAO 数据库重复代码
 * TODO:
 *     封装两个方法, 一个简化非 DQL
 *                 一个简化 DQL
 */
public abstract class BaseDao
{
    /**
     * 封装简化非 DQL 语句
     *
     * @param sql    带占位符的 SQL 语句
     * @param params 占位符的值    注意: 传入占位符的值, 必等于 SQL 语句 ? 位置
     * @return 执行影响的行数
     */
    public int executeUpdate(String sql, Object... params) throws SQLException
    {
        // 获取连接
        Connection connection = JdbcUtilsVersion2.getConnection();

        // 创建 PreparedStatement, 并且传入 SQL 语句结果
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
        // 开启事务后, 不要管连接, 业务层处理, connection.setAutoCommit(false);
        if (connection.getAutoCommit())
        {
            // 没有开启事务, 正常回收连接
            JdbcUtilsVersion2.freeConnection();
        }

        return rows;
    }

    /*
      非 DQL 语句封装方法 -> 返回值, 固定为 int0

      DQL 语句封装方法 -> 返回值是什么类型?    List<T>

                        并不是 List<Map> Map 中的 key 和 value自定义, 不用先设定好
                                        Map 没有数据校验机制
                                        Map 不支持反射操作

                        数据库数据 -> java的实体类

                        table
                            t_user
                                id
                                account
                                password
                                nickname

                         Java
                             User
                                 id
                                 account
                                 password
                                 nickname

                         表中一行数据 -> Java类的一个对象 -> 多行 -> List<Java实体类>list;

      <T>声明一个泛型，不确定类型
          1. 确定泛型 User.class T = User
          2. 要使用反射技术属性赋值

      public <T> List<T> executeQuery(class<T> clazz,String sql,Object...params);
     */

    /**
     * 将查询结果封装到一个实体类集合
     *
     * @param clazz  要接受返回值的实体类集合的模板对象
     * @param sql    查询语句, 要求列名或者别名等于实体类的属性名 u_id as uId -> uId
     * @param params 占位符的值,要和 ? 位置对象传递
     * @param <T>    声明的结果的类型
     * @return 查询的实体类集合
     */
    public <T> List<T> executeQuery(Class<T> clazz, String sql, Object... params) throws SQLException, InstantiationException, IllegalAccessException, NoSuchFieldException, NoSuchMethodException, InvocationTargetException
    {
        // 获取连接
        Connection connection = JdbcUtilsVersion2.getConnection();

        // 创建 PreparedStatement, 并且传入 SQL 语句结果
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        //  占位符赋值
        if (params != null && params.length != 0)
        {
            for (int i = 1; i <= params.length; i++)
            {
                preparedStatement.setObject(i, params[i - 1]);
            }
        }

        // 发送 SQL 语句
        ResultSet resultSet = preparedStatement.executeQuery();

        // 结果集解析
        List<T> list = new ArrayList<>();

        // 获取列的信息对象
        ResultSetMetaData metaData = resultSet.getMetaData();

        // 列名数组, 用于存储查询结果集中所有列名
        String[] columnNames = new String[metaData.getColumnCount()];

        // 获取列名或别名, 并将其转换成对应的实体类属性名
        for (int i = 1; i <= columnNames.length; i++)
        {
            String columnName = metaData.getColumnLabel(i);
            Field field;

            try
            {
                field = clazz.getDeclaredField(columnName);
            }
            catch (NoSuchFieldException e)
            {
                // 如果找不到同名属性, 则尝试将列名转换成驼峰命名法格式的属性名
                String propertyName = StringUtils.toCamelCase(columnName);
                field = clazz.getDeclaredField(propertyName);
            }

            columnNames[i - 1] = field.getName();
        }

        while (resultSet.next())
        {
            // 一行数据对应一个 T 类型的对象
            T t = clazz.getDeclaredConstructor().newInstance();    // 调用类的无参构造函数实例化对象

            // 自动遍历列: 注意, 要从 1 开始, 并且小于等于总列数
            for (int i = 1; i <= columnNames.length; i++)
            {
                // 对象的属性值
                Object value = resultSet.getObject(i);

                // 获取属性名称
                String propertyName = metaData.getColumnLabel(i);

                // 反射给对象的属性值赋值
                Field field;

                try
                {
                    field = clazz.getDeclaredField(propertyName);
                }
                catch (NoSuchFieldException e)
                {
                    // 如果找不到同名属性, 则尝试将属性名转换成下划线分隔符格式的列名
                    String columnName = StringUtils.toUnderscoreCase(propertyName);
                    field = clazz.getDeclaredField(columnName);
                }

                field.setAccessible(true);    // 属性可以设置, 取消 private 的修饰限制

                /*
                参数1: 要赋值的对象, 如果属性是静态, 第一个参数可以为null
                参数2: 具体的属性值
                 */
                field.set(t, value);
            }

            // 一行数据的所有列全部存到了 T 中
            //将 T 存储到集合中即可
            list.add(t);
        }

        // 关闭资源
        resultSet.close();
        preparedStatement.close();

        if (connection.getAutoCommit())
        {
            // 没有事务, 可以关闭
            JdbcUtilsVersion2.freeConnection();
        }

        return list;
    }

    public static class StringUtils
    {
        /**
         * 将字符串转换成驼峰命名法格式的字符串（首字母小写）
         *
         * @param str 原始字符串
         * @return 转换后的字符串
         */
        public static String toCamelCase(String str)
        {
            StringBuilder sb = new StringBuilder();
            boolean isUpperCase = false;

            for (int i = 0; i < str.length(); i++)
            {
                char c = str.charAt(i);

                if (c == '_')
                {
                    isUpperCase = true;
                }
                else if (isUpperCase)
                {
                    sb.append(Character.toUpperCase(c));
                    isUpperCase = false;
                }
                else if (i == 0)
                {
                    sb.append(Character.toLowerCase(c));
                }
                else
                {
                    sb.append(c);
                }
            }

            return sb.toString();
        }

        /**
         * 将字符串转换成下划线分隔符格式的字符串
         *
         * @param str 原始字符串
         * @return 转换后的字符串
         */
        public static String toUnderscoreCase(String str)
        {
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < str.length(); i++)
            {
                char c = str.charAt(i);

                if (Character.isUpperCase(c))
                {
                    sb.append('_').append(Character.toLowerCase(c));
                }
                else
                {
                    sb.append(c);
                }
            }

            return sb.toString();
        }
    }
}
