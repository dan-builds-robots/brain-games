package dan.ajayi.braingames;

import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;

import android.os.CountDownTimer;

import android.view.View;

import android.widget.Button;

import android.widget.ImageView;

import android.widget.TextView;

import android.widget.Toast;

import dan.ajayi.braingames.R;

import java.util.ArrayList;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    ImageView playButton;

    ImageView logo;

    ImageView signature;

    TextView highScoreText;

    TextView question;

    TextView score;

    TextView timer;

    ImageView hourglass;

    ImageView menu;

    ImageView sfxBtn;

    ImageView musicBtn;

    ImageView homeBtn;

    ImageView sfxX;

    ImageView musicX;

    ImageView settingsBtn;

    Button button1;

    Button button2;

    Button button3;

    Button button4;

    GridLayout buttonLayout;

    int numCorrect;

    int totalQuestions;

    int a;

    int b;

    int randOperation;

    String operation;

    ArrayList<String> operations = new ArrayList();

    ArrayList<Integer> answers = new ArrayList();

    int correctAnswerLocation;

    int correctAnswer;

    ArrayList<Button> buttons = new ArrayList();

    Random rand = new Random();

    int secsLeft = 30;

    ArrayList<Integer> visibility = new ArrayList();

    SharedPreferences prefs;

    int highScore;

    boolean musicMuted;

    boolean sfxMuted;

    MediaPlayer correctSfx;

    MediaPlayer wrongSfx;

    MediaPlayer buttonTap;

    MediaPlayer gameMusic;


    public void start (View view) {

        if (!sfxMuted) {

            buttonTap.start();

        }

        reset();

        switchVisibility();

        settingsBtn.setVisibility(View.INVISIBLE);

    }

    public void chooseAnswer (View view) {

        totalQuestions++;

        if (view.getTag().toString().equals(correctAnswerLocation + "")) {

            numCorrect++;

            if (!sfxMuted) {

                correctSfx.start();

            }

        } else {

            if (!sfxMuted) {

                wrongSfx.start();

            }

        }

        updateScoreText();

        generateQuestion();

    }


    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        gameMusic = MediaPlayer.create(this,R.raw.the_tides);

        prefs = this.getSharedPreferences("dan.ajayi.braingames", Context.MODE_PRIVATE);

        musicMuted = prefs.getBoolean("musicMuted",false);

        sfxMuted = prefs.getBoolean("sfxMuted",false);

        if (!musicMuted) {

            gameMusic.setLooping(true);

            gameMusic.start();

        }

        correctSfx = MediaPlayer.create(this,R.raw.ding);

        wrongSfx = MediaPlayer.create(this,R.raw.buzz);

        buttonTap = MediaPlayer.create(this,R.raw.button_tap);


        playButton = findViewById(R.id.playButton);

        logo = findViewById(R.id.logo);

        signature = findViewById(R.id.signature);

        highScoreText = findViewById(R.id.highScoreText);

        question = findViewById(R.id.question);

        timer = findViewById(R.id.timerText);

        hourglass = findViewById(R.id.hourglass);

        score = findViewById(R.id.scoreText);

        button1 = findViewById(R.id.button1);

        button2 = findViewById(R.id.button2);

        button3 = findViewById(R.id.button3);

        button4 = findViewById(R.id.button4);

        menu = findViewById(R.id.menu);

        musicBtn = findViewById(R.id.musicBtn);

        sfxBtn = findViewById(R.id.sfxBtn);

        homeBtn = findViewById(R.id.homeBtn);

        sfxX = findViewById(R.id.sfxX);

        musicX = findViewById(R.id.musicX);

        settingsBtn = findViewById(R.id.settingsBtn);

        highScore = prefs.getInt("highScore",0);

        highScoreText.setText("High Score: " + highScore);

        buttonLayout = findViewById(R.id.buttonLayout);

        buttons.add(button1);

        buttons.add(button2);

        buttons.add(button3);

        buttons.add(button4);

        numCorrect = 0;

        totalQuestions = 0;

        operations.add("+");

        operations.add("-");

        operations.add("x");

        operations.add("/");

        operation = "";

        visibility.add(View.INVISIBLE);

        visibility.add(View.VISIBLE);

        updateScoreText();

        generateQuestion();

    }

    public void updateScoreText() {

        score.setText(numCorrect + "/" + totalQuestions);

    }

    public void generateQuestion() {

        //Generate random question, set text equal to question

        a = rand.nextInt(21);

        b = rand.nextInt(21);

        correctAnswerLocation = rand.nextInt(4);

        randOperation = rand.nextInt(4);

        operation = operations.get(randOperation);

        answers.clear();
        if (operation.equals("+")) {

            correctAnswer = a + b;

        }

        if (operation.equals("-")) {

            while (b > a) {

                b = rand.nextInt(21);

            }

            correctAnswer = a - b;

        }

        if (operation.equals("x")) {

            a = rand.nextInt(15);

            b = rand.nextInt(15);

            correctAnswer = a * b;


        }

        if (operation.equals("/")) {

            while (b == 0 || a % b != 0) {

                b = rand.nextInt(21);

            }

            correctAnswer = a / b;

        }

        //create false answers and put them in random positions, set buttons equals to answers

        for (int i = 0; i < 4; i++) {

            if (i == correctAnswerLocation) {

                answers.add(correctAnswer);

                buttons.get(correctAnswerLocation).setText(correctAnswer + "");

            } else {

                int wrongAnswer = rand.nextInt(51);

                while (wrongAnswer == correctAnswer) {

                    wrongAnswer = rand.nextInt(51);

                }

                answers.add(wrongAnswer);

                buttons.get(i).setText(answers.get(i) + "");

            }

        }

        question.setText(a + " " + operation + " " + b);

    }

    public void switchVisibility() {

        playButton.setVisibility(visibility.get(0));

        logo.setVisibility(visibility.get(0));

        signature.setVisibility(visibility.get(0));

        highScoreText.setVisibility(visibility.get(0));

        question.setVisibility(visibility.get(1));

        hourglass.setVisibility(visibility.get(1));

        score.setVisibility(visibility.get(1));

        timer.setVisibility(visibility.get(1));

        buttonLayout.setVisibility(visibility.get(1));

        Integer holdVar = visibility.get(0);

        visibility.set(0, visibility.get(1));

        visibility.set(1, holdVar);

    }

    public void makeToast() {

        Toast.makeText(this, "You scored: " + numCorrect, Toast.LENGTH_SHORT).show();

    }

    public void reset() {

        numCorrect = 0;

        totalQuestions = 0;

        updateScoreText();

        secsLeft = 30;

        new CountDownTimer(secsLeft * 1000 + 100,1000){

            @Override

            public void onTick(long millisUntilFinished) {

                timer.setText(secsLeft + "s");

                secsLeft--;

            }

            @Override

            public void onFinish() {

                timer.setText(secsLeft + "s");

                secsLeft--;

                switchVisibility();

                makeToast();

                settingsBtn.setVisibility(View.VISIBLE);

                if (numCorrect > highScore) {

                    prefs.edit().putInt("highScore", numCorrect).apply();

                    highScore = prefs.getInt("highScore", 0);

                    highScoreText.setText("High Score: " + highScore);

                }

            }

        }.start();

    }

    public void muteMusic(View view) {

        if (!sfxMuted) {

            buttonTap.start();

        }

        if (musicMuted) {

            prefs.edit().putBoolean("musicMuted", false).apply();

            musicMuted = false;

            gameMusic = MediaPlayer.create(this,R.raw.the_tides);

            gameMusic.setLooping(true);

            gameMusic.start();

            musicX.setVisibility(View.INVISIBLE);

        } else {

            prefs.edit().putBoolean("musicMuted", true).apply();

            musicMuted = true;

            gameMusic.stop();

            musicX.setVisibility(View.VISIBLE);

        }

    }

    public void muteSfx(View view) {

        if (sfxMuted) {

            buttonTap.start();

            prefs.edit().putBoolean("sfxMuted", false).apply();

            sfxMuted = false;

            sfxX.setVisibility(View.INVISIBLE);

        } else {

            prefs.edit().putBoolean("sfxMuted", true).apply();

            sfxMuted = true;

            sfxX.setVisibility(View.VISIBLE);

        }

    }

    public void home(View view) {

        if (!sfxMuted) {

            buttonTap.start();

        }

        playButton.setVisibility(View.VISIBLE);

        menu.setVisibility(View.INVISIBLE);

        musicBtn.setVisibility(View.INVISIBLE);

        sfxBtn.setVisibility(View.INVISIBLE);

        homeBtn.setVisibility(View.INVISIBLE);

        musicX.setVisibility(View.INVISIBLE);

        sfxX.setVisibility(View.INVISIBLE);

    }

    public void settingsPanel(View view) {

        if (!sfxMuted) {

            buttonTap.start();

        }

        menu.setVisibility(View.VISIBLE);

        musicBtn.setVisibility(View.VISIBLE);

        sfxBtn.setVisibility(View.VISIBLE);

        homeBtn.setVisibility(View.VISIBLE);

        playButton.setVisibility(View.INVISIBLE);

        if (musicMuted) {

            musicX.setVisibility(View.VISIBLE);

        }

        if (sfxMuted) {

            sfxX.setVisibility(View.VISIBLE);

        }



    }
}