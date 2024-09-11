
<!-- JavaScript: 사용자 정보를 REST API에서 가져와 HTML에 반영 -->
document.addEventListener("DOMContentLoaded", function () {
    // /api/mypage/user-info API 호출
    fetch("/api/mypage/user-info")
        .then(response => response.json())  // JSON 데이터를 파싱
        .then(data => {
            // 데이터를 HTML 입력 필드에 삽입
            document.getElementById("userName").value = data.mname;
            document.getElementById("email").value = data.email;
            document.getElementById("phone").value = data.phone;
            document.getElementById("address").value = data.address;
        })
        .catch(error => console.error("Error fetching user info:", error));
});