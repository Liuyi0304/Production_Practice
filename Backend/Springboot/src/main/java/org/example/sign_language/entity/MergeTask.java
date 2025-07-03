package org.example.sign_language.entity;

import java.util.List;

public class MergeTask {
    private String taskId;
    private List<String> videoUrls;
    private Status status;
    private String outputFile;
    private String errorMessage;

    public MergeTask() {
        this.status = Status.PENDING;
    }

    public MergeTask(String taskId, List<String> videoUrls) {
        this.taskId = taskId;
        this.videoUrls = videoUrls;
        this.status = Status.PENDING;
    }

    public enum Status {
        PENDING, PROCESSING, COMPLETED, FAILED
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public List<String> getVideoUrls() {
        return videoUrls;
    }

    public void setVideoUrls(List<String> videoUrls) {
        this.videoUrls = videoUrls;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getOutputFile() {
        return outputFile;
    }

    public void setOutputFile(String outputFile) {
        this.outputFile = outputFile;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}