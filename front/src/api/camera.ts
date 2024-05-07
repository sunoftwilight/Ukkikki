import { AxiosResponse } from "axios";
import { formDataApi } from "../utils/http-commons";
import { ResponseData } from "../types/Camera";

export const upLoadPhoto = async (
    params: FormData,
    Response: (Response: AxiosResponse<ResponseData>) => void,
    Error: (Error: AxiosResponse<ResponseData>) => void) => {
        await formDataApi.post('/file/upload',params)
        .then(Response)
        .catch(Error)
};
