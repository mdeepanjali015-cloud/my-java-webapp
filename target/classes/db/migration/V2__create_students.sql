-- SQL Server migration to create students table
CREATE TABLE students (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    first_name NVARCHAR(100) NOT NULL,
    last_name NVARCHAR(100) NOT NULL,
    email NVARCHAR(255) NOT NULL UNIQUE
);
