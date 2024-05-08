import { AxiosResponse } from 'axios';
import { commonApi } from '../utils/http-commons';
import { ResponseData } from '../types/ApiResponseType';
import { FileDownloadDto } from '../types/AlbumType';

const url = 'file';

export const downloadFile = async(
  fileDownloadDto : FileDownloadDto,
  Response : (Response : AxiosResponse<ResponseData>) => void, 
  Error : (Error : AxiosResponse<ResponseData>) => void) => {
  await commonApi.get(`/${url}/download`, {params: { 'fileDownloadDto': fileDownloadDto }})
  .then(Response)
  .catch(Error)
}
