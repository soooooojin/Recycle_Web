$(document).ready(function() {
    // 모든 쿠키를 가져오기
    const cookies = document.cookie.split('; ');
    const items = [];

    // 쿠키를 객체 형태로 변환
    cookies.forEach(function(cookie) {
        const [name, value] = cookie.split('=');
        // 필요한 경우 쿠키 이름에 대한 필터링을 수행할 수 있음
        if (name.startsWith('item_')) {
            const item = JSON.parse(decodeURIComponent(value));
            items.push(item);
        }
    });

    // 읽어온 쿠키 데이터로 필요한 작업 수행
    items.forEach(function(item) {
        // 예: 화면에 표시
        const row = `<tr>
                      <td>
                        <div class="product-info">
                          <img src="https://via.placeholder.com/50" alt="제품 이미지" class="product-image">
                          <div class="product-details">
                            <strong>${item.iname}</strong>
                          </div>
                        </div>
                      </td>
                      <td>${item.iprice}</td>
                      <td>
                        <button class="delete-button">삭제</button>
                      </td>
                    </tr>`;
        $('#orderItemsTable').append(row);
    });

    console.log('로드된 쿠키:', items);
});