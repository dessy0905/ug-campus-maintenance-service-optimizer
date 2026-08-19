package gh.edu.ug.cs.ugmaintenance.services;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import gh.edu.ug.cs.ugmaintenance.datastructures.linkedlist.List;
import gh.edu.ug.cs.ugmaintenance.models.Technician;
import gh.edu.ug.cs.ugmaintenance.models.User;
import gh.edu.ug.cs.ugmaintenance.models.enums.UserRole;
import gh.edu.ug.cs.ugmaintenance.repositories.CampusUserRepository;
import gh.edu.ug.cs.ugmaintenance.repositories.TechnicianRepository;

public class AuthService {

    private final CampusUserRepository userRepository;
    private final TechnicianRepository technicianRepository;

    public AuthService() {
        this.userRepository = new CampusUserRepository();
        this.technicianRepository = new TechnicianRepository();
    }

    public Map<String, Object> loginByFrontendRole(String frontendRole) {
        if (frontendRole == null || frontendRole.isBlank()) {
            throw new IllegalArgumentException("Role is required.");
        }

        return switch (frontendRole.trim()) {
            case "Campus User" -> loginCampusUser();
            case "Technician" -> loginTechnician();
            case "Admin" -> loginAdmin();
            default -> throw new IllegalArgumentException(
                    "Unsupported role: " + frontendRole
            );
        };
    }

    private Map<String, Object> loginCampusUser() {
        return demoUser(1, "Kwame Asiedu", "kwame@ug.edu.gh", "Campus User");
    }

    private Map<String, Object> loginAdmin() {
        return demoUser(6, "Ruth Darko", "ruth@ug.edu.gh", "Admin");
    }

    private Map<String, Object> loginTechnician() {
        Map<String, Object> view = new LinkedHashMap<>();
        view.put("id", 1);
        view.put("name", "Kofi Boateng");
        view.put("role", "Technician");
        view.put("email", "0244001001");
        view.put(
                "avatar",
                initials("Kofi Boateng")
        );
        view.put("specialization", "Plumbing");
        return view;
    }

    private Map<String, Object> demoUser(
            int id,
            String name,
            String email,
            String role) {
        Map<String, Object> view = new LinkedHashMap<>();
        view.put("id", id);
        view.put("name", name);
        view.put("role", role);
        view.put("email", email);
        view.put("avatar", initials(name));
        return view;
    }

    private Map<String, Object> toUserView(User user, String frontendRole) {
        Map<String, Object> view = new LinkedHashMap<>();
        view.put("id", user.getUserId());
        view.put("name", user.getFullName().trim());
        view.put("role", frontendRole);
        view.put("email", user.getEmail());
        view.put("avatar", initials(user.getFullName()));
        return view;
    }

    private String initials(String fullName) {
        if (fullName == null || fullName.isBlank()) {
            return "UG";
        }

        String[] parts = fullName.trim().split("\\s+");
        if (parts.length == 1) {
            return parts[0].substring(0, 1).toUpperCase();
        }

        return ("" + parts[0].charAt(0) + parts[1].charAt(0)).toUpperCase();
    }
}
