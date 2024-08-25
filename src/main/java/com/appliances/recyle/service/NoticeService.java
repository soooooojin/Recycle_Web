package com.appliances.recyle.service;

import com.appliances.recyle.domain.Notice;
import java.util.List;

public interface NoticeService {
    Notice createNotice(Notice notice);

    Notice readNotice(Long nno);

    List<Notice> readAllNotices();

    Notice updateNotice(Long nno, Notice notice);

    void deleteNotice(Long nno);
}