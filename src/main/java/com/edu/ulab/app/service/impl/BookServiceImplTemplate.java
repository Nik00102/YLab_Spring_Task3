package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BookServiceImplTemplate implements BookService {

    private final JdbcTemplate jdbcTemplate;

    public BookServiceImplTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public BookDto createBook(BookDto bookDto) {
        final String INSERT_SQL = "INSERT INTO BOOK(TITLE, AUTHOR, PAGE_COUNT, USER_ID) VALUES (?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                new PreparedStatementCreator() {
                    public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                        PreparedStatement ps =
                                connection.prepareStatement(INSERT_SQL, new String[]{"id"});
                        ps.setString(1, bookDto.getTitle());
                        ps.setString(2, bookDto.getAuthor());
                        ps.setLong(3, bookDto.getPageCount());
                        ps.setLong(4, bookDto.getUserId());
                        return ps;
                    }
                },
                keyHolder);

        bookDto.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return bookDto;
    }

    @Override
    public BookDto updateBook(BookDto bookDto, Long userId) {
        // реализовать недстающие методы
        BookDto updatedBook = createBook(bookDto);
        getBooksByUserId(userId).add(updatedBook);
        log.info("Updated book: {}", updatedBook);
        return updatedBook;
    }

    @Override
    public List<BookDto> getBooksByUserId(Long userId) {
        // реализовать недстающие методы
        final String GET_ALL_BOOKS_SQL = "SELECT * FROM BOOK";
        List<BookDto> allBooks = jdbcTemplate.query(GET_ALL_BOOKS_SQL, new BeanPropertyRowMapper<BookDto>(BookDto.class));
        return allBooks.stream().filter(bookDto -> bookDto.getUserId().equals(userId)).collect(Collectors.toList());
    }

    @Override
    public boolean deleteBooksByUserId(Long userId) {
        // реализовать недстающие методы
        final String DELETE_BOOK_BY_ID = "DELETE FROM BOOK WHERE id = ?";

        final String GET_ALL_BOOKS_SQL = "SELECT * FROM BOOK";
        List<BookDto> allBooks = jdbcTemplate.query(GET_ALL_BOOKS_SQL, new BeanPropertyRowMapper<BookDto>(BookDto.class));

        List<Long> booksIdForDeleting = allBooks.stream()
                .filter(bookDto -> bookDto.getUserId().equals(userId))
                .map(BookDto::getId)
                .collect(Collectors.toList());
        log.info("BooksID which will be deleted: {}", booksIdForDeleting);

        for (Long id: booksIdForDeleting) {
            jdbcTemplate.update(DELETE_BOOK_BY_ID, id);
        }
        return getBooksByUserId(userId)!=null;
    }
}
