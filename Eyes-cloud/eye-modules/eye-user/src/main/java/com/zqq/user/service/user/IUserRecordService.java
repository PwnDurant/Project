package com.zqq.user.service.user;

import com.zqq.common.core.domain.PageQueryDTO;
import com.zqq.common.core.domain.TableDataInfo;
import com.zqq.user.domain.record.vo.RecordVO;

import java.util.List;

public interface IUserRecordService {

    List<RecordVO> list(PageQueryDTO dto);

    TableDataInfo redisList(PageQueryDTO dto);
}
