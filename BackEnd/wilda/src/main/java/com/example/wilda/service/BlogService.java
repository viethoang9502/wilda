package com.example.wilda.service;

import java.util.List;

import com.example.wilda.entity.Blog;
import com.example.wilda.model.request.CreateBlogRequest;

public interface BlogService {
    
    List<Blog> getList();

    List<Blog> getListNewest(int limit);

    Blog getBlog(long id);

    Blog createBlog(CreateBlogRequest request);

    Blog updateBlog(long id,CreateBlogRequest request);

    void deleteBlog(long id);

}
