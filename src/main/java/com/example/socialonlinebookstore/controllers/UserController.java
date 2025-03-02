package com.example.socialonlinebookstore.controllers;

import com.example.socialonlinebookstore.domainmodel.*;
import com.example.socialonlinebookstore.mappers.*;
import com.example.socialonlinebookstore.services.UserProfileService;
import com.example.socialonlinebookstore.services.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

@Controller
public class UserController {

    @Autowired
    private UserMapper userRepository;

    @Autowired
    private UserProfileMapper userProfileMapper;

    @Autowired
    private UserProfileService userProfileService;

    @Autowired
    private UserService userService;

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private BookAuthorMapper bookAuthorMapper;

    @Autowired
    private BookCategoryMapper bookCategoryMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/")
    public ModelAndView index(@RequestParam(required = false) String error) {
        ModelAndView mav = new ModelAndView("index");
        mav.addObject("error", error);
        return mav;
    }

    @PostMapping("/register")
    public ModelAndView register(@RequestParam String username, @RequestParam String password) {
        Optional<User> existingUser = userRepository.findByUsername(username);
        if (existingUser.isPresent()) {
            return new ModelAndView("redirect:/?error=User already exists");
        }
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(passwordEncoder.encode(password));
        userRepository.save(newUser);
        return new ModelAndView("redirect:/create-profile");
    }

    @PostMapping("/create-profile")
    public String createProfile(@RequestParam String username, @RequestParam String fullName, @RequestParam int age,
                                @RequestParam String address, @RequestParam String phoneNumber,
                                @RequestParam String favoriteBookAuthors, @RequestParam String favoriteBookCategories) {
        userProfileService.createProfile(username, fullName, age, address, phoneNumber, favoriteBookAuthors, favoriteBookCategories);
        return "redirect:/";
    }

    @GetMapping("/profile")
    public String showProfile(Model model, Principal principal) {
        String username = principal.getName();
        UserProfile userProfile = userProfileService.getUserProfile(username);
        model.addAttribute("userProfile", userProfile);
        return "profile";
    }

