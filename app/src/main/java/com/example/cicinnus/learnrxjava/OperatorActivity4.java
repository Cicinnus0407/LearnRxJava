package com.example.cicinnus.learnrxjava;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * Created by Cicinnus on 2017/1/5.
 */

public class OperatorActivity4 extends AppCompatActivity {

    private TextView tv_log;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operator4);
        tv_log = (TextView) findViewById(R.id.tv_log);
    }

    /**
     * 因为发射的数据没有耗时操作，所以看起来会是顺序执行
     *
     * @param view
     */
    public void merge(View view) {
        tv_log.setText("合并两次发射的数据\n");
        Observable<Integer> observable1 = Observable.just(0, 1, 2);
        Observable<Integer> observable2 = Observable.just(4, 5, 6);
        Observable.merge(observable1, observable2)
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
                        tv_log.append("onNext: " + integer + "\n");
                    }
                });
    }


    /**
     * 当某一Observe发射了onError，使用mergeDelayError可以将所有发射结束后再报error
     *
     * @param view
     */
    public void mergeDelayError(View view) {
        tv_log.setText("合并多个Observer\n");
        Observable<Integer> observable1 = Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                subscriber.onNext(0);
                subscriber.onNext(1);
                subscriber.onNext(2);
                subscriber.onError(new Throwable("手动调用error"));
            }
        });
        Observable<Integer> observable2 = Observable.just(5, 6, 7);
        Observable.mergeDelayError(observable1, observable2)
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
                        tv_log.append("onNext: " + integer + "\n");
                    }
                });

    }

    /**
     * 顺序发射observer
     *
     * @param view
     */
    public void concat(View view) {
        tv_log.append("顺序合并Observer\n");
        Observable<Integer> observable1 = Observable
                .create(new Observable.OnSubscribe<Integer>() {
                    @Override
                    public void call(Subscriber<? super Integer> subscriber) {

                        try {
                            subscriber.onNext(0);
                            Thread.sleep(500);
                            subscriber.onNext(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        subscriber.onCompleted();
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
        Observable<Integer> observable2 = Observable.just(5, 6, 7);
        Observable.concat(observable1, observable2)
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
                        tv_log.append("onNext: " + integer + "\n");

                    }
                });
    }

    /**
     * 合并多个Observer发射的数据，然后发射函数的返回结果，它只发射与发射数据项最少的那个Observable一样多的数据，
     * 假如两个Observable数据分布为4项，5项，则最终合并是4项
     *
     * @param view
     */
    public void zip(View view) {
        tv_log.setText("合并多个Observer发射的数据，然后发射函数的返回结果\n");
        List<Integer> num = new ArrayList<>();
        List<String> name = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            num.add(i);
            name.add("name" + i);
        }
        num.add(5);
        Observable<Integer> observable1 = Observable.from(num);
        Observable<String> observable2 = Observable.from(name);
        Observable.zip(observable1, observable2, new Func2<Integer, String, String>() {
            @Override
            public String call(Integer integer, String s) {
                return s + " num: " + integer;
            }
        }).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                tv_log.append("onCompleted");

            }

            @Override
            public void onError(Throwable e) {
                tv_log.append("onError: " + e.getMessage());

            }

            @Override
            public void onNext(String s) {
                tv_log.append("onNext: " + s + "\n");
            }
        });

    }

    /**
     * 在一个Observable在发射数据之前先发射一个指定的数据序列
     *
     * @param view
     */
    public void startWith(View view) {
        Observable
                .range(0, 5)
                .startWith(11, 12)
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
                        tv_log.append("onNext: " + integer + "\n");

                    }
                });
    }

    /**
     * 合并两个Observer最近发射的数据
     *
     * @param view
     */
    public void combineLatest(View view) {
        tv_log.setText("合并两个Observer最近发射的数据\n");
        Observable<String> observableA = Observable.just("one", "two", "three");
        Observable<Integer> observableB = Observable.just(1, 2, 3, 4);
        Observable.combineLatest(observableA, observableB, new Func2<String, Integer, String>() {
            @Override
            public String call(String s, Integer integer) {
                return "observableA: " + s + " observableB: " + integer;
            }
        }).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                tv_log.append("onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                tv_log.append("onError: " + e.getMessage());
            }

            @Override
            public void onNext(String s) {
                tv_log.append("onNext: " + s + "\n");
            }
        });
    }

    /**
     * 在另一个Observable发射的数据定义的时间窗口内，这个Observable发射了一条数据，就结合两个Observable发射的数据
     * @param view
     */
    public void join(View view) {
        tv_log.setText("结合两个Observer的数据");
        Observable<Integer> observableA = Observable.range(1, 2).subscribeOn(Schedulers.newThread());
        Observable<Integer> observableB = Observable.range(7, 3).subscribeOn(Schedulers.newThread());
        observableA.join(observableB, new Func1<Integer, Observable<Integer>>() {
            @Override
            public Observable<Integer> call(Integer integer) {

                return Observable.just(integer).delay(1, TimeUnit.SECONDS);
            }
        }, new Func1<Integer, Observable<Integer>>() {
            @Override
            public Observable<Integer> call(Integer integer) {

                return Observable.just(integer).delay(1,TimeUnit.SECONDS);
            }
        }, new Func2<Integer, Integer, Integer>() {
            @Override
            public Integer call(Integer integer, Integer integer2) {
                return integer+integer2;
            }
        }).observeOn(AndroidSchedulers.mainThread())
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
                        tv_log.append("onNext: " + integer + "\n");

                    }
                });

    }
}
