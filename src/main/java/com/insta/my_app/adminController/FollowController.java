package com.insta.my_app.adminController;

import com.insta.my_app.domain.User;
import com.insta.my_app.model.FollowDTO;
import com.insta.my_app.repos.UserRepository;
import com.insta.my_app.service.FollowService;
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
@RequestMapping("/Admin/follows")
public class FollowController {

    private final FollowService followService;
    private final UserRepository userRepository;

    public FollowController(final FollowService followService,
            final UserRepository userRepository) {
        this.followService = followService;
        this.userRepository = userRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("followerValues", userRepository.findAll(Sort.by("userId"))
                .stream()
                .collect(CustomCollectors.toSortedMap(User::getUserId, User::getUsername)));
        model.addAttribute("followingValues", userRepository.findAll(Sort.by("userId"))
                .stream()
                .collect(CustomCollectors.toSortedMap(User::getUserId, User::getUsername)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("follows", followService.findAll());
        return "follow/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("follow") final FollowDTO followDTO) {
        return "follow/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("follow") @Valid final FollowDTO followDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "follow/add";
        }
        followService.create(followDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("follow.create.success"));
        return "redirect:/Admin/follows";
    }

    @GetMapping("/edit/{followId}")
    public String edit(@PathVariable(name = "followId") final Integer followId, final Model model) {
        model.addAttribute("follow", followService.get(followId));
        return "follow/edit";
    }

    @PostMapping("/edit/{followId}")
    public String edit(@PathVariable(name = "followId") final Integer followId,
            @ModelAttribute("follow") @Valid final FollowDTO followDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "follow/edit";
        }
        followService.update(followId, followDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("follow.update.success"));
        return "redirect:/Admin/follows";
    }

    @PostMapping("/delete/{followId}")
    public String delete(@PathVariable(name = "followId") final Integer followId,
            final RedirectAttributes redirectAttributes) {
        followService.delete(followId);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("follow.delete.success"));
        return "redirect:/Admin/follows";
    }

}
