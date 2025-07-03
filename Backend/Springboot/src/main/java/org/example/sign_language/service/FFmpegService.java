package org.example.sign_language.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.example.sign_language.common.config.AppConfig;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FFmpegService {
    private final AppConfig config;

    public File mergeVideos(List<File> inputFiles, Path outputPath) {
        try {
            // 生成合并列表文件
            File listFile = createConcatFile(inputFiles);

            ProcessBuilder builder = new ProcessBuilder(
                    config.getFfmpegPath(),
                    "-f", "concat",
                    "-safe", "0",
                    "-i", listFile.getAbsolutePath(),
                    "-c", "copy",
                    outputPath.toString()
            );

            Process process = builder.start();
            int exitCode = process.waitFor();

            if (exitCode != 0) {
                throw new RuntimeException("FFmpeg failed: " +
                        IOUtils.toString(process.getErrorStream(), StandardCharsets.UTF_8));
            }

            return outputPath.toFile();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Video merge failed", e);
        }
    }

    private File createConcatFile(List<File> files) throws IOException {
        File listFile = File.createTempFile("ffmpeg-list", ".txt");
        try (BufferedWriter writer = Files.newBufferedWriter(listFile.toPath())) {
            for (File file : files) {
                writer.write("file '" + file.getAbsolutePath() + "'");
                writer.newLine();
            }
        }
        return listFile;
    }
}
