-- Users
INSERT INTO app_user (name, email, password)
VALUES ('Amit Sharma', 'amit.sharma@example.com', 'password1'),
       ('Priya Singh', 'priya.singh@example.com', 'password2'),
       ('Rahul Verma', 'rahul.verma@example.com', 'password3'),
       ('Suresh Kumar', 'suresh.kumar@example.com', 'password4'),
       ('Neha Gupta', 'neha.gupta@example.com', 'password5'),
       ('Ravi Mehta', 'ravi.mehta@example.com', 'password6'),
       ('Shweta Jain', 'shweta.jain@example.com', 'password7'),
       ('Vikas Reddy', 'vikas.reddy@example.com', 'password8'),
       ('Aarav Desai', 'aarav.desai@example.com', 'password9'),
       ('Simran Kaur', 'simran.kaur@example.com', 'password10');

-- Applicants
INSERT INTO applicant (resume, is_first_session, user_id)
VALUES ('resume1.pdf', true, 1),
       ('resume2.pdf', false, 2),
       ('resume3.pdf', true, 3),
       ('resume4.pdf', true, 4),
       ('resume5.pdf', false, 5),
       ('resume6.pdf', true, 6),
       ('resume7.pdf', false, 7),
       ('resume8.pdf', true, 8),
       ('resume9.pdf', false, 9),
       ('resume10.pdf', true, 10);

-- Employers
INSERT INTO employer (company_name, company_website, user_id)
VALUES ('Tech Solutions', 'https://techsolutions.com', 1),
       ('Innovatech', 'https://innovatech.com', 2),
       ('Softwares Inc.', 'https://softwaresinc.com', 3),
       ('DataCorp', 'https://datacorp.com', 4),
       ('AI Dynamics', 'https://aidynamics.com', 5),
       ('NextGen Tech', 'https://nextgen.com', 6),
       ('CloudWorks', 'https://cloudworks.com', 7),
       ('ByteHub', 'https://bytehub.com', 8),
       ('Quantum Labs', 'https://quantumlabs.com', 9),
       ('GreenTech', 'https://greentech.com', 10);

-- Jobs
INSERT INTO job (title, description, location, job_status, company, employer_id)
VALUES ('Software Engineer', 'Develop and maintain software applications.', 'Mumbai', 'OPEN', 'Tech Solutions', 1),
       ('Data Analyst', 'Analyze data and generate reports.', 'Bangalore', 'CLOSED', 'Innovatech', 2),
       ('Product Manager', 'Oversee product development lifecycle.', 'Chennai', 'OPEN', 'Softwares Inc.', 3),
       ('Data Scientist', 'Design machine learning models.', 'Hyderabad', 'OPEN', 'DataCorp', 4),
       ('AI Engineer', 'Develop AI-powered solutions.', 'Pune', 'CLOSED', 'AI Dynamics', 5),
       ('Full Stack Developer', 'Build end-to-end applications.', 'Delhi', 'OPEN', 'NextGen Tech', 6),
       ('Cloud Architect', 'Design cloud infrastructure solutions.', 'Noida', 'OPEN', 'CloudWorks', 7),
       ('UX Designer', 'Design user interfaces for web applications.', 'Kolkata', 'CLOSED', 'ByteHub', 8),
       ('Blockchain Developer', 'Develop blockchain-based applications.', 'Mumbai', 'OPEN', 'Quantum Labs', 9),
       ('IoT Engineer', 'Build IoT systems for smart cities.', 'Bangalore', 'CLOSED', 'GreenTech', 10);

INSERT INTO job_applications (job_id, applicant_id, application_status, applied_date)
VALUES (1, 1, 'APPLIED', '2024-12-01 09:00:00'),
       (2, 2, 'ACCEPTED', '2024-12-02 10:00:00'),
       (3, 3, 'APPLIED', '2024-12-03 11:00:00'),
       (4, 4, 'ACCEPTED', '2024-12-04 12:00:00'),
       (5, 5, 'APPLIED', '2024-12-05 13:00:00'),
       (6, 6, 'ACCEPTED', '2024-12-06 14:00:00'),
       (7, 7, 'APPLIED', '2024-12-07 15:00:00'),
       (8, 8, 'ACCEPTED', '2024-12-08 16:00:00'),
       (9, 9, 'APPLIED', '2024-12-09 17:00:00'),
       (10, 10, 'ACCEPTED', '2024-12-10 18:00:00');

-- Mentors
INSERT INTO mentor (user_id, average_rating)
VALUES (1, 4.5),
       (2, 4.8),
       (3, 4.2),
       (4, 4.6),
       (5, 4.7),
       (6, 4.3),
       (7, 4.4),
       (8, 4.9),
       (9, 4.5),
       (10, 4.8);

-- Sessions
INSERT INTO session (session_start_time, session_end_time, session_fee, otp, mentor_id, applicant_id, session_type,
                     session_status, session_link)
