package com.bipal.scheduler.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JobInfo {
    private String jobName;
    private String jobGroup;
    private String jobDescription;
    private String postUrl;
    private String postPayload;

    public JobInfo(String jobName, String jobGroup, String jobDescription, String postUrl, String postPayload) {
        this.jobName = jobName;
        this.jobGroup = jobGroup;
        this.jobDescription = jobDescription;
        this.postUrl = postUrl;
        this.postPayload = postPayload;
    }

    private JobInfo() {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JobInfo jobInfo = (JobInfo) o;
        return Objects.equals(jobName, jobInfo.jobName) &&
                Objects.equals(jobGroup, jobInfo.jobGroup) &&
                Objects.equals(jobDescription, jobInfo.jobDescription) &&
                Objects.equals(postUrl, jobInfo.postUrl) &&
                Objects.equals(postPayload, jobInfo.postPayload);
    }
}
