import java.util.*;

public interface SocialNetwork {

    /*
        OVERVIEW: un socialNetwork è un tipo di dato astratto che organizza e analizza post.
        Un post aggiunto al SocialNetwrk rappresenta un cambiamento di stato che dipende dal tipo del post così descritto:
        0- questo è un post sociale e comporta solo l'aggiunta del post nella lista dei post sociali.
        1- coporta la creazione di un utente con nome = post.author e descrizione = post.text, se l'utente esiste già allora
            ne modifica la descrizione
        3- comporta che l'utente post.author inizi a seguire post.text, se questi utenti esistono. se l'utente post.author
            segue già post.text allora il primo smetterà di seguire il secondo
    */

    //REQUIRES: p != null && p.getType = {0,1,3} &&
    //  p.getType = 0 -> containsUser(p.getAuthor)
    //  p.getType = 3 -> containsUser(p.getAuthor) && containsUser(p.getText) && p.getAuthor != p.getText
    //MODIFY: this
    //THROWS: PostException, NullPointerException
    //EFFECTS: p.getType = 0 -> aggiunge il Post al social network
    //  p.getType = 1 -> se p.getAuthor non esiste crea un utente con nome = p.getAuthor e descrizione = p.getText
    //                   altrimenti modifica la descrizione di p.getAuthor così: descrizione = p.getText
    //                   aggiunge il post al social network
    //  p.getType = 3 -> se p.getAuthor non segue p.getText lo inizia a seguire, altrimenti p.getAuthor smette di seguire p.getText
    //                  aggiunge il post al social network
    public void addPost(Post p) throws PostException;

    //REQUIRES: pl != null && PerOgni post nella lista deve valere:
    //    p != null && p.getType = {0,1,3} &&
    //    p.getType = 0 -> containsUser(p.getAuthor)
    //    p.getType = 3 -> containsUser(p.getAuthor) && containsUser(p.getText) && p.getAuthor != p.getText
    //MODIFY: this
    //THROWS: PostException, NullPointerException
    //EFFECTS: PerOgni post nella lista in ordine naturale addPost(post)
    public void addPostList(List<Post> pl) throws PostException;

    //REQUIRES: true
    //MODIFY: none
    //THROWS: none
    //EFFECTS: ritorna una lista di stringhe che contengono gli autori dei PostSociali contenuti in this, in ordine di
    //  aggiunta del post. ritorna una lista vuota se non ci sono post in this
    public List<String> getMentionedUser();

    //REQUIRES: username != null && containsUser(username)
    //MODIFY: none
    //THROWS: UserNotFoundException, NullPointerException
    //EFFECTS: ritorna tutti i post contenuti in this tale che p.getAuthor = username
    public List<Post> writtenBy(String username) throws UserNotFoundException;

    //REQUIRES: words != null
    //MODIFY: none
    //THROWS: NullPointerException
    //EFFECTS: ritorna la lista di tutti i PostSociali presenti in this che contengono almeno una parola contentuta
    // nella lista di parole passate come parametro
    public List<Post> containing(List<String> words);

    //REQUIRES: words != null
    //MODIFY: none
    //THROWS: NullPointerException
    //EFFECTS: ritorna la lista di tutti i Post presenti in this che contengono almeno una parola contentuta
    // nella lista di parole passate come parametro
    public List<Post> containingAll(List<String> words);

    //REQUIRES: true
    //MODIFY: none
    //THROWS: NullPointerException
    //EFFECTS: ritorna una lista contenente i primi maxInfluencers(variabile di istanza) nomi della lista
    //  di user presenti in this ordinata in modo decresscente rispetto alla cardinalità dell'insieme di utenti
    //  che lo seguono
    public List<String> influencers();

    //REQUIRES: i >= 1
    //MODIFY: this
    //THROWS: IllegalArgumentException
    //EFFECTS: maxInfluencers = i, variabile utilizzata in influencers()
    public void setMaxInfluencers(int i);

    //REQUIRES: s != null
    //MODIFY: none
    //THROWS: NullPointerException
    //EFFECTS: ritorna True se this contiene un Post identificato dall'id s, False altrimenti
    public boolean containsPost(String s);

    //REQUIRES: u != null
    //MODIFY: none
    //THROWS: NullPointerException
    //EFFECTS: ritorna True se this contiene un Utente identificato dal nome s, False altrimenti
    public boolean containsUser(String u);

    //REQUIRES: a != null && b != null && containsUsera(a) && containsUsera(b)
    //MODIFY: none
    //THROWS: NullPointerException, UserNotFoundException
    //EFFECTS: ritorna True se in this a segue b, False altrimenti
    public boolean AsegueB(String a, String b)  throws UserNotFoundException;

    //REQUIRES: true
    //MODIFY: none
    //THROWS: none
    //EFFECTS: ritorna una copia non modificabile della lista dei post sociali contenuti in this
    public List<Post> getAllSocialPost();

    //REQUIRES: true
    //MODIFY: none
    //THROWS: none
    //EFFECTS: ritorna una copia non modificabile della lista dei post non sociali(tipo 1,3) contenuti in this
    public List<Post> getAllNotSocialPost();

    //REQUIRES: true
    //MODIFY: none
    //THROWS: none
    //EFFECTS: ritorna una map non modificabile che mappa Stringhe(nomi di utent) a insiemi di stringe(nomi di utenti seguiti)
    public Map<String, Set<String>> getMicroblog();

    //REQUIRES: true
    //MODIFY: none
    //THROWS: none
    //EFFECTS: ritorna maxInfluencers, variabile utilizzata in influencers()
    public int getMaxInfluencers();
}
