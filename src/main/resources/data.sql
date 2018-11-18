/*Populate table CLIENT*/
insert into client (name, phone)
values ('Ymir', '8098-777-1111'),
  ('Odin', '8098-777-1112'),
  ('Frigg', '8098-777-1113'),
  ('Thor', '8098-777-1114'),
  ('Balder', '8098-777-1115'),
  ('Tyr', '8098-777-1116'),
  ('Bragi', '8098-777-1117'),
  ('Loki', '8098-777-1118'),
  ('Hel', '8098-777-1119'),
  ('Heimdall', '8098-777-1120'),
  ('Freyr', '8098-777-1121'),
  ('Freya', '8098-777-1122');

/*Populate table CLIENT_ACCOUNT*/
insert into client_account (amount, owner)
values (123, 1),
  (456, 1),
  (1237, 3),
  (4564, 2),
  (500, 4),
  (877, 4),
  (965, 10),
  (149, 12);

/*Populate table BANK_ACCOUNT*/
insert into bank_account (amount)
values (50000);
