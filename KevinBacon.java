import java.io.*;
import java.util.*;

public class KevinBacon {

    private Map<String, Set<String>> actorToMovies;
    private Map<String, Set<String>> movieToActors;
    private Set<String> allActors;

    private Map<String, Integer> distances;
    private Map<String, String> parent;
    private Map<String, String> parentMovie;
    private String sourceActor;

    public KevinBacon() {
        actorToMovies = new HashMap<>();
        movieToActors = new HashMap<>();
        allActors = new HashSet<>();
        distances = new HashMap<>();
        parent = new HashMap<>();
        parentMovie = new HashMap<>();
    }

    public void loadMoviesFile(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split("/");
                if (parts.length < 2) continue;

                String movie = parts[0].trim();
                Set<String> actors = new HashSet<>();

                for (int i = 1; i < parts.length; i++) {
                    String actor = parts[i].trim();
                    if (!actor.isEmpty()) {
                        actors.add(actor);
                        allActors.add(actor);
                        actorToMovies.putIfAbsent(actor, new HashSet<>());
                        actorToMovies.get(actor).add(movie);
                    }
                }

                if (!actors.isEmpty()) {
                    movieToActors.put(movie, actors);
                }
            }

            System.out.println("Loaded " + movieToActors.size() + " movies");
            System.out.println("Found " + allActors.size() + " actors");

        } catch (IOException e) {
            System.err.println("Error reading movies file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void calculateBaconNumbers(String source) {
        this.sourceActor = source;
        distances.clear();
        parent.clear();
        parentMovie.clear();

        if (!allActors.contains(source)) {
            System.err.println("Error: Actor '" + source + "' not found");
            return;
        }

        Queue<String> queue = new LinkedList<>();
        queue.add(source);
        distances.put(source, 0);

        while (!queue.isEmpty()) {
            String currentActor = queue.poll();
            int currentDistance = distances.get(currentActor);

            Set<String> movies = actorToMovies.getOrDefault(currentActor, Collections.emptySet());

            for (String movie : movies) {
                Set<String> coActors = movieToActors.getOrDefault(movie, Collections.emptySet());

                for (String coActor : coActors) {
                    if (!distances.containsKey(coActor)) {
                        distances.put(coActor, currentDistance + 1);
                        parent.put(coActor, currentActor);
                        parentMovie.put(coActor, movie);
                        queue.add(coActor);
                    }
                }
            }
        }
    }

    public void printHistogram() {
        Map<Integer, Integer> histogram = new TreeMap<>();

        for (String actor : allActors) {
            int baconNumber = distances.getOrDefault(actor, -1);
            if (baconNumber == -1) {
                histogram.put(Integer.MAX_VALUE, histogram.getOrDefault(Integer.MAX_VALUE, 0) + 1);
            } else {
                histogram.put(baconNumber, histogram.getOrDefault(baconNumber, 0) + 1);
            }
        }

        System.out.println("\nBacon number         Frequency");
        System.out.println("--------------------------------------------------");

        for (Map.Entry<Integer, Integer> entry : histogram.entrySet()) {
            if (entry.getKey() == Integer.MAX_VALUE) {
                System.out.printf("%-20s %d%n", "infinity", entry.getValue());
            } else {
                System.out.printf("%-20d %d%n", entry.getKey(), entry.getValue());
            }
        }
    }

    public void printPath(String targetActor) {
        if (!allActors.contains(targetActor)) {
            System.out.println(targetActor + " not found in database");
            System.out.println();
            return;
        }

        if (!distances.containsKey(targetActor)) {
            System.out.println(targetActor + " has a Bacon number of infinity (not connected)");
            System.out.println();
            return;
        }

        int baconNumber = distances.get(targetActor);
        System.out.println(targetActor + " has a Bacon number of " + baconNumber);

        if (baconNumber == 0) {
            System.out.println();
            return;
        }

        String current = targetActor;

        while (parent.containsKey(current)) {
            String nextActor = parent.get(current);
            String movie = parentMovie.get(current);
            System.out.println(current + " was in \"" + movie + "\" with " + nextActor);
            current = nextActor;
        }

        System.out.println();
    }

    public void processTestFile(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            System.out.println("\n=== Detailed Chains for Queries ===\n");

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                printPath(line);
            }

        } catch (IOException e) {
            System.err.println("Error reading test file: " + e.getMessage());
        }
    }

    public static List<String> readTestActors(String filename) {
    List<String> actors = new ArrayList<>();

    try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
        String line;
        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (!line.isEmpty()) {
                actors.add(line);
            }
        }
    } catch (IOException e) {
        System.err.println("Error reading test file: " + e.getMessage());
    }

    return actors;
}

    public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    KevinBacon kb = new KevinBacon();

    String moviesFile = "movies.txt";
    String testFile = "test-sample.txt";
    System.out.println("Loading movies from: " + moviesFile);
    kb.loadMoviesFile(moviesFile);

    if (kb.allActors.isEmpty()) {
        System.err.println("No actors found in database. Exiting.");
        scanner.close();
        return;
    }

    List<String> testActors = readTestActors(testFile);

    if (testActors.isEmpty()) {
        System.err.println("Test file is empty or missing. Exiting.");
        scanner.close();
        return;
    }

    System.out.println("\nActors available for selection (from test-sample.txt):");
    for (String actor : testActors) {
        System.out.println(" - " + actor);
    }

    String sourceActor;
    while (true) {
        System.out.print("\nEnter source actor name from the list above: ");
        sourceActor = scanner.nextLine().trim();

        if (testActors.contains(sourceActor)) {
            break;
        }
        System.out.println("Invalid name. Please choose an actor from the list.");
    }

    System.out.println("\nCalculating distances from: " + sourceActor);
    kb.calculateBaconNumbers(sourceActor);

    kb.printHistogram();

    File file = new File(testFile);
    if (file.exists() && file.isFile()) {
        System.out.println("\nProcessing test file: " + testFile);
        kb.processTestFile(testFile);
    }

    scanner.close();
    System.out.println("Done!");
}


}
