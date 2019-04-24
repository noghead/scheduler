package com.bipal.scheduler.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ScheduleJobPayload {
    private long startTimeEpochMillis;
    private long endTimeEpochMillis;
    private int repeatIntervalMillis;
    private int repeatCount;

    private JobInfo jobInfo;

    public ScheduleJobPayload(long startTimeEpochMillis, long endTimeEpochMillis, int repeatIntervalMillis, int repeatCount, JobInfo jobInfo) {
        this.startTimeEpochMillis = startTimeEpochMillis;
        this.endTimeEpochMillis = endTimeEpochMillis;
        this.repeatIntervalMillis = repeatIntervalMillis;
        this.repeatCount = repeatCount;
        this.jobInfo = jobInfo;
    }

    private ScheduleJobPayload() {
    }

    public long getStartTimeEpochMillis() {
        return startTimeEpochMillis;
    }

    public long getEndTimeEpochMillis() {
        return endTimeEpochMillis;
    }

    public int getRepeatIntervalMillis() {
        return repeatIntervalMillis;
    }

    public int getRepeatCount() {
        return repeatCount;
    }

    public JobInfo getJobInfo() {
        return jobInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScheduleJobPayload scheduleJobPayload = (ScheduleJobPayload) o;
        return startTimeEpochMillis == scheduleJobPayload.startTimeEpochMillis &&
                endTimeEpochMillis == scheduleJobPayload.endTimeEpochMillis &&
                repeatIntervalMillis == scheduleJobPayload.repeatIntervalMillis &&
                repeatCount == scheduleJobPayload.repeatCount &&
                Objects.equals(jobInfo, scheduleJobPayload.jobInfo);
    }
}
