$(document).ready(function() {
    // 페이지 로드 시 쿠키 초기화
    clearAllCookies();

    const popupBackground = $('#popupBackground');
    const popupContent = $('.popup-content');
    const popupMessage = $('#popupMessage');
    const confirmYes = $('#confirmYes');
    const confirmNo = $('#confirmNo');
    const selectionButtons = $('#selectionButtons');

    // 팝업 닫기 함수
    function closePopup() {
        popupBackground.hide();
        popupContent.hide();
        resetPopup();
    }

    // X 버튼 클릭 시 팝업 닫기
    $('#popupClose').on('click', function() {
        closePopup();
    });

    // 업로드 버튼 클릭 시
    $('#uploadButton').off('click').on('click', function() {
        const formData = new FormData($('#uploadForm')[0]);

        // 팝업 열기
        popupBackground.show();
        popupContent.show();
        popupMessage.text("이미지 분류 중...");

        // 서버에 파일 업로드 및 이미지 분류 요청
        $.ajax({
            url: '/classify',
            type: 'POST',
            data: formData,
            processData: false,
            contentType: false,
            success: function(response) {
                const parsedResponse = JSON.parse(response);
                const resultLabel = parsedResponse.predicted_class_label;

                // 분류 결과를 팝업창에 표시
                popupMessage.text(`분류 결과: ${resultLabel}`);
                confirmYes.show();
                confirmNo.show();

                // 확인 버튼 클릭 시 팝업 닫기 및 항목 추가
                confirmYes.off('click').on('click', function () {
                    fetchAndDisplayItem(resultLabel);
                    closePopup();
                });

                // 수동으로 다른 항목 선택
                confirmNo.off('click').on('click', function () {
                    confirmYes.hide();
                    confirmNo.hide();
                    selectionButtons.show();
                    selectionButtons.empty();

                    // DB에서 제품명 목록을 가져와 버튼 생성
                    $.getJSON('/api/getAllItems', function (items) {
                        items.forEach(function (item) {
                            const button = $(`<button>${item.iname}</button>`);
                            button.click(function () {
                                fetchAndDisplayItem(item.iname);
                                closePopup();
                            });
                            selectionButtons.append(button);
                        });
                    }).fail(function () {
                        alert('제품 목록을 불러오는 중 오류가 발생했습니다.');
                    });
                });
            },
            error: function () {
                popupMessage.text('이미지 분류 중 오류가 발생했습니다.');
            }
        });
    });

    // DB에서 데이터를 가져와 화면에 표시하는 함수
    function fetchAndDisplayItem(iname) {
        $.getJSON('/api/getAllItems', function(items) {
            const matchedItem = items.find(item => item.iname === iname);

            if (matchedItem) {
                const row = `<tr>
                            <td><img src="${matchedItem.purl}" alt="${matchedItem.iname}" width="100"></td>
                            <td>${matchedItem.iname}</td>
                            <td>${matchedItem.iprice}</td>
                        </tr>`;
                $('#classifiedItems').append(row);
                saveToCookie(matchedItem.purl, matchedItem.iname, matchedItem.iprice);
            } else {
                alert('DB에서 해당 항목을 찾을 수 없습니다.');
            }
        }).fail(function () {
            alert('데이터를 불러오지 못했습니다.');
        });
    }

    // 신청하기 버튼 클릭 시 실행되는 함수
    $('#applyButton').on('click', function() {
        const cookies = document.cookie.split('; ');
        const items = [];

        // 쿠키를 객체로 변환하여 배열에 저장
        cookies.forEach(function(cookie) {
            const [name, value] = cookie.split('=');
            if (name.startsWith('item_')) {
                const item = JSON.parse(decodeURIComponent(value));
                items.push(item);
            }
        });

        // 쿠키 설정 후 100ms 지연 후 페이지 이동
        setTimeout(function() {
            window.location.href = `/echopickup/product/order`;
        });
    });

    // 쿠키에 데이터 저장하는 함수
    function saveToCookie(url, name, price) {
        const item = { purl: url, iname: name, iprice: price };
        const cookieIndex = new Date().getTime();
        const expires = new Date();
        expires.setTime(expires.getTime() + (24 * 60 * 60 * 1000)); // 쿠키 만료 시간 설정

        document.cookie = `item_${name}_${cookieIndex}=${encodeURIComponent(JSON.stringify(item))};path=/;`;

        console.log('저장된 쿠키:', document.cookie); // 디버깅용 로그
    }

    // 쿠키 초기화 함수
    function clearAllCookies() {
        const cookies = document.cookie.split("; ");
        for (let cookie of cookies) {
            const eqPos = cookie.indexOf("=");
            const name = eqPos > -1 ? cookie.substr(0, eqPos) : cookie;
            document.cookie = `${name}=;expires=Thu, 01 Jan 1970 00:00:00 GMT;path=/`;
        }
    }

    // 팝업 초기화 함수
    function resetPopup() {
        popupMessage.text('');
        confirmYes.hide();
        confirmNo.hide();
        selectionButtons.hide();
    }
});