package com.appliances.recyle.service;

import com.appliances.recyle.domain.Notice;
import com.appliances.recyle.repository.NoticeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class NoticeServiceImpl implements NoticeService {

    private final NoticeRepository noticeRepository;

    @Autowired
    public NoticeServiceImpl(NoticeRepository noticeRepository) {
        this.noticeRepository = noticeRepository;
    }

    @Override
    public Notice createNotice(Notice notice) {
        return noticeRepository.save(notice);
    }

    @Override
    public Notice readNotice(Long nno) {
        return noticeRepository.findById(nno).orElse(null);
    }

    @Override
    public List<Notice> readAllNotices() {
        return noticeRepository.findAll();
    }

    @Override
    public Notice updateNotice(Long nno, Notice notice) {
        if (noticeRepository.existsById(nno)) {
            notice.setNno(nno);
            return noticeRepository.save(notice);
        }
        return null;
    }

    @Override
    public void deleteNotice(Long nno) {
        noticeRepository.deleteById(nno);
    }

    @Override
    public Page<Notice> getNotices(Pageable pageable) {
        return noticeRepository.findAll(pageable);
    }
}