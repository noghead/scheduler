package com.bipal.scheduler.service;

import com.bipal.scheduler.job.GenericPostJob;
import com.bipal.scheduler.model.JobInfo;
import com.bipal.scheduler.model.ScheduleJobPayload;
import com.bipal.scheduler.model.SchedulerConstants;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Set;

@Service
public class SchedulerServiceImpl implements SchedulerService {
    private Scheduler scheduler;

    @Autowired
    public SchedulerServiceImpl(@Qualifier("scheduler") Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public JobKey schedule(ScheduleJobPayload scheduleJobPayload) throws SchedulerException {
        JobInfo jobInfo = scheduleJobPayload.getJobInfo();
        JobKey jobKey = new JobKey(jobInfo.getJobName(), jobInfo.getJobGroup());

        JobDetail jobDetail = JobBuilder.newJob(GenericPostJob.class)
                .withIdentity(jobKey)
                .usingJobData(SchedulerConstants.POST_URL_KEY, jobInfo.getPostUrl())
                .usingJobData(SchedulerConstants.POST_PAYLOAD_KEY, jobInfo.getPostPayload())
                .withDescription(jobInfo.getJobDescription())
                .build();

        SimpleTrigger trigger = createTrigger(jobDetail, scheduleJobPayload, jobInfo);

        if (scheduler.checkExists(jobKey)) {
            scheduler.deleteJob(jobKey);
        }
        scheduler.scheduleJob(jobDetail, trigger);

        return trigger.getJobKey();
    }

    private SimpleTrigger createTrigger(JobDetail jobDetail, ScheduleJobPayload scheduleJobPayload, JobInfo jobInfo) {
        Date startTime = scheduleJobPayload.getStartTimeEpochMillis() == 0L ? new Date() : new Date(scheduleJobPayload.getStartTimeEpochMillis());
        Date endTime = scheduleJobPayload.getEndTimeEpochMillis() == 0L ? null : new Date(scheduleJobPayload.getEndTimeEpochMillis());

        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity(jobInfo.getJobName(), jobInfo.getJobGroup())
                .withDescription(jobInfo.getJobDescription())
                .startAt(startTime)
                .endAt(endTime)
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInMilliseconds(scheduleJobPayload.getRepeatIntervalMillis())
                        .withRepeatCount(scheduleJobPayload.getRepeatCount())
                        .withMisfireHandlingInstructionFireNow())
                .build();

    }

    public Set<JobKey> getJobs(String jobGroup) throws SchedulerException {
        return scheduler.getJobKeys(GroupMatcher.groupEquals(jobGroup));
    }

    @Override
    public void cancel(JobKey jobKey) throws SchedulerException {
        scheduler.deleteJob(jobKey);
    }
}
