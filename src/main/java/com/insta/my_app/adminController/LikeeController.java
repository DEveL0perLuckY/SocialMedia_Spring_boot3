package com.insta.my_app.adminController;

import com.insta.my_app.domain.Post;
import com.insta.my_app.domain.User;
import com.insta.my_app.model.LikeeDTO;
import com.insta.my_app.repos.PostRepository;
import com.insta.my_app.repos.UserRepository;
import com.insta.my_app.service.LikeeService;
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
@RequestMapping("/Admin/likes")
public class LikeeController {

    private final LikeeService likeeService;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public LikeeController(final LikeeService likeeService, final UserRepository userRepository,
            final PostRepository postRepository) {
        this.likeeService = likeeService;
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
        model.addAttribute("likees", likeeService.findAll());
        return "likee/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("likee") final LikeeDTO likeeDTO) {
        return "likee/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("likee") @Valid final LikeeDTO likeeDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "likee/add";
        }
        likeeService.create(likeeDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("likee.create.success"));
        return "redirect:/Admin/likes";
    }

    @GetMapping("/edit/{likeId}")
    public String edit(@PathVariable(name = "likeId") final Integer likeId, final Model model) {
        model.addAttribute("likee", likeeService.get(likeId));
        return "likee/edit";
    }

    @PostMapping("/edit/{likeId}")
    public String edit(@PathVariable(name = "likeId") final Integer likeId,
            @ModelAttribute("likee") @Valid final LikeeDTO likeeDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "likee/edit";
        }
        likeeService.update(likeId, likeeDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("likee.update.success"));
        return "redirect:/Admin/likes";
    }

    @PostMapping("/delete/{likeId}")
    public String delete(@PathVariable(name = "likeId") final Integer likeId,
            final RedirectAttributes redirectAttributes) {
        likeeService.delete(likeId);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("likee.delete.success"));
        return "redirect:/Admin/likes";
    }

}
