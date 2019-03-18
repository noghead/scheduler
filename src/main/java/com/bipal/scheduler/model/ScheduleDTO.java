package com.bipal.scheduler.model;


public class ScheduleDTO {
    private long startTimeEpochMillis;
    private long endTimeEpochMillis;
    private int repeatIntervalSeconds;
    private int repeatCount;

    public ScheduleDTO(long startTimeEpochMillis, long endTimeEpochMillis, int repeatIntervalSeconds, int repeatCount) {
        this.startTimeEpochMillis = startTimeEpochMillis;
        this.endTimeEpochMillis = endTimeEpochMillis;
        this.repeatIntervalSeconds = repeatIntervalSeconds;
        this.repeatCount = repeatCount;
    }

    public long getStartTimeEpochMillis() {
        return startTimeEpochMillis;
    }

    public long getEndTimeEpochMillis() {
        return endTimeEpochMillis;
    }

    public int getRepeatIntervalSeconds() {
        return repeatIntervalSeconds;
    }

    public int getRepeatCount() {
        return repeatCount;
    }
}
