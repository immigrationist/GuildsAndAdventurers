import org.w3c.dom.ls.LSOutput;

import java.util.List;

public class Guild implements Comparable<Guild>{
    private String name;
    private List<Adventurer> adventurers;

    public Guild (String name, List<Adventurer> adventurers){
        setName(name);
        setAdventurers(adventurers);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Adventurer> getAdventurers() {
        return adventurers;
    }

    public void setAdventurers(List<Adventurer> adventurers) {
        this.adventurers = adventurers;
    }

    public String toString(){
        String info = "";
        info += "Guild Name: " + this.getName() + "\n";
        info += "Adventurers: " + this.getAdventurers() + "\n";
        return info;
    }

    @Override
    public int compareTo(Guild o) {
        return Integer.compare(this.adventurers.size(), o.adventurers.size());
    }
}
