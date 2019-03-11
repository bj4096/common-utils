package com.sljl.core.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Hashtable;

/**
 * 二维码生成工具
 * 基于zxing实现,建议使用1.7版本，其他版本可能出现未知的问题
 *
 * @author L.Y.F
 */
public class QRCodeUtil {

    // 图片宽度的一般
    private static final int IMAGE_WIDTH = 100;
    private static final int IMAGE_HEIGHT = 100;
    private static final int IMAGE_HALF_WIDTH = IMAGE_WIDTH / 2;
    private static final int FRAME_WIDTH = 2;
    private static final int BLACK = 0xFF000000;
    private static final int WHITE = 0xFFFFFFFF;

    // 二维码写码器
    private static MultiFormatWriter mutiWriter = new MultiFormatWriter();

    /**
     * 得到二维码BufferedImage
     *
     * @param content 二维码显示的文本
     * @param width 二维码的宽度
     * @param height 二维码的高度
     * @param srcImagePath 中间嵌套的图片
     *
     * @return
     *
     * @throws WriterException
     * @throws IOException
     */
    public static BufferedImage genBarcode(String content, int width, int height, InputStream srcImagePath) throws WriterException, IOException {
        // 读取源图像
        BufferedImage scaleImage = scale1(srcImagePath, IMAGE_WIDTH, IMAGE_HEIGHT, false);

        int[][] srcPixels = new int[IMAGE_WIDTH][IMAGE_HEIGHT];
        for (int i = 0; i < scaleImage.getWidth(); i++) {
            for (int j = 0; j < scaleImage.getHeight(); j++) {
                srcPixels[i][j] = scaleImage.getRGB(i, j);
            }
        }

        Hashtable<EncodeHintType, Object> hint = new Hashtable<EncodeHintType, Object>();
        hint.put(EncodeHintType.CHARACTER_SET, "utf-8");
        hint.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        // 生成二维码
        BitMatrix matrix = mutiWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hint);

        // 二维矩阵转为一维像素数组
        int halfW = matrix.getWidth() / 2;
        int halfH = matrix.getHeight() / 2;
        int[] pixels = new int[width * height];

        // System.out.println(matrix.getHeight());
        for (int y = 0; y < matrix.getHeight(); y++) {
            for (int x = 0; x < matrix.getWidth(); x++) {
                // 读取图片
                if (x > halfW - IMAGE_HALF_WIDTH && x < halfW + IMAGE_HALF_WIDTH && y > halfH - IMAGE_HALF_WIDTH && y < halfH + IMAGE_HALF_WIDTH) {
                    pixels[y * width + x] = srcPixels[x - halfW + IMAGE_HALF_WIDTH][y - halfH + IMAGE_HALF_WIDTH];
                }
                // 在图片四周形成边框
                else if ((x > halfW - IMAGE_HALF_WIDTH - FRAME_WIDTH && x < halfW - IMAGE_HALF_WIDTH + FRAME_WIDTH && y > halfH - IMAGE_HALF_WIDTH - FRAME_WIDTH && y < halfH + IMAGE_HALF_WIDTH + FRAME_WIDTH) || (x > halfW + IMAGE_HALF_WIDTH - FRAME_WIDTH && x < halfW + IMAGE_HALF_WIDTH + FRAME_WIDTH && y > halfH - IMAGE_HALF_WIDTH - FRAME_WIDTH && y < halfH + IMAGE_HALF_WIDTH + FRAME_WIDTH) || (x > halfW - IMAGE_HALF_WIDTH - FRAME_WIDTH && x < halfW + IMAGE_HALF_WIDTH + FRAME_WIDTH && y > halfH - IMAGE_HALF_WIDTH - FRAME_WIDTH && y < halfH - IMAGE_HALF_WIDTH + FRAME_WIDTH) || (x > halfW - IMAGE_HALF_WIDTH - FRAME_WIDTH && x < halfW + IMAGE_HALF_WIDTH + FRAME_WIDTH && y > halfH + IMAGE_HALF_WIDTH - FRAME_WIDTH && y < halfH + IMAGE_HALF_WIDTH + FRAME_WIDTH)) {
                    pixels[y * width + x] = 0xfffffff;
                } else {
                    // 此处可以修改二维码的颜色，可以分别制定二维码和背景的颜色；
                    pixels[y * width + x] = matrix.get(x, y) ? 0xff000000 : 0xfffffff;
                }
            }
        }

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        image.getRaster().setDataElements(0, 0, width, height, pixels);

