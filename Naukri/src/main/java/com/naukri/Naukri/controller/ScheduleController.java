package com.naukri.Naukri.controller;



import org.springframework.web.bind.annotation.*;
import com.naukri.Naukri.dto.ScheduleRequest;
import com.naukri.Naukri.service.DynamicSchedulerService;

@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    private final DynamicSchedulerService scheduler;
    
    // Use the same drive URL as in NaukriUploadScheduler
    private final String driveFileUrl = "https://drive.google.com/uc?export=download&id=1FAKXeQ5QwwKBtlSiIzOX5c5Wuz0owZ_B";

    public ScheduleController(DynamicSchedulerService scheduler) {
        this.scheduler = scheduler;
    }

    @PostMapping("/upload")
    public String scheduleUpload(@RequestBody ScheduleRequest request) {
        scheduler.schedule(request.getDateTime(), driveFileUrl);
        return "Resume upload scheduled at: " + request.getDateTime();
    }
}