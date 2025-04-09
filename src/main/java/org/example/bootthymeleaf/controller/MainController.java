package org.example.bootthymeleaf.controller;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.bootthymeleaf.model.dto.UpdateWordForm;
import org.example.bootthymeleaf.model.dto.WordForm;
import org.example.bootthymeleaf.model.entity.Word;
import org.example.bootthymeleaf.model.repository.WordRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class MainController {
    private final WordRepository wordRepository;

    @GetMapping
    public String index(Model model) {
//        Word word = new Word();
//        word.setText("고양이");
//        wordRepository.save(word);
        // 방법 1. sort한다 (데이터 적을 때) -> 추천 안함
//        model.addAttribute("words", wordRepository.findAll().stream().sorted((a,b) -> a.getCreatedAt().compareTo(b.getCreatedAt())).toList());
        // 방법 2. 쿼리 같은 걸 만들어야 함 -> 기준을 createdAt으로 잡기 (기존에는 uuid 기준)
          model.addAttribute("words", wordRepository.findAllByOrderByCreatedAtDesc());

//        model.addAttribute("message", message);
//        타임리프에서 폼에서 제공 하려면 변수를 추가해야함
        model.addAttribute("wordForm", new WordForm());
        return "index";
    }

    @PostMapping("/word")
    public String addWord(WordForm wordForm, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message", "끝말잇기 추가");
        Word word = new Word();
        word.setText(wordForm.getWord());
        wordRepository.save(word);
        return "redirect:/";
    }

    @PostMapping("/update")
    public String updateWord(UpdateWordForm form, RedirectAttributes redirectAttributes) {
        // JPA는 업데이트용 메서드나 기능이 따로 없음
        // JPA는 수정용이 따로 없음
        // -> 교체 개념이에요 => put <-> patch : 멱등성
        // JPA : Jakarta Persistence API
//        Word word = new Word();
//        word.setText(form.getNewWord());
//        word.setUuid(form.getUuid());
        Word oldWord = wordRepository.findById(form.getUuid()).orElseThrow();
        oldWord.setText(form.getNewWord());
        wordRepository.save(oldWord);
        redirectAttributes.addFlashAttribute("message", "정상적으로 교제되었습니다. %s".formatted(oldWord.getUuid()));
        return "redirect:/";
    }

    @PostMapping("/delete")
    public String deleteWord(@RequestParam("id") String uuid, RedirectAttributes redirectAttributes) {
        wordRepository.deleteById(uuid);
        redirectAttributes.addFlashAttribute("message", "끝말잇기 삭제 %s".formatted(uuid));
        return "redirect:/";
    }

    @PostMapping("/reset")
    public String resetWords(RedirectAttributes redirectAttributes) {
        wordRepository.deleteAll();

        // parameter로 message 전달됨
        // 주소창을 통해서 전달 -> Parameter로 해서 Model로 전달하기
//        redirectAttributes.addAttribute("message", "전체 삭제 되었습니다.");
        // 바로 model로 전달해줌
        redirectAttributes.addFlashAttribute("message", "전부 삭제 되었습니다.");
        return "redirect:/";
    }
}
