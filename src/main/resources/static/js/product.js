$(document).ready(function() {

    // 페이지 로드 시 localStorage 초기화
    // localStorage.clear();
    clearItemKeysFromLocalStorage();
    // 페이지 로드 시 쿠키 초기화
    clearAllCookies();
    // 테이블에서 행 삭제


    const popupBackground = $('#popupBackground');
    const popupMessage = $('#popupMessage');
    const popupActions = $('#popupActions');
    const confirmYes = $('#confirmYes');
    const confirmNo = $('#confirmNo');
    const selectionButtons = $('#selectionButtons');
    const fileInputContainer = $('#fileInputContainer');
    const classifiedItems = $('#classifiedItems');

    let selectedImageUrl = null; // 선택된 이미지 URL을 저장할 변수

    // 이미지 파일 선택 시 파일을 미리 읽어와 미리보기 URL 생성
    $('#imageInput').change(function(event) {
        const file = event.target.files[0];
        if (file) {
            const reader = new FileReader();
            reader.onload = function(e) {
                selectedImageUrl = e.target.result; // 이미지 URL 저장
                console.log("Selected image URL: ", selectedImageUrl); // 이미지 URL 콘솔에 출력
            };
            reader.readAsDataURL(file);
        }
    });

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
                // 서버로부터 받은 JSON 응답 (response가 이미 JSON 객체로 파싱되어 있음)
                console.log('Raw response:', response);
                console.log('Response Type:', typeof response);

                // JSON 응답이 문자열 형태라면 파싱
                // const parsedResponse = JSON.parse(response);

                // 분류 결과에서 predicted_class_label 추출
                const resultLabel = response.predicted_class_label;
                console.log('Result Label Type:', typeof resultLabel);
                alert('받은 응답: ' + response.confidence + "분류결과" + response.predicted_class_label);

                // MongoDB에 이미지 저장 요청
                $.ajax({
                    url: '/uploadImage',
                    type: 'POST',
                    data: formData,
                    processData: false,
                    contentType: false,
                    success: function(imageId) {
                        console.log('이미지 업로드 성공:', imageId);
                        alert('이미지 업로드 성공: ' + imageId);  // 이미지 ID를 표시
                        const imageUrl = `/image/${imageId}`; // 이미지 URL 생성

                // 분류 결과를 팝업창에 표시
                popupMessage.text(`분류 결과: ${resultLabel}`);
                confirmYes.show();
                confirmNo.show();

                // 예 버튼 클릭 시
                confirmYes.off('click').on('click', function () {
                    fetchAndDisplayItem(resultLabel, imageUrl);
                    resetPopup();
                });

                // 아니요 버튼 클릭 시
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
                                fetchAndDisplayItem(item.iname, imageUrl);
                                resetPopup();
                            });
                            selectionButtons.append(button);
                        });
                    }).fail(function () {
                        alert('제품 목록을 불러오는 중 오류가 발생했습니다.');
                    });
                });
                        // 쿠키에 이미지 URL 저장

                    },
                    error: function() {
                        alert('이미지 업로드 실패');
                    }
                });
            },
            error: function () {
                popupMessage.text('이미지 분류 중 오류가 발생했습니다.');
            }
        });
    });

    // DB에서 데이터를 가져와 화면에 표시하는 함수
    function fetchAndDisplayItem(iname, imageUrl) {
        const token = localStorage.getItem('accessToken');
        if (!token) {
            alert('로그인이 필요합니다.');
            return;
        }

        $.ajax({
            url: '/api/getAllItems',
            type: 'GET',
            headers: {
                'Authorization': 'Bearer ' + token
            },
            success: function(items) {
                const matchedItem = items.find(item => item.iname === iname);
                if (matchedItem) {
                    const row =
                        `<tr>
                            <td class="product-image-container">
                                <img src="${imageUrl}" alt="제품 이미지" class="product-image" style="width: 100px; height: 100px;">
                            </td>
                            <td class="product-info">${matchedItem.iname}</td>
                            <td class="product-info">${matchedItem.iprice}</td> 
                            <td><button class="deleteButton" data-id="${imageUrl}">삭제</button></td>
                         </tr>`;
                    $('#classifiedItems').append(row);
                    const lastRow = $('#classifiedItems tr:last');
                    lastRow.find('.deleteButton').click(function() {
                        const itemName = matchedItem.iname;
                        lastRow.remove();
                        deleteCookie(itemName);
                        deleteItemFromServer(imageUrl);
                    });

                    saveToLocalStorage(imageUrl, matchedItem.iname, matchedItem.iprice);
                } else {
                    alert('DB에서 해당 항목을 찾을 수 없습니다.');
                }
            },
            error: function() {
                alert('제품 목록을 불러오는 중 오류가 발생했습니다.');
            }
        });
    }

    // function deleteItemFromServer(imageUrl) {
    //     const imageId = imageUrl.split('/').pop(); // URL에서 이미지 ID 추출
    //     $.ajax({
    //         url: '/image/' + imageId,
    //         type: 'DELETE',
    //         success: function() {
    //             // 서버에서 성공적으로 삭제된 경우
    //             alert('삭제 성공');
    //         },
    //         error: function() {
    //             alert('삭제 실패');
    //         }
    //     });
    // }
    function deleteItemFromServer(imageUrl) {
        const imageId = imageUrl.split('/').pop(); // URL에서 이미지 ID 추출
        $.ajax({
            url: '/image/' + imageId,
            type: 'DELETE',
            success: function() {
                // 서버에서 성공적으로 삭제된 경우
                // localStorage.removeItem(imageId);
                deleteItemFromLocalStorage(imageUrl);
                alert('삭제 성공');
            },
            error: function() {
                alert('삭제 실패');
            }
        });
    }

    function deleteItemFromLocalStorage(imageUrl) {
        // localStorage의 모든 키를 순회
        for (let i = 0; i < localStorage.length; i++) {
            const key = localStorage.key(i);

            // 'item_'로 시작하는 키만 검사
            if (key.startsWith('item_')) {
                const storedItem = JSON.parse(localStorage.getItem(key));

                // 저장된 값의 imageUrl과 삭제하려는 imageUrl이 일치하는지 확인
                if (storedItem.imageUrl === imageUrl) {
                    // 일치하는 항목을 로컬 스토리지에서 삭제
                    localStorage.removeItem(key);
                    console.log(`삭제된 항목: ${key}`);
                    return;  // 항목을 삭제했으면 함수를 종료
                }
            }
        }
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
        window.location.href = `/echopickup/product/order`;
    });

    // 쿠키에 데이터 저장하는 함수
    // function saveToCookie(imageUrl, name, price) {
    //     const item = {imageUrl, iname: name, iprice: price };
    //     const cookieIndex = new Date().getTime(); // 시간으로 고유 인덱스 생성
    //     document.cookie = `item_${name}_${cookieIndex}=${encodeURIComponent(JSON.stringify(item))};path=/`;
    // }

    // 데이터를 localStorage에 저장
    function saveToLocalStorage(imageUrl, name, price) {
        const item = { imageUrl, iname: name, iprice: price };
        const key = `item_${name}_${new Date().getTime()}`;  // 고유한 키 생성
        localStorage.setItem(key, JSON.stringify(item));
    }

    function clearItemKeysFromLocalStorage() {
        // localStorage의 모든 키를 순회
        for (let i = 0; i < localStorage.length; i++) {
            const key = localStorage.key(i);

            // 'item_'로 시작하는 키 찾기
            if (key.startsWith('item_')) {
                localStorage.removeItem(key);  // 해당 키 삭제
                i--; // 키가 삭제되면 인덱스가 줄어드므로 i를 감소시켜야 순회가 정상 작동
            }
        }
    }

    // 쿠키에서 항목 삭제하는 함수
    function deleteCookie(name) {
        const cookies = document.cookie.split('; ');
        cookies.forEach(function(cookie) {
            if (cookie.includes(name)) {
                const eqPos = cookie.indexOf("=");
                const cookieName = eqPos > -1 ? cookie.substr(0, eqPos) : cookie;
                document.cookie = `${cookieName}=;expires=Thu, 01 Jan 1970 00:00:00 GMT;path=/`;
            }
        });
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



});
