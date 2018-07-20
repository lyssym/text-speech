package com.bgi.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;


public class IOUtil {
    public static final int READASTREAM = 1;
    public static final int READASPATH = 2;

    /**
     * 从路径加载源文件
     * @param sourcePath
     * @return
     */
    private static BufferedReader loadResourceAsPath(String sourcePath) {
        try {
            BufferedReader br = Files.newBufferedReader(Paths.get(sourcePath), StandardCharsets.UTF_8);
            return br;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * 从流加载源文件
     * @param sourcePath
     * @return
     */
    private static BufferedReader loadResourceAsStream(String sourcePath) {
        InputStream in = IOUtil.class.getResourceAsStream(sourcePath);
        BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
        return br;
    }
    
    /**
     * 选择数据加载方式
     * @param sourcePath
     * @param mode
     * @return
     * @throws IOException
     */
    public static BufferedReader loadResource(String sourcePath, int mode) throws IOException {
        if(mode == READASPATH) {
            return loadResourceAsPath(sourcePath);
        } else if (mode == READASTREAM) {
            return loadResourceAsStream(sourcePath);
        }
        return null;
    }
    
    /**
     * 文件存储缓存
     * @param filepath
     * @return
     * @throws IOException
     */
    public static BufferedWriter newBufferedWriter(String filepath) throws IOException {
        try {
            File file = new File(filepath);
            if (!file.exists())
                file.createNewFile();
            BufferedWriter bw = Files.newBufferedWriter(Paths.get(filepath), StandardCharsets.UTF_8);
            return bw;
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return null;
    }
}
