package org.acme.tika;

import java.io.InputStream;
import java.time.Duration;
import java.time.Instant;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import org.jboss.logging.Logger;

import io.quarkus.tika.TikaParser;

@Path("/parse")
public class TikaParserResource {
    private static final Logger log = Logger.getLogger(TikaParserResource.class);

    @Inject
    TikaParser parser;

    @POST
    @Path("/text")
    @Consumes({ "application/pdf", "application/vnd.oasis.opendocument.text" })
    @Produces(MediaType.TEXT_PLAIN)
    public String extractText(InputStream stream) {
        Instant start = Instant.now();

        String text = parser.getText(stream);

        Instant finish = Instant.now();

        log.info(Duration.between(start, finish).toMillis() + " mls have passed");

        return text;
    }
}
