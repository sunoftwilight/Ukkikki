import { AxiosResponse } from 'axios';
import { commonApi } from '../utils/http-commons';
import { ResponseData } from '../types/ApiResponseType';
import { AlarmRequestType } from '../types/AlarmType';

const url = 'alarm';

export const connectAlarm = async(
  Response : (Response : AxiosResponse<ResponseData>) => void, 
  Error : (Error : AxiosResponse<ResponseData>) => void) => {
  await commonApi.get(`/${url}/sub`)
  .then(Response)
  .catch(Error)
}

export const getAlarm = async(
  alarmPageDto : AlarmRequestType,
  Response : (Response : AxiosResponse<ResponseData>) => void, 
  Error : (Error : AxiosResponse<ResponseData>) => void) => {
  await commonApi.get(`/${url}/list`, { params: alarmPageDto } )
  .then(Response)
  .catch(Error)
}