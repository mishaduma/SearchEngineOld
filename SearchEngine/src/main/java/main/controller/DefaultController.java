package main.controller;

import lombok.RequiredArgsConstructor;
import main.model.PageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class DefaultController {

    private final PageService pageService;

    @RequestMapping("/")
    public String index(Model model) {
        pageService.findPages("http://www.playback.ru/");
        model.addAttribute("pages", pageService.listPages());
        model.addAttribute("pagesCount", pageService.listPages().size());
        return "index";
    }
}
