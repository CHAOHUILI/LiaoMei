package com.vidmt.lmei.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.google.gson.jpush.s;

import android.hardware.Camera.Area;

public class Persion implements Serializable{
	private Integer id;//编号
	private String tel;//电话
	private String pwd;//密码
	private String nick_name;//昵称
	private Integer sex;//性别，1.男   2.女
	private String birthday;//生日
	private Integer age;//生日
	private Integer level;//级别
	private Integer token;//爱意
	private String qq;//QQ号
	private String area;//城市
	private String photo;//头像
	private String photo_ident;//图片认证
	private String voice_ident;//语音认证
	private String video;//视频地址
	private String sup_ability;//超能力
	private String sign;//个人简介
	private Integer love;//爱心点数
	private Integer praise;//获赞数量
	//状态4-申请照片认证，5-照片认证通过，6-照片认证不通过
	private Integer ident_state;
	private Integer use_state;//用户状态，1.启用   2.禁用
	private Integer voice_state;//语音状态，1.开启   2.不开启
	private Integer video_state;//视频状态，1.开启   2.不开启
	private String create_date;//创建时间
	private String freeze_time;//解封时间
	private String album;//相册
	private Double rate;//回复成功率
	private Integer charm;//魅力值
	private String login_time;//登录时间
	private String third_login;//第三方登录号
    private Integer video_money;//视频多少钱一分钟
    private Integer voice_money;//声音多少钱一分钟
    private Integer height;//身高
    private String insterest;//("_"分隔)
    private String occupation;//职业
    private Integer sms_money;//普通聊天价格
    private Integer walk_state;//1-同意领走，2-不同意领走
    private String thumbnails;//头像缩略图
    
    private Integer sms_state;//IM聊天状态    1聊天收费   2聊天不收费
    private Integer login_give_money;//1-未赠送，2-已赠送
    private Double s_position_x;//经度
    private Double s_position_y;//纬度
    private Float distance;//距离
    
    private String paste_flag;
    
    private Integer voice_time_length;//认证语音长度，秒
    private Integer wealth;//财富等级
    private Integer fortune;//财富值
    
    private Double capitalBalance;//资金帐户
    
//    private String r_account;//容联云子账号
//    private String r_pwd;//容联云密码
    
    private Integer isLike;//是否点赞
    private Integer isBlack;//是否加入黑名单

    private String ident_pass_datetime;//认证通过时间
    
    private String englishSorting ;
    
    private Integer yet_video;//0 从没有认证过 1已经认证过视频 （即不会再次提示送金币字样）
    
    private Integer yet_photo;//0 从没有认证过 1 已经认证过照片（即不会再次提示送金币字样）
    public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getNick_name() {
		return nick_name;
	}

