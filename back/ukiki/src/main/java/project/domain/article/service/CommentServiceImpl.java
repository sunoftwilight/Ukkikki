package project.domain.article.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.domain.article.collection.CommentCollection;
import project.domain.article.dto.response.ArticleCreateResDto;
import project.domain.article.repository.CommentRepository;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{

    private final CommentRepository commentRepository;


    @Override
    @Transactional
    public void createComment(ArticleCreateResDto articleCreateResDto) {
        Optional<CommentCollection> cc = commentRepository.findById(articleCreateResDto.getArticleId());


        // 이미 있다면 끝.
        if(cc.isPresent()){
            return ;
        }

        // CommentCollection 객체 생성,
        CommentCollection commentCollection = CommentCollection.builder()
                .id(articleCreateResDto.getArticleId())
                .build();

        commentRepository.save(commentCollection);
    }
}
