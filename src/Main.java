import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException {

        List<Guild> guilds = initializeGuilds();
        guilds.forEach(System.out::println);

        System.out.println("Filter by skill");
        System.out.println(filterBySkill(Skill.MEMECRAFTING, guilds));

        System.out.println("\nTop '1' Wealthiest Adventurers Per Guild");
        System.out.println(identifyNWealthiestAdventurer(1, guilds));

        System.out.println("\nAverage gold by skill Swordsmanship");
        System.out.println(averageGoldEarnedBySpecificSkill(guilds));

        System.out.println("\nAdventurers grouped by role");
        System.out.println(groupAdventurersByRole(guilds));

        System.out.println("\nMost versatile guild by role");
        System.out.println(findMostVersatileGuild(guilds));

        System.out.println("\nCustom format adventurers list");
        System.out.println(formatAdventurerNames(guilds));

        System.out.println("\nAdventurer with most skills");
        System.out.println(findAdventurerWithMostSkills(guilds));

        System.out.println("\nGuilds by average age in ascending order");
        Main rank = new Main();
        rank.rankGuildsByAverageAge(guilds);

        System.out.println("\nSkill wise adventurer count");
        System.out.println(createSkillWiseAdventurerCount(guilds));

        System.out.println("\nBonus gold earned");
        System.out.println(bonusGoldEarned(guilds));
    }

    private static List<Guild> initializeGuilds() {
        List<Guild> guilds = new ArrayList<>();

        // DragonSlayers Guild
        List<Adventurer> dragonSlayers = new ArrayList<>();
        dragonSlayers.add(new Adventurer("Legolas", 23, "Archer", 1200, List.of(Skill.ARCHERY, Skill.SWORDSMANSHIP)));
        dragonSlayers.add(new Adventurer("Aragorn", 35, "Warrior", 2000, List.of(Skill.SWORDSMANSHIP, Skill.THIEVERY)));
        dragonSlayers.add(new Adventurer("Frodo", 20, "Scout", 800, List.of(Skill.STEALTH, Skill.RUNECRAFTING)));
        guilds.add(new Guild("DragonSlayers", dragonSlayers));

        // Barbarians Guild
        List<Adventurer> barbarians = new ArrayList<>();
        barbarians.add(new Adventurer("Ragnar", 30, "Raider", 1800, List.of(Skill.MEMECRAFTING, Skill.HORSEMANSHIP)));
        barbarians.add(new Adventurer("Floki", 35, "Engineer", 1500, List.of(Skill.MEMECRAFTING, Skill.HEALING)));
        barbarians.add(new Adventurer("Lagertha", 32, "Warrior", 2100, List.of(Skill.SWORDSMANSHIP, Skill.STEALTH)));
        guilds.add(new Guild("Barbarians", barbarians));

        // KnightsWatch Guild
        List<Adventurer> knightsWatch = new ArrayList<>();
        knightsWatch.add(new Adventurer("JonSnow", 25, "Leader", 2500, List.of(Skill.HEALING, Skill.HORSEMANSHIP)));
        knightsWatch.add(new Adventurer("SamTarly", 24, "Scholar", 800, List.of(Skill.BLACKSMITHING, Skill.STEALTH, Skill.NECROMANCY)));
        knightsWatch.add(new Adventurer("Brienne", 32, "Knight", 2200, List.of(Skill.MEMECRAFTING, Skill.SWORDSMANSHIP, Skill.ARCHERY)));
        guilds.add(new Guild("KnightsWatch", knightsWatch));

        // ShadowClan Guild
        List<Adventurer> shadowClan = new ArrayList<>();
        shadowClan.add(new Adventurer("Altair", 30, "Assassin", 2000, List.of(Skill.STEALTH, Skill.SWORDSMANSHIP)));
        shadowClan.add(new Adventurer("Ezio", 28, "Strategist", 1900, List.of(Skill.BLACKSMITHING, Skill.NECROMANCY)));
        shadowClan.add(new Adventurer("Cassandra", 27, "Rogue", 1500, List.of(Skill.STEALTH, Skill.ARCHERY)));
        guilds.add(new Guild("ShadowClan", shadowClan));

        return guilds;
    }

    public static List<Adventurer> filterBySkill(Skill skill, List<Guild> guilds) {
        return guilds.stream()
                // combining all adventurers into one stream
                .flatMap(guild -> guild.getAdventurers().stream())
                .filter(adventurer -> adventurer.getSkills().contains(skill))
                .toList();
    }

    public static Map<String, List<Adventurer>> identifyNWealthiestAdventurer(int n, List<Guild> guilds) {
        return guilds.stream()
                // grouping adventurers by guild
                .collect(Collectors.toMap(
                        Guild::getName,
                        guild -> guild.getAdventurers().stream()
                                .sorted(Comparator.comparing(Adventurer::getGoldEarned).reversed())
                                .limit(n)
                                .collect(Collectors.toList())));
    }

    public static double averageGoldEarnedBySpecificSkill(List<Guild> guilds) {
        return guilds.stream()
                .flatMap(guild -> guild.getAdventurers().stream())
                .filter(adventurer -> adventurer.getSkills().contains(Skill.SWORDSMANSHIP))
                .mapToDouble(Adventurer::getGoldEarned)
                .average()
                .orElse(0.0);
    }

    // map because it organizes adventurers (List) by role (String)
    public static Map<String, List<Adventurer>> groupAdventurersByRole(List<Guild> guilds) {
        return guilds.stream()
                .flatMap(guild -> guild.getAdventurers().stream())
                .collect(Collectors.groupingBy(Adventurer::getRole));
    }

    public static Guild findMostVersatileGuild(List<Guild> guilds) {
        return guilds.stream()
                .max(Comparator.comparing(guild -> guild.getAdventurers().stream()
                        .flatMap(adventurer -> adventurer.getSkills().stream())
                        .distinct()
                        .count()
                ))
                .orElse(null);
    }

    public static List<String> formatAdventurerNames(List<Guild> guilds) {
        // Name (Role) - Master of Skill1, Skill2, ...
        return guilds.stream()
                .flatMap(guild -> guild.getAdventurers().stream())
                .map(adventurer -> "\n" + adventurer.getName() + " (" +
                        adventurer.getRole() + ") " + " - Master of " + adventurer.getSkills().stream()
                        .map(Skill::name)//converting enum to string
                        .collect(Collectors.joining(", "))) // joining skills with commas
                .toList();

    }

    public static Adventurer findAdventurerWithMostSkills(List<Guild> guilds) {
        return guilds.stream()
                .flatMap(guild -> guild.getAdventurers().stream())
                .max(Comparator.comparing(adventurer -> adventurer.getSkills().size()))
                .orElse(null);
    }

    public void rankGuildsByAverageAge(List<Guild> guilds) {
          guilds.stream()
                .sorted(Comparator.comparingDouble(guild -> guild.getAdventurers().stream()
                        .mapToInt(Adventurer::getAge)
                        .average()
                        .orElse(0.0)))
                .forEach(guild -> System.out.println(guild.getName() + " - Average Age: " +
                        guild.getAdventurers().stream()
                                .mapToInt(Adventurer::getAge)
                                .average()
                                .orElse(0.0)));
    }

    public static Map<Skill, Long> createSkillWiseAdventurerCount(List<Guild> guilds){
        return guilds.stream()
                .flatMap(guild -> guild.getAdventurers().stream())
                .flatMap(adventurer -> adventurer.getSkills().stream())
                .collect(Collectors.groupingBy(skill -> skill, Collectors.counting()));

    }

    public static List<Adventurer> bonusGoldEarned(List<Guild> guilds){
        return guilds.stream()
                .flatMap(guild -> guild.getAdventurers().stream())
                .filter(adventurer -> adventurer.getGoldEarned() < 1000)
                .peek(adventurer -> adventurer.setGoldEarned(adventurer.getGoldEarned() + adventurer.getGoldEarned() * 0.2))
                .collect(Collectors.toList());
    }
}