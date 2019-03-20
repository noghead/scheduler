package com.bipal.scheduler.controller;

import com.bipal.scheduler.model.Schedule;
import com.bipal.scheduler.service.SchedulerService;
import org.quartz.SchedulerException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/scheduler")
public class SchedulerController {

    private SchedulerService schedulerService;

    public SchedulerController(SchedulerService schedulerService) {
        this.schedulerService = schedulerService;
    }

    @RequestMapping(value = "/schedule", method = RequestMethod.POST)
    public ResponseEntity<Schedule> schedule(@RequestBody Schedule schedule) {
        try {
            schedulerService.schedule(schedule);
            return ResponseEntity.ok(schedule);
        } catch (SchedulerException e) {
            return new ResponseEntity<>(schedule, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(schedule, HttpStatus.BAD_REQUEST);
        }
    }
}
