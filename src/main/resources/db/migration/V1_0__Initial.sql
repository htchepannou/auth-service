CREATE TABLE  login (
  login_id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  login_active BIT,
  login_admin BIT,
  login_date DATETIME,
  login_logout_date DATETIME,
  login_party_fk BIGINT
);

CREATE TABLE  application (
  app_id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  app_name VARCHAR(50)
);

CREATE TABLE prel(
  prel_id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  prel_type_fk BIGINT,
  prel_source_fk BIGINT,
  prel_dest_fk BIGINT,
  prel_qualifier VARCHAR(20)
);

CREATE TABLE permission(
  perm_id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  perm_app_fk BIGINT,
  perm_role_fk BIGINT,

  perm_value VARCHAR(20)
)

