package com.appliances.recyle.controller;

import com.appliances.recyle.domain.Notice;
import com.appliances.recyle.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

//    @GetMapping("/echopickup/notice")
//    public String getNotices(Model model,
//                             @RequestParam(defaultValue = "0") int page,
//                             @RequestParam(defaultValue = "10") int size) {
//        Pageable pageable = PageRequest.of(page, size);
//        Page<Notice> noticePage = noticeService.getNotices(pageable);
//        model.addAttribute("noticePage", noticePage);
//        return "echopickup/notice"; // 여기에서 경로를 지정합니다.
//    }
}