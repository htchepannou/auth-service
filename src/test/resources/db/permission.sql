insert into application(app_id, app_name) values(1, 'app1');
insert into application(app_id, app_name) values(2, 'app2');

insert into permission(perm_app_fk, perm_role_fk, perm_value) VALUE (1, 1, 'add');
insert into permission(perm_app_fk, perm_role_fk, perm_value) VALUE (1, 1, 'edit');
insert into permission(perm_app_fk, perm_role_fk, perm_value) VALUE (1, 1, 'delete');

insert into permission(perm_app_fk, perm_role_fk, perm_value) VALUE (2, 1, 'access');
insert into permission(perm_app_fk, perm_role_fk, perm_value) VALUE (2, 1, 'edit');

insert into prel(prel_type_fk, prel_source_fk, prel_dest_fk, prel_qualifier) values(10, 100, 1000, 1);
