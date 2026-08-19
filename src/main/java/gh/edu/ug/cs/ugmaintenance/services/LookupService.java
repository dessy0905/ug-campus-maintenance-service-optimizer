package gh.edu.ug.cs.ugmaintenance.services;

import java.util.Optional;

import gh.edu.ug.cs.ugmaintenance.models.Location;
import gh.edu.ug.cs.ugmaintenance.models.ServiceCategory;
import gh.edu.ug.cs.ugmaintenance.repositories.LocationRepository;
import gh.edu.ug.cs.ugmaintenance.repositories.ServiceCategoryRepository;

public class LookupService {

    private final LocationRepository locationRepository;
    private final ServiceCategoryRepository categoryRepository;

    public LookupService() {
        this.locationRepository = new LocationRepository();
        this.categoryRepository = new ServiceCategoryRepository();
    }

    public int resolveLocationId(String locationName) {
        if (locationName == null || locationName.isBlank()) {
            throw new IllegalArgumentException("Location is required.");
        }

        Optional<Location> location = locationRepository.findByName(locationName.trim());
        if (location.isEmpty()) {
            throw new IllegalArgumentException(
                    "Unknown location: " + locationName
            );
        }

        return location.get().getLocationId();
    }

    public int resolveCategoryId(String categoryName) {
        if (categoryName == null || categoryName.isBlank()) {
            throw new IllegalArgumentException("Category is required.");
        }

        String normalized = normalizeCategoryName(categoryName.trim());
        Optional<ServiceCategory> category = categoryRepository.findByName(normalized);

        if (category.isEmpty()) {
            throw new IllegalArgumentException(
                    "Unknown service category: " + categoryName
            );
        }

        return category.get().getCategoryId();
    }

    public String getLocationName(int locationId) {
        return new RouteService().getLocationName(locationId);
    }

    public String getCategoryName(int categoryId) {
        return categoryRepository.findById(categoryId)
                .map(ServiceCategory::getCategoryName)
                .orElse("Category #" + categoryId);
    }

    private String normalizeCategoryName(String categoryName) {
        return switch (categoryName.toLowerCase()) {
            case "hvac" -> "AC services";
            case "general maintenance" -> "Cleaning";
            case "masonry" -> "Carpentry";
            default -> categoryName;
        };
    }
}
