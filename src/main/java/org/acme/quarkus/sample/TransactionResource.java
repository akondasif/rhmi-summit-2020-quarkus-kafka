package org.acme.quarkus.sample;

import io.smallrye.reactive.messaging.annotations.Channel;
import org.jboss.resteasy.annotations.SseElementType;
import org.reactivestreams.Publisher;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * A simple resource retrieving the "in-memory" "my-data-stream" and sending the items to a server sent event.
 */
@Path("/transactions")
public class TransactionResource {

    @Inject
    @Channel("my-data-stream") Publisher<String> transactions;

    @GET
    @Path("/stream")
    @Produces(MediaType.SERVER_SENT_EVENTS) // denotes that server side events (SSE) will be produced
    @SseElementType("text/json") // denotes that the contained data, within this SSE, is just regular text/plain data
    public Publisher<String> stream() {
        return transactions;
    }
}
