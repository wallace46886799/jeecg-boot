DROP TABLE IF EXISTS dream_user;
CREATE TABLE dream_user (
  id varchar(32) NOT NULL COMMENT '主键ID',
  name varchar(30) default NULL COMMENT '姓名',
  sex varchar(2) default NULL COMMENT '性别',
  age int(11) default NULL COMMENT '年龄',
  birthday date default NULL COMMENT '生日',
  id_card_num varchar(50) default NULL COMMENT '身份证号',
  phone varchar(50) default NULL COMMENT '手机号',
  email varchar(50) default NULL COMMENT '邮箱',
  content varchar(1000) default NULL COMMENT '个人简介',
  PRIMARY KEY  (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS dream_user_param;
CREATE TABLE dream_user_param (
  id varchar(32) NOT NULL COMMENT '主键ID',
  user_id varchar(32) default NULL COMMENT '用户编号',
  prefix_name varchar(200) default NULL COMMENT '参数前缀',
  surfix_key varchar(200) default NULL COMMENT '参数后缀',
  param_value varchar(1000) default NULL COMMENT '参数值',
  PRIMARY KEY  (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS dream_securities;
CREATE TABLE dream_securities (
  id varchar(32) NOT NULL COMMENT '主键ID',
  name varchar(30) default NULL COMMENT '证券公司名称',
  prefix_name varchar(30) default NULL COMMENT '证券公司前缀',
  content varchar(1000) default NULL COMMENT '证券公司简介',
  PRIMARY KEY  (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS dream_securities_param;
CREATE TABLE dream_securities_param (
  id varchar(32) NOT NULL COMMENT '主键ID',
  securities_id varchar(32) default NULL COMMENT '证券公司编号',
  prefix_name varchar(200) default NULL COMMENT '参数前缀',
  surfix_key varchar(200) default NULL COMMENT '参数后缀',
  param_value varchar(1000) default NULL COMMENT '参数值',
  PRIMARY KEY  (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS dream_securities_account;
CREATE TABLE dream_securities_account (
  id varchar(32) NOT NULL COMMENT '主键ID',
  account_name  varchar(1000) default NULL COMMENT '账户名称',
  prefix_name varchar(30) default NULL COMMENT '账户前缀',
  capital_account varchar(1000) default NULL COMMENT '资金账户',
  sha_account varchar(1000) default NULL COMMENT '沪A账户',
  sza_account varchar(1000) default NULL COMMENT '深A账户',
  password varchar(1000) default NULL COMMENT '密码',
  user_id varchar(32) NOT NULL  COMMENT '用户编号',
  securities_id varchar(32) NOT NULL  COMMENT '证券公司编号',
  account_desc  varchar(1000) default NULL COMMENT '账户描述',
  PRIMARY KEY  (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS dream_securities_account_param;
CREATE TABLE dream_securities_account_param (
  id varchar(32) NOT NULL COMMENT '主键ID',
  securities_account_id varchar(32) default NULL COMMENT '账户编号',
  prefix_name varchar(200) default NULL COMMENT '参数前缀',
  surfix_key varchar(200) default NULL COMMENT '参数后缀',
  param_value varchar(1000) default NULL COMMENT '参数值',
  PRIMARY KEY  (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS dream_ipo_calendar;
CREATE TABLE dream_ipo_calendar (
  id varchar(32) NOT NULL COMMENT '主键ID',
  ipo_date date default NULL COMMENT '发行日期',
  market_type varchar(200) default NULL COMMENT '发行市场',
  stock_id varchar(32) default NULL COMMENT '新股编号',
  stock_name varchar(200) default NULL COMMENT '新股名称',
  stock_limit varchar(1000) default NULL COMMENT '认购限额',
  stock_desc varchar(1000) default NULL COMMENT '新股说明',
  PRIMARY KEY  (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS dream_ipo_log;
CREATE TABLE dream_ipo_log (
  id varchar(32) NOT NULL COMMENT '主键ID',
  securities_account_id varchar(32) default NULL COMMENT '账户编号',
  ipo_date date default NULL COMMENT '发行日期',
  market_type varchar(200) default NULL COMMENT '发行市场',
  stock_id varchar(32) default NULL COMMENT '新股编号',
  stock_name varchar(200) default NULL COMMENT '新股名称',
  stock_share varchar(1000) default NULL COMMENT '认购额度',
  apply_time varchar(1000) default NULL COMMENT '认购时间',
  apply_message varchar(1000) default NULL COMMENT '认购消息',
  status varchar(1000) default NULL COMMENT '认购状态',
  PRIMARY KEY  (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;