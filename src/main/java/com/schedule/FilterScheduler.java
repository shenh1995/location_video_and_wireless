package com.schedule;

import java.util.HashSet;



import org.apache.log4j.Logger;

import org.quartz.CronScheduleBuilder;

import org.quartz.JobBuilder;

import org.quartz.JobDetail;

import org.quartz.Scheduler;

import org.quartz.SchedulerException;

import org.quartz.SchedulerFactory;

import org.quartz.Trigger;

import org.quartz.TriggerBuilder;

import org.quartz.impl.StdSchedulerFactory;

public class FilterScheduler {

	private static Logger logger = Logger.getLogger(FilterScheduler.class);
	public static HashSet<String> filterMacSet = new HashSet<String>();
	  
    //创建调度器
    public static Scheduler getScheduler() throws SchedulerException{

        SchedulerFactory schedulerFactory = new StdSchedulerFactory();

        return schedulerFactory.getScheduler();

    }

    /**
     * @throws SchedulerException
     * 这里是每天凌晨1点到5点整点触发
     */
    public static void schedulerJob() throws SchedulerException{
    	logger.info("定时器开始启动....");
        //创建任务
        JobDetail jobDetail = JobBuilder.newJob(FilterJob.class).withIdentity("job1", "group1").build();
        //创建触发器，每天
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger1", "group3")
                            .withSchedule(CronScheduleBuilder.cronSchedule("0 0 1-5 * * ?")) //秒 分 时
                            .build();
        Scheduler scheduler = getScheduler();
        //将任务及其触发器放入调度器	
        scheduler.scheduleJob(jobDetail, trigger);
        //调度器开始调度任务
        scheduler.start();
    }

    public static void main(String[] args) throws SchedulerException {
        FilterScheduler.schedulerJob();
    }
}