document.getElementById('changepwbutton').addEventListener('click', function(event) {
    event.preventDefault();  // 폼 제출 기본 동작 막기

    // 입력된 비밀번호 값 가져오기
    const currentPassword = document.getElementById('currentPassword').value;
    const newPassword = document.getElementById('newPassword').value;
    const confirmPassword = document.getElementById('confirmPassword').value;

    // 비밀번호 변경 요청 데이터
    const passwordData = {
        currentPassword: currentPassword,
        newPassword: newPassword,
        confirmPassword: confirmPassword
    };

    // 비밀번호 변경 API 호출
    fetch('/api/mypage/changepassword', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(passwordData),
    })
        .then(response => response.json())
        .then(data => {
            alert('비밀번호가 성공적으로 변경되었습니다!');
            console.log('비밀번호 변경 결과:', data);
        })
        .catch(error => {
            console.error('Error:', error);
            alert('비밀번호 변경 중 문제가 발생했습니다.');
        });
});