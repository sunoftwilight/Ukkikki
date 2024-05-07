import axios, { AxiosInstance } from "axios";

// const httpsStatusCode: Record<string, Number> = {
	// OK : 200,
  // CREATE : 201,
  // DELETE : 204,
	// BADREQUEST : 400,
	// UNAUTHORIZEZD : 401,
	// FOBIDDEN : 403,
	// NOTFOUND : 404,
	// METHODERROR : 405,
	// CONFLICT : 409,
  // SERVER : 500
// }

// axios.defaults.withCredentials = true;

const baseURL: string = "https://k10d202.p.ssafy.io/api";

export const noHeaderApi: AxiosInstance = axios.create({
	baseURL: baseURL,
});

export const commonApi: AxiosInstance = axios.create({
	baseURL: baseURL,
	headers: {
		"Content-Type": "appclication/json",
	},
});

export const formDataApi: AxiosInstance = axios.create({
	baseURL: baseURL,
	headers: {
		"Access-Control-Allow-Origin": "*",
		"Content-Type": "multipart/form-data",
	},
});
