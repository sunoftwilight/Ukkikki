import axios, { AxiosInstance } from "axios";
import { tokenRefresh } from "../api/user";
import { httpStatusCode } from "./http-status";

axios.defaults.withCredentials = true;

const baseURL: string = "https://k10d202.p.ssafy.io/api";


// 새 토큰 저장
const newAccess = (header:string) => {

  const stored = localStorage.getItem('USER_STORE');
  if (stored) {
    const obj = JSON.parse(stored)
    obj.state.accessToken = header;
    localStorage.setItem('USER_STORE', JSON.stringify(obj));
  }
  
}


// S3 조회 API 
export const imgApi: AxiosInstance = axios.create({
  headers: {
    'Access-Control-Allow-Origin': '*',
    'Content-Type': 'application/json',
    'x-amz-server-side-encryption-customer-algorithm' : 'AES256'
  },
  responseType: 'blob'
});

// token이 필요 없는 api
export const publicApi: AxiosInstance = axios.create({
	baseURL: baseURL,
	headers: {
		'Content-Type': 'application/json',
	}
});

// token이 필요한 api
export const privateApi: AxiosInstance = axios.create({
  baseURL: baseURL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// S3 이미지 다운로드를 위한 api
export const downloadApi = (sseKey: string) => {
  const downApi = axios.create({
  baseURL: baseURL,
  responseType: 'blob',
  headers: {
    'Content-Type': 'application/json',
    'sseKey': sseKey 
  }})

  downApi.interceptors.request.use((config) => {
    const stored = localStorage.getItem('USER_STORE');
    if (stored) {
      const obj = JSON.parse(stored);
      if (obj.state.accessToken !== '') {
        config.headers['authorization'] = obj.state.accessToken;
      }
    }
    return config;
  });
  return downApi;
};

export const formDataApi: AxiosInstance = axios.create({
	baseURL: baseURL,
	headers: {
		"Content-Type": "multipart/form-data",
	},
});


// privateInterceptors
privateApi.interceptors.request.use(
  (config) => {
		const stored = localStorage.getItem('USER_STORE');
		if (stored){
			const obj = JSON.parse(stored)
			if (obj.state.accessToken !== ''){
				config.headers['authorization'] = obj.state.accessToken;
			}
		}
    return config;
  },
  (error) => {
    return error
  }
);

privateApi.interceptors.response.use(
  (response) => {
    return response;
  },
  (error) => {
    const { config, response: { status, data }, } = error;
    if(status === 401 && data === "access token expired") {
      const originRequest = config;
      tokenRefresh(
        (res) => {
          // 성공 시
          if (res.status === httpStatusCode.OK && res.headers.authorization) {
            
            newAccess(res.headers.authorization)
            axios.defaults.headers.authorization = `${res.headers.authorization}`;
            originRequest.headers.authorization = `${res.headers.authorization}`;

            // 토큰 교환 후 재 시도.
            return axios(originRequest);
          }
        },
        () => {
        }
      )
    }
  }
);

// formDataInterceptors
formDataApi.interceptors.request.use(
  (config) => {
		const stored = localStorage.getItem('USER_STORE');
		if (stored){
			const obj = JSON.parse(stored)
			if (obj.state.accessToken !== ''){
				config.headers['authorization'] = obj.state.accessToken;
			}
		}
    return config;
  },
  (error) => {
    return error
  }
);

formDataApi.interceptors.response.use(
  (response) => {
    return response;
  },
  (error) => {
    const { config, response: { status, data }, } = error;
    if(status === 401 && data === "access token expired") {
      const originRequest = config;
      tokenRefresh(
        (res) => {
          // 성공 시
          if (res.status === httpStatusCode.OK && res.headers.authorization) {
            
            newAccess(res.headers.authorization)
            axios.defaults.headers.authorization = `${res.headers.authorization}`;
            originRequest.headers.authorization = `${res.headers.authorization}`;

            // 토큰 교환 후 재 시도.
            return axios(originRequest);
          }
        },
        () => {
        }
      )
    }
  }
);
