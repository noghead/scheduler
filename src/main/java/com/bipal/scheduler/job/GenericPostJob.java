package com.bipal.scheduler.job;

import com.bipal.scheduler.model.SchedulerConstants;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.BoundRequestBuilder;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GenericPostJob implements Job {

    private final Logger logger = LoggerFactory.getLogger(GenericPostJob.class);

    private AsyncHttpClient asyncHttpClient;

    @Autowired
    public GenericPostJob(AsyncHttpClient asyncHttpClient) {
        this.asyncHttpClient = asyncHttpClient;
    }

    /**
     * Posts a request to trigger a task defined in the JobDataMap.
     *
     * @param context The context in which this Job is executed in.
     */
    @Override
    public void execute(JobExecutionContext context) {
        logger.info("Scheduled Job Executing: {}", context.getJobDetail().getDescription());

        String url = context
                .getJobDetail()
                .getJobDataMap()
                .getString(SchedulerConstants.POST_URL_KEY);
        String payload = context
                .getJobDetail()
                .getJobDataMap()
                .getString(SchedulerConstants.POST_PAYLOAD_KEY);

        BoundRequestBuilder requestBuilder = asyncHttpClient.preparePost(url)
                .addHeader("Content-type", "application/json")
                .setBody(payload);

        //requestBuilder.execute();

        logger.info("Scheduled Job Finished Executing: {}", context.getJobDetail().getDescription());
    }
}
