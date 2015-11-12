package com.dingding.ddframe.ddbase.net;

/**
 * Created by zzz on 15-1-16.
 */
public class MesType {


    public static final String WX_APP_ID = "wx204d11209ee1ffea";
    /**
     * COMMON 包下0x00001...开头。
     */
    public static final int GETCITYLIST = 0x00001021; // 城市列表
    public static final int SEARCHCONDITION = 0x000010016; // 通过城市编码获取相关查询信息
    public static final int BASESYSINFO = 0x000010036; // 获取基础数据。 用来初始化数据
    public static final int APPVERSION = 0x000010026; // 获取app版本
    public static final int TRYGETIMEI = 0x000010027; // 获取app版本

    /**
     * USER包2开头
     */
    public static final int CREATEAPPID = 0x00002001; // 生成appId
    public static final int LOGIN = 0x00002002; // 登陆
    public static final int LOGINOUT = 0x00002003; // 登出
    public static final int CUSTOMERLIST = 0x00002004; // 获取客户列表
    public static final int ADDCUSTOMER = 0x00002005; // 增加客户信息
    public static final int MEMBERLIST = 0x00002006; // 查看组员
    public static final int RESETPWD = 0x00002007; // 重设密码
    public static final int HEADMOBILE = 0x00002008; // 重设头像或者手机号
    public static final int CUSTOMERINFO = 0x00002009; // 重设头像或者手机号
    public static final int GETMEMBERLIST = 0x000020010; // 组长获取组员信息
    public static final int SEARCHORDER = 0x000020011; // 经纪人通过手机号搜素订单列表
    public static final int CUSTOMERDETAIL = 0x000020012;// 客户评价
    public static final int LOGINAGENTBYTOKEN = 0x000020013; // 通过token串登陆
    public static final int USERAPICONTROLLER = 0x000020014; // 给业主打电话校验 
    public static final int ADDRESSISSEE = 0x000020015; // 校验地址是否在本经纪人商圈

    /**
     * HOUSE包3开头
     */
    public static final int HOUSEISEXIST = 0x00005010; // 核查房源是否存在 去掉了。
    public static final int ENTERHOUSE = 0x00005002; // 录入房源
    public static final int REDUCEPRICE = 0x000030001; // 修改房源价格
    public static final int MODIFYCOVER = 0x00001025; // 房源设置封面
    public static final int SYNHOUSEINFO = 0x000030014; // 小区，楼栋等信息获取楼盘信息(同步字典)
    public static final int ADDREMARKS = 0x00001014; // 添加房源备注
    public static final int ADDHOUSEPIC = 0x00001015; // 上传房源图片
    public static final int DIALOGVERIFI = 0x000020012;// 核销房源
    public static final int GETHIUSEREMARK = 0x000030013;// 获取房源备注
    public static final int ADDORDERREMARKS = 0x00001016; // 添加订单备注
    public static final int ADDHOUSEREMARKS = 0x00001017; // 添加房源备注
    public static final int GETHOUSEPICLIST = 0x00001018; // 编辑图片用到的
    public static final int MODIFYISHASKEY = 0x00001018; // 修改是否有钥匙

    public static final int ADDREMARKTAGS = 0x00001019; // 对房源的标签信息和备注信息进行保存
    public static final int GETTODAYDIALCOUNT = 0x00001020; // 对房源的标签信息和备注信息进行保存
    public static final int CHANGECANINDATE = 0x00001021; // 修改房源可入住日期

    public static final int QUERYTRANSFERENTRUSTBYPAGE = 0x00001022; // 乐播房源查询
    public static final int QUERYSURVEYIMAGEBYPRODUCTID = 0x00001024; // 实勘
    public static final int QUERYWAITMANVERIFYLIST = 0x00001040;  //查询经纪人待核查房源接口
    /**
     * ORDER包4开头
     */
    public static final int CREATEORDER = 0x000040003; // 新建订单
    public static final int ADDORDERITEM = 0x000040006; // 生成子订单
    public static final int CANCLEORDER = 0x000040004; // 修改订单状态 1 取消 2签到 3 确认
    public static final int WAITDISPOSEORDER = 0x000040002; // 获取待处理订单界面
    public static final int MODIFYORDERTIME = 0x000040001; // 修改订单时间
    public static final int ALLOTORDER = 0x000020100; // 分配订单
    public static final int OKNETDATE = 0x000020008; // 组员确认订单
    public static final int DELETEORDERITEM = 0x000020008; // 时间不匹配(删除子订单)
    public static final int WAITDISPOSEORDERDETIL = 0x000020002; // 获取待处理订单界面详情
    public static final int SIGN = 0x000040010; // 签约
    public static final int MODIFYAPPOINTLOCATION = 0x000040012; // 修改约看地点
    public static final int ORDERCOMMENT = 0x000040013; // 修改约看地点

