package com.bipal.scheduler.model;


public class JobDTO {
    private String jobName;
    private String jobGroup;
    private String jobDescription;
    private String postUrl;
    private String postPayload;

    public JobDTO(String jobName, String jobGroup, String jobDescription, String postUrl, String postPayload) {
        this.jobName = jobName;
        this.jobGroup = jobGroup;
        this.jobDescription = jobDescription;
        this.postUrl = postUrl;
        this.postPayload = postPayload;
    }

    public String getJobName() {
        return jobName;
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public String getPostUrl() {
        return postUrl;
    }

    public String getPostPayload() {
        return postPayload;
    }
}
