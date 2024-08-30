//product 스크립트입니다~!

$(document).ready(function() {

    // 페이지 로드 시 쿠키 초기화
    clearAllCookies();

    const popupBackground = $('#popupBackground');
    const popupMessage = $('#popupMessage');
    const popupActions = $('#popupActions');
    const confirmYes = $('#confirmYes');
    const confirmNo = $('#confirmNo');
    const selectionButtons = $('#selectionButtons');
    const fileInputContainer = $('#fileInputContainer');
    const classifiedItems = $('#classifiedItems');

    // 업로드 버튼 클릭 시
    $('#uploadButton').off('click').on('click', function() {  // off()로 기존 핸들러 제거 후 on()으로 바인딩
        const formData = new FormData($('#uploadForm')[0]);

        // 팝업 열기
        popupBackground.show();
        popupMessage.text("이미지 분류 중...");

        // 서버에 파일 업로드 및 이미지 분류 요청
        $.ajax({
            url: '/classify',
            type: 'POST',
            data: formData,
            processData: false,
            contentType: false,
            success: function(response) {
                // JSON 응답이 문자열 형태라면 파싱
                const parsedResponse = JSON.parse(response);

                // 분류 결과에서 predicted_class_label 추출
                const resultLabel = parsedResponse.predicted_class_label;
                alert('받은 응답: ' + response);

                // 분류 결과를 팝업창에 표시
                popupMessage.text(`분류 결과: ${resultLabel}`);
                confirmYes.show();
                confirmNo.show();

                // 자동으로 결과를 확인
                // 확인 버튼 클릭 시 중복 방지
                confirmYes.off('click').on('click', function () {
                    fetchAndDisplayItem(resultLabel);
                    resetPopup();
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
                                resetPopup();
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
    // function fetchAndDisplayItem(iname) {
    //     $.getJSON('/api/getAllItems', function(items) {
    //         console.log("서버에서 받은 응답:", items);
    //
    //         // 서버에서 받은 데이터 중에서 iname이 일치하는 항목 찾기
    //         const matchedItem = items.find(item => item.iname === iname);
    //
    //         if (matchedItem) {
    //             // 일치하는 항목이 있을 경우 화면에 표시
    //             const listItem = `<li>제품명: ${matchedItem.iname}, 가격: ${matchedItem.iprice}, 수량: 1</li>`;
    //             $('#classifiedItems').append(listItem);
    //             saveToCookie(matchedItem.iname, matchedItem.iprice, 1);
    //         } else {
    //             alert('DB에서 해당 항목을 찾을 수 없습니다.');
    //         }
    //     }).fail(function (jqXHR, textStatus, errorThrown) {
    //         console.error("데이터 가져오기 실패:", textStatus, errorThrown);
    //         alert('데이터를 불러오지 못했습니다.');
    //     });
    // }

    function fetchAndDisplayItem(iname) {
        $.getJSON('/api/getAllItems', function(items) {
            console.log("서버에서 받은 응답:", items);

            // 서버에서 받은 데이터 중에서 iname이 일치하는 항목 찾기
            const matchedItem = items.find(item => item.iname === iname);

            if (matchedItem) {
                // 일치하는 항목이 있을 경우 화면에 테이블 행으로 추가
                const row = `<tr>
                            <td>${matchedItem.iname}</td>
                            <td>${matchedItem.iprice}</td>                        
                        </tr>`;
                $('#classifiedItems').append(row);
                saveToCookie(matchedItem.iname, matchedItem.iprice);
            } else {
                alert('DB에서 해당 항목을 찾을 수 없습니다.');
            }
        }).fail(function (jqXHR, textStatus, errorThrown) {
            console.error("데이터 가져오기 실패:", textStatus, errorThrown);
            alert('데이터를 불러오지 못했습니다.');
        });
    }

    // 신청하기 버튼 클릭 시 실행되는 함수
    document.getElementById('applyButton').addEventListener('click', function() {
        const cookies = document.cookie.split('; ');
        let queryParams = '';

        // 모든 쿠키 데이터를 URL 파라미터로 변환
        cookies.forEach(function(cookie, index) {
            const [name, value] = cookie.split('=');
            queryParams += `${encodeURIComponent(name)}=${encodeURIComponent(value)}`;
            if (index < cookies.length - 1) {
                queryParams += '&';
            }
        });

        // 다음 페이지로 이동
        window.location.href = `/nextPage?${queryParams}`;
    });

    // 쿠키에 데이터 저장하는 함수
    function saveToCookie(name, price, quantity) {
        const item = { iname: name, iprice: price, quantity: quantity };
        const cookieIndex = new Date().getTime(); // 시간으로 고유 인덱스 생성
        document.cookie = `item_${name}_${cookieIndex}=${JSON.stringify(item)};path=/`;
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
        popupBackground.hide();
        confirmYes.hide();
        confirmNo.hide();
        selectionButtons.hide();
        popupMessage.text('');
    }
});
