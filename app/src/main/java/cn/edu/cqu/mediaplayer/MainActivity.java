package cn.edu.cqu.mediaplayer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements OnClickListener{

    private Button bPlay;
    private Button bStop;
    private Button bNext;
    private ListView songlist;
    private String strChooedUrl;
    private Player myPlayer;
    private SeekBar seekbar;
    private boolean isPlaying;
    private  int iPositionNum = (-1);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bPlay = (Button)findViewById(R.id.btnPlay);
        bNext = (Button)findViewById(R.id.btnNext);
        bStop = (Button)findViewById(R.id.btnStop);
        songlist = (ListView) findViewById(R.id.listSongs);
        isPlaying = false;


        //设置播放进度条
        seekbar = (SeekBar) findViewById(R.id.seekBar);
        myPlayer = new Player(seekbar);
        seekbar.setOnSeekBarChangeListener(new SeekBarChangeEvent());


        //填充ListView
        String[] songs =getResources().getStringArray(R.array.songlist);
        final ArrayAdapter adapter_song=new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1,songs);
        songlist.setAdapter(adapter_song);
        //设置ListView点击响应事件
        songlist.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                iPositionNum = position;
                strChooedUrl=String.valueOf(adapter_song.getItem(iPositionNum));
                strChooedUrl="http://mpianatra.com/Courses/files/"+strChooedUrl+".mp3";
                myPlayer.playUrl(strChooedUrl);

                Toast.makeText(getBaseContext(), adapter_song.getItem(iPositionNum).toString()+" Is Selcted", Toast.LENGTH_SHORT).show();
                myPlayer.play();
                bPlay.setBackgroundResource(R.mipmap.pause);
                isPlaying = true;
            }
        });
    }

    //按钮点击事件相应
    public void onClick(View v){
        switch (v.getId()) {
            case R.id.btnPlay:
                    if(isPlaying==false) {
                        Toast.makeText(getBaseContext(), "Play Pressed", Toast.LENGTH_SHORT).show();
                        myPlayer.play();
                        bPlay.setBackgroundResource(R.mipmap.pause);
                        isPlaying = true;
                    }
                    else{
                        Toast.makeText(getBaseContext(), "Pause Pressed", Toast.LENGTH_SHORT).show();
                        myPlayer.pause();
                        bPlay.setBackgroundResource(R.mipmap.start);
                        isPlaying = false;
                    }
                break;
            case R.id.btnNext:
                Toast.makeText(getBaseContext(), "Next Pressed", Toast.LENGTH_SHORT).show();
                iPositionNum ++;
                myPlayer.play();//开始播放下一首
                bPlay.setBackgroundResource(R.mipmap.pause);
                isPlaying = true;
                break;
            case R.id.btnStop:
                Toast.makeText(getBaseContext(), "Stop Pressed", Toast.LENGTH_SHORT).show();
                myPlayer.stop();
                isPlaying = true;
                bPlay.setBackgroundResource(R.mipmap.start);
                break;
        }
    }
    // 进度改变
    class SeekBarChangeEvent implements OnSeekBarChangeListener{
        int progress;

        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
            // 原本是(progress/seekBar.getMax())*player.mediaPlayer.getDuration()
            this.progress = progress * myPlayer.mediaPlayer.getDuration()
                    / seekBar.getMax();
        }

        public void onStartTrackingTouch(SeekBar seekBar) {

        }
        public void onStopTrackingTouch(SeekBar seekBar) {
            // seekTo()的参数是相对与影片时间的数字，而不是与seekBar.getMax()相对的数字
            myPlayer.mediaPlayer.seekTo(progress);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (myPlayer != null) {
            myPlayer.stop();
            myPlayer = null;
        }
    }

}
