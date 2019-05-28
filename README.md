# payments-service

An API implementation simulating money between two accounts. 
The application is initialised with two accounts; account '1' and '2', each containing 4000 pence. 
Currency is passed in pence to prevent loss of precision. 


###### Build & Run

Build the application by doing: (Will start application on localhost:9999 as part of end-to-end tests)
```
./gradlew build
```

Run built jar:
```
java -jar build/libs/payments-service-1.0.jar
```

###### API Example

```
POST
http://localhost:9999/payment
{
    "creditingAccountNumber": "1",
    "debitingAccountNumber": "2",
    "amount": 100
}
```

###### Technologies Used

* Google Guice
* Java 8
* Spark Java
* H2 DB
* Google Gson
* Jackson
* JUnit
* Mockito
* REST Assured
