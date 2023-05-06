DELETE
FROM comments;
ALTER TABLE comments
    ALTER COLUMN id RESTART WITH 1;
DELETE
FROM requests;
ALTER TABLE requests
    ALTER COLUMN id RESTART WITH 1;
DELETE
FROM bookings;
ALTER TABLE bookings
    ALTER COLUMN id RESTART WITH 1;
DELETE
FROM items;
ALTER TABLE items
    ALTER COLUMN id RESTART WITH 1;
DELETE
FROM users;
ALTER TABLE users
    ALTER COLUMN id RESTART WITH 1;

INSERT INTO users (name, email)
VALUES ('user_1', 'user_1@mail.ru'),
       ('user_2', 'user_2@mail.ru'),
       ('user_3', 'user_3@mail.ru');

INSERT INTO items (name, description, available, owner_id, request_id)
VALUES ('Item1', 'Description', true, 1, null),
       ('Item2', 'Description', true, 2, null),
       ('Item3', 'Description', true, 2, null),
       ('Item4', 'Description', true, 3, null);

INSERT INTO bookings (status, start_time, end_time, item_id, booker_id)
VALUES ('WAITING', '2023-06-01T12:00:00', '2023-06-02T12:00:00', 1, 2),
       ('REJECTED', '2023-06-01T12:00:00', '2023-06-02T12:00:00', 1, 2),
       ('APPROVED', '2023-04-26T12:00:00', '2023-04-27T12:00:00', 1, 2),
       ('APPROVED', '2023-05-10T12:00:00', '2023-06-10T12:00:00', 1, 2);

INSERT INTO requests (description, created, requestor_id)
VALUES ('description', '2023-06-10T12:00:00', 1),
       ('description2', '2023-06-10T12:00:00', 2);

INSERT INTO comments (text, author_name, created, item_id, booker_id)
VALUES ('text_comment', 'user_2', '2023-06-10T12:00:00', 1, 2);