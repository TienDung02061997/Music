package com.e.dungn.music;

import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button playeBtn;
    SeekBar positionBar;
    SeekBar volumerBar;
    TextView elapsedTimelabel;
    TextView remaininglabel;
    MediaPlayer mediaPlayer;
    int totlalTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playeBtn =(Button)findViewById(R.id.playBtn);
        elapsedTimelabel=(TextView) findViewById(R.id.elapsedTimelabel);
        remaininglabel =(TextView)findViewById(R.id.remaininglabel);

        //Media player
        mediaPlayer = MediaPlayer.create(this,R.raw.hoatau_psb4);
        //vong lap nhac
        mediaPlayer.setLooping(true);
        //Di chuyển phương tiện đến vị trí thời gian được chỉ định bằng cách xem xét chế độ đã cho
        // --co the la dieu chinh vi tri
        mediaPlayer.seekTo(0);
        //can bang am thanh dau ra gia tri thuong tu 0-1;
        // thu setStreamVolume
        mediaPlayer.setVolume(0.5f,0.5f);
        //getDuration Lấy thời lượng của tập tin.
        //thời lượng tính bằng mili giây, nếu không có thời lượng (ví dụ: nếu phát trực tiếp nội dung), -1 được trả về.
        totlalTime=mediaPlayer.getDuration();


        //position bar
        positionBar =(SeekBar)findViewById(R.id.positionBar);
        //tong thoi gian
        positionBar.setMax(totlalTime);
        positionBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            // khi di chuyen seekbar se co gia tri progress
            //onProgressChanged chỉ được thông báo cho lần chạm đầu tiên và lần chạm cuối cùng trên thanh tìm kiếm
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser){
                    mediaPlayer.seekTo(progress);
                    //Đặt tiến trình hiện tại thành giá trị được chỉ định.
                    positionBar.setProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //Volumer bar

        volumerBar =(SeekBar)findViewById(R.id.volumeBar);
        //setOnSeekBarChangeListener () được sử dụng để thêm các loại sự kiện khác nhau trên chuyển động của thanh tìm kiếm.
        // Ví dụ: bạn có thể dễ dàng thay
        // đổi giá trị số bằng cách di chuyển thanh tìm kiếm ở bên trái hoặc bên phải
        volumerBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float volumeNum =progress/100f;
                mediaPlayer.setVolume(volumeNum,volumeNum);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        //thread-luong (update  prositionbar and timelabel)
        //Runnable trang thai thuc hien tac vu
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mediaPlayer !=null) {
                    try {
                        Message message =new Message();
                        //what
                        //Mã tin nhắn do người dùng định nghĩa để người nhận có thể xác định nội dung của tin nhắn này.
                        //getCienPocation () - Nhận vị trí phát lại hiện tại trong một tệp âm thanh
                        message.what =mediaPlayer.getCurrentPosition();
                        handler.sendMessage(message);
                        Thread.sleep(1000);

                    }  catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }
    // cho phép bạn gửi và xử lý Messagevà các đối tượng Runnable được liên kết với một luồng
    //Mỗi phiên bản Handler được liên kết với một luồng và hàng đợi tin nhắn của luồng đó
    // (1) để lên lịch các tin nhắn và runnables sẽ được thực thi tại một thời điểm nào đó trong tương lai; và
    // (2) để tranh thủ một hành động được thực hiện trên một luồng khác với luồng của bạn.
    private Handler handler =new Handler(){
        @Override
        public void handleMessage(Message msg) {
            int CurrentPosition=msg.what;
            //cap nhat positionBar
            positionBar.setProgress(CurrentPosition);
            //update lablels
            String elapsedTime =createTimeLabel(CurrentPosition);
            elapsedTimelabel.setText(elapsedTime);

            String remaining =createTimeLabel(totlalTime-CurrentPosition);
            remaininglabel.setText("- "+remaining);
        }
    };
    // hien thi thoi gian ben trai
    public String createTimeLabel(int time){
        String timelabel="";
        int min=time/1000/60;
        int sec=time% 60;//chia lay du time
        timelabel =min + ":";
        if(sec<10)
            timelabel +="0";
        timelabel +=sec;
         return timelabel;
    }

    public void playBtnClick(View view) {
        //Kiểm tra xem MediaPlayer có đang phát không đúng nếu hiện đang chơi, sai khác
        if(!mediaPlayer.isPlaying()){
            //stop
            mediaPlayer.start();
            playeBtn.setBackgroundResource(R.drawable.stop);
        }
        else {
            mediaPlayer.pause();
            playeBtn.setBackgroundResource(R.drawable.play);
        }
    }
}
