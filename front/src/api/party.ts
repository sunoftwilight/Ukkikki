import { AxiosResponse } from 'axios';
import { formDataApi, privateApi, imgApi } from '../utils/http-commons';
import { ResponseData } from '../types/ApiResponseType';
import { CreateDoenResponesData, PartyListResponesData } from '../types/Group';

const url = 'party'

export const createParty = async (
  params: FormData,
	Response: (Response: AxiosResponse<CreateDoenResponesData>) => void,
	Error: (Error: AxiosResponse<ResponseData>) => void) => {
		await formDataApi.post(`/${url}/create`, params)
		.then(Response)
		.catch(Error)
  }

export const getPartyList = async (
	Response: (Response: AxiosResponse<PartyListResponesData>) => void,
	Error: (Error: AxiosResponse<ResponseData>) => void) => {
		await privateApi.get(`/${url}/list`)
		.then(Response)
		.catch(Error)
	}

export const getPartyThumb = async (
	thumbUrl: string,
	headerOpt: Record<string,string>,
	Response: (Response: AxiosResponse<ResponseData>) => void,
	Error: (Error: AxiosResponse<ResponseData>) => void) => {
		await imgApi.get(`${thumbUrl}`,{headers: {...headerOpt}})
		.then(Response)
		.catch(Error)
	}