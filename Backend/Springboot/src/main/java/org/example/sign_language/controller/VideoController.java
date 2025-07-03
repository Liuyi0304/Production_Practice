package org.example.sign_language.controller;


import org.apache.http.HttpHeaders;
import org.example.sign_language.common.Result;
import org.example.sign_language.entity.ApiResponse;
import org.example.sign_language.entity.MergeTask;
import org.example.sign_language.entity.Video;
import org.example.sign_language.service.DeepSeekService;
import org.example.sign_language.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class VideoController {
    @Autowired
    private VideoService videoService;
    @Autowired
    private DeepSeekService deepSeekService;

    @GetMapping("/video/{title}")
    public Result selectByTitle(@PathVariable String title) {
        Video video = videoService.selectVideo(title);
        if(video==null){
            return Result.error("4001","出错了……");
        }
        return Result.success(video);
    }
    @GetMapping("/words_to_videos/{sentence}")
    public Result selectByWords(@PathVariable String sentence) throws InterruptedException {
        String content = deepSeekService.analyzeSentence(sentence);

        if (content == null) {
            return Result.error("4002", "Failed to analyze sentence");
        }
        content=content.substring(content.indexOf("[")+1,content.indexOf("]"));

        List<String> words = new ArrayList<>();
        int l=-1,r=0;
        for(int i=0;i<content.length();i++){
            if(content.charAt(i)=='"'){
                if(l<r){
                    l=i;
                }else{
                    r=i;
                    words.add(content.substring(l+1,r));
                }
            }
        }
        List<String> videoUrls=new ArrayList<>();
        for(String word : words){
            Video video = videoService.selectVideo(word);
            if(video==null){
                continue;
            }
            videoUrls.add(video.getPath());
        }
        if(videoUrls.isEmpty()) return Result.error("4002","视频出错了");
        String path="http://localhost:7877/download/";
        String taskId = videoService.createMergeTask(videoUrls);
        MergeTask task = videoService.getTaskStatus(taskId);
//        while(!task.getStatus().equals(MergeTask.Status.COMPLETED)){
//            task = videoService.getTaskStatus(taskId);
//        }
        Thread.sleep(3000);
        path+=taskId+".mp4";
        return Result.success(path);
    }


//    @GetMapping("/status/{taskId}")
//    public ResponseEntity<ApiResponse> getStatus(
//            @PathVariable String taskId) {
//        MergeTask task = videoService.getTaskStatus(taskId);
//        return ResponseEntity.ok(ApiResponse.success("Task status", task));
//    }

//    @GetMapping("/download/{filename}")
//    public ResponseEntity<Resource> download(
//            @PathVariable String filename) {
//        Resource resource = videoService.getMergedVideo(filename);
//        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_TYPE, "video/mp4")
//                .body(resource);
//    }
}
