package org.acme.awt.rest;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class ImageResourceTest {

    @Test
    public void testWatermarkJPG() throws IOException {
        final byte[] imgBytes = given()
                .multiPart("image", new File(ImageResourceTest.class.getResource("/test-image-503x417.jpg").getFile()))
                .when()
                .post("/watermark")
                .asByteArray();
        final BufferedImage image = ImageIO.read(new ByteArrayInputStream(imgBytes));
        Assertions.assertNotNull(image, "The image returned is not a valid, known format, e.g. PNG");
        Assertions.assertTrue(image.getWidth() == 503 && image.getHeight() == 417,
                String.format("Image's expected dimension is %d x %d, but was %d x %d.",
                        503, 417, image.getWidth(), image.getHeight()));
        final int[] pixel = new int[4]; //4BYTE RGBA
        image.getData().getPixel(418, 314, pixel);
        Assertions.assertTrue(pixel[0] > 100, "There should have been more red. Watermark failed.");
        image.getData().getPixel(67, 49, pixel);
        Assertions.assertTrue(pixel[0] > 100, "There should have been more red. Watermark failed.");
    }

    @Test
    public void testWatermarkPNG() throws IOException {
        final byte[] imgBytes = given()
                .multiPart("image", new File(ImageResourceTest.class.getResource("/test-image-836x379.png").getFile()))
                .when()
                .post("/watermark")
                .asByteArray();
        final BufferedImage image = ImageIO.read(new ByteArrayInputStream(imgBytes));
        Assertions.assertNotNull(image, "The image returned is not a valid, known format, e.g. PNG");
        Assertions.assertTrue(image.getWidth() == 836 && image.getHeight() == 379,
                String.format("Image's expected dimension is %d x %d, but was %d x %d.",
                        836, 379, image.getWidth(), image.getHeight()));
        final int[] pixel = new int[4]; //4BYTE RGBA
        image.getData().getPixel(800, 311, pixel);
        Assertions.assertTrue(pixel[0] > 100, "There should have been more red. Watermark failed.");
        image.getData().getPixel(770, 366, pixel);
        Assertions.assertTrue(pixel[2] > 100, "There should have been more blue. Watermark failed.");
        image.getData().getPixel(65, 51, pixel);
        Assertions.assertTrue(pixel[0] > 100, "There should have been more red. Watermark failed.");
    }

}
