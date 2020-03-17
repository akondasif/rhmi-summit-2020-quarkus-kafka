Quarkus Kafka Quickstart
========================

This project illustrates how you can interact with Apache Kafka using MicroProfile Reactive Messaging.

## Kafka Cluster

First you need a Kafka cluster. You can follow the instructions from the [Apache Kafka web site](https://kafka.apache.org/quickstart) or run `docker-compose up` if you have docker installed on your machine.

Another application should write JSON transaction objects in the following format to a `transactions` topic:

```
{
    "id": "0d64e882-a59b-43d7-a468-89c122fd37ee",
    "givenname": "Jane",
    "surname": "Doe",
    "zip": "47420-7769",
    "amount": "378.00",
    "timestamp": "2020-03-06T07:33:33.796Z"
}
```

## Start this Quarkus Application

The application can be started using:

```bash
mvn quarkus:dev
```

Then, open your browser to `http://localhost:8080/index.html`, and you should see transactions rendered in a table.

## Anatomy

In addition to the `index.html` page, the application is composed by 3 components:

* `TransactionConverter` - on the consuming side, the `TransactionConverter` receives the Kafka message and convert the Transaction.
The result is sent to an in-memory stream of data
* `TransactionResource`  - the `TransactionResource` retrieves the in-memory stream of data in which the converted Transactions are sent and send these Transactions to the browser using Server-Sent Events.

The interaction with Kafka is managed by MicroProfile Reactive Messaging.
The configuration is located in the application configuration.

## Running in Native Mode

You can compile the application into a native binary using:

`mvn clean package -Pnative`

and run with:

`./target/kafka-quickstart-1.0-SNAPSHOT-runner`
