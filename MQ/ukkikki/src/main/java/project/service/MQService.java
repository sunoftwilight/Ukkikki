package project.service;

import project.dto.MQDto;

public interface MQService {
    public void fileUpload(MQDto mqDto);
    public void fileAiUpload(int index);
    public void finish(int index);
}
