package org.acme.kms;

import java.util.Base64;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.smallrye.mutiny.Uni;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.kms.KmsAsyncClient;
import software.amazon.awssdk.services.kms.model.DecryptResponse;
import software.amazon.awssdk.services.kms.model.EncryptResponse;

@Path("/async")
@Produces(MediaType.TEXT_PLAIN)
@Consumes(MediaType.TEXT_PLAIN)
public class QuarkusKmsAsyncResource {

    @Inject
    KmsAsyncClient kms;

    @ConfigProperty(name = "key.arn")
    String keyArn;

    @POST
    @Path("/encrypt")
    public Uni<String> encrypt(String data) {
        return Uni.createFrom().completionStage(kms.encrypt(req -> req.keyId(keyArn).plaintext(SdkBytes.fromUtf8String(data))))
            .onItem().transform(EncryptResponse::ciphertextBlob)
            .onItem().transform(blob -> Base64.getEncoder().encodeToString(blob.asByteArray()));
    }

    @POST
    @Path("/decrypt")
    public Uni<String> decrypt(String data) {
        return Uni.createFrom().item(SdkBytes.fromByteArray(Base64.getDecoder().decode(data.getBytes())))
            .onItem().transformToUni(msg ->
                        Uni.createFrom().completionStage(kms.decrypt(req -> req.keyId(keyArn).ciphertextBlob(msg)))
            )
            .onItem().transform(DecryptResponse::plaintext)
            .onItem().transform(SdkBytes::asUtf8String);
    }
}