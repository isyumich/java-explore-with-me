DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS categories CASCADE;
DROP TABLE IF EXISTS locations CASCADE;
DROP TABLE IF EXISTS event_states CASCADE;
DROP TABLE IF EXISTS events CASCADE;
DROP TABLE IF EXISTS requests CASCADE;
DROP TABLE IF EXISTS compilations CASCADE;
DROP TABLE IF EXISTS compilations_events CASCADE;
DROP TABLE IF EXISTS views_events CASCADE;
DROP TABLE IF EXISTS comments CASCADE;


CREATE TABLE IF NOT EXISTS users
(
    id                          BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name                        VARCHAR(255) NOT NULL,
    email                       VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS categories
(
    id                          BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name                        VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS locations
(
    id                          BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    lat                         FLOAT NOT NULL,
    lon                         FLOAT NOT NULL
);

CREATE TABLE IF NOT EXISTS events
(
    id                          BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    annotation                  TEXT,
    category_id                 BIGINT REFERENCES categories(id) ON DELETE CASCADE,
    description                 TEXT,
    event_date                  TIMESTAMP WITHOUT TIME ZONE,
    created_on                  TIMESTAMP WITHOUT TIME ZONE,
    initiator_id                BIGINT REFERENCES users(id) ON DELETE CASCADE,
    location_id                 BIGINT REFERENCES locations(id) ON DELETE CASCADE,
    paid                        BOOLEAN,
    participant_limit           INTEGER,
    published_on                TIMESTAMP WITHOUT TIME ZONE,
    request_moderation          BOOLEAN,
    state                       VARCHAR(40),
    title                       VARCHAR(120)
);

CREATE TABLE IF NOT EXISTS requests
(
    id                          BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    created                     TIMESTAMP WITHOUT TIME ZONE,
    event_id                    BIGINT REFERENCES events(id) ON DELETE CASCADE,
    requester_id                BIGINT REFERENCES users(id) ON DELETE CASCADE,
    status                      VARCHAR(120) NOT NULL
);

CREATE TABLE IF NOT EXISTS compilations
(
    id                          BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    pinned                      BOOLEAN,
    title                       VARCHAR(550) UNIQUE
);

CREATE TABLE IF NOT EXISTS compilations_events
(
    event_id                    BIGINT REFERENCES events(id) ON DELETE CASCADE,
    compilation_id              BIGINT REFERENCES compilations(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS views_events
(
    event_id                    BIGINT REFERENCES events(id) ON DELETE CASCADE,
    user_id                     BIGINT REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS comments
(
    id                          BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    event_id                    BIGINT REFERENCES events(id) ON DELETE CASCADE,
    author_id                   BIGINT REFERENCES users(id) ON DELETE CASCADE,
    text                        TEXT,
    status                      VARCHAR(30),
    created_on                  TIMESTAMP WITHOUT TIME ZONE,
    published_on                TIMESTAMP WITHOUT TIME ZONE
)



