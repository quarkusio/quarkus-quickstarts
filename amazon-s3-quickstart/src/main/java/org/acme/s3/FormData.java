package org.acme.s3;

import java.io.InputStream;

import javax.ws.rs.FormParam;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.annotations.providers.multipart.PartType;

import static javax.ws.rs.core.MediaType.APPLICATION_OCTET_STREAM;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;

public class FormData {

    @FormParam("file")
    @PartType(APPLICATION_OCTET_STREAM)
    public InputStream data;

    @FormParam("filename")
    @PartType(TEXT_PLAIN)
    public String fileName;

    @FormParam("mimetype")
    @PartType(TEXT_PLAIN)
    public String mimeType;

}
