-- NOTE: This SQL seed duplicates the CSV seed files in this directory; keep them in sync (or remove one source) to avoid drift.
INSERT INTO service_categories
(id, category_name, description)
VALUES
(1, 'Plumbing', 'Water pipe and drainage repairs'),
(2, 'Electrical', 'Power and lighting maintenance'),
(3, 'ICT Support', 'Computer network and device support'),
(4, 'Carpentry', 'Furniture and woodwork repairs'),
(5, 'Cleaning', 'Sanitation and general housekeeping'),
(6, 'Security', 'Access control and safety support'),
(7, 'AC Services', 'Air conditioning repairs');
-- campus_users

INSERT INTO campus_users
(id, full_name, email, phone_number, role, created_at, updated_at)
VALUES
(1, 'Kwame Asiedu', 'kwame@ug.edu.gh', '0200000001', 'Student', '2026-01-10 11:00:00', NULL),
(2, 'Afia Boateng', 'afia@ug.edu.gh', '0200000002', 'Lecturer', '2026-01-10 11:05:00', NULL),
(3, 'Michael Addo', 'michael@ug.edu.gh', '0200000003', 'Maintenance Officer', '2026-01-10 11:10:00', NULL),
(4, 'Grace Owusu', 'grace@ug.edu.gh', '0200000004', 'ICT Staff', '2026-01-10 11:15:00', NULL),
(5, 'Nana Yaw', 'nana@ug.edu.gh', '0200000005', 'Security Officer', '2026-01-10 11:20:00', NULL),
(6, 'Ruth Darko', 'ruth@ug.edu.gh', '0200000006', 'Administrator', '2026-01-10 11:25:00', NULL),
(7, 'Selorm Tetteh', 'selorm@ug.edu.gh', '0200000007', 'Student', '2026-01-10 11:30:00', NULL),
(8, 'Evelyn Ansah', 'evelyn@ug.edu.gh', '0200000008', 'Lecturer', '2026-01-10 11:35:00', NULL);
-- locations

