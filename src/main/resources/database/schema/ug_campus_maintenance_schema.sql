-- ============================================================
-- UG Campus Maintenance Service Optimizer
-- MySQL Database Schema
-- ============================================================

-- Drop tables in reverse dependency order (safe re-run)
DROP TABLE IF EXISTS audit_logs;
DROP TABLE IF EXISTS algorithm_runs;
DROP TABLE IF EXISTS technician_assignments;
DROP TABLE IF EXISTS request_status_logs;
DROP TABLE IF EXISTS service_requests;
DROP TABLE IF EXISTS technicians;
DROP TABLE IF EXISTS roads;
DROP TABLE IF EXISTS service_categories;
DROP TABLE IF EXISTS locations;
DROP TABLE IF EXISTS campus_users;

-- ============================================================
-- 1. campus_users
--    Stores information about all system users.
-- ============================================================
CREATE TABLE campus_users (
    id            INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    full_name     VARCHAR(100)  NOT NULL,
    email         VARCHAR(100)  NOT NULL UNIQUE,
    phone_number  VARCHAR(20),
    role          ENUM(
                    'Student',
                    'Lecturer',
                    'Maintenance Officer',
                    'ICT Staff',
                    'Security Officer',
                    'Administrator'
                  ) NOT NULL,
    created_at    TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP     NULL     ON UPDATE CURRENT_TIMESTAMP
);

-- ============================================================
-- 2. locations
--    Stores campus locations where maintenance requests occur.
-- ============================================================
CREATE TABLE locations (
    id              INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    location_name   VARCHAR(100) NOT NULL,
    location_type   ENUM(
                      'Department',
                      'Hall',
                      'Library',
                      'Office',
                      'Laboratory',
                      'Lecture Hall'
                    ) NOT NULL,
    description     TEXT,
    created_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================
-- 3. service_categories
--    Defines the types of maintenance services available.
-- ============================================================
CREATE TABLE service_categories (
    id              INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    category_name   VARCHAR(100) NOT NULL UNIQUE,
    description     TEXT
);

-- ============================================================
-- 4. roads
--    Represents the campus road network for route planning
--    and shortest-path algorithms.
-- ============================================================
CREATE TABLE roads (
    id                    INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    from_location_id      INT UNSIGNED NOT NULL,
    to_location_id        INT UNSIGNED NOT NULL,
    distance_km           DECIMAL(5,2) NOT NULL,
    travel_time_minutes   INT          NOT NULL,
    road_condition        ENUM('Excellent', 'Good', 'Fair', 'Poor') NOT NULL,
    created_at            TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_roads_from_location
        FOREIGN KEY (from_location_id) REFERENCES locations (id)
        ON UPDATE CASCADE ON DELETE RESTRICT,

    CONSTRAINT fk_roads_to_location
        FOREIGN KEY (to_location_id)   REFERENCES locations (id)
        ON UPDATE CASCADE ON DELETE RESTRICT
);

-- ============================================================
-- 5. technicians
--    Stores maintenance personnel available for assignment.
--    References service_categories to indicate specialization.
-- ============================================================
CREATE TABLE technicians (
    id                  INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    full_name           VARCHAR(100) NOT NULL,
    specialization      VARCHAR(100),
    category_id         INT UNSIGNED NOT NULL,
    phone_number        VARCHAR(20),
    vehicle_assigned    VARCHAR(50),
    availability_status BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at          TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_technicians_category
        FOREIGN KEY (category_id) REFERENCES service_categories (id)
        ON UPDATE CASCADE ON DELETE RESTRICT
);

-- ============================================================
-- 6. service_requests
--    Core table — stores maintenance requests submitted by users.
--    Note: assigned_technician_id is intentionally excluded;
--    assignments are managed via the technician_assignments table.
-- ============================================================
CREATE TABLE service_requests (
    id              INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    user_id         INT UNSIGNED NOT NULL,
    location_id     INT UNSIGNED NOT NULL,
    category_id     INT UNSIGNED NOT NULL,
    request_title   VARCHAR(255) NOT NULL,
    description     TEXT,
    urgency_level   TINYINT UNSIGNED NOT NULL CHECK (urgency_level BETWEEN 1 AND 5),
    status          ENUM(
                      'Pending',
                      'Assigned',
                      'In Progress',
                      'Completed',
                      'Cancelled'
                    ) NOT NULL DEFAULT 'Pending',
    request_date    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    completion_date DATETIME,

    CONSTRAINT fk_service_requests_user
        FOREIGN KEY (user_id)     REFERENCES campus_users (id)
        ON UPDATE CASCADE ON DELETE RESTRICT,

    CONSTRAINT fk_service_requests_location
        FOREIGN KEY (location_id) REFERENCES locations (id)
        ON UPDATE CASCADE ON DELETE RESTRICT,

    CONSTRAINT fk_service_requests_category
        FOREIGN KEY (category_id) REFERENCES service_categories (id)
        ON UPDATE CASCADE ON DELETE RESTRICT
);

-- ============================================================
-- 7. request_status_logs
--    Tracks every status change made to a maintenance request.
-- ============================================================
CREATE TABLE request_status_logs (
    id          INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    request_id  INT UNSIGNED NOT NULL,
    old_status  VARCHAR(50),
    new_status  VARCHAR(50)  NOT NULL,
    updated_by  INT UNSIGNED NOT NULL,
    comments    TEXT,
    updated_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_status_logs_request
        FOREIGN KEY (request_id) REFERENCES service_requests (id)
        ON UPDATE CASCADE ON DELETE CASCADE,

    CONSTRAINT fk_status_logs_user
        FOREIGN KEY (updated_by)  REFERENCES campus_users (id)
        ON UPDATE CASCADE ON DELETE RESTRICT
);

-- ============================================================
-- 8. technician_assignments
--    Maintains assignment history between requests and technicians.
-- ============================================================
CREATE TABLE technician_assignments (
    id                  INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    request_id          INT UNSIGNED NOT NULL,
    technician_id       INT UNSIGNED NOT NULL,
    assigned_date       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    assignment_status   ENUM('Assigned', 'Accepted', 'Completed', 'Rejected')
                        NOT NULL DEFAULT 'Assigned',

    CONSTRAINT fk_assignments_request
        FOREIGN KEY (request_id)    REFERENCES service_requests (id)
        ON UPDATE CASCADE ON DELETE CASCADE,

    CONSTRAINT fk_assignments_technician
        FOREIGN KEY (technician_id) REFERENCES technicians (id)
        ON UPDATE CASCADE ON DELETE RESTRICT
);

-- ============================================================
-- 9. algorithm_runs
--    Stores performance results for algorithms used in the project.
-- ============================================================
CREATE TABLE algorithm_runs (
    id              INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    algorithm_name  VARCHAR(100)    NOT NULL,
    runtime_ms      DECIMAL(10,2),
    memory_used_kb  DECIMAL(10,2),
    input_size      INT,
    execution_date  DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================
-- 10. audit_logs
--     Tracks actions performed within the system for accountability.
-- ============================================================
CREATE TABLE audit_logs (
    id          INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    user_id     INT UNSIGNED NOT NULL,
    action_type VARCHAR(100) NOT NULL,
    description TEXT,
    created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_audit_logs_user
        FOREIGN KEY (user_id) REFERENCES campus_users (id)
        ON UPDATE CASCADE ON DELETE RESTRICT
);
