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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    @GetMapping("/echopickup/notice")
    public String getNotices(Model model,
                             @RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Notice> noticePage = noticeService.getNotices(pageable);
        model.addAttribute("noticePage", noticePage);
        return "echopickup/notice"; // 공지사항 목록 페이지
    }

    // 공지사항 상세보기 매핑 추가
    @GetMapping("/echopickup/notice/{nno}")
    public String getNoticeDetail(@PathVariable Long nno, Model model) {
        Notice notice = noticeService.readNotice(nno);
        model.addAttribute("notice", notice);
        return "echopickup/notice-detail";  // 상세 페이지로 이동
    }
}