INSERT INTO locations
(id, location_name, location_type, description, created_at)
VALUES
(1, 'Maths department', 'Department', 'Campus academic department', '2026-01-10 08:00:00'),
(2, 'Computer science department', 'Department', 'Campus academic department', '2026-01-10 08:01:00'),
(3, 'Statistics department', 'Department', 'Campus academic department', '2026-01-10 08:02:00'),
(4, 'ISSSER', 'Department', 'Institute of Statistical, Social and Economic Research', '2026-01-10 08:03:00'),
(5, 'Law school', 'Department', 'Faculty of law', '2026-01-10 08:04:00'),
(6, 'ISSER ANNEX', 'Department', 'Annex for ISSER activities', '2026-01-10 08:05:00'),
(7, 'School of engineering', 'Department', 'Engineering faculty unit', '2026-01-10 08:06:00'),
(8, 'Office of risk management', 'Office', 'Campus risk and compliance office', '2026-01-10 08:07:00'),
(9, 'Center for urban management studies', 'Department', 'Academic research center', '2026-01-10 08:08:00'),
(10, 'International house', 'Office', 'International affairs office', '2026-01-10 08:09:00'),
(11, 'K. A Busia', 'Lecture Hall', 'lecture halls', '2026-01-10 08:10:00'),
(12, 'JQB ( Jones Quartey Building )', 'Lecture Hall', 'Academic building', '2026-01-10 08:11:00'),
(13, 'RT HON ALBAN BAGBIN hotspot zone', 'Office', 'Campus hotspot zone', '2026-01-10 08:12:00'),
(14, 'Department of archaeology and heritage studies', 'Department', 'Academic department', '2026-01-10 08:13:00'),
(15, 'Department of communication studies', 'Department', 'Academic department', '2026-01-10 08:14:00'),
(16, 'School of information and communication studies', 'Department', 'Academic school', '2026-01-10 08:15:00'),
(17, 'School of education and leadership', 'Department', 'Academic school', '2026-01-10 08:16:00'),
(18, 'Institute of African studies', 'Department', 'Academic institute', '2026-01-10 08:17:00'),
(19, 'Institute of continuing and distance education', 'Department', 'Academic institute', '2026-01-10 08:18:00'),
(20, 'Department of geography', 'Department', 'Academic department', '2026-01-10 08:19:00'),
(21, 'Department of earth science', 'Department', 'Academic department', '2026-01-10 08:20:00'),
(22, 'Legon centre for international affairs and diplomacy (LECIAD)', 'Department', 'Academic center', '2026-01-10 08:21:00'),
(23, 'African regional postgraduate programme in insect science (ARPPIS)', 'Department', 'Academic programme', '2026-01-10 08:22:00'),
(24, 'Department of family and consumer sciences (Annex)', 'Department', 'Academic department', '2026-01-10 08:23:00'),
(25, 'Department of chemistry', 'Department', 'Academic department', '2026-01-10 08:24:00'),
(26, 'Department of physics', 'Department', 'Academic department', '2026-01-10 08:25:00'),
(27, 'Absa bank', 'Office', 'Banking facility', '2026-01-10 08:26:00'),
(28, 'Standard chartered', 'Office', 'Banking facility', '2026-01-10 08:27:00'),
(29, 'Balme library', 'Library', 'Main university library', '2026-01-10 08:28:00'),
(30, 'UG bookshop', 'Office', 'Campus bookstore', '2026-01-10 08:29:00'),
(31, 'Department of economics', 'Department', 'Academic department', '2026-01-10 08:30:00'),
(32, 'Office of student affairs', 'Office', 'Student services office', '2026-01-10 08:31:00'),
(33, 'Cedi conference center', 'Office', 'Conference venue', '2026-01-10 08:32:00'),
(34, 'Department of philosophy and classics', 'Department', 'Academic department', '2026-01-10 08:33:00'),
(35, 'Department for the study of religions', 'Department', 'Academic department', '2026-01-10 08:34:00'),
(36, 'Department of African and Asian languages', 'Department', 'Academic department', '2026-01-10 08:35:00'),
(37, 'Department of European languages', 'Department', 'Academic department', '2026-01-10 08:36:00'),
(38, 'Department of French', 'Department', 'Academic department', '2026-01-10 08:37:00'),
(39, 'Volta hall', 'Hall', 'Student residence hall', '2026-01-10 08:38:00'),
(40, 'UGBS', 'Department', 'University of Ghana Business School', '2026-01-10 08:39:00'),
(41, 'Staff development and learning resource center', 'Office', 'Learning support center', '2026-01-10 08:40:00'),
(42, 'LOT lecture hall', 'Lecture Hall', 'Large lecture hall', '2026-01-10 08:41:00'),
(43, 'School of nursing and midwifery', 'Department', 'Academic school', '2026-01-10 08:42:00'),
(44, 'Department of nutrition and food science', 'Department', 'Academic department', '2026-01-10 08:43:00'),
(45, 'School of pharmacy', 'Department', 'Academic school', '2026-01-10 08:44:00'),
(46, 'St Thomas aquinas Catholic Church', 'Office', 'Religious center', '2026-01-10 08:45:00'),
(47, 'Department of biochemistry and molecular biology', 'Department', 'Academic department', '2026-01-10 08:46:00'),
(48, 'New N-Block(NNB)', 'Lecture Hall', 'Lecture hall complex', '2026-01-10 08:47:00'),
(49, 'GCB', 'Lecture Hall', 'Lecture hall complex', '2026-01-10 08:48:00'),
(50, 'Department of marine and fishery science', 'Department', 'Academic department', '2026-01-10 08:49:00'),
(51, 'School of social sciences', 'Department', 'Academic school', '2026-01-10 08:50:00'),
(52, 'Department of information studies', 'Department', 'Academic department', '2026-01-10 08:51:00'),
(53, 'Department of political science', 'Department', 'Academic department', '2026-01-10 08:52:00'),
(54, 'New Block (NB)', 'Lecture Hall', 'Lecture hall complex', '2026-01-10 08:53:00'),
(55, 'Center for biodiversity conservation research', 'Department', 'Academic research center', '2026-01-10 08:54:00'),
(56, 'Department of plant and environmental biology', 'Department', 'Academic department', '2026-01-10 08:55:00'),
(57, 'Akuafo hall', 'Hall', 'Student residence hall', '2026-01-10 08:56:00'),
(58, 'Legon hall', 'Hall', 'Student residence hall', '2026-01-10 08:57:00'),
(59, 'Legon hall annex A,B', 'Hall', 'Student residence annex', '2026-01-10 08:58:00'),
(60, 'Department of soil science', 'Department', 'Academic department', '2026-01-10 08:59:00'),
(61, 'Department of crop science', 'Department', 'Academic department', '2026-01-10 09:00:00'),
(62, 'Akuafo hall annex A, B, C', 'Hall', 'Student residence annex', '2026-01-10 09:01:00'),
(63, 'First bank national hotspot zone', 'Office', 'Campus hotspot zone', '2026-01-10 09:02:00'),
(64, 'Mensah sarbah hall', 'Hall', 'Student residence hall', '2026-01-10 09:03:00'),
(65, 'Mensah sarbah hall A, B, C', 'Hall', 'Student residence halls', '2026-01-10 09:04:00'),
(66, 'Central cafeteria (CC)', 'Office', 'Campus dining facility', '2026-01-10 09:05:00'),
(67, 'SRC union office', 'Office', 'Student representative office', '2026-01-10 09:06:00'),
(68, 'UG clinic', 'Office', 'Campus health center', '2026-01-10 09:07:00'),
(69, 'Vikings hall', 'Hall', 'Student residence hall', '2026-01-10 09:08:00'),
(70, 'Valco trust hall', 'Hall', 'Student residence hall', '2026-01-10 09:09:00'),
(71, 'Center for ageing studies', 'Department', 'Academic research center', '2026-01-10 09:10:00'),
(72, 'Stanbic bank', 'Office', 'Banking facility', '2026-01-10 09:11:00'),
(73, 'Access bank', 'Office', 'Banking facility', '2026-01-10 09:12:00'),
(74, 'Republic bank', 'Office', 'Banking facility', '2026-01-10 09:13:00'),
(75, 'Prudential bank', 'Office', 'Banking facility', '2026-01-10 09:14:00'),
(76, 'UG Cooperative credit union ltd', 'Office', 'Financial service center', '2026-01-10 09:15:00'),
(77, 'Cal bank', 'Office', 'Banking facility', '2026-01-10 09:16:00'),
(78, 'CBG', 'Office', 'Banking facility', '2026-01-10 09:17:00'),
(79, 'UMB', 'Office', 'Banking facility', '2026-01-10 09:18:00'),
(80, 'International student hostel (ISH) 1', 'Hall', 'Student hostel', '2026-01-10 09:19:00'),
(81, 'International student hostel (ISH) 2', 'Hall', 'Student hostel', '2026-01-10 09:20:00'),
(82, 'Jubilee hall', 'Hall', 'Student residence hall', '2026-01-10 09:21:00'),
(83, 'Hilla Limann Hall', 'Hall', 'Student residence hall', '2026-01-10 09:22:00'),
(84, 'Alexander Kwapong hall', 'Hall', 'Student residence hall', '2026-01-10 09:23:00'),
(85, 'Jean Nelson hall', 'Hall', 'Student residence hall', '2026-01-10 09:24:00'),
(86, 'Elizabeth Sey hall', 'Hall', 'Student residence hall', '2026-01-10 09:25:00'),
(87, 'Diamond Jubilee Hall', 'Hall', 'Student residence hall', '2026-01-10 09:26:00'),
(88, 'Bani hostel', 'Hall', 'Student hostel', '2026-01-10 09:27:00'),
(89, 'Evandy hostel', 'Hall', 'Student hostel', '2026-01-10 09:28:00'),
(90, 'Pentagon hostel', 'Hall', 'Student hostel', '2026-01-10 09:29:00'),
(91, 'Great Hall', 'Hall', 'Assembly Hall', '2026-01-10 09:39:00');