        return image;
    }

    /**
     * 得到二维码BufferedImage
     *
     * @param content 二维码显示的文本
     * @param width 二维码的宽度
     * @param height 二维码的高度
     *
     * @return
     *
     * @throws WriterException
     * @throws IOException
     */
    public static BufferedImage genBarcode(String content, int width, int height) throws WriterException, IOException {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        BitMatrix bitMatrix = multiFormatWriter.encode(content, BarcodeFormat.QR_CODE, width, height);
        BufferedImage bufferedImage = toBufferedImage(bitMatrix);
        return bufferedImage;
    }

    /**
     * 把传入的原始图像按高度和宽度进行缩放，生成符合要求的图标
     *
     * @param srcImageFile 源文件地址
     * @param height 目标高度
     * @param width 目标宽度
     * @param hasFiller 比例不对时是否需要补白：true为补白; false为不补白;
     *
     * @throws IOException
     */
    private static BufferedImage scale1(InputStream srcImageUrl, int height, int width, boolean hasFiller) throws IOException {
        double ratio = 0.0; // 缩放比例
        ImageInputStream imageInput = ImageIO.createImageInputStream(srcImageUrl);
        BufferedImage srcImage = ImageIO.read(imageInput);
        Image destImage = srcImage.getScaledInstance(width, height, BufferedImage.SCALE_SMOOTH);
        // 计算比例
        if ((srcImage.getHeight() > height) || (srcImage.getWidth() > width)) {
            if (srcImage.getHeight() > srcImage.getWidth()) {
                ratio = (new Integer(height)).doubleValue() / srcImage.getHeight();
            } else {
                ratio = (new Integer(width)).doubleValue() / srcImage.getWidth();
            }
            AffineTransformOp op = new AffineTransformOp(AffineTransform.getScaleInstance(ratio, ratio), null);
            destImage = op.filter(srcImage, null);
        }
        if (hasFiller) {// 补白
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphic = image.createGraphics();
            graphic.setColor(Color.white);
            graphic.fillRect(0, 0, width, height);
            if (width == destImage.getWidth(null)) {
                graphic.drawImage(destImage, 0, (height - destImage.getHeight(null)) / 2, destImage.getWidth(null), destImage.getHeight(null), Color.white, null);
            } else {
                graphic.drawImage(destImage, (width - destImage.getWidth(null)) / 2, 0, destImage.getWidth(null), destImage.getHeight(null), Color.white, null);
            }
            graphic.dispose();
            destImage = image;
        }
        return (BufferedImage) destImage;
    }

    //    /**
    //     * 把传入的原始图像按高度和宽度进行缩放，生成符合要求的图标
    //     *
    //     * @param srcImageFile 源文件地址
    //     * @param height 目标高度
    //     * @param width 目标宽度
    //     * @param hasFiller 比例不对时是否需要补白：true为补白; false为不补白;
    //     * @throws IOException
    //     */
    //    private static BufferedImage scale(String srcImageFile, int height, int width, boolean hasFiller) throws IOException {
    //        double ratio = 0.0; // 缩放比例
    //        File file = new File(srcImageFile);
    //        BufferedImage srcImage = ImageIO.read(file);
    //        Image destImage = srcImage.getScaledInstance(width, height, BufferedImage.SCALE_SMOOTH);
    //        // 计算比例
    //        if ((srcImage.getHeight() > height) || (srcImage.getWidth() > width)) {
    //            if (srcImage.getHeight() > srcImage.getWidth()) {
    //                ratio = (new Integer(height)).doubleValue() / srcImage.getHeight();
    //            } else {
    //                ratio = (new Integer(width)).doubleValue() / srcImage.getWidth();
    //            }
    //            AffineTransformOp op = new AffineTransformOp(AffineTransform.getScaleInstance(ratio, ratio), null);
    //            destImage = op.filter(srcImage, null);
    //        }
    //        if (hasFiller) {// 补白
    //            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    //            Graphics2D graphic = image.createGraphics();
    //            graphic.setColor(Color.white);
    //            graphic.fillRect(0, 0, width, height);
    //            if (width == destImage.getWidth(null)) {
    //                graphic.drawImage(destImage, 0, (height - destImage.getHeight(null)) / 2, destImage.getWidth(null),
    //                        destImage.getHeight(null), Color.white, null);
    //            } else {
    //                graphic.drawImage(destImage, (width - destImage.getWidth(null)) / 2, 0, destImage.getWidth(null),
    //                        destImage.getHeight(null), Color.white, null);
    //            }
    //            graphic.dispose();
    //            destImage = image;
    //        }
    //        return (BufferedImage) destImage;
    //    }

    private static BufferedImage toBufferedImage(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
            }
        }
        return image;
    }

    public static void main(String[] args) throws Exception {
        String srcImageUrl = "http://file.fwjia.com:88/d/file/2013-08-0/2412fbe21dc666a2e33ccd15158e9fbc.jpg";
        URL url = new URL(srcImageUrl); //创建URL   
        URLConnection urlconn = url.openConnection(); // 试图连接并取得返回状态码urlconn.connect();   
        HttpURLConnection httpconn = (HttpURLConnection) urlconn;
        if (httpconn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            InputStream is = urlconn.getInputStream();
            ImageIO.write(QRCodeUtil.genBarcode("http://www.baidu.com", 500, 500, is), "jpg", new File("C:/bbb.jpg"));
        }
    }

}