	public void setNick_name(String nick_name) {
		this.nick_name = nick_name;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getToken() {
		return token;
	}

	public void setToken(Integer token) {
		this.token = token;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getPhoto_ident() {
		return photo_ident;
	}

	public void setPhoto_ident(String photo_ident) {
		this.photo_ident = photo_ident;
	}

	public String getVoice_ident() {
		return voice_ident;
	}

	public void setVoice_ident(String voice_ident) {
		this.voice_ident = voice_ident;
	}

	public String getVideo() {
		return video;
	}

	public void setVideo(String video) {
		this.video = video;
	}

	public String getSup_ability() {
		return sup_ability;
	}

	public void setSup_ability(String sup_ability) {
		this.sup_ability = sup_ability;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public Integer getLove() {
		return love;
	}

	public void setLove(Integer love) {
		this.love = love;
	}

	public Integer getPraise() {
		return praise;
	}

	public void setPraise(Integer praise) {
		this.praise = praise;
	}

	public Integer getIdent_state() {
		return ident_state;
	}

	public void setIdent_state(Integer ident_state) {
		this.ident_state = ident_state;
	}

	public Integer getUse_state() {
		return use_state;
	}

	public void setUse_state(Integer use_state) {
		this.use_state = use_state;
	}

	public Integer getVoice_state() {
		return voice_state;
	}

	public void setVoice_state(Integer voice_state) {
		this.voice_state = voice_state;
	}

	public Integer getVideo_state() {
		return video_state;
	}

	public void setVideo_state(Integer video_state) {
		this.video_state = video_state;
	}

	public String getCreate_date() {
		return create_date;
	}

	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}

	public String getFreeze_time() {
		return freeze_time;
	}

	public void setFreeze_time(String freeze_time) {
		this.freeze_time = freeze_time;
	}

	public String getAlbum() {
		return album;
	}

	public void setAlbum(String album) {
		this.album = album;
	}

	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	public Integer getCharm() {
		return charm;
	}

	public void setCharm(Integer charm) {
		this.charm = charm;
	}

	public String getLogin_time() {
		return login_time;
	}

	public void setLogin_time(String login_time) {
		this.login_time = login_time;
	}

	public String getThird_login() {
		return third_login;
	}

	public void setThird_login(String third_login) {
		this.third_login = third_login;
	}

	public Integer getVideo_money() {
		return video_money;
	}

	public void setVideo_money(Integer video_money) {
		this.video_money = video_money;
	}

	public Integer getVoice_money() {
		return voice_money;
	}

	public void setVoice_money(Integer voice_money) {
		this.voice_money = voice_money;
	}



	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public String getInsterest() {
		return insterest;
	}

	public void setInsterest(String insterest) {
		this.insterest = insterest;
	}

	public String getOccupation() {
		return occupation;
	}

	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}

	public Integer getSms_money() {
		return sms_money;
	}

	public void setSms_money(Integer sms_money) {
		this.sms_money = sms_money;
	}

	public Integer getWalk_state() {
		return walk_state;
	}

	public void setWalk_state(Integer walk_state) {
		this.walk_state = walk_state;
	}

	public String getThumbnails() {
		return thumbnails;
	}

	public void setThumbnails(String thumbnails) {
		this.thumbnails = thumbnails;
	}

	public Integer getSms_state() {
		return sms_state;
	}

	public void setSms_state(Integer sms_state) {
		this.sms_state = sms_state;
	}

	public Integer getLogin_give_money() {
		return login_give_money;
	}

	public void setLogin_give_money(Integer login_give_money) {
		this.login_give_money = login_give_money;
	}

	public Double getS_position_x() {
		return s_position_x;
	}

	public void setS_position_x(Double s_position_x) {
		this.s_position_x = s_position_x;
	}

	public Double getS_position_y() {
		return s_position_y;
	}

	public void setS_position_y(Double s_position_y) {
		this.s_position_y = s_position_y;
	}

	public Float getDistance() {
		return distance;
	}

	public void setDistance(Float distance) {
		this.distance = distance;
	}

	public String getPaste_flag() {
		return paste_flag;
	}

	public void setPaste_flag(String paste_flag) {
		this.paste_flag = paste_flag;
	}

	public Integer getVoice_time_length() {
		return voice_time_length;
	}

	public void setVoice_time_length(Integer voice_time_length) {
		this.voice_time_length = voice_time_length;
	}

	public Integer getWealth() {
		return wealth;
	}

	public void setWealth(Integer wealth) {
		this.wealth = wealth;
	}

	public Integer getFortune() {
		return fortune;
	}

	public void setFortune(Integer fortune) {
		this.fortune = fortune;
	}

	public Double getCapitalBalance() {
		return capitalBalance;
	}

	public void setCapitalBalance(Double capitalBalance) {
		this.capitalBalance = capitalBalance;
	}

	public Integer getIsLike() {
		return isLike;
	}

	public void setIsLike(Integer isLike) {
		this.isLike = isLike;
	}

	public Integer getIsBlack() {
		return isBlack;
	}

	public void setIsBlack(Integer isBlack) {
		this.isBlack = isBlack;
	}

	public String getIdent_pass_datetime() {
		return ident_pass_datetime;
	}

	public void setIdent_pass_datetime(String ident_pass_datetime) {
		this.ident_pass_datetime = ident_pass_datetime;
	}

	public int getRanking() {
		return ranking;
	}

	public void setRanking(int ranking) {
		this.ranking = ranking;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public Area getArea1() {
		return area1;
	}

	public void setArea1(Area area1) {
		this.area1 = area1;
	}

	public String getA_name() {
		return a_name;
	}

	public void setA_name(String a_name) {
		this.a_name = a_name;
	}

	public Recommended getRecommended() {
		return recommended;
	}

	public void setRecommended(Recommended recommended) {
		this.recommended = recommended;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getRongyuntoken() {
		return rongyuntoken;
	}

	public void setRongyuntoken(String rongyuntoken) {
		this.rongyuntoken = rongyuntoken;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public BigDecimal getInvite_money() {
		return invite_money;
	}

	public void setInvite_money(BigDecimal invite_money) {
		this.invite_money = invite_money;
	}

	public String getInvite_url() {
		return invite_url;
	}

	public void setInvite_url(String invite_url) {
		this.invite_url = invite_url;
	}

	public int getLongtime() {
		return longtime;
	}

	public void setLongtime(int longtime) {
		this.longtime = longtime;
	}

	public BalanceOfPayments getBalanceOfPayments() {
		return balanceOfPayments;
	}

	public void setBalanceOfPayments(BalanceOfPayments balanceOfPayments) {
		this.balanceOfPayments = balanceOfPayments;
	}

	public String getSelf_value() {
		return self_value;
	}

	public void setSelf_value(String self_value) {
		this.self_value = self_value;
	}

	public Integer getReg_type() {
		return reg_type;
	}

	public void setReg_type(Integer reg_type) {
		this.reg_type = reg_type;
	}

	public Integer getOtherkey() {
		return otherkey;
	}

	public void setOtherkey(Integer otherkey) {
		this.otherkey = otherkey;
	}

	public Integer getVideo_ident() {
		return video_ident;
	}

	public void setVideo_ident(Integer video_ident) {
		this.video_ident = video_ident;
	}

	public Integer getLevel_version() {
		return level_version;
	}

	public void setLevel_version(Integer level_version) {
		this.level_version = level_version;
	}

	public Integer getStar() {
		return star;
	}

	public void setStar(Integer star) {
		this.star = star;
	}

	public String getEnglishSorting() {
		return englishSorting;
	}

	public void setEnglishSorting(String englishSorting) {
		this.englishSorting = englishSorting;
	}

	public Integer getYet_video() {
		return yet_video;
	}

	public void setYet_video(Integer yet_video) {
		this.yet_video = yet_video;
	}

	public Integer getYet_photo() {
		return yet_photo;
	}

	public void setYet_photo(Integer yet_photo) {
		this.yet_photo = yet_photo;
	}

	private int ranking;//排名
    
    private String cityId;//城市编号
    private Area area1;//区域列表
    private String a_name;//区域名称
    
    private Recommended recommended;//推荐表
    
    private String ip;//用户IP
    
	private String rongyuntoken;//融云TOKEN
	
	private int version;//乐观锁字段 对前端没有作用
	
	private BigDecimal invite_money;//邀请所得奖励（rmb）
	
	private String invite_url;//邀请好友的专属url

	private int longtime;//聊天时长
	
	private BalanceOfPayments balanceOfPayments;
	
	private String self_value;//我的排名附属字段
	
	private Integer reg_type;//1为安卓   2为iOS
	
	private Integer otherkey;//用于App端显示的ID号
	
	private Integer video_ident;//视频认证  1 未申请  2申请中  3已通过 4被拒   视频认证
	
	private Integer level_version;//用于更新等级的乐观锁
	
	private Integer star;//用户的友好度 （1-5星）
	
}