// 화면에서 바로 비번 확인 js
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

// 폼 제출 시 확인 이벤트 리스너
document.getElementById('registrationForm').addEventListener('submit', function(event) {
    var password = document.getElementById('pw').value;
    var confirmPassword = document.getElementById('confirm-pw').value;
    var errorMessage = document.getElementById('password-error');
    var fullAddress = updateFullAddress();

    if (password !== confirmPassword) {
        errorMessage.style.display = 'block'; // 오류 메시지 표시
        event.preventDefault(); // 폼 제출 방지
    }

    if (!fullAddress){
        alert("주소와 전화번호를 모두 입력해주세요.");
        return;
    }

    document.getElementById('fullAddressField').value = fullAddress;

});

// 아이디 중복 확인용.

// 주소 팝업에서 검색결과 항목을 클릭했을때
function DaumPostcode() {
    new daum.Postcode({
        oncomplete: function(data) {

            // 각 주소의 노출 규칙에 따라 주소를 조합한다.
            // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
            var addr = ''; // 주소 변수

            //사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
            if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
                addr = data.roadAddress;
            } else { // 사용자가 지번 주소를 선택했을 경우(J)
                addr = data.jibunAddress;
            }

            // 우편번호와 주소 정보를 해당 필드에 넣는다.
            document.getElementById('postcode').value = data.zonecode;
            document.getElementById("address").value = addr;
            // 커서를 상세주소 필드로 이동한다.
            document.getElementById("detailAddress").focus();
        }
    }).open();
}


// 주소 업데이트 함수(상세주소 합치는 함수)
function updateFullAddress() {
    var postcode = document.getElementById('postcode').value;
    var address = document.getElementById('address').value;
    var detailAddress = document.getElementById('detailAddress').value;

    return '(' + postcode + ') ' + address + ',' + detailAddress;
}

