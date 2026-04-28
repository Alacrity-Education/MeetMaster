package ro.alacrity.meetmaster.web;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;
import ro.alacrity.meetmaster.domain.Meet;
import ro.alacrity.meetmaster.domain.MeetRepository;

import java.time.Instant;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class MeetsController {

    private final MeetRepository meetRepository;

    public MeetsController(MeetRepository meetRepository) {
        this.meetRepository = meetRepository;
    }

    @GetMapping("/meets")
    public String listMeets(@RequestParam(defaultValue = "7") String period, Model model) {
        long now = Instant.now().getEpochSecond();

        var meets = switch (period) {
            case "all" -> meetRepository.findAllByOrderByCreateTimeDesc();
            case "30"  -> meetRepository.findByCreateTimeGreaterThanEqualOrderByCreateTimeDesc(now - 30 * 86400L);
            default    -> {
                period = "7";
                yield meetRepository.findByCreateTimeGreaterThanEqualOrderByCreateTimeDesc(now - 7 * 86400L);
            }
        };

        model.addAttribute("meets", meets);
        model.addAttribute("period", period);
        return "meets";
    }

    @PostMapping("/meets")
    public String createMeet(@RequestParam String name,
                             @RequestParam(defaultValue = "7") String period,
                             @AuthenticationPrincipal OAuth2User user) {
        String creator = user.getName();
        String id = meetRepository.generateUniqueId();
        var participants = new HashSet<String>();
        participants.add(creator);
        meetRepository.save(new Meet(id, name, participants, creator, true));
        return "redirect:/meets?period=" + period;
    }

    @GetMapping("/m/{id}")
    public String meetDetail(@PathVariable String id,
                             HttpServletRequest request,
                             @AuthenticationPrincipal OAuth2User user,
                             Model model) {
        Meet meet = meetRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        model.addAttribute("meet", meet);
        model.addAttribute("currentUrl", request.getRequestURL().toString());
        model.addAttribute("isParticipant", meet.getParticipants().contains(user.getName()));
        model.addAttribute("isCreator", meet.getCreator().equals(user.getName()));
        model.addAttribute("participantsHash", participantsHash(meet.getParticipants()));
        return "meet-detail";
    }

    @GetMapping("/api/m/{id}/participants-hash")
    @ResponseBody
    public Map<String, String> participantsHash(@PathVariable String id) {
        Meet meet = meetRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return Map.of("hash", participantsHash(meet.getParticipants()));
    }

    private static String participantsHash(Set<String> participants) {
        return Integer.toHexString(
                participants.stream().sorted().collect(Collectors.joining(",")).hashCode()
        );
    }

    @Transactional
    @PostMapping("/m/{id}/participants")
    public String addParticipant(@PathVariable String id,
                                 @RequestParam String name,
                                 @AuthenticationPrincipal OAuth2User user) {
        Meet meet = meetRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (!meet.getCreator().equals(user.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        String trimmed = name.strip();
        if (!trimmed.isEmpty()) {
            meet.getParticipants().add(trimmed);
        }
        return "redirect:/m/" + id;
    }

    @Transactional
    @PostMapping("/m/{id}/participate")
    public String participate(@PathVariable String id,
                              @AuthenticationPrincipal OAuth2User user) {
        Meet meet = meetRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (!meet.isActive()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Meeting is no longer active");
        }
        meet.getParticipants().add(user.getName());
        return "redirect:/m/" + id;
    }
}
