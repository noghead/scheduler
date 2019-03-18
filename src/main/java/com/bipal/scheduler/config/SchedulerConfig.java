/*
 * Copyright (c) 2019  HONEYWELL INTERNATIONAL SARL.
 * This software is a copyrighted work and/or information protected as a trade secret. Legal rights of Honeywell Inc.
 * in this software are distinct from ownership of any medium in which the software is embodied. Copyright or trade
 *  secret notices included must be reproduced in any copies authorized by Honeywell Inc. The information in this
 *  software is subject to change without notice and should not be considered as a commitment by Honeywell Inc.
 *
 */

package com.bipal.scheduler.config;


import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.quartz.QuartzAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@Configuration
public class SchedulerConfig {

    @Bean
    @Qualifier("scheduler")
    Scheduler getQuartzScheduler(QuartzAutoConfiguration quartzAutoConfiguration) throws SchedulerException {
        SchedulerFactoryBean schedulerFactoryBean = quartzAutoConfiguration.quartzScheduler();
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        scheduler.start();
        return scheduler;
    }
}
