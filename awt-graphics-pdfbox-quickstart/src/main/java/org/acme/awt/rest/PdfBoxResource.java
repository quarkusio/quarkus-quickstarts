package org.acme.awt.rest;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import javax.imageio.ImageIO;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Path("/pdf2png")
public class PdfBoxResource {

    /**
     * Takes the first page of a PDF file and returns a PNG picture of it.
     *
     * @param data "POST" field "pdf" must contain a valid PDF file binary
     * @return PNG binary
     */
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response pdf2png(MultipartFormDataInput data) throws IOException {
        final InputStream inputStream = data.getFormDataMap().get("pdf").get(0).getBody(InputStream.class, null);
        if (inputStream == null) {
            throw new IllegalArgumentException();
        }
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try (PDDocument doc = Loader.loadPDF(inputStream)) {
            final PDFRenderer renderer = new PDFRenderer(doc);
            ImageIO.write(renderer.renderImage(0), "PNG", bos);
        }
        return Response.accepted()
                .type(MediaType.APPLICATION_OCTET_STREAM_TYPE)
                .entity(bos.toByteArray()).build();
    }
}
