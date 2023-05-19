CREATE
    DATABASE IF NOT EXISTS my_jdbc_customer_manage_system;

USE
    my_jdbc_customer_manage_system;

-- 员工表

CREATE TABLE t_customer
(
    id     INT PRIMARY KEY AUTO_INCREMENT COMMENT '客户主键',
    `name`   VARCHAR(20) COMMENT '客户名称',
    gender VARCHAR(4) COMMENT '客户性别',
    age    INT COMMENT '客户年龄',
    salary DECIMAL(8, 2) COMMENT '客户工资',
    phone  VARCHAR(11) COMMENT '客户电话'
)
