create table USER
(
  ID     int AUTO_INCREMENT
    primary key not null,
  ACCOUNTID    VARCHAR(100),
  NICKNAME     VARCHAR(50),
  TOKEN        CHAR(36),
  GMT_CREATE   BIGINT,
  GMT_MODIFIED BIGINT
);


