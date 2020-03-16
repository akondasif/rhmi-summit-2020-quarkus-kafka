package org.acme.quarkus.sample;

import io.smallrye.reactive.messaging.annotations.Broadcast;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import io.vertx.core.json.JsonObject;
import javax.enterprise.context.ApplicationScoped;

/**
 * A bean consuming data from the "transactions" Kafka topic and applying some conversion.
 * The result is pushed to the "my-data-stream" stream which is an in-memory stream.
 */
@ApplicationScoped
public class TransactionConverter {

    /**
     * Example of the JSON we perform a conversion on:
     * {
        "id": "0d64e882-a59b-43d7-a468-89c122fd37ee",
        "givenname": "Annetta",
        "surname": "Cronin",
        "zip": "47420-7769",
        "amount": "378.00",
        "timestamp": "2020-03-06T07:33:33.796Z"
       }
     */

    private static final double EUR_CONVERSION_RATE = 0.09;

    @Incoming("transactions")
    @Outgoing("my-data-stream")
    @Broadcast
    public String process(String incomingTx) {
        JsonObject txJson = new JsonObject(incomingTx);

        double txAmount = txJson.getDouble("amount");
        double txEuroAmt = txAmount * EUR_CONVERSION_RATE;

        txJson.put("euros", txEuroAmt);

        String outTxJson = txJson.encode();

        return outTxJson;
    }

}
