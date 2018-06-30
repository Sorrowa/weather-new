package com.example.zhangzihao.weather;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.widget.Toast.*;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, OnQueryTextListener {


    private double latitude = 0;
    private double longitude;
    private TextView city;
    private TextView min;
    private TextView max;
    private TextView tmp;
    private Toolbar toolbar;
    private Button button;
    //private Button forecast;
    private DrawerLayout drawerLayout;
    private ListView listView;
    private String[] cities;
    private String[] tip;
    private String[] tip_contents;//用于记录各种小贴士
    public AMapLocationClient mLocationClient = null;
    public AMapLocationClientOption mLocationOption = null;
    public String city_now = "北京";
    public OkHttpClient okHttpClient;
    public OkHttpClient okHttpClient_one;
    private SwipeRefreshLayout swipeRefreshLayout;
    public ImageView imageView;
    private TextView wind;
    private TextView winddir;
    private ListView tips;
    private TextView tip_content;
    private String city_you_want = null;
    private know_the_icon knowTheIcon;
    private SearchView msearchView;
    private ListAdapter madapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //di定位信息初始化
        mLocationClient = new AMapLocationClient(getApplicationContext());
        mLocationOption = new AMapLocationClientOption();
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Device_Sensors);
        mLocationOption.setOnceLocation(true);
        mLocationClient.setLocationOption(mLocationOption);
        mLocationClient.setLocationListener(mapLocationListener);
        mLocationClient.startLocation();

        //各个控件的初始化
        city = (TextView) findViewById(R.id.city);
        tmp = (TextView) findViewById(R.id.temperatrue);
        min = (TextView) findViewById(R.id.min);
        max = (TextView) findViewById(R.id.max);
        wind = (TextView) findViewById(R.id.wind);
        winddir = (TextView) findViewById(R.id.winddir);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        button = (Button) findViewById(R.id.list);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        listView = (ListView) findViewById(R.id.listview);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe);
        imageView = (ImageView) findViewById(R.id.imageView);
        tips = (ListView) findViewById(R.id.tips);
        tip_content = (TextView) findViewById(R.id.tip_content);
        msearchView = (SearchView) findViewById(R.id.searchView1);

        imageView.setImageResource(R.drawable.sun);


        //下拉控件的实现
        swipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.white);
        swipeRefreshLayout.setColorSchemeResources(R.color.red, R.color.blue, R.color.pink);//下拉的主题颜色


        //设置listview
        init_city();
        listView.setAdapter(madapter= new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, cities));
//        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, cities));
        init_tips();
        tips.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, tip));


        toolbar.setTitle(" ");

        //为drawerLayout设置开关，用来监听打开和关闭的事件
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open_drawer, R.string.close_drawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                swipeRefreshLayout.setEnabled(true);
                super.onDrawerClosed(drawerView);
                Log.i("mag", "关闭");
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                swipeRefreshLayout.setEnabled(false);
                super.onDrawerOpened(drawerView);
                Log.i("mag", "打开");

            }

        };
        //drawerLayout.setDrawerListener(toggle);

        Toolbar.OnMenuItemClickListener onMenuItemClickListener = new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return false;
            }
        };
        msearchView.setOnQueryTextListener(this);
        msearchView.setSubmitButtonEnabled(false);
        msearchView.setIconifiedByDefault(false);
        listView.setTextFilterEnabled(true);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });


