package com.hzih.face.recognition.utils.zip;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;

import java.io.*;

/**
 * Created by 钱晓盼 on 14-4-23
 * 文件压缩抽象类
 * @author Administrator .
 */
public class AbstractZipHandler {
    public static final String ZIP_SUFFIX = ".zip";

    public static final String DEF_DECODING = "UTF-8";

    private File inputFile;

    private File outFile;

    private FileFilter fileFilter;

    private ZipArchiveOutputStream zipOutput;

    private String encoding;

    public AbstractZipHandler(File inputFile, File outFile) throws IOException {
        if (null == inputFile || !inputFile.exists() || !inputFile.canRead()) {
            throw new IllegalArgumentException("The inputFile must be directory and can read.");
        }
        if (false == (outFile.getCanonicalPath().toLowerCase().endsWith(ZIP_SUFFIX))) {
            throw new IllegalArgumentException("The outFile must be zip file.");
        }

        this.inputFile = inputFile;
        this.outFile = outFile;

        zipOutput = new ZipArchiveOutputStream(outFile);
        zipOutput.setEncoding(this.getEncoding());
    }


    public void zip() {
        File[] files = this.getFileFilter() == null ? inputFile.listFiles():inputFile.listFiles(this.getFileFilter());
        try {
            for (File file : files) {
                zipFile(file);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != zipOutput) {
                try {
                    zipOutput.close();
                } catch (IOException e) {
                }
                zipOutput = null;
            }

        }
    }

    public void zip(File file) {
        try {
            zipFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != zipOutput) {
                try {
                    zipOutput.close();
                } catch (IOException e) {
                }
                zipOutput = null;
            }

        }
    }

    private void zipFile(File file) throws IOException {
        if (file.isDirectory()) {
            zipOutput.putArchiveEntry(new ZipArchiveEntry(this.getEntryName(
                    inputFile, file) + "/"));
            zipOutput.closeArchiveEntry();
            for (File f : this.getFileFilter() == null ? file.listFiles()
                    : file.listFiles(this.getFileFilter())) {
                zipFile(f);
            }
        } else {
            zipOutput.putArchiveEntry(new ZipArchiveEntry(this.getEntryName(
                    inputFile, file)));
            IOUtils.copy(new FileInputStream(file), zipOutput);
            zipOutput.closeArchiveEntry();
        }
    }

    public String getEntryName(File inputFile, File entryFile)
            throws IOException {
        String result = null;
        String inputFileParentPath = inputFile.getParentFile().getCanonicalPath();
        String entryFilePath = entryFile.getCanonicalPath();
        result = entryFilePath.substring(entryFilePath.indexOf(inputFileParentPath) + inputFileParentPath.length() + 1);
        return result;
    }

    public FileFilter getFileFilter() {
        return fileFilter;
    }

    public void setFileFilter(FileFilter fileFilter) {
        this.fileFilter = fileFilter;
    }

    public String getEncoding() {
        return null == encoding ? DEF_DECODING : encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    /**
     * 把zip文件解压到指定的文件夹
     * @param zipFilePath zip文件路径, 如 "D:/test/aa.zip"
     * @param saveFileDir 解压后的文件存放路径, 如"D:/test/"
     */
    public static void decompressZip(String zipFilePath,String saveFileDir) {
        if(isEndsWithZip(zipFilePath)) {
            File file = new File(zipFilePath);
            File fileDir = new File(saveFileDir);
            if(!fileDir.exists()){
                fileDir.mkdirs();
            }
            if(file.exists()) {
                InputStream is = null;
//can read Zip archives
                ZipArchiveInputStream zais = null;
                try {
                    is = new FileInputStream(file);
                    zais = new ZipArchiveInputStream(is);
                    ArchiveEntry archiveEntry = null;
//把zip包中的每个文件读取出来
//然后把文件写到指定的文件夹
                    while((archiveEntry = zais.getNextEntry()) != null) {
//获取文件名
                        String entryFileName = archiveEntry.getName();
//构造解压出来的文件存放路径
                        String entryFilePath = saveFileDir + entryFileName;
                        byte[] content = new byte[(int) archiveEntry.getSize()];
                        zais.read(content);
                        OutputStream os = null;
                        try {
//把解压出来的文件写到指定路径
                            File entryFile = new File(entryFilePath);
                            os = new FileOutputStream(entryFile);
                            os.write(content);
                        }catch(IOException e) {
                            throw new IOException(e);
                        }finally {
                            if(os != null) {
                                os.flush();
                                os.close();
                            }
                        }

                    }
                }catch(Exception e) {
                    throw new RuntimeException(e);
                }finally {
                    try {
                        if(zais != null) {
                            zais.close();
                        }
                        if(is != null) {
                            is.close();
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }
    /**
     * 判断文件名是否以.zip为后缀
     * @param fileName 需要判断的文件名
     * @return 是zip文件返回true,否则返回false
     */
    public static boolean isEndsWithZip(String fileName) {
        boolean flag = false;
        if(fileName != null && !"".equals(fileName.trim())) {
            if(fileName.endsWith(".ZIP")||fileName.endsWith(".zip")){
                flag = true;
            }
        }
        return flag;
    }
}
