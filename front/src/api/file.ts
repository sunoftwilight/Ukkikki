import { AxiosResponse } from 'axios';
import { downloadApi } from '../utils/http-commons';
import { ResponseData } from '../types/ApiResponseType';
import { FileDownloadDto } from '../types/AlbumType';

const url = 'file';

export const downloadFile = async(
  fileDownloadDto : FileDownloadDto,
  Response : (Response : AxiosResponse<Blob>) => void, 
  Error : (Error : AxiosResponse<ResponseData>) => void) => {
  await downloadApi.get(`/${url}/download`, { params: fileDownloadDto })
  .then(Response)
  .catch(Error)
}
