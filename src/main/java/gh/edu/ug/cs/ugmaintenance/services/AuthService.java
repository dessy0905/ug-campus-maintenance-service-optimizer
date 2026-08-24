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

    public Map<String, Object> login(String frontendRole, Integer entityId) {
        if (frontendRole == null || frontendRole.isBlank()) {
            throw new IllegalArgumentException("Role is required.");
        }

        return switch (frontendRole.trim()) {
            case "Campus User" -> loginCampusUser(entityId);
            case "Technician" -> loginTechnician(entityId);
            case "Admin" -> loginAdmin(entityId);
            default -> throw new IllegalArgumentException(
                    "Unsupported role: " + frontendRole
            );
        };
    }

    public Map<String, Object> loginByFrontendRole(String frontendRole) {
        return login(frontendRole, null);
    }

    private Map<String, Object> loginCampusUser(Integer userId) {
        if (userId != null && userId > 0) {
            Optional<User> user = userRepository.findById(userId);
            if (user.isPresent()) {
                return toUserView(user.get(), "Campus User");
            }
        }
        Optional<User> student = userRepository.findFirstByRole(UserRole.STUDENT);
        if (student.isPresent()) {
            return toUserView(student.get(), "Campus User");
        }
        List<User> all = userRepository.findAll();
        if (all.size() > 0) {
            return toUserView(all.get(0), "Campus User");
        }
        return demoUser(1, "Campus User", "user@ug.edu.gh", "Campus User");
    }

    private Map<String, Object> loginAdmin(Integer userId) {
        if (userId != null && userId > 0) {
            Optional<User> user = userRepository.findById(userId);
            if (user.isPresent()) {
                return toUserView(user.get(), "Admin");
            }
        }
        Optional<User> admin = userRepository.findFirstByRole(UserRole.ADMIN);
        if (admin.isPresent()) {
            return toUserView(admin.get(), "Admin");
        }
        List<User> all = userRepository.findAll();
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getRole() == UserRole.ADMIN) {
                return toUserView(all.get(i), "Admin");
            }
        }
        if (all.size() > 0) {
            return toUserView(all.get(0), "Admin");
        }
        return demoUser(6, "Admin Staff", "admin@ug.edu.gh", "Admin");
    }

    private Map<String, Object> loginTechnician(Integer technicianId) {
        if (technicianId != null && technicianId > 0) {
            Optional<Technician> tech = technicianRepository.findById(technicianId);
            if (tech.isPresent()) {
                return toTechnicianView(tech.get());
            }
        }
        List<Technician> all = technicianRepository.findAll();
        if (all.size() > 0) {
            return toTechnicianView(all.get(0));
        }
        throw new IllegalStateException("No technicians found in database.");
    }

    private Map<String, Object> toTechnicianView(Technician technician) {
        Map<String, Object> view = new LinkedHashMap<>();
        LookupService lookup = new LookupService();
        view.put("id", technician.getTechnicianId());
        view.put("name", technician.getFullName());
        view.put("role", "Technician");
        view.put("email", technician.getPhoneNumber());
        view.put("phone", technician.getPhoneNumber());
        view.put("specialization", technician.getSpecialization());
        view.put("categoryId", technician.getCategoryId());
        view.put("category", lookup.getCategoryName(technician.getCategoryId()));
        view.put("locationId", technician.getLocationId());
        view.put("location", lookup.getLocationName(technician.getLocationId()));
        view.put("status", technician.isAvailabilityStatus() ? "Active" : "Busy");
        view.put("avatar", initials(technician.getFullName()));
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
        view.put("phone", user.getPhoneNumber());
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
