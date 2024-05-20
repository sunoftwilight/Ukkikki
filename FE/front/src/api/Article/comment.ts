import { AxiosResponse } from 'axios';
import { privateApi } from '../../utils/http-commons';
import { ResponseData } from '../../types/ApiResponseType';
import { CommentInputType } from '../../types/Comment/CommentType';
import { CommentResponseData } from '../../types/Comment/CommentInterface';

const url = 'article';


export const getComment = async(
    articleId: number,
    Response : (Response : AxiosResponse<CommentResponseData>) => void, 
    Error : (Error : AxiosResponse<ResponseData>) => void) => {
    await privateApi.get(`/${url}/comment/${articleId}`)
    .then(Response)
    .catch(Error)
  }

  
export const createComment = async(
    articleId: number,
    params : CommentInputType,
    Response : (Response : AxiosResponse<ResponseData>) => void, 
    Error : (Error : AxiosResponse<ResponseData>) => void) => {
    await privateApi.post(`/${url}/comment/enter/${articleId}`, params)
    .then(Response)
    .catch(Error)
  }

  
export const modifyComment = async(
    articleId: number,
    commentId: number,
    params : CommentInputType,
    Response : (Response : AxiosResponse<ResponseData>) => void, 
    Error : (Error : AxiosResponse<ResponseData>) => void) => {
    await privateApi.patch(`/${url}/comment/modify/${articleId}/${commentId}`,params)
    .then(Response)
    .catch(Error)
  }

  
export const deleteComment = async(
    articleId: number,
    commentId: number,
    Response : (Response : AxiosResponse<ResponseData>) => void, 
    Error : (Error : AxiosResponse<ResponseData>) => void) => {
    await privateApi.delete(`/${url}/comment/${articleId}/${commentId}`)
    .then(Response)
    .catch(Error)
  }
  
export const createReply = async(
    articleId: number,
    commentId: number,
    params : CommentInputType,
    Response : (Response : AxiosResponse<ResponseData>) => void, 
    Error : (Error : AxiosResponse<ResponseData>) => void) => {
    await privateApi.post(`/${url}/reply/enter/${articleId}/${commentId}`,params)
    .then(Response)
    .catch(Error)
  }

  
export const modifyReply = async(
    articleId: number,
    commentId: number,
    replyId: number,
    params : CommentInputType,
    Response : (Response : AxiosResponse<ResponseData>) => void, 
    Error : (Error : AxiosResponse<ResponseData>) => void) => {
    await privateApi.patch(`/${url}/reply/modify/${articleId}/${commentId}/${replyId}`, params)
    .then(Response)
    .catch(Error)
  }

  
export const deleteReply = async(
    articleId: number,
    commentId: number,
    replyId: number,
    Response : (Response : AxiosResponse<ResponseData>) => void, 
    Error : (Error : AxiosResponse<ResponseData>) => void) => {
    await privateApi.delete(`/${url}/reply/${articleId}/${commentId}/${replyId}`)
    .then(Response)
    .catch(Error)
  }
