package com.hzih.face.recognition.utils;

import org.apache.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by Administrator on 15-7-28.
 */
public class YuvToRGB {
    static Logger logger = Logger.getLogger(YuvToRGB.class);
    private static int R = 0;
    private static int G = 1;
    private static int B = 2;
    //I420是yuv420格式，是3个plane，排列方式为(Y)(U)(V)
    public static int[] I420ToRGB(byte[] src, int width, int height){
        int numOfPixelY = 0;
        int positionOfU = numOfPixelY + width * height;
        int positionOfV = positionOfU + ((width * height) >> 2);
        int x = (width * 3 % 4);
        int widthRGB = width * 3 + ( x> 0 ?(4-x) :0);
//        byte[] bgr = new byte[widthRGB * height];
        int[] bgr = new int[widthRGB * height];
        for(int y = 0; y < height; y++){
            int startY = y * widthRGB;
            for(x = 0; x < width; x++){
                int Y = y * width + x;
                int U = positionOfU + (y/2) * (width/2) + x/2;
                int V = positionOfV + (y/2) * (width/2) + x/2;
                int index = startY + x * 3;
//                RGB tmp = ycbcr2rgbMy(src[Y], src[U], src[V]);
                RGB tmp = ycbcr2rgb(src[Y], src[U], src[V]);
                bgr[index + R] = tmp.r;
                bgr[index + G] = tmp.g;
                bgr[index + B] = tmp.b;
//                System.out.println(index +" "+ tmp.b);
//                logger.info(tmp.r + " " + tmp.g + " " + tmp.b);
            }
        }


//        int inLen = bgr.length/4;
//        int[] result = new int[inLen];
//        int idx = 0;
//        for (int i = 0; i < bgr.length;i++) {
//            byte[] b = new byte[4];
//            b[0] = bgr[i++];
//            b[1] = bgr[i++];
//            b[2] = bgr[i++];
//            b[3] = bgr[i];
//            int d = byte2Int(b);
//            result[idx++] = d;
//        }
//        int[] result = new int[bgr.length];
//        for (int i = 0; i < bgr.length;i++) {
//            result[i] = bgr[i];
//        }
//
//        return result;
        return bgr;
    }


    public static byte[] I420ToRGB2(byte[] src, int width, int height) {
        int numOfPixelY = 0;
        int positionOfU = numOfPixelY + width * height;
        int positionOfV = positionOfU + ((width * height) >> 2);
        int x = (width * 3 % 4);
        int widthRGB = width * 3 + ( x> 0 ?(4-x) :0);
        byte[] bgr = new byte[widthRGB * height];
//        int[] bgr = new int[widthRGB * height];
        for(int y = 0; y < height; y++){
            int startY = y * widthRGB;
            for(x = 0; x < width; x++){
                int Y = y * width + x;
                int U = positionOfU + (y/2) * (width/2) + x/2;
                int V = positionOfV + (y/2) * (width/2) + x/2;
                int index = startY + x * 3;
//                RGB tmp = ycbcr2rgbMy(src[Y], src[U], src[V]);
                RGB tmp = ycbcr2rgb(src[Y], src[U], src[V]);
                bgr[index + R] = revise(tmp.b);
                bgr[index + G] = revise(tmp.g);
                bgr[index + B] = revise(tmp.r);
//                System.out.println(index +" "+ tmp.b);
//                logger.info(tmp.r + " " + tmp.g + " " + tmp.b);
            }
        }
        return bgr;
    }

    public static byte revise(int rgb) {

        if( rgb > 255) return (byte)255;
        if( rgb < 0) return 0;
        return (byte)rgb;

    }


