package com.appliances.recyle.controller;

import com.appliances.recyle.service.NoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Log4j2
@Controller
@RequestMapping("/echopickup")
@RequiredArgsConstructor
public class RecycleController {

    private final NoticeService noticeService;

    @GetMapping("/index")
    public void index() {

    }

//    @GetMapping("/notice")
//    public void notice() {
//
//    }

//    @GetMapping("/notice")
//    public ModelAndView showNotices() {
//        List<Notice> notices = noticeService.readAllNotices();
//        ModelAndView mav = new ModelAndView("notice"); // 수정된 경로
//        mav.addObject("noticeList", notices);
//        return mav;/
//    }

    // RESTful API 메서드들 (필요에 따라 추가)

}
