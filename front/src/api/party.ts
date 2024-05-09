import { AxiosResponse } from 'axios';
import { formDataApi, privateApi } from '../utils/http-commons';
import { ResponseData } from '../types/ApiResponseType';

const url = 'party'

export const createParty = async (
  params: FormData,
	Response: (Response: AxiosResponse<ResponseData>) => void,
	Error: (Error: AxiosResponse<ResponseData>) => void) => {
		await formDataApi.post(`/${url}/create`, params)
		.then(Response)
		.catch(Error)
  }

export const getPartyList = async (
	Response: (Response: AxiosResponse<ResponseData>) => void,
	Error: (Error: AxiosResponse<ResponseData>) => void) => {
		await privateApi.get(`/${url}/getList`)
		.then(Response)
		.catch(Error)
	}

