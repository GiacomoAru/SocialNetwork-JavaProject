import java.util.List;

public interface User {
    /*
    Overview: un User è un tipo di dato astratto mutabile che rappresenta un utente di un social network. Esso ha una nome,
                una descrizionen e un insieme di post associati
     */

    //REQUIRES: s != null
    //MODIFY: this
    //THROWS: NullPointerException
    //EFFECTS: description = s
    public void modifyDes(String s);

    //REQUIRES: p != null && p.author == getName()
    //MODIFY: this
    //THROWS: NullPointerException, postNotAddedException
    //EFFECTS: Aggiunge un post alla lista dei post
    public void linkPost(Post p) throws PostNotAddedException;

    //REQUIRES: true
    //MODIFY: none
    //THROWS: none
    //EFFECTS: ritorna una stringa equivalente al nome dell'utente
    public String getName();

    //REQUIRES: true
    //MODIFY: none
    //THROWS: none
    //EFFECTS: ritorna una stringa equivalente alla descrizione dell'utente
    public String getDes();

    //REQUIRES: true
    //MODIFY: none
    //THROWS: none
    //EFFECTS: ritorna una lista non modificabile, clone della lista di post associata all'utente
    public List<Post> getLinkedPost();

    //REQUIRES: true
    //MODIFY: none
    //THROWS: none
    //EFFECTS: ritorna una Stringa rappresentante l'istanza si User
    public String toString();

    //REQUIRES: o istanceof User
    //MODIFY: none
    //THROWS: illegalArgumentException
    //EFFECTS: ritorna True se this.author == o.author, False altrimenti
    public boolean equals(Object o);
}
