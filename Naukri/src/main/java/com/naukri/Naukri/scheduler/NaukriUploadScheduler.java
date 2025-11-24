package com.naukri.Naukri.scheduler;


import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.naukri.Naukri.service.NaukriUploadService;

@Component
public class NaukriUploadScheduler {

    private final NaukriUploadService service;

    // Replace with your public Google Drive link
    private final String driveFileUrl = "https://drive.google.com/uc?export=download&id=1FAKXeQ5QwwKBtlSiIzOX5c5Wuz0owZ_B";

    public NaukriUploadScheduler(NaukriUploadService service) {
        this.service = service;
    }

    // 9:30 AM
    @Scheduled(cron = "0 30 9 * * *", zone = "Asia/Kolkata")
    public void uploadAt930() {
        System.out.println("Running at 9:30 AM");
        service.uploadFromDrive(driveFileUrl);
    }

    // 11:00 AM
    @Scheduled(cron = "0 0 11 * * *", zone = "Asia/Kolkata")
    public void uploadAt1100() {
        System.out.println("Running at 11:00 AM");
        service.uploadFromDrive(driveFileUrl);
    }

    // 2:30 PM
    @Scheduled(cron = "0 30 14 * * *", zone = "Asia/Kolkata")
//    @Scheduled(cron = "0 31 13 * * *", zone = "Asia/Kolkata")

    public void uploadAt230() {
        System.out.println("Running at 2:30 PM");
        service.uploadFromDrive(driveFileUrl);
    }
}
