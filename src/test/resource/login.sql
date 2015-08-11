-- inactive
insert into login(login_id, login_party_fk, login_active) values(100, 100, 0);

-- logged out
insert into login(login_id, login_party_fk, login_active, login_logout_date) values(101, 101, 1, now());

-- get
insert into login(login_id, login_party_fk, login_active, login_date) values(200, 200, 1, now());
