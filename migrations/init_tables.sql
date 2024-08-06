CREATE TABLE IF NOT EXISTS note (
    id          INT         GENERATED ALWAYS AS IDENTITY,
    ip          VARCHAR(20) NOT NULL,
    sourceText  TEXT        NOT NULL,
    translated  TEXT        NOT NULL
);