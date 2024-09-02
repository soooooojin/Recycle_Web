package com.appliances.recyle.service;

import com.appliances.recyle.domain.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NoticeService {
    Notice createNotice(Notice notice);

    Notice readNotice(Long nno);

    List<Notice> readAllNotices();

    Notice updateNotice(Long nno, Notice notice);

    void deleteNotice(Long nno);

    // 페이징을 위한 메서드 정의
    Page<Notice> getNotices(Pageable pageable);
}