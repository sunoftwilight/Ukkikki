import axios, { AxiosInstance } from "axios";

const httpsStatusCode: Record<string, Number> = {
    OK : 200,
    fail : 204,
	delete : 205,
	BADREQUEST : 400,
	UNAUTHORIZEZD : 401,
	FOBIDDEN : 403,
	NOTFOUND: 404,
	CONFLICT: 409
}

const baseURL: string = 'https://k10d202.p.ssafy.io/api';

axios.defaults.withCredentials = true;

export const noHeaderApi: AxiosInstance = axios.create({
    baseURL: baseURL
});

export const publicApi: AxiosInstance = axios .create({
    baseURL: baseURL,
    headers: {
        'Content-Type': 'appclication/json'
    }
});

export const privateApi: AxiosInstance = axios.create({
    baseURL: baseURL,
    headers: {
        'Content-Type': 'application/json',
        // 'access': `${localStorage.getItem('accessToken')}`,
    }
})  

export const formDataApi: AxiosInstance = axios.create({
    baseURL: baseURL,
    headers: {
      'Content-Type': 'multipart/form-data',
      //'access': `${localStorage.getItem('accessToken')}`,
    },
  });
  
  