    @GetMapping("/login")
    public String showLoginPage(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            String username = authentication.getName();
            boolean hasProfile = userService.hasProfile(username);
            model.addAttribute("hasProfile", hasProfile);
        }
        return "index";
    }

    @PostMapping("/login")
    public ModelAndView login(@RequestParam String username, @RequestParam String password) {
        ModelAndView mav = new ModelAndView("redirect:/?error=true");

        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            mav.setViewName("redirect:/?error=User is not registered");
            return mav;
        }

        User user = userOptional.get();
        if (passwordEncoder.matches(password, user.getPassword())) {
            mav.setViewName("redirect:/profile");
        } else {
            mav.setViewName("redirect:/?error=Invalid credentials");
        }

        return mav;
    }

    @GetMapping("/create-profile")
    public String showCreateProfileForm() {
        return "create-profile";
    }

    @GetMapping("/book-offers")
    public String showBookOffers(Model model, Authentication authentication) {
        String username = authentication.getName();
        List<Book> bookOffers = userProfileService.retrieveBookOffers(username);
        model.addAttribute("bookOffers", bookOffers);
        return "book-offers";
    }

    @PostMapping("/add-book-offer")
    @Transactional
    public String addBookOffer(@RequestParam String title, @RequestParam String authors,
                               @RequestParam String category, @RequestParam String summary,
                               Model model, Principal principal) {
        String username = principal.getName();
        UserProfile userProfile = userProfileService.getUserProfile(username);

        // Split the comma-separated string into a List
        List<String> authorList = Arrays.stream(authors.split(","))
                .filter(author -> !author.isEmpty())
                .map(String::trim) // Trim any leading/trailing spaces
                .toList();

        // Process each author name
        List<BookAuthor> authorNames = authorList.stream()
                .map(name -> {
                    Optional<BookAuthor> foundAuthors = bookAuthorMapper.findByName(name);
                    if (foundAuthors.isEmpty()) {
                        BookAuthor newAuthor = new BookAuthor(name);
                        bookAuthorMapper.save(newAuthor);
                        return newAuthor;
                    } else {
                        return foundAuthors.get();
                    }
                })
                .collect(Collectors.toList());
        model.addAttribute("authors", authorNames);

        BookCategory bookCategory = bookCategoryMapper.findByName(category)
                .stream().findFirst().orElseGet(() -> {
                    BookCategory newCategory = new BookCategory(category);
                    return bookCategoryMapper.save(newCategory);
                });

        List<Book> existingBooks = bookMapper.findByTitleAndBookAuthorsInAndBookCategory(title, authorNames, bookCategory);
        if (!existingBooks.isEmpty()) {
            model.addAttribute("message", "Book offer already exists.");
            return "add-book-offer";
        }

        // Create a new book if it does not exist
        Book newBook = new Book();
        newBook.setTitle(title);
        newBook.setBookAuthors(authorNames);
        newBook.setBookCategory(bookCategory);
        newBook.setSummary(summary);
        newBook.setUserName(username);
        bookMapper.save(newBook);

        userProfileService.addBookOffer(username, newBook);
        userProfileMapper.save(userProfile);

        model.addAttribute("book", newBook);
        model.addAttribute("name", username);
        model.addAttribute("message", "New book offer added successfully.");
        return "redirect:/profile";
    }

    @GetMapping("/add-book-offer")
    public String showAddBookOfferPage(Model model) {
        model.addAttribute("book", new Book());
        return "add-book-offer";
    }

    @GetMapping("/book-requests")
    public String showBookRequests(Model model, Authentication authentication) {
        String username = authentication.getName();
        UserProfile userProfile = userProfileService.getUserProfile(username);
        List<Book> bookRequests = userProfileService.retrieveBookRequests(username);
        model.addAttribute("requests", bookRequests);

        return "book-requests";
    }


    @GetMapping("/add-book-request")
    public String showAddBookRequestPage(Model model) {
        model.addAttribute("book", new Book());
        return "add-book-request";
    }

    @Transactional
    @PostMapping("/add-book-request")
    public String addBookRequest(@RequestParam String title, @RequestParam String requestingUsername,
                                 Model model, Principal principal) {
        String username = principal.getName();
        UserProfile userProfile = userProfileService.getUserProfile(username);
        UserProfile requestingUser = userProfileService.getUserProfile(requestingUsername);

        Book book = bookMapper.findByTitle(title).orElseThrow(() -> new RuntimeException("Book does not exist."));
        model.addAttribute("book", book);
        if (!userProfile.getRequestedBooks().contains(book)) {
            book.getRequestingUsers().add(requestingUser);
            bookMapper.save(book);
            userProfile.getRequestedBooks().add(book);
            model.addAttribute("message", "Requested book from user " + username + "successfully.");
        }
        return "redirect:/book-requests";
    }

    @PostMapping("/search-book-offers")
    public String searchBookOffers(@RequestParam String title,
                                   @RequestParam(required = false) String authors,
                                   @RequestParam String searchType,
                                   Model model, Authentication authentication) {
        String username = authentication.getName();

        List<Book> books;

        if ("exact".equals(searchType)) {
            books = searchBooksExact(title, authors);
        } else {
            books = searchBooksApproximate(title, authors);
        }

        model.addAttribute("books", books);
        model.addAttribute("searchTitle", title);
        model.addAttribute("searchAuthors", authors);
        model.addAttribute("username", username);

        return "search-book-offers-results";
    }


    private List<Book> searchBooksExact(String title, String authors) {
        if (authors == null || authors.isEmpty()) {
            return bookMapper.findByTitle(title)
                    .map(List::of)
                    .orElseGet(List::of);
        } else {
            List<String> authorList = stream(authors.split(","))
                    .map(String::trim)
                    .filter(author -> !author.isEmpty())
                    .distinct()
                    .toList();
            List<BookAuthor> authorEntities = authorList.stream()
                    .map(name -> bookAuthorMapper.findByName(name)
                            .orElseThrow(() -> new RuntimeException("Author " + name + " does not exist.")))
                    .collect(Collectors.toList());
            return bookMapper.findByTitleAndBookAuthorsIn(title, authorEntities);
        }
    }

    private List<Book> searchBooksApproximate(String title, String authors) {
        if (authors == null || authors.isEmpty()) {
            return bookMapper.findByTitleContaining(title);
        } else {
            List<String> authorList = stream(authors.split(","))
                    .map(String::trim)
                    .filter(author -> !author.isEmpty())
                    .distinct()
                    .toList();
            List<BookAuthor> authorEntities = authorList.stream()
                    .map(name -> bookAuthorMapper.findByNameContaining(name))
                    .collect(Collectors.toList());
            return bookMapper.findByTitleContainingAndBookAuthorsIn(title, authorEntities);
        }
    }


    @GetMapping("/search-book-offers")
    public String showSearchBookOffersPage(Model model) {
        model.addAttribute("book", new Book());
        return "search-book-offers";
    }

    @GetMapping("/search-book-offers-results")
    public String showBookOffersResults(Model model, Authentication authentication) {
        model.addAttribute("book", new Book());
        return "search-book-offers-results";
    }

    @GetMapping("/remove-book")
    public String showRemoveBookOfferPage(Model model) {
        model.addAttribute("book", new Book());
        return "redirect:/book-offers";
    }

    @Transactional
    @PostMapping("/remove-book/{id}")
    public String removeBookOffer(Principal principal, @PathVariable int id) {
        String username = principal.getName();
        Book book = bookMapper.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid book ID: " + id));
        userProfileService.removeBookOffer(id, username);
        bookMapper.delete(book);
        return "redirect:/book-offers";
    }

    @GetMapping("/recommended-books")
    public String showRecommendBooksPage(Model model, Authentication authentication) {
        String username = authentication.getName();
        List<Book> recommendedBooks = userProfileService.recommendBooks(username, null);
        model.addAttribute("recommendedBooks", recommendedBooks);
        return "recommended-books";
    }

    @PostMapping("/recommended-books-authors")
    public String showRecommendBooksWithAuthorsPage(Model model, Authentication authentication) {
        String username = authentication.getName();
        List<Book> recommendedBooks = userProfileService.recommendBooks(username, "AuthorsOnly");
        model.addAttribute("recommendedBooks", recommendedBooks);
        return "recommended-books-authors";
    }

    @PostMapping("/recommended-books-categories")
    public String showRecommendBooksWithCategoriesPage(Model model, Authentication authentication) {
        String username = authentication.getName();
        List<Book> recommendedBooks = userProfileService.recommendBooks(username, "CategoriesOnly");
        model.addAttribute("recommendedBooks", recommendedBooks);
        return "recommended-books-categories";
    }

    @RequestMapping("/user-details/{username}")
    public String showUserDetails(@PathVariable String username, Model model, Authentication authentication) {
        String auth_username = authentication.getName();
        if (auth_username.equals(username)){
            return "redirect:/search-book-offers-results";
        }

        UserProfile userProfile = userProfileService.getUserProfile(username);
        model.addAttribute("userProfile", userProfile);
        return "user-details";
    }

    @GetMapping("/user-details/{username}")
    public String showUserDetails(@PathVariable String username, Model model) {
        UserProfile userProfile = userProfileService.getUserProfile(username);
        model.addAttribute("userProfile", userProfile);
        return "user-details";
    }



}