package com.insta.my_app.adminController;

import com.insta.my_app.domain.Post;
import com.insta.my_app.domain.User;
import com.insta.my_app.model.CommentDTO;
import com.insta.my_app.repos.PostRepository;
import com.insta.my_app.repos.UserRepository;
import com.insta.my_app.service.CommentService;
import com.insta.my_app.util.CustomCollectors;
import com.insta.my_app.util.WebUtils;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/Admin/comments")
public class CommentController {

    private final CommentService commentService;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public CommentController(final CommentService commentService,
            final UserRepository userRepository, final PostRepository postRepository) {
        this.commentService = commentService;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("userValues", userRepository.findAll(Sort.by("userId"))
                .stream()
                .collect(CustomCollectors.toSortedMap(User::getUserId, User::getUsername)));
        model.addAttribute("postValues", postRepository.findAll(Sort.by("postId"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Post::getPostId, Post::getPostId)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("comments", commentService.findAll());
        return "comment/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("comment") final CommentDTO commentDTO) {
        return "comment/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("comment") @Valid final CommentDTO commentDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "comment/add";
        }
        commentService.create(commentDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("comment.create.success"));
        return "redirect:/Admin/comments";
    }

    @GetMapping("/edit/{commentId}")
    public String edit(@PathVariable(name = "commentId") final Integer commentId,
            final Model model) {
        model.addAttribute("comment", commentService.get(commentId));
        return "comment/edit";
    }

    @PostMapping("/edit/{commentId}")
    public String edit(@PathVariable(name = "commentId") final Integer commentId,
            @ModelAttribute("comment") @Valid final CommentDTO commentDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "comment/edit";
        }
        commentService.update(commentId, commentDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("comment.update.success"));
        return "redirect:/Admin/comments";
    }

    @PostMapping("/delete/{commentId}")
    public String delete(@PathVariable(name = "commentId") final Integer commentId,
            final RedirectAttributes redirectAttributes) {
        commentService.delete(commentId);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("comment.delete.success"));
        return "redirect:/Admin/comments";
    }

}