//对于网络上的信息进行解析
        okHttpClient = new OkHttpClient();//用于测试//
        okHttpClient_one = new OkHttpClient();
        //latitude=120.343;
        //longitude=36.088;
        Request request = new Request.Builder()
                .url("https://free-api.heweather.com/s6/weather?location=" + latitude + "," + longitude + "&key=30a3a5d631cd44b5acfd0ce4a1f4c0da")
                .build();

        Request request_one = new Request.Builder()
                .url("https://free-api.heweather.com/s6/weather/lifestyle?location=" + latitude + "," + longitude + "&key=30a3a5d631cd44b5acfd0ce4a1f4c0da")
                .build();

        Call call = okHttpClient.newCall(request);
        Call call1 = okHttpClient_one.newCall(request_one);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Internet(response);
            }
        });
        call1.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Internet_one(response);
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name= (String) madapter.getItem(position);
                //makeText(MainActivity.this, "" + name, LENGTH_SHORT).show();

                Request request = new Request.Builder()
                        .url("https://free-api.heweather.com/s6/weather?location=" + name + "&key=30a3a5d631cd44b5acfd0ce4a1f4c0da")
                        .build();
                Request request_one = new Request.Builder()
                        .url("https://free-api.heweather.com/s6/weather/lifestyle?location=" + name + "&key=30a3a5d631cd44b5acfd0ce4a1f4c0da")
                        .build();

                city_now = name;//用于记录当前的名字

                Call call = okHttpClient.newCall(request);
                Call call1 = okHttpClient_one.newCall(request_one);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Internet(response);
                        drawerLayout.closeDrawer(GravityCompat.START);
                    }
                });
                call1.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Internet_one(response);
                    }
                });
            }


        });

        //设置下拉刷新的监听
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {


            @Override
            public void onRefresh() {
                Log.d("zzh", "city=" + city_now);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        String name = city_now;
                        Request request = new Request.Builder()
                                .url("https://free-api.heweather.com/s6/weather?location=" + name + "&key=30a3a5d631cd44b5acfd0ce4a1f4c0da")
                                .build();
                        Request request_one = new Request.Builder()
                                .url("https://free-api.heweather.com/s6/weather/lifestyle?location=" + name + "&key=30a3a5d631cd44b5acfd0ce4a1f4c0da")
                                .build();

                        city_now = name;//用于记录当前的名字

                        Call call = okHttpClient.newCall(request);
                        Call call1 = okHttpClient_one.newCall(request_one);
                        call.enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                e.printStackTrace();
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                Internet(response);
                            }
                        });
                        call1.enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                e.printStackTrace();
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                Internet_one(response);
                            }
                        });

                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 1000);
            }
        });

        tips.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (tip_contents != null) {
                    String content = tip_contents[(int) id];
                    tip_content.setText(content);//点击时设置内容
                } else {
                    makeText(MainActivity.this, "没有数据呢", LENGTH_SHORT).show();
                }
            }
        });



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }

    //    public void afterTextChanged(Editable editable){
//        String input=
//    }
    public void init_city() {
        cities = new String[]{
                "北京",
                "天津",
                "石家庄",
                "唐山",
                "秦皇岛",
                "邯郸",
                "邢台",
                "保定",
                "张家口",
                "承德",
                "沧州",
                "廊坊",
                "衡水",
                "太原",
                "大同",
                "阳泉",
                "长治",
                "晋城",
                "朔州",
                "呼和浩特",
                "包头",
                "乌海",
                "赤峰",
                "通辽",
                "沈阳",
                "大连",
                "鞍山",
                "抚顺",
                "本溪",
                "丹东",
                "锦州",
                "营口",
                "阜新",
                "辽阳",
                "盘锦",
                "铁岭",
                "朝阳",
                "葫芦岛",
                "长春",
                "吉林",
                "四平",
                "辽源",
                "通化",
                "白山",
                "松原",
                "白城",
                "哈尔滨",
                "齐齐哈尔",
                "鸡西",
                "鹤岗",
                "双鸭山",
                "大庆",
                "伊春",
                "佳木斯",
                "七台河",
                "牡丹江",
                "黑河",
                "南京",
                "无锡",
                "徐州",
                "常州",
                "苏州",
                "南通",
                "连云港",
                "淮阴",
                "盐城",
                "扬州",
                "镇江",
                "泰州",
                "宿迁",
                "杭州",
                "宁波",
                "温州",
                "嘉兴",
                "湖州",
                "绍兴",
                "金华",
                "衢州",
                "舟山",
                "台州",
                "合肥",
                "芜湖",
                "蚌埠",
                "淮南",
                "马鞍山",
                "淮北",
                "铜陵",
                "安庆",
                "黄山",
                "滁州",
                "阜阳",
                "宿州",
                "巢湖",
                "六安",
                "福州",
                "厦门",
                "莆田",
                "三明",
                "泉州",
                "漳州",
                "南平",
                "龙岩",
                "南昌",
                "景德镇",
                "萍乡",
                "九江",
                "新余",
                "鹰潭",
                "赣州",
                "济南",
                "青岛",
                "淄博",
                "枣庄",
                "东营",
                "烟台",
                "潍坊",
                "济宁",
                "泰安",
                "威海",
                "日照",
                "莱芜",
                "临沂",
                "德州",
                "聊城",
                "菏泽",
                "郑州",
                "开封",
                "洛阳",
                "平顶山",
                "安阳",
                "鹤壁",
                "新乡",
                "焦作",
                "濮阳",
                "许昌",
                "漯河",
                "三门峡",
                "南阳",
                "商丘",
                "信阳",
                "武汉",
                "黄石",
                "十堰",
                "宜昌",
                "襄樊",
                "鄂州",
                "荆门",
                "孝感",
                "荆州",
                "黄冈",
                "咸宁",
                "长沙",
                "株洲",
                "湘潭",
                "衡阳",
                "邵阳",
                "岳阳",
                "常德",
                "张家界",
                "益阳",
                "郴州",
                "永州",
                "怀化",
                "娄底",
                "广州",
                "韶关",
                "深圳",
                "珠海",
                "汕头",
                "佛山",
                "江门",
                "湛江",
                "茂名",
                "肇庆",
                "惠州",
                "梅州",
                "汕尾",
                "河源",
                "阳江",
                "清远",
                "东莞",
                "中山",
                "潮州",
                "揭阳",
                "云浮",
                "南宁",
                "柳州",
                "桂林",
                "梧州",
                "北海",
                "防城港",
                "钦州",
                "贵港	",
                "玉林",
                "海口",
                "三亚",
                "重庆",
                "成都",
                "自贡",
                "攀枝花",
                "泸州",
                "德阳",
                "绵阳",
                "广元",
                "遂宁",
                "内江",
                "乐山",
                "南充",
                "宜宾",
                "广安",
                "达州",
                "贵阳",
                "六盘水",
                "遵义",
                "昆明",
                "曲靖",
                "玉溪",
                "西安",
                "铜川",
                "宝鸡",
                "咸阳",
                "渭南",
                "延安",
                "汉中",
                "榆林",
                "兰州",
                "嘉峪关",
                "金昌",
                "白银",
                "天水",
                "西宁",
                "银川",
                "石嘴山",
                "吴忠",
                "乌鲁木齐",
                "克拉玛依",
        };

    }


    AMapLocationListener mapLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