VALUES ('2024-12-01 09:00:00', '2024-12-01 10:00:00', null, '123456', 1, 1, 'FREE', 'COMPLETED',
        'https://sessionlink1.com'),
       ('2024-12-02 11:00:00', '2024-12-02 12:00:00', 750.00, '654321', 2, 2, 'PAID', 'SCHEDULED',
        'https://sessionlink2.com'),
       ('2024-12-03 13:00:00', '2024-12-03 14:00:00', 600.00, '654322', 3, 3, 'PAID', 'COMPLETED',
        'https://sessionlink3.com'),
       ('2024-12-04 14:00:00', '2024-12-04 15:00:00', 450.00, '654323', 4, 4, 'PAID', 'SCHEDULED',
        'https://sessionlink4.com'),
       ('2024-12-05 16:00:00', '2024-12-05 17:00:00', 800.00, '654324', 5, 5, 'PAID', 'COMPLETED',
        'https://sessionlink5.com'),
       ('2024-12-06 18:00:00', '2024-12-06 19:00:00', 700.00, '654325', 6, 6, 'PAID', 'SCHEDULED',
        'https://sessionlink6.com'),
       ('2024-12-07 10:00:00', '2024-12-07 11:00:00', 850.00, '654326', 7, 7, 'PAID', 'COMPLETED',
        'https://sessionlink7.com'),
       ('2024-12-08 11:00:00', '2024-12-08 12:00:00', 400.00, '654327', 8, 8, 'PAID', 'SCHEDULED',
        'https://sessionlink8.com'),
       ('2024-12-09 13:00:00', '2024-12-09 14:00:00', null, '654328', 9, 9, 'FREE', 'COMPLETED',
        'https://sessionlink9.com'),
       ('2024-12-10 15:00:00', '2024-12-10 16:00:00', null, '654329', 10, 10, 'FREE', 'SCHEDULED',
        'https://sessionlink10.com');

-- Payments
INSERT INTO payment (amount, payment_status, payment_time, session_id)
VALUES (500.00, 'COMPLETED', '2024-12-01 12:00:00', 1),
       (750.00, 'PENDING', '2024-12-02 13:00:00', 2),
       (600.00, 'COMPLETED', '2024-12-03 14:00:00', 3),
       (450.00, 'PENDING', '2024-12-04 15:00:00', 4),
       (800.00, 'COMPLETED', '2024-12-05 16:00:00', 5),
       (700.00, 'PENDING', '2024-12-06 17:00:00', 6),
       (850.00, 'COMPLETED', '2024-12-07 18:00:00', 7),
       (400.00, 'PENDING', '2024-12-08 19:00:00', 8),
       (550.00, 'COMPLETED', '2024-12-09 20:00:00', 9),
       (650.00, 'PENDING', '2024-12-10 21:00:00', 10);

-- Wallets
INSERT INTO wallet (user_id, balance)
VALUES (1, 1000.00),
       (2, 1500.00),
       (3, 2000.00),
       (4, 2500.00),
       (5, 3000.00),
       (6, 3500.00),
       (7, 4000.00),
       (8, 4500.00),
       (9, 5000.00),
       (10, 5500.00);


-- Ratings Insert Statements
INSERT INTO rating (rating_value, comment, session_id, mentor_id, applicant_id)
VALUES (4.5, 'Great session, learned a lot!', 1, 1, 1),
       (4.8, 'Very insightful discussion and guidance.', 2, 2, 2),
       (4.3, 'The session was informative, but could have been more engaging.', 3, 3, 3),
       (4.6, 'I got the answers I was looking for. Nice session.', 4, 4, 4),
       (4.7, 'The mentor was very helpful and provided valuable insights.', 5, 5, 5),
       (4.4, 'Good session but I expected more practical examples.', 6, 6, 6),
       (4.9, 'Absolutely amazing! The mentor was excellent.', 7, 7, 7),
       (4.5, 'The session met my expectations.', 8, 8, 8),
       (4.8, 'Mentor provided clear explanations. Great learning experience.', 9, 9, 9),
       (4.6, 'Overall good session, but more interaction would have been better.', 10, 10, 10);

-- Wallet Transactions
INSERT INTO wallet_transaction (amount, transaction_type, session_session_id, transaction_id, wallet_id, time_stamp)
VALUES (500.00, 'DEBIT', 1, 'TXN123', 1, '2024-12-01 12:00:00'),
       (750.00, 'CREDIT', 2, 'TXN456', 2, '2024-12-02 13:00:00'),
       (600.00, 'DEBIT', 3, 'TXN789', 3, '2024-12-03 14:00:00'),
       (450.00, 'CREDIT', 4, 'TXN101', 4, '2024-12-04 15:00:00'),
       (800.00, 'DEBIT', 5, 'TXN202', 5, '2024-12-05 16:00:00'),
       (700.00, 'CREDIT', 6, 'TXN303', 6, '2024-12-06 17:00:00'),
       (850.00, 'DEBIT', 7, 'TXN404', 7, '2024-12-07 18:00:00'),
       (400.00, 'CREDIT', 8, 'TXN505', 8, '2024-12-08 19:00:00'),
       (550.00, 'DEBIT', 9, 'TXN606', 9, '2024-12-09 20:00:00'),
       (650.00, 'CREDIT', 10, 'TXN707', 10, '2024-12-10 21:00:00');


