package com.bipal.scheduler.service;

import com.bipal.scheduler.job.GenericPostJob;
import com.bipal.scheduler.model.JobDTO;
import com.bipal.scheduler.model.ScheduleDTO;
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
    public JobKey schedule(ScheduleDTO schedule, JobDTO jobDTO) throws SchedulerException {
        JobKey jobKey = new JobKey(jobDTO.getJobName(), jobDTO.getJobGroup());

        JobDetail jobDetail = JobBuilder.newJob(GenericPostJob.class)
                .withIdentity(jobKey)
                .usingJobData(SchedulerConstants.POST_URL_KEY, jobDTO.getPostUrl())
                .usingJobData(SchedulerConstants.POST_PAYLOAD_KEY, jobDTO.getPostPayload())
                .withDescription(jobDTO.getJobDescription())
                .build();

        SimpleTrigger trigger = createTrigger(jobDetail, schedule, jobDTO);

        if (scheduler.checkExists(jobKey)) {
            scheduler.deleteJob(jobKey);
        }
        scheduler.scheduleJob(jobDetail, trigger);

        return trigger.getJobKey();
    }

    private SimpleTrigger createTrigger(JobDetail jobDetail, ScheduleDTO schedule, JobDTO jobDTO) {
        Date startTime = schedule.getStartTimeEpochMillis() == 0L ? new Date() : new Date(schedule.getStartTimeEpochMillis());
        Date endTime = schedule.getEndTimeEpochMillis() == 0L ? null : new Date(schedule.getEndTimeEpochMillis());

        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity(jobDTO.getJobName(), jobDTO.getJobGroup())
                .withDescription(jobDTO.getJobDescription())
                .startAt(startTime)
                .endAt(endTime)
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds(schedule.getRepeatIntervalSeconds())
                        .withRepeatCount(schedule.getRepeatCount())
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