INSERT INTO technicians (id, full_name, specialization, category_id, phone_number, vehicle_assigned, availability_status, created_at)
VALUES
(1, 'Kofi Boateng', 'Plumbing', 1, '0244001001', 'Pickup-01', 1, '2026-01-10 10:00:00'),
(2, 'Ama Serwaa', 'Electrical', 2, '0244001002', 'Van-02', 1, '2026-01-10 10:05:00'),
(3, 'Esi Mensah', 'ICT Support', 3, '0244001003', 'Saloon-03', 1, '2026-01-10 10:10:00'),
(4, 'Kwame Badu', 'Carpentry', 4, '0244001004', 'Pickup-01', 0, '2026-01-10 10:15:00'),
(5, 'Lydia Owusu', 'Cleaning', 5, '0244001005', 'Van-04', 1, '2026-01-10 10:20:00'),
(6, 'Daniel Acquah', 'Security', 6, '0244001006', 'Bike-06', 1, '2026-01-10 10:25:00'),
(7, 'Boye Jonadson', 'AC Services', 7, '00595994432', 'Van-05', 1, '2026-01-10 10:35:00'),

(8, 'Michael Asante', 'Plumbing', 1, '0244001008', 'Pickup-02', 1, '2026-01-10 10:40:00'),
(9, 'Patricia Nkrumah', 'Electrical', 2, '0244001009', 'Van-03', 1, '2026-01-10 10:45:00'),
(10, 'Abigail Owusu', 'ICT Support', 3, '0244001010', 'Saloon-04', 1, '2026-01-10 10:50:00'),
(11, 'Edwin Tetteh', 'Carpentry', 4, '0244001011', 'Pickup-03', 1, '2026-01-10 10:55:00'),
(12, 'Nana Yaw Boateng', 'Cleaning', 5, '0244001012', 'Van-05', 1, '2026-01-10 11:00:00'),
(13, 'Portia Agyeman', 'Security', 6, '0244001013', 'Bike-07', 1, '2026-01-10 11:05:00'),
(14, 'Samuel Osei', 'AC services', 7, '0244001014', 'Van-06', 1, '2026-01-10 11:10:00'),
(15, 'Grace Adu', 'Plumbing', 1, '0244001015', 'Pickup-04', 1, '2026-01-10 11:15:00'),
(16, 'Emmanuel Mensah', 'Electrical', 2, '0244001016', 'Van-07', 1, '2026-01-10 11:20:00'),
(17, 'Comfort Aidoo', 'ICT Support', 3, '0244001017', 'Saloon-05', 1, '2026-01-10 11:25:00'),
(18, 'Frank Asare', 'Carpentry', 4, '0244001018', 'Pickup-05', 0, '2026-01-10 11:30:00'),
(19, 'Maame Efua Darko', 'Cleaning', 5, '0244001019', 'Van-08', 1, '2026-01-10 11:35:00'),
(20, 'Prosper Addo', 'Security', 6, '0244001020', 'Bike-08', 1, '2026-01-10 11:40:00'),
(21, 'Christiana Boadu', 'AC services', 7, '0244001021', 'Van-09', 1, '2026-01-10 11:45:00'),
(22, 'Richard Ansah', 'Plumbing', 1, '0244001022', 'Pickup-06', 1, '2026-01-10 11:50:00'),
(23, 'Helena Tamakloe', 'Electrical', 2, '0244001023', 'Van-10', 1, '2026-01-10 11:55:00'),
(24, 'Josephine Doku', 'ICT Support', 3, '0244001024', 'Saloon-06', 1, '2026-01-10 12:00:00'),
(25, 'Isaac Nyarko', 'Carpentry', 4, '0244001025', 'Pickup-07', 1, '2026-01-10 12:05:00'),
(26, 'Adwoa Appiah', 'Cleaning', 5, '0244001026', 'Van-11', 1, '2026-01-10 12:10:00'),
(27, 'Ernestina Sarpong', 'Security', 6, '0244001027', 'Bike-09', 1, '2026-01-10 12:15:00'),
(28, 'Kojo Boateng', 'AC services', 7, '0244001028', 'Van-12', 1, '2026-01-10 12:20:00'),
(29, 'Henrietta Owusu', 'Plumbing', 1, '0244001029', 'Pickup-08', 1, '2026-01-10 12:25:00'),
(30, 'Jonathan Mantey', 'Electrical', 2, '0244001030', 'Van-13', 1, '2026-01-10 12:30:00');

