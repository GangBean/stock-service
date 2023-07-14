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

insert into trade(type, date_time, amount) values('DEPOSIT', '2023-07-01T11:25:00', 1000);
insert into trade(type, date_time, amount) values('WITHDRAW', '2023-07-02T01:25:30', 1000);
insert into trade(type, date_time, amount) values('PAYMENT', '2023-07-03T17:55:00', 1000);

insert into stock(balance, name, price) values(10000, 'XX전자', 70000);
insert into stock(id, balance, name, price) values(100, 10000, '컴퓨터회사', 100000);
insert into stock(id, balance, name, price) values(101, 10000, '전기차회사', 1000000);
insert into stock(id, balance, name, price) values(102, 10000, '음료회사', 2500);
insert into stock(id, balance, name, price) values(103, 10000, '구멍가게', 1100);

insert into account_trades(account_id, trades_id) values(1, 1);
insert into account_trades(account_id, trades_id) values(1, 2);
insert into account_trades(account_id, trades_id) values(1, 3);

insert into account_stock(balance, price, total_paid, account_id, stock_id) values(10000, 1000, 10000000, 1, 1);
insert into stock_history(price, written_at) values (80, '2023-07-01T13:00:00');
insert into stock_history(price, written_at) values (90, '2023-07-01T14:00:00');
insert into stock_histories(stock_id, histories_id) values (1, 1);
insert into stock_histories(stock_id, histories_id) values (1, 2);