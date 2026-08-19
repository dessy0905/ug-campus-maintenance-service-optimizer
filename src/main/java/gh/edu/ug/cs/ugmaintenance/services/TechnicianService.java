package gh.edu.ug.cs.ugmaintenance.services;

import gh.edu.ug.cs.ugmaintenance.datastructures.linkedlist.List;
import gh.edu.ug.cs.ugmaintenance.models.Technician;
import gh.edu.ug.cs.ugmaintenance.repositories.TechnicianRepository;

import java.util.Optional;

public class TechnicianService {

    private final TechnicianRepository repository;

    public TechnicianService() {
        this.repository = new TechnicianRepository();
    }

    public Optional<Technician> getTechnicianById(int technicianId) {

        if (technicianId <= 0) {
            throw new IllegalArgumentException(
                    "Invalid technician ID."
            );
        }

        return repository.findById(technicianId);
    }

    public List<Technician> getAvailableTechnicians() {

        return repository.findAvailableTechnicians();
    }

    public List<Technician> getAllTechnicians() {
        return repository.findAll();
    }

    public List<Technician> getTechniciansByCategory(int categoryId) {

        if (categoryId <= 0) {
            throw new IllegalArgumentException(
                    "Invalid category ID."
            );
        }

        return repository.findByCategory(categoryId);
    }

    public List<Technician> getAvailableTechniciansByCategory(
            int categoryId) {

        if (categoryId <= 0) {
            throw new IllegalArgumentException(
                    "Invalid category ID."
            );
        }

        return repository.findAvailableByCategory(categoryId);
    }

    public boolean updateAvailability(
            int technicianId,
            boolean available) {

        if (technicianId <= 0) {
            throw new IllegalArgumentException(
                    "Invalid technician ID."
            );
        }

        return repository.updateAvailability(
                technicianId,
                available
        );
    }
}