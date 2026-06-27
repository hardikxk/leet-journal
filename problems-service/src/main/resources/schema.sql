CREATE TABLE IF NOT EXISTS user_problem (
    id         SERIAL PRIMARY KEY,
    username   VARCHAR(200) NOT NULL,
    problem_id INT          NOT NULL,
    is_done    BOOLEAN      NOT NULL DEFAULT FALSE,
    is_flagged BOOLEAN      NOT NULL DEFAULT FALSE,
    UNIQUE (username, problem_id)
);
