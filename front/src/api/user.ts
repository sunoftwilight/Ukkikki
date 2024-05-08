import { AxiosResponse } from 'axios';
import { noHeaderApi, publicApi } from '../utils/http-commons';
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
    await noHeaderApi.post(`/${url}/reissue`)
    .then(Response)
    .catch(Error)
  }
  
  