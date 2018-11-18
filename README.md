# Spring Boot Simple Bank (Web, JPA, H2, Lombok)

1.Выпуск электронных денег. При вызове в таблице bank_account увеличивается сумма.

PUT http://localhost:8080/bank/emission

Example (application/json):
{
    "amount": 44
}

2.Покупка денег. Указав номер счета и сумму покупки, деньги переводятся со счета финучреждения на указанный счет клиента.

POST http://localhost:8080/bank/buying

Example (application/json):
{
    "account": 1,
    "amount": 100
}

3.Перевод средств с одного счета на другой счет. 

POST http://localhost:8080/account/transfer

Example (application/json):
{
    "sender": 2,
    "recipient": 6,
    "amount": 10
}

4.Получение истории всех переводов по карте(за все время или за период).

GET http://localhost:8080/log/card/{cardId}

GET http://localhost:8080/log/datecard/{cardId}?from={date}&to={date}

5.Получение истории всех переводов клиента(за все время или за период).

GET http://localhost:8080/log/client/{name}
G
ET http://localhost:8080/log/dateclient/{name}?from={date}&to={date}
