package com.insta.my_app.adminController;

import com.insta.my_app.domain.User;
import com.insta.my_app.model.PostDTO;
import com.insta.my_app.repos.UserRepository;
import com.insta.my_app.service.PostService;
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
@RequestMapping("/Admin/posts")
public class PostController {

    private final PostService postService;
    private final UserRepository userRepository;

    public PostController(final PostService postService, final UserRepository userRepository) {
        this.postService = postService;
        this.userRepository = userRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("userValues", userRepository.findAll(Sort.by("userId"))
                .stream()
                .collect(CustomCollectors.toSortedMap(User::getUserId, User::getUsername)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("posts", postService.findAll());
        return "post/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("post") final PostDTO postDTO) {
        return "post/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("post") @Valid final PostDTO postDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "post/add";
        }
        postService.create(postDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("post.create.success"));
        return "redirect:/Admin/posts";
    }

    @GetMapping("/edit/{postId}")
    public String edit(@PathVariable(name = "postId") final Integer postId, final Model model) {
        model.addAttribute("post", postService.get(postId));
        return "post/edit";
    }

    @PostMapping("/edit/{postId}")
    public String edit(@PathVariable(name = "postId") final Integer postId,
            @ModelAttribute("post") @Valid final PostDTO postDTO, final BindingResult bindingResult,
            final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "post/edit";
        }
        postService.update(postId, postDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("post.update.success"));
        return "redirect:/Admin/posts";
    }

    @PostMapping("/delete/{postId}")
    public String delete(@PathVariable(name = "postId") final Integer postId,
            final RedirectAttributes redirectAttributes) {
        final String referencedWarning = postService.getReferencedWarning(postId);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            postService.delete(postId);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("post.delete.success"));
        }
        return "redirect:/Admin/posts";
    }

}
