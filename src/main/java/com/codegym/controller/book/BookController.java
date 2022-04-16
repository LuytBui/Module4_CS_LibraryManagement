package com.codegym.controller.book;

import com.codegym.model.auth.ErrorMessage;
import com.codegym.model.book.Book;
import com.codegym.model.book.BookForm;
import com.codegym.service.book.IBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/books")
public class BookController {
    public final int PAGE_SIZE = 12;

    @Autowired
    private IBookService bookService;

    @Value("${file-upload}")
    private String uploadPath;

    @GetMapping
    public ResponseEntity<List<String>> findAllPublisher() {
        List<String> publisher = bookService.findAllPublisher();
        return new ResponseEntity<>(publisher, HttpStatus.OK);
    }

    @GetMapping("/page/{pageNumber}")
    public ResponseEntity<Page<Book>> showPage(@RequestParam(name = "q") Optional<String> q, @PathVariable int pageNumber){
        Pageable pageable = PageRequest.of(pageNumber, PAGE_SIZE);
        Page<Book> books = bookService.findAll(pageable);
        if (q.isPresent()) {
            books = bookService.findAllByNameContaining(q.get(), pageable);
        }
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> findById(@PathVariable Long id) {
        Optional<Book> book = bookService.findById(id);
        if (!book.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(book.get(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Book> saveProduct(@ModelAttribute BookForm bookForm) {
        MultipartFile img = bookForm.getImage();
        if (img.getSize() != 0) {
            String fileName = img.getOriginalFilename();
            long currentTime = System.currentTimeMillis();
            fileName = currentTime + "_" + fileName;
            try {
                FileCopyUtils.copy(img.getBytes(), new File(uploadPath + fileName));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Book book = new Book(bookForm.getId(), bookForm.getName(), bookForm.getCategory(), bookForm.getDescription(), fileName, bookForm.getStatus(), bookForm.getPublisher(), bookForm.getQuantity());
            return new ResponseEntity<>(bookService.save(book), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/{id}")
    public ResponseEntity<Book> updateProduct(@PathVariable Long id, @ModelAttribute BookForm bookForm) {
        Optional<Book> bookOptional = bookService.findById(id);
        MultipartFile img = bookForm.getImage();
        if (bookOptional.isPresent()) {
            Book oldBook = bookOptional.get();
            if (img.getSize() != 0) {
                String fileName = img.getOriginalFilename();
                long currentTime = System.currentTimeMillis();
                fileName = currentTime + fileName;
                oldBook.setImage(fileName);
                try {
                    FileCopyUtils.copy(img.getBytes(), new File(uploadPath + fileName));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            oldBook.setId(bookForm.getId());
            oldBook.setName(bookForm.getName());
            oldBook.setCategory(bookForm.getCategory());
            oldBook.setDescription(bookForm.getDescription());
            oldBook.setStatus(bookForm.getStatus());
            oldBook.setPublisher(bookForm.getPublisher());
            oldBook.setQuantity(bookForm.getQuantity());
            return new ResponseEntity<>(bookService.save(oldBook), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Book> deleteProduct(@PathVariable Long id) {
        Optional<Book> bookOptional = bookService.findById(id);
        if (!bookOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        bookService.deleteById(id);
        return new ResponseEntity<>(bookOptional.get(), HttpStatus.OK);
    }
    @GetMapping("/{publisher}/page/{pageNumber}")
    public ResponseEntity<Page<Book>> showPageByPublisher(@RequestParam(name = "q") Optional<String> q, @PathVariable Optional<String> publisher ,@PathVariable int pageNumber){
        Pageable pageable = PageRequest.of(pageNumber, PAGE_SIZE);
        Page<Book> books = bookService.findAllByPublisher(publisher.get(), pageable);
        if (q.isPresent()) {
            books = bookService.findAllByNameContaining(q.get(), pageable);
        }
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @PostMapping("/{id}/borrow")
    public ResponseEntity<?> borrowBook(@PathVariable Long id) {
        Optional<Book> bookOptional = bookService.findById(id);
        if (!bookOptional.isPresent()) {
            ErrorMessage errorMessage = new ErrorMessage("Không tồn tại sách này");
            return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
        }
        Book book = bookOptional.get();
        if (book.getQuantity() <= 0) {
            String message = "Sách " + book.getName() + " đã hết rồi";
            ErrorMessage errorMessage = new ErrorMessage(message);
            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }
        book.setQuantity(book.getQuantity() - 1);
        return new ResponseEntity<>(bookService.save(book), HttpStatus.OK);
    }
}
