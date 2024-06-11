-- liquibase formatted sql

-- changeset vfilin:1
CREATE TABLE notification_task
(
    id        serial primary key,
    chat_id   BIGINT,
    task_text VARCHAR(255),
    task_date TIMESTAMP
);