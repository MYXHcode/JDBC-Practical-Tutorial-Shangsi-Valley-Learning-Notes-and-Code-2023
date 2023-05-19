package com.myxh.api.druid;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.junit.Test;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author MYXH
 * @date 2023/5/14
 * Description: druid 连接池使用类
 */
public class DruidUsePart
{
    /**
     * 直接使用代码设置连接池连接参数方式
     * 1. 创建一个 druid 连接池对象
     * 2. 设置连接池参数(必须或非必须)
     * 3. 获取连接(通用方法, 所有连接池都一样)
     * 4. 回收连接(通用方法,所有连接池都一)
     */
    @Test
    public void testHardCoding() throws SQLException
    {
        // 连接池对象
        DruidDataSource druidDataSource = new DruidDataSource();

        // 设置参数
        // 必须参数: 连接数据库驱动类的全限定符, 注册驱动(url 或 user 或 password)
        druidDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");    // 帮助我们进行驱动注册和获取连接
        druidDataSource.setUrl("jdbc:mysql://localhost:3306/my_jdbc");
        druidDataSource.setUsername("MYXH");
        druidDataSource.setPassword("520.ILY!");

        // 非必须参数: 初始化连接数量, 最大的连接数量...
        druidDataSource.setInitialSize(5);    // 初始化的连接数量
        druidDataSource.setMaxActive(10);    // 最大的连接数量

        // 获取连接
        Connection connection = druidDataSource.getConnection();

        // 数据库 CURD

        // 回收连接
        connection.close();    // 连接池提供的连接, close()就是回收连接
    }

    /**
     * 通过读取外部配置文件的方法, 实例化 druid 连接池对象
     */
    @Test
    public void testSoftCoding() throws Exception
    {
        // 1. 读取外部配置的文件 Properties
        Properties properties = new Properties();

        // src 下的文件, 可以使用类加载器提供的方法
        InputStream inputStream = DruidUsePart.class.getClassLoader().getResourceAsStream("druid.properties");

        properties.load(inputStream);

        // 2. 使用连接池的工具类的工场模式, 创建连接池
        DataSource druidDataSource = DruidDataSourceFactory.createDataSource(properties);

        Connection connection = druidDataSource.getConnection();

        // 数据库 CURD

        connection.close();
    }
}
