package project.domain.directory.mapper;

import java.util.List;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import project.domain.directory.collection.TrashBin;
import project.domain.directory.dto.response.GetTrashBinDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GetTrashBinMapper {

    @Mappings({
        @Mapping(source = "trashBin.id", target = "id"),
        @Mapping(source = "trashBin.trashBinName", target = "trashBinName"),
        @Mapping(source = "trashBin.dirIdList", target = "dirIdList"),
        @Mapping(target = "dirNameList", ignore = true),
        @Mapping(source = "trashBin.fileIdList", target = "fileIdList"),
        @Mapping(target = "photoUrlList", ignore = true),
    })
    GetTrashBinDto toGetTrashBinDto(
        TrashBin trashBin,
        List<String> dirNameList,
        List<String> photoUrlList
    );

    @AfterMapping
    default void fillGetTrashBinDto(
        @MappingTarget GetTrashBinDto getTrashBinDto,
        List<String> dirNameList,
        List<String> photoUrlList
    ) {
        getTrashBinDto.setDirNameList(dirNameList);
        getTrashBinDto.setPhotoUrlList(photoUrlList);
    }

}
