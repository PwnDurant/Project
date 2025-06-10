package com.zyp.note.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zyp.note.pojo.domain.NoteInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NoteMapper extends BaseMapper<NoteInfo> {
}