    public static final int MAKEPAY = 0x000040112; // 修改约看地点
    public static final int QUERYAPPOINTINFO = 0x000040113; // 修改约看地点
    public static final int GETPERFORMDETAILS = 0x000040114; // 按照绩效月查询经纪人的绩效明细房源列表

    /**
     * SEARCH包5开头
     */
    public static final int SEARCHHOUSELIST = 0x00005001; // 搜索房源
    public static final int ADDHOUSELIST = 0x00001023; // 搜索房源   下拉加载

    public static final int GETHOUSEINFO = 0x00005014; // 获取房源详情信息
    public static final int MINERESULT = 0x00001009; // 我的查看业绩
    public static final int PLOTINFO = 0x00001011; // 根据城市id获取城市下小区列表


    /**
     * 合同相关
     */

    public static final int GETCONTRACTINFO = 0x00006001;     //获取合同信息
    public static final int FILLCONTRACTSTEP1 = 0x00006002;        //补全合同第一步
    public static final int FILLCONTRACTSTEP2 = 0x00006003;        //补全合同第二步
    public static final int FILLCONTRACTSTEP3 = 0x00006004;        //补全合同第三步
    public static final int FILLCONTRACTSTEP4 = 0x00006005;        //补全合同第四步
    public static final int FILLCONTRACTSTEP5 = 0x00006006;        //补全合同第五步
    public static final int FILLCONTRACTSTEP6 = 0x00006005;        //补全合同第六步
    public static final int SINGSENDSMS = 0x000060009;        //补全合同验证码

    /**
     * 文件上传
     */
    public static final int UPLOADFILE = 0x00007001;        //补全合同第四步


//    public static boolean WAITDISPOSEF = false; // 待处理fragment中 用来区分是否需要请求网络
//    public static boolean WAITLOOKF = false;    // 待看
//    public static boolean SIGNEDFRAGMENT = false;    // 待签约中，刷新专用
    public static boolean WAITCUSTOMER = false;


    public static boolean SHOWREDCIRCLE1 = false;
    public static boolean SHOWREDCIRCLE2 = false;


    /**
     *合同补录
     */
    public static final int ADDCUSANDORDER = 0x00007001; //添加用户和订单
    public static final int QUERYHOSUE = 0x00007002; //根据房屋编号查找房源
    public static final int ADDORDERITEM2 = 0x00007003; //添加子订单
    public static final int ADDCONTRACT = 0x00007004; //添加合同
    public static final int CONTRACTFIR = 0x00007005; //房源及业主信息提交接口1
    public static final int ENTERCONTRACTSEC = 0x00007006; //用途租期交房时间等信息的提交2
    public static final int ENTERCONTRACTTHI = 0x00007007; //租金、费用信息提交接口3
    public static final int ENTERCONTRACTFOU = 0x00007008; //维修、违约、物业交割提交4
    public static final int ENTERCONTRACTFIF = 0x00007009; //上传备份资料接口方法5
    public static final int ADDZIROOMCONTRACT = 0x00007010; //自如来源生成合同
    public static final int ENTERZRCONTRACTFIR = 0x00007011; //自如来源录入合同第一步
    public static final int ENTERZRCONTRACTSEC = 0x00007012; //自如来源录入合同第二步
    public static final int QUERYHOSUELISTBYHOUSENUM = 0x00007012; //根据商品ID查找房源详情
    public static final int CONTINUEMAKEUP4DD = 0x00007013;
    public static final int CONTINUEMAKEUP4ZR = 0x00007014;

    /**
     *京东白条
     */
    public static final int GETJDWHITEAGREEMENTSTATE = 0x00010001; //10.1获取京东白条签约状态
    public static final int CREATEJDPAYAGREEMENT = 0x00010002;     //10.2 创建京东支付协议申请
    public static final int SIGNJDPAYAGREEMENT = 0x00010004;       //10.4 签字确定京东支付协议申请
    public static final int CANCELJDPAYAGREEMENT = 0x00010005;     //10.5 取消京东白条申请
    public static final int GETJDWHITESAVECONTACTINFO = 0x00010006;     //保存京东白条第一步数据


}
