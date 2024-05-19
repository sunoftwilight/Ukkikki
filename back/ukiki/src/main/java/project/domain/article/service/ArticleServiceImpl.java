package project.domain.article.service;

import com.amazonaws.services.s3.model.SSECustomerKey;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import project.domain.article.collection.CommentCollection;
import project.domain.article.dto.request.ArticleCreateDto;
import project.domain.article.dto.request.ArticleUpdateDto;
import project.domain.article.dto.response.ArticleCreateResDto;
import project.domain.article.dto.response.ArticlePageDto;
import project.domain.article.dto.response.SimpleArticleDto;
import project.domain.article.dto.response.SimpleArticlePhotoDto;
import project.domain.article.entity.Article;
import project.domain.article.entity.ArticlePhoto;
import project.domain.article.entity.PhotoType;
import project.domain.article.mapper.ArticleMapper;
import project.domain.article.mapper.ArticlePhotoMapper;
import project.domain.article.repository.ArticlePhotoRepository;
import project.domain.article.repository.ArticleRepository;
import project.domain.article.repository.CommentRepository;
import project.domain.directory.collection.File;
import project.domain.directory.mapper.GetFileMapper;
import project.domain.directory.repository.FileRepository;
import project.domain.member.dto.request.CustomUserDetails;
import project.domain.member.entity.Member;
import project.domain.member.entity.MemberRole;
import project.domain.member.entity.Profile;
import project.domain.member.repository.MemberRepository;
import project.domain.member.repository.ProfileRepository;
import project.domain.party.dto.response.SimplePartyDto;
import project.domain.party.entity.MemberParty;
import project.domain.party.entity.Party;
import project.domain.party.repository.MemberpartyRepository;
import project.domain.party.repository.PartyRepository;
import project.domain.photo.dto.request.FileUploadDto;
import project.domain.photo.entity.Photo;
import project.domain.photo.entity.PhotoUrl;
import project.domain.photo.mapper.PhotoMapper;
import project.domain.photo.repository.PhotoRepository;
import project.domain.photo.service.FileUploadDownloadService;
import project.global.exception.BusinessLogicException;
import project.global.exception.ErrorCode;
import project.global.util.S3Util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService{

    private final MemberRepository memberRepository;
    private final PartyRepository partyRepository;
    private final MemberpartyRepository memberpartyRepository;
    private final ArticleRepository articleRepository;
    private final PhotoRepository photoRepository;
    private final ArticlePhotoRepository articlePhotoRepository;
    private final FileRepository fileRepository;
    private final ProfileRepository profileRepository;
    private final CommentRepository commentRepository;


    private final ArticleMapper articleMapper;
    private final ArticlePhotoMapper articlePhotoMapper;
    private final GetFileMapper fileMapper;
    private final PhotoMapper photoMapper;

    private final S3Util s3Util;

    private final CommentServiceImpl commentService;
    private final FileUploadDownloadService fileUploadDownloadService;

    @Override
    @Transactional
    public ArticleCreateResDto createArticle(Long partyId, ArticleCreateDto articleCreateDto, List<MultipartFile> multipartFiles) {

        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long memberId = userDetails.getId();

        // GUEST 차단
        if(memberId == 0){
            throw new BusinessLogicException(ErrorCode.NOT_ROLE_GUEST);
        }

        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new BusinessLogicException(ErrorCode.MEMBER_NOT_FOUND));

        Party party = partyRepository.findById(partyId)
            .orElseThrow(() -> new BusinessLogicException(ErrorCode.PARTY_NOT_FOUND));

        // 내 파티 권한 가지고 오기
        MemberParty memberParty = memberpartyRepository.findByMemberIdAndPartyId(memberId, partyId)
            .orElseThrow(() -> new BusinessLogicException(ErrorCode.NOT_EXIST_PARTY_USER));

        // BLOCK or Viewr 차단
        if (memberParty.getMemberRole().equals(MemberRole.BLOCK)){
            throw new BusinessLogicException(ErrorCode.NOT_ROLE_EDIT);
        }

        Profile profile = profileRepository.findByMemberIdAndPartyId(memberId, partyId)
            .orElseThrow(() -> new BusinessLogicException(ErrorCode.MEMBER_NOT_PROFILE));

        Article article = articleRepository.save(
            Article.builder()
                .title(articleCreateDto.getTitle())
                .content(articleCreateDto.getContent())
                .writer(profile.getNickname())
                .writerUrl(profile.getProfileUrl())
                .member(member)
                .party(party)
                .build());

        // Device 사진 추가하기
        List<String> deviceFileList = new ArrayList<>();
        if(multipartFiles != null){
            deviceFileList = fileUploadDownloadService.uploadProcess(multipartFiles, FileUploadDto.builder()
                .partyId(partyId).key(articleCreateDto.getPassword()).build());
        }

        // 게시판 사진 리스트
        List<ArticlePhoto> articlePhotoList = new ArrayList<>();
        for (String filePk : deviceFileList) {
            // 파일 찾기
            File file = fileRepository.findById(filePk)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.FILE_NOT_FOUND));
            // 파일로 사진 찾기
            Photo photo = photoRepository.findById(file.getPhotoDto().getId())
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.PHOTO_FILE_NOT_FOUND));
            // ArticlePhoto 생성
            ArticlePhoto articlePhoto = ArticlePhoto.create(photo, article);
            // 저장
            ArticlePhoto saveArticlePhoto = articlePhotoRepository.save(articlePhoto);
            articlePhotoList.add(saveArticlePhoto);
        }
        for (String filePk : articleCreateDto.getPhotoIdList()) {
            // 파일 찾기
            File file = fileRepository.findById(filePk)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.FILE_NOT_FOUND));
            // 파일로 사진 찾기
            Photo photo = photoRepository.findById(file.getPhotoDto().getId())
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.PHOTO_FILE_NOT_FOUND));
            // ArticlePhoto 생성
            ArticlePhoto articlePhoto = ArticlePhoto.create(photo, article);
            // 저장
            ArticlePhoto saveArticlePhoto = articlePhotoRepository.save(articlePhoto);
            articlePhotoList.add(saveArticlePhoto);
        }

        articleRepository.save(article);
        article.setArticlePhotoList(articlePhotoList);

        ArticleCreateResDto res = ArticleCreateResDto
            .builder()
            .articleId(article.getId())
            .build();
        commentService.createComment(res);
        // 게시판 PK 반환
        return res;
    }

    @Override
    public SimpleArticleDto getArticleDetail(Long partyId, Long articleId) {

        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long memberId = userDetails.getId();

        // 회원인증
        if(memberId != 0){

            memberRepository.findById(memberId)
                .orElseThrow(()-> new BusinessLogicException(ErrorCode.MEMBER_NOT_FOUND));

            MemberParty memberParty = memberpartyRepository.findByMemberIdAndPartyId(memberId, partyId)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.NOT_EXIST_PARTY_USER));

            if (memberParty.getMemberRole().equals(MemberRole.BLOCK)){
                throw new BusinessLogicException(ErrorCode.FORBIDDEN_ERROR);
            }
        }


        Article article = articleRepository.findById(articleId)
            .orElseThrow(()-> new BusinessLogicException(ErrorCode.ARTICLE_NOT_FOUND));

        SimpleArticleDto res = articleMapper.toSimpleArticleDto(article);

        String createDate = article.getCreateDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String modifiedDate = article.getLastModifiedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        res.setModify(!createDate.equals(modifiedDate));



        List<SimpleArticlePhotoDto> fileList = article.getArticlePhotoList()
            .stream()
            .map(ArticlePhoto::getPhoto)
            .map(photo -> {
                Optional<File> opfile = fileRepository.findByPhotoDtoId(photo.getId());
                if(opfile.isPresent()){
                    SimpleArticlePhotoDto addList = fileMapper.toSimpleArticlePhotoDto(opfile.get());
                    addList.setId(articleId);
                    return addList;
                }
                return null;
            })
            .filter(Objects::nonNull)
            .collect(Collectors.toList());


        res.setPhotoList(fileList);

        return res;
    }

    @Override
    public ArticlePageDto getArticleList(Long partyId, Pageable pageable) {

        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long memberId = userDetails.getId();

        Member member = memberRepository.findById(memberId)
            .orElseThrow(()-> new BusinessLogicException(ErrorCode.MEMBER_NOT_FOUND));

        partyRepository.findById(partyId).orElseThrow(()-> new BusinessLogicException(ErrorCode.PARTY_NOT_FOUND));

        Page<Article> articleList = articleRepository.findAllByPartyId(partyId, pageable);
        List<SimpleArticleDto> articleDtoList = articleList.stream()

            .map((article) -> {
                SimpleArticleDto simpleArticleDto = articleMapper.toSimpleArticleDto(article);
                simpleArticleDto.setModify(!article.getCreateDate().isEqual(article.getLastModifiedDate()));
                List<ArticlePhoto> articlePhotoList = article.getArticlePhotoList();
                simpleArticleDto.setPhotoList(articlePhotoMapper.toSimpleArticlePhotoDtoList(articlePhotoList));
                return simpleArticleDto;
            })
            .toList();
        ArticlePageDto res = ArticlePageDto.builder()
            .articleDtoList(articleDtoList)
            .size(pageable.getPageSize())
            .page(pageable.getPageSize())
            .next(articleList.hasNext())
            .build();

        return res;
    }

    @Override
    public SimpleArticleDto updateArticle(Long partyId, Long articleId, ArticleUpdateDto articleUpdateDto, List<MultipartFile> multipartFiles) {

        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long memberId = userDetails.getId();

        if(memberId == 0){
            throw new BusinessLogicException(ErrorCode.NOT_ROLE_GUEST);
        }

        memberRepository.findById(memberId)
            .orElseThrow(()-> new BusinessLogicException(ErrorCode.MEMBER_NOT_FOUND));

        partyRepository.findById(partyId)
            .orElseThrow(()-> new BusinessLogicException(ErrorCode.PARTY_NOT_FOUND));


        MemberParty memberParty = memberpartyRepository.findByMemberIdAndPartyId(memberId, partyId)
            .orElseThrow(()-> new BusinessLogicException(ErrorCode.NOT_EXIST_PARTY_USER));

        Article article = articleRepository.findById(articleId)
            .orElseThrow(()-> new BusinessLogicException(ErrorCode.ARTICLE_NOT_FOUND));

        // 글쓴이가 다르다면 리턴
        if (!memberId.equals(article.getMember().getId())){
            throw new BusinessLogicException(ErrorCode.FORBIDDEN_ERROR);
        }
        // 제목 변화 여부
        if (articleUpdateDto.getTitle() != null){
            article.setTitle(articleUpdateDto.getTitle());
        }
        // 본문 변화 여부
        if (articleUpdateDto.getContent() != null){
            article.setContent(articleUpdateDto.getContent());
        }

        // Device 사진 추가하기
        List<String> deviceFileList = new ArrayList<>();
        if(multipartFiles != null){
            deviceFileList = fileUploadDownloadService.uploadProcess(multipartFiles, FileUploadDto.builder()
                .partyId(partyId).key(articleUpdateDto.getPassword()).build());
        }
        for (String filePk : deviceFileList) {
            // 파일 찾기
            File file = fileRepository.findById(filePk)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.FILE_NOT_FOUND));
            // 파일로 사진 찾기
            Photo photo = photoRepository.findById(file.getPhotoDto().getId())
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.PHOTO_FILE_NOT_FOUND));
            // ArticlePhoto 생성
            ArticlePhoto articlePhoto = ArticlePhoto.create(photo, article);
            // 리스트에 넣고 저장
            article.getArticlePhotoList().add(articlePhotoRepository.save(articlePhoto));
        }

        // 폴더 사진 추가 여부
        if (articleUpdateDto.getArticlePhotoList() != null){
            if (articleUpdateDto.isAddPhoto()){
                // 포토 추가
                for (Object filePk : articleUpdateDto.getArticlePhotoList()) {
                    // 파일 찾기
                    File file = fileRepository.findById(String.valueOf(filePk))
                        .orElseThrow(() -> new BusinessLogicException(ErrorCode.FILE_NOT_FOUND));
                    // 파일로 사진 찾기
                    Photo photo = photoRepository.findById(file.getPhotoDto().getId())
                        .orElseThrow(() -> new BusinessLogicException(ErrorCode.PHOTO_FILE_NOT_FOUND));
                    // ArticlePhoto 생성
                    ArticlePhoto articlePhoto = ArticlePhoto.create(photo, article);
                    articlePhotoRepository.findByArticleIdAndPhotoId(articleId, photo.getId())
                        .ifPresentOrElse(
                            articlePhoto1 -> article.getArticlePhotoList().add(articlePhoto1),
                            () -> article.getArticlePhotoList().add(articlePhotoRepository.save(articlePhoto))
                        );
                    // 리스트에 넣고 저장
                    article.getArticlePhotoList().add(articlePhotoRepository.save(articlePhoto));
                }
            }else{
                // 포토 삭제
                for (Object filePk : articleUpdateDto.getArticlePhotoList()) {
                    articlePhotoRepository.findById(Long.parseLong(String.valueOf(filePk)))
                        .ifPresent(ArticlePhoto::delete);
                }
            }
        }
        articleRepository.save(article);
        SimpleArticleDto res = articleMapper.toSimpleArticleDto(article);

        res.setModify(true);

        List<ArticlePhoto> articlePhotoList = article.getArticlePhotoList();
        res.setPhotoList(articlePhotoMapper.toSimpleArticlePhotoDtoList(articlePhotoList));
        
        return res;

    }

    @Override
    public void deleteArticle(Long partyId, Long articleId) {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long memberId = userDetails.getId();

        memberRepository.findById(memberId)
            .orElseThrow(()-> new BusinessLogicException(ErrorCode.MEMBER_NOT_FOUND));

        partyRepository.findById(partyId)
            .orElseThrow(()-> new BusinessLogicException(ErrorCode.PARTY_NOT_FOUND));

        Article article = articleRepository.findById(articleId)
                .orElseThrow(()-> new BusinessLogicException(ErrorCode.ARTICLE_NOT_FOUND));

        // 멤버 권한 조회
        memberpartyRepository.findByMemberIdAndPartyId(memberId,partyId)
            .ifPresentOrElse(memberParty -> {
                // BLOCK 유저거나 글쓴이가 아니라면
                if (memberParty.getMemberRole().equals(MemberRole.BLOCK) || !memberId.equals(article.getMember().getId())) {
                    // 마스터 권한이 아니라면 리턴
                    if (!memberParty.getMemberRole().equals(MemberRole.MASTER)){
                        throw new BusinessLogicException(ErrorCode.NOT_ROLE_EDIT);
                    }
                }
            }, ()-> {
                    // 멤버 권한을 못찾았을 때 리턴
                    throw new BusinessLogicException(ErrorCode.MEMBER_NOT_MATCH);
                }
            );

        // 댓글 삭제
        commentRepository.findById(articleId)
            .ifPresent(commentRepository::delete);

        // 게시판 사진 삭제
        List<ArticlePhoto> articlePhotoList = article.getArticlePhotoList();
        articlePhotoRepository.deleteAll(articlePhotoList);

        // 게시판 삭제
        articleRepository.delete(article);


    }

}
