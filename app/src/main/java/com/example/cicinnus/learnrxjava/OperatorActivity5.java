package com.example.cicinnus.learnrxjava;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.schedulers.TimeInterval;
import rx.schedulers.Timestamped;

/**
 * Created by Cicinnus on 2017/1/5.
 */
public class OperatorActivity5 extends AppCompatActivity {

    private TextView tv_log;
    private LinearLayout ll_subscribe;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operator5);
        tv_log = (TextView) findViewById(R.id.tv_log);
        ll_subscribe = (LinearLayout) findViewById(R.id.ll_subscribe);
    }

    /**
     * 注意：若发射数据后有更新UI操作需将调度器指定AndroidSchedulers.mainThread()
     *
     * @param view
     */
    public void delay(View view) {
        tv_log.setText("延迟1秒发射数据\n");
        Observable
                .create(new Observable.OnSubscribe<Integer>() {
                    @Override
                    public void call(Subscriber<? super Integer> subscriber) {
                        subscriber.onNext(0);
                        subscriber.onNext(1);
                        subscriber.onNext(2);
                        subscriber.onCompleted();
                        tv_log.append("call: " + new SimpleDateFormat("yyyy/MM/dd HH:MM:ss").format(System.currentTimeMillis()) + "\n");

                    }
                })
                .delay(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        tv_log.append("onCompleted: " + new SimpleDateFormat("yyyy/MM/dd HH:MM:ss").format(System.currentTimeMillis()) + "\n");
                    }

                    @Override
                    public void onError(Throwable e) {
                        tv_log.append("onError: " + e.getMessage());
                    }

                    @Override
                    public void onNext(Integer integer) {
                        tv_log.append("onNext: " + new SimpleDateFormat("yyyy/MM/dd HH:MM:ss").format(System.currentTimeMillis()) + "\n");
                    }
                });
    }

    /**
     * 和delay的区别在于：delay是延时发射数据，而delaySubscription是延迟订阅
     *
     * @param view
     */
    public void delaySubscription(View view) {
        tv_log.setText("延迟2s订阅,不在主线程，具体查看log\n");
        Observable<Integer> observable = Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                Log.d("call: ", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(System.currentTimeMillis()));
                subscriber.onNext(0);
                subscriber.onNext(1);
                subscriber.onNext(2);
            }
        }).delaySubscription(2, TimeUnit.SECONDS);
        observable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        tv_log.append("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        tv_log.append("onError: " + e.getMessage());
                    }

                    @Override
                    public void onNext(Integer integer) {
                        tv_log.append("onNext: " + integer + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(System.currentTimeMillis()) + "\n");
                    }
                });
    }

    /**
     * Do系列操作符
     *
     * @param view
     */
    public void Do(View view) {
        tv_log.setText("Do系列操作符,可用于观察整个订阅的流程\n");
        Observable
                .just(0, 1, 2)
                .doOnNext(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        tv_log.append("doOnNext: " + integer + "\n");
                    }
                })
                .doOnError(new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        tv_log.append("doOnError: " + throwable.getMessage() + "\n");
                    }
                })
                .doOnCompleted(new Action0() {
                    @Override
                    public void call() {
                        tv_log.append("doOnCompleted\n");
                    }
                })
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        tv_log.append("doOnSubscribe\n");
                    }
                })
                .doOnUnsubscribe(new Action0() {
                    @Override
                    public void call() {
                        tv_log.append("doOnUnSubscription\n");
                    }
                })
                .doOnTerminate(new Action0() {
                    @Override
                    public void call() {
                        tv_log.append("doOnTerminate\n");
                    }
                })
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        tv_log.append("onCompleted\n");
                    }

                    @Override
                    public void onError(Throwable e) {
                        tv_log.append("onError: " + e.getMessage() + "\n");
                    }

                    @Override
                    public void onNext(Integer integer) {
                        tv_log.append("onNext: " + integer + "\n");

                    }
                });
    }

    /**
     * @param view
     */
    public void subscribeOn(View view) {
        final StringBuffer stringBuffer = new StringBuffer();
        Observable
                .create(new Observable.OnSubscribe<Drawable>() {
                    @Override
                    public void call(Subscriber<? super Drawable> subscriber) {
                        stringBuffer.append("发送事件" + Thread.currentThread().getName() + "\n");
                        Drawable drawable = getResources().getDrawable(R.mipmap.ic_launcher);
                        subscriber.onNext(drawable);
                        subscriber.onCompleted();
                    }
                })
                //在io中创建Observable
                .subscribeOn(Schedulers.io())
                //在新的线程中
                .observeOn(Schedulers.newThread())
                .map(new Func1<Drawable, ImageView>() {
                    @Override
                    public ImageView call(Drawable drawable) {
                        ImageView imageView = new ImageView(OperatorActivity5.this);
                        imageView.setImageDrawable(drawable);
                        return imageView;
                    }
                })
                //操作ui
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ImageView>() {
                    @Override
                    public void onCompleted() {
                        tv_log.append("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        tv_log.append("onError: " + e.getMessage());
                    }

                    @Override
                    public void onNext(ImageView imageView) {
                        ll_subscribe.addView(imageView);
                        tv_log.append("接收事件\n" + stringBuffer);
                    }
                });
    }

    /**
     * 将原始Observable转换为另一个Obserervable，后者发射一个标志替换前者的数据项，这个标志表示前者的两个连续发射物之间流逝的时间长度
     *
     * @param view
     */
    public void timeInterval(View view) {
        tv_log.setText("获取发射数据的间隔\n");
        Observable
                .interval(1, TimeUnit.SECONDS)
                .filter(new Func1<Long, Boolean>() {
                    @Override
                    public Boolean call(Long aLong) {
                        return aLong < 5;
                    }
                })
                .timeInterval()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<TimeInterval<Long>>() {
                    @Override
                    public void onCompleted() {
                        tv_log.append("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        tv_log.append("onError: " + e.getMessage());
                    }

                    @Override
                    public void onNext(TimeInterval<Long> longTimeInterval) {
                        tv_log.append("onNext: " + longTimeInterval.getValue() + " : " + longTimeInterval.getIntervalInMilliseconds() + "\n");
                    }
                });
    }

    /**
     * 该操作符和TimeInterval一样最终发射的都是TimeInterval类型数据。
     * 但是不同的是，该操作符发射数据每一项包含数据的原始发射时间（TimeInterval是时间间隔）
     * @param view
     */
    public void timestamp(View view) {
        tv_log.setText("获取发射数据的起始时间\n");
        Observable
                .interval(1, TimeUnit.SECONDS)
                .filter(new Func1<Long, Boolean>() {
                    @Override
                    public Boolean call(Long aLong) {
                        return aLong<5;
                    }
                })
                .timestamp()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Timestamped<Long>>() {
                    @Override
                    public void onCompleted() {
                        tv_log.append("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        tv_log.append("onError :" + e.getMessage());
                    }

                    @Override
                    public void onNext(Timestamped<Long> longTimestamped) {
                        tv_log.append("onNext: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(longTimestamped.getTimestampMillis()) + "\n");
                    }
                });
    }
}
