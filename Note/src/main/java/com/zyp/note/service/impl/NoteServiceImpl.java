package com.zyp.note.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zyp.note.common.base.R;
import com.zyp.note.common.base.ResultCode;
import com.zyp.note.common.exception.SystemException;
import com.zyp.note.mapper.NoteMapper;
import com.zyp.note.pojo.domain.BaseEntity;
import com.zyp.note.pojo.domain.NoteInfo;
import com.zyp.note.service.INoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteServiceImpl implements INoteService {

    @Autowired
    private NoteMapper noteMapper;

    @Override
    public R<List<NoteInfo>> getList() {
        List<NoteInfo> noteInfoList = noteMapper.selectList(new LambdaQueryWrapper<NoteInfo>()
                .eq(BaseEntity::getDeleteFlag, 0)
                .orderByDesc(NoteInfo::getUpdateTime));
        return R.ok(noteInfoList);
    }

    @Override
    public R<Boolean> addNote(NoteInfo noteInfo) {
        int insert = noteMapper.insert(noteInfo);
        if(insert==1){
            return R.ok();
        }
        return R.fail();
    }

    @Override
    public R<Boolean> updateNote(NoteInfo noteInfo) {
        NoteInfo noteInfo1 = noteMapper.selectOne(new LambdaQueryWrapper<NoteInfo>()
                .eq(NoteInfo::getId, noteInfo.getId())
                .eq(NoteInfo::getDeleteFlag, 0));
        if(noteInfo1==null) throw new SystemException(ResultCode.FAILED);
        int row = noteMapper.updateById(noteInfo);
        if(row==1) return R.ok();
        return R.fail();
    }

    @Override
    public R<Boolean> deleteNote(Long noteId) {
        NoteInfo noteInfo = noteMapper.selectOne(new LambdaQueryWrapper<NoteInfo>()
                .eq(NoteInfo::getId, noteId)
                .eq(NoteInfo::getDeleteFlag, 0));
        if(noteInfo==null) throw new SystemException(ResultCode.FAILED);
        noteInfo.setDeleteFlag(1);
        int row = noteMapper.updateById(noteInfo);
        if(row==1) return R.ok();
        return R.fail();
    }
}
