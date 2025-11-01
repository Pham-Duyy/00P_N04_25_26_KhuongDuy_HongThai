package com.fund.group09.Controller;

import com.fund.group09.Model.*;
import com.fund.group09.Repository.*;
import com.fund.group09.Service.GroupService;
import com.fund.group09.Service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpSession;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
@RequestMapping("/admin/groups")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private MemberService memberService;

    @Autowired
    private InvitationRepository invitationRepository;

    // Xử lý trang quản lý nhóm
    @GetMapping
    public String index(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size,
            HttpSession session,
            Model model
    ) {
        if (!isLoggedIn(session) || !isAdmin(session)) {
            return "redirect:/login?error=access_denied";
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<Group> groupPage;
        
        if (keyword != null && !keyword.trim().isEmpty()) {
            groupPage = groupRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                keyword, keyword, pageable);
            model.addAttribute("keyword", keyword);
        } else {
            groupPage = groupRepository.findAll(pageable);
        }

        List<Group> groups = groupPage.getContent();
        Map<Long, Map<String, Object>> groupStats = new HashMap<>();
        
        for (Group group : groups) {
            Map<String, Object> stats = groupService.getGroupSummary(group.getId());
            groupStats.put(group.getId(), stats);
        }

        Map<Long, String> formattedDates = formatCreatedDates(groups);

        model.addAttribute("groups", groups);
        model.addAttribute("groupPage", groupPage);
        model.addAttribute("groupStats", groupStats);
        model.addAttribute("formattedDates", formattedDates);
        model.addAttribute("totalGroups", groupPage.getTotalElements());
        model.addAttribute("allUsers", userRepository.findAll());

        return "admin/groups/index";
    }

    // Tạo nhóm mới
    @PostMapping("/create")
    public String createGroup(
            @ModelAttribute Group group,
            RedirectAttributes redirectAttributes,
            HttpSession session
    ) {
        if (!isLoggedIn(session) || !isAdmin(session)) {
            return "redirect:/login?error=access_denied";
        }

        try {
            if (!isValidGroupName(group.getName())) {
                redirectAttributes.addFlashAttribute("error", "Tên nhóm phải từ 3 đến 50 ký tự!");
                return "redirect:/admin/groups";
            }

            group.setCreatedDate(LocalDateTime.now());
            group.setIsActive(true);
            group.setCreatedBy((String) session.getAttribute("userEmail"));
            if (group.getJoinCode() == null || group.getJoinCode().isEmpty()) {
                group.setJoinCode(generateJoinCode());
            }
            if (group.getMaxMembers() == null) {
                group.setMaxMembers(50);
            }
            if (group.getGroupType() == null) {
                group.setGroupType("PUBLIC");
            }

            Group savedGroup = groupRepository.save(group);

            // Chỉ tạo lời mời cho tất cả user có vai trò USER
            List<User> allUsers = userRepository.findByRole("USER");
            int invitationCount = 0;

            for (User user : allUsers) {
                try {
                    Invitation invitation = new Invitation();
                    invitation.setUser(user);
                    invitation.setGroup(savedGroup);
                    invitation.setStatus("PENDING");
                    invitation.setInvitedBy((String) session.getAttribute("userEmail"));
                    invitation.setInvitedDate(LocalDateTime.now());
                    invitationRepository.save(invitation);
                    invitationCount++;
                } catch (Exception e) {
                    System.err.println("Lỗi khi tạo lời mời cho user " + user.getEmail() + ": " + e.getMessage());
                }
            }

            redirectAttributes.addFlashAttribute("success",
                String.format("✅ Đã tạo nhóm \"%s\" và gửi lời mời đến %d người dùng!", 
                            savedGroup.getName(), invitationCount));

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "❌ Lỗi khi tạo nhóm: " + e.getMessage());
        }

        return "redirect:/admin/groups";
    }

    @PostMapping("/create-ajax")
    @ResponseBody
    public Map<String, Object> createGroupAjax(@RequestBody Map<String, Object> data, HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        try {
            String name = (String) data.get("name");
            String description = (String) data.get("description");
            Integer maxMembers = data.get("maxMembers") != null ? Integer.parseInt(data.get("maxMembers").toString()) : 50;
            String groupType = (String) data.getOrDefault("groupType", "PUBLIC");
            Boolean isActive = data.get("isActive") != null ? Boolean.parseBoolean(data.get("isActive").toString()) : true;

            if (name == null || name.trim().length() < 3 || name.trim().length() > 50) {
                result.put("success", false);
                result.put("message", "Tên nhóm phải từ 3 đến 50 ký tự!");
                return result;
            }

            Group group = new Group();
            group.setName(name);
            group.setDescription(description);
            group.setCreatedDate(LocalDateTime.now());
            group.setIsActive(isActive);
            group.setCreatedBy((String) session.getAttribute("userEmail"));
            group.setJoinCode(generateJoinCode());
            group.setMaxMembers(maxMembers);
            group.setGroupType(groupType);

            Group savedGroup = groupRepository.save(group);

            List<User> allUsers = userRepository.findByRole("USER");
            int invitationCount = 0;

            for (User user : allUsers) {
                try {
                    Invitation invitation = new Invitation();
                    invitation.setUser(user);
                    invitation.setGroup(savedGroup);
                    invitation.setStatus("PENDING");
                    invitation.setInvitedBy((String) session.getAttribute("userEmail"));
                    invitation.setInvitedDate(LocalDateTime.now());
                    invitationRepository.save(invitation);
                    invitationCount++;
                } catch (Exception e) {
                    System.err.println("Lỗi khi tạo lời mời cho user " + user.getEmail() + ": " + e.getMessage());
                }
            }

            result.put("success", true);
            result.put("message", String.format("✅ Đã tạo nhóm thành công!\n📧 Gửi lời mời: %d", invitationCount));
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "❌ Lỗi khi tạo nhóm: " + e.getMessage());
        }
        return result;
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<String> deleteGroup(@PathVariable Long id, HttpSession session) {
        if (!isLoggedIn(session) || !isAdmin(session)) {
            return ResponseEntity.status(403).body("Không có quyền thực hiện thao tác này");
        }

        try {
            groupService.deleteGroup(id);
            return ResponseEntity.ok("Xóa nhóm thành công");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Lỗi khi xóa nhóm: " + e.getMessage());
        }
    }

    public void deleteGroup(Long id) {
        invitationRepository.deleteByGroupId(id);
        groupRepository.deleteById(id);
    }

    @GetMapping("/detail/{id}")
    public String viewGroupDetail(@PathVariable Long id, Model model, HttpSession session) {
        if (!isLoggedIn(session) || !isAdmin(session)) {
            return "redirect:/login?error=access_denied";
        }

        Group group = groupRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy nhóm"));

        List<Member> members = memberService.getMembersByGroup(id);
        group.setMembers(members);

        Map<String, Object> summary = groupService.getGroupSummary(id);
        List<Invitation> pendingInvitations = invitationRepository.findByGroupAndStatus(group, "PENDING");

        model.addAttribute("group", group);
        model.addAttribute("summary", summary);
        model.addAttribute("pendingInvitations", pendingInvitations);

        return "admin/groups/detail";
    }

    // Utility methods
    private boolean isValidGroupName(String name) {
        return name != null && name.trim().length() >= 3 && name.trim().length() <= 50;
    }

    private Map<Long, String> formatCreatedDates(List<Group> groups) {
        Map<Long, String> formattedDates = new HashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        
        for (Group group : groups) {
            if (group.getCreatedDate() != null) {
                formattedDates.put(group.getId(), group.getCreatedDate().format(formatter));
            }
        }
        
        return formattedDates;
    }

    private boolean isLoggedIn(HttpSession session) {
        return session.getAttribute("userRole") != null && 
               session.getAttribute("userId") != null;
    }

    private boolean isAdmin(HttpSession session) {
        return "ADMIN".equals(session.getAttribute("userRole"));
    }

    private String generateJoinCode() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }
}