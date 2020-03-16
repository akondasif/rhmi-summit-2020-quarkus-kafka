package org.acme.quarkus.sample;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.awaitility.Awaitility.await;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.sse.SseEventSource;

import org.junit.jupiter.api.Test;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
@QuarkusTestResource(KafkaResource.class)
class TransactionResourceTest {

    private static final String TX_SSE_ENDPOINT = "http://localhost:8081/transactions/stream";

    @Test
    void testTransactionsEventStream() {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(TX_SSE_ENDPOINT);

        List<Double> received = new CopyOnWriteArrayList<>();

        SseEventSource source = SseEventSource.target(target).build();
        source.register(inboundSseEvent -> received.add(Double.valueOf(inboundSseEvent.readData())));
        source.open();
        await().atMost(100000, MILLISECONDS).until(() -> received.size() == 3);
        source.close();
    }
}
