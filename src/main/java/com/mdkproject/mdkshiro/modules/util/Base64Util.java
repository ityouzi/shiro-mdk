package com.mdkproject.mdkshiro.modules.util;

import org.apache.commons.codec.binary.Base64;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import java.io.*;

/**
 * base64 图片相互转换
 */
public class Base64Util {

    private static final int CACHE_SIZE = 1024;

    /**
     * BASE64字符串解码为二进制数
     *
     * @param base64
     * @return
     * @throws Exception
     */
    public static byte[] decode(String base64) {
        return new org.apache.commons.codec.binary.Base64().decode(base64.getBytes());
    }

    /**
     * 二进制数据编码为BASE64字符
     *
     * @param bytes
     * @return
     * @throws Exception
     */
    public static String encode(byte[] bytes) {
        return new String(new Base64().encode(bytes));
    }

    /**
     * 将文件编码为BASE64字符
     *
     * @param filePath
     * @return
     * @throws Exception
     */
    public static String encodeFile(String filePath) throws Exception {
        byte[] bytes = fileToByte(filePath);
        return encode(bytes);
    }

    /**
     * 文件转换为二进制数组
     *
     * @param filePath
     * @return
     * @throws Exception
     */
    public static byte[] fileToByte(String filePath) throws Exception {
        byte[] data = new byte[0];
        File file = new File(filePath);
        if (file.exists()) {
            FileInputStream in = new FileInputStream(file);
            ByteArrayOutputStream out = new ByteArrayOutputStream(2048);
            byte[] cache = new byte[CACHE_SIZE];
            int nRead = 0;
            while ((nRead = in.read(cache)) != -1) {
                out.write(cache, 0, nRead);
                out.flush();
            }
            out.close();
            in.close();
            data = out.toByteArray();
        }
        return data;
    }


    /**
     * BASE64字符串转回文
     *
     * @param filePath
     * @param base64
     * @throws Exception
     */
    public static void decodeToFile(String filePath, String base64) throws Exception {
        byte[] bytes = decode(base64);
        byteArrayToFile(bytes, filePath);
    }

    /**
     * 二进制数据写文件
     *
     * @param bytes
     * @param filePath
     * @throws Exception
     */
    public static void byteArrayToFile(byte[] bytes, String filePath) throws Exception {
        InputStream in = new ByteArrayInputStream(bytes);
        File destFile = new File(filePath);
        if (!destFile.getParentFile().exists()) {
            destFile.getParentFile().mkdirs();
        }
        destFile.createNewFile();
        OutputStream out = new FileOutputStream(destFile);
        byte[] cache = new byte[CACHE_SIZE];
        int nRead = 0;
        while ((nRead = in.read(cache)) != -1) {
            out.write(cache, 0, nRead);
            out.flush();
        }
        out.close();
        in.close();
    }

    /**
     * @Author: xuechaoke
     * @Date: 2018/12/22 12:55
     * @Description: 根据再FastDFS 文件服务器的地址获取文件的byte[]，再用base64编码
     * @Param: [fileDir]  文件在FastDFS服务器上的地址
     * @return: java.lang.String  文件的base64编码字符串
     */
//    public static String fileDir2Base64(String fileDir) {
//        if (fileDir == null || "".equals(fileDir)) {
//            return null;
//        }
//        byte[] bytes = FastDFSClient.downloadFile(fileDir);
//        if (bytes != null && bytes.length > 0) {
//            return encode(FastDFSClient.downloadFile(fileDir));
//        }
//        return null;
//
//    }

    /**
     * @Author: xuechaoke
     * @Date: 2019/1/7 12:11
     * @Description: MultipartFile类型 转base64字符串
     * @Param: [multipartFile]
     * @return: java.lang.String
     */
    public static String multipartFile2Base64Str(MultipartFile multipartFile) throws IOException {
        if (multipartFile != null && !multipartFile.isEmpty()) {
            byte[] bytes = multipartFile.getBytes();
            return Base64Util.encode(bytes);
        }
        return null;
    }

    /**
     * @Author: xuechaoke
     * @Date: 2019/1/7 14:28
     * @Description: 图片base64字符串转图片byte[]
     * @Param: [Base64Str]
     * @return: byte[]
     */
    public static byte[] base64Str2byteArray(String Base64Str) {
        if (Base64Str == null || Base64Str.length() == 0) {
            return null;
        }
        return Base64Util.decode(Base64Str);
    }


    //把base64 转 路径
    public static boolean GenerateImage(String base64str, String savepath) { // 对字节数组字符串进行Base64解码并生成图片
        if (base64str == null) // 图像数据为空
            return false;
        // System.out.println("开始解码");
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            // Base64解码
            byte[] b = decoder.decodeBuffer(base64str);
            // System.out.println("解码完成");
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) { // 调整异常数据
                    b[i] += 256;
                }
            }
            // System.out.println("开始生成图片");
            // 生成jpeg图片
            OutputStream out = new FileOutputStream(savepath);  //写入文件
            out.write(b);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    /**
     * 将文件转换成byte数组
     * @return
     */
    public static byte[] File2byte(File tradeFile){
        byte[] buffer = null;
        try
        {
            FileInputStream fis = new FileInputStream(tradeFile);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1)
            {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        return buffer;
    }



    public static void main(String[] args) throws Exception {

        System.out.println(Base64Util.encodeFile("C:/img/idcardPhoto/429008199008061113.jpg"));
    }


}