package com.myxh.api.statement;

import java.sql.*;
import java.util.Scanner;

/**
 * @author MYXH
 * @date 2023/5/10
 * @Description 输入账号密码, 模拟用户登录
 * TODO:
 *     1. 明确 JDBC 的使用流程和详细讲解内部设计 API 方法
 *     2. 发现问题, 引出 PreparedStatement
 * TODO:
 *     输入账号和密码
 *     进行数据库信息查询(t_user)
 *     反馈登录成功还是登录失败
 * TODO:
 *     1. 键盘输入事件, 收集账号和密码信悬
 *     2. 注册驱动
 *     3. 获取连接
 *     4. 创建 Statement
 *     5. 发送查询 SQL 语句, 并获取返回结果
 *     6. 结果判断, 显示登录成功还是失败
 *     7. 关闭资源
 */
public class StatementUserLoginPart
{
    public static void main(String[] args) throws SQLException, ClassNotFoundException
    {
        // 1. 获取用户输入信息
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入账号：");
        String account1 = scanner.nextLine();
        System.out.println("请输入密码：");
        String password1 = scanner.nextLine();

        // 2. 注册驱动
        /*
        方案 1:
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver())
            注意: 8+ com.mysql.cj.jdbc.Driver
                  5+ com.mysql.jdbc.Driver
            问题: 注册两次驱动
                  1. DriverManager.registerDriver方法本身会注册一次
                  2. Driver.static{ DriverManager.registerDriver() }静态代码块, 也会注册一次
            解决: 只想注册一次驱动
                  只触发静态代码块即可, Driver
            触发静态代码块:
                类加载机制: 类加载的时刻,会触发静态代码块
                          加载[class 文件 -> JVM 虚拟机的 class 对象]
                          连接[验证(检查文件类型) -> 准备(静态变量默认值) -> 解析(触发静态代码块)]
                          初始化(静态属性赋真实值)
                触发类加载:
                    1. new关键字
                    2. 调用静态方法
                    3. 调用静态属性
                    4. 接口 jdk1.8 default 默认实现
                    5. 反射
                    6. 子类触发父类
                    7. 程序的入口 main
         */

        // 方案 1:
        // DriverManager.registerDriver(new Driver());

        // 方案 2: 代码扩展性差, 升级 MySQL 新版本的驱动或换成 Oracle
        // new Driver();

        // 方案 3: 反射, 字符串的 Driver 全限定符可以引导外部的配置文件 -> xx.properties -> Oracle -> 配置文件修改
        Class.forName("com.mysql.cj.jdbc.Driver");

        // 3. 获取数据库连接
        /*
        getConnection(1,2,3)方法, 是一个重载方法
        允许开发者, 用不同的形式传入数据库连接的核心参数

        核心属性:
            1. 数据库软件所在的主机的 ip 地址: localhost 或 127.0.0.1
            2. 数据库软件所在的主机的端口号: 3306
            3. 连接的具体库: my_jdbc
            4. 连接的账号∶ MYXH
            5. 连接的密码: 520.ILY!
            6. 可选的信息

        3 个参数:
            String url    数据库软件所在的信息, 连接的具体库, 以及其他可选信息
                          语法: jdbc:数据库管理软件名称[mysql 或 oracle]://ip地址 或 主机名:port端口号/数据库名?key=value
                                &key=value 可选信息
                          具体: jdbc:mysql://127.0.0.1:3306/my_jdbc
                                jdbc:mysql://localhost:3306/my_jdbc
                          本机的省略写法: 如果你的数据库软件安装到本机, 可以进行一些省略
                               jdbc:mysql://127.0.0.1:3306/my_jdbc -> jdbc:mysql:///my_jdbc
                               省略了[本机地址]和[3306 默认端口号]
                               强调: 必须是本机, 并且端口号使用 3306 才可省略, 用///

            String user        数据库的账号 MYXH
            String password    数据库的密码 520.ILY!

        2 个参数:
            String url     : 此 url 和 3 个参数的 url 的作用一样, 数据库 ip,端口号,具体的数据库和可选信息
            Properties info: 存储账号和密码
                             Properties 类似于 Map, 只不过 key = value 都是字符串形式

        1 个参数:
            String url      : 数据库 ip, 端口号, 具体的数据库, 可选信息(账号密码)
                              jdbc:数据库软件名://ip:port/数据库?key=value&key=value&key=value

                              jdbc:mysql://localhost:3306/my_jdbc?user=MYXH&password=520.ILY!
                              携带固定的参数名 user password 传递账号和密码信息

        url 的路径属性可选信息:
            url?user=账号&password=密码

            8.0.31 版本驱动，下面都是一些可选属性:
                8.0.25 以后，自动识别时区, serverTimezone=Asia/Shanghai 不用添加, 8.0.25 之前版本，还是要加的
                8 版本以后,默认使用的就是 utf-8 格式, useUnicode=true&characterEncoding=utf8&useSSL=true 都可以省略

            serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf8&usesSL=true
         */

        Connection connection = DriverManager.getConnection("jdbc:mysql:///my_jdbc", "MYXH", "520.ILY!");

        // Properties info = new Properties();
        // info.put("user", "MYXH");
        // info.put("password", "520.ILY!");
        // Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/my_jdbc", info);

        // Connection connection = DriverManager.getConnection("jdbc:mysql:///my_jdbc?user=MYXH&password=520.ILY!");

        // 4. 创建发送 SQL 语句的 statement 对象
        // statement 可以发送 SQL 语句到数据库, 并且获取返回结果
        Statement statement = connection.createStatement();

        // 5. 发送 SQL 语句(编写 SQL 语句, 发送 SQL 语句)
        /*
        请输入账号：
        hacker
        请输入密码：
        ' or '1' = '1
        登录成功！
         */
        String sql = "SELECT * FROM t_user WHERE account = '"+account1+"' AND password = '"+password1+"';";

        /*
        SQL 分类: DDL(容器创建，修改,剧除) DML(插入，修改,剧除) DQL(查询) DCL(权限控制) TPL(事务控制语言)

        参数: sql 非 DQL
        返回: int
            情况1: DML 返回影响的行数, 例如: 删除了三条数据 return 3; 插入了两条 return 2; 修改了 0 条 return 0;
            情况2: 非 DML return 0;
        int row = executeUpdate(sql)

        参数: sql DQL
        返回: resultSet 结果封装对象
        ResultSet resultSet = executeQuery(sql);
         */

        // int i = statement.executeUpdate(sql);
        ResultSet resultSet = statement.executeQuery(sql);

        // 6. 查询结果集解析 ResultSet
        /*
        Java 是一种面向对象的思维，将查询结果封装成了 resultSet 对象，我们应该理解，内部一定也是有行和有列的

        resultSet -> 逐行获取数据, 行 -> 行的列的数据

        A ResultSet object maintains a cursor pointing to its current row of data.
        Initially the cursor is positioned before the first row. The next method moves the cursor to the next row,
        and because it returns false when there are no more rows in the ResultSet object,
        it can be used in a while loop to iterate through the result set.
        ResultSet 对象维护一个指向其当前数据行的光标。最初，光标位于第一行之前。next方法将光标移到下一行，因为当ResultSet对象中没有更多行时，
        它会返回false，所以可以在while循环中使用它来迭代结果集。

        想要进行数据解析, 我们需要进行两件事情: 1. 移动游标指定获取数据行 2. 获取指定数据行的列数据即可

        1. 游标移动问题
            resultSet 内部包含一个游标, 指定当前行数据
            默认游标指定的是第一行数据之前
            我们可以调用next方法向后移动一行游标
            如果我们有很多行数据, 我们可以使用 while(next){获取每一行的数据}

            boolean = next()    true: 有更多行数据,并且向下移动一行
                                false: 没有更多行数据,不移动

            TODO: 移动光标的方法有很多, 只需要记 next 即可, 配合 while 循环获取全部数据

        2. 获取列的数据问题(获取光标指定的行的列的数据)

            resultSet.get 类型(String columnLabel 或 int columnIndex);
                columnLabel: 列名, 如果有别名写别名    select * 或 (id, account, password, nickname)
                                                    select id as aid, account as ac from
                columnIndex: 列的下角标获取从左向右从 1 开始
         */

        // while (resultSet.next())
        // {
        //     int id = resultSet.getInt(1);
        //     String account2 = resultSet.getString("account");
        //     String password2 = resultSet.getString(3);
        //     String nickname = resultSet.getString("nickname");
        //     System.out.println(id + "--" + account2 + "--" + password2 + "--" + nickname);
        // }

        // 移动一次光标, 只要有数据, 就代表登录成功
        if (resultSet.next())
        {
            System.out.println("登录成功！");
        }
        else
        {
            System.out.println("登录失败！");
        }

        // 7. 关闭数据
        resultSet.close();
        statement.close();
        connection.close();
    }
}
