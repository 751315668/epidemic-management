package com.epidemicmanagement;

import com.alibaba.druid.pool.DruidDataSource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author XUE
 */
@SpringBootTest
public class DruidTest {

    @Resource
    private DataSource dataSource;

    @Test
    public void druidLoad() throws SQLException {
        // 默认数据源
        System.out.println("数据源" + dataSource.getClass());
        // 获得连接
        Connection connection = dataSource.getConnection();
        System.out.println("connection" + connection);

        DruidDataSource druidDataSource = (DruidDataSource) dataSource;
        System.out.println("druidDataSource 数据源最大连接数：" + druidDataSource.getMaxActive());
        System.out.println("druidDataSource 数据源初始化连接数：" + druidDataSource.getInitialSize());

        //关闭连接
        connection.close();
    }

}
