document.addEventListener('DOMContentLoaded', function () {
    // Axios 인터셉터 설정
    axios.interceptors.request.use(function (config) {
        const token = localStorage.getItem('accessToken');
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    }, function (error) {
        return Promise.reject(error);
    });

    // 만료된 토큰을 처리하는 interceptor
    axios.interceptors.response.use(function (response) {
        return response;
    }, async function (error) {
        if (error.response && error.response.status === 401) {
            const refreshToken = localStorage.getItem('refreshToken');
            if (refreshToken) {
                const response = await axios.post('/refreshToken', { refreshToken });
                if (response.status === 200) {
                    const newAccessToken = response.data.accessToken;
                    localStorage.setItem('accessToken', newAccessToken);
                    error.config.headers.Authorization = `Bearer ${newAccessToken}`;
                    return axios(error.config);
                }
            } else {
                window.location.href = '/echopickup/member/login';
            }
        }
        return Promise.reject(error);
    });
});