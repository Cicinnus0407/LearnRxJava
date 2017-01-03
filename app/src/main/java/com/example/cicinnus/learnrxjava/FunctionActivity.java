package com.example.cicinnus.learnrxjava;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by Cicinnus on 2017/1/3.
 */

public class FunctionActivity extends AppCompatActivity {

    private TextView tv_log;
    private Long currentNum;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function);
        tv_log = (TextView) findViewById(R.id.log);
    }

    /**
     * create
     *
     * @param view
     */
    public void create(View view) {
        tv_log.setText("");
        final String TAG = "create";
        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("1");
                subscriber.onNext("2");
                subscriber.onNext("3");
            }
        });
        Subscriber<String> subscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted: ");
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError: ", e);
            }

            @Override
            public void onNext(String s) {
                Log.d(TAG, "onNext: " + s);
                tv_log.append("onNext: " + s + "\n");
            }
        };
        observable.subscribe(subscriber);
    }

    /**
     * from 发射的数据是统一类型时候可以使用该方法
     * 同时支持Java并发编程，Future，具体可参考三个参数的方法
     * from(Future<? extends T> future,long timeout,TimeUnit unit)
     *
     * @param view
     */
    public void from(View view) {
        tv_log.setText("");

        final String TAG = "FROM";
        String[] args = new String[]{"This", "is", "'\'from'\'", "test"};
        Observable<String> observable = Observable.from(args);
        Subscriber<String> subscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted: ");

            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError: ", e);
            }

            @Override
            public void onNext(String s) {
                Log.d(TAG, "onNext: " + s);
                tv_log.append("onNext：" + s + "\n");
            }
        };
        observable.subscribe(subscriber);
    }

    /**
     * just
     *
     * @param view
     */
    public void just(View view) {
        tv_log.setText("");
        Observable.just("one", "two", "three")
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        tv_log.append("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        tv_log.append("onError" + e.getMessage());
                    }

                    @Override
                    public void onNext(String s) {
                        tv_log.append("onNext：" + s + "\n");
                    }
                });

    }

    /**
     * 发射不同的数据，返回序列化后的数据
     *
     * @param view
     */
    public void just2(View view) {
        tv_log.setText("");
        Observable.just(0, "zero", 1.0, "one", 2, "two")
                .subscribe(new Subscriber<Serializable>() {
                    @Override
                    public void onCompleted() {
                        tv_log.append("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        tv_log.append("onError" + e.getMessage());
                    }

                    @Override
                    public void onNext(Serializable serializable) {
                        tv_log.append("onNext：" + serializable.toString() + "\n");
                    }
                });

    }

    /**
     * 返回在指定范围内发出一系列整数的Observable。
     * 第二个参数不能为负数，如果为0则不发射数据
     *
     * @param view
     */
    public void range(View view) {
        tv_log.setText("");
        Observable.range(1, 5)
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        tv_log.append("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        tv_log.append("onError" + e.getMessage());
                    }

                    @Override
                    public void onNext(Integer integer) {
                        tv_log.append("onNext：" + integer + "\n");
                    }
                });
    }


    /**
     * timer
     * timer默认在computation调度器上执行，如果需要更新UI，需要他通过observeOn(AndroidSchedulers.mainThread())
     * 或者使用三个参数的方法，传入Scheduler
     *
     * @param view
     */
    public void timer(View view) {
        tv_log.setText("");
        Observable.timer(5, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {
                        tv_log.append("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        tv_log.append("onError" + e.getMessage());
                    }

                    @Override
                    public void onNext(Long aLong) {
                        tv_log.append("onNext" + aLong + "\n");
                    }
                });
    }

    /**
     * 间隔一段时间执行
     * 注意：此方法会一直执行，直到解除订阅
     *
     * @param view
     */
    public void interval(View view) {
        tv_log.setText("");
        currentNum = 0L;
        Observable observable = Observable.interval(1, TimeUnit.SECONDS, AndroidSchedulers.mainThread());
        Subscription subscription = observable.subscribe(new Subscriber<Long>() {
            @Override
            public void onCompleted() {
                tv_log.append("onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                tv_log.append("onError");
            }

            @Override
            public void onNext(Long aLong) {
                tv_log.append("onNext：" + aLong + "\n");
                currentNum = aLong;

            }
        });
    }
}
