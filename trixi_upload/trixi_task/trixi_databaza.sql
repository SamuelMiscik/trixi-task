-- Zapnut pri testovani
-- DROP TABLE IF EXISTS cast_obce;
-- DROP TABLE IF EXISTS obec;

CREATE TABLE obec (
                      kod INTEGER PRIMARY KEY, -- UNIQUE NOT NULL because it is basically the id of the village and it cannot be same as other village
                      name VARCHAR(100) NOT NULL -- longest name of a village in Czech Republic is „Nová Ves u Nového Města na Moravě = 33 chars so 100 is more then enough
);

CREATE TABLE cast_obce (
                           kod INTEGER PRIMARY KEY,
                           name VARCHAR(100) NOT NULL,
                           kod_obce INTEGER NOT NULL,

                           CONSTRAINT cast_obce_obec_FK
                               FOREIGN KEY (kod_obce)
                                   REFERENCES obec(kod)
                                   ON UPDATE CASCADE -- If parent updates than child updates too
                                   ON DELETE RESTRICT -- Parent cannot be deleted when a child is still attached (failsafe)
);

-- TESTING ----------------------------------------------------------------------------------

-- INSERT INTO obec (kod, name)
-- VALUES
--     (1, 'Praha'),
--     (2, 'Brno');
--
-- INSERT INTO cast_obce (kod, name, kod_obce)
-- VALUES
--     (101, 'Holesovice', 1),
--     (102, 'Zizkov', 1),
--     (201, 'Stred', 2);
--
-- SELECT * FROM obec;
-- SELECT * FROM cast_obce;