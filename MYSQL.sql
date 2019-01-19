CREATE DATABASE IF NOT EXISTS employee;

USE employee;

--Table to capture in time--

CREATE TABLE employee_Tracker_In(
inc_sk INT PRIMARY KEY AUTO_INCREMENT,
ID INT,
IN_TIME TIMESTAMP,
LOC VARCHAR(20),
REG_CODE INT,
COUNTRY_CODE INT)

--Table to capture out time --

CREATE TABLE employee_Tracker_Out(
inc_sk INT PRIMARY KEY AUTO_INCREMENT,
ID INT,
OUT_TIME TIMESTAMP,
LOC VARCHAR(20),
REG_CODE INT,
RWG_CODE INT,
COUNTRY_CODE INT)

--Table to store roster--

CREATE TABLE eRoster(
ID INT,
SSTART TIMESTAMP,
SEND TIMESTAMP,
YEAR INT,
MONTH INT,
REG_CODE INT,
COUNTRY_CODE INT,
updated_sk TIMESTAMP PRIMARY KEY DEFAULT CURRENT_TIMESTAMP)

--Country code lookup table---

CREATE TABLE country_code(
ccode INT PRIMARY KEY,
country VARCHAR(30;

--Region code---

CREATE TABLE rgn_code(
rcode INT PRIMARY KEY,
region varchar(30));

CREATE TABLE parameter_roster(
line_num INT PRIMARY KEY AUTO_INCREMENT,
parameter_name VARCHAR(30) NOT NULL,
parameter_value VARCHAR(20) NOT NULL)




