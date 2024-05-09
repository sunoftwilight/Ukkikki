import { AxiosResponse } from 'axios';
import { privateApi } from '../utils/http-commons';
import { ResponseData } from '../types/ApiResponseType';

const url = 'directories';

export const getDirectory = async(
  dirId: string,
  Response : (Response : AxiosResponse<ResponseData>) => void, 
  Error : (Error : AxiosResponse<ResponseData>) => void) => {
  await privateApi.get(`/${url}/${dirId}`)
  .then(Response)
  .catch(Error)
}