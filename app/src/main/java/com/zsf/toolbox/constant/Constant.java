package com.zsf.toolbox.constant;

/**
 * Created by EWorld
 * 2022/5/27
 */
public class Constant {
    public static final String KEY = "cff6a8928d2c5adcf376556105031396";

    public static final String API_BASE_JVHE_URL = "http://op.juhe.cn/onebox/";
    //常用汇率查询
    public static final String API_COMMON_EXCHANGE_RATE = "http://op.juhe.cn/onebox/exchange/query?key=" + KEY;
    //货币列表
    public static final String API_CURRENCY = "http://op.juhe.cn/onebox/exchange/list?key=" + KEY;
    //实时货币汇率查询换算 http://op.juhe.cn/onebox/exchange/currency?key=cff6a8928d2c5adcf376556105031396&from=JPY&to=BHD
    public static final String API_CURRENCY_EXCHANGE_ = "http://op.juhe.cn/onebox/exchange/currency?key=" + KEY;

    //一语言录
    public static final String API_ONE_WORD = "https://api.ixiaowai.cn/";
    //添狗日记 https://api.ixiaowai.cn/tgrj/index.php
    public static final String API_SIMP = "https://api.ixiaowai.cn/";
    //毒鸡汤
    public static final String API_ANTI_MOTIVATIONAL_QUOTES = " http://api.lkblog.net/";
    //随机狗狗图 https://dog.ceo/api/breeds/image/random
    public static final String API_RANDOM_DOG_IMG = "https://dog.ceo/api/";
    //IP地址 https://api.ipify.org?format=json
    public static final String API_IP_ADDRESS = "https://api.ipify.org";
    //随机一只猫  https://api.thecatapi.com/v1/images/search
    public static final String API_CAT_IMG = "https://api.thecatapi.com/v1/images/search";
    //淘宝买家秀图 https://api.ghser.com/tao
    public static final String API_TAO_BAO_IMG = "https://api.ghser.com";
    //随机二次元图
    public static final String API_COMIC_IMG = "https://api.ghser.com";

    //https://learnku.com/articles/30329 持续更新免费的 API，做一个 API 的搬运工——终身维护

    //随机美女
    //http://img.btu.pp.ua/random/api.php?type=img
    //#参数mode={1,2}，默认1，2是IG图包 1是图包1
    //http://img.btu.pp.ua/random/api.php?mode=2

    //播放抖音搞笑短视频 https://v.api.aa1.cn/api/api-video-gaoxiao/index.php?aa1=rf7y8w5yf79u8hwf4

    //******************************************************************************************START
    //https://likepoems.com/articles/106 信息来自
    //https://zhuanlan.zhihu.com/p/336053929 信息来自
    //https://api.isoyu.com/bing_images.php
    public static final String API_BING_DAILY = "https://api.isoyu.com";
    //https://api.dujin.org/bing/m.php
    public static final String API_BING_DAILY_2 = "https://api.dujin.org";
    //网红专栏壁纸 https://api.isoyu.com/beibei_images.php
    public static final String API_INTERNET_CELEBRITY_PIC = "https://api.isoyu.com";
    //美女图片  https://api.isoyu.com/mm_images.php
    //public static final String API_BEAUTY_GIRL = "https://api.isoyu.com";
    // https://cdn.seovx.com/?mom=302
    //https://cdn.seovx.com/ha/?mom=302
    public static final String API_BEAUTY_GIRL = "https://cdn.seovx.com/";

    //******************************************************************************************END

    //垃圾分类 https://www.sfwind.xyz/AppPrivacy/GarbageClassification.json
    public static final String API_GARBAGE_CLASSIFICATION = "https://www.sfwind.xyz/";
}
