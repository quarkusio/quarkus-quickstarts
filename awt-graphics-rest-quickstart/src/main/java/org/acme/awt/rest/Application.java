package org.acme.awt.rest;

import io.quarkus.runtime.Startup;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.imageio.ImageIO;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

@Startup
@ApplicationScoped
public class Application {

    private BufferedImage quarkusIco = null;

    @PostConstruct
    public void init() throws IOException, FontFormatException {
        final GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        // Original font source: https://ftp.gnu.org/gnu/freefont/
        // Application packages them locally in ./src/main/resources
        ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(
                Application.class.getResourceAsStream("/MyFreeMono.ttf"),
                "MyFreeMono.ttf not found.")));
        ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(
                Application.class.getResourceAsStream("/MyFreeSerif.ttf"),
                "MyFreeSerif.ttf not found.")));
        quarkusIco = ImageIO.read(Objects.requireNonNull(
                Application.class.getResourceAsStream("/quarkus-icon.png"),
                "quarkus-icon.png was not found. Watermarking will not work."));
    }

    public BufferedImage quarkusIco() {
        return quarkusIco;
    }
}
