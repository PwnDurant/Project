package com.zqq.user.controller.user;

import com.zqq.common.core.controller.BaseController;
import com.zqq.common.core.domain.PageQueryDTO;
import com.zqq.common.core.domain.R;
import com.zqq.common.core.domain.TableDataInfo;
import com.zqq.user.service.user.IUserRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户上传历史记录
 */
@RestController
@RequestMapping("/user/message")
public class UserRecordController extends BaseController {

    @Autowired
    private IUserRecordService userRecordService;

    /**
     * 查询历史记录
     * @param dto 每页数据，第几页，结果，上传时间
     * @return 返回查询出来的结果
     */
    @GetMapping("/list")
    public R<TableDataInfo> list(PageQueryDTO dto){
        return R.ok(getTableDataInfo(userRecordService.list(dto)));
    }

    /**
     * 查询历史记录
     * @param dto 每页数据，第几页，结果，上传时间
     * @return 返回查询出来的结果
     */
    @GetMapping("/redis/list")
    public TableDataInfo redisList(PageQueryDTO dto){
        return userRecordService.redisList(dto);
    }

}