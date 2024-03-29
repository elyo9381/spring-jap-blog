package com.cos.blog.service;

import com.cos.blog.dto.ReplySaveRequestDto;
import com.cos.blog.model.Board;
import com.cos.blog.model.Reply;
import com.cos.blog.model.RoleType;
import com.cos.blog.model.User;
import com.cos.blog.repository.BoardRepository;
import com.cos.blog.repository.ReplyRepository;
import com.cos.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    private final ReplyRepository replyRepository;

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder encoder;

    @Transactional
    public void 글쓰기(Board board,User user){
        board.setCount(0);
        board.setUser(user);
        boardRepository.save(board);
    }

    @Transactional(readOnly = true)
    public Page<Board> 글목록(Pageable pageable) {
        return boardRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Board 글상세보기(int id) {
        return boardRepository.findById(id).orElseThrow(()->{
            return new IllegalArgumentException("글 상세보기 실패 : 아이디 없습니다. ");
        });
    }

    @Transactional
    public void 글삭제하기(int id) {
         boardRepository.deleteById(id);
    }

    @Transactional
    public void 글수정하기(int id,Board requestBoard) {
        Board board = boardRepository.findById(id).orElseThrow(()->{
            return new IllegalArgumentException("글 찾기 실패 : 아이디 없습니다. ");
        });
        board.setTitle(requestBoard.getTitle());
        board.setContent(requestBoard.getContent());

        //해당 함수로 종료시 Service가 종료될때) 트랜잭션이 종료됩니다. 이떄 더티체킹 - 자동업데이트가 됩 db flush
    }


    @Transactional
    public void 댓글쓰기(ReplySaveRequestDto replySaveRequestDto) {

        User user = userRepository.findById(replySaveRequestDto.getUserId()).orElseThrow(() -> {
            return new IllegalArgumentException("댓글 쓰기 실패 : 유저 id를 찾을 수 없습니다.");
        });

        Board board = boardRepository.findById(replySaveRequestDto.getBoardId()).orElseThrow(() -> {
            return new IllegalArgumentException("댓글 쓰기 실패 : board id를 찾을 수 없습니다.");
        });


        // 방법 1
//        Reply reply = Reply.builder()
//                .user(user)
//                .board(board)
//                .content(replySaveRequestDto.getContent())
//                .build();

        // 방법 2
        Reply reply = new Reply();
        reply.update(user,board,replySaveRequestDto.getContent());

        replyRepository.save(reply);

//        int result = replyRepository.mSave(replySaveRequestDto.getUserId(), replySaveRequestDto.getBoardId(), replySaveRequestDto.getContent());
//        System.out.println("BoardService : "+result);
    }

    @Transactional
    public void 댓글삭제(int replyId) {
        replyRepository.deleteById(replyId);
    }
}

//    @Transactional(readOnly = true) // Select할때 트랜잭션 시작,서비스 종료시에 트랜잭션 종료 (정합성)
//    public User 로그인(User user) {
//        return userRepository.findByUsernameAndPassword(user.getUsername(),user.getPassword());
//    }
