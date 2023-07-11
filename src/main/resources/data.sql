insert into member (username, password, nickname, activated) values ('admin', '$2a$08$lDnHPz7eUkSi6ao14Twuau08mzhWrL4kyZGGU5xfiGALO/Vxd5DOi', 'admin', 1);
insert into member (username, password, nickname, activated) values ('member', '$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC', 'member', 1);

insert into authority (authority_name) values ('ROLE_USER');
insert into authority (authority_name) values ('ROLE_ADMIN');

insert into user_authority (user_id, authority_name) values (1, 'ROLE_USER');
insert into user_authority (user_id, authority_name) values (1, 'ROLE_ADMIN');
insert into user_authority (user_id, authority_name) values (2, 'ROLE_USER');
insert into bank(name, number) values ('은행', 1);
insert into account(number, bank_id, member_user_id, balance) values('1234', 1, 1, 1000);
insert into account(number, bank_id, member_user_id, balance) values('1235', 1, 2, 1000);
insert into trade(type, date_time, account_id, amount) values('DEPOSIT', '2023-07-01T11:25:00', 1, 1000);
insert into trade(type, date_time, account_id, amount) values('WITHDRAW', '2023-07-02T01:25:30', 1, 1000);
insert into trade(type, date_time, account_id, amount) values('PAYMENT', '2023-07-03T17:55:00', 1, 1000);
insert into stock(balance, name, price) values(100, '백만전자', 100);