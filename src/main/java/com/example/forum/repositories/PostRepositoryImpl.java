package com.example.forum.repositories;

import com.example.forum.exceptions.EntityNotFoundException;
import com.example.forum.models.Post;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PostRepositoryImpl implements PostRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public PostRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Post> get() {
        try (Session session = sessionFactory.openSession()) {
            Query<Post> query = session.createQuery("from Post", Post.class);
            return query.list();
        }
    }

    @Override
    public Post get(int id) {
        try (Session session = sessionFactory.openSession()) {
            Post post = session.get(Post.class, id);
            if (post == null) {
                throw new EntityNotFoundException("Post", id);
            }
            return post;
        }
    }

    @Override
    public void create(Post post) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(post);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(Post post) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(post);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(int id) {
        Post postToDelete = get(id);
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(postToDelete);
            session.getTransaction().commit();
        }
    }
}
