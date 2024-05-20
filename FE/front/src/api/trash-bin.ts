import { AxiosResponse } from 'axios';
import { privateApi } from '../utils/http-commons';
import { ResponseData } from '../types/ApiResponseType';
import { GetTrashResponseType, trashInfoType } from '../types/TrashType';

const url = 'trash-bin';

export const getTrashBin = async(
  trashBinId: number,
  Response : (Response : AxiosResponse<GetTrashResponseType>) => void, 
  Error : (Error : AxiosResponse<ResponseData>) => void) => {
  await privateApi.get(`/${url}/${trashBinId}`)
  .then(Response)
  .catch(Error)
}

export const delTrashBin = async(
  trashBinId: number,
  trashInfo: trashInfoType,
  Response : (Response : AxiosResponse<ResponseData>) => void, 
  Error : (Error : AxiosResponse<ResponseData>) => void) => {
  await privateApi.delete(`/${url}/${trashBinId}/trashes`, { data: trashInfo })
  .then(Response)
  .catch(Error)
}

export const restoreTrashBin = async(
  trashBinId: number,
  trashInfo: trashInfoType,
  Response : (Response : AxiosResponse<ResponseData>) => void, 
  Error : (Error : AxiosResponse<ResponseData>) => void) => {
  await privateApi.patch(`/${url}/${trashBinId}/trashes`, trashInfo)
  .then(Response)
  .catch(Error)
}