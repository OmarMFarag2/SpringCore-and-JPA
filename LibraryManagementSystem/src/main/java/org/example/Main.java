package org.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.example.entity.Author;
import org.example.entity.Book;
import org.example.entity.Category;
import org.example.entity.Publisher;
import org.example.query.LibraryServices;

public class Main {
    static void main() {
        EntityManagerFactory emf =
                Persistence.createEntityManagerFactory("libraryPU");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        Author author = new Author("Robert C. Martin");
        Publisher publisher = new Publisher("Pearson");
        Category category = new Category("Programming");

        Book book = new Book("Clean Code");

        author.addBook(book);
        book.setPublisher(publisher);
        book.addCategory(category);

        em.persist(publisher);
        em.persist(category);
        em.persist(author);
        tx.commit();
        System.out.println("Data inserted.");
    //******************part 2************************

        populateData(em);

        LibraryServices.findBooksByAuthorName(em, "Robert C. Martin");
        LibraryServices.findBooksByPublisher(em, "Pearson");
        LibraryServices.findBookById(em, 1L);
        LibraryServices.findAuthorsWithBooks(em);
        LibraryServices.countBooksByAuthor(em);

        LibraryServices.findBooksByTitle(em, "Clean Code");

        LibraryServices.findBooksDynamic(
                em,
                "Clean Code",
                "Robert C. Martin"
        );

        em.close();
        emf.close();
        }
    public static void populateData(EntityManager em) {

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        Author martin = new Author("Robert C. Martin");
        Author bloch = new Author("Joshua Bloch");
        Author horstmann = new Author("Cay S. Horstmann");

        Publisher pearson = new Publisher("Pearson");
        Publisher oreilly = new Publisher("O'Reilly Media");
        Publisher addisonWesley = new Publisher("Addison-Wesley");

        Category programming = new Category("Programming");
        Category java = new Category("Java");
        Category softwareEngineering = new Category("Software Engineering");

        Book cleanCode = new Book("Clean Code");
        Book cleanArchitecture = new Book("Clean Architecture");
        Book effectiveJava = new Book("Effective Java");
        Book coreJava = new Book("Core Java");

        martin.addBook(cleanCode);
        martin.addBook(cleanArchitecture);

        bloch.addBook(effectiveJava);

        horstmann.addBook(coreJava);

        cleanCode.setPublisher(pearson);
        cleanArchitecture.setPublisher(pearson);
        effectiveJava.setPublisher(addisonWesley);
        coreJava.setPublisher(oreilly);

        cleanCode.addCategory(programming);
        cleanCode.addCategory(softwareEngineering);

        cleanArchitecture.addCategory(programming);
        cleanArchitecture.addCategory(softwareEngineering);

        effectiveJava.addCategory(java);
        effectiveJava.addCategory(programming);

        coreJava.addCategory(java);
        coreJava.addCategory(programming);

        em.persist(pearson);
        em.persist(oreilly);
        em.persist(addisonWesley);

        em.persist(programming);
        em.persist(java);
        em.persist(softwareEngineering);

        em.persist(martin);
        em.persist(bloch);
        em.persist(horstmann);

        tx.commit();

        System.out.println("Sample data inserted.");
    }
    }
