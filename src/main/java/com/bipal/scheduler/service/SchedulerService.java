package com.bipal.scheduler.service;

import com.bipal.scheduler.model.JobInfo;
import com.bipal.scheduler.model.Schedule;
import org.quartz.JobKey;
import org.quartz.SchedulerException;

import java.util.Set;

public interface SchedulerService {
    /**
     * Schedules new generic job that will post <code>{@link JobInfo#postPayload}</code> to <code>{@link JobInfo#postUrl}</code>
     * on the schedule specified in <code>{@link Schedule}</code>
     *
     * @param schedule Data object that has information about the schedule.
     * @return A JobKey that references a scheduler job.
     * @throws SchedulerException if schedule could not be scheduled.
     */
    JobKey schedule(Schedule schedule) throws SchedulerException;

    /**
     * Gets a set of Job with group name equals to jobGroup.
     * @param jobGroup The name of the group to find.
     * @return Set of job keys.
     * @throws SchedulerException if schedule could not be scheduled.
     */
    Set<JobKey> getJobs(String jobGroup) throws SchedulerException;

    /**
     * Cancels a generic job.
     * @param triggerKey TriggerKey of the job to cancel.
     * @throws SchedulerException if schedule could not be scheduled.
     */
    void cancel(JobKey triggerKey) throws SchedulerException;
}
