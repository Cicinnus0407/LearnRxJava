package com.example.cicinnus.learnrxjava;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Cicinnus on 2017/1/3.
 */

public class FunctionActivity extends AppCompatActivity {

    private TextView tv_log;
    private Subscription subscription;
    private ScrollView scrollView;
    private String test;
    private LinearLayout ll_map;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function);
        tv_log = (TextView) findViewById(R.id.log);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        ll_map = (LinearLayout) findViewById(R.id.ll_map);
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
        tv_log.setText("5秒后执行" + "\n");
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

        Observable observable = Observable.interval(1, TimeUnit.SECONDS, AndroidSchedulers.mainThread());
        subscription = observable.subscribe(new Subscriber<Long>() {
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
                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                });
            }
        });
    }

    /**
     * 停止interval
     *
     * @param view
     */
    public void stopInterval(View view) {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
            tv_log.append("stop Interval");
        }
    }

    /**
     * repeat 从重复发射序列数据
     *
     * @param view
     */
    public void repeat(View view) {
        tv_log.setText("");
        String[] args = new String[]{"one", "two"};
        Observable
                .from(args)
                .repeat(2)
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        tv_log.append("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String s) {
                        tv_log.append("onNext:" + s + "\n");
                    }
                });
    }


    /**
     * defer
     * This allows an Observer to easily obtain updates or a refreshed version of the sequence.
     * 译：允许Observer简便地更新队列
     *
     * @param view
     */
    public void defer(View view) {
        tv_log.setText("");
        test = "旧数据";
        Observable<String> observable = Observable.defer(new Func0<Observable<String>>() {
            @Override
            public Observable<String> call() {
                return Observable.just(test);
            }
        });
        Subscriber<String> subscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {
                tv_log.append("onCompleted");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                tv_log.append("onNext" + s + "\n");
            }
        };

        test = "新数据";
        observable.subscribe(subscriber);
    }

    /**
     * map
     *
     * @param view
     */
    public void map(View view) {
        tv_log.setText("数据:0, 6, 7, 4, 9, 1, 5" + "判断是否小于5" + "\n");
        Integer[] integers = new Integer[]{0, 6, 7, 4, 9, 1, 5};
        Observable
                .from(integers)
                .map(new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer integer) {
                        tv_log.append("call:" + integer + "\n");
                        return integer < 5;
                    }
                })
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                        tv_log.append("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        tv_log.append("onNext:" + aBoolean + "\n");
                    }
                });
    }

    /**
     * map例子2
     *
     * @param view
     */
    public void map2(View view) {
        //模拟在一个人的集合中获取所有人的名字
        tv_log.setText("模拟在一个人的集合中获取所有人的名字" + "\n");
        List<Person> dataList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Person person = new Person();
            person.setName("name" + i);
            person.setAge(i * 5);
            person.setId(String.format("%s", i * 12461));
            dataList.add(person);
        }
        Observable
                .from(dataList)
                .map(new Func1<Person, String>() {
                    @Override
                    public String call(Person person) {
                        return person.getName();
                    }
                })
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        tv_log.append("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String s) {
                        tv_log.append("onNext：" + s + "\n");
                    }
                });
    }

    /**
     * map操作符示例3
     *
     * @param view
     */
    public void map3(View view) {
        tv_log.setText("从assets中读取一个图片，设置给imageView\n");
        InputStream inputStream = null;
        try {
            inputStream = getAssets().open("ic_launcher.png");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Observable
                .just(inputStream)
                .map(new Func1<InputStream, Drawable>() {
                    @Override
                    public Drawable call(InputStream inputStream) {
                        return Drawable.createFromStream(inputStream, "ic_lanucher");
                    }
                })
                .map(new Func1<Drawable, ImageView>() {
                    @Override
                    public ImageView call(Drawable drawable) {
                        ImageView imageView = new ImageView(FunctionActivity.this);
                        imageView.setImageDrawable(drawable);
                        return imageView;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ImageView>() {
                    @Override
                    public void onCompleted() {
                        tv_log.append("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        tv_log.append("onError：" + e.getMessage());
                    }

                    @Override
                    public void onNext(ImageView imageView) {
                        ll_map.addView(imageView);
                    }
                });
    }

    /**
     * flatmap
     * 并发多个observer,对于执行顺序没有要求可以使用
     *
     * @param view
     */
    public void flatmap(View view) {
        Integer[] args = new Integer[]{0, 1, 2};
        Observable
                .from(args)
                .flatMap(new Func1<Integer, Observable<String>>() {
                    @Override
                    public Observable<String> call(final Integer integer) {
                        return Observable.create(new Observable.OnSubscribe<String>() {
                            @Override
                            public void call(Subscriber<? super String> subscriber) {

                                Log.d("CALL", "FlatMap：" + Thread.currentThread().getName());
                                try {
                                    Thread.sleep(200);
                                    subscriber.onNext(String.format("FlatMap:%s", integer));
                                    subscriber.onCompleted();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                    subscriber.onError(e);
                                }
                            }
                        }).subscribeOn(Schedulers.newThread());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        tv_log.append("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        tv_log.append("onError：" + e.getMessage() + "\n");
                    }

                    @Override
                    public void onNext(String s) {
                        Log.d("OnNext", "onNext: FlatMap " + s);
                        tv_log.append("onNext: " + s + "\n");
                    }
                });
    }

    /**
     * concatMap 顺序执行Observer
     *
     * @param view
     */
    public void concatMap(View view) {
        tv_log.setText("");
        Integer[] integers = new Integer[]{0, 1, 2};
        Observable
                .from(integers)
                .concatMap(new Func1<Integer, Observable<String>>() {
                    @Override
                    public Observable<String> call(final Integer integer) {
                        return Observable.create(new Observable.OnSubscribe<String>() {
                            @Override
                            public void call(Subscriber<? super String> subscriber) {
                                Log.d("CONCATMAP", "call: " + Thread.currentThread().getName());
                                try {
                                    Thread.sleep(200);
                                    subscriber.onNext(integer + " ConcatMap");
                                    subscriber.onCompleted();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                    subscriber.onError(e);
                                }
                            }
                        }).subscribeOn(Schedulers.newThread());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        tv_log.append("onCompleted");

                    }

                    @Override
                    public void onError(Throwable e) {
                        tv_log.append("onError：" + e.getMessage() + "\n");

                    }

                    @Override
                    public void onNext(String s) {
                        Log.d("OnNext", "onNext: FlatMap " + s);
                        tv_log.append("onNext: " + s + "\n");
                    }
                });
    }

    /**
     * @param view
     */
    public void switchMap(View view) {
        tv_log.setText("");
        Integer[] integers = new Integer[]{0, 1, 2};
        Observable
                .from(integers)
                .switchMap(new Func1<Integer, Observable<String>>() {
                    @Override
                    public Observable<String> call(Integer integer) {
                        return Observable.just("SwitchMap:"+integer).subscribeOn(Schedulers.newThread());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        tv_log.append("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        tv_log.append("onError："+e.getMessage());
                    }

                    @Override
                    public void onNext(String s) {
                        tv_log.append("onNext："+s+"\n");
                    }
                });
    }
}