    public static byte[] convertRgb2Bmp(byte[] rgbBuf, int width, int height, File outFile) throws Exception{

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try{

            bos.write(new byte[]{'B', 'M'});
            bos.write(intToDWord(width*height*3 + 0x36));
            bos.write(intToDWord(0));
            //bos.write(intToWord(0));
            bos.write(intToDWord(0x36));
            bos.write(intToDWord(0x28));
            bos.write(intToDWord(width));
            bos.write(intToDWord(height));
            bos.write(intToWord(1));
            bos.write(intToWord(24));
            bos.write(intToDWord(0));
            bos.write(intToDWord(width*height*3));
            bos.write(intToDWord(0));
            bos.write(intToDWord(0));
            bos.write(intToDWord(0));
            bos.write(intToDWord(0));

            int len = rgbBuf.length;
            for(int i=len-1;i>=0;i--){
//            for(int i=0;i<len-1;i++){

                bos.write(rgbBuf[i]);

            }
            bos.flush();

        }catch(Exception ex){

            ex.printStackTrace();
            throw ex;

        }finally{

            bos.close();

        }

        FileOutputStream fos;
        fos = new FileOutputStream(outFile);
        fos.write(bos.toByteArray());
        fos.close();

        return bos.toByteArray();

    }
    /**
     * @param parValue
     * @return
     */
    public static byte[] intToDWord(int parValue) {

        byte retValue[] = new byte[4];
        retValue[0] = (byte) (parValue & 0x00FF);
        retValue[1] = (byte) ((parValue>>8) & 0x000000FF);
        retValue[2] = (byte) ((parValue>>16) & 0x000000FF);
        retValue[3] = (byte) ((parValue>>24) & 0x000000FF);
        return retValue;

    }

    public static byte[] intToWord(int parValue) {

        byte retValue[] = new byte[2];
        retValue[0] = (byte) (parValue & 0x00FF);
        retValue[1] = (byte) ((parValue>>8) & 0x00FF);
        return retValue;

    }


    static public byte[] decodeYUV420P(byte[] rgbBuf, byte[] yuv420p,int width, int height) {

        final int frameSize = width * height;
        if (rgbBuf == null)
            throw new NullPointerException("buffer 'rgbBuf' is null");
        if (rgbBuf.length < frameSize * 3)
            throw new IllegalArgumentException("buffer 'rgbBuf' size "
                    + rgbBuf.length + " < minimum " + frameSize * 3);

        if (yuv420p == null)
            throw new NullPointerException("buffer 'yuv420p' is null");

        if (yuv420p.length < frameSize * 3 / 2)
            throw new IllegalArgumentException("buffer 'yuv420p' size "
                    + yuv420p.length + " < minimum " + frameSize * 3 / 2);

        int i = 0, y = 0;
        int uvp = 0, u = 0, v = 0;
        int y1192 = 0, r = 0, g = 0, b = 0;

        for (int j = 0, yp = 0; j < height; j++) {
            uvp = frameSize + (j >> 1) * width;
            u = 0;
            v = 0;
            for (i = 0; i < width; i++, yp++) {
                y = (0xff & ((int) yuv420p[yp])) - 16;
                if (y < 0)
                    y = 0;
                if ((i & 1) == 0) {
                    v = (0xff & yuv420p[uvp++]) - 128;
                    u = (0xff & yuv420p[uvp++]) - 128;
                }

                y1192 = 1192 * y;
                r = (y1192 + 1634 * v);
                g = (y1192 - 833 * v - 400 * u);
                b = (y1192 + 2066 * u);

                if (r < 0)
                    r = 0;
                else if (r > 262143)
                    r = 262143;
                if (g < 0)
                    g = 0;
                else if (g > 262143)
                    g = 262143;
                if (b < 0)
                    b = 0;
                else if (b > 262143)
                    b = 262143;

                rgbBuf[yp * 3] = (byte) (r >> 10);
                rgbBuf[yp * 3 + 1] = (byte) (g >> 10);
                rgbBuf[yp * 3 + 2] = (byte) (b >> 10);
            }
        }
        return rgbBuf;
    }


    private static int byte2Int(byte[] b){
        int intValue = 0;
        for (int i =0;i<b.length;i++){
            intValue += (b[i] & 0xFF) << (8*(3-i));
        }
        return intValue;
    }

