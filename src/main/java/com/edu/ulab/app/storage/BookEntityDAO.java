package com.edu.ulab.app.storage;

import com.edu.ulab.app.entity.Book;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class BookEntityDAO implements DAO<Book>{

    /* связь many-to-one реализуется, используя Map<Long, List<Book>>,
    где
        key: идентификатор Long userId (индекс читателя, взявшего книгу)
        value: значение в формате List<Book> (набор книг, числящихся за читателем)*/

    private static Map<Long, List<Book>> books = new HashMap<Long,List<Book>>();
    private static Long index = 0L;
    private UserEntityDAO userEntityDAO;

    public BookEntityDAO(UserEntityDAO userEntityDAO) {
        this.userEntityDAO = userEntityDAO;
    }

    //получить список всех книг определенного читателя
    public List<Book> getUserBooks(long userId) {
        return books.get(userId);
    }

    //список всех книг читателей
    @Override
    public List<Book> getAll() {
        List<Book> result = new ArrayList<>();
        for(List<Book> list : books.values()) {
            result.addAll(list);
        }
        return result;
    }

    //сохранение новой книги
    @Override
    public Book save(Book bookEntity) {
        List<Book> result = getAll();
        Long userId = bookEntity.getPerson().getId();
        if (!result.contains(bookEntity)) {
            index++;
            bookEntity.setId(index);
            List<Book> booksForUser;
            if (getUserBooks(userId)==null) {
                booksForUser= new ArrayList<>();
            } else {
                booksForUser = getUserBooks(userId);
            }
            booksForUser.add(bookEntity);
            books.put(userId,booksForUser);
            log.info("Created bookEntity: {}", bookEntity);
        } else {
            List<Book> booksForUser = books.get(userId);
            Long id = booksForUser.get(booksForUser.indexOf(bookEntity)).getId();
            bookEntity.setId(id);
            log.info("Couldn't create bookEntity (storage already have this bookEntity) : {}", bookEntity);
        }
        return bookEntity;
    }

    //обновление данных книги
    @Override
    public Book update(Book bookEntity, Long userId) {
        Book book = null;
        //book.setUserId(userId);
        if (!books.get(userId).contains(bookEntity)) {
            book = save(bookEntity);
        }  else {
            book = books.get(userId).stream()
                    .filter(bEntity -> bEntity.equals(bookEntity)).findFirst().get();
            book.setPageCount(bookEntity.getPageCount());
            book.setTitle(bookEntity.getTitle());
            book.setAuthor(book.getAuthor());
        }
        return book;
    }

    //удаление книг из списка читателя
    public void delete(Long userId) {
        if (books.containsKey(userId)) {
            books.remove(userId);
        } else
            log.info("Couldn't delete books (storage doesn't have this userId) : {}", userId);
    }
}
