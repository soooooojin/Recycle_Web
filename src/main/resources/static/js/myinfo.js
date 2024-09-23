// JavaScript: 사용자 정보를 REST API에서 가져와 HTML에 반영
document.addEventListener("DOMContentLoaded", function () {
    // /api/mypage/user-info API 호출
    fetch("/api/mypage")
        .then(response => { // JSON 데이터를 파싱
            if (!response.ok) {
                if (response.status === 404) {
                    throw new Error("사용자 정보를 찾을 수 없습니다.");
                } else {
                    throw new Error("서버 에러가 발생했습니다.");
                }
            }
            return response.json();
        })
        .then(data => {
            // DB 속 fullAddress를 , 기준으로 나눔
            const fullAddress = data.address || '';

            if(fullAddress){
                const addressParts = fullAddress.split(')');

                // ) 뒤의 문자열 존재 확인
                let newaddress = '';
                let detailAddress = '';
                if (addressParts.length > 1) {
                    newaddress = addressParts[1].trim(); // 우편주소 제거한 주소

                    const detailParts = newaddress.split(',');
                    if (detailParts.length > 1) {
                        detailAddress = detailParts.slice(1).map(part => part.trim()).join(', ');
                        newaddress = detailParts[0].trim();  // ',' 앞의 값이 newAddress
                    }
                }
                document.getElementById("address").value = newaddress;  // 주소 부분
                document.getElementById("detailAddress").value = detailAddress;  // 상세주소 부분
            }
            else {
                document.getElementById("address").value = '';  // 주소 부분
                document.getElementById("detailAddress").value = '';  // 상세주소 부분
            }


            // 데이터를 HTML 입력 필드에 삽입
            document.getElementById("userName").value = data.mname;
            document.getElementById("email").value = data.email;
            document.getElementById("phone").value = data.phone;

        })
        .catch(error => console.error("Error fetching user info:", error));
});

// 수정 버튼 눌렸을때
document.getElementById("editButton").addEventListener("click", function(event) {
    event.preventDefault();  // 기본 폼 제출 방지

    var phoneNum = document.getElementById('phone').value;

    // 주소 정보를 합치기
    var fullAddress = updateFullAddress();

    // 콘솔에서 전송할 데이터 확인
    console.log("전송된 데이터:", { phone: phoneNum, address: fullAddress });

    // POST 요청으로 데이터 전송
    fetch("/echopickup/mypage", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            phone : phoneNum,
            address: fullAddress
        })
    })
        .then(response => {
            // 상태 코드가 200 ~ 299 범위에 있을 때만 성공으로 처리
            if (response.ok) {
                return response.text();
            } else {
                throw new Error("업데이트에 실패했습니다. 다시 시도해주세요");
            }
        })
        .then(data => {
            alert("변경되었습니다."+phoneNum+fullAddress);
            window.location.href = '/echopickup/mypage';
        })
        .catch(error => {
            console.error("Error updating address:", error);
            alert(error.message);  // 오류 메시지를 알림
        });
});





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
