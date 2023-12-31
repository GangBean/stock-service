insert into member (username, password, nickname) values ('admin', '$2a$08$lDnHPz7eUkSi6ao14Twuau08mzhWrL4kyZGGU5xfiGALO/Vxd5DOi', 'admin');
insert into member (username, password, nickname) values ('member', '$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC', 'member');

insert into authority (authority_name) values ('ROLE_USER');
insert into authority (authority_name) values ('ROLE_ADMIN');

insert into member_authority (member_id, authority_name) values (1, 'ROLE_USER');
insert into member_authority (member_id, authority_name) values (1, 'ROLE_ADMIN');
insert into member_authority (member_id, authority_name) values (2, 'ROLE_USER');

insert into bank(name, number) values ('은행', 1);

insert into account(number, bank_id, member_id, balance) values('1234', 1, 1, 1000);
insert into account(number, bank_id, member_id, balance) values('1235', 1, 2, 1000);

insert into trade(type, date_time, amount) values('DEPOSIT', '2023-07-01T11:25:00', 1000);
insert into trade(type, date_time, amount) values('WITHDRAW', '2023-07-02T01:25:30', 1000);
insert into trade(type, date_time, amount) values('PAYMENT', '2023-07-03T17:55:00', 1000);

insert into stock(balance, name, price) values(100, '백만전자', 100);
insert into stock(balance, name, price) values(100, '십만전자', 100);
insert into stock(id, balance, name, price) values(10, 100, '천만전자', 100);

insert into account_trades(account_id, trades_id) values(1, 1);
insert into account_trades(account_id, trades_id) values(1, 2);
insert into account_trades(account_id, trades_id) values(1, 3);

insert into account_stock(balance, price, total_paid, stock_id) values(10000, 1000, 10000000, 1);
insert into account_stocks(account_id, stocks_id) values (1, 1);

insert into stock_history(price, written_at) values (80, '2023-07-01T01:00:00');
insert into stock_history(price, written_at) values (90, '2023-07-01T02:00:00');
insert into stock_history(price, written_at) values (100, '2023-07-01T03:00:00');
insert into stock_history(price, written_at) values (90, '2023-07-01T04:00:00');
insert into stock_history(price, written_at) values (90, '2023-07-01T05:00:00');
insert into stock_history(price, written_at) values (90, '2023-07-01T06:00:00');
insert into stock_history(price, written_at) values (90, '2023-07-01T07:00:00');
insert into stock_history(price, written_at) values (95, '2023-07-01T08:00:00');
insert into stock_history(price, written_at) values (90, '2023-07-01T09:00:00');
insert into stock_history(price, written_at) values (90, '2023-07-01T10:00:00');
insert into stock_history(price, written_at) values (90, '2023-07-01T11:00:00');
insert into stock_history(price, written_at) values (90, '2023-07-01T12:00:00');
insert into stock_history(price, written_at) values (90, '2023-07-01T13:00:00');
insert into stock_history(price, written_at) values (90, '2023-07-01T14:00:00');
insert into stock_history(price, written_at) values (90, '2023-07-01T15:00:00');
insert into stock_history(price, written_at) values (90, '2023-07-01T16:00:00');
insert into stock_history(price, written_at) values (90, '2023-07-01T17:00:00');
insert into stock_history(price, written_at) values (90, '2023-07-01T18:00:00');
insert into stock_history(price, written_at) values (90, '2023-07-01T19:00:00');
insert into stock_history(price, written_at) values (90, '2023-07-01T20:00:00');
insert into stock_history(price, written_at) values (90, '2023-07-01T21:00:00');
insert into stock_history(price, written_at) values (90, '2023-07-01T22:00:00');
insert into stock_history(price, written_at) values (90, '2023-07-01T23:00:00');
insert into stock_history(price, written_at) values (90, '2023-07-02T00:00:00');
insert into stock_history(price, written_at) values (90, '2023-07-02T01:00:00');
insert into stock_history(price, written_at) values (91, '2023-07-02T02:00:00');
insert into stock_history(price, written_at) values (90, '2023-07-02T03:00:00');
insert into stock_history(price, written_at) values (90, '2023-07-02T04:00:00');
insert into stock_history(price, written_at) values (90, '2023-07-02T05:00:00');
insert into stock_history(price, written_at) values (90, '2023-07-02T06:00:00');
insert into stock_history(price, written_at) values (90, '2023-07-02T07:00:00');
insert into stock_history(price, written_at) values (90, '2023-07-02T08:00:00');
insert into stock_history(price, written_at) values (90, '2023-07-02T09:00:00');
insert into stock_history(price, written_at) values (90, '2023-07-02T10:00:00');
insert into stock_history(price, written_at) values (90, '2023-07-02T11:00:00');
insert into stock_history(price, written_at) values (90, '2023-07-02T12:00:00');
insert into stock_history(price, written_at) values (90, '2023-07-02T13:00:00');
insert into stock_history(price, written_at) values (90, '2023-07-02T14:00:00');
insert into stock_history(price, written_at) values (90, '2023-07-02T15:00:00');
insert into stock_history(price, written_at) values (90, '2023-07-02T16:00:00');
insert into stock_history(price, written_at) values (90, '2023-07-02T17:00:00');
insert into stock_history(price, written_at) values (90, '2023-07-02T18:00:00');
insert into stock_history(price, written_at) values (90, '2023-07-02T19:00:00');
insert into stock_history(price, written_at) values (90, '2023-07-02T20:00:00');
insert into stock_history(price, written_at) values (90, '2023-07-02T21:00:00');
insert into stock_history(price, written_at) values (90, '2023-07-02T22:00:00');
insert into stock_history(price, written_at) values (90, '2023-07-02T23:00:00');
insert into stock_history(price, written_at) values (90, '2023-07-03T00:00:00');

