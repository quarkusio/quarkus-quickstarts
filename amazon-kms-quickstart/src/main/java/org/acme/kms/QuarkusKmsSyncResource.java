package org.acme.kms;

import java.util.Base64;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.kms.KmsClient;
import software.amazon.awssdk.services.kms.model.DecryptResponse;

@Path("/sync")
@Produces(MediaType.TEXT_PLAIN)
@Consumes(MediaType.TEXT_PLAIN)
public class QuarkusKmsSyncResource {

    @Inject
    KmsClient kms;

    @ConfigProperty(name = "key.arn")
    String keyArn;

    @POST
    @Path("/encrypt")
    public String encrypt(String data) {
        SdkBytes encryptedBytes = kms.encrypt(req -> req.keyId(keyArn).plaintext(SdkBytes.fromUtf8String(data))).ciphertextBlob();

        return Base64.getEncoder().encodeToString(encryptedBytes.asByteArray());
    }

    @POST
    @Path("/decrypt")
    public String decrypt(String data) {
        SdkBytes encryptedData = SdkBytes.fromByteArray(Base64.getDecoder().decode(data.getBytes()));
        DecryptResponse decrypted = kms.decrypt(req -> req.keyId(keyArn).ciphertextBlob(encryptedData));

        return decrypted.plaintext().asUtf8String();
    }
}