package com.edu.ulab.app.repository;

import com.edu.ulab.app.config.SystemJpaTest;
import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.entity.Person;
import com.vladmihalcea.sql.SQLStatementCountValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.stream.Collectors;

import static com.vladmihalcea.sql.SQLStatementCountValidator.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Тесты репозитория {@link BookRepository}.
 */
@SystemJpaTest
public class BookRepositoryTest {
    @Autowired
    BookRepository bookRepository;
    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        SQLStatementCountValidator.reset();
    }

    @DisplayName("Сохранить книгу и автора. Число select должно равняться 2")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void findAllBadges_thenAssertDmlCount() {
        //Given

        Person person = new Person();
        person.setAge(111);
        person.setTitle("reader");
        person.setFullName("Test Test");

        Person savedPerson = userRepository.save(person);

        Book book = new Book();
        book.setAuthor("Test Author");
        book.setTitle("test");
        book.setPageCount(1000);
        book.setPerson(savedPerson);

        //When
        Book result = bookRepository.save(book);

        //Then
        assertThat(result.getPageCount()).isEqualTo(1000);
        assertThat(result.getTitle()).isEqualTo("test");
        assertSelectCount(2);
        assertInsertCount(0);
        assertUpdateCount(0);
        assertDeleteCount(0);
    }

    // update
    @DisplayName("Обновить книгу и автора. Число select должно равняться 3")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void updateBook_thenAssertDmlCount() {
        //Given
        Long userId = 1001L;
        Person person = new Person();
        person.setId(userId);

        Book book = new Book();
        book.setAuthor("Test Author");
        book.setTitle("test");
        book.setPageCount(1000);
        book.setPerson(person);

        //When
        List<Book> result = bookRepository.findAll().stream()
                .filter(b -> b.getPerson().getId().equals(userId))
                .collect(Collectors.toList());
        result.add(book);
        bookRepository.saveAll(result);

        //Then
        assertThat(result.size()).isEqualTo(3);
        assertSelectCount(3);
        assertInsertCount(0);
        assertUpdateCount(0);
        assertDeleteCount(0);
    }


    // get
    @DisplayName("Найти список книг. Число select должно равняться 2")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void getBooks_thenAssertDmlCount() {
        //Given
        Long userId = 1001L;

        //When
        List<Book> result = bookRepository.findAll().stream()
                .filter(book -> book.getPerson().getId().equals(userId))
                .collect(Collectors.toList());

        //Then
        assertThat(result.size()).isEqualTo(2);
        assertSelectCount(2);
        assertInsertCount(0);
        assertUpdateCount(0);
        assertDeleteCount(0);
    }


    // delete
    @DisplayName("Удалить список книг. Число select должно равняться 2")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void deleteBooks_thenAssertDmlCount() {
        //Given
        Long userId = 1001L;

        //When
        List<Long> booksForDeleting = bookRepository.findAll().stream()
                .filter(book -> book.getPerson().getId().equals(userId))
                .map(Book::getId)
                .collect(Collectors.toList());
        bookRepository.deleteAllById(booksForDeleting);

        //Then
        assertThat(booksForDeleting.size()).isEqualTo(2);
        assertSelectCount(2);
        assertInsertCount(0);
        assertUpdateCount(0);
        assertDeleteCount(0);
    }


    // * failed


    // example failed test
}
