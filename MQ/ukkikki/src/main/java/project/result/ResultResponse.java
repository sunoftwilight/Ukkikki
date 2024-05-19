package project.result;

public record ResultResponse(ResultCode resultCode, Object data) {

    public ResultResponse(ResultCode resultCode) {
        this(resultCode, null);
    }
}
