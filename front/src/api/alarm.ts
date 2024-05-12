import { AxiosResponse } from 'axios';
import { privateApi } from '../utils/http-commons';
import { ResponseData } from '../types/ApiResponseType';
import { AlarmRequestType, AlarmResponseType } from '../types/AlarmType';

const url = 'alarm';

export const connectAlarm = async(
  Response : (Response : AxiosResponse<ResponseData>) => void, 
  Error : (Error : AxiosResponse<ResponseData>) => void) => {
  await privateApi.get(`/${url}/sub`)
  .then(Response)
  .catch(Error)
}

export const getAlarm = async(
  alarmPageDto : AlarmRequestType,
  Response : (Response : AxiosResponse<AlarmResponseType>) => void, 
  Error : (Error : AxiosResponse<ResponseData>) => void) => {
  await privateApi.get(`/${url}/list`, { params: alarmPageDto })
  .then(Response)
  .catch(Error)
}