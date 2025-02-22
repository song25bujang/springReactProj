package com.nc13.react_board.controller;

import com.nc13.react_board.model.BoardDTO;
import com.nc13.react_board.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/board/")
public class BoardController {
    private BoardService BOARD_SERVICE;

    @Autowired
    public BoardController(BoardService boardService){
        BOARD_SERVICE = boardService;
    }

    @GetMapping("showOne/{id}")
    public BoardDTO selectOne(@PathVariable int id){
        BoardDTO b= BOARD_SERVICE.selectOne(id);
//        System.out.println(b.toString());
        return b;
    }
    @GetMapping("showList/{pageNo}")
    public HashMap<String, Object> selectList(@PathVariable int pageNo) {
        HashMap<String, Object> resultMap = new HashMap<>();
        int maxPage = BOARD_SERVICE.selectMaxPage();
        int startPage = 1;
        int endPage = 1;

        if(maxPage < 5){
            endPage = maxPage;
        }
        else if(pageNo <= 3){
            endPage = 5;
        } else if(pageNo >= maxPage - 2){
            startPage = maxPage - 4;
            endPage = maxPage;
        } else{
            startPage = pageNo - 2;
            endPage = pageNo + 2;
        }
        resultMap.put("currentPage", pageNo);
        resultMap.put("startPage", startPage);
        resultMap.put("endPage", endPage);
        resultMap.put("maxPage", maxPage);
        resultMap.put("boardList", BOARD_SERVICE.selectAll(pageNo));
        return resultMap;
    }
    @PostMapping("write") //@RequestBody?
    public HashMap<String,Object> write(@RequestBody BoardDTO boardDTO){
        boardDTO.setWriterId(2L);
        System.out.println(boardDTO.toString());
        HashMap<String, Object> resultMap = new HashMap<>();
        try{
            BOARD_SERVICE.insert(boardDTO);
            resultMap.put("result","success");
            resultMap.put("resultId",boardDTO.getId());
        }catch (Exception e){
            e.printStackTrace();
            resultMap.put("result","fail");
        }
        return resultMap;
    }
    @PostMapping("update/{id}")
    public HashMap<String,Object> update(@PathVariable int id, @RequestBody BoardDTO boardDTO){
        HashMap<String,Object> resultMap = new HashMap<>();
        System.out.println("수정할 boardDTO: "+boardDTO.toString());
        BOARD_SERVICE.update(boardDTO);
        resultMap.put("destId",boardDTO.getId());
        return resultMap;
    }
}
