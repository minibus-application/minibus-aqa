package org.minibus.aqa.core.helpers;

import io.appium.java_client.MobileElement;
import org.codehaus.commons.nullanalysis.NotNull;
import org.minibus.aqa.Constants;
import org.minibus.aqa.core.common.env.Device;
import org.openqa.selenium.OutputType;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class ImageProcessor {

    private static final int DEFAULT_THRESHOLD_VALUE = 170;

    public static BufferedImage thresholdImage(@NotNull final File imageFile) {
        try {
            BufferedImage imageToProcess = ImageIO.read(imageFile);
            Color color;
            int width = imageToProcess.getWidth();
            int height = imageToProcess.getHeight();
            int i, j;

            for (i = 0; i < height; i++) {
                for (j = 0; j < width; j++) {
                    Color c = new Color(imageToProcess.getRGB(j, i));
                    int red = c.getRed();
                    int green = c.getGreen();
                    int blue = c.getBlue();

                    if (((red + green + blue) / 3) < DEFAULT_THRESHOLD_VALUE || getPureColor(c.getRGB()) == ImageColor.GRAY) {
                        color = new Color(255, 255, 255);
                    } else {
                        color = new Color(0, 0, 0);
                    }

                    imageToProcess.setRGB(j, i, color.getRGB());
                }
            }

            return imageToProcess;
        } catch (Exception e) {
            throw new RuntimeException("An error while thresholding the image:\n" + e);
        }
    }

    public static boolean hasColor(MobileElement element, ImageColor color, ImageRegion region) {
        BufferedImage image = getElementBufferedImage(element);
        int width = image.getWidth();
        int height = image.getHeight();

        switch (region) {
            case TOP:
                image = cropImage(image, 0, 0, new Rectangle(width, height / 3));
                break;
            case BOTTOM:
                image = cropImage(image, 0, height * 2 / 3, new Rectangle(width, height / 3));
                break;
            case RIGHT:
                image = cropImage(image, width * 2 / 3, 0, new Rectangle(width / 3, height));
                break;
            case LEFT:
                image = cropImage(image, 0, 0, new Rectangle(width / 3, height));
                break;
            case CENTER:
                image = cropImage(image, width / 3, height / 3, new Rectangle(width / 3, height / 3));
                break;
        }

        return hasColor(image, color);
    }

    public static boolean hasColor(BufferedImage image, ImageColor color) {
        int width = image.getWidth();
        int height = image.getHeight();

        Color expectedColor = color.get();
        Color actualColor;

        int colorMatchesWithinImage = 0;
        int totalColoredPixels = width * height;

        for (int w = 0; w < width; w++) {
            for (int h = 0; h < height; h++) {
                actualColor = getPureColor(image.getRGB(w, h)).get();
                if (actualColor.getRGB() == expectedColor.getRGB()) {
                    colorMatchesWithinImage++;
                } else if (actualColor.getRGB() == Color.WHITE.getRGB()) {
                    totalColoredPixels--;
                }
            }
        }

        return colorMatchesWithinImage != 0 && (colorMatchesWithinImage >= totalColoredPixels / 2);
    }

    public static boolean hasColor(MobileElement element, ImageColor color) {
        return hasColor(getElementBufferedImage(element), color);
    }

    public static boolean hasColor(MobileElement element, String hexColor) {
        return hasColor(element, getPureColor(Color.decode(hexColor).getRGB()));
    }

    public static File getElementScreenshot(MobileElement element) {
        File screenshot = Device.getScreenshot();

        try {
            byte[] imageBytes = element.getScreenshotAs(OutputType.BYTES);
            BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(imageBytes));
            ImageIO.write(bufferedImage, Constants.PNG, screenshot);
        } catch (IOException e) {
            // Logger.get().info(e.getMessage());
            throw new RuntimeException(e);
        }

        return screenshot;
    }

    private static BufferedImage getElementBufferedImage(MobileElement element) {
        File screenshot = getElementScreenshot(element);
        BufferedImage bufferedImage;

        try {
            bufferedImage = ImageIO.read(screenshot);
        } catch (IOException e) {
            // Logger.get().info(e.getMessage());
            throw new RuntimeException(e);
        }

        return bufferedImage;
    }

    private static ImageColor getPureColor(int rgb) {
        float[] hsb = rgbToHsb(rgb);

        if (hsb[1] < 0.1 && hsb[2] > 0.9) return ImageColor.WHITE;
        else if (hsb[1] < 0.1 && (hsb[2] > 0.1 && hsb[2] < 0.9)) return ImageColor.GRAY;
        else if (hsb[2] < 0.1) return ImageColor.BLACK;
        else {
            float deg = hsb[0] * 360;
            if (deg >= 0 && deg < 20) return ImageColor.RED;
            else if (deg >= 20 && deg < 40) return ImageColor.ORANGE;
            else if (deg >= 40 && deg < 60) return ImageColor.YELLOW;
            else if (deg >= 60 && deg < 180) return ImageColor.GREEN;
            else if (deg >= 180 && deg < 190) return ImageColor.CYAN;
            else if (deg >= 190 && deg < 270) return ImageColor.BLUE;
            else if (deg >= 270 && deg < 285) return ImageColor.PURPLE;
            else if (deg >= 285 && deg < 330) return ImageColor.PINK;
            else return ImageColor.RED;
        }
    }

    private static float[] rgbToHsb(int rgb) {
        float[] hsb = new float[3];
        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;
        int b = (rgb) & 0xFF;
        return Color.RGBtoHSB(r, g, b, hsb);
    }

    private static BufferedImage cropImage(BufferedImage bufferedImage, int x, int y, Rectangle rectangle) {
        return bufferedImage.getSubimage(x, y, (int) rectangle.getWidth(), (int) rectangle.getHeight());
    }

    public enum ImageRegion {
        TOP, BOTTOM, RIGHT, LEFT, CENTER
    }

    public enum ImageColor {
        WHITE(Color.WHITE, "WHITE"),
        GRAY(Color.GRAY, "GRAY"),
        BLACK(Color.BLACK, "BLACK"),
        RED(Color.RED, "RED"),
        ORANGE(Color.ORANGE, "ORANGE"),
        YELLOW(Color.YELLOW, "YELLOW"),
        GREEN(Color.GREEN, "GREEN"),
        CYAN(Color.CYAN, "CYAN"),
        BLUE(Color.BLUE, "BLUE"),
        PURPLE(Color.MAGENTA, "PURPLE"),
        PINK(Color.PINK, "PINK");

        private final Color color;
        private final String name;

        ImageColor(Color color, String name) {
            this.color = color;
            this.name = name;
        }

        public Color get() {
            return color;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
