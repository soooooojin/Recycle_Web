$(document).ready(function() {
    const cookies = document.cookie.split('; ');  // 쿠키를 개별 항목으로 분리
    let orders = [];  // 전송할 주문 목록을 담을 배열
    let totalAmount = 0;

    console.log('로드된 쿠키:', cookies);

    // 쿠키에서 item 정보를 찾아서 처리
    cookies.forEach(function(cookie) {
        try {
            const cookieParts = cookie.split('=');
            const cookieName = cookieParts[0];
            const cookieValue = cookieParts[1];

            // 쿠키 이름이 특정 패턴(item_로 시작)인 경우에만 처리
            if (cookieName.startsWith('item_')) {
                const imageData = JSON.parse(decodeURIComponent(cookieValue));

                if (imageData && imageData.imageUrl && imageData.iname) {
                    // 쿠키 데이터를 사용하여 화면에 항목을 표시
                    const row = `<tr>
                                    <td>
                                        <div class="product-info">
                                            <img src="${imageData.imageUrl}" alt="제품 이미지" class="product-image">
                                            <div class="product-details">
                                                <strong>${imageData.iname}</strong>
                                            </div>
                                        </div>
                                    </td>
                                    <td>${imageData.iprice}원</td>
                                </tr>`;
                    $('#orderItemsTable').append(row);

                    // 개별 가격을 총 금액에 더함
                    totalAmount += imageData.iprice;

                } else {
                    console.error('유효하지 않은 쿠키 데이터:', imageData);
                }
            }
        } catch (error) {
            console.error('쿠키 데이터 파싱 중 오류 발생:', error);
            alert('쿠키 데이터를 파싱하는 중 오류가 발생했습니다.');
        }
    });

    // 총 결제 금액을 화면에 표시
    $('.order-summary .total span:last-child').text(totalAmount + '원');

    // "결제하기" 버튼 클릭 시 폼의 데이터를 읽어와서 전송
    $('.order-btn').on('click', function(event) {
        event.preventDefault(); // 폼 기본 제출 기능 방지

        const selectedDate = document.getElementById('date').value; // 사용자가 선택한 수거 예정일
        const fullAddress = updateFullAddress(); // 사용자가 입력한 주소

        if (!selectedDate || !fullAddress) {
            alert("수거 예정일과 주소를 모두 입력해주세요.");
            return; // 필수 값이 없으면 서버로 보내지 않음
        }

        // orders 배열을 여기서 채웁니다 (사용자가 폼을 제출한 후에 데이터를 추가)
        cookies.forEach(function(cookie) {
            const cookieParts = cookie.split('=');
            const cookieName = cookieParts[0];
            const cookieValue = cookieParts[1];

            if (cookieName.startsWith('item_')) {
                const imageData = JSON.parse(decodeURIComponent(cookieValue));
                orders.push({
                    iname: imageData.iname,
                    purl: imageData.imageUrl,
                    oaddress: fullAddress,
                    amount: parseInt(totalAmount),  // 개별 가격
                    odate: selectedDate  // 사용자가 입력한 날짜
                });
            }
        });

        console.log("전송할 데이터:", orders);
        console.log("넌 뭔데:",JSON.stringify(orders));

        // 주문 정보가 있으면 서버로 전송
        if (orders.length > 0) {
            $.ajax({
                url: '/echopickup/product/order',
                type: 'POST',
                data: JSON.stringify(orders),
                contentType: 'application/json',
                success: function(response) {
                    console.log('주문이 성공적으로 저장되었습니다.');
                    // 성공 시 처리 로직
                    window.location.href = '/echopickup/product/pay'; // 성공 시 결제 페이지로 이동
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