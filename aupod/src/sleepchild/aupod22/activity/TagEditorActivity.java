package sleepchild.aupod22.activity;

import android.content.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import com.mpatric.mp3agic.*;
import java.io.*;
import java.util.*;
import sleepchild.aupod22.*;
import sleepchild.aupod22.models.*;
import sleepchild.postman.*;
import sleepchild.aupod22.models.events.*;
import sleepchild.aupod22.library.*;

public class TagEditorActivity extends BaseActivity{

    private static SongItem xsongItem=null;

    private EditText etTitle, etArtist, etAlbum, etYear;
    View art;
    public static final String EPATH="Sleep.tagedit.act.path";
    String mPath;
    Mp3File mp3;
    ID3v2 v2;
    private SongItem mSongItem;
    byte[] newArt;
    String newArtType="image/*";

    @Override
    public void onCreate(Bundle si){
        super.onCreate(si);
        setContentView(R.layout.activity_tageditor);
        //
        if(xsongItem!=null){
            mSongItem = xsongItem;
            xsongItem = null;
            init();
        }
        else{
            toast("no data.");
        }
    }

    public static void start(Context ctx,SongItem si){
        TagEditorActivity.xsongItem = si;
        ctx.startActivity(new Intent(ctx, TagEditorActivity.class));
    }

    private void init(){
        if(mSongItem==null){
            //setContentView(error);
            return;
        }
        etTitle = (EditText) findViewById(R.id.activity_tageditor_title);
        etArtist = (EditText) findViewById(R.id.activity_tageditor_artist);
        etAlbum = (EditText) findViewById(R.id.activity_tageditor_album);
        etYear = (EditText) findViewById(R.id.activity_tageditor_year);
        art = findViewById(R.id.tageditor_art);
        //*
        mPath = mSongItem.path;
        if(mPath!=null && !mPath.isEmpty()){
            try{
                mp3 = new Mp3File(mPath);
                v2 = mp3.getId3v2Tag();
                if(v2!=null){
                    String t = nn(v2.getTitle());
                    if(t.isEmpty()){
                        t = mSongItem.title;
                    }
                    etTitle.setText(t);
                    etArtist.setText(nn(v2.getArtist()));
                    etAlbum.setText(nn(v2.getAlbum()));

                    byte[] image = v2.getAlbumImage();
                    if(image!=null && image.length>3){// why 3?
                        art.setBackground( new BitmapDrawable(getResources(), BitmapFactory.decodeByteArray(image,0,image.length)));
                        
                    }

                }

            }
            catch (InvalidDataException e){
                toast("InvalidDataException: "+e.getDetailedMessage());
                finish();
            }
            catch (IOException e){
                toast("IOException: "+e.getMessage());
                finish();
            }
            catch (UnsupportedTagException e){
                toast("UnsupportedTagException: "+e.getMessage());
                finish();
            }
        }
        //*/
    }

    // never null
    // return str elif str == null return empty string ""
    String nn(String str){
        if(str==null){
            return "";
        }
        return str;
    }

    int pikerRequestCode = 826384;
    void getNewArt(){
        if(v2==null){
            if(mp3!=null){
                if(mp3.hasId3v2Tag()){
                    v2 = mp3.getId3v2Tag();
                }else{
                    v2 = new ID3v24Tag();
                }
            }
        }
        if(v2!=null){
            Intent piker = new Intent(Intent.ACTION_GET_CONTENT);
            piker.setType("image/*");
            startActivityForResult(piker, pikerRequestCode);
        }
    }

    String artPath =null;
    public void setArt(Intent data){
        if(data!=null){
            try{
                artPath = data.getData().getPath();
                InputStream ins = getContentResolver().openInputStream(data.getData());
                byte[] buff = new byte[ins.available()];
                ins.read(buff);
                Bitmap a = BitmapFactory.decodeByteArray(buff,0,buff.length);
                //Bitmap a = BitmapFactory.decodeStream(ins);
                art.setBackground(new BitmapDrawable(getResources(), a));
                if(buff!=null){
                    newArt = buff;
                    newArtType = getContentResolver().getType(data.getData());
                }
            }
            catch (FileNotFoundException e)
            {}catch(IOException e){}
        }  
    }

    private boolean saveData(){
        try{
            if(mp3 == null){
                try{
                    mp3 = new Mp3File(mPath); 
                }catch(Exception e){
                    //
                }
                if(mp3==null){
                    toast("Failed to Edit: Not an mp3 file!");
                    return false;
                }
            }
            if(v2==null){
                if(mp3.hasId3v2Tag()){
                    v2 = mp3.getId3v2Tag();
                }else{
                    v2 = new ID3v24Tag();
                }
            }
            //title
            String t = etTitle.getText().toString();
            v2.setTitle(t);
            mSongItem.title = t;
            // artist
            String ar = etArtist.getText().toString();
            v2.setArtist(ar);
            mSongItem.artist = ar;
            //album
            String al = etAlbum.getText().toString();
            v2.setAlbum(al);
            mSongItem.album = al;
            //year
            String yr = etYear.getText().toString();
            v2.setYear(yr);
            // album art
            if(newArt!=null){
                v2.setAlbumImage(newArt, newArtType);
            }
            //v2.setAlbumImage(new byte[10]," ");
            //
            mp3.setId3v2Tag(v2);
            try
            {
                if(new File(mp3.getFilename()).exists()){

                }
                String fn = mPath.substring(0,mPath.lastIndexOf("."));
                mp3.save(fn);
                new File(mPath).delete();
                new File(fn).renameTo(new File(mPath));
                MediaStoreUpdator.update(ctx,mSongItem, artPath);
                PostMan.getInstance().post(new SongInfoChangedEvent(mSongItem));
                return true;
            }
            catch (IOException e)
            {}
            catch (NotSupportedException e)
            {}
        }catch(Exception e){
            toast(e.getMessage());
        }
        return false;
    }


    public void onButton(View v){
        int id = v.getId();
        switch(id){
            case R.id.tageditor_art:
                getNewArt();
                break;
            case R.id.activity_tageditor_btn_save:
                if(saveData()){
                    finish();
                }
                break;
            case R.id.activity_tageditor_exit:
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode==pikerRequestCode){
            setArt(data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }



}
