package com.wzc.lease.web.admin.service.impl; // 声明包路径

import com.wzc.lease.common.minio.MinioProperties; // 导入Minio配置属性类（存放endpoint、accessKey等配置）
import com.wzc.lease.web.admin.service.FileService; // 导入文件服务接口
import io.minio.*; // 导入Minio SDK相关类（MinioClient、各种Args构建器等）
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Autowired; // 导入自动注入注解
import org.springframework.stereotype.Service; // 导入Service注解，标记为Spring服务层组件
import org.springframework.web.multipart.MultipartFile; // 导入MultipartFile，用于接收前端上传的文件

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat; // 导入日期格式化工具，用于生成日期目录
import java.util.Date; // 导入日期类
import java.util.UUID; // 导入UUID工具类，用于生成唯一文件名，防止文件重名覆盖

/**
 * 文件服务实现类 —— 负责将前端上传的文件存储到Minio对象存储服务中
 */
@Service // 标记为Spring的Service组件，Spring会自动扫描并创建该类的Bean
public class FileServiceImpl implements FileService {

    @Autowired // 自动注入MinioClient（由MinioConfiguration中的@Bean方法创建）
    private MinioClient minioClient;

    @Autowired // 自动注入Minio配置属性（包含endpoint、accessKey、secretKey、bucketName）
    private MinioProperties minioProperties;

    /**
     * 文件上传方法
     * 
     * @param file 前端传过来的文件对象
     * @return 上传结果（目前返回空字符串，后续可改为返回文件访问URL）
     */
    @Override
    public String upload(MultipartFile file) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
            // 第一步：检查存储桶（bucket）是否已经存在
            // 存储桶就像一个"文件夹"，所有上传的文件都放在里面
            boolean b = minioClient.bucketExists(
                    BucketExistsArgs.builder()
                            .bucket(minioProperties.getBucketName()) // 从配置中获取桶名（如"lease"）
                            .build());

            // 第二步：如果桶不存在，就创建一个新桶，并设置访问策略
            if (!b) {
                // 创建桶
                minioClient.makeBucket(
                        MakeBucketArgs.builder()
                                .bucket(minioProperties.getBucketName())
                                .build());

                // 设置桶的访问策略为"公开可读"
                // 这样上传的图片/文件可以通过URL直接在浏览器中访问，无需登录认证
                minioClient.setBucketPolicy(
                        SetBucketPolicyArgs.builder()
                                .bucket(minioProperties.getBucketName())
                                .config(createBucketPolicyConfig(minioProperties.getBucketName()))
                                .build());
            }

            // 第三步：构造文件存储路径
            // 格式：日期目录/UUID-原始文件名，例如：20260305/a1b2c3d4-photo.jpg
            // 用日期分目录是为了方便管理，用UUID前缀是为了防止同名文件互相覆盖
            String filename = new SimpleDateFormat("yyyyMMdd").format(new Date())
                    + "/" + UUID.randomUUID() + "-" + file.getOriginalFilename();

            // 第四步：将文件上传到Minio服务器
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(minioProperties.getBucketName()) // 指定上传到哪个桶
                            .stream(file.getInputStream(), file.getSize(), -1) // 文件流、文件大小、-1表示自动分片
                            .object(filename) // 指定文件在桶中的存储路径和名称
                            .build());
            return minioProperties.getEndpoint() + "/" + minioProperties.getBucketName() + "/" + filename;
    }

    /**
     * 生成桶的公开读取策略（JSON格式）
     *
     * <p>
     * 这个策略的含义是：允许任何人（Principal: *）对桶中的所有文件（Resource: arn:aws:s3:::桶名/*）
     * 执行读取操作（Action: s3:GetObject）。
     * 简单说就是：让桶里的文件可以通过URL公开访问（比如在网页上显示图片）
     *
     * @param bucketName 桶名称，会替换JSON中的%s占位符
     * @return 符合S3标准的权限策略JSON字符串
     */
    private String createBucketPolicyConfig(String bucketName) {

        return """
                {
                  "Statement" : [ {
                    "Action" : "s3:GetObject",
                    "Effect" : "Allow",
                    "Principal" : "*",
                    "Resource" : "arn:aws:s3:::%s/*"
                  } ],
                  "Version" : "2012-10-17"
                }
                """.formatted(bucketName); // 用桶名替换%s占位符，如 arn:aws:s3:::lease/*
    }
}
