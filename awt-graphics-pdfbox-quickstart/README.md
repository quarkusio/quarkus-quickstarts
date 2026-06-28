# Web service to convert PDF to PNG

This quickstart demonstrates a use case for [Quarkus AWT extension](https://github.com/quarkusio/quarkus/tree/main/extensions/awt) and [Apache PDFBox](https://github.com/apache/pdfbox).

There is a single POST endpoint that consumes a PDF file as a multipart form data
and returns an octet stream with a PNG image of the PDF's first page.

[Quarkus AWT extension](https://github.com/quarkusio/quarkus/tree/main/extensions/awt) enables a set of
ImageIO and AWT functionality in Quarkus Native images that is essential for Apache PDFBox rendering.
See the extension documentation and tests to learn the available scope.
Given the nature of native libraries in JDK implementing various image processing algorithms,
venturing outside the tested scope might result in native image build time or runtime failure.

# Additional system dependencies
Note `microdnf` command installing `fontconfig` library in [Dockerfile.jvm](./src/main/docker/Dockerfile.jvm)
and [Dockerfile.legacy-jar](./src/main/docker/Dockerfile.legacy-jar) to support jvm mode. 
Both `freetype` and `fontconfig` libraries are needed for native mode in [Dockerfile.native](./src/main/docker/Dockerfile.native).

# Usage with curl

e.g.

```bash
curl -F "pdf=@/tmp/test-pdf.pdf" http://localhost:8080/pdf2png --output /tmp/result.png;
```

Converts the first page of the given PDF to a PNG file and returns it.

# Usage with a client code

See [PdfBoxResourceTest.java](./src/test/java/org/acme/awt/rest/PdfBoxResourceTest.java). The test is executed
in native mode with:

```bash
./mvnw clean verify -Pnative
```
To run native tests locally, a JDK 11.0.16+ with Mandrel (or GraalVM) 21.3+ is required.
Additionally, `freetype-devel` and `fontconfig` libraries must be installed. 

# What the result looks like

You can use the attached [test-pdf.pdf](./src/test/resources/test-pdf.pdf) and see how it converts to the image below:

![Alt text](./doc/example.png)
