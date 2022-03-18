package org.acme.s3;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.jboss.resteasy.reactive.MultipartForm;

import io.smallrye.mutiny.Uni;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.core.async.AsyncResponseTransformer;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsResponse;

@Path("/async-s3")
public class S3AsyncClientResource extends CommonResource {
    @Inject
    S3AsyncClient s3;

    @POST
    @Path("upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Uni<Response> uploadFile(@MultipartForm FormData formData) throws Exception {

        if (formData.filename == null || formData.filename.isEmpty()) {
            return Uni.createFrom().item(Response.status(Status.BAD_REQUEST).build());
        }

        if (formData.mimetype == null || formData.mimetype.isEmpty()) {
            return Uni.createFrom().item(Response.status(Status.BAD_REQUEST).build());
        }

        return Uni.createFrom()
                .completionStage(() -> {
                    return s3.putObject(buildPutRequest(formData), AsyncRequestBody.fromFile(formData.data));
                })
                .onItem().ignore().andSwitchTo(Uni.createFrom().item(Response.created(null).build()))
                .onFailure().recoverWithItem(th -> {
                    th.printStackTrace();
                    return Response.serverError().build();
                });
    }

    @GET
    @Path("download/{objectKey}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Uni<Response> downloadFile(String objectKey) {
        return Uni.createFrom()
                .completionStage(() -> s3.getObject(buildGetRequest(objectKey), AsyncResponseTransformer.toBytes()))
                .onItem()
                .transform(object -> Response.ok(object.asUtf8String())
                        .header("Content-Disposition", "attachment;filename=" + objectKey)
                        .header("Content-Type", object.response().contentType()).build());
    }

    @GET
    public Uni<List<FileObject>> listFiles() {
        ListObjectsRequest listRequest = ListObjectsRequest.builder()
                .bucket(bucketName)
                .build();

        return Uni.createFrom().completionStage(() -> s3.listObjects(listRequest))
                .onItem().transform(result -> toFileItems(result));
    }

    private List<FileObject> toFileItems(ListObjectsResponse objects) {
        return objects.contents().stream()
                .map(FileObject::from)
                .sorted(Comparator.comparing(FileObject::getObjectKey))
                .collect(Collectors.toList());
    }
}
