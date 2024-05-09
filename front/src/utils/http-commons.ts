import axios, { AxiosInstance } from "axios";

// import { tokenRefresh } from "../api/user";
// import { httpStatusCode } from "./http-status";

axios.defaults.withCredentials = true;

const baseURL: string = "https://k10d202.p.ssafy.io/api";

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
  },
});

export const downloadApi = (sseKey: string) => {
  const downApi = axios.create({
  baseURL: baseURL,
  responseType: 'blob',
  headers: {
    'Access-Control-Allow-Origin': '*',
    'Content-Type': 'application/json',
    'sseKey': sseKey 
  }})

  downApi.interceptors.request.use((config) => {
    const stored = localStorage.getItem('USER_STORE');
    if (stored) {
      const obj = JSON.parse(stored);
      if (obj.state.accessToken !== '') {
        config.headers['authorization'] = obj.state.accessToken;
        // config.headers['authorization'] = "eyJhbGciOiJIUzI1NiJ9.eyJjYXRlZ29yeSI6ImFjY2VzcyIsImlkIjoxLCJ1c2VybmFtZSI6IuyEseq3nCIsInByb3ZpZGVySWQiOiJrYWthbyAzNDU4Njg5NDM3IiwiaWF0IjoxNzE1MjM1ODk5LCJleHAiOjE3MTYwOTk4OTl9.mdm4F9ymRYeyAKJcds4sl1_j_g-5oRfSMkQZJBcNVHk"
      }
    }
    return config;
  });
  return downApi;
};

export const formDataApi: AxiosInstance = axios.create({
	baseURL: baseURL,
	headers: {
		"Access-Control-Allow-Origin": "*",
		"Content-Type": "multipart/form-data",
	},
});


privateApi.interceptors.request.use(
  (config) => {
		const stored = localStorage.getItem('USER_STORE');
		if (stored){
			const obj = JSON.parse(stored)
			if (obj.state.accessToken !== ''){
				config.headers['authorization'] = obj.state.accessToken;
				// config.headers['authorization'] = 'eyJhbGciOiJIUzI1NiJ9.eyJjYXRlZ29yeSI6ImFjY2VzcyIsImlkIjo1MiwidXNlcm5hbWUiOiLshLHqt5wiLCJwcm92aWRlcklkIjoia2FrYW8gMzQ1ODY4OTQzNyIsImlhdCI6MTcxNTEzMzkzNywiZXhwIjoxNzE1OTk3OTM3fQ.JZUCLuNRLK71yot5hBo13cfVkvKnEHDpZIebJUqX6dc';
			}
		}
    return config;
  },
  async () => {
    // const { config, response: { status }, } = error;
    // // 토큰 만료일 경우.
    // if (status === 401) {
    //   if (error.response.data.message === 'access token expired') {
    //     const originRequest = config;

    //     // 토큰 재발급.
    //     await tokenRefresh(
    //       (res) => {
    //         // 성공 시
    //         if (res.status === httpStatusCode.OK && res.headers.access) {
    //           localStorage.setItem('accessToken', res.headers.access);
    //           axios.defaults.headers.access = `${res.headers.access}`;
    //           originRequest.headers.access = `${res.headers.access}`;

    //           // 토큰 교환 후 재 시도.
    //           return axios(originRequest);
    //         }
    //       },
    //       () => {
            
    //       }
    //     )
    //   }
    // }
  }
);

formDataApi.interceptors.request.use(
  (config) => {
		const stored = localStorage.getItem('USER_STORE');
		if (stored){
			const obj = JSON.parse(stored)
			if (obj.state.accessToken !== ''){
				config.headers['authorization'] = obj.state.accessToken;
				// config.headers['authorization'] = 'eyJhbGciOiJIUzI1NiJ9.eyJjYXRlZ29yeSI6ImFjY2VzcyIsImlkIjo1MiwidXNlcm5hbWUiOiLshLHqt5wiLCJwcm92aWRlcklkIjoia2FrYW8gMzQ1ODY4OTQzNyIsImlhdCI6MTcxNTEzMzkzNywiZXhwIjoxNzE1OTk3OTM3fQ.JZUCLuNRLK71yot5hBo13cfVkvKnEHDpZIebJUqX6dc';
			}
		}
    return config;
  },
  async () => {
    // const { config, response: { status }, } = error;
    // // 토큰 만료일 경우.
    // if (status === 401) {
    //   if (error.response.data.message === 'access token expired') {
    //     const originRequest = config;

    //     // 토큰 재발급.
    //     await TokenRefresh(
    //       (res) => {
    //         // 성공 시
    //         if (res.status === httpStatusCode.OK && res.headers.access) {
    //           localStorage.setItem('accessToken', res.headers.access);
    //           axios.defaults.headers.access = `${res.headers.access}`;
    //           originRequest.headers.access = `${res.headers.access}`;

    //           // 토큰 교환 후 재 시도.
    //           return axios(originRequest);
    //         }
    //       },
    //       () => {
    //         localStorage.clear();
    //       }
    //     )
    //   }
    // }
  }
);
