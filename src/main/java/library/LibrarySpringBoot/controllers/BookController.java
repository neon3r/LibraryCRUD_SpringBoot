package library.LibrarySpringBoot.controllers;

import jakarta.validation.Valid;
import library.LibrarySpringBoot.models.Book;
import library.LibrarySpringBoot.services.BookService;
import library.LibrarySpringBoot.services.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;
    private final PeopleService peopleService;

    @Autowired
    public BookController(BookService bookService, PeopleService peopleService) {
        this.bookService = bookService;
        this.peopleService = peopleService;
    }

    @GetMapping()
    public String index(Model model, @RequestParam(value = "page", required = false, defaultValue = "0") int page,
                        @RequestParam(value = "items_per_page", required = false, defaultValue = "1000") int itemsPerPage,
                        @RequestParam(value = "sort_by", required = false, defaultValue = "id") String sortBy) {
        model.addAttribute("books", bookService.findAll(page, itemsPerPage, sortBy));
        return "/books/index";
    }

    @GetMapping("/search")
    public String search(Model model, @RequestParam(value = "request", required = false, defaultValue = "") String request){
        model.addAttribute("books", bookService.findAllByTitleStartsWith(request));
        return "/books/search";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        model.addAttribute("book", bookService.findOne(id));
        model.addAttribute("owner", bookService.findOne(id).getOwner());
        model.addAttribute("people", peopleService.findAll());
        return "/books/show";
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book book) {
        return "/books/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return "/books/new";
        bookService.create(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model) {
        model.addAttribute("book", bookService.findOne(id));
        return "/books/edit";
    }

    @PatchMapping("/{id}")
    public String update(@PathVariable("id") int id, @ModelAttribute("book") @Valid Book book, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return "/books/edit";
        bookService.update(id, book);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/free")
    public String free(@PathVariable("id") int id) {
        bookService.release(id);
        return "redirect:/books/{id}";
    }

    @PatchMapping("/{id}/take")
    public String take(@PathVariable("id") int id, @ModelAttribute("book") Book book) {
        bookService.take(id, book);
        return "redirect:/books/{id}";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        bookService.delete(id);
        return "redirect:/books";
    }
}
