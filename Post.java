import java.util.Calendar;

public interface Post {

    /*Overview: un post è un tipo di dato astratto non modificabile, che rappresenta una transizione di stato
    di un SocialNetwork.
    Contiene un Autore(stringa), un testo(stringa di max 140 caratteri), un tipo (int), una data di creazione (calendar)
    e un id univoco (stringa)
     */

    //REQUIRES: true
    //MODIFY: none
    //THROWS: none
    //EFFECTS: ritorna l'autore del post
    public String getAuthor();

    //REQUIRES: true
    //MODIFY: none
    //THROWS: none
    //EFFECTS: return the type of the post
    public int getType();

    //REQUIRES: true
    //MODIFY: none
    //THROWS: none
    //EFFECTS: ritorna l'id del post
    public String getId();

    //REQUIRES: true
    //MODIFY: none
    //THROWS: none
    //EFFECTS: ritorna un clone del calendar associato alla data di pubblicazione
    public Calendar getTimeStamp();

    //REQUIRES: true
    //MODIFY: none
    //THROWS: none
    //EFFECTS: ritorna la differenza di millisecondi tra il
    public long getTimestampInMills();

    //REQUIRES: true
    //MODIFY: none
    //THROWS: none
    //EFFECTS: ritorna usa stringa equivalente al testo del post
    public String getText();

    //REQUIRES: true
    //MODIFY: none
    //THROWS: none
    //EFFECTS: ritorna una stringa rappresentante l'istanza del tipo di dato astratto
    public String toString();

    //REQUIRES: o instanceof MyPost
    //MODIFY: none
    //THROWS: IllegalArgumentException
    //EFFECTS: ritorna True se .getId == this.getId , False altrimenti
    public boolean equals(Object o);

    //REQUIRES: o instanceof MyPost
    //MODIFY: none
    //THROWS: IllegalArgumentException
    //EFFECTS: ritorna un intero <0 se this < o, =0 se this == o, >0 se this > o
    public int compareTo(MyPost o);
}