INSERT INTO roads (id, from_location_id, to_location_id, distance_km, travel_time_minutes, road_condition, created_at)
VALUES
(1, 1, 2, 0.80, 6, 'Excellent', '2026-01-10 09:00:00'),
(2, 2, 3, 1.20, 10, 'Good', '2026-01-10 09:05:00'),
(3, 2, 5, 1.50, 12, 'Fair', '2026-01-10 09:10:00'),
(4, 3, 6, 0.70, 5, 'Excellent', '2026-01-10 09:15:00'),
(5, 4, 1, 1.00, 8, 'Good', '2026-01-10 09:20:00'),
(6, 5, 7, 0.90, 7, 'Fair', '2026-01-10 09:25:00'),
(7, 6, 8, 1.30, 9, 'Good', '2026-01-10 09:30:00'),
(8, 7, 8, 0.60, 6, 'Excellent', '2026-01-10 09:35:00');

INSERT INTO service_requests (id, user_id, location_id, category_id, request_title, description, urgency_level, status, request_date, completion_date)
VALUES
(1, 1, 3, 1, 'Leaking pipe in library', 'Water leakage near the study area', 'High', 'Pending', '2026-02-01 08:00:00', NULL),
(2, 2, 6, 2, 'Power outage in lecture theatre', 'Several lights are off in the lecture hall', 'High', 'Assigned', '2026-02-02 09:30:00', NULL),
(3, 7, 4, 5, 'Blocked drain at hall', 'Drainage is blocked near the dormitory entrance', 'Medium', 'In Progress', '2026-02-03 10:15:00', NULL),
(4, 4, 8, 3, 'Computer lab network issue', 'Students cannot access the lab network', 'Critical', 'Completed', '2026-02-04 11:00:00', '2026-02-04 14:30:00'),
(5, 8, 2, 4, 'Broken desk in faculty office', 'One office desk is damaged and unstable', 'Low', 'Pending', '2026-02-05 12:30:00', NULL),
(6, 5, 1, 6, 'Security gate malfunction', 'Main gate access card reader is failing', 'High', 'Cancelled', '2026-02-06 13:00:00', NULL);

