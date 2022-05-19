package sleepchild.aupod22.models;

public class SearchResult
{
    public String title;
    public int type;// 0=title, 1=artist, 2=album
    public SongItem si;
    //public SongItem si;
    
    public SearchResult(String title, int type, SongItem obj){
        this.title = title;
        this.type = type;
        this.si = obj;
    }
}
