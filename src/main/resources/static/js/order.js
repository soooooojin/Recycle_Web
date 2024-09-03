$(document).ready(function() {
    // 쿠키에서 데이터를 가져와 적절하게 처리
    const cookies = document.cookie.split('; ');  // 쿠키를 개별 항목으로 분리
    let totalAmount = 0;
    console.log('로드된 쿠키:', cookies);

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
                                  <td>${imageData.iprice}</td>
                                </tr>`;
                    $('#orderItemsTable').append(row);

                    // 가격을 합산
                    totalAmount += imageData.iprice;
                } else {
                    console.error('Invalid cookie data:', imageData);
                }
            }
        } catch (error) {
            console.error('Error parsing cookie:', error);
            alert('쿠키 데이터를 파싱하는 중 오류가 발생했습니다.');
        }
    });
    // 총 결제 금액을 화면에 표시
    $('.order-summary .total span:last-child').text(totalAmount + '원');
});