insert into stock_history(price, written_at) values (105, '2023-07-03T01:00:00');
insert into stock_history(price, written_at) values (91, '2023-07-03T02:00:00');
insert into stock_history(price, written_at) values (92, '2023-07-03T03:00:00');
insert into stock_history(price, written_at) values (90, '2023-07-03T04:00:00');
insert into stock_history(price, written_at) values (98, '2023-07-03T05:00:00');
insert into stock_history(price, written_at) values (92, '2023-07-03T06:00:00');
insert into stock_history(price, written_at) values (83, '2023-07-03T07:00:00');
insert into stock_history(price, written_at) values (70, '2023-07-03T08:00:00');
insert into stock_history(price, written_at) values (85, '2023-07-03T09:00:00');
insert into stock_history(price, written_at) values (91, '2023-07-03T10:00:00');
insert into stock_histories(stock_id, histories_id) values (1, 1);
insert into stock_histories(stock_id, histories_id) values (1, 2);
insert into stock_histories(stock_id, histories_id) values (1, 3);
insert into stock_histories(stock_id, histories_id) values (1, 4);
insert into stock_histories(stock_id, histories_id) values (1, 5);
insert into stock_histories(stock_id, histories_id) values (1, 6);
insert into stock_histories(stock_id, histories_id) values (1, 7);
insert into stock_histories(stock_id, histories_id) values (1, 8);
insert into stock_histories(stock_id, histories_id) values (1, 9);
insert into stock_histories(stock_id, histories_id) values (1, 10);
insert into stock_histories(stock_id, histories_id) values (1, 11);
insert into stock_histories(stock_id, histories_id) values (1, 12);
insert into stock_histories(stock_id, histories_id) values (1, 13);
insert into stock_histories(stock_id, histories_id) values (1, 14);
insert into stock_histories(stock_id, histories_id) values (1, 15);
insert into stock_histories(stock_id, histories_id) values (1, 16);
insert into stock_histories(stock_id, histories_id) values (1, 17);
insert into stock_histories(stock_id, histories_id) values (1, 18);
insert into stock_histories(stock_id, histories_id) values (1, 19);
insert into stock_histories(stock_id, histories_id) values (1, 20);
insert into stock_histories(stock_id, histories_id) values (1, 21);
insert into stock_histories(stock_id, histories_id) values (1, 22);
insert into stock_histories(stock_id, histories_id) values (1, 23);
insert into stock_histories(stock_id, histories_id) values (1, 24);
insert into stock_histories(stock_id, histories_id) values (1, 25);
insert into stock_histories(stock_id, histories_id) values (1, 26);
insert into stock_histories(stock_id, histories_id) values (1, 27);
insert into stock_histories(stock_id, histories_id) values (1, 28);
insert into stock_histories(stock_id, histories_id) values (1, 29);
insert into stock_histories(stock_id, histories_id) values (1, 30);
insert into stock_histories(stock_id, histories_id) values (1, 31);
insert into stock_histories(stock_id, histories_id) values (1, 32);
insert into stock_histories(stock_id, histories_id) values (1, 33);
insert into stock_histories(stock_id, histories_id) values (1, 34);
insert into stock_histories(stock_id, histories_id) values (1, 35);
insert into stock_histories(stock_id, histories_id) values (1, 36);
insert into stock_histories(stock_id, histories_id) values (1, 37);
insert into stock_histories(stock_id, histories_id) values (1, 38);
insert into stock_histories(stock_id, histories_id) values (1, 39);
insert into stock_histories(stock_id, histories_id) values (1, 40);
insert into stock_histories(stock_id, histories_id) values (1, 41);
insert into stock_histories(stock_id, histories_id) values (1, 42);
insert into stock_histories(stock_id, histories_id) values (1, 43);
insert into stock_histories(stock_id, histories_id) values (1, 44);
insert into stock_histories(stock_id, histories_id) values (1, 45);
insert into stock_histories(stock_id, histories_id) values (1, 46);
insert into stock_histories(stock_id, histories_id) values (1, 47);
insert into stock_histories(stock_id, histories_id) values (1, 48);
insert into stock_histories(stock_id, histories_id) values (1, 49);
insert into stock_histories(stock_id, histories_id) values (1, 50);
insert into stock_histories(stock_id, histories_id) values (1, 51);
insert into stock_histories(stock_id, histories_id) values (1, 52);
insert into stock_histories(stock_id, histories_id) values (1, 53);
insert into stock_histories(stock_id, histories_id) values (1, 54);
insert into stock_histories(stock_id, histories_id) values (1, 55);
insert into stock_histories(stock_id, histories_id) values (1, 56);
insert into stock_histories(stock_id, histories_id) values (1, 57);
insert into stock_histories(stock_id, histories_id) values (1, 58);