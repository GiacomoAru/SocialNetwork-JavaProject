import java.util.Calendar;

public class MyPost implements Post, Comparable<MyPost>{

    private final String id;
    private final String author;
    private final String text;
    private final Calendar timestamp;
    private final int tipoDiPost;

    /*
    Invariante: (author, timestamp) != null && != empty
                text != null && text.length <= 140
                id = "" + tipoDiPost + "." + timestamp.getTimeInMills + "." + author
    FunzioneAstratta: this.author  = autore del post
            this.text = contenuto del testo
            this.tipoDiPost = tipo del post
            this.timestamp = data di pubblicazione
            this.id = stringa che definice univocamente il post.
    */

    //REQUIRES: author != null && author != ""
    //         text != null && text.length <= 140
    //MODIFY: this.text, this.author, this.id, this.timestamp, this.tipoDiPost
    //THROWS: NullPointerException, IllegalArgumentException
    //EFFECTS: genera una nuova istanza corretta di MyPost
    public MyPost(String author, int tipoDiPost, String text){

        if(author == null) throw new NullPointerException("author == null");
        if(author.isEmpty()) throw  new IllegalArgumentException("author.isEmpty == true");
        this.author = author;

        this.tipoDiPost = tipoDiPost;

        if(text == null) throw new NullPointerException("text == null");
        if(text.length() > 140) throw  new IllegalArgumentException("text.length > 140");
        this.text = text;

        timestamp = Calendar.getInstance();
        id = tipoDiPost + "." + timestamp.getTimeInMillis() + "." +  author;
    }

    public String getAuthor() {
        return author;
    }
    public int getType(){
        return tipoDiPost;
    }
    public String getId() {
        return id;
    }
    public Calendar getTimeStamp() {
        return (Calendar) timestamp.clone();
    }
    public long getTimestampInMills() {
        return timestamp.getTimeInMillis();
    }
    public String getText() {
        return text;
    }

    @Override
    public String toString(){
        return  tipoDiPost + "\n" + author + "\n" + timestamp.getTime() + "\n" + text;
    }
    @Override
    public boolean equals(Object o) {
        if (o instanceof MyPost) return (((MyPost) o).getId().equals(id));
        else throw new IllegalArgumentException("The object is not instance of MyPoster");
    }
    @Override
    public int compareTo(MyPost o) {
        //deve creare un ordinamento totale, se non sono uguali devono essere o a minore di b o il contrario
        int dummy = (int) (this.getTimestampInMills() - o.getTimestampInMills());

        if(dummy == 0){
            dummy =  this.getType() - o.getType();
        }
        if(dummy == 0){
            dummy =  this.getAuthor().compareTo(o.getAuthor());
        }

        return dummy;
    }
}