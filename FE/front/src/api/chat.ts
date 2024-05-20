import { AxiosResponse } from 'axios';
import { privateApi } from '../utils/http-commons';
import { ResponseData } from '../types/ApiResponseType';
import { getMsgResponseType, pageableType } from '../types/ChatType';

export const getMsg = async (
  partyId: number,
  headerOpt: Record<string,string>,
  pageable: pageableType,
  Response : (Response : AxiosResponse<getMsgResponseType>) => void, 
  Error : (Error : AxiosResponse<ResponseData>) => void) => {
  await privateApi.get(`/chat-list/${partyId}`, { params: pageable, headers: {...headerOpt}})
  .then(Response)
  .catch(Error)
}