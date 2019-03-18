package com.bipal.scheduler.service;

import com.bipal.scheduler.model.JobDTO;
import com.bipal.scheduler.model.ScheduleDTO;
import com.bipal.scheduler.model.SchedulerConstants;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.internal.util.collections.Sets;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.matchers.GroupMatcher;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class SchedulerServiceTest {

    private SchedulerService schedulerService;

    @Mock
    Scheduler scheduler;

    @Before
    public void setUp() {
        initMocks(this);
        schedulerService = new SchedulerServiceImpl(scheduler);
    }

    @Test
    public void scheduleSimpleSchedule() throws SchedulerException {
        long startTime = System.currentTimeMillis();
        long endTime = startTime + TimeUnit.DAYS.toMillis(1);
        int repeatInterval = 1;
        int repeatCount = 2;
        ScheduleDTO scheduleDTO = new ScheduleDTO(startTime, endTime, repeatInterval, repeatCount);

        JobDTO jobDTO = new JobDTO("test", "group", "test job", "testurl.com/test", "{\"hello\"}");

        JobKey jobKey = schedulerService.schedule(scheduleDTO, jobDTO);

        ArgumentCaptor<JobDetail> captorJobDetails = ArgumentCaptor.forClass(JobDetail.class);
        ArgumentCaptor<Trigger> captorTrigger = ArgumentCaptor.forClass(Trigger.class);

        verify(scheduler).scheduleJob(captorJobDetails.capture(), captorTrigger.capture());
        assertEquals("test", captorJobDetails.getValue().getKey().getName());
        assertEquals("group", captorJobDetails.getValue().getKey().getGroup());
        assertEquals("testurl.com/test", captorJobDetails.getValue().getJobDataMap().getString(SchedulerConstants.POST_URL_KEY));
        assertEquals("{\"hello\"}", captorJobDetails.getValue().getJobDataMap().getString(SchedulerConstants.POST_PAYLOAD_KEY));

        assertEquals(startTime, captorTrigger.getValue().getStartTime().getTime(), 100L);

        assertNotNull(jobKey);
        assertEquals("test", jobKey.getName());
        assertEquals("group", jobKey.getGroup());
    }

    @Test
    public void getJobs() throws SchedulerException {
        Set<JobKey> jobkeys = Sets.newSet(new JobKey("test", "JobGroup"), new JobKey("test2", "JobGroup"));
        when(scheduler.getJobKeys(GroupMatcher.groupEquals("JobGroup"))).thenReturn(jobkeys);
        Set<JobKey> result = schedulerService.getJobs("JobGroup");
        assertEquals(jobkeys, result);
    }

    @Test
    public void cancelJob() throws SchedulerException {
        JobKey triggerKey = new JobKey("test", "group");
        schedulerService.cancel(triggerKey);
        verify(scheduler).deleteJob(triggerKey);
    }
}