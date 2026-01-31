import java.util.*;

public class RecommendationSystem {

    public static void main(String[] args) {
        // Sample data: users and their liked items
        Map<String, List<String>> userLikes = new HashMap<>();
        userLikes.put("Alice", Arrays.asList("Laptop", "Smartphone", "Headphones"));
        userLikes.put("Bob", Arrays.asList("Smartphone", "Camera", "Headphones"));
        userLikes.put("Charlie", Arrays.asList("Laptop", "Camera", "Smartwatch"));
        userLikes.put("David", Arrays.asList("Smartwatch", "Smartphone", "Laptop"));

        Scanner sc = new Scanner(System.in);
        System.out.println("Enter your name (Alice/Bob/Charlie/David): ");
        String user = sc.nextLine();

        // Check if user exists
        if (!userLikes.containsKey(user)) {
            System.out.println("User not found!");
            return;
        }

        List<String> likedItems = userLikes.get(user);

        // Recommendation logic:
        // Recommend items liked by other users but not yet liked by current user
        Set<String> recommended = new HashSet<>();
        for (Map.Entry<String, List<String>> entry : userLikes.entrySet()) {
            if (!entry.getKey().equals(user)) {
                for (String item : entry.getValue()) {
                    if (!likedItems.contains(item)) {
                        recommended.add(item);
                    }
                }
            }
        }

        // Display recommendations
        System.out.println("\n===== Recommendations for " + user + " =====");
        if (recommended.isEmpty()) {
            System.out.println("No new recommendations available!");
        } else {
            for (String item : recommended) {
                System.out.println("- " + item);
            }
        }
    }
}
