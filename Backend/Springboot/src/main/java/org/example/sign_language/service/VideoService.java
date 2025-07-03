package org.example.sign_language.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.apache.commons.io.FileUtils;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.example.sign_language.common.config.AppConfig;
import org.example.sign_language.entity.MergeTask;
import org.example.sign_language.entity.Video;
import org.example.sign_language.mapper.VideoMapper;

import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;


@Service
public class VideoService {
    private static final Logger logger = LoggerFactory.getLogger(VideoService.class);
    @Autowired
    private VideoMapper videoMapper;

    @Autowired
    private AppConfig config;

    @Autowired
    private FFmpegService ffmpegService;

    private final Map<String, MergeTask> taskStore = new ConcurrentHashMap<>();

    @Transactional
    public Video selectVideo(String title) {
        List<Video> videos = videoMapper.selectVideo(title);
        if (videos.isEmpty()) {
            return null;
        }
        return videos.getFirst();
    }

    public String createMergeTask(List<String> videoUrls) {
        String taskId = UUID.randomUUID().toString();
        MergeTask task = new MergeTask(taskId, videoUrls);
        taskStore.put(taskId, task);
        CompletableFuture.runAsync(() -> processTask(task));
        return taskId;
    }

    private void processTask(MergeTask task) {
        try {
            task.setStatus(MergeTask.Status.PROCESSING);

            // 1. 下载视频
            List<File> downloadedFiles = downloadVideos(task.getVideoUrls());

            // 2. 合并视频
            File outputFile = ffmpegService.mergeVideos(
                    downloadedFiles,
                    Paths.get(config.getOutputDir(), task.getTaskId() + ".mp4")
            );

            // 3. 清理临时文件
            cleanupTempFiles(downloadedFiles);

            task.setOutputFile(outputFile.getName());
            task.setStatus(MergeTask.Status.COMPLETED);
        } catch (Exception e) {
            task.setStatus(MergeTask.Status.FAILED);
            task.setErrorMessage(e.getMessage());
        }
    }

    private List<File> downloadVideos(List<String> urls) {
        return urls.stream().map(url -> {
            CloseableHttpClient client = HttpClients.createDefault();
            try {
                HttpGet request = new HttpGet(url);

                // 创建目标文件路径
                Path outputPath = Paths.get(config.getTempDir(), getFilenameFromUrl(url));
                File outputFile = outputPath.toFile();

                // 确保目录存在
                Files.createDirectories(outputPath.getParent());

                // 执行下载
                try (InputStream inputStream = client.execute(request).getEntity().getContent()) {
                    FileUtils.copyInputStreamToFile(inputStream, outputFile);
                }

                // 验证文件
                if (!outputFile.exists()) {
                    throw new IOException("文件未创建: " + outputFile.getName());
                }
                if (outputFile.length() == 0) {
                    throw new IOException("空文件: " + outputFile.getName());
                }
                return outputFile;
            } catch (IOException e) {
                throw new UncheckedIOException("视频下载失败: " + url, e);
            } finally {
                try {
                    client.close();
                } catch (IOException e) {
                    logger.error("HTTP客户端关闭失败", e);
                }
            }
        }).collect(Collectors.toList());
    }

    private String getFilenameFromUrl(String url) {
        return url.substring(url.lastIndexOf('/') + 1);
    }

    public Resource getMergedVideo(String filename) {
        Path path = Paths.get(config.getOutputDir(), filename);
        return new FileSystemResource(path);
    }

    public MergeTask getTaskStatus(String taskId) {
        return taskStore.getOrDefault(taskId, new MergeTask());
    }

    private void cleanupTempFiles(List<File> files) {
        for (File file : files) {
            if (file.exists()) {
                file.delete();
            }
        }
    }
}