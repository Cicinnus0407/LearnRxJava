package com.example.cicinnus.learnrxjava;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Cicinnus on 2017/1/4.
 */

public class OperatorActivity3 extends AppCompatActivity {

    private TextView tv_log;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operator3);
        tv_log = (TextView) findViewById(R.id.tv_log);
    }


    /**
     * 过滤方法
     *
     * @param view
     */
    public void filter(View view) {
        tv_log.setText("过滤0-9之间小于5的数,合并成集合\n");
        Observable
                .range(0, 10)
                .filter(new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer integer) {
                        return integer < 5;
                    }
                })
                .toList()
                .subscribe(new Subscriber<List<Integer>>() {
                    @Override
                    public void onCompleted() {
                        tv_log.append("onCompleted");

                    }

                    @Override
                    public void onError(Throwable e) {
                        tv_log.append("onError: " + e.getMessage());

                    }

                    @Override
                    public void onNext(List<Integer> integers) {
                        tv_log.append("onNext: " + integers + "\n");

                    }
                });
    }

    /**
     * 高级过滤方法
     * 也可以过滤自定义类型的type
     *
     * @param view
     */
    public void ofType(View view) {
        tv_log.setText("'0, \"zero\", 1, \"one\", 2, \"two\"\n过滤字符串和int,demo取字符串\n");
        Observable
                .just(0, "zero", 1, "one", 2, "two")
                .ofType(String.class)
                .toList()
                .subscribe(new Subscriber<List<String>>() {
                    @Override
                    public void onCompleted() {
                        tv_log.append("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        tv_log.append("onError: " + e.getMessage() + "\n");
                    }

                    @Override
                    public void onNext(List<String> strings) {
                        tv_log.append("onNext: " + strings + "\n");
                    }
                });
    }

    /**
     * 取发射的第一项数据
     *
     * @param view
     */
    public void first(View view) {
        tv_log.setText("5,6,7,8 取第一个大于5的数");
        Observable
                .just(5, 6, 7, 8)
                .first(new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer integer) {
                        return integer > 5;
                    }
                })
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        tv_log.append("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {

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
    public void firstOrDefault(View view) {
        tv_log.setText("5,6,7,8,9" + "取大于10的第一个数，没有则默认为10\n");
        Observable
                .just(5, 6, 7, 8, 9)
                .firstOrDefault(10, new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer integer) {
                        return integer > 10;
                    }
                })
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
     * 和first的区别在于如果没有符合条件的数据，会直接调用onCompleted，而first会抛出异常
     *
     * @param view
     */
    public void takeFirst(View view) {
        tv_log.setText("5,6" + "第一个比6大的数据\n");
        Observable
                .just(5, 6)
                .takeFirst(new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer integer) {
                        return integer > 6;
                    }
                })
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
     * 判断发射的数据是否大于1，是则调用onNext，否则执行onError
     *
     * @param view
     */
    public void single(View view) {
        tv_log.setText("数据[0,1]" + "判断发射的数据是否大于1\n");
        Observable
                .just(0, 1)
                .single()
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        tv_log.append("onCompleted");
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
    public void last(View view) {
        tv_log.setText("数据[5,6,7,8,9],获取最后一项大于5的数据\n");
        Observable
                .just(5, 6, 7, 8, 9)
                .last(new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer integer) {
                        return integer > 5;
                    }
                })
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
     * 跳过前几项数据
     *
     * @param view
     */
    public void skip(View view) {
        tv_log.setText("数据[0-10]\n跳过5项数据\n");
        Observable
                .range(0, 10)
                .skip(5)
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
     * 和skip相反
     *
     * @param view
     */
    public void skipLast(View view) {
        tv_log.setText("数据[0-10]\n跳过最后5项数据\n");
        Observable
                .range(0, 10)
                .skipLast(5)
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
     * 获取数据的一部分
     *
     * @param view
     */
    public void take(View view) {
        tv_log.setText("数据0-9获取其中的4项\n");
        Observable
                .range(0, 10)
                .take(4)
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
     * 过了一段指定的时间还没发射数据时才发射一个数据
     *
     * @param view
     */
    public void debounce(View view) {
        tv_log.setText("过了一段指定的时间还没发射数据时才发射一个数据");
        Observable
                .range(0, 10)
                .flatMap(new Func1<Integer, Observable<String>>() {
                    @Override
                    public Observable<String> call(Integer integer) {
                        try {
                            Thread.currentThread().sleep(200 * integer);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return Observable.just(String.format("%s", integer));
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .debounce(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        tv_log.append("onCompleted\n");
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
     * 过滤重复数据
     *
     * @param view
     */
    public void distinct(View view) {
        tv_log.setText("过滤重复数据：\"1\", \"2\", \"3\", \"3\", \"5\", \"6\"\n");
        Observable
                .just("1", "2", "3", "3", "5", "6")
                .distinct()
                .toList()
                .subscribe(new Subscriber<List<String>>() {
                    @Override
                    public void onCompleted() {
                        tv_log.append("onCompleted\n");

                    }

                    @Override
                    public void onError(Throwable e) {
                        tv_log.append("onError: " + e.getMessage());

                    }

                    @Override
                    public void onNext(List<String> strings) {
                        tv_log.append("onNext: " + strings + "\n");
                    }
                });
    }

    /**
     * 发射设置下标的数据
     * @param view
     */
    public void elementAt(View view) {
        tv_log.setText("数据0-9\n发射下标为5的数据（下标从0开始）\n");
        Observable
                .range(0,10)
                .elementAt(5)
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        tv_log.append("onCompleted\n");

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
     * 忽略数据,这个操作符效果就如同empty（）方法创建一个空的Observable,只会执行onCompleted()方法，
     * 不同的是ignoreElements是对数据源的处理，而empty（）是创建Observable。
     * @param view
     */
    public void ignoreElement(View view) {
        tv_log.setText("忽略所有数据,调用onCompleted方法\n");
        Observable
                .range(0,10)
                .ignoreElements()
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        tv_log.append("onCompleted\n");

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
