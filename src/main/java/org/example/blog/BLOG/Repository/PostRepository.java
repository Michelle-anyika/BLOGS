package org.example.blog.BLOG.Repository;


import org.example.blog.BLOG.Model.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PostRepository extends MongoRepository<Post, String> {

    List<Post> findByUserId(String userId);
}

