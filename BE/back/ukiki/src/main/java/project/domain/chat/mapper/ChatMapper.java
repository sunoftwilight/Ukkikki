package project.domain.chat.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import project.domain.chat.dto.request.ChatDto;
import project.domain.chat.dto.response.SimpleChatDto;
import project.domain.chat.entity.Chat;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ChatMapper {

    Chat toChat(ChatDto chatDto);


    @Mapping(source = "profile.profileUrl", target = "profileUrl")
    @Mapping(source = "profile.type", target = "profileType")
    @Mapping(source = "userName", target = "memberName")
    SimpleChatDto toSimpleChatDto(Chat chat);
}
