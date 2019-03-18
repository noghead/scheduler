package com.bipal.scheduler.job;

import com.bipal.scheduler.model.SchedulerConstants;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.BoundRequestBuilder;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class GenericPostJobTest {

    private Job job;

    @Mock
    AsyncHttpClient asyncHttpClient;

    @Before
    public void setUp() {
        initMocks(this);
        job = new GenericPostJob(asyncHttpClient);
    }

    @Test
    public void executeDoesPost() throws JobExecutionException, InterruptedException {
        JobExecutionContext context = mock(JobExecutionContext.class);
        JobDetail jobDetails = mock(JobDetail.class);
        when(context.getJobDetail()).thenReturn(jobDetails);
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put(SchedulerConstants.POST_URL_KEY, "http://localhost:8090/what");
        jobDataMap.put(SchedulerConstants.POST_PAYLOAD_KEY, "{\"what\":\"up\"}");

        when(jobDetails.getJobDataMap()).thenReturn(jobDataMap);

        BoundRequestBuilder boundedRequestBuilder = mock(BoundRequestBuilder.class);
        when(asyncHttpClient.preparePost(anyString())).thenReturn(boundedRequestBuilder);
        when(boundedRequestBuilder.addHeader(anyString(), anyString())).thenReturn(boundedRequestBuilder);
        when(boundedRequestBuilder.setBody(anyString())).thenReturn(boundedRequestBuilder);

        job.execute(context);
        verify(boundedRequestBuilder).execute();
    }
}