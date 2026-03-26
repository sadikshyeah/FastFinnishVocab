package my.project.vocab.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMethod;

import my.project.vocab.domain.Word;
import my.project.vocab.domain.WordRepository;

@Controller
public class WordController {
    @Autowired
    private WordRepository repository;

    @Secured("ROLE_USER")
    @RequestMapping("/learn")
    public String wordLearn(Model model) {
        model.addAttribute("words", repository.findAll());
        return "wordLearn";
    }

    @Secured({ "ROLE_ADMIN", "ROLE_USER" })
    @RequestMapping(value = { "/", "/wordlist" })
    public String wordList(Model model, Authentication auth) {
        model.addAttribute("words", repository.findAll());
        
        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return "wordList"; // Admin view
        } else {
            return "redirect:/learn"; // Regular user view
        }
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping("/add")
    public String addWord(Model model) {
        model.addAttribute("word", new Word());
        return "addword";
    }

    // CREATE - save
    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(Word word) {
        repository.save(word);
        return "redirect:/wordlist";
    }

    // DELETE
    @Secured("ROLE_ADMIN")
    @RequestMapping("/delete/{id}")
    public String deleteWord(@PathVariable("id") Long id) {
        repository.deleteById(id);
        return "redirect:/wordlist";
    }

    // UPDATE - show edit form
    @Secured("ROLE_ADMIN")
    @RequestMapping("/edit/{id}")
    public String editWord(@PathVariable("id") Long id, Model model) {
        model.addAttribute("word", repository.findById(id).orElse(null));
        return "editword";
    }

}
