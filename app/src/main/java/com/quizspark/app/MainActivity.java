package com.quizspark.app;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ScrollView;
import android.content.SharedPreferences;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends Activity {
    private final int PURPLE = Color.rgb(108,77,255), DARK = Color.rgb(32,18,77), BG = Color.rgb(247,246,255), INK = Color.rgb(37,35,61);
    private LinearLayout root, content;
    private TextView scoreLabel, questionLabel;
    private ProgressBar progress;
    private int index=0, score=0, streak=0;
    private boolean answered=false;
    private String category="All Topics";
    private final List<Q> questions = new ArrayList<>();
    private final List<Q> round = new ArrayList<>();
    private SharedPreferences prefs;

    static class Q {
        String cat, text, explanation; String[] options; int answer;
        Q(String c,String t,String[] o,int a,String e){cat=c;text=t;options=o;answer=a;explanation=e;}
    }
    @Override public void onCreate(Bundle b){super.onCreate(b); prefs=getSharedPreferences("quizspark",MODE_PRIVATE); seed(); home();}
    private void seed(){
        questions.add(new Q("Science","Which planet is known as the Red Planet?",new String[]{"Venus","Mars","Jupiter","Mercury"},1,"Iron minerals on Mars' surface give it a reddish appearance."));
        questions.add(new Q("Geography","What is the largest ocean on Earth?",new String[]{"Atlantic","Indian","Pacific","Arctic"},2,"The Pacific Ocean is the largest and deepest ocean basin."));
        questions.add(new Q("Science","What is the chemical symbol for gold?",new String[]{"Ag","Gd","Go","Au"},3,"Au comes from the Latin word aurum."));
        questions.add(new Q("History","In which country did the Olympic Games originate?",new String[]{"Italy","Greece","Egypt","France"},1,"The ancient Olympic Games began in Olympia, Greece."));
        questions.add(new Q("Technology","Which company developed the Android operating system initially?",new String[]{"Apple","IBM","Android Inc.","Nokia"},2,"Android Inc. developed it before Google acquired the company in 2005."));
        questions.add(new Q("Nature","What is the fastest land animal?",new String[]{"Lion","Cheetah","Horse","Leopard"},1,"A cheetah can reach very high speeds over short distances."));
        questions.add(new Q("Space","What is the closest star to Earth?",new String[]{"Sirius","Proxima Centauri","The Sun","Polaris"},2,"The Sun is our nearest star, about 150 million kilometres away on average."));
        questions.add(new Q("Science","Which gas do plants absorb during photosynthesis?",new String[]{"Oxygen","Nitrogen","Carbon dioxide","Hydrogen"},2,"Plants use carbon dioxide, water and light to make sugars."));
        questions.add(new Q("Geography","What is the capital city of Japan?",new String[]{"Kyoto","Tokyo","Osaka","Nagoya"},1,"Tokyo is Japan's capital and largest metropolitan area."));
        questions.add(new Q("Sports","How many players from one team are on the field in football (soccer)?",new String[]{"9","10","11","12"},2,"A standard football team has eleven players on the field."));
        questions.add(new Q("Literature","Who wrote the play Romeo and Juliet?",new String[]{"Charles Dickens","William Shakespeare","Jane Austen","Mark Twain"},1,"William Shakespeare wrote the tragedy, likely in the 1590s."));
        questions.add(new Q("Technology","What does CPU stand for?",new String[]{"Central Processing Unit","Computer Power Utility","Core Program Usage","Central Performance User"},0,"The CPU executes instructions and coordinates many computer operations."));
        questions.add(new Q("History","The Great Wall is located in which country?",new String[]{"India","China","South Korea","Mongolia"},1,"The Great Wall is a historic series of fortifications in China."));
        questions.add(new Q("Nature","What is the largest mammal in the world?",new String[]{"African elephant","Giraffe","Blue whale","Hippopotamus"},2,"The blue whale is the largest known animal to have ever lived."));
        questions.add(new Q("Science","At sea level, water boils at what temperature in Celsius?",new String[]{"50°C","90°C","100°C","120°C"},2,"At standard atmospheric pressure, water boils at 100°C."));
    }
    private GradientDrawable shape(int color,int radius){GradientDrawable d=new GradientDrawable();d.setColor(color);d.setCornerRadius(radius);return d;}
    private TextView text(String s,int size,int color,boolean bold){TextView t=new TextView(this);t.setText(s);t.setTextSize(size);t.setTextColor(color);if(bold)t.setTypeface(Typeface.DEFAULT,Typeface.BOLD);t.setGravity(Gravity.CENTER_VERTICAL);return t;}
    private LinearLayout column(){LinearLayout l=new LinearLayout(this);l.setOrientation(LinearLayout.VERTICAL);return l;}
    private void base(){root=column();root.setBackgroundColor(BG);setContentView(root);}
    private void pad(View v,int l,int t,int r,int b){v.setPadding(l,t,r,b);}
    private void home(){base();LinearLayout hero=column();hero.setBackground(new GradientDrawable(GradientDrawable.Orientation.TL_BR,new int[]{DARK,PURPLE,Color.rgb(255,103,163)}));((GradientDrawable) hero.getBackground()).setCornerRadius(40);pad(hero,24,28,24,28);LinearLayout.LayoutParams hp=new LinearLayout.LayoutParams(-1,-2);hp.setMargins(18,18,18,16);root.addView(hero,hp);
        hero.addView(text("✦  QUIZSPARK",14,Color.WHITE,true));TextView title=text("Make your\nbrain shine.",34,Color.WHITE,true);title.setLineSpacing(2,1);LinearLayout.LayoutParams tp=new LinearLayout.LayoutParams(-1,-2);tp.topMargin=20;hero.addView(title,tp);TextView sub=text("Quick questions. Big discoveries.",15,Color.WHITE,false);LinearLayout.LayoutParams sp=new LinearLayout.LayoutParams(-1,-2);sp.topMargin=8;hero.addView(sub,sp);
        TextView best=text("🏆  Best score: "+prefs.getInt("best",0)+" points",14,Color.WHITE,true);LinearLayout.LayoutParams bp=new LinearLayout.LayoutParams(-1,-2);bp.topMargin=22;hero.addView(best,bp);
        ScrollView scroll=new ScrollView(this);root.addView(scroll,new LinearLayout.LayoutParams(-1,0,1));content=column();pad(content,20,6,20,24);scroll.addView(content);
        content.addView(text("Choose your challenge",21,INK,true));TextView catSub=text("Pick a topic or mix them all.",14,Color.GRAY,false);content.addView(catSub);
        String[] cats={"All Topics","Science","Geography","Technology","History","Nature","Space","Sports","Literature"};String[] icons={"✨","🔬","🌍","💻","🏛️","🌿","🚀","⚽","📚"};
        for(int i=0;i<cats.length;i++){final String c=cats[i];LinearLayout card=new LinearLayout(this);card.setGravity(Gravity.CENTER_VERTICAL);card.setBackground(shape(Color.WHITE,24));pad(card,16,13,16,13);TextView icon=text(icons[i],25,INK,true);card.addView(icon);LinearLayout mid=column();LinearLayout.LayoutParams mp=new LinearLayout.LayoutParams(0,-2,1);mp.leftMargin=14;card.addView(mid,mp);mid.addView(text(c,16,INK,true));mid.addView(text(c.equals("All Topics")?"A little bit of everything":"Explore "+c.toLowerCase()+" questions",12,Color.GRAY,false));TextView arrow=text("›",28,PURPLE,true);card.addView(arrow);LinearLayout.LayoutParams cp=new LinearLayout.LayoutParams(-1,-2);cp.topMargin=10;content.addView(card,cp);card.setOnClickListener(v->start(c));}
    }
    private void start(String c){category=c;round.clear();for(Q q:questions)if(c.equals("All Topics")||q.cat.equals(c))round.add(q);Collections.shuffle(round);if(round.size()>10)while(round.size()>10)round.remove(round.size()-1);index=0;score=0;streak=0;showQuestion();}
    private void showQuestion(){answered=false;base();LinearLayout top=column();top.setBackground(shape(DARK,32));pad(top,22,18,22,20);LinearLayout.LayoutParams tpar=new LinearLayout.LayoutParams(-1,-2);tpar.setMargins(16,16,16,16);root.addView(top,tpar);top.addView(text("QUIZSPARK  /  "+category.toUpperCase(),12,Color.rgb(207,197,255),true));LinearLayout row=new LinearLayout(this);row.setGravity(Gravity.CENTER_VERTICAL);LinearLayout.LayoutParams rp=new LinearLayout.LayoutParams(-1,-2);rp.topMargin=12;top.addView(row,rp);TextView qn=text("Question "+(index+1)+" of "+round.size(),15,Color.WHITE,true);row.addView(qn,new LinearLayout.LayoutParams(0,-2,1));row.addView(text("⭐ "+score,15,Color.WHITE,true));ProgressBar pb=new ProgressBar(this,null,android.R.attr.progressBarStyleHorizontal);pb.setMax(round.size());pb.setProgress(index);pb.setProgressTintList(android.content.res.ColorStateList.valueOf(Color.rgb(255,190,92)));pb.setProgressBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.rgb(79,63,130)));LinearLayout.LayoutParams pp=new LinearLayout.LayoutParams(-1,8);pp.topMargin=14;top.addView(pb,pp);
        ScrollView sc=new ScrollView(this);root.addView(sc,new LinearLayout.LayoutParams(-1,0,1));content=column();pad(content,20,8,20,20);sc.addView(content);Q q=round.get(index);TextView emoji=text(iconFor(q.cat),38,INK,true);content.addView(emoji);TextView cat=text(q.cat.toUpperCase()+"  •  +10 POINTS",12,PURPLE,true);LinearLayout.LayoutParams cpar=new LinearLayout.LayoutParams(-1,-2);cpar.topMargin=12;content.addView(cat,cpar);TextView question=text(q.text,25,INK,true);question.setLineSpacing(4,1);LinearLayout.LayoutParams qpar=new LinearLayout.LayoutParams(-1,-2);qpar.topMargin=10;content.addView(question,qpar);
        for(int i=0;i<q.options.length;i++){final int pick=i;Button b=new Button(this);b.setText((char)('A'+i)+"    "+q.options[i]);b.setAllCaps(false);b.setTextSize(15);b.setTextColor(INK);b.setGravity(Gravity.CENTER_VERTICAL);b.setPadding(18,8,14,8);b.setBackground(shape(Color.WHITE,22));LinearLayout.LayoutParams op=new LinearLayout.LayoutParams(-1,-2);op.topMargin=12;content.addView(b,op);b.setOnClickListener(v->choose(pick));}
        TextView foot=text("🔥 Current streak: "+streak+"   •   Take your time",13,Color.GRAY,false);LinearLayout.LayoutParams fp=new LinearLayout.LayoutParams(-1,-2);fp.topMargin=20;content.addView(foot,fp);
    }
    private String iconFor(String c){switch(c){case "Science":return "🔬";case "Geography":return "🌍";case "Technology":return "💻";case "History":return "🏛️";case "Nature":return "🌿";case "Space":return "🚀";case "Sports":return "⚽";case "Literature":return "📚";default:return "✨";}}
    private void choose(int pick){if(answered)return;answered=true;Q q=round.get(index);boolean good=pick==q.answer;if(good){score+=10;streak++;}else streak=0;
        // Rebuild question view with clear answer feedback and explanation.
        base();LinearLayout panel=column();panel.setBackground(shape(DARK,32));pad(panel,22,22,22,22);LinearLayout.LayoutParams pl=new LinearLayout.LayoutParams(-1,-2);pl.setMargins(16,16,16,16);root.addView(panel,pl);panel.addView(text(good?"✨  NICE WORK!":"💡  KEEP LEARNING",13,Color.rgb(255,206,113),true));TextView big=text(good?"That's correct!":"Not quite this time.",27,Color.WHITE,true);LinearLayout.LayoutParams bl=new LinearLayout.LayoutParams(-1,-2);bl.topMargin=12;panel.addView(big,bl);TextView ans=text("Answer: "+q.options[q.answer],16,Color.WHITE,true);LinearLayout.LayoutParams al=new LinearLayout.LayoutParams(-1,-2);al.topMargin=12;panel.addView(ans,al);
        TextView expl=text(q.explanation,15,INK,false);expl.setLineSpacing(5,1);pad(expl,18,18,18,18);expl.setBackground(shape(Color.WHITE,22));LinearLayout.LayoutParams el=new LinearLayout.LayoutParams(-1,-2);el.setMargins(20,8,20,8);root.addView(expl,el);TextView scoreText=text("⭐  Score: "+score+" points     🔥 Streak: "+streak,16,PURPLE,true);pad(scoreText,22,14,22,14);root.addView(scoreText);
        Button next=new Button(this);next.setText(index==round.size()-1?"See my results  →":"Next question  →");next.setAllCaps(false);next.setTextSize(16);next.setTextColor(Color.WHITE);next.setBackground(shape(PURPLE,24));LinearLayout.LayoutParams np=new LinearLayout.LayoutParams(-1,58);np.setMargins(20,20,20,18);root.addView(next,np);next.setOnClickListener(v->{index++;if(index<round.size())showQuestion();else results();});Button skipHome=new Button(this);skipHome.setText("Back to topics");skipHome.setAllCaps(false);skipHome.setTextColor(PURPLE);skipHome.setBackgroundColor(Color.TRANSPARENT);root.addView(skipHome);skipHome.setOnClickListener(v->home());
    }
    private void results(){int best=Math.max(score,prefs.getInt("best",0));prefs.edit().putInt("best",best).apply();base();LinearLayout hero=column();hero.setGravity(Gravity.CENTER);hero.setBackground(new GradientDrawable(GradientDrawable.Orientation.TL_BR,new int[]{DARK,PURPLE,Color.rgb(255,103,163)}));((GradientDrawable) hero.getBackground()).setCornerRadius(38);pad(hero,24,30,24,30);LinearLayout.LayoutParams hp=new LinearLayout.LayoutParams(-1,-2);hp.setMargins(18,24,18,20);root.addView(hero,hp);hero.addView(text("🏆",54,Color.WHITE,true));TextView title=text("Challenge complete!",27,Color.WHITE,true);title.setGravity(Gravity.CENTER);LinearLayout.LayoutParams tp=new LinearLayout.LayoutParams(-1,-2);tp.topMargin=14;hero.addView(title,tp);TextView points=text(score+" / "+(round.size()*10),42,Color.WHITE,true);points.setGravity(Gravity.CENTER);LinearLayout.LayoutParams pp=new LinearLayout.LayoutParams(-1,-2);pp.topMargin=14;hero.addView(points,pp);TextView label=text("POINTS EARNED",13,Color.WHITE,true);label.setGravity(Gravity.CENTER);hero.addView(label);int pct=round.isEmpty()?0:score/(round.size()*10)*100;TextView msg=text(pct>=80?"Brilliant! You're a knowledge star.":pct>=50?"Great effort! Keep your curiosity going.":"Every question is a chance to learn. Try again!",15,Color.WHITE,false);msg.setGravity(Gravity.CENTER);LinearLayout.LayoutParams mp=new LinearLayout.LayoutParams(-1,-2);mp.topMargin=18;hero.addView(msg,mp);
        TextView stat=text("🏅  Best score: "+best+" points\n🧠  Questions answered: "+round.size()+"\n✨  Topic: "+category,17,INK,true);pad(stat,22,18,22,18);stat.setBackground(shape(Color.WHITE,24));LinearLayout.LayoutParams st=new LinearLayout.LayoutParams(-1,-2);st.setMargins(20,0,20,12);root.addView(stat,st);
        Button again=new Button(this);again.setText("Play again  ↻");again.setAllCaps(false);again.setTextSize(16);again.setTextColor(Color.WHITE);again.setBackground(shape(PURPLE,24));LinearLayout.LayoutParams ap=new LinearLayout.LayoutParams(-1,58);ap.setMargins(20,16,20,8);root.addView(again,ap);again.setOnClickListener(v->start(category));Button home=new Button(this);home.setText("Choose another topic");home.setAllCaps(false);home.setTextColor(PURPLE);home.setBackgroundColor(Color.TRANSPARENT);root.addView(home);home.setOnClickListener(v->home());
    }
}
