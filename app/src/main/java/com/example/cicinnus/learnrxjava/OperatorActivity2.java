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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.observables.GroupedObservable;
import rx.schedulers.Schedulers;


/**
 * Created by Cicinnus on 2017/1/4.
 */

public class OperatorActivity2 extends AppCompatActivity {

    private TextView tv_log;
    private LinearLayout ll_map;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operator2);
        tv_log = (TextView) findViewById(R.id.tv_log);
        ll_map = (LinearLayout) findViewById(R.id.ll_map);

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
                        ImageView imageView = new ImageView(OperatorActivity2.this);
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
        tv_log.setText("并发多个observer");
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
        tv_log.setText("顺序执行Observer");
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
     * 当原始Observable发射一个新的数据（Observable）时，
     * 它将取消订阅并停止监视产生执之前那个数据的Observable，只监视当前这一个.
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
                        Log.d("SWITCHMAP", "call: "+integer);
                        return Observable.just("SwitchMap:"+integer*10).subscribeOn(Schedulers.newThread());
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


    /**
     * 分类
     * 从0-9之间将小于5和大于等于5的数分两组
     * booleanIntegerGroupedObservable变量有一个getKey（）方法,该方法返回的是分组的key，
     * 他的值就是groupBy方法call回调所用函数的值，也就是integer <5的值，true和false。有几个分组也是有此值决定的。
     * @param view
     */
    public void groupBY(View view) {
        tv_log.setText("从0-9之间将小于5和大于等于5的数分两组\n");
        Observable
                .range(0, 10)
                .groupBy(new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer integer) {

                        return integer < 5;

                    }
                })
                .subscribe(new Subscriber<GroupedObservable<Boolean, Integer>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(final GroupedObservable<Boolean, Integer> booleanIntegerGroupedObservable) {

                        booleanIntegerGroupedObservable
                                .toList()
                                .subscribe(new Subscriber<List<Integer>>() {
                                    @Override
                                    public void onCompleted() {
                                        tv_log.append("onCompleted" + "\n");
                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }

                                    @Override
                                    public void onNext(List<Integer> integers) {
                                        Log.d("KEY", "onNext: "+booleanIntegerGroupedObservable.getKey());
                                        tv_log.append("onNext: " + integers + "\n");
                                    }
                                });
                    }
                });
    }

    /**
     * 操作符对原始Observable发射的第一项数据应用一个函数，然后将那个函数的结果作为自己的第一项数据发射。
     * 它将函数的结果同第二项数据一起填充给这个函数来产生它自己的第二项数据。它持续进行这个过程来产生剩余的数据序列。
     * 例如计算1+2+3+4+5的和
     * scan有一个重载方法，可以设置一个初始值，只需将scan加个参数scan（Integer num，new Func2）。
     * @param view
     */
    public void scan(View view) {
        tv_log.setText("1-5之间每一项与前一项的叠加\n");
        Observable
                .range(1,5)
                .scan(new Func2<Integer, Integer, Integer>() {
                    @Override
                    public Integer call(Integer integer, Integer integer2) {
                        return integer+integer2;
                    }
                })
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        tv_log.append("onNext: "+integer+"\n");
                    }
                });
    }

    /**
     * buffer
     * 操作符将一个Observable变换为另一个，原来的Observable正常发射数据，变换产生的Observable发射这些数据的缓存集合，
     * 如果原来的Observable发射了一个onError通知，Buffer会立即传递这个通知，而不是首先发射缓存的数据，即使在这之前缓存中包含了原始Observable发射的数据。
     * @param view
     */
    public void buffer(View view) {
        tv_log.setText("0-9之间分组，一组2个数，每组之间间隔3\n");
        Observable
                .range(0,10)
                .buffer(2,3)
                .subscribe(new Subscriber<List<Integer>>() {
                    @Override
                    public void onCompleted() {
                        tv_log.append("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        tv_log.append("onError: "+e.getMessage());
                    }

                    @Override
                    public void onNext(List<Integer> integers) {
                        tv_log.append("onNext: "+integers+"\n");
                    }
                });
    }

    /**
     *Window和Buffer类似，但不是发射来自原始Observable的数据包，它发射的是Observables，
     * 这些Observables中的每一个都发射原始Observable数据的一个子集，最后发射一个onCompleted通知。
     * @param view
     */
    public void window(View view) {
        tv_log.setText("0-9之间的数据分别输出，每组数据2个，间隔为3\n");
       Observable
               .range(0,10)
               .window(2,3)
               .subscribe(new Subscriber<Observable<Integer>>() {
                   @Override
                   public void onCompleted() {
                       tv_log.append("onCompleted1\n");
                   }

                   @Override
                   public void onError(Throwable e) {

                   }

                   @Override
                   public void onNext(Observable<Integer> integerObservable) {
                        integerObservable
                                .subscribe(new Subscriber<Integer>() {
                                    @Override
                                    public void onCompleted() {
                                        tv_log.append("onCompleted2\n");
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        tv_log.append("onError: "+e.getMessage());
                                    }

                                    @Override
                                    public void onNext(Integer integer) {
                                        tv_log.append("onNext2："+integer+"\n");
                                    }
                                });
                   }
               });
    }
}
