import { AxiosResponse } from 'axios';
import { downloadApi } from '../utils/http-commons';
import { ResponseData } from '../types/ApiResponseType';
import { FileDownloadDto, MultiFileDownloadDto } from '../types/AlbumType';
import qs from 'qs';

const url = 'file';

export const downloadFile = async(
  fileDownloadDto : FileDownloadDto,
  Response : (Response : AxiosResponse<Blob>) => void, 
  Error : (Error : AxiosResponse<ResponseData>) => void) => {
  await downloadApi.get(`/${url}/download`, { params: fileDownloadDto })
  .then(Response)
  .catch(Error)
}

export const multiDownloadFile = async(
  multiFileDownloadDto : MultiFileDownloadDto,
  Response : (Response : AxiosResponse<Blob>) => void, 
  Error : (Error : AxiosResponse<ResponseData>) => void) => {
  await downloadApi.get(
    `/${url}/multi-select-download`, { 
      params: multiFileDownloadDto,
      paramsSerializer: (params) => {
        return qs.stringify(params, { arrayFormat: "comma"})
      }
  })
  .then(Response)
  .catch(Error)
  }