-- VISWA GRID: Department Management Migration
-- Run this script once against the existing viswa_grid database.
USE viswa_grid;

CREATE TABLE IF NOT EXISTS departments (
    department_id INT NOT NULL AUTO_INCREMENT,
    department_name VARCHAR(100) NOT NULL,
    description VARCHAR(500) DEFAULT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (department_id),
    UNIQUE KEY uk_departments_name (department_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Add department assignment to existing complaints.
-- If department_id already exists, skip this ALTER TABLE statement.
ALTER TABLE complaints
    ADD COLUMN department_id INT NULL,
    ADD CONSTRAINT fk_complaints_department
        FOREIGN KEY (department_id) REFERENCES departments(department_id)
        ON UPDATE CASCADE ON DELETE SET NULL;

-- Initial departments. INSERT IGNORE prevents duplicate names on reruns.
INSERT IGNORE INTO departments (department_name, description) VALUES
('Roads & Transportation', 'Road maintenance, potholes, footpaths and traffic-related civic infrastructure'),
('Water Supply', 'Water leakage, pipeline damage and public water supply issues'),
('Sanitation & Waste Management', 'Garbage collection, cleanliness and waste disposal'),
('Electricity & Street Lighting', 'Streetlights, public lighting and electrical infrastructure'),
('Drainage & Sewerage', 'Blocked drains, sewage overflow and stormwater drainage'),
('Parks & Public Spaces', 'Parks, public spaces and community infrastructure');
