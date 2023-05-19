package com.myxh.api.utils;

import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author MYXH
 * @date 2023/5/15
 * Description: 基于工具类的 CURD
 */
public class JdbcCrudPart
{
    @Test
    public void testInsert() throws SQLException
    {
        Connection connection = JdbcUtilsVersion2.getConnection();

        // 数据库 CURD 动作

        JdbcUtilsVersion2.freeConnection();
    }
}
