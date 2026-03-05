```sql
CREATE DATABASE IF NOT EXISTS carioquinha_rio
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;

USE carioquinha_rio;

SET FOREIGN_KEY_CHECKS = 0;
    DROP TABLE IF EXISTS log_auditoria;
    DROP TABLE IF EXISTS bebe;
    DROP TABLE IF EXISTS maternidade;
SET FOREIGN_KEY_CHECKS = 1;

CREATE TABLE maternidade (
    id   BIGINT(20) NOT NULL AUTO_INCREMENT,
    nome VARCHAR(150) NOT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci;

CREATE TABLE bebe (
    id                     BIGINT(20) NOT NULL AUTO_INCREMENT,
    nome                   VARCHAR(150) NOT NULL,
    data_nascimento        DATE NOT NULL,
    nome_mae               VARCHAR(150) NOT NULL,
    nome_pai               VARCHAR(150) NULL DEFAULT NULL,
    maternidade_id         BIGINT(20) NOT NULL,
    mensagem_responsavel   VARCHAR(500) NOT NULL,
    foto_base64            LONGTEXT NULL DEFAULT NULL,
    foto_mime              VARCHAR(50) NULL DEFAULT NULL,
    PRIMARY KEY (id),
    KEY idx_bebe_maternidade (maternidade_id),
    CONSTRAINT fk_bebe_maternidade
        FOREIGN KEY (maternidade_id) REFERENCES maternidade (id)
            ON UPDATE CASCADE
            ON DELETE RESTRICT
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci;

CREATE TABLE log_auditoria (
    id             BIGINT(20) NOT NULL AUTO_INCREMENT,
    id_bebe        BIGINT(20) NOT NULL,
    data_hora      DATETIME NOT NULL,
    acaoRealizada  VARCHAR(100) NOT NULL,
    id_operador    VARCHAR(50) NOT NULL,
    PRIMARY KEY (id),
    KEY idx_log_id_bebe (id_bebe),
    CONSTRAINT fk_log_bebe
        FOREIGN KEY (id_bebe) REFERENCES bebe (id)
            ON UPDATE CASCADE
            ON DELETE RESTRICT
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci;

INSERT INTO maternidade (nome) VALUES
    ('Maternidade A'),
    ('Maternidade B'),
    ('Maternidade C');
```
