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
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import org.jboss.resteasy.reactive.MultipartForm;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;


@Path("/s3")
public class S3SyncClientResource extends CommonResource {
    @Inject
    S3Client s3;

    @POST
    @Path("upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadFile(@MultipartForm FormData formData) throws Exception {

        if (formData.filename == null || formData.filename.isEmpty()) {
            return Response.status(Status.BAD_REQUEST).build();
        }

        if (formData.mimetype == null || formData.mimetype.isEmpty()) {
            return Response.status(Status.BAD_REQUEST).build();
        }

        PutObjectResponse putResponse = s3.putObject(buildPutRequest(formData),
                RequestBody.fromFile(formData.data));
        if (putResponse != null) {
            return Response.ok().status(Status.CREATED).build();
        } else {
            return Response.serverError().build();
        }
    }

    @GET
    @Path("download/{objectKey}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response downloadFile(String objectKey) {
        ResponseBytes<GetObjectResponse> objectBytes = s3.getObjectAsBytes(buildGetRequest(objectKey));
        ResponseBuilder response = Response.ok(objectBytes.asUtf8String());
        response.header("Content-Disposition", "attachment;filename=" + objectKey);
        response.header("Content-Type", objectBytes.response().contentType());
        return response.build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<FileObject> listFiles() {
        ListObjectsRequest listRequest = ListObjectsRequest.builder().bucket(bucketName).build();

        //HEAD S3 objects to get metadata
        return s3.listObjects(listRequest).contents().stream()
                .map(FileObject::from)
                .sorted(Comparator.comparing(FileObject::getObjectKey))
                .collect(Collectors.toList());
    }
}
