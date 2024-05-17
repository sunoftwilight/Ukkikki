import { AxiosResponse } from 'axios';
import { privateApi } from '../utils/http-commons';
import { ResponseData } from '../types/ApiResponseType';
import { pageableType } from '../types/ChatType';

export const getMsg = async (
  partyId: number,
  pageable: pageableType,
  Response : (Response : AxiosResponse<ResponseData>) => void, 
  Error : (Error : AxiosResponse<ResponseData>) => void) => {
  await privateApi.get(`/chat-list/${partyId}`, { params: pageable })
  .then(Response)
  .catch(Error)
}