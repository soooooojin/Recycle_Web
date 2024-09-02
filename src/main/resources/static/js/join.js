// 비번 확인 js
document.getElementById('confirm-pw').addEventListener('input', function(event) {
    var password = document.getElementById('pw').value;
    var confirmPassword = document.getElementById('confirm-pw').value;
    var errorMessage = document.getElementById('password-error');

    if (password !== confirmPassword) {
        errorMessage.style.display = 'block'; // 오류 메시지 표시
    } else {
        errorMessage.style.display = 'none'; // 오류 메시지 숨기기
    }
});

// 폼 제출 시 비밀번호 확인 이벤트 리스너
document.getElementById('registrationForm').addEventListener('submit', function(event) {
    var password = document.getElementById('pw').value;
    var confirmPassword = document.getElementById('confirm-pw').value;
    var errorMessage = document.getElementById('password-error');

    if (password !== confirmPassword) {
        errorMessage.style.display = 'block'; // 오류 메시지 표시
        event.preventDefault(); // 폼 제출 방지
    }
});

// 아이디 중복 확인용.
