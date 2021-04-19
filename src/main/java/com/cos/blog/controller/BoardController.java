package com.cos.blog.controller;


import com.cos.blog.model.Board;
import com.cos.blog.service.BoardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Slf4j
@Controller
public class BoardController {


    @Autowired
    private BoardService boardService;

    @GetMapping({"","/"})
    public String index(Model model,
                        @PageableDefault(size=3,sort = "id",direction = Sort.Direction.DESC)
                                Pageable pageable){ //컨트로로로에서 세션을 어떻게 찾는지?
        // /WEB-INF/views/joinForm.jsp
        List<Board> content = boardService.글목록(pageable).getContent();

        model.addAttribute("boards",boardService.글목록(pageable));

        return "index";
    }

    @GetMapping("/board/{id}")
    public String findById(@PathVariable int id,Model model){

        model.addAttribute("board",boardService.글상세보기(id));
        return "board/detail";

    }

    @GetMapping("/board/{id}/updateForm")
    public String updateForm(@PathVariable int id,Model model){
        model.addAttribute("board",boardService.글상세보기(id));
        return "board/updateForm";
    }

    //USER 권한이 필요
    @GetMapping("/board/saveForm")
    public String saveForm(){
        return "board/saveForm";
    }


    //세션에 접근하는 방법
    // 하지만 /페이지는 로그인이 필요없어 생략
//    @GetMapping({"","/"})
//    public String index(@AuthenticationPrincipal PrincipalDetail principal){ //컨트로로로에서 세션을 어떻게 찾는지?
//        // /WEB-INF/views/joinForm.jsp
//        log.info("로그인 사용자 아이디 :{}" , principal.getUsername());
//        return "index";
//    }
}