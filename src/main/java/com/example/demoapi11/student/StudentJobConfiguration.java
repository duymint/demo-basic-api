package com.example.demoapi11.student;

import com.example.demoapi11.service.StudentJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StudentJobConfiguration {

    @Bean
    public JobDetail studentCheckJobDetail() {
        return JobBuilder
                .newJob(StudentJob.class)
                .withIdentity(JobKey.jobKey("studentCheckBirthdayJob"))
                .storeDurably()
                .build();
    }
    @Bean
    public Trigger studentJobTrigger() {
        return TriggerBuilder
                .newTrigger()
                .forJob(studentCheckJobDetail())
                .withIdentity(TriggerKey.triggerKey("studentCheckBirthdayJob"))
                .withSchedule(CronScheduleBuilder.cronSchedule("*/5 * * * * ?"))
                .build();
    }
}
