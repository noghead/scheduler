# About the Project
This scheduler is a spring boot application that uses the [Quartz](https://github.com/quartz-scheduler).
Intended to be used to schedule anything given a schedule, url, and payload.

## Getting started
The application is ready build and use after checkout. By default it will use an in memory database to save schedules. This is intended to be used for testing purposes. 
For persisted storage, see section in application.properties to use mysql.

After checking out the repo*
1. Open a terminal.
2. cd to root of project.
3. ./gradlew bootRun

*This runs on windows as well. Use gradlew.bat in step 3 to run.

#### Use scheduler's api to schedule something.
Once the application is running. You can see the schedule's [swagger page](http://localhost:8080/swagger-ui.html)

Run the demo schedule. In a console; make a post to /scheduler/schedule with a JSON body that contains your schedule information.
```
curl -X POST --header 'Content-Type: application/json' --header 'Accept: application/json' -d '{"endTimeEpochMillis":0,"jobInfo":{"jobDescription":"Description of Job","jobGroup":"My Job Group","jobName":"My Job Name","postPayload":"Hello Scheduler","postUrl":"http://localhost:8080/scheduler/echo"},"repeatCount":2,"repeatIntervalMillis":1000,"startTimeEpochMillis":0}' 'http://localhost:8080/scheduler/schedule'
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
1. We told the scheduler to post a payload "Hello Scheduler" to localhost:8080/scheduler/echo
2. We said repeat it 2x (total of 3).
3. Thus,  we see logs from scheduler and the echo endpoint 3 times.

This is what the Schedule request payload looks like (more readable).

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
| Property | Description |
| --- | --- |
| startTimeEpochMillis | When to start the schedule |
| git diff | Show file differences that haven't been staged |


#### Configure with In memory datbase.
Use these settings in application.properties
```
spring.datasource.url=jdbc:h2:mem:AZ;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
spring.datasource.driverClassName=org.h2.Driver
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
```

#### Configure with mysql database.
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

