package ru.netology.repository;

import ru.netology.model.Post;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

// Stub
public class PostRepository {
    private final AtomicLong postID;
    private final Map<Long, Post> postMap;

    public PostRepository() {
        postID = new AtomicLong(1);
        postMap = new ConcurrentHashMap<>();
    }

    public List<Post> all() {
        return new ArrayList<>(postMap.values());
    }

    public Optional<Post> getById(long id) {
        return Optional.ofNullable(postMap.get(id));
    }

    public Post save(Post post) {
        long id = post.getId();
        if (id > 0 && postMap.containsKey(id)) {
            postMap.put(id, post);
        } else {
            id = postID.getAndIncrement();
            post.setId(id);
            postMap.put(id, post);
        }
        return post;
    }

    public void removeById(long id) {
        postMap.remove(id);
    }
}
