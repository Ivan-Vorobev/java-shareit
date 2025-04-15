DROP TABLE IF EXISTS bookings;
DROP TABLE IF EXISTS items;
DROP TABLE IF EXISTS requests;
DROP TABLE IF EXISTS users;

CREATE TABLE IF NOT EXISTS users
(
    id
    BIGINT
    GENERATED
    BY
    DEFAULT AS
    IDENTITY
    NOT
    NULL,
    name
    VARCHAR
(
    255
) NOT NULL,
    email VARCHAR
(
    512
) NOT NULL,
    CONSTRAINT pk_users_id PRIMARY KEY
(
    id
),
    CONSTRAINT uq_users_email UNIQUE
(
    email
)
    );

CREATE TABLE IF NOT EXISTS requests
(
    id
    BIGINT
    GENERATED
    BY
    DEFAULT AS
    IDENTITY
    NOT
    NULL,
    description
    VARCHAR
(
    500
) NOT NULL,
    requestor_id BIGINT NOT NULL,
    created_date DATE NOT NULL,
    CONSTRAINT pk_requests_id PRIMARY KEY
(
    id
),
    CONSTRAINT fk_requests_requestor_id FOREIGN KEY
(
    requestor_id
) REFERENCES users
(
    id
)
    );

CREATE INDEX IF NOT EXISTS idx_requests_created ON requests (created_date);

CREATE TABLE IF NOT EXISTS items
(
    id
    BIGINT
    GENERATED
    BY
    DEFAULT AS
    IDENTITY
    NOT
    NULL,
    name
    VARCHAR
(
    255
) NOT NULL,
    description VARCHAR
(
    500
) NOT NULL,
    available BOOLEAN NOT NULL,
    owner_id BIGINT NOT NULL,
    request_id BIGINT,
    CONSTRAINT pk_items_id PRIMARY KEY
(
    id
),
    CONSTRAINT fk_items_owner_id FOREIGN KEY
(
    owner_id
) REFERENCES users
(
    id
),
    CONSTRAINT fk_items_request_id FOREIGN KEY
(
    request_id
) REFERENCES requests
(
    id
)
    );

CREATE TABLE IF NOT EXISTS bookings
(
    id
    BIGINT
    GENERATED
    BY
    DEFAULT AS
    IDENTITY
    NOT
    NULL,
    start_date
    DATE
    NOT
    NULL,
    end_date
    DATE
    NOT
    NULL,
    item_id
    BIGINT
    NOT
    NULL,
    booker_id
    BIGINT
    NOT
    NULL,
    status
    VARCHAR
(
    50
) NOT NULL,
    CONSTRAINT pk_bookings_id PRIMARY KEY
(
    id
),
    CONSTRAINT fk_bookings_booker_id FOREIGN KEY
(
    booker_id
) REFERENCES users
(
    id
),
    CONSTRAINT fk_bookings_item_id FOREIGN KEY
(
    item_id
) REFERENCES items
(
    id
)
    );

CREATE INDEX IF NOT EXISTS idx_bookings_status ON bookings (status);