//可在其中解析amapLocation获取相应内容。
                } else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:"
                            + amapLocation.getErrorCode() + ", errInfo:"
                            + amapLocation.getErrorInfo());
                }
            }

            latitude = amapLocation.getLatitude();//获取纬度
            longitude = amapLocation.getLongitude();//获取经度
            Log.d("zzh", "纬度=" + Double.toString(latitude));
        }
    };

    public void Internet(Response response) {
        try {
            //Log.d("zzh", response.body().string());只能有效调用一次
            JSONObject HeWeather = new JSONObject(response.body().string());
            JSONArray HeWeather6 = HeWeather.getJSONArray("HeWeather6");
            JSONObject one = HeWeather6.getJSONObject(0);
            JSONObject basic = one.getJSONObject("basic");
            JSONObject now = one.getJSONObject("now");
            JSONArray daily_forecast = one.getJSONArray("daily_forecast");
            JSONObject one_day = daily_forecast.getJSONObject(0);


            String tmp_now = now.getString("tmp");//模版位置
            String txt = now.getString("cond_txt");
            String tmp_max = one_day.getString("tmp_max");
            String tmp_min = one_day.getString("tmp_min");
            String city_name = basic.getString("location");
            String wind_sc = now.getString("wind_sc");
            String wind_dir = now.getString("wind_dir");


            city_now = city_name;

            city.setText(city_name);
            max.setText("最高温度为：" + tmp_max + "摄氏度");
            min.setText("最低温度为：" + tmp_min + "摄氏度");
            tmp.setText(tmp_now + "°C");
            wind.setText("风力：" + wind_sc);
            winddir.setText("风向：" + wind_dir);
            Log.d("zzh", "天气为" + txt);
            knowTheIcon = new know_the_icon(imageView, txt);
            knowTheIcon.show_me_answer();//设置图标

            new Thread() {
                public void run() {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            //更新UI
                            knowTheIcon.show_me_answer();//设置图标
                        }

                    });
                }
            }.start();


            //init_tips(tip);
            //tips.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,tip));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void Internet_one(Response response) {
        try {
            //Log.d("zzh", response.body().string());
            JSONObject HeWeather = new JSONObject(response.body().string());
            JSONArray HeWeather6 = HeWeather.getJSONArray("HeWeather6");
            JSONObject one = HeWeather6.getJSONObject(0);
            JSONArray life = one.getJSONArray("lifestyle");
            JSONObject comf = life.getJSONObject(0);
            JSONObject drsg = life.getJSONObject(1);
            JSONObject sport = life.getJSONObject(3);
            JSONObject trav = life.getJSONObject(4);
            JSONObject uv = life.getJSONObject(5);
            JSONObject cw = life.getJSONObject(6);
            JSONObject air = life.getJSONObject(7);
            JSONObject flu = life.getJSONObject(2);

            String comfs = comf.getString("txt");
            String drsgs = drsg.getString("txt");
            String flus = flu.getString("txt");
            String sports = sport.getString("txt");
            String travs = trav.getString("txt");
            String uvs = uv.getString("txt");
            String cws = cw.getString("txt");
            String airs = air.getString("txt");
            tip_contents = new String[]{
                    comfs, drsgs, flus, sports, travs, uvs, cws, airs
            };
            tip_content.setText(comfs);


            //init_tips(tip);
            //tips.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,tip));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void init_tips() {
        tip = new String[]{
                "舒适度指数",
                "洗车指数",
                "穿衣指数",
                "感冒指数",
                "运动指数",
                "旅游指数",
                "紫外线指数",
                "空气污染扩散条件指数",
        };
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        if (s == null || s.length() == 0) {
            listView.clearTextFilter();
            listView.setAdapter(madapter= new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, cities));
        } else {
            listView.setFilterText(s);
        }
       return true;
    }
}



