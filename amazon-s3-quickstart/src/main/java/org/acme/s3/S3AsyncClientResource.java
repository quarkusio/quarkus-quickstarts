package org.acme.s3;

import java.io.File;
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

import org.jboss.resteasy.annotations.jaxrs.PathParam;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import io.smallrye.mutiny.Uni;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.core.async.AsyncResponseTransformer;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsResponse;
import software.amazon.awssdk.services.s3.model.S3Object;

@Path("/async-s3")
public class S3AsyncClientResource extends CommonResource {
    @Inject
    S3AsyncClient s3;

    @POST
    @Path("upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Uni<Response> uploadFile(@MultipartForm FormData formData) throws Exception {

        if (formData.fileName == null || formData.fileName.isEmpty()) {
            return Uni.createFrom().item(Response.status(Status.BAD_REQUEST).build());
        }

        if (formData.mimeType == null || formData.mimeType.isEmpty()) {
            return Uni.createFrom().item(Response.status(Status.BAD_REQUEST).build());
        }

        return Uni.createFrom()
                .completionStage(() -> {
                    return s3.putObject(buildPutRequest(formData), AsyncRequestBody.fromFile(uploadToTemp(formData.data)));
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
    public Uni<Response> downloadFile(@PathParam("objectKey") String objectKey) throws Exception {
        File tempFile = tempFilePath();

        return Uni.createFrom()
                .completionStage(() -> s3.getObject(buildGetRequest(objectKey), AsyncResponseTransformer.toFile(tempFile)))
                .onItem()
                .apply(object -> Response.ok(tempFile)
                        .header("Content-Disposition", "attachment;filename=" + objectKey)
                        .header("Content-Type", object.contentType()).build());
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<List<FileObject>> listFiles() {
        ListObjectsRequest listRequest = ListObjectsRequest.builder()
                .bucket(bucketName)
                .build();

        return Uni.createFrom().completionStage(() -> s3.listObjects(listRequest))
                .onItem().transform(result -> toFileItems(result));
    }

    private List<FileObject> toFileItems(ListObjectsResponse objects) {
        return objects.contents().stream()
                .sorted(Comparator.comparing(S3Object::lastModified).reversed())
                .map(FileObject::from).collect(Collectors.toList());
    }
}
