import { AxiosResponse } from 'axios';
import { formDataApi, privateApi, imgApi } from '../utils/http-commons';
import { ResponseData } from '../types/ApiResponseType';
import { CreateDoenResponesData,
	PartyListResponesData,
	PartyLinkResponse,
	EnterGuestRespones,
	PartyCheckPassRespones,
	PartyUserListRespones,
	PartyPassChangeResponse } from '../types/GroupType';
import { PartyDetailResponse } from '../types/GroupType';

const url = 'party'

export const createParty = async (
  params: FormData,
	Response: (Response: AxiosResponse<CreateDoenResponesData>) => void,
	Error: (Error: AxiosResponse<ResponseData>) => void) => {
		await formDataApi.post(`/${url}/create`, params)
		.then(Response)
		.catch(Error)
  }

export const getPartyList = async (
	Response: (Response: AxiosResponse<PartyListResponesData>) => void,
	Error: (Error: AxiosResponse<ResponseData>) => void) => {
		await privateApi.get(`/${url}/list`)
		.then(Response)
		.catch(Error)
	}

export const getPartyThumb = async (
	thumbUrl: string,
	headerOpt: Record<string,string>,
	Response: (Response: AxiosResponse<ResponseData>) => void,
	Error: (Error: AxiosResponse<ResponseData>) => void) => {
		await imgApi.get(`${thumbUrl}`,{headers: {...headerOpt}})
		.then(Response)
		.catch(Error)
	}

  export const getPartyDetail = async (
    partyId : number,
    Response : (Response : AxiosResponse<PartyDetailResponse>) => void, 
    Error : (Error : AxiosResponse<ResponseData>) => void) => {
    await privateApi.get(`/${url}/${partyId}`)
    .then(Response)
    .catch(Error)
  }

	export const getPartyLink = async (
		partyId : number,
		Response : (Response : AxiosResponse<PartyLinkResponse>) => void, 
    Error : (Error : AxiosResponse<ResponseData>) => void) => {
    await privateApi.get(`/${url}/link/${partyId}`)
    .then(Response)
    .catch(Error)
	}


	// 오늘 작업예정

	// 1차 연동완료
	export const enterPartyMember = async (
		partyId : number,
		Response : (Response : AxiosResponse<ResponseData>) => void, 
    Error : (Error : AxiosResponse<ResponseData>) => void) => {
    await privateApi.get(`/${url}/enter/member/${partyId}`)
    .then(Response)
    .catch(Error)
	}

	// 1차 완료
	export const enterPartyGuest = async (
		partyId : number,
		Response : (Response : AxiosResponse<EnterGuestRespones>) => void, 
    Error : (Error : AxiosResponse<ResponseData>) => void) => {
    await privateApi.get(`/${url}/enter/guest/${partyId}`)
    .then(Response)
    .catch(Error)
	}

	// 1차 완료
	export const checkPartyPass = async (
		partyId : number,
		pass: string,
		simplePass: string,
		Response : (Response : AxiosResponse<PartyCheckPassRespones>) => void, 
    Error : (Error : AxiosResponse<ResponseData>) => void) => {
    await privateApi.post(`/${url}/check/password/${partyId}`, {"password": pass, "simplePassword" : simplePass})
    .then(Response)
    .catch(Error)
	}

	export const checkChangePartyPass = async (
		partyId : number,
		Response : (Response : AxiosResponse<ResponseData>) => void, 
    Error : (Error : AxiosResponse<ResponseData>) => void) => {
    await privateApi.post(`/${url}/check/changed-password/${partyId}`)
    .then(Response)
    .catch(Error)
	}

	// 1차 완료
	export const changePartyProfile = async (
		params: FormData,
		partyId: number,
		Response : (Response : AxiosResponse<ResponseData>) => void, 
    Error : (Error : AxiosResponse<ResponseData>) => void) => {
    await formDataApi.post(`/${url}/change-profile/${partyId}`, params)
    .then(Response)
    .catch(Error)
	}

	export const changePartyGrant = async (
		partyId : number,
		targetId : number,
		memberRole: {memberRole: string},
		Response : (Response : AxiosResponse<ResponseData>) => void, 
    Error : (Error : AxiosResponse<ResponseData>) => void) => {
    await privateApi.patch(`/${url}/grant/${partyId}/${targetId}`, memberRole)
    .then(Response)
    .catch(Error)
	}

	export const changePartyInfo = async (
		params: FormData,
		partyId: number,
		Response : (Response : AxiosResponse<ResponseData>) => void, 
    Error : (Error : AxiosResponse<ResponseData>) => void) => {
    await formDataApi.patch(`/${url}/change/party-info/${partyId}`, params)
    .then(Response)
    .catch(Error)
	}

	export const changePartyPass = async (
		partyId : number,
		passInfo : Record<string, string>,
		Response : (Response : AxiosResponse<PartyPassChangeResponse>) => void, 
    Error : (Error : AxiosResponse<ResponseData>) => void) => {
    await privateApi.patch(`/${url}/change/password/${partyId}`, passInfo)
    .then(Response)
    .catch(Error)
	}

	// 1
	export const getPartyUserList = async (
		partyId : number,
		Response : (Response : AxiosResponse<PartyUserListRespones>) => void, 
    Error : (Error : AxiosResponse<ResponseData>) => void) => {
    await privateApi.get(`/${url}/user-list/${partyId}`)
    .then(Response)
    .catch(Error)
	}

	export const getPartyBlockUserList = async (
		partyId : number,
		Response : (Response : AxiosResponse<PartyUserListRespones>) => void, 
    Error : (Error : AxiosResponse<ResponseData>) => void) => {
    await privateApi.get(`/${url}/block/user-list/${partyId}`)
    .then(Response)
    .catch(Error)
	}

	export const kickPartyUser = async (
		partyId : number,
		targetId : number,
		Response : (Response : AxiosResponse<ResponseData>) => void, 
    Error : (Error : AxiosResponse<ResponseData>) => void) => {
    await privateApi.delete(`/${url}/kick/${partyId}/${targetId}`)
    .then(Response)
    .catch(Error)
	}

	export const blockPartyUser = async (
		partyId : number,
		targetId : number,
		Response : (Response : AxiosResponse<ResponseData>) => void, 
    Error : (Error : AxiosResponse<ResponseData>) => void) => {
    await privateApi.patch(`/${url}/block/${partyId}/${targetId}`)
    .then(Response)
    .catch(Error)
	}

	//
	export const exitParty = async (
		partyId : number,
		Response : (Response : AxiosResponse<ResponseData>) => void, 
    Error : (Error : AxiosResponse<ResponseData>) => void) => {
    await privateApi.delete(`/${url}/exit/${partyId}`)
    .then(Response)
    .catch(Error)
	}