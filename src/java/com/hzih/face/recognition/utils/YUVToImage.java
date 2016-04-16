package com.hzih.face.recognition.utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class YUVToImage {
	DataInputStream dis;
	byte[] oneFrame;
	int width;
	int height;
	String type= "4:2:0";
	int oneFrameLength;

	public YUVToImage(int width, int height) {
		this.width = width;
		this.height = height;
		this.type = "4:2:0"; //this version only support the 4:2:0 yuv
	}

	public void startReading(String filename) throws FileNotFoundException {
        dis = new DataInputStream(new BufferedInputStream(
                new FileInputStream(filename)));
        double pengali = 1.5;
        if ("4:2:0".equals(type)) {
            pengali = 1.5;
        }
        oneFrameLength = (int) (width * height * (pengali));
        oneFrame = new byte[oneFrameLength];
	}

	public int getRGBFromStream(int x, int y){
		int arraySize = height * width;
		int Y = unsignedByteToInt(oneFrame[y * width + x]);
		int U = unsignedByteToInt(oneFrame[(y/2) * (width/2) + x/2 + arraySize]);
		int V = unsignedByteToInt(oneFrame[(y/2) * (width/2) + x/2 + arraySize + arraySize/4]);

//		int R = (int)(Y + 1.370705 * (V-128));
//		int G = (int)(Y - 0.698001 * (V-128) - 0.337633 * (U-128));
//		int B = (int)(Y + 1.732446 * (U-128));
//
		int R = (int)(Y + 1.4075 * (V - 128));
		int G = (int)(Y - 0.3455 * (U - 128) - (0.7169 * (V - 128)));
		int B =(int) (Y + 1.7790 * (U - 128));

//        int R = (int)(1.164 * (Y - 16) + 2.018 * (V -128));
//        int G = (int)(1.164 * (Y - 16) - 0.38 * (U - 128) - 0.813 * (V - 128));
//        int B = (int)(1.164 * (Y - 16) + 1.159 * (U -128));

		if(R>255) R = 255;
		if(G>255) G = 255;
		if(B>255) B = 255;

		if(R<0) R = 0;
		if(G<0) G = 0;
		if(B<0) B = 0;

		int rColor = (0xff << 24) | (R << 16) | (G << 8) | B;
		return rColor;
	}

	public int getGrayScaleFromY(int x, int y) {
		int arraySize = height * width;
		int Y = unsignedByteToInt(oneFrame[y * width + x]);
		int R = Y;

		if(R>255) R = 255;

		int G = R;
		int B = R;

		int rColor = (0xff << 24) | (R << 16) | (G << 8) | B;

		return rColor;
	}

	public BufferedImage nextImage() throws IOException {
        int n = dis.read(oneFrame);
        if (n != oneFrameLength) {
            return null;
        }
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                int rColor = getRGBFromStream(i, j);
                image.setRGB(i, j, rColor);
            }
        }

        //ImageIO.write(image, "png", new File("result.png"));
        return image;

	}
	public BufferedImage nextImageYOnly() throws IOException {
        int n = dis.read(oneFrame);
        if (n != oneFrameLength) {
            return null;
        }
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                int rColor = getGrayScaleFromY(i, j);
                image.setRGB(i, j, rColor);
            }
        }
        return image;
	}

	public void endReading() {
		try {
			if(dis!=null){
                dis.close();
            }
		} catch (IOException e) {
		}
	}

	public static int unsignedByteToInt(byte b) {
		return (int) b & 0xFF;
	}

    public static void I420ToImage(String yuvFile,int width,int height,File outFile,String outFileType) throws IOException {
        BufferedImage currentImage;
        YUVToImage ryuv = new YUVToImage(width, height); //
        ryuv.startReading(yuvFile);
        while((currentImage = ryuv.nextImage())!=null) {
            ImageIO.write(currentImage, outFileType, outFile);// 写图片
        }
        ryuv.endReading();
    }

    public static void I420ToImageYOnly(String yuvFile,int width,int height,File outFile,String outFileType) throws IOException {
        BufferedImage currentImage;
        YUVToImage ryuv = new YUVToImage(width, height); //
        ryuv.startReading(yuvFile);
        while((currentImage = ryuv.nextImageYOnly())!=null) {
            ImageIO.write(currentImage, outFileType, outFile);// 写图片
        }
        ryuv.endReading();
    }
    
    
	public static void main(String args[]) throws Exception {
		BufferedImage currentImage;

        YUVToImage ryuv = new YUVToImage(722, 976); //read the qcif yuv
		ryuv.startReading("D:/test/faceinfo_1_1439417676/photo1_722_976.yuv");
		
//		WriteYUV wyuv = new WriteYUV(176, 144);
//		wyuv.startWriting("test_qcif.yuv");
        File outFile = new File("D:/test/faceinfo_1_1439417676/photo1_722_976.jpg");

//        while((currentImage = ryuv.nextImageYOnly())!=null) {
        while((currentImage = ryuv.nextImage())!=null) {
//			wyuv.writeImageYOnly(currentImage);
            ImageIO.write(currentImage, "jpg", outFile);// 写图片

        }

//		wyuv.endWriting();
		ryuv.endReading();
	}
}
