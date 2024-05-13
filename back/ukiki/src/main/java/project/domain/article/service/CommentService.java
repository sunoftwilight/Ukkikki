package project.domain.article.service;

import project.domain.article.dto.response.ArticleCreateResDto;

public interface CommentService {
    // 댓글 관리
    void createComment(ArticleCreateResDto articleCreateResDto);

}
