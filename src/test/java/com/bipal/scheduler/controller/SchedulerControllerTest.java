package com.bipal.scheduler.controller;

import com.bipal.scheduler.model.JobInfo;
import com.bipal.scheduler.model.ScheduleJobPayload;
import com.bipal.scheduler.service.SchedulerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SchedulerControllerTest {

    @MockBean
    private SchedulerService schedulerService;

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext ctx;

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        initMocks(this);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void schedule() throws Exception {
        long now = System.currentTimeMillis();
        JobInfo jobInfo = new JobInfo("Jerb", "Good_Job", "They Tuk Our Jerbs!", "http://localhost:8080/test", "{}");
        ScheduleJobPayload scheduleJobPayload = new ScheduleJobPayload(now, now + 1000, 100, 1, jobInfo );
        String url = "/scheduler/schedule";
        String payload = objectMapper.writeValueAsString(scheduleJobPayload);
        this.mockMvc.perform(
                post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().is2xxSuccessful());

        verify(schedulerService).schedule(scheduleJobPayload);
    }

    @Test
    public void scheduleWithBadJobDetails() throws Exception {
        long now = System.currentTimeMillis();
        JobInfo jobInfo = new JobInfo("Jerb", "Good_Job", "They Tuk Our Jerbs!", "http://localhost:8080/test", "{}");
        ScheduleJobPayload scheduleJobPayload = new ScheduleJobPayload(now, now + 1000, 100, 1, jobInfo );
        String url = "/scheduler/schedule";
        String payload = objectMapper.writeValueAsString(scheduleJobPayload);

        when(schedulerService.schedule(scheduleJobPayload)).thenThrow(new IllegalArgumentException("Test"));
        this.mockMvc.perform(
                post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest());

        verify(schedulerService).schedule(scheduleJobPayload);
    }

    @Test
    public void scheduleWithThrowsSchedulerException() throws Exception {
        long now = System.currentTimeMillis();
        JobInfo jobInfo = new JobInfo("Jerb", "Good_Job", "They Tuk Our Jerbs!", "http://localhost:8080/test", "{}");
        ScheduleJobPayload scheduleJobPayload = new ScheduleJobPayload(now, now + 1000, 100, 1, jobInfo );
        String url = "/scheduler/schedule";
        String payload = objectMapper.writeValueAsString(scheduleJobPayload);

        when(schedulerService.schedule(scheduleJobPayload)).thenThrow(new SchedulerException("Test"));
        this.mockMvc.perform(
                post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isInternalServerError());

        verify(schedulerService).schedule(scheduleJobPayload);
    }

}