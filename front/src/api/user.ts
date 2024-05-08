import { AxiosResponse } from 'axios';
import { publicApi, privateApi } from '../utils/http-commons';
import { ResponseData } from '../types/ApiResponseType';
import { UserResponeseData } from '../types/UserInfoType';


const url = 'member';

export const userInfo = async (
	Response: (Response: AxiosResponse<UserResponeseData>) => void,
	Error: (Error: AxiosResponse<UserResponeseData>) => void) => {
		await privateApi.get(`/${url}/info/my`)
		.then(Response)
		.catch(Error)
  }

  export const tokenRefresh = async(
    Response : (Response : AxiosResponse<ResponseData>) => void, 
    Error : (Error : AxiosResponse<ResponseData>) => void) => {
    await publicApi.post(`/${url}/reissue`)
    .then(Response)
    .catch(Error)
  }
  
  