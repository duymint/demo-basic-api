package com.example.demoapi11.service;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class StudentJob extends QuartzJobBean {

    @Autowired
    private StudentService studentService;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        studentService.checkDOBWithQuartz();
    }
}
