package com.example.wilda.service;

import java.util.List;

import com.example.wilda.entity.Tag;
import com.example.wilda.model.request.CreateTagRequest;

public interface TagService {
    
    List<Tag> getListTag();

    Tag createTag(CreateTagRequest request);

    Tag updateTag(long id,CreateTagRequest request);

    void enableTag(long id);

    void deleleTag(long id);

}
