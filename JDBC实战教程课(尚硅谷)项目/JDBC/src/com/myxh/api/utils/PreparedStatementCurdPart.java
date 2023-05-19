package com.myxh.api.utils;

import com.myxh.api.entity.User;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;


/**
 * @author MYXH
 * @date 2023/5/15
 * @Description 使用 PreparedStatement 进行 t_user 表 的 CURD 操作
 */
public class PreparedStatementCurdPart extends BaseDao
{
    // 测试方法需要导入 junit 的测试包
    @Test
    public void testInsert() throws SQLException
    {
        /*
        t_user 插入一条用户数据
            account: DaoTest1
            password: test1
            nickname: Dao测试员1
         */

        // 编写 SQL 语句结果, 动态值的部分使用 ? 代替
        String sql = "insert into t_user(account, password, nickname) values(?, ?, ?);";

        int rows = executeUpdate(sql, "DaoTest1", "test1", "Dao测试员1");
        System.out.println("rows = " + rows);
    }

    @Test
    public void testUpdate() throws SQLException
    {
        /*
        修改 id = 3 的用户 nickname = 新的测试员
         */

        // 编写 SQL 语句结果, 动态值的部分使用 ? 代替
        String sql = "update t_user set nickname = ? where id = ?;";

        int rows = executeUpdate(sql, "新的测试员", 3);
        System.out.println("rows = " + rows);
    }

    @Test
    public void testDelete() throws SQLException
    {
        /*
        删除 id = 20004 的用户数据
         */

        // 编写 SQL 语句结果, 动态值的部分使用 ? 代替
        String sql = "delete from t_user where id = ?;";

        int rows = executeUpdate(sql, 20004);
        System.out.println("rows = " + rows);
    }

    @Test
    public void testSelect() throws SQLException, NoSuchFieldException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException
    {
        /*
        目标: 查询所有用户数据
         */

        // 编写 SQL 语句结果, 动态值的部分使用 ? 代替
        String sql = "select id, account, password, nickname from t_user;";

        List<User> list = executeQuery(User.class, sql);
        System.out.println("list = " + list);
    }
}
