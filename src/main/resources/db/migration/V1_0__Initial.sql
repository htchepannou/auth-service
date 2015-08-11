CREATE TABLE  login (
  login_id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  login_active BIT,
  login_admin BIT,
  login_date DATETIME,
  login_logout_date DATETIME,
  login_party_fk BIGINT
);
