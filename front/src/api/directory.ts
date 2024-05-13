import { AxiosResponse } from 'axios';
import { privateApi } from '../utils/http-commons';
import { ResponseData } from '../types/ApiResponseType';
import { AlbumResponse, DirInfoType } from '../types/AlbumType';

const url = 'directories';

export const getDirectory = async (
  dirId: string,
  Response : (Response : AxiosResponse<AlbumResponse>) => void, 
  Error : (Error : AxiosResponse<ResponseData>) => void) => {
  await privateApi.get(`/${url}/${dirId}`)
  .then(Response)
  .catch(Error)
}

export const createDirectory = async (
  dirInfo : DirInfoType,
  Response : (Response : AxiosResponse<AlbumResponse>) => void, 
  Error : (Error : AxiosResponse<ResponseData>) => void) => {
  await privateApi.post(`/${url}`, dirInfo)
  .then(Response)
  .catch(Error)
}

export const delDirectory = async (
  dirId: string,
  Response : (Response : AxiosResponse<AlbumResponse>) => void, 
  Error : (Error : AxiosResponse<ResponseData>) => void) => {
  await privateApi.delete(`/${url}/${dirId}`)
  .then(Response)
  .catch(Error)
}

export const editDirectory = async (
  dirId : string,
  newName: string,
  Response : (Response : AxiosResponse<AlbumResponse>) => void, 
  Error : (Error : AxiosResponse<ResponseData>) => void) => {
  await privateApi.patch(`/${url}/${dirId}/rename`, { 'newName': newName })
  .then(Response)
  .catch(Error)
}