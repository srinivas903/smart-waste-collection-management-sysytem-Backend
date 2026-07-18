-- Reference schema — Hibernate (spring.jpa.hibernate.ddl-auto=update)
-- will create/update these tables automatically on startup.
-- This file is provided for reference / manual setup only.

CREATE DATABASE IF NOT EXISTS swcms_db;
USE swcms_db;

CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(50),
    address VARCHAR(500),
    role ENUM('CITIZEN','STAFF','ADMIN') NOT NULL,
    created_at DATETIME
);

CREATE TABLE IF NOT EXISTS vehicles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    vehicle_number VARCHAR(50) NOT NULL UNIQUE,
    driver_name VARCHAR(255) NOT NULL,
    driver_phone VARCHAR(50),
    capacity_in_tons DOUBLE,
    status ENUM('AVAILABLE','ON_DUTY','MAINTENANCE') NOT NULL
);

CREATE TABLE IF NOT EXISTS pickup_requests (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    citizen_id BIGINT NOT NULL,
    pickup_address VARCHAR(500) NOT NULL,
    waste_type ENUM('ORGANIC','RECYCLABLE','HAZARDOUS','E_WASTE','GENERAL') NOT NULL,
    preferred_date DATE NOT NULL,
    notes VARCHAR(1000),
    status ENUM('PENDING','ASSIGNED','COLLECTED','CANCELLED') NOT NULL,
    created_at DATETIME,
    FOREIGN KEY (citizen_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS vehicle_assignments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    pickup_request_id BIGINT NOT NULL UNIQUE,
    vehicle_id BIGINT NOT NULL,
    assigned_by_staff_id BIGINT,
    scheduled_date DATE NOT NULL,
    status ENUM('SCHEDULED','IN_PROGRESS','COMPLETED','CANCELLED') NOT NULL,
    created_at DATETIME,
    FOREIGN KEY (pickup_request_id) REFERENCES pickup_requests(id),
    FOREIGN KEY (vehicle_id) REFERENCES vehicles(id),
    FOREIGN KEY (assigned_by_staff_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS complaints (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    citizen_id BIGINT NOT NULL,
    pickup_request_id BIGINT,
    subject VARCHAR(255) NOT NULL,
    description VARCHAR(2000) NOT NULL,
    status ENUM('OPEN','IN_PROGRESS','RESOLVED','REJECTED') NOT NULL,
    resolution_notes VARCHAR(2000),
    created_at DATETIME,
    resolved_at DATETIME,
    FOREIGN KEY (citizen_id) REFERENCES users(id),
    FOREIGN KEY (pickup_request_id) REFERENCES pickup_requests(id)
);
