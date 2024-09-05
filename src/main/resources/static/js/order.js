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