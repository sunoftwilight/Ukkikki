import { AxiosResponse } from 'axios';
import { privateApi } from '../utils/http-commons';
import { ResponseData } from '../types/ApiResponseType';

export const sendMsg = async (
  partyId: string,
  Response : (Response : AxiosResponse<AlbumResponse>) => void, 
  Error : (Error : AxiosResponse<ResponseData>) => void) => {
  await privateApi.get(`/${url}/${dirId}`)
  .then(Response)
  .catch(Error)
}