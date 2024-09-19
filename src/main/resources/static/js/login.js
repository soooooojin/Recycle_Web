document.getElementById('login-form').addEventListener('submit', function(event) {
    event.preventDefault(); // Prevent the default form submission

    // Capture the input values
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;

    // Store the values in the data object
    const data = {
        username: username,
        password: password
    };

    console.log('Data:', data);

    // Optionally, you can now send this data using Axios
    axios.post('/generateToken', data)
        .then(function (res) {
            console.log('Response:', res.data);
            const accessToken = res.data.accessToken
            const refreshToken = res.data.refreshToken

            // 웹 브라우저의 로컬 스토리지 저장.
            // 확인, 크롬 -> 개발자도구 -> application -> local Storage 확인 가능.
            localStorage.setItem("accessToken", accessToken)
            localStorage.setItem("refreshToken", refreshToken)
            window.location.href = '/echopickup/index'; // Redirect on success
        })
        .catch(function (error) {
            console.error('Error:', error.response);
            window.location.href = '/echopickup/member/login';
        });
});