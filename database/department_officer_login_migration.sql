-- Run once after department_management_migration.sql in MySQL Workbench.
USE viswa_grid;
CREATE TABLE IF NOT EXISTS department_officers (
  officer_id INT NOT NULL AUTO_INCREMENT,
  username VARCHAR(50) NOT NULL,
  full_name VARCHAR(100) NOT NULL,
  password_hash VARCHAR(100) NOT NULL,
  department_id INT NOT NULL,
  is_active BOOLEAN NOT NULL DEFAULT TRUE,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (officer_id),
  UNIQUE KEY uk_department_officers_username (username),
  KEY idx_department_officers_department (department_id),
  CONSTRAINT fk_department_officers_department FOREIGN KEY (department_id)
    REFERENCES departments(department_id) ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
