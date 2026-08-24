package gh.edu.ug.cs.ugmaintenance.services;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import gh.edu.ug.cs.ugmaintenance.datastructures.linkedlist.List;
import gh.edu.ug.cs.ugmaintenance.models.Location;
import gh.edu.ug.cs.ugmaintenance.models.ServiceCategory;
import gh.edu.ug.cs.ugmaintenance.repositories.LocationRepository;
import gh.edu.ug.cs.ugmaintenance.repositories.ServiceCategoryRepository;

public class LookupService {

    private final LocationRepository locationRepository;
    private final ServiceCategoryRepository categoryRepository;

    private static final Map<Integer, String> LOCATION_ID_TO_NAME = new ConcurrentHashMap<>();
    private static final Map<String, Integer> LOCATION_NAME_TO_ID = new ConcurrentHashMap<>();
    private static final Map<Integer, String> CATEGORY_ID_TO_NAME = new ConcurrentHashMap<>();
    private static final Map<String, Integer> CATEGORY_NAME_TO_ID = new ConcurrentHashMap<>();
    private static volatile boolean initialized = false;

    public LookupService() {
        this.locationRepository = new LocationRepository();
        this.categoryRepository = new ServiceCategoryRepository();
        ensureCacheLoaded();
    }

    private synchronized void ensureCacheLoaded() {
        if (initialized) {
            return;
        }
        try {
            List<Location> locations = locationRepository.findAll();
            for (int i = 0; i < locations.size(); i++) {
                Location loc = locations.get(i);
                LOCATION_ID_TO_NAME.put(loc.getLocationId(), loc.getLocationName());
                LOCATION_NAME_TO_ID.put(loc.getLocationName().trim().toLowerCase(), loc.getLocationId());
            }

            List<ServiceCategory> categories = categoryRepository.findAll();
            for (int i = 0; i < categories.size(); i++) {
                ServiceCategory cat = categories.get(i);
                CATEGORY_ID_TO_NAME.put(cat.getCategoryId(), cat.getCategoryName());
                CATEGORY_NAME_TO_ID.put(cat.getCategoryName().trim().toLowerCase(), cat.getCategoryId());
            }
            initialized = true;
        } catch (Exception e) {
            // Fall back to on-demand queries if DB is offline during unit tests
        }
    }

    public int resolveLocationId(String locationName) {
        if (locationName == null || locationName.isBlank()) {
            throw new IllegalArgumentException("Location is required.");
        }

        String key = locationName.trim().toLowerCase();
        Integer cachedId = LOCATION_NAME_TO_ID.get(key);
        if (cachedId != null) {
            return cachedId;
        }

        Optional<Location> location = locationRepository.findByName(locationName.trim());
        if (location.isEmpty()) {
            throw new IllegalArgumentException(
                    "Unknown location: " + locationName
            );
        }

        Location loc = location.get();
        LOCATION_ID_TO_NAME.put(loc.getLocationId(), loc.getLocationName());
        LOCATION_NAME_TO_ID.put(key, loc.getLocationId());
        return loc.getLocationId();
    }

    public int resolveCategoryId(String categoryName) {
        if (categoryName == null || categoryName.isBlank()) {
            throw new IllegalArgumentException("Category is required.");
        }

        String normalized = normalizeCategoryName(categoryName.trim());
        String key = normalized.toLowerCase();
        Integer cachedId = CATEGORY_NAME_TO_ID.get(key);
        if (cachedId != null) {
            return cachedId;
        }

        Optional<ServiceCategory> category = categoryRepository.findByName(normalized);
        if (category.isEmpty()) {
            throw new IllegalArgumentException(
                    "Unknown service category: " + categoryName
            );
        }

        ServiceCategory cat = category.get();
        CATEGORY_ID_TO_NAME.put(cat.getCategoryId(), cat.getCategoryName());
        CATEGORY_NAME_TO_ID.put(key, cat.getCategoryId());
        return cat.getCategoryId();
    }

    public String getLocationName(int locationId) {
        String cached = LOCATION_ID_TO_NAME.get(locationId);
        if (cached != null) {
            return cached;
        }
        return locationRepository.findById(locationId)
                .map(Location::getLocationName)
                .orElse("Location #" + locationId);
    }

    public String getCategoryName(int categoryId) {
        String cached = CATEGORY_ID_TO_NAME.get(categoryId);
        if (cached != null) {
            return cached;
        }
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
