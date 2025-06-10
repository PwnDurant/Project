package com.zyp.note.service;

import com.zyp.note.common.base.R;
import com.zyp.note.pojo.domain.NoteInfo;

import java.util.List;

public interface INoteService {
    R<List<NoteInfo>> getList();

    R<Boolean> addNote(NoteInfo noteInfo);

    R<Boolean> updateNote(NoteInfo noteInfo);

    R<Boolean> deleteNote(Long noteId);
}
