package project.domain.article.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import project.domain.article.dto.request.ArticleCreateDto;
import project.domain.article.dto.response.ArticleCreateResDto;
import project.domain.article.dto.response.ArticlePageDto;
import project.domain.article.dto.response.SimpleArticleDto;
import project.domain.article.entity.Article;
import project.domain.article.entity.ArticlePhoto;
import project.domain.article.mapper.ArticleMapper;
import project.domain.article.mapper.ArticlePhotoMapper;
import project.domain.article.repository.ArticlePhotoRepository;
import project.domain.article.repository.ArticleRepository;
import project.domain.directory.collection.File;
import project.domain.directory.repository.FileRepository;
import project.domain.member.dto.request.CustomUserDetails;
import project.domain.member.entity.Member;
import project.domain.member.entity.MemberRole;
import project.domain.member.entity.Profile;
import project.domain.member.repository.MemberRepository;
import project.domain.member.repository.ProfileRepository;
import project.domain.party.entity.MemberParty;
import project.domain.party.entity.Party;
import project.domain.party.repository.MemberpartyRepository;
import project.domain.party.repository.PartyRepository;
import project.domain.photo.entity.Photo;
import project.domain.photo.repository.PhotoRepository;
import project.global.exception.BusinessLogicException;
import project.global.exception.ErrorCode;

import java.util.ArrayList;
import java.util.List;

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

    private final ArticleMapper articleMapper;
    private final ArticlePhotoMapper articlePhotoMapper;


    @Override
    public ArticleCreateResDto createArticle(Long partyId, ArticleCreateDto articleCreateDto) {

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
        if (memberParty.getMemberRole().equals(MemberRole.BLOCK) ||
            memberParty.getMemberRole().equals(MemberRole.VIEWER)){
            throw new BusinessLogicException(ErrorCode.NOT_ROLE_EDIT);
        }

        Profile profile = profileRepository.findByMemberIdAndPartyId(memberId, partyId)
            .orElseThrow(() -> new BusinessLogicException(ErrorCode.MEMBER_NOT_PROFILE));

        Article article = articleRepository.save(
            Article.builder()
                .title(articleCreateDto.getTitle())
                .content(articleCreateDto.getContent())
                .writer(profile.getNickname())
                .member(member)
                .party(party)
                .build());
        
        // 게시판 사진 리스트
        List<ArticlePhoto> articlePhotoList = new ArrayList<>();

        for (String filePk : articleCreateDto.getPhotoIdList()) {
            // 파일 찾기
            File file = fileRepository.findById(filePk)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.FILE_NOT_FOUND));
            // 파일로 사진 찾기
            Photo photo = photoRepository.findById(file.getPhotoDto().getId())
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.PHOTO_FILE_NOT_FOUND));
            // ArticlePhoto 생성
            ArticlePhoto articlePhoto = ArticlePhoto.create(photo, article);
            // 리스트에 넣고 저장
            articlePhotoList.add(articlePhotoRepository.save(articlePhoto));
        }


        articleRepository.save(article);
        article.setArticlePhotoList(articlePhotoList);

        // 게시판 PK 반환
        return ArticleCreateResDto
            .builder()
            .articleId(article.getId())
            .build();
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

        res.setModify(article.getCreateDate().isEqual(article.getLastModifiedDate()));

        List<ArticlePhoto> articlePhotoList = article.getArticlePhotoList();
        res.setPhotoList(articlePhotoMapper.toSimpleArticlePhotoDtoList(articlePhotoList));

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
                simpleArticleDto.setModify(article.getCreateDate().isEqual(article.getLastModifiedDate()));
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

}
