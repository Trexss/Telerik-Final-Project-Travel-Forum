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
                                                (1, 'Discovering Hidden Gems in Southeast Asia', 'Southeast Asia offers incredible destinations beyond the usual tourist spots. From secret beaches in Thailand to mountain villages in Vietnam, this region is full of surprises. I spent three months exploring and these are my top recommendations for adventurous travelers.'),
                                                (1, 'Best Budget Airlines for European Travel', 'Flying across Europe does not have to break the bank. After testing dozens of budget carriers, I found some that offer great value without compromising safety. Here is my comprehensive guide to choosing the right airline for your European adventure.'),
                                                (1, 'Solo Travel Safety Tips Every Woman Should Know', 'Traveling alone as a woman can be empowering and transformative. However, staying safe requires preparation and awareness. These practical tips helped me travel confidently through over thirty countries while minimizing risks and maximizing experiences.'),
                                                (1, 'Top Ten Hiking Trails Around the World', 'From the Inca Trail in Peru to the Laugavegur Trail in Iceland, our planet offers breathtaking hiking experiences. Each trail presents unique challenges and rewards. Here are the ten most memorable hikes from my travels and what makes each one special.'),
                                                (1, 'How to Plan a Three Week Trip to Japan', 'Japan combines ancient traditions with cutting-edge technology. Planning a trip requires understanding transport options, cultural etiquette, and must-see destinations. This detailed guide covers everything from JR Pass tips to hidden temples in Kyoto.'),
                                                (1, 'Street Food Adventures in Bangkok', 'Bangkok street food scene is legendary for good reason. From pad thai to mango sticky rice, the flavors are unforgettable. I explored over fifty street stalls and night markets to bring you this ultimate guide to eating your way through Bangkok safely and deliciously.'),
                                                (1, 'Backpacking Through Patagonia on a Budget', 'Patagonia stunning landscapes attract adventurers worldwide, but costs can add up quickly. After two months backpacking through Argentina and Chile, I learned how to experience this incredible region without emptying my savings. Here are my best money-saving strategies.'),
                                                (1, 'Cultural Etiquette Guide for India', 'India rich cultural diversity means travelers need to be mindful of local customs and traditions. Understanding proper etiquette enhances your experience and shows respect to locals. These guidelines helped me navigate social situations during my six-month journey through India.'),
                                                (1, 'Best Time to Visit Scandinavian Countries', 'Timing your Scandinavian adventure can make all the difference between magical experiences and disappointing weather. Each season offers unique attractions from northern lights to midnight sun. This comprehensive seasonal guide helps you plan the perfect Nordic journey.'),
                                                (1, 'Island Hopping in Greece: Complete Itinerary', 'Greece boasts thousands of islands, each with distinct character and charm. Planning an island hopping route requires balancing ferry schedules, accommodation availability, and personal interests. Here is my proven three-week itinerary covering the best Greek islands.'),
                                                (1, 'Volunteering Abroad: My Experience in Costa Rica', 'Combining travel with meaningful volunteer work enriches the experience for everyone involved. I spent two months working at a wildlife conservation project in Costa Rica. This post shares what to expect when volunteering abroad and how to find legitimate opportunities.'),
                                                (1, 'Essential Photography Gear for Travel', 'Capturing travel memories requires balancing gear quality with portability. After years of experimenting, I found the perfect setup that delivers professional results without weighing down my backpack. Here is my complete photography equipment guide for serious travel photographers.'),
                                                (1, 'Overcoming Language Barriers While Traveling', 'Not speaking the local language should not stop you from exploring the world. Through trial and error, I developed effective communication strategies that work anywhere. These practical tips and recommended apps will help you connect with locals regardless of language differences.'),
                                                (1, 'Ultimate Road Trip Through New Zealand', 'New Zealand dramatic landscapes make it perfect for epic road trips. From geothermal wonders to fjords, every turn reveals something spectacular. This detailed guide covers the best routes, campervan tips, and must-see stops for an unforgettable Kiwi adventure.'),
                                                (1, 'Digital Nomad Life in Bali: Honest Review', 'Bali attracts digital nomads seeking tropical paradise while working remotely. After living there for six months, I experienced both the magic and challenges. Here is my honest assessment of coworking spaces, cost of living, visa requirements, and whether Bali lives up to the hype.');

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