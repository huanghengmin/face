package com.hzih.face.recognition.utils.zip;

import de.innosystec.unrar.Archive;
import de.innosystec.unrar.rarfile.FileHeader;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.zip.Zip64Mode;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

/**
 * Created by 钱晓盼 on 14-4-23.
 */
public class ZipFileUtil {
    /**
     * 把文件压缩成zip格式
     *
     * @param files       需要压缩的文件
     * @param zipFilePath 压缩后的zip文件路径
     */
    public static void compressFiles2Zip(File[] files, String zipFilePath) {
        if (files != null && files.length > 0) {
            if (isEndsWithZip(zipFilePath)) {
                ZipArchiveOutputStream zaos = null;
                try {
                    File zipFile = new File(zipFilePath);
                    zaos = new ZipArchiveOutputStream(zipFile);
                    zaos.setUseZip64(Zip64Mode.AsNeeded);
                    for (File file : files) {
                        if (file != null) {
                            ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(file, file.getName());
                            zaos.putArchiveEntry(zipArchiveEntry);
                            InputStream is = null;
                            try {
                                is = new FileInputStream(file);
                                byte[] buffer = new byte[1024 * 5];
                                int len = -1;
                                while ((len = is.read(buffer)) != -1) {
                                    zaos.write(buffer, 0, len);
                                }
                                zaos.closeArchiveEntry();
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            } finally {
                                if (is != null)
                                    is.close();
                            }
                        }
                    }
                    zaos.finish();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    try {
                        if (zaos != null) {
                            zaos.close();
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    public static void compressFile2Zip(InputStream is, File file, File zipFile) throws Exception {
        if (isEndsWithZip(zipFile.getPath())) {
            ZipArchiveOutputStream zaos = null;
            try {
                zaos = new ZipArchiveOutputStream(zipFile);
                zaos.setUseZip64(Zip64Mode.AsNeeded);
                if (file != null) {
                    ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(file, file.getName());
                    zaos.putArchiveEntry(zipArchiveEntry);
                    try {
                        byte[] buffer = new byte[1024 * 5];
                        int len = -1;
                        while ((len = is.read(buffer)) != -1) {
                            zaos.write(buffer, 0, len);
                        }
                        zaos.closeArchiveEntry();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    } finally {
                        if (is != null)
                            is.close();
                    }
                }
                zaos.finish();
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    if (zaos != null) {
                        zaos.close();
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

        }
    }

    /**
     * 把zip文件解压到指定的文件夹
     *
     * @param zipFilePath zip文件路径, 如 "D:/test/aa.zip"
     * @param saveFileDir 解压后的文件存放路径, 如"D:/test/"
     */
    public static void decompressZip(String zipFilePath, String saveFileDir) throws Exception {
        if (isEndsWithZip(zipFilePath)) {
            File file = new File(zipFilePath);
            File fileDir = new File(saveFileDir);
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }
            if (file.exists()) {
                InputStream is = null;
//can read Zip archives
                ZipArchiveInputStream zais = null;
                try {
                    is = new FileInputStream(file);
                    zais = new ZipArchiveInputStream(is);
                    ArchiveEntry archiveEntry = null;
//把zip包中的每个文件读取出来
//然后把文件写到指定的文件夹
                    while ((archiveEntry = zais.getNextEntry()) != null) {
//获取文件名
                        String entryFileName = archiveEntry.getName();
//构造解压出来的文件存放路径
                        String entryFilePath;
                        if (saveFileDir.endsWith("/")) {
                            entryFilePath = saveFileDir + entryFileName;
                        } else {
                            entryFilePath = saveFileDir + "/" + entryFileName;
                        }
//                        byte[] content = new byte[(int) archiveEntry.getSize()];
//                        zais.read(content);
                        byte[] buff = new byte[1024 * 1024];
                        OutputStream os = null;
                        try {
//把解压出来的文件写到指定路径
                            File entryFile = new File(entryFilePath);
                            os = new FileOutputStream(entryFile);
                            int len = 0;
                            while ((len = zais.read(buff)) != -1) {
                                os.write(buff, 0, len);
                            }
                        } catch (IOException e) {
                            throw new IOException(e);
                        } finally {
                            if (os != null) {
                                os.flush();
                                os.close();
                            }
                        }

                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    try {
                        if (zais != null) {
                            zais.close();
                        }
                        if (is != null) {
                            is.close();
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    public static List<String> decompressZipList(String zipFilePath, String saveFileDir) throws Exception {
        List<String> list = new ArrayList<String>();
        if (isEndsWithZip(zipFilePath)) {
            File file = new File(zipFilePath);
            File fileDir = new File(saveFileDir);
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }
            if (file.exists()) {
                InputStream is = null;
//can read Zip archives
                ZipArchiveInputStream zais = null;
                try {
                    is = new FileInputStream(file);
                    zais = new ZipArchiveInputStream(is);
                    ArchiveEntry archiveEntry = null;
//把zip包中的每个文件读取出来
//然后把文件写到指定的文件夹
                    while ((archiveEntry = zais.getNextEntry()) != null) {
//获取文件名
                        String entryFileName = archiveEntry.getName();
//构造解压出来的文件存放路径
                        String entryFilePath;
                        if (saveFileDir.endsWith("/")) {
                            entryFilePath = saveFileDir + entryFileName;
                        } else {
                            entryFilePath = saveFileDir + "/" + entryFileName;
                        }
//                        byte[] content = new byte[(int) archiveEntry.getSize()];
//                        zais.read(content);
                        byte[] buff = new byte[1024 * 1024];
                        OutputStream os = null;
                        try {
//把解压出来的文件写到指定路径
                            File entryFile = new File(entryFilePath);
                            os = new FileOutputStream(entryFile);
                            int len = 0;
                            while ((len = zais.read(buff)) != -1) {
                                os.write(buff, 0, len);
                            }
                            list.add(entryFilePath);
                        } catch (IOException e) {
                            throw new IOException(e);
                        } finally {
                            if (os != null) {
                                os.flush();
                                os.close();
                            }
                        }

                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    try {
                        if (zais != null) {
                            zais.close();
                        }
                        if (is != null) {
                            is.close();
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return list;
    }


    /**
     * 判断文件名是否以.zip为后缀
     *
     * @param fileName 需要判断的文件名
     * @return 是zip文件返回true, 否则返回false
     */
    public static boolean isEndsWithZip(String fileName) {
        boolean flag = false;
        if (fileName != null && !"".equals(fileName.trim())) {
            if (fileName.endsWith(".ZIP") || fileName.endsWith(".zip")) {
                flag = true;
            }
        }
        return flag;
    }

    public static List<String> decompressGZ(String zipFilePath, final String saveFileDir) throws Exception {
        List<String> list = new ArrayList<String>();
        if (isEndsWithGZ(zipFilePath)) {
            File file = new File(zipFilePath);
            if (file.length() == 0) {
                return list;
            }
            File fileDir = new File(saveFileDir);
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }
            if (file.exists()) {
                InputStream is = null;
                BufferedInputStream bufIn = null;
                GZIPInputStream gzip = null;
                TarArchiveInputStream tais = null;

                try {
                    is = new FileInputStream(file);
                    bufIn = new BufferedInputStream(is);
                    gzip = new GZIPInputStream(bufIn);
                    tais = new TarArchiveInputStream(gzip);
                    TarArchiveEntry archiveEntry = null;

                    while ((archiveEntry = tais.getNextTarEntry()) != null) {
                        if (archiveEntry.isDirectory()) {
                            String entryFileName = archiveEntry.getName();
                            String tempDir = saveFileDir;
                            if (saveFileDir.endsWith("/")) {
                                tempDir = saveFileDir + entryFileName;
                            } else {
                                tempDir = saveFileDir + "/" + entryFileName;
                            }
                            File _fileDir = new File(tempDir);
                            if (!_fileDir.exists()) {
                                _fileDir.mkdirs();
                            }
                            continue;
                        }
                        //获取文件名
                        String entryFileName = archiveEntry.getName();
                        //构造解压出来的文件存放路径
                        String entryFilePath;
                        if (saveFileDir.endsWith("/")) {
                            entryFilePath = saveFileDir + entryFileName;
                        } else {
                            entryFilePath = saveFileDir + "/" + entryFileName;
                        }

//                        byte[] content = new byte[(int) archiveEntry.getSize()];
//                        zais.read(content);
                        byte[] buff = new byte[1024 * 1024];
                        OutputStream os = null;
                        try {
//把解压出来的文件写到指定路径
                            File entryFile = new File(entryFilePath);
                            os = new FileOutputStream(entryFile);
                            int len = 0;
                            while ((len = tais.read(buff)) != -1) {
                                os.write(buff, 0, len);
                            }
                            list.add(entryFilePath);
                        } catch (IOException e) {
                            throw new IOException(e);
                        } finally {
                            if (os != null) {
                                os.flush();
                                os.close();
                            }

                        }

                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    try {
                        if (tais != null) {
                            tais.close();
                        }
                        if (gzip != null) {
                            gzip.close();
                        }
                        if (bufIn != null) {
                            bufIn.close();
                        }
                        if (is != null) {
                            is.close();
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return list;
    }

    /**
     * 判断文件名是否以.zip为后缀
     *
     * @param fileName 需要判断的文件名
     * @return 是zip文件返回true, 否则返回false
     */
    public static boolean isEndsWithGZ(String fileName) {
        boolean flag = false;
        if (fileName != null && !"".equals(fileName.trim())) {
            if (fileName.endsWith(".GZ") || fileName.endsWith(".gz")) {
                flag = true;
            }
        }
        return flag;
    }

    public static boolean isEndsWithRAR(String fileName) {
        boolean flag = false;
        if (fileName != null && !"".equals(fileName.trim())) {
            if (fileName.endsWith(".RAR") || fileName.endsWith(".rar")) {
                flag = true;
            }
        }
        return flag;
    }

    public static List<String> decompressRAR(String rarFilePath, String saveFileDir) {
        List<String> list = new ArrayList<String>();
        if (isEndsWithRAR(rarFilePath)) {
            File file = new File(rarFilePath);
            if (file.length() == 0) {
                return list;
            }
            File fileDir = new File(saveFileDir);
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }
            if (file.exists()) {

                Archive archive = null;
                FileOutputStream fos = null;
                try {
                    archive = new Archive(file);
                    FileHeader fh = archive.nextFileHeader();
                    int count = 0;
                    File destFileName = null;
                    while (fh != null) {
//                        System.out.println((++count) + ") " + fh.getFileNameString());
                        String compressFileName = fh.getFileNameString().trim();
                        compressFileName = compressFileName.replaceAll("\\\\", "/");
                        System.out.println(compressFileName);
                        String str;
                        if (compressFileName.lastIndexOf("/") > 0) {
                            str = compressFileName.substring(0, compressFileName.lastIndexOf("/"));
                            String dir = fileDir.getAbsolutePath() + "/" + str;
                            File dirFile = new File(dir);
                            if (!dirFile.exists()) {
                                dirFile.mkdirs();
                            }
                        }
                        destFileName = new File(fileDir.getAbsolutePath() + "/" + compressFileName);
                        if (fh.isDirectory()) {
                            if (!destFileName.exists()) {
                                destFileName.mkdirs();
                            }
                            fh = archive.nextFileHeader();
                            continue;
                        }
                        if (!destFileName.getParentFile().exists()) {
                            destFileName.getParentFile().mkdirs();
                        }
                        fos = new FileOutputStream(destFileName);
                        archive.extractFile(fh, fos);
                        fos.close();
                        fos = null;
                        fh = archive.nextFileHeader();
                        list.add(destFileName.getPath());
                    }
                    archive.close();
                    archive = null;


                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    try {
                        if (fos != null) {
                            fos.close();
                        }
                        if (archive != null) {
                            archive.close();
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return list;
    }
}
