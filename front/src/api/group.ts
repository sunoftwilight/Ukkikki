import { AxiosResponse } from 'axios';
import { privateApi } from '../utils/http-commons';
import { ResponseData } from '../types/ApiResponseType';
import { PartyDetailResponse } from '../types/GroupType';

const url = 'party';

export const getPartyDetail = async(
  partyId : number,
  Response : (Response : AxiosResponse<PartyDetailResponse>) => void, 
  Error : (Error : AxiosResponse<ResponseData>) => void) => {
  await privateApi.get(`/${url}/${partyId}`)
  .then(Response)
  .catch(Error)
}