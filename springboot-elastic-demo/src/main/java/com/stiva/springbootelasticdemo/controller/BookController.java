package com.stiva.springbootelasticdemo.controller;


import com.stiva.springbootelasticdemo.model.Book;
import com.stiva.springbootelasticdemo.repository.BookDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*",allowedHeaders = "*")
public class BookController {

   @Autowired
    private BookDao bookDao;

    @GetMapping("/books/{id}")
    public Map<String,Object> getBookById(@PathVariable String id){
        return bookDao.getBookById(id);
    }

    @GetMapping("/Hello")
    public String helloWorld(){
        return "Hello Ajinkya!!";
    }

    @GetMapping("/books")
    public List<Book> getAll(@RequestParam("query") String text){
        try {
            return bookDao.getAll(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @PostMapping("/books/create")
    public Book insertBook(@RequestBody Book book){
        return bookDao.insertBook(book);
    }

    @PutMapping(value="/books/update/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public Map<String,Object> updateBookId(@RequestBody Book book,@PathVariable String id){
        return bookDao.updateBookById(id,book);
    }

    @DeleteMapping("/books/{id}")
    public void deleteBookById(@PathVariable String id){
      bookDao.deleteBookById(id);
    }

    @GetMapping("/books/getSearchCount")
    public long getSearchCount(String txt){
        try {
            return bookDao.getCount(txt);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @GetMapping("/books/getAllRecords")
    public List<Book> getAllRecords(){
        try {
            return bookDao.getAllRecords();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

