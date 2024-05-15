import { AxiosResponse } from 'axios';
import { publicApi, privateApi } from '../utils/http-commons';
import { ResponseData } from '../types/ApiResponseType';
import { UserResponeseData, GroupKeyResponeseData } from '../types/UserInfoType';

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
  
export const simpleInsert = async(
  params: any,
  Response : (Response : AxiosResponse<ResponseData>) => void,
  Error : (Error : AxiosResponse<ResponseData>) => void) => {
    await privateApi.post(`/${url}/password`, params)
    .then(Response)
    .catch(Error)
  }

export const simpleCheck = async(
  data: Record<string,string>,
  Response : (Response : AxiosResponse<GroupKeyResponeseData>) => void,
  Error : (Error : AxiosResponse<ResponseData>) => void) => {
    await privateApi.get(`/${url}/mykey`,{headers: {...data}})
    .then(Response)
    .catch(Error)
  }

  export const logOut = async (
    Response : (Response : AxiosResponse<GroupKeyResponeseData>) => void,
    Error : (Error : AxiosResponse<ResponseData>) => void) => {
      await privateApi.post(`/${url}/logout`)
      .then(Response)
      .catch(Error)
  }

  export const changeUploadGroup = async (
    partyId: number,
    Response : (Response: AxiosResponse<ResponseData>) => void,
    Error : (Error: AxiosResponse<ResponseData>) => void) => {
      await privateApi.patch(`/${url}/directory/${partyId}`)
      .then(Response)
      .catch(Error)
    }