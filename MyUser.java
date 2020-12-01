import java.util.*;

public class MyUser implements User{
    private final String name;
    private String description;
    private final LinkedList<Post> PostList = new LinkedList<Post>();

    /*
    Invariante: name != null && !name.isEmpty() && description != null && PostList != null
               PerOgni post appartenente a PostList post.getAuthor == name
    FunzioneAstratta: this.name = nome dell'utente
            this.description = descrizione associata all'utente
            this.PostList = lista di post che hanno come autore il nome dell'utente
    */

    //REQUIRES: name != null && !name.isEmpty() && descriptions != null
    //MODIFY: this.name, this.descriptions
    //THROWS: NullPointerException, IllegalArgumentException
    //EFFECTS: crea una istanza corretta di MyUser
    public MyUser(String name, String description){
        if(name == null) throw new NullPointerException("name == null");
        if(name.isEmpty()) throw  new IllegalArgumentException("name.isEmpty");
        this.name = name;

        if(description == null) throw new NullPointerException("description == null");
        this.description = description;
    }

    public void modifyDes(String s){
        if(description == null) throw new NullPointerException("description can't be == null");
        else description = s;
    }
    public void linkPost(Post p) throws PostNotAddedException {

        if(p == null) throw new NullPointerException("description can't be == null");
        if(!p.getAuthor().equals(name)) throw new PostNotAddedException("post.author != user.name");

        PostList.add(p);
    }

    public String getName() {
        return name;
    }
    public String getDes() {
        return description;
    }
    public List<Post> getLinkedPost(){
        return Collections.unmodifiableList(PostList);
    }

    @Override
    public String toString(){
        return "N: " + name + "\nD: " + description;
    }
    @Override
    public boolean equals(Object o){
        if (o instanceof MyUser) return (((User) o).getName().equals(name));
        else throw new IllegalArgumentException("The object is not instance of MyUser");
    }
}
