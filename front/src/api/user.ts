import { AxiosResponse } from 'axios';
import { publicApi } from '../utils/http-commons';
import { ResponseData } from '../types/ApiResponseType';


const url = 'member';

export const UserInfo = async (
	Response: (Response: AxiosResponse<ResponseData>) => void,
	Error: (Error: AxiosResponse<ResponseData>) => void) => {
		await publicApi.get(`/${url}/info/my`)
		.then(Response)
		.catch(Error)
  }

  export const TokenRefresh = async(
    Response : (Response : AxiosResponse<ResponseData>) => void, 
    Error : (Error : AxiosResponse<ResponseData>) => void) => {
    await publicApi.post(`/${url}/reissue`)
    .then(Response)
    .catch(Error)
  }
  
  