package com.playin.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;

import java.io.File;

public class SimilarImage {

    /**
     * 1~5说明两张照片极其相似，6~10说明较为相似，10以上说明不相似
     * @param pic1
     * @param pic2
     * @return
     */
    public static int compare(File pic1, File pic2) {
        try {
            // 第一步，缩小尺寸
            Bitmap soruceBmp1 = BitmapFactory.decodeFile(pic1.getAbsolutePath());
            Bitmap bmp1 = ThumbnailUtils.extractThumbnail(soruceBmp1, 8, 8);
            soruceBmp1.recycle();
            // 第二步，简化色彩
            Bitmap greyBmp1 = convertGreyImg(bmp1);
            bmp1.recycle();
            // 第三步，计算平均值
            int avg1 = getAvg(greyBmp1);
            // 第四步，比较像素的灰度
            String greyCode1 = getBinary(greyBmp1, avg1);
            // 第五步，计算哈希值
            String hash1 = binaryString2hexString(greyCode1);

            Bitmap soruceBmp2 = BitmapFactory.decodeFile(pic2.getAbsolutePath());
            Bitmap bmp2 = ThumbnailUtils.extractThumbnail(soruceBmp2, 8, 8);
            soruceBmp2.recycle();
            Bitmap greyBmp2 = convertGreyImg(bmp2);
            bmp2.recycle();
            int avg2 = getAvg(greyBmp2);
            String greyCode2 = getBinary(greyBmp2, avg2);
            String hash2 = binaryString2hexString(greyCode2);

            return diff(hash1, hash2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 将彩色图转换为灰度图
     *
     * @param img 位图
     * @return 返回转换好的位图
     */
    public static Bitmap convertGreyImg(Bitmap img) {
        int width = img.getWidth();         //获取位图的宽
        int height = img.getHeight();       //获取位图的高
        int[] pixels = new int[width * height]; //通过位图的大小创建像素点数组
        img.getPixels(pixels, 0, width, 0, 0, width, height);
        int alpha = 0xFF << 24;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int original = pixels[width * i + j];
                int red = ((original & 0x00FF0000) >> 16);
                int green = ((original & 0x0000FF00) >> 8);
                int blue = (original & 0x000000FF);

                int grey = (int) ((float) red * 0.3 + (float) green * 0.59 + (float) blue * 0.11);
                grey = alpha | (grey << 16) | (grey << 8) | grey;
                pixels[width * i + j] = grey;
            }
        }
        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        result.setPixels(pixels, 0, width, 0, 0, width, height);
        return result;
    }

    public static int getAvg(Bitmap img) {
        int width = img.getWidth();
        int height = img.getHeight();
        int[] pixels = new int[width * height];
        img.getPixels(pixels, 0, width, 0, 0, width, height);
        int avgPixel = 0;
        for (int pixel : pixels) {
            avgPixel += pixel;
        }
        return avgPixel / pixels.length;
    }

    public static String getBinary(Bitmap img, int average) {
        StringBuilder sb = new StringBuilder();
        int width = img.getWidth();
        int height = img.getHeight();
        int[] pixels = new int[width * height];

        img.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int original = pixels[width * i + j];
                if (original >= average) {
                    pixels[width * i + j] = 1;
                } else {
                    pixels[width * i + j] = 0;
                }
                sb.append(pixels[width * i + j]);
            }
        }
        return sb.toString();
    }

    public static String binaryString2hexString(String bString) {
        if (bString == null || bString.equals("") || bString.length() % 8 != 0)
            return null;
        StringBuilder sb = new StringBuilder();
        int iTmp;
        for (int i = 0; i < bString.length(); i += 4) {
            iTmp = 0;
            for (int j = 0; j < 4; j++) {
                iTmp += Integer.parseInt(bString.substring(i + j, i + j + 1)) << (4 - j - 1);
            }
            sb.append(Integer.toHexString(iTmp));
        }
        return sb.toString();
    }

    public static Bitmap convertWABImg(Bitmap img, int average) {
        System.out.println("平均值=" + average);
        int width = img.getWidth();
        int height = img.getHeight();
        int[] pixels = new int[width * height];

        img.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int original = pixels[width * i + j];
                if (original >= average) {
                    pixels[width * i + j] = -16777216;
                } else {
                    pixels[width * i + j] = -1;
                }
            }
        }
        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        result.setPixels(pixels, 0, width, 0, 0, width, height);
        return result;
    }

    private static int diff(String s1, String s2) {
        char[] s1s = s1.toCharArray();
        char[] s2s = s2.toCharArray();
        int diffNum = 0;
        for (int i = 0; i<s1s.length; i++) {
            if (s1s[i] != s2s[i]) {
                diffNum++;
            }
        }
        return diffNum;
    }
}
