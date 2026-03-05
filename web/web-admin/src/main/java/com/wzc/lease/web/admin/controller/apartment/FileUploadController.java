package com.wzc.lease.web.admin.controller.apartment;


import com.wzc.lease.common.result.Result;
import com.wzc.lease.web.admin.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@Tag(name = "文件管理")
@RequestMapping("/admin/file") // 定义当前控制器的统一请求路径前缀
@RestController
public class FileUploadController {

    @Autowired
    private FileService service;

    @Operation(summary = "上传文件")
    @PostMapping("upload") // 处理 HTTP POST 请求，常用于上传或提交数据
    public Result<String> upload(@RequestParam MultipartFile file) {
        //file.getBytes();//上传小文件使用
        //file.getInputStream()
//        System.out.println(file.getName());
//        System.out.println(file.getOriginalFilename());
        try {
            String url = service.upload(file);
            return Result.ok(url);
        } catch (Exception e) {
            return Result.fail();
        }
    }

}
