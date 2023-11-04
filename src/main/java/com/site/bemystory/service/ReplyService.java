package com.site.bemystory.service;

import com.site.bemystory.domain.Book;
import com.site.bemystory.domain.Reply;
import com.site.bemystory.domain.User;
import com.site.bemystory.dto.ReplyDTO;
import com.site.bemystory.dto.ReplyRequest;
import com.site.bemystory.exception.AuthorizationException;
import com.site.bemystory.exception.ErrorCode;
import com.site.bemystory.repository.BookRepository;
import com.site.bemystory.repository.ReplyRepository;
import com.site.bemystory.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReplyService {
    private final ReplyRepository replyRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    /**
     * 모댓글 달기
     */
    @Transactional
    public ReplyRequest writeReply(Long bId, String writer, ReplyDTO reply){
        User wuser =  userRepository.findByUserName(writer).orElseThrow();
        log.info("writer: {}", wuser.getUser_id());
        Book book = bookRepository.findById(bId).orElseThrow();
        log.info("book: {}", book.getTitle());
        Reply r = Reply.builder()
                .grp(1l)
                .grps(0l)
                .writer(wuser)
                .grpl(0)
                .book(book)
                .build();
        r.setDate(reply.getDate());
        r.setContent(reply.getContent());
        replyRepository.save(r);
        r.setGrp(r.getId());
        return new ReplyRequest(r.getId());
    }

    @Transactional
    public ReplyRequest deleteReply(String writer, Long replyId){
        Reply r = replyRepository.findById(replyId).orElseThrow();
        //댓쓴 사람이 아닌 유저가 지우려고 하면 예외 발생
        if(!Objects.equals(r.getWriter().getUserName(), writer)){
            throw new AuthorizationException(ErrorCode.INVALID_AUTHORIZATION, "댓글을 지울 권한이 없습니다.");
        }
        ReplyRequest request = new ReplyRequest(r.getId());
        replyRepository.delete(r);
        return request;
    }
}
