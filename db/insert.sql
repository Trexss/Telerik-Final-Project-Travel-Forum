INSERT INTO tags (name) VALUES
                            ('Java'), ('Spring'), ('Scuba'), ('Photography'),
                            ('Travel'), ('SQL'), ('Career'), ('Forum'),
                            ('Tech'), ('Beginners');

INSERT INTO users (username, first_name, last_name, email, password, phone_number, is_admin, is_blocked) VALUES
                                                                                                             ('ivo_dev', 'Ivan', 'Popov', 'ivo@example.com', 'pass123', '0888123456', 1, 0),
                                                                                                             ('dimitar10', 'Dimitar', 'Ivanov', 'dimitar@example.com', 'pass123', NULL, 0, 0),
                                                                                                             ('niko', 'Nikolay', 'Georgiev', 'niko@example.com', 'pass123', NULL, 0, 0),
                                                                                                             ('maria_photo', 'Maria', 'Petrova', 'maria@example.com', 'pass123', '0888777666', 0, 0),
                                                                                                             ('alex_travel', 'Alexander', 'Kolev', 'alex@example.com', 'pass123', NULL, 0, 0);

INSERT INTO posts (user_id, title, content) VALUES
                                                (1, 'Why learning Java changed my career', 'I started learning Java a year ago and the experience has been amazing. The more I learn, the more opportunities I see. Stay consistent and you will see progress.'),
                                                (2, 'Best practices for writing clean code', 'Clean code is not just about formatting. It is about understanding, readability and long-term maintainability. Here are some rules that help me daily...'),
                                                (3, 'Scuba Diving in Bulgaria – Top Spots', 'If you love diving, Bulgaria has some amazing locations. My favorites are Maslen Nos, Kiten Wreck, and Dragon Rocks.'),
                                                (4, 'Travel photography tips for beginners', 'Good photography is mostly about light and perspective. Even with a basic camera you can shoot great pictures if you focus on composition.'),
                                                (1, 'Spring Boot vs plain Java EE', 'When should you use Spring Boot instead of Java EE? In this post I summarize advantages and real-world examples.');

INSERT INTO comments (post_id, user_id, content) VALUES
                                                     (1, 2, 'Great motivation, thanks for sharing!'),
                                                     (1, 3, 'I completely agree! Consistency is everything.'),
                                                     (2, 1, 'Solid advice. Naming things is indeed very important.'),
                                                     (3, 5, 'I plan to visit these diving locations next summer.'),
                                                     (4, 4, 'Totally true, light is the key to everything.');

INSERT INTO post_likes (user_id, post_id) VALUES
                                              (2, 1), (3, 1), (4, 1), (1, 2), (5, 3), (2, 3), (3, 4), (1, 5);

INSERT INTO post_tags (post_id, tag_id) VALUES
                                            (1, 1), (1, 7), (2, 1), (2, 9),
                                            (3, 3), (3, 5), (4, 4),
                                            (5, 1), (5, 2);