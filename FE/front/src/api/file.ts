import { AxiosResponse } from 'axios';
import { downloadApi, formDataApi } from '../utils/http-commons';
import { ResponseData } from '../types/ApiResponseType';
import { FileDownloadDto, MultiFileDownloadDto } from '../types/AlbumType';
import qs from 'qs';

const url = 'file';

export const downloadFile = async(
  sseKey : string,
  fileDownloadDto : FileDownloadDto,
  Response : (Response : AxiosResponse<Blob>) => void, 
  Error : (Error : AxiosResponse<ResponseData>) => void) => {
  await downloadApi(sseKey).get(`/${url}/download`, { params: fileDownloadDto })
  .then(Response)
  .catch(Error)
}

export const multiDownloadFile = async(
  sseKey : string,
  multiFileDownloadDto : MultiFileDownloadDto,
  Response : (Response : AxiosResponse<Blob>) => void, 
  Error : (Error : AxiosResponse<ResponseData>) => void) => {
  await downloadApi(sseKey)
  .get(
    `/${url}/multi-select-download`, { 
      params: multiFileDownloadDto,
      paramsSerializer: (params) => {
        return qs.stringify(params, { arrayFormat: "comma"});
      }
  })
  .then(Response)
  .catch(Error)
  }

export const uploadFile = async (
  params: FormData,
  Response: (Response: AxiosResponse<ResponseData>) => void,
  Error: (Error: AxiosResponse<ResponseData>) => void,) => {
  await formDataApi.post(`/${url}/upload/dir`, params)
  .then(Response)
  .catch(Error);
};
