package org.example.sign_language.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.example.sign_language.common.Result;
import org.example.sign_language.common.enums.ResultCodeEnum;
import org.example.sign_language.common.enums.RoleEnum;
import org.example.sign_language.entity.Account;
import org.example.sign_language.exception.CustomException;
import org.example.sign_language.service.UserService;
import org.example.sign_language.service.VideoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

@RestController
public class WebController {
    @Autowired
    private VideoService videoService;
    @jakarta.annotation.Resource
    private UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(WebController.class);
    private static final String filePath = System.getProperty("user.dir") + "/src/main/resources/static/videos/";
    private static final String filePath2 = System.getProperty("user.dir") + "/output/";


    @GetMapping("/")
    public Result hello_world(){
        return Result.success("访问成功");
    }

    @GetMapping("/videos/{fileName}")
    public void videoPath(@PathVariable String fileName, HttpServletResponse response){
        OutputStream os;
        try{
            if(StrUtil.isNotEmpty(fileName)){
                response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
                response.setContentType("application/octet-stream");
                byte[] bytes = FileUtil.readBytes(filePath + fileName);
                os = response.getOutputStream();
                os.write(bytes);
                os.flush();
                os.close();
            }
        }catch (Exception e){
            System.out.println("视频传输失败");
        }
    }

    @GetMapping("/output/{fileName}")
    public void mergedVideoPath(@PathVariable String fileName, HttpServletResponse response){
        OutputStream os;
        try{
            if(StrUtil.isNotEmpty(fileName)){
                response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
                response.setContentType("application/octet-stream");
                byte[] bytes = FileUtil.readBytes(filePath2 + fileName);
                os = response.getOutputStream();
                os.write(bytes);
                os.flush();
                os.close();
            }
        }catch (Exception e){
            logger.error("视频传输失败", e);
            System.out.println("视频传输失败");
        }
    }
    @GetMapping("/download/{filename}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) throws IOException {
        Resource resource = videoService.getMergedVideo(filename);
        if (resource == null || !resource.exists()) {
            logger.error("文件不存在: {}", filename);

            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "video/mp4")
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + filename + "\"")
                .body(resource);
    }
    @PostMapping("/login")
    public Result login(@RequestBody Account account){

        if(ObjectUtil.isEmpty(account.getUsername())||ObjectUtil.isEmpty(account.getPassword())|| ObjectUtil.isEmpty(account.getRole())){
            return Result.error(ResultCodeEnum.PARAM_LOST_ERROR);
        }
        System.out.println(account);
        if(RoleEnum.USER.name().equals(account.getRole())){
            account = userService.login(account);
        }else {
            throw new CustomException(ResultCodeEnum.PARAM_LOST_ERROR);
        }
        return Result.success(account);
    }
    @PostMapping("/register")
    public Result register(@RequestBody Account account){
        if(StrUtil.isBlank(account.getUsername())||StrUtil.isBlank(account.getPassword())||ObjectUtil.isEmpty(account.getRole())){
            return Result.error(ResultCodeEnum.PARAM_LOST_ERROR);
        }
        account.setAvatar("http://localhost:7877/picture/default_avatar.jpg");
        if(RoleEnum.USER.name().equals(account.getRole())){
            userService.register(account);
        }else{
            throw new CustomException(ResultCodeEnum.PARAM_LOST_ERROR);
        }
        return Result.success();
    }
    @PutMapping("/updatePassword")
    public Result updatePassword(@RequestBody Account account){
        if(StrUtil.isBlank(account.getUsername()) || StrUtil.isBlank(account.getPassword())
                || ObjectUtil.isEmpty(account.getNewPassword())){
            return Result.error(ResultCodeEnum.PARAM_LOST_ERROR);
        }
        if(RoleEnum.USER.name().equals(account.getRole())){
            userService.updatePassword(account);
        }
        return Result.success();
    }
}

