import { AxiosResponse } from 'axios';
import { privateApi } from '../utils/http-commons';
import { ResponseData } from '../types/ApiResponseType';
import { MemoResponseData } from '../types/DetailImgType';

const url = 'photo';

export const getMemo = async (
  fileId: string,
  Response : (Response : AxiosResponse<MemoResponseData>) => void, 
  Error : (Error : AxiosResponse<ResponseData>) => void) => {
  await privateApi.get(`/${url}/memo/${fileId}`)
  .then(Response)
  .catch(Error)
}

export const createMemo = async (
    fileId: string,
    content: string,
    Response : (Response : AxiosResponse<ResponseData>) => void, 
    Error : (Error : AxiosResponse<ResponseData>) => void) => {
        await privateApi.post(`${url}/memo/create/${fileId}`,{content})
        .then(Response)
        .catch(Error)
}

export const modifyMemo = async (
    memoId: number,
    content: string,
    Response : (Response : AxiosResponse<ResponseData>) => void, 
    Error : (Error : AxiosResponse<ResponseData>) => void) => {
        await privateApi.post(`${url}/memo/modify/${memoId}`,{content})
        .then(Response)
        .catch(Error)
}

export const deleteMemo = async (
    memoId: number,
    Response : (Response : AxiosResponse<ResponseData>) => void, 
    Error : (Error : AxiosResponse<ResponseData>) => void) => {
        await privateApi.delete(`${url}/memo/delete/${memoId}`)
        .then(Response)
        .catch(Error)
}

export const createLike = async (
  fileId : string,
  Response : (Response : AxiosResponse<ResponseData>) => void, 
  Error : (Error : AxiosResponse<ResponseData>) => void) => {
    await privateApi.post(`${url}/likes/${fileId}`)
    .then(Response)
    .catch(Error)
}

export const deleteLike = async (
  fileId : string,
  Response : (Response : AxiosResponse<ResponseData>) => void, 
  Error : (Error : AxiosResponse<ResponseData>) => void) => {
    await privateApi.delete(`${url}/likes/${fileId}`)
    .then(Response)
    .catch(Error)
}