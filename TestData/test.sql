USE master;
GO
IF EXISTS(SELECT * FROM sysdatabases WHERE name= 'name')
BEGIN
RAISERROR('Dropping existing name database ....',0,1)
DROP database name;
END
GO
RAISERROR('Creating name database ....',0,1)
GO
CREATE DATABASE name;
GO
USE name;
GO
RAISERROR('Creating Tables ....',0,1)
GO
CREATE TABLE name (id INTEGER not NULL, c1 BOOLEAN, stwwring VARCHAR(14), cqwhar VARCHAR(1), iqwnt INTEGER(6), dqwouble DECIMAL(6,2), daqwte DATE, lqwong BIGINT, fqwakebool VARCHAR(7), faqwkedate VARCHAR(11),  PRIMARY KEY (id));