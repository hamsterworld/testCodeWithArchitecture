package com.example.demo.post.controller.port;

import com.example.demo.post.domain.Post;
import com.example.demo.post.domain.PostCreate;
import com.example.demo.post.domain.PostUpdate;

public interface PostService {
    public Post getPostById(long id);

    public Post create(PostCreate postCreate);

    public Post update(long id, PostUpdate postUpdate);
}
