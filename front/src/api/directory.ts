import { AxiosResponse } from 'axios';
import { privateApi } from '../utils/http-commons';
import { ResponseData } from '../types/ApiResponseType';
import { AlbumResponse } from '../types/AlbumType';

const url = 'directories';

export const getDirectory = async(
  dirId: string,
  Response : (Response : AxiosResponse<AlbumResponse>) => void, 
  Error : (Error : AxiosResponse<ResponseData>) => void) => {
  await privateApi.get(`/${url}/${dirId}`)
  .then(Response)
  .catch(Error)
}
