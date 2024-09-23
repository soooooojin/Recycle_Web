    package com.appliances.recyle.controller;

    import com.appliances.recyle.domain.Member;
    import com.appliances.recyle.domain.MemberRole;
    import com.appliances.recyle.domain.Question;
    import com.appliances.recyle.repository.MemberRepository;
    import com.appliances.recyle.service.QuestionService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.data.domain.Page;
    import org.springframework.data.domain.PageRequest;
    import org.springframework.data.domain.Pageable;
    import org.springframework.data.domain.Sort;
    import org.springframework.security.core.Authentication;
    import org.springframework.security.core.context.SecurityContextHolder;
    import org.springframework.security.core.userdetails.UserDetails;
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.Model;
    import org.springframework.web.bind.annotation.*;
    import org.springframework.web.servlet.mvc.support.RedirectAttributes;

    @Controller
    @RequestMapping("/echopickup")
    public class QuestionController {

        private final QuestionService questionService;
        private final MemberRepository memberRepository;

        @Autowired
        public QuestionController(QuestionService questionService, MemberRepository memberRepository) {
            this.questionService = questionService;
            this.memberRepository = memberRepository;
        }

        // 문의 작성 폼을 보여주는 메서드
        @GetMapping("/question")
        public String showQuestionForm() {
            return "echopickup/question";  // echopickup/question.html로 이동
        }

        // 문의 작성 폼 제출을 처리하는 메서드
        @PostMapping("/question")
        public String createQuestion(@RequestParam String qtitle,
                                     @RequestParam String qcomment,
                                     RedirectAttributes redirectAttributes) {

            // 로그인된 사용자의 이메일 가져오기
            final String email;
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                email = userDetails.getUsername();  // username이 이메일인 경우
            } else {
                redirectAttributes.addFlashAttribute("error", "로그인 정보가 필요합니다.");
                return "redirect:/echopickup/question";  // 로그인 페이지로 리다이렉트하는 것도 고려
            }

            Member member = this.memberRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid email: " + email));

            Question question = Question.builder()
                    .qtitle(qtitle)
                    .qcomment(qcomment)
                    .member(member)
                    .build();

            this.questionService.createQuestion(question);
            redirectAttributes.addFlashAttribute("message", "문의가 등록되었습니다.");
            return "redirect:/echopickup/question-list";  // 질문 목록 페이지로 리다이렉트
        }


        @GetMapping("/question-list")
        public String getQuestions(Model model,
                                   @RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "10") int size) {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "regDate"));
            Page<Question> questionPage = questionService.getQuestions(pageable);

            model.addAttribute("questionPage", questionPage);
            return "echopickup/question-list";
        }

        @GetMapping("/question-list/{qno}")
        public String getQuestionDetail(@PathVariable("qno") Long qno, Model model) {
            Question question = questionService.readQuestion(qno);
            model.addAttribute("question", question);

            // 로그인된 사용자의 이메일 가져오기
            final String email;
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                email = userDetails.getUsername();  // username이 이메일인 경우

                // 사용자 정보 조회
                Member member = memberRepository.findByEmail(email)
                        .orElseThrow(() -> new IllegalArgumentException("Invalid email: " + email));

                // Set<MemberRole>에서 ADMIN 역할이 있는지 확인
                boolean isAdmin = member.getRoleSet().contains(MemberRole.ADMIN); // ADMIN 역할이 있는지 확인
                model.addAttribute("isAdmin", isAdmin);  // 관리자 권한 여부를 모델에 추가
            } else {
                model.addAttribute("isAdmin", false);
            }

            return "echopickup/question-detail";
        }


        // 질문 삭제 메서드 (POST 요청 처리)
        @PostMapping("/question-delete/{qno}")
        public String deleteQuestion(@PathVariable Long qno, RedirectAttributes redirectAttributes) {
            questionService.deleteQuestion(qno);
            redirectAttributes.addFlashAttribute("message", "질문이 삭제되었습니다.");
            return "redirect:/echopickup/question-list";
        }

        // 질문 수정 폼을 보여주는 메서드
        @GetMapping("/question-update/{qno}")
        public String showUpdateForm(@PathVariable Long qno, Model model) {
            Question question = questionService.readQuestion(qno);
            model.addAttribute("question", question);
            return "echopickup/question-update";
        }

        // 질문 수정 요청을 처리하는 메서드
        @PostMapping("/question-update/{qno}")
        public String updateQuestion(@PathVariable Long qno,
                                     @RequestParam String qtitle,
                                     @RequestParam String qcomment,
                                     RedirectAttributes redirectAttributes) {
            Question question = questionService.readQuestion(qno);
            question.setQtitle(qtitle);
            question.setQcomment(qcomment);
            questionService.updateQuestion(qno, question);
            redirectAttributes.addFlashAttribute("message", "문의가 수정되었습니다.");
            return "redirect:/echopickup/question-list/" + qno;  // 수정 후 질문 상세 페이지로 리다이렉트
        }
    }