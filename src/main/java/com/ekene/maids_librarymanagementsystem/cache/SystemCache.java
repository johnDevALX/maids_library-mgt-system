package com.ekene.maids_librarymanagementsystem.cache;

import com.ekene.maids_librarymanagementsystem.book.model.Book;
import com.ekene.maids_librarymanagementsystem.patron.model.Patron;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.redisson.api.EvictionMode;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class SystemCache {

    private final RedissonClient redissonClient;

    @SneakyThrows
    public void addBook(Book book) {
        if (Objects.isNull(book)) {
            return;
        }
        getBookMap().setMaxSize(2000, EvictionMode.LRU);
        getBookMap().put(book.getId(), book, 14, TimeUnit.DAYS);
    }

    @SneakyThrows
    public void addPatron(Patron patron) {
        if (Objects.isNull(patron)) {
            return;
        }
        getPatronMap().setMaxSize(2000, EvictionMode.LRU);
        getPatronMap().put(patron.getId(), patron, 14, TimeUnit.DAYS);
    }

    @SneakyThrows
    public Book getBook(Long id) {
        return getBookMap().get(id);
    }
    @SneakyThrows
    public void deleteBook(Long id) {
        getBookMap().remove(id);
    }
    private RMapCache<Long, Book> getBookMap() {
        var bookCache = String.join(".", "maids.cc.library.mgt.system.recent.books");
        return this.redissonClient.getMapCache(bookCache);
    }

    @SneakyThrows
    public Patron getPatron(Long id) {
        return getPatronMap().get(id);
    }

    @SneakyThrows
    public void deletePatron(Long id) {
        getPatronMap().remove(id);
    }
    private RMapCache<Long, Patron> getPatronMap() {
        var patronCache = String.join(".", "maids.cc.library.mgt.system.recent.patron");
        return this.redissonClient.getMapCache(patronCache);
    }
}
