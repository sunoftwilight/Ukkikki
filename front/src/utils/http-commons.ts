import axios, { AxiosInstance } from "axios";
import { userStore } from "../stores/UserStore";
import { useStore } from "zustand";
import { tokenRefresh } from "../api/user";
import { httpStatusCode } from "./http-status";

axios.defaults.withCredentials = true;

const baseURL: string = "https://k10d202.p.ssafy.io/api";
const accessToken = userStore.getState().accessToken;
const user = useStore(userStore)
export const publicApi: AxiosInstance = axios.create({
	baseURL: baseURL,
	headers: {
		'Access-Control-Allow-Origin': '*',
		'Content-Type': 'application/json',
	}
});

export const privateApi: AxiosInstance = axios.create({
  baseURL: baseURL,
  headers: {
    'Access-Control-Allow-Origin': '*',
    'Content-Type': 'application/json',
    'access': `${accessToken}`,
  },
});

export const downloadApi: AxiosInstance = axios.create({
  baseURL: baseURL,
	responseType: 'blob',
  headers: {
    'Access-Control-Allow-Origin': '*',
    'Content-Type': 'application/json',
    'access': `${accessToken}`,
  },
});

export const formDataApi: AxiosInstance = axios.create({
	baseURL: baseURL,
	headers: {
		"Access-Control-Allow-Origin": "*",
		"Content-Type": "multipart/form-data",
    'access': `${accessToken}`,
	},
});



privateApi.interceptors.request.use(
  (config) => {
    const token = user.accessToken
    if (token !== '') {
      // localStorage에 access 토큰이 있으면 요청 헤더에 추가
      config.headers['access'] = token;
    }
    return config;
  },
  async (error) => {
    const { config, response: { status }, } = error;
    // 토큰 만료일 경우.
    if (status === 401) {
      if (error.response.data.message === 'access token expired') {
        const originRequest = config;

        // 토큰 재발급.
        await tokenRefresh(
          (res) => {
            // 성공 시
            if (res.status === httpStatusCode.OK && res.headers.access) {
              localStorage.setItem('accessToken', res.headers.access);
              axios.defaults.headers.access = `${res.headers.access}`;
              originRequest.headers.access = `${res.headers.access}`;

              // 토큰 교환 후 재 시도.
              return axios(originRequest);
            }
          },
          () => {
            
          }
        )
      }
    }
  }
);


// export const formDataApi: AxiosInstance = axios.create({
//   baseURL: baseURL,
//   headers: {
//     'Content-Type': 'multipart/form-data',
//     'access': `${localStorage.getItem('accessToken')}`,
//   },
// });

// formDataApi.interceptors.request.use(
//   (config) => {
//     const token = localStorage.getItem('accessToken');
//     if (token) {
//       config.headers['access'] = `${token}`;
//     }
//     return config;
//   },
//   async (error) => {
//     const { config, response: { status }, } = error;
//     // 토큰 만료일 경우.
//     if (status === 401) {
//       if (error.response.data.message === 'access token expired') {
//         const originRequest = config;

//         // 토큰 재발급.
//         await TokenRefresh(
//           (res) => {
//             // 성공 시
//             if (res.status === httpStatusCode.OK && res.headers.access) {
//               localStorage.setItem('accessToken', res.headers.access);
//               axios.defaults.headers.access = `${res.headers.access}`;
//               originRequest.headers.access = `${res.headers.access}`;

//               // 토큰 교환 후 재 시도.
//               return axios(originRequest);
//             }
//           },
//           () => {
//             localStorage.clear();
//           }
//         )
//       }
//     }
//   }
// );
