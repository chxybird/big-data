package com.bird.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.hdfs.DistributedFileSystem;
import org.apache.hadoop.hdfs.protocol.DatanodeInfo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author 李璞
 * @Date 2022/1/5 14:23
 * @Description Hadoop-HDFS 工具类
 */
@Slf4j
public class HdfsUtils {

    /**
     * NameNode地址
     */
    private static final String URL = "hdfs://192.168.78.134:8020";
    /**
     * 认证账号
     */
    private static final String AUTH_USER = "root";

    private static final Configuration CONFIGURATION = new Configuration();


    /**
     * @Author 李璞
     * @Date 2022/1/5 15:10
     * @Description 获取连接
     */
    private static FileSystem getClient() throws Exception {
        return FileSystem.get(new URI(URL), CONFIGURATION, AUTH_USER);
    }

    /**
     * @Author 李璞
     * @Date 2022/1/5 15:35
     * @Description 归还连接
     */
    private static void closeClient(FileSystem fileSystem) throws Exception {
        fileSystem.close();
    }

    /**
     * @Author 李璞
     * @Date 2022/1/5 16:16
     * @Description 判断文件或者目录是否存在
     */
    public boolean exist(String path) throws Exception {
        FileSystem client = getClient();
        boolean exists = client.exists(new Path(path));
        closeClient(client);
        return exists;
    }

    /**
     * @Author 李璞
     * @Date 2022/1/5 16:25
     * @Description 创建文件夹(如果存在等价于什么都不做)
     */
    public static boolean mkdir(String path) throws Exception {
        FileSystem client = getClient();
        boolean flag = client.mkdirs(new Path(path));
        closeClient(client);
        return flag;
    }

    /**
     * @Author 李璞
     * @Date 2022/1/5 16:25
     * @Description 删除文件夹(删除时包含该目录下的文件)或者文件 如果不存在就不操作返回false
     */
    public static boolean rm(String path) throws Exception {
        FileSystem client = getClient();
        boolean flag = client.deleteOnExit(new Path(path));
        closeClient(client);
        return flag;
    }

    /**
     * @Author 李璞
     * @Date 2022/1/6 10:37
     * @Description 文件夹或者文件重命名
     * 示例将bird目录下单awk.txt文件更名为test.txt 传参为 /bird/awk.txt  /bird/test.txt
     * 当文件不存在的时候不操作 返回false
     */
    public static boolean rename(String oldPath, String newPath) throws Exception {
        FileSystem client = getClient();
        boolean flag = client.rename(new Path(oldPath), new Path(newPath));
        closeClient(client);
        return flag;
    }

    /**
     * @Author 李璞
     * @Date 2022/1/6 11:15
     * @Description 查找当前文件夹下的所有文件/目录
     */
    public static List<FileStatus> getFileList(String path) throws Exception {
        FileSystem client = getClient();
        FileStatus fileStatus = client.getFileStatus(new Path(path));
        if (fileStatus.isFile()) {
            return null;
        }
        FileStatus[] fileStatuses = client.listStatus(new Path(path));
        ArrayList<FileStatus> result = new ArrayList<>(Arrays.asList(fileStatuses));
        closeClient(client);
        return result;
    }

    /**
     * @Author 李璞
     * @Date 2022/1/6 13:29
     * @Description 查找所有的DataNode节点信息
     */
    public static List<DatanodeInfo> getDataNodeList() throws Exception {
        DistributedFileSystem client = (DistributedFileSystem) getClient();
        DatanodeInfo[] dataNodeStats = client.getDataNodeStats();
        ArrayList<DatanodeInfo> dataNodeList = new ArrayList<>(Arrays.asList(dataNodeStats));
        closeClient(client);
        return dataNodeList;
    }

    /**
     * @Author lipu
     * @Date 2021/6/10 16:00
     * @Description 上传文件 目录不存在自己创建 文件存在覆盖
     */
    public static void upload(MultipartFile file, String path) throws Exception {
        FileSystem client = getClient();
        //获取文件名
        String originalFilename = file.getOriginalFilename();
        //组装上传的地址
        Path uploadPath = new Path(URL + path + "/" + originalFilename);
        //文件流写数据
        FSDataOutputStream outputStream = client.create(uploadPath);
        InputStream inputStream = file.getInputStream();
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
        byte[] bytes = new byte[4096];
        int len = 0;
        while (((len = bufferedInputStream.read(bytes))) > 0) {
            outputStream.write(bytes, 0, len);
        }
        bufferedInputStream.close();
        outputStream.close();
        client.close();

    }

    /**
     * @Author lipu
     * @Date 2021/6/10 16:43
     * @Description 文件下载
     */
    public static void download(HttpServletResponse response, String path) throws Exception {
        FileSystem client = getClient();
        //获取下载的文件名
        FileStatus fileStatus = client.getFileStatus(new Path(path));
        String name = fileStatus.getPath().getName();
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-disposition", "attachment;filename="
                + URLEncoder.encode(name, "utf-8"));
        //读取文件为流 这里用到缓冲流
        FSDataInputStream inputStream = client.open(new Path(path));
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
        ServletOutputStream outputStream = response.getOutputStream();
        byte[] bytes = new byte[4096];
        int len = 0;
        while (((len = bufferedInputStream.read(bytes))) > 0) {
            outputStream.write(bytes, 0, len);
        }
        bufferedInputStream.close();
        outputStream.close();
        closeClient(client);
    }

}
