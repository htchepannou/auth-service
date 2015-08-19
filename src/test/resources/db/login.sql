-- should_return_401_when_inactive
insert into login(login_id, login_party_fk, login_active) values(100, 100, 0);

-- should_return_401_when_logged_out
insert into login(login_id, login_party_fk, login_active, login_logout_date) values(101, 101, 1, now());

-- should_return_access_token
insert into login(login_id, login_party_fk, login_active, login_date) values(200, 200, 1, now());
