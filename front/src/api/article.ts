import { AxiosResponse } from 'axios';
import { privateApi, formDataApi, imgApi } from '../utils/http-commons';
import { ResponseData } from '../types/ApiResponseType';
import { ArticleListRespones, ArticleDetailRespones } from '../types/ArticleType';

export const getArticleList = async(
  groupPk: number,
  Response : (Response : AxiosResponse<ArticleListRespones>) => void, 
  Error : (Error : AxiosResponse<ResponseData>) => void) => {
  await privateApi.get(`/article/article-list/${groupPk}`)
  .then(Response)
  .catch(Error)
}

export const getArticleDetail = async(
  groupPk: number,
  articlePk: number,
  Response : (Response : AxiosResponse<ArticleDetailRespones>) => void, 
  Error : (Error : AxiosResponse<ResponseData>) => void) => {
  await privateApi.get(`/article/detail/${groupPk}/${articlePk}`)
  .then(Response)
  .catch(Error)
}

export const createArticle = async(
  groupPk: number,
  data: FormData,
  Response : (Response : AxiosResponse<ResponseData>) => void, 
  Error : (Error : AxiosResponse<ResponseData>) => void) => {
  await formDataApi.post(`/article/create/${groupPk}`,data)
  .then(Response)
  .catch(Error)
  }

export const updateArticle = async(
  groupPk: number,
  articlePk: number,
  data: FormData,
  Response : (Response : AxiosResponse<ResponseData>) => void, 
  Error : (Error : AxiosResponse<ResponseData>) => void) => {
  await formDataApi.patch(`/article/update/${groupPk}/${articlePk}`, data)
  .then(Response)
  .catch(Error)
  }

export const deleteAritcle = async(
  groupPk: number,
  articlePk: number,
  Response : (Response : AxiosResponse<ResponseData>) => void, 
  Error : (Error : AxiosResponse<ResponseData>) => void) => {
  await privateApi.delete(`/article/delete/${groupPk}/${articlePk}`)
  .then(Response)
  .catch(Error)
  }


export const getArticleImg = async (
  thumbUrl: string,
  headerOpt: Record<string,string>,
  Response: (Response: AxiosResponse<ResponseData>) => void,
  Error: (Error: AxiosResponse<ResponseData>) => void) => {
    await imgApi.get(`${thumbUrl}`,{headers: {...headerOpt}})
    .then(Response)
    .catch(Error)
  }
  