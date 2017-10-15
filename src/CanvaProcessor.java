import javafx.scene.canvas.Canvas;
import javafx.scene.image.*;

public class CanvaProcessor {
    private int b, g, r, width, height;
    private double f;
    byte[] pixels;

    public WritableImage setContrast(Canvas canvas, int contrast) {
        width = (int) canvas.getWidth();
        height = (int) canvas.getHeight();

        f = (259.0 * ((double) contrast + 255.0)) / (255.0 * (259.0 - (double) contrast));

        if (pixels == null || pixels.length != width * height * 4) {
            pixels = new byte[width * height * 4];
        }
        WritableImage bufImg = canvas.snapshot(null, null);
        PixelReader pr = bufImg.getPixelReader();
        pr.getPixels(0, 0, width, height, PixelFormat.getByteBgraInstance(), pixels, 0, 4 * width);
        int p = 0;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                b = pixels[p] & 0xff;
                g = pixels[p + 1] & 0xff;
                r = pixels[p + 2] & 0xff;

                b = (int) (f * (b - 128) + 128);
                g = (int) (f * (g - 128) + 128);
                r = (int) (f * (r - 128) + 128);

                if (b > 255) {
                    b = 255;
                }
                if (g > 255) {
                    g = 255;
                }
                if (r > 255) {
                    r = 255;
                }
                if (b < 0) {
                    b = 0;
                }
                if (g < 0) {
                    g = 0;
                }
                if (r < 0) {
                    r = 0;
                }

                pixels[p] = (byte) (b & 0xff);
                pixels[p + 1] = (byte) (g & 0xff);
                pixels[p + 2] = (byte) (r & 0xff);
                p += 4;
            }
        }
        PixelWriter pw = bufImg.getPixelWriter();
        pw.setPixels(0, 0, width, height, PixelFormat.getByteBgraInstance(), pixels, 0, 4 * width);
        return bufImg;
    }

    public WritableImage showSkinOnly(Canvas canvas) {
        width = (int) canvas.getWidth();
        height = (int) canvas.getHeight();

        if (pixels == null || pixels.length != width * height * 4) {
            pixels = new byte[width * height * 4];
        }
        WritableImage bufImg = canvas.snapshot(null, null);
        PixelReader pr = bufImg.getPixelReader();
        pr.getPixels(0, 0, width, height, PixelFormat.getByteBgraInstance(), pixels, 0, 4 * width);
        int p = 0;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                b = pixels[p] & 0xff;
                g = pixels[p + 1] & 0xff;
                r = pixels[p + 2] & 0xff;

                if (!(r > g && g > b)) {
                    b = g = r = 0;
                }

                pixels[p] = (byte) (b & 0xff);
                pixels[p + 1] = (byte) (g & 0xff);
                pixels[p + 2] = (byte) (r & 0xff);
                p += 4;
            }
        }
        PixelWriter pw = bufImg.getPixelWriter();
        pw.setPixels(0, 0, width, height, PixelFormat.getByteBgraInstance(), pixels, 0, 4 * width);
        return bufImg;
    }
}
