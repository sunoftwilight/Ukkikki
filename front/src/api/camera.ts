import { AxiosResponse } from "axios";
import { formDataApi } from "../utils/http-common";
import { RequestData, ResponseData } from "../types/Camera";

export const upLoadPhoto = async (
    params: RequestData,
    Response: (Response: AxiosResponse<ResponseData>) => void,
    Error: (Error: AxiosResponse<ResponseData>) => void) => {
        await formDataApi('')
        .then(Response)
        .catch(Error)
};
