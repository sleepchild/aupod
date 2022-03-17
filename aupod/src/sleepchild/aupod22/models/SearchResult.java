package sleepchild.aupod22.models;

public class SearchResult
{
    public String title;
    public int type;// 0=title, 1=artist, 2=album
    public Object obj;
    public SearchResult(String title, int type, Object obj){
        this.title = title;
        this.type = type;
        this.obj = obj;
    }
}
