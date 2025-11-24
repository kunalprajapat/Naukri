package com.naukri.Naukri.service;



import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class DynamicSchedulerService {

    private final NaukriUploadService uploadService;
    private final TaskScheduler scheduler;

    public DynamicSchedulerService(NaukriUploadService uploadService) {
        this.uploadService = uploadService;
        ThreadPoolTaskScheduler ts = new ThreadPoolTaskScheduler();
        ts.setPoolSize(5);
        ts.initialize();
        this.scheduler = ts;
    }

    public void schedule(String dateTimeString, String driveFileUrl) {
        LocalDateTime ldt = LocalDateTime.parse(dateTimeString.replace(" ", "T")); // yyyy-MM-ddTHH:mm
        Date date = Date.from(ldt.atZone(ZoneId.of("Asia/Kolkata")).toInstant());

        scheduler.schedule(() -> uploadService.uploadFromDrive(driveFileUrl), date);
        System.out.println("Scheduled upload at " + dateTimeString);
    }
}


