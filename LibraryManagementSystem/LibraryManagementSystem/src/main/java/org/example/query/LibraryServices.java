package org.example.query;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.example.entity.*;

import java.util.ArrayList;
import java.util.List;

public class LibraryServices {


    public static void findBooksByAuthorName(EntityManager em, String authorName) {
        List<Book> books = em.createQuery(
                        "SELECT b FROM Book b WHERE b.author.name = :name",
                        Book.class
                ).setParameter("name", authorName)
                .getResultList();

        System.out.println("Books by " + authorName + ":");

        for (Book book : books) {
            System.out.println(book.getTitle());
        }
    }

    public static void findBooksByPublisher(EntityManager em, String publisherName) {
        List<Book> books = em.createQuery(
                        "SELECT b FROM Book b WHERE b.publisher.name = :name",
                        Book.class
                ).setParameter("name", publisherName)
                .getResultList();

        System.out.println("Books by " + publisherName + ":");

        for (Book book : books) {
            System.out.println(book.getTitle());
        }
    }

    public static void findBookById(EntityManager em, Long id) {
        Book book = em.createQuery(
                        "SELECT b FROM Book b WHERE b.id = ?1",
                        Book.class
                ).setParameter(1, id)
                .getSingleResult();

        System.out.println("Book with ID " + id + ":");
        System.out.println(book.getTitle());
    }

    public static void findAuthorsWithBooks(EntityManager em) {
        List<Author> authors = em.createQuery(
                "SELECT DISTINCT a FROM Author a JOIN FETCH a.books",
                Author.class
        ).getResultList();

        for (Author author : authors) {
            System.out.println(author.getName());

            for (Book book : author.getBooks()) {
                System.out.println("  " + book.getTitle());
            }
        }
    }
    public static void countBooksByAuthor(EntityManager em) {
        List<Object[]> results = em.createQuery(
                "SELECT a.name, COUNT(b) " +
                        "FROM Author a LEFT JOIN a.books b " +
                        "GROUP BY a.name",
                Object[].class
        ).getResultList();

        for (Object[] row : results) {
            System.out.println(row[0] + " -> " + row[1] + " books");
        }
    }
    public static void findBooksByTitle(EntityManager em, String title) {

        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<Book> cq = cb.createQuery(Book.class);

        Root<Book> book = cq.from(Book.class);

        cq.select(book)
                .where(cb.equal(book.get("title"), title));

        List<Book> books = em.createQuery(cq).getResultList();

        System.out.println("Books with title: " + title);

        for (Book b : books) {
            System.out.println(b.getTitle());
        }
    }

    public static void findBooksDynamic(
            EntityManager em,
            String title,
            String authorName) {

        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<Book> cq = cb.createQuery(Book.class);

        Root<Book> book = cq.from(Book.class);

        List<Predicate> predicates = new ArrayList<>();

        if (title != null && !title.isBlank()) {
            predicates.add(
                    cb.equal(book.get("title"), title)
            );
        }

        if (authorName != null && !authorName.isBlank()) {
            predicates.add(
                    cb.equal(book.get("author").get("name"), authorName)
            );
        }

        cq.select(book);

        if (!predicates.isEmpty()) {
            cq.where(cb.and(predicates.toArray(new Predicate[0])));
        }

        List<Book> books = em.createQuery(cq).getResultList();

        System.out.println("Dynamic search:");

        for (Book b : books) {
            System.out.println(
                    b.getTitle() + " - " + b.getAuthor().getName()
            );
        }
    }
}
