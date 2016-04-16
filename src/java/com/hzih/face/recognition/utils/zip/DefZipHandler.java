package com.hzih.face.recognition.utils.zip;

import java.io.File;
import java.io.IOException;

/**
 * Created by 钱晓盼 on 14-4-23.
 * 文件压缩默认实现类
 * @author Administrator
 *
 */
public class DefZipHandler extends AbstractZipHandler {
    public DefZipHandler(File inputFile, File outFile) throws IOException {
        super(inputFile, outFile);
    }
}