    private static RGB ycbcr2rgbMy(byte y, byte u, byte v) {
//        System.out.println(y + " " + u + " " + v);
        int r = 256 * y + 360 * v - 360 * 128 + 128;
        int g = 256 * y - 63 * u + 63 * 128 - 184 * v + 184 * 128 + 128;
        int b = 256 * y + 455 * u - 455 * 128 + 128;



        if (r > 0xff00) r = 0xff00;
        if (g > 0xff00) g = 0xff00;
        if (b > 0xff00) b = 0xff00;

        if (r < 0) r = 0;
        if (g < 0) g = 0;
        if (b < 0) b = 0;

        RGB rgb = new RGB();
        rgb.r = r >> 8;
        rgb.g = g >> 8;
        rgb.b = b >> 8;

        return rgb;
    }

    private static class RGB{
//        public byte r, g, b;
        public int r, g, b;
    }

    private static RGB yuvTorgb(byte Y, byte U, byte V){
        RGB rgb = new RGB();
//        rgb.r = (int)((Y&0xff) + 1.4075 * ((V&0xff)-128));
//        rgb.g = (int)((Y&0xff) - 0.3455 * ((U&0xff)-128) - 0.7169*((V&0xff)-128));
//        rgb.b = (int)((Y&0xff) + 1.779 * ((U&0xff)-128));
//        rgb.r =(rgb.r<0? 0: rgb.r>255? 255 : rgb.r);
//        rgb.g =(rgb.g<0? 0: rgb.g>255? 255 : rgb.g);
//        rgb.b =(rgb.b<0? 0: rgb.b>255? 255 : rgb.b);
        return rgb;
    }

    public static int unsignedByteToInt(byte b) {
        return (int) b & 0xFF;
    }

    public static RGB ycbcr2rgb(byte Cy,byte Cb,byte Cr){
//   int MyR = (int)(Y + 1.402 * (Cr -128));
//   int MyG = (int)(Y - 0.34414 * (Cb - 128) - 0.71414 * (Cr - 128));
//   int MyB = (int)(Y + 1.772 * (Cb -128));

//        int MyR = (int)(1.164 * (Y - 16) + 2.018 * (Cr -128));
//        int MyG = (int)(1.164 * (Y - 16) - 0.38 * (Cb - 128) - 0.813 * (Cr - 128));
//        int MyB = (int)(1.164 * (Y - 16) + 1.159 * (Cb -128));

//        int MyR = (int)(1.164 * (Y - 16) + 2.018 * (Cr -128));
//        int MyG = (int)(1.164 * (Y - 16) - 0.38 * (Cb - 128) - 0.813 * (Cr - 128));
//        int MyB = (int)(1.164 * (Y - 16) + 1.159 * (Cb -128));

//        int MyR = (int)(1.164 * (Y - 16) + 1.596 * (Cr -128));
//        int MyG = (int)(1.164 * (Y - 16) - 0.392 * (Cb - 128) - 0.813 * (Cr - 128));
//        int MyB = (int)(1.164 * (Y - 16) + 1.159 * (Cb -128));

//     int MyR = (int) (Y + 1.14 * Cr);
//     int MyG = (int) (Y - 0.39* Cb - 0.58 * Cr);
//     int MyB = (int) (Y + 2.03 * Cb);

        int Y = unsignedByteToInt(Cy);
        int G = unsignedByteToInt(Cb);
        int B = unsignedByteToInt(Cr);
        int MyR = (int)(Y + 1.4075 * (B - 128));
        int MyG = (int)(Y - 0.3455 * (G - 128) - (0.7169 * (B - 128)));
        int MyB =(int) (Y + 1.7790 * (G - 128));

        if (MyR > 255) MyR = 255;
        if (MyG > 255) MyG = 255;
        if (MyB > 255) MyB = 255;

        if (MyR < 0) MyR =0;
        if (MyG < 0) MyG =0;
        if (MyB < 0) MyB =0;

        RGB rgb = new RGB();
        rgb.b = MyB;
        rgb.g = MyG;
        rgb.r = MyR;
        return rgb;
    }

}
