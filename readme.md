# About the Project
This scheduler is a spring boot application that allows you to schedule anything given url, payload and a schedule. It is powered by [Quartz](https://github.com/quartz-scheduler).

#### Some Use Cases
- Need to trigger an event at some specific time with or without repetitions.  Use scheduler to create a job that POSTs a payload to your URL at a specific time.
- Need to retry after some time. Something failed, use the scheduler to send you an event some time in the future.
- One microservice needs to send an event to another; immidiately or later.

## Getting started
The application is ready to build and use after checkout. By default it will use an in memory database to save schedules. This is intended to be used for testing purposes. 
For persisted storage, see comments in application.properties or see section below about configuring with mysql.

#### Running the scheduler appliation.

After checking out the repo*
1. Open a terminal.
2. cd to root of project.
3. ./gradlew bootRun

*This runs on windows as well. Use gradlew.bat in step 3 to run.

#### Quick Demo.
Once the application is running. You can see the scheduleJobPayload's swagger page at http://localhost:8080/swagger-ui.html

Use this JSON payload in swagger (or postman) to POST to http://localhost:8080/scheduler/scheduleJobPayload
```json
{
  "startTimeEpochMillis": 0,
  "endTimeEpochMillis": 0,
  "repeatIntervalMillis": 1000,
  "repeatCount": 2,
  "jobInfo": {
    "jobName": "My Job Name",
    "jobGroup": "My Job Group",
    "jobDescription": "Description of Job",
    "postUrl": "http://localhost:8080/scheduler/echo",
    "postPayload": "Hello Scheduler"
  }
}
```

Alternatively,  you can use curl.
```
curl -X POST --header 'Content-Type: application/json' --header 'Accept: application/json' -d '{"endTimeEpochMillis":0,"jobInfo":{"jobDescription":"Description of Job","jobGroup":"My Job Group","jobName":"My Job Name","postPayload":"Hello Scheduler","postUrl":"http://localhost:8080/scheduler/echo"},"repeatCount":2,"repeatIntervalMillis":1000,"startTimeEpochMillis":0}' 'http://localhost:8080/scheduler/scheduleJobPayload'
```

You should see this output from the application:
```
Scheduled Job Executing: Description of Job
Scheduled Job Finished Executing: Description of Job
Echoing Hello Scheduler
Scheduled Job Executing: Description of Job
Scheduled Job Finished Executing: Description of Job
Echoing Hello Scheduler
Scheduled Job Executing: Description of Job
Scheduled Job Finished Executing: Description of Job
Echoing Hello Scheduler
```

What is happening
1. Our job description was "Description of Job" so we see that its executing.
2. Next we see that its finished executing.
3. Then we see a log echoing "Hello Scheduler" which is what was in our post payload. Normally you'd a POST url for your own app. For demo purposes, this is posting to an echo endpoint on the Scheduler itself.
4. This executes three times because we scheduled this job to repeat twice. I.E. It executes the job and repeats two more times.

### Schedule Request Payload Explained.

| Property | Description |
| --- | --- |
| startTimeEpochMillis | When to start the scheduleJobPayload in Epoch Millis. 0 For when scheduler recieves the request. |
| endTimeEpochMillis | When to end the scheduleJobPayload. Schedule will end after all repeat intervals are completed or end time is reached. |
| repeatIntervalMillis | Time between triggering the scheduleJobPayload again. |
| repeatCount | How many times to repeat. 0 for execute once and do not repeat. |
| jobInfo.jobName | Name of job, name + group form a unique key. Can be used to perform CRUD operations on job |
| jobInfo.jobGroup | Group of job, name + group form a unique key. Can be used to perform CRUD operations on job |
| jobInfo.jobDescription | A field to describe your job. |
| jobInfo.postUrl | When the job executes, the URL it will post to. |
| jobInfo.postPayload | When the job executes, the payload it will send. |

### Configure with In memory datbase.
Use these settings in application.properties
```
spring.datasource.url=jdbc:h2:mem:AZ;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
spring.datasource.driverClassName=org.h2.Driver
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
```

### Configure with mysql database.
Use these settings in application.properties. Replace values for your database.
```
spring.quartz.job-store-type=jdbc
spring.datasource.url=jdbc:mysql://127.0.0.1:3306/quartz_scheduler?serverTimezone=GMT&createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=password
spring.quartz.jdbc.initialize-schema=always
spring.quartz.jdbc.schema=classpath:db/tables_mysql_innodb.sql
```

The application should create a database schema and tables on its own. Default schema is quartz_scheduler.
The table construction is defined in /resources/db/tables_mysql_innodb.sql

