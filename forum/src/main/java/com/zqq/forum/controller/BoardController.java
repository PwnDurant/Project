package com.zqq.forum.controller;


import com.zqq.forum.common.AppResult;
import com.zqq.forum.common.ResultCode;
import com.zqq.forum.exception.ApplicationException;
import com.zqq.forum.model.Board;
import com.zqq.forum.service.IBoardService;
import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/board")
public class BoardController {

//    从配置文件中读取值，如果没有配置，默认为9
    @Value("${zqq-forum.index.board-num:9}")
    private Integer num;

    @Autowired
    private IBoardService iBoardService;

    /**
     * 查询首页板块列表
     * @return
     */
    @GetMapping("/topList")
    public AppResult<List<Board>> topList(){
        log.info("num:{}",num);
        List<Board> boards=iBoardService.selectByNum(num);

        if(boards==null){
            boards=new ArrayList<>();
        }

        return AppResult.success(boards);
    }

    @GetMapping("/getById")
    public AppResult<Board> getById(@RequestParam("id") @Nonnull Long id){

        Board board = iBoardService.selectById(id);
        if(board==null||board.getDeleteState()==1){
            log.warn(ResultCode.FAILED_BOARD_NOT_EXISTS.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_BOARD_NOT_EXISTS));
        }

        return AppResult.success(board);

    }

}
