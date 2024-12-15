package com.project.btoproject.controller.UIcontroller;

import com.project.btoproject.model.Guide;
import com.project.btoproject.model.PointRecord;
import com.project.btoproject.model.User;
import com.project.btoproject.service.IAllUsersService;
import com.project.btoproject.service.IGuideService;
import com.project.btoproject.service.IPointRecordService;
import com.project.btoproject.service.IUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class UIPointRecordController {
    private final IGuideService guideService;
    private final IPointRecordService pointRecordService;

    public UIPointRecordController(IGuideService guideService, IPointRecordService pointRecordService) {
        this.guideService = guideService;
        this.pointRecordService = pointRecordService;
    }

    @GetMapping("/getAllRecords")
    public String getAllPointRecordsPage(Model model) {
        List<PointRecord> records = pointRecordService.findAllRecords().stream().toList();
        model.addAttribute("all_records", records);
        return "all-point-record-list";
    }

    @GetMapping("/getRecordsOfGuide/{id}")
    public String getPointRecordPage(Model model, @PathVariable Long id) {
        Guide guide = guideService.getGuideById(id);
        List< PointRecord> pointRecords = pointRecordService.getPointRecordsByGuide(guide);
        model.addAttribute("guide_records", pointRecords);
        return "point-record-list";
    }
}
