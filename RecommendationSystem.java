import java.util.*;

public class AIRecommendationSystem {

    static Map<String, Map<String, Integer>> ratings = new HashMap<>();

    public static void main(String[] args) {

        // Training Data (User -> Item -> Rating)
        addUser("Alice", Map.of(
                "Laptop", 5,
                "Smartphone", 4,
                "Headphones", 3));

        addUser("Bob", Map.of(
                "Laptop", 4,
                "Smartphone", 5,
                "Camera", 4));

        addUser("Charlie", Map.of(
                "Laptop", 5,
                "Camera", 5,
                "Smartwatch", 4));

        addUser("David", Map.of(
                "Smartphone", 4,
                "Laptop", 3,
                "Smartwatch", 5));

        Scanner sc = new Scanner(System.in);
        System.out.print("Enter user name: ");
        String targetUser = sc.nextLine();

        if (!ratings.containsKey(targetUser)) {
            System.out.println("User not found!");
            return;
        }

        List<String> recommendations = recommend(targetUser);

        System.out.println("\n===== AI Recommendations for " + targetUser + " =====");

        if (recommendations.isEmpty()) {
            System.out.println("No recommendations available.");
        } else {
            for (String item : recommendations) {
                System.out.println("👉 " + item);
            }
        }
    }

    static void addUser(String name, Map<String, Integer> map) {
        ratings.put(name, new HashMap<>(map));
    }

    // ---------------- AI CORE ----------------

    static List<String> recommend(String user) {

        Map<String, Double> scores = new HashMap<>();
        Map<String, Double> similaritySum = new HashMap<>();

        for (String other : ratings.keySet()) {

            if (other.equals(user)) continue;

            double similarity = cosineSimilarity(
                    ratings.get(user),
                    ratings.get(other));

            if (similarity <= 0) continue;

            for (String item : ratings.get(other).keySet()) {

                // Recommend only unseen items
                if (!ratings.get(user).containsKey(item)) {

                    scores.put(item,
                            scores.getOrDefault(item, 0.0)
                                    + similarity * ratings.get(other).get(item));

                    similaritySum.put(item,
                            similaritySum.getOrDefault(item, 0.0)
                                    + similarity);
                }
            }
        }

        // Final predicted score
        Map<String, Double> predictions = new HashMap<>();

        for (String item : scores.keySet()) {
            predictions.put(item,
                    scores.get(item) / similaritySum.get(item));
        }

        // Sort highest first
        List<Map.Entry<String, Double>> list =
                new ArrayList<>(predictions.entrySet());

        list.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));

        List<String> result = new ArrayList<>();

        for (var e : list) {
            result.add(e.getKey());
        }

        return result;
    }

    // ---------------- MATH MODEL ----------------

    static double cosineSimilarity(
            Map<String, Integer> u1,
            Map<String, Integer> u2) {

        double dot = 0;
        double mag1 = 0;
        double mag2 = 0;

        for (String item : u1.keySet()) {

            int r1 = u1.get(item);
            mag1 += r1 * r1;

            if (u2.containsKey(item)) {
                int r2 = u2.get(item);
                dot += r1 * r2;
                mag2 += r2 * r2;
            }
        }

        if (dot == 0) return 0;

        return dot / (Math.sqrt(mag1) * Math.sqrt(mag2));
    }
}

