package com.appliances.recyle.controller;

import com.appliances.recyle.domain.MemberRole;
import com.appliances.recyle.domain.Notice;
import com.appliances.recyle.repository.MemberRepository;
import com.appliances.recyle.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.appliances.recyle.domain.Member;

import java.util.List;

@Controller
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private MemberRepository memberRepository;

    // 공지사항 목록 보기 (HTML 페이지용)
    @GetMapping("/echopickup/notice")
    public String getNotices(Model model,
                             @RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "nno"));
        Page<Notice> noticePage = noticeService.getNotices(pageable);
        model.addAttribute("noticePage", noticePage);
        return "echopickup/notice"; // 공지사항 목록 HTML 페이지
    }

    // 공지사항 목록 보기 (JSON 데이터용 - 안드로이드 API)
    @GetMapping("/echopickup/api/notices")
    @ResponseBody
    public List<Notice> getNoticesApi(@RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "nno"));
        Page<Notice> noticePage = noticeService.getNotices(pageable);
        return noticePage.getContent(); // JSON 배열로 반환
    }


    // 공지사항 상세 보기 (HTML 페이지용)
    @GetMapping("/echopickup/notice/{nno}")
    public String getNoticeDetail(@PathVariable Long nno, Model model) {
        Notice notice = noticeService.readNotice(nno);
        model.addAttribute("notice", notice);
        return "echopickup/notice-detail";  // 상세 HTML 페이지로 이동
    }

    // 공지사항 상세 보기 (JSON 데이터용 - 안드로이드 API)
    @GetMapping("/echopickup/api/notices/{nno}")
    @ResponseBody
    public Notice getNoticeDetailApi(@PathVariable Long nno) {
        return noticeService.readNotice(nno);  // JSON 데이터로 공지사항 상세정보 반환
    }

    // 공지사항 작성 화면으로 이동
    @GetMapping("/echopickup/notice/write")
    public String showWriteForm(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final String email;  // final로 선언

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            email = userDetails.getUsername();  // username이 이메일인 경우
        } else {
            model.addAttribute("errorMessage", "로그인 정보가 필요합니다.");
            return "redirect:/echopickup/notice";
        }

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Invalid email: " + email));

        if (member.getRoleSet().contains(MemberRole.ADMIN)) { // ADMIN 역할 확인
            return "echopickup/notice-write";  // 공지사항 작성 페이지로 이동
        } else {
            model.addAttribute("errorMessage", "접근할 수 없습니다.");
            return "redirect:/echopickup/notice";
        }
    }

    @PostMapping("/echopickup/notice/write")
    public String writeNotice(@RequestParam String ntitle,
                              @RequestParam String ncomment,
                              RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final String email;  // final로 선언

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            email = userDetails.getUsername();  // username이 이메일인 경우
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "로그인 정보가 필요합니다.");
            return "redirect:/echopickup/notice";
        }

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Invalid email: " + email));

        if (member.getRoleSet().contains(MemberRole.ADMIN)) {
            Notice notice = Notice.builder()
                    .ntitle(ntitle)
                    .ncomment(ncomment)
                    .member(member)
                    .build();

            noticeService.createNotice(notice);
            redirectAttributes.addFlashAttribute("message", "공지사항이 등록되었습니다.");
            return "redirect:/echopickup/notice";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "공지사항 작성 권한이 없습니다.");
            return "redirect:/echopickup/notice";
        }
    }
}
