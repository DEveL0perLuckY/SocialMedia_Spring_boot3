package com.insta.my_app.adminController;

import com.insta.my_app.domain.User;
import com.insta.my_app.model.NotificationDTO;
import com.insta.my_app.repos.UserRepository;
import com.insta.my_app.service.NotificationService;
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
@RequestMapping("/Admin/notifications")
public class NotificationController {

    private final NotificationService notificationService;
    private final UserRepository userRepository;

    public NotificationController(final NotificationService notificationService,
            final UserRepository userRepository) {
        this.notificationService = notificationService;
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
        model.addAttribute("notifications", notificationService.findAll());
        return "notification/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("notification") final NotificationDTO notificationDTO) {
        return "notification/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("notification") @Valid final NotificationDTO notificationDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "notification/add";
        }
        notificationService.create(notificationDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("notification.create.success"));
        return "redirect:/Admin/notifications";
    }

    @GetMapping("/edit/{notificationId}")
    public String edit(@PathVariable(name = "notificationId") final Integer notificationId,
            final Model model) {
        model.addAttribute("notification", notificationService.get(notificationId));
        return "notification/edit";
    }

    @PostMapping("/edit/{notificationId}")
    public String edit(@PathVariable(name = "notificationId") final Integer notificationId,
            @ModelAttribute("notification") @Valid final NotificationDTO notificationDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "notification/edit";
        }
        notificationService.update(notificationId, notificationDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("notification.update.success"));
        return "redirect:/Admin/notifications";
    }

    @PostMapping("/delete/{notificationId}")
    public String delete(@PathVariable(name = "notificationId") final Integer notificationId,
            final RedirectAttributes redirectAttributes) {
        notificationService.delete(notificationId);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("notification.delete.success"));
        return "redirect:/Admin/notifications";
    }

}
