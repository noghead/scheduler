package com.bipal.scheduler.controller;

import com.bipal.scheduler.model.ScheduleJobPayload;
import com.bipal.scheduler.service.SchedulerService;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/scheduler")
public class SchedulerController {
    private final Logger logger = LoggerFactory.getLogger(SchedulerController.class);

    private SchedulerService schedulerService;

    public SchedulerController(SchedulerService schedulerService) {
        this.schedulerService = schedulerService;
    }

    @RequestMapping(value = "/schedule", method = RequestMethod.POST)
    public ResponseEntity<ScheduleJobPayload> schedule(@RequestBody ScheduleJobPayload scheduleJobPayload) {
        try {
            schedulerService.schedule(scheduleJobPayload);
            return ResponseEntity.ok(scheduleJobPayload);
        } catch (SchedulerException e) {
            return new ResponseEntity<>(scheduleJobPayload, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(scheduleJobPayload, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/echo", method = RequestMethod.POST)
    public ResponseEntity<String> schedule(@RequestBody String message) {
        logger.info("Echoing {}", message);
        return ResponseEntity.ok(message);
    }
}
