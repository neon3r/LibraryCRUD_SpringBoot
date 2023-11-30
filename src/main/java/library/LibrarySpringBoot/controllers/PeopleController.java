package library.LibrarySpringBoot.controllers;

import jakarta.validation.Valid;
import library.LibrarySpringBoot.models.Person;
import library.LibrarySpringBoot.services.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/people")
public class PeopleController {
    private final PeopleService peopleService;

    @Autowired
    public PeopleController(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @GetMapping()
    public String index(Model model, @RequestParam(value = "page", required = false, defaultValue = "0") int page,
                        @RequestParam(value = "items_per_page", required = false, defaultValue = "1000") int itemsPerPage,
                        @RequestParam(value = "sort_by", required = false, defaultValue = "name") String sortBy) {
        model.addAttribute("people", peopleService.findAll(page, itemsPerPage, sortBy));
        return "/people/index";
    }

    @GetMapping("/search")
    public String search(Model model, @RequestParam(value = "request", required = false, defaultValue = "") String request){
        model.addAttribute("people", peopleService.findAllByNameStartsWith(request));
        return "/people/search";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        model.addAttribute("person", peopleService.findOne(id));
        model.addAttribute("bookList", peopleService.bookList(id));
        return "/people/show";
    }

    @GetMapping("/new")
    public String newPerson(@ModelAttribute("person") Person person) {
        return "/people/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult){
        if(bindingResult.hasErrors()) return "/people/new";
        peopleService.create(person);
        return "redirect:/people";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model) {
        model.addAttribute("person", peopleService.findOne(id));
        return "/people/edit";
    }

    @PatchMapping("/{id}")
    public String update(@PathVariable("id") int id, @ModelAttribute("person") @Valid Person person, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) return "/people/edit";
        peopleService.update(id, person);
        return "redirect:/people";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        peopleService.delete(id);
        return "redirect:/people";
    }
}
