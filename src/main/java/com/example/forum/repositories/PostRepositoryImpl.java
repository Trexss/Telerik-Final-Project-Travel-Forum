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
    public List<Post> getAllPosts() {
        try (Session session = sessionFactory.openSession()) {
            Query<Post> query = session.createQuery("from Post", Post.class);
            return query.list();
        }
    }
    @Override
    public List<Post> getAllPostsSorted(String sortBy, String order) {
        try (Session session = sessionFactory.openSession()) {

            // Validate fields to avoid HQL injection
            if (!sortBy.equals("createdAt") && !sortBy.equals("likes")) {
                sortBy = "createdAt";
            }
            if (!order.equalsIgnoreCase("asc") && !order.equalsIgnoreCase("desc")) {
                order = "desc";
            }

            String hql = "from Post p order by p." + sortBy + " " + order;

            Query<Post> query = session.createQuery(hql, Post.class);

            return query.list();
        }
    }

    @Override
    public Post getPostById(int id) {
        try (Session session = sessionFactory.openSession()) {
            Post post = session.get(Post.class, id);
            if (post == null) {
                throw new EntityNotFoundException("Post", id);
            }
            return post;
        }
    }

    @Override
    public Post getPostByIdWithComments(int id) {
        try (Session session = sessionFactory.openSession()) {
            Query<Post> query = session.createQuery(
                    "SELECT p FROM Post p LEFT JOIN FETCH p.comments WHERE p.id = :id",
                    Post.class
            );
            query.setParameter("id", id);
            Post post = query.uniqueResult();
            if (post == null) {
                throw new EntityNotFoundException("Post", id);
            }
            return post;
        }
    }

    @Override
    public void createPost(Post post) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(post);
            session.getTransaction().commit();
        }
    }

    @Override
    public void updatePost(Post post) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(post);
            session.getTransaction().commit();
        }
    }

    @Override
    public void deletePostById(int id) {
        Post postToDelete = getPostById(id);
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(postToDelete);
            session.getTransaction().commit();
        }
    }
}
