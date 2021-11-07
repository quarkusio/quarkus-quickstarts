package org.acme.awt.rest;

import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Path("/watermark")
public class ImageResource {

    @Inject
    Application application;

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response image(MultipartFormDataInput data) throws IOException {
        final InputStream inputStream = data.getFormDataMap().get("image").get(0).getBody(InputStream.class, null);
        if(inputStream == null ) {
            throw  new IllegalArgumentException();
        }
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        // Defaults to PNG, handles transparency
        ImageIO.write(watermarkImage(inputStream), "PNG", bos);
        return Response
                .accepted()
                .type(MediaType.APPLICATION_OCTET_STREAM_TYPE)
                .entity(bos.toByteArray())
                .build();
    }

    /**
     * Resizes a given image to the biggest of
     * given height, width values, keeping aspect ratio.
     *
     * @param img      image to be resized
     * @param heightX, desired height or 0
     * @param widthY,  desired width or 0
     * @return resized image
     */
    private static BufferedImage resizeWatermark(BufferedImage img, int heightX, int widthY, float transparency) {
        if (heightX < 1 && widthY < 1) {
            // no op
            return img;
        }
        final int currentW = img.getWidth();
        final int currentH = img.getHeight();
        int width, height;
        if (heightX >= widthY) {
            width = currentW * heightX / currentH;
            height = heightX;
        } else {
            width = widthY;
            height = currentH * widthY / currentW;
        }
        final BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        final Graphics2D g = (Graphics2D) resizedImage.getGraphics();
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, transparency));
        g.drawImage(img.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);
        return resizedImage;
    }

    private BufferedImage watermarkImage(InputStream in) throws IOException {
        final BufferedImage img = ImageIO.read(in);
        if (img == null || img.getWidth() < 5 || img.getHeight() < 5) {
            // no op
            return img;
        }
        final Graphics2D g = (Graphics2D) img.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw watermark image
        BufferedImage watermark;
        if (img.getHeight() >= img.getWidth()) {
            watermark = resizeWatermark(application.quarkusIco(), 0, img.getWidth() / 3, 0.5f);
        } else {
            watermark = resizeWatermark(application.quarkusIco(), img.getHeight() / 3, 0, 0.5f);
        }
        img.getGraphics().drawImage(watermark,
                img.getWidth() - watermark.getWidth(), img.getHeight() - watermark.getHeight(), null);

        // Draw watermark text
        final int wCenter = img.getWidth() / 2;
        final int hCenter = img.getHeight();
        final AffineTransform originalMatrix = g.getTransform();
        final AffineTransform af = AffineTransform.getRotateInstance(Math.toRadians(4), wCenter, hCenter);
        g.setColor(Color.PINK);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
        // Name of the font is not just the name of the file. It is baked in it.
        // The size is hardcoded for brevity, some proportional scaling
        // with getFontMetrics and getStringBounds might be in order.
        g.setFont(new Font("MyFreeMono", Font.PLAIN, 30));
        g.drawString("Mandrel", 20, 22);
        g.transform(af);
        g.setFont(new Font("MyFreeSerif", Font.PLAIN, 30));
        g.drawString("Mandrel", 20, 72);
        g.transform(af);
        g.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 30));
        g.drawString("Mandrel", 20, 122);
        g.setTransform(originalMatrix);
        g.dispose();
        return img;
    }

}
