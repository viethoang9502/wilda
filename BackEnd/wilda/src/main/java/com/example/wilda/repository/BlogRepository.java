package com.example.wilda.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.wilda.entity.Blog;

@Repository
public interface BlogRepository extends JpaRepository<Blog,Long> {
    
    @Query(value = "Select * from Blog order by id desc limit :limit",nativeQuery = true)
    List<Blog> getListNewest(int limit);

}
