$(document).ready(function() {
    let orders = [];  // 전송할 주문 목록을 담을 배열
    let totalAmount = 0;

    // localStorage에서 item 정보를 가져와 처리
    for (let i = 0; i < localStorage.length; i++) {
        const key = localStorage.key(i);

        // key가 특정 패턴(item_로 시작)인 경우에만 처리
        if (key.startsWith('item_')) {
            const itemData = JSON.parse(localStorage.getItem(key));

            if (itemData && itemData.imageUrl && itemData.iname) {
                // 화면에 항목 표시
                const row = `<tr>
                                <td>
                                    <div class="product-info">
                                        <img src="${itemData.imageUrl}" alt="제품 이미지" class="product-image">
                                        <div class="product-details">
                                            <strong>${itemData.iname}</strong>
                                        </div>
                                    </div>
                                </td>
                                <td>${itemData.iprice}원</td>
                            </tr>`;
                $('#orderItemsTable').append(row);

                // 개별 가격을 총 금액에 더함
                totalAmount += itemData.iprice;

            } else {
                console.error('유효하지 않은 localStorage 데이터:', itemData);
            }
        }
    }

    // 총 결제 금액을 화면에 표시
    $('.order-summary .total span:last-child').text(totalAmount + '원');

    // "결제하기" 버튼 클릭 시 폼의 데이터를 읽어와서 전송
    $('.order-btn').on('click', function(event) {
        event.preventDefault(); // 폼 기본 제출 기능 방지

        const selectedDate = document.getElementById('date').value; // 사용자가 선택한 수거 예정일
        const fullAddress = updateFullAddress(); // 사용자가 입력한 주소
        const username = document.getElementById('name').value; // 신청인 이름
        const phone = document.getElementById('phone').value; // 전화번호
        const address = document.getElementById('address').value; // 주소 가져오기
        const zipCode = document.getElementById('postcode').value; // 우편번호

        if (!selectedDate || !fullAddress) {
            alert("수거 예정일과 주소를 모두 입력해주세요.");
            return; // 필수 값이 없으면 서버로 보내지 않음
        }

        // orders 배열을 여기서 채웁니다 (사용자가 폼을 제출한 후에 데이터를 추가)
        for (let i = 0; i < localStorage.length; i++) {
            const key = localStorage.key(i);

            if (key.startsWith('item_')) {
                const itemData = JSON.parse(localStorage.getItem(key));
                orders.push({
                    iname: itemData.iname,
                    purl: itemData.imageUrl,
                    oaddress: fullAddress,
                    amount: parseInt(itemData.iprice),  // 개별 가격
                    odate: selectedDate  // 사용자가 입력한 날짜
                });
            }
        }

        console.log("전송할 데이터:", orders);

        // 결제 데이터 전송 및 Pay 객체 저장
        const payData = {
            amount: totalAmount,  // 결제 금액
            pmethod: pay_method,  // 결제 방식 [카드, 통장, 페이]
            pstatus: "진행 중",  // 결제 상태 [진행 중, 환불]
            pdate: LocalDateTime.now()       // 결제 날짜는 아직 null
        };

        // 주문 정보가 있으면 서버로 전송
        if (orders.length > 0) {
            $.ajax({
                url: '/echopickup/product/order',
                type: 'POST',
                data: JSON.stringify(orders),
                contentType: 'application/json',
                success: function(response) {
                    // 성공 시 처리 로직
                    requestInicisPayment(totalAmount, username, phone, address, zipCode);
                    console.log('결제가 완료되었습니다. 결제 정보 & 주문 정보 DB 저장');
                },
                error: function(error) {
                    console.error('주문 저장 실패:', error);
                    // 실패 시 처리 로직
                }
            });
        }
    });
});

// 주소 업데이트 함수
function updateFullAddress() {
    var postcode = document.getElementById('postcode').value;
    var address = document.getElementById('address').value;
    var detailAddress = document.getElementById('detailAddress').value;

    return '(' + postcode + ') ' + address + ' ' + detailAddress;
}

// 결제 함수
function requestInicisPayment(totalAmount, username, phone, address, zipCode) {
    // 포트원
    // 관린자 콘솔 , https://admin.portone.io/
    // 로그인 후, 연동관리 -> 연동정보 -> 고객사 식별코드 가져오기,
    var IMP = window.IMP;
    var merchant_uid = "O" + new Date().getTime(); // 고유한 주문번호 생성
    IMP.init('imp61045886'); // 가맹점 식별코드 입력
    IMP.request_pay({
        pg: "html5_inicis",           // 등록된 pg사 (적용된 pg사는 KG이니시스)
        pay_method: "card",           // 결제방식: card(신용카드), trans(실시간계좌이체), vbank(가상계좌), phone(소액결제)
        // 확인 테스트시, 해당 아이디를 매번 변경해서 확인 해보기.
        merchant_uid: merchant_uid,   // 주문번호
        name: "echopickup",           // 상품명
        amount: totalAmount,          // 금액
        buyer_name: username,         // 주문자
        buyer_tel: phone,             // 전화번호 (필수입력)
        buyer_addr: updateFullAddress(),    	  // 주소
        buyer_postcode: zipCode       // 우편번호
    }, function(rsp) {
        if (rsp.success) {
            // 결제 성공 시 콜백
            alert('결제가 완료되었습니다.');
            // 이니시스 콜백 처리로 서버에서 Order 저장됨
        } else {
            alert('결제에 실패했습니다. 에러 내용: ' + rsp.error_msg);
        }
    });
}