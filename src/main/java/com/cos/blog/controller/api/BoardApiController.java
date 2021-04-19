package com.cos.blog.controller.api;

import com.cos.blog.Config.auth.PrincipalDetail;
import com.cos.blog.dto.ResponseDto;
import com.cos.blog.model.Board;
import com.cos.blog.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BoardApiController {

    private final BoardService boardService;


    @PostMapping("/api/board")
    public ResponseDto<Integer> save(
            @RequestBody Board board,
            @AuthenticationPrincipal PrincipalDetail principal){

        boardService.글쓰기(board,principal.getUser());
        return new ResponseDto<Integer>(HttpStatus.OK.value(),1); //자바오브젝트가 JSON으로 변환해서 리턴(JackSON)
    }


    @DeleteMapping("/api/board/{id}")
    public ResponseDto<Integer> deleteById(@PathVariable int id){
        boardService.글삭제하기(id);
        return new ResponseDto<Integer>(HttpStatus.OK.value(),1); //자바오브젝트가 JSON으로 변환해서 리턴(JackSON)
    }

    @PutMapping("/api/board/{id}")
    public ResponseDto<Integer> update(@PathVariable int id, @RequestBody Board board){
        log.info("id = {}",id);
        log.info("title = {}",board.getTitle());
        log.info("content = {}",board.getContent());
        boardService.글수정하기(id, board);
        return new ResponseDto<Integer>(HttpStatus.OK.value(),1); //자바오브젝트가 JSON으로 변환해서 리턴(JackSON)
    }



}