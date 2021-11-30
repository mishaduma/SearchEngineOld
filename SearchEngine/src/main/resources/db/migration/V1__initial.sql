CREATE TABLE IF NOT EXISTS field
(
    id
    INT
    AUTO_INCREMENT
    NOT
    NULL,
    name
    VARCHAR
(
    255
) NOT NULL,
    selector VARCHAR
(
    255
) NOT NULL,
    weight FLOAT NOT NULL,
    CONSTRAINT pk_field PRIMARY KEY
(
    id
)
    );

CREATE TABLE IF NOT EXISTS lemma
(
    id
    INT
    AUTO_INCREMENT
    NOT
    NULL,
    lemma
    VARCHAR
(
    250
) NOT NULL,
    frequency INT NOT NULL,
    CONSTRAINT pk_lemma PRIMARY KEY
(
    id
)
    );

CREATE TABLE IF NOT EXISTS page
(
    id
    INT
    AUTO_INCREMENT
    NOT
    NULL,
    `path`
    VARCHAR
(
    250
) NOT NULL,
    code INT NOT NULL,
    content MEDIUMTEXT NOT NULL,
    CONSTRAINT pk_page PRIMARY KEY
(
    id
)
    );

CREATE TABLE IF NOT EXISTS search_index
(
    id
    INT
    AUTO_INCREMENT
    NOT
    NULL,
    page_id
    INT
    NOT
    NULL,
    lemma_id
    INT
    NOT
    NULL,
    lemma_rank
    FLOAT
    NOT
    NULL,
    CONSTRAINT
    pk_searchindex
    PRIMARY
    KEY
(
    id
)
    );

CREATE INDEX idx_ae560880774d631628e8553b2 ON lemma (lemma);

CREATE INDEX idx_ecac4f41dd1850060392b4ba3 ON page (`path`);