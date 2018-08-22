package com.stiva.springbootelasticdemo.controller;


import com.stiva.springbootelasticdemo.model.Book;
import com.stiva.springbootelasticdemo.repository.BookDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/books")
public class BookController {

   @Autowired
    private BookDao bookDao;

    @GetMapping("/{id}")
    public Map<String,Object> getBookById(@PathVariable String id){
        return bookDao.getBookById(id);
    }

    @GetMapping
    public List<Book> getAll(@RequestParam("query") String text){
        try {
            return bookDao.getAll(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @PostMapping
    public Book insertBook(@RequestBody Book book){
        return bookDao.insertBook(book);
    }

    @PutMapping
    public Map<String,Object> updateBookId(@RequestBody Book book,@PathVariable String id){
        return bookDao.updateBookById(id,book);
    }

    @DeleteMapping("/{id}")
    public void deleteBookById(@PathVariable String id){
      bookDao.deleteBookById(id);
    }
}