INSERT INTO technician_assignments (id, request_id, technician_id, assigned_date, assignment_status)
VALUES
(1, 2, 2, '2026-02-02 09:45:00', 'Accepted'),
(2, 3, 5, '2026-02-03 10:30:00', 'Assigned'),
(3, 4, 3, '2026-02-04 11:10:00', 'Completed'),
(4, 1, 1, '2026-02-01 08:20:00', 'Assigned');

INSERT INTO request_status_logs (id, request_id, old_status, new_status, updated_by, comments, updated_at)
VALUES
(1, 1, 'Pending', 'Assigned', 3, 'Technician assigned for the repair', '2026-02-01 08:20:00'),
(2, 2, 'Pending', 'Assigned', 3, 'Maintenance officer assigned electrician', '2026-02-02 09:45:00'),
(3, 3, 'Pending', 'In Progress', 3, 'Technician started cleanup work', '2026-02-03 10:30:00'),
(4, 4, 'Pending', 'Assigned', 4, 'ICT support team engaged', '2026-02-04 11:10:00'),
(5, 4, 'Assigned', 'Completed', 6, 'Task completed successfully', '2026-02-04 14:30:00'),
(6, 6, 'Pending', 'Cancelled', 6, 'Request cancelled by administrator', '2026-02-06 13:00:00');

INSERT INTO algorithm_runs (id, algorithm_name, runtime_ms, memory_used_kb, input_size, execution_date)
VALUES
(1, 'Dijkstra', 12.50, 2048.00, 8, '2026-02-07 10:00:00'),
(2, 'Greedy Best First', 9.80, 1536.00, 8, '2026-02-07 10:05:00'),
(3, 'Dynamic Programming', 18.20, 3072.00, 10, '2026-02-07 10:10:00'),
(4, 'BFS', 7.40, 1024.00, 6, '2026-02-07 10:15:00');

INSERT INTO audit_logs (id, user_id, action_type, description, created_at)
VALUES
(1, 6, 'Login', 'Administrator logged into the system', '2026-02-07 08:00:00'),
(2, 3, 'Request Update', 'Updated status for service request #1', '2026-02-01 08:20:00'),
(3, 4, 'Request Update', 'Assigned technician for request #4', '2026-02-04 11:10:00'),
(4, 6, 'Algorithm Run', 'Executed routing optimization analysis', '2026-02-07 10:15:00'),
(5, 3, 'Request Review', 'Reviewed pending requests for the day', '2026-02-07 11:00:00');
