package project.domain.article.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import project.domain.article.dto.request.ArticleCreateDto;
import project.domain.article.dto.response.ArticleCreateResDto;
import project.domain.article.entity.Article;
import project.domain.article.entity.ArticlePhoto;
import project.domain.article.repository.ArticlePhotoRepository;
import project.domain.article.repository.ArticleRepository;
import project.domain.member.dto.request.CustomUserDetails;
import project.domain.member.entity.Member;
import project.domain.member.entity.MemberRole;
import project.domain.member.repository.MemberRepository;
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
public class ArticleServiceImpl implements ArticleService{

    MemberRepository memberRepository;
    PartyRepository partyRepository;
    MemberpartyRepository memberpartyRepository;
    ArticleRepository articleRepository;
    PhotoRepository photoRepository;
    ArticlePhotoRepository articlePhotoRepository;

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
        
        Article article = articleRepository.save(
            Article.builder()
                .title(articleCreateDto.getTitle())
                .content(articleCreateDto.getContent())
                .member(member)
                .party(party)
                .build());
        
        // 게시판 사진 리스트
        List<ArticlePhoto> articlePhotoList = new ArrayList<>();

        for (Long photoPk : articleCreateDto.getPhotoIdList()) {

            Photo photo = photoRepository.findById(photoPk)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.PHOTO_FILE_NOT_FOUND));

            ArticlePhoto articlePhoto = ArticlePhoto.create(photo, article);

            articlePhotoList.add(articlePhotoRepository.save(articlePhoto));
        }

        article.setArticlePhotoList(articlePhotoList);

        articleRepository.save(article);

        // 게시판 PK 반환
        return ArticleCreateResDto
            .builder()
            .articleId(article.getId())
            .build();
    }
}
