CREATE
    DATABASE IF NOT EXISTS my_jdbc;

USE
    my_jdbc;

CREATE TABLE IF NOT EXISTS t_user
(
    id       INT PRIMARY KEY AUTO_INCREMENT COMMENT '用户主键',
    account  VARCHAR(20) NOT NULL UNIQUE COMMENT '账号',
    password VARCHAR(64) NOT NULL COMMENT '密码',
    nickname VARCHAR(20) NOT NULL COMMENT '昵称'
);

INSERT INTO t_user(account, password, nickname)
VALUES ('root', '123456', '经理'),
       ('admin', '000000', '管理员');

-- 查询需求: 查询全部用户数据

SELECT id, account, password, nickname
FROM t_user;

SELECT *
FROM t_user
WHERE account = 'root'
  AND password = '123456';

-- 继续在 my_jdbc 的库中创建银行表

CREATE TABLE t_bank
(
    id      INT PRIMARY KEY AUTO_INCREMENT COMMENT '账号主键',
    account VARCHAR(20) NOT NULL UNIQUE COMMENT '账号',
    money   DECIMAL(10, 2) UNSIGNED COMMENT '金额不能为负值！'
);

INSERT INTO t_bank(account, money)
VALUES ('经理', 10000),
       ('管理员', 9000);
