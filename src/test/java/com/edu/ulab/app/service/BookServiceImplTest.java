package com.edu.ulab.app.service;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.entity.Person;
import com.edu.ulab.app.mapper.BookMapper;
import com.edu.ulab.app.repository.BookRepository;
import com.edu.ulab.app.service.impl.BookServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Тестирование функционала {@link com.edu.ulab.app.service.impl.BookServiceImpl}.
 */
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@DisplayName("Testing book functionality.")
public class BookServiceImplTest {
    @InjectMocks
    BookServiceImpl bookService;

    @Mock
    BookRepository bookRepository;

    @Mock
    BookMapper bookMapper;

    @Test
    @DisplayName("Создание книги. Должно пройти успешно.")
    void saveBook_Test() {
        //given
        Person person  = new Person();
        person.setId(1L);

        BookDto bookDto = new BookDto();
        bookDto.setUserId(1L);
        bookDto.setAuthor("test author");
        bookDto.setTitle("test title");
        bookDto.setPageCount(1000);

        BookDto result = new BookDto();
        result.setId(1L);
        result.setUserId(1L);
        result.setAuthor("test author");
        result.setTitle("test title");
        result.setPageCount(1000);

        Book book = new Book();
        book.setPageCount(1000);
        book.setTitle("test title");
        book.setAuthor("test author");
        book.setPerson(person);

        Book savedBook = new Book();
        savedBook.setId(1L);
        savedBook.setPageCount(1000);
        savedBook.setTitle("test title");
        savedBook.setAuthor("test author");
        savedBook.setPerson(person);

        //when

        when(bookMapper.bookDtoToBook(bookDto)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(savedBook);
        when(bookMapper.bookToBookDto(savedBook)).thenReturn(result);


        //then
        BookDto bookDtoResult = bookService.createBook(bookDto);
        assertEquals(1L, bookDtoResult.getId());
    }


    // update
    @Test
    @DisplayName("Обновление книги в списке книг читателя. Должно пройти успешно.")
    void updateBook_Test() {
        //given
        Long userId = 1L;
        Person person  = new Person();
        person.setId(userId);

        BookDto bookDto = new BookDto();
        bookDto.setUserId(userId);
        bookDto.setAuthor("test author");
        bookDto.setTitle("test title");
        bookDto.setPageCount(1000);

        BookDto updatedBookDto = new BookDto();
        updatedBookDto.setId(1L);
        updatedBookDto.setUserId(userId);
        updatedBookDto.setAuthor("test author");
        updatedBookDto.setTitle("test title");
        updatedBookDto.setPageCount(1000);

        Book book = new Book();
        book.setPageCount(1000);
        book.setTitle("test title");
        book.setAuthor("test author");
        book.setPerson(person);

        Book savedBook = new Book();
        savedBook.setId(1L);
        savedBook.setPageCount(1000);
        savedBook.setTitle("test title");
        savedBook.setAuthor("test author");
        savedBook.setPerson(person);

        //when
        when(bookMapper.bookDtoToBook(bookDto)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(savedBook);
        when(bookMapper.bookToBookDto(savedBook)).thenReturn(updatedBookDto);

        //then
        BookDto bookDtoResult = bookService.updateBook(bookDto,userId);
        assertEquals(1L, bookDtoResult.getId());
    }

    // get
    @Test
    @DisplayName("Поиск книг читателя. Должно пройти успешно.")
    void getBooks_Test() {
        //given
        Long userId = 1L;

        Person person  = new Person();
        person.setId(userId);

        BookDto bookDto1 = new BookDto();
        bookDto1.setUserId(userId);
        bookDto1.setAuthor("test author1");
        bookDto1.setTitle("test title1");
        bookDto1.setPageCount(1001);

        BookDto bookDto2 = new BookDto();
        bookDto1.setUserId(userId);
        bookDto1.setAuthor("test author2");
        bookDto1.setTitle("test title2");
        bookDto1.setPageCount(1002);

        List<Book> books = new ArrayList<>();

        Book book1 = new Book();
        book1.setPageCount(1001);
        book1.setTitle("test title1");
        book1.setAuthor("test author1");
        book1.setPerson(person);

        Book book2 = new Book();
        book2.setPageCount(1002);
        book2.setTitle("test title2");
        book2.setAuthor("test author2");
        book2.setPerson(person);

        books.add(book1);
        books.add(book2);


        //when
        when(bookRepository.findAll().stream()
                .filter(book -> book.getPerson().getId().equals(userId))
                .collect(Collectors.toList()))
                .thenReturn(books);
        when(bookMapper.bookToBookDto(book1)).thenReturn(bookDto1);
        when(bookMapper.bookToBookDto(book2)).thenReturn(bookDto2);

        //then
        List<BookDto> result = bookService.getBooksByUserId(userId);
        assertEquals(2,result.size());
    }

    // delete
    @Test
    @DisplayName("Удаление книг читателя. Должно пройти успешно.")
    void deleteBooks_Test() {
        //given
        Long userId = 1L;

        Person person  = new Person();
        person.setId(userId);

        List<Long> bookIds = new ArrayList<>();
        List<Book> books = new ArrayList<>();

        Book book1 = new Book();
        book1.setPageCount(1001);
        book1.setTitle("test title1");
        book1.setAuthor("test author1");
        book1.setPerson(person);
        book1.setId(1L);

        Book book2 = new Book();
        book2.setPageCount(1002);
        book2.setTitle("test title2");
        book2.setAuthor("test author2");
        book2.setPerson(person);
        book2.setId(2L);

        bookIds.add(book1.getId());
        bookIds.add(book2.getId());

        books.add(book1);
        books.add(book2);

        //when
        when(bookRepository.findAll().stream()
                .filter(book -> book.getPerson().getId().equals(userId))
                .collect(Collectors.toList()))
                .thenReturn(books);

        //then
        assertEquals(true, bookService.deleteBooksByUserId(userId));
    }
    // * failed
}
