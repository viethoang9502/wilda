package com.example.wilda.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.wilda.entity.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag,Long> {
    
}
