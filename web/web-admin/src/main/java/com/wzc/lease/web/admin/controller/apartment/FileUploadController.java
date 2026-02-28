package com.wzc.lease.web.admin.controller.apartment;


import com.wzc.lease.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@Tag(name = "文件管理")
@RequestMapping("/admin/file") // 定义当前控制器的统一请求路径前缀
@RestController
public class FileUploadController {

    @Operation(summary = "上传文件")
    @PostMapping("upload") // 处理 HTTP POST 请求，常用于上传或提交数据
    public Result<String> upload(@RequestParam MultipartFile file) {
        return Result.ok();
    }